package com.opensooq.mobileApp.data.repositories

import android.content.Context
import android.util.Log
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Metadata
import com.opensooq.mobileApp.utils.JsonUtils
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import java.security.MessageDigest

/**
 * Repository class responsible for managing and updating categories, search flows, field labels, and options.
 * This class interacts with the Realm database and handles the caching and updating of JSON data.
 *
 * @param realm The Realm instance used to interact with the database.
 * @param context The context used for accessing resources and assets.
 */
class CategoriesRepository(private val realm: Realm, private val context: Context) {

     /**
     * Checks and updates the categories, search flows, and options based on the provided JSON data.
     * This method calculates the hash of each JSON and compares it with stored metadata to determine if an update is needed.
     *
     * @param categoriesJson The JSON string representing the categories.
     * @param assignJson The JSON string representing the search flows and field labels.
     * @param attributesJson The JSON string representing the options and fields.
     */
    fun checkAndUpdateCategories(categoriesJson: String,assignJson: String, attributesJson : String) {
        val categoriesHash = calculateHash(categoriesJson)
        val assignHash = calculateHash(assignJson)
        val attributesHash = calculateHash(attributesJson)

        val categoriesMetadata = getMetadata("categories_metadata")
        val assignMetadata = getMetadata("assign_metadata")
        val attributesMetadata = getMetadata("attributes_metadata")


        // Handle Categories JSON
        if (categoriesMetadata == null || categoriesMetadata.jsonHash != categoriesHash) {
            updateCategories(categoriesJson, categoriesHash, categoriesMetadata)
        }
        // Handle Assign JSON
        if (assignMetadata == null || assignMetadata.jsonHash != assignHash) {
            updateSearchFlowAndFieldsLabel(assignJson, assignHash, assignMetadata)
        }
        // Handle Attributes JSON
        if (attributesMetadata == null || attributesMetadata.jsonHash != attributesHash) {
            updateOptionsAndFields(attributesJson, attributesHash, attributesMetadata)
        }
    }

    /**
     * Retrieves metadata based on the given metadata ID.
     *
     * @param metadataId The ID of the metadata to retrieve.
     * @return The `Metadata` object if found, or null if not found.
     */
    private fun getMetadata(metadataId: String): Metadata? {
        return realm.query<Metadata>("id == $0", metadataId).first().find()
    }

    private fun updateCategories(categoriesJson: String, newHash: String, metadata: Metadata?) {
        realm.writeBlocking {
            val categories = JsonUtils.parseCategories(categoriesJson)
            categories.forEach { category ->
                val existingCategory = query<MainCategory>("id == $0", category.id).first().find()
                if (existingCategory == null) {
                    copyToRealm(category)
                } else {
                    updateCategory(existingCategory, category)
                }
            }
            val metadataToUpdate = metadata ?: Metadata().apply { id = "categories_metadata" }
            metadataToUpdate.jsonHash = newHash

            copyToRealm(metadataToUpdate)
        }
    }

    /**
     * Updates the search flow and field labels in the Realm database based on the provided JSON data.
     * The method also updates the corresponding metadata with the new hash.
     *
     * @param assignJson The JSON string representing the search flows and field labels.
     * @param newHash The new hash of the assign JSON.
     * @param metadata The existing metadata, or null if not present.
     */
    private fun updateSearchFlowAndFieldsLabel(assignJson: String, newHash: String, metadata: Metadata?) {
        if (assignJson.isEmpty()) {
            Log.e("CategoriesRepository", "assignJson is empty")
            return
        }
        realm.writeBlocking {
            val searchFlowList = JsonUtils.parseSearchFlow(assignJson)
            val fieldLabelsList = JsonUtils.parseFieldLabels(assignJson)

            searchFlowList.forEach { searchFlow ->
                copyToRealm(searchFlow)
            }

            fieldLabelsList.forEach { fieldLabel ->
                copyToRealm(fieldLabel)
            }

            val metadataToUpdate = metadata ?: Metadata().apply { id = "assign_metadata" }
            metadataToUpdate.jsonHash = newHash
            copyToRealm(metadataToUpdate)
        }
    }

    /**
     * Updates the options and fields in the Realm database based on the provided JSON data.
     * The method also updates the corresponding metadata with the new hash.
     *
     * @param attributesJson The JSON string representing the options and fields.
     * @param newHash The new hash of the attributes JSON.
     * @param metadata The existing metadata, or null if not present.
     */
    private fun updateOptionsAndFields(attributesJson: String, newHash: String, metadata: Metadata?) {
        if (attributesJson.isEmpty()) {
            Log.e("CategoriesRepository", "assignJson is empty")
            return
        }
        realm.writeBlocking {
            val optionsList = JsonUtils.parseOptions(attributesJson)
            val fieldList = JsonUtils.parseFields(attributesJson)

            optionsList.forEach { option ->
                copyToRealm(option)
            }

            fieldList.forEach { field ->
                copyToRealm(field)
            }

            val metadataToUpdate = metadata ?: Metadata().apply { id = "attributes_metadata" }
            metadataToUpdate.jsonHash = newHash
            copyToRealm(metadataToUpdate)
        }
    }

    /**
     * Updates the categories in the Realm database based on the provided JSON data.
     * The method also updates the corresponding metadata with the new hash.
     *
     * @param categoriesJson The JSON string representing the categories.
     * @param newHash The new hash of the categories JSON.
     * @param metadata The existing metadata, or null if not present.
     */
    private fun updateCategory(existingCategory: MainCategory, newCategory: MainCategory) {
        existingCategory.apply {
            name = newCategory.name
            order = newCategory.order
            parentId = newCategory.parentId
            label = newCategory.label
            labelEn = newCategory.labelEn
            hasChild = newCategory.hasChild
            reportingName = newCategory.reportingName
            icon = newCategory.icon
            labelAr = newCategory.labelAr

            subCategories.clear()
            subCategories.addAll(newCategory.subCategories)
        }
    }

    /**
     * Retrieves all main categories from the Realm database.
     *
     * @return A mutable list of `MainCategory` objects.
     */
    fun getCategories(): MutableList<MainCategory> {
        return realm.query<MainCategory>().find().toMutableList()
    }

    /**
     * Calculates the SHA-256 hash of the provided data string.
     *
     * @param data The data to hash.
     * @return The SHA-256 hash as a hexadecimal string.
     */
    private fun calculateHash(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
