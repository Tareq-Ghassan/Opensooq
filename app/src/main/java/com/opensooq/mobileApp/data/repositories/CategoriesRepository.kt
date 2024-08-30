package com.opensooq.mobileApp.data.repositories

import android.content.Context
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Metadata
import com.opensooq.mobileApp.utils.JsonUtils
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import java.security.MessageDigest

class CategoriesRepository(private val realm: Realm, private val context: Context) {

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

    private fun updateSearchFlowAndFieldsLabel(assignJson: String, newHash: String, metadata: Metadata?) {
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
    private fun updateOptionsAndFields(attributesJson: String, newHash: String, metadata: Metadata?) {
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

    fun getCategories(): MutableList<MainCategory> {
        return realm.query<MainCategory>().find().toMutableList()
    }

    private fun calculateHash(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
