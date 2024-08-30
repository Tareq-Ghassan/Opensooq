package com.opensooq.mobileApp.data.repositories

import android.content.Context
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Metadata
import com.opensooq.mobileApp.utils.JsonUtils
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import java.security.MessageDigest

class CategoriesRepository(private val realm: Realm, private val context: Context) {

    fun checkAndUpdateCategories(categoriesJson: String) {
        val newHash = calculateHash(categoriesJson)
        val metadata = getMetadata()

        if (metadata == null || metadata.jsonHash != newHash) {
            updateCategories(categoriesJson, newHash, metadata)
        }
    }

    private fun getMetadata(): Metadata? {
        return realm.query<Metadata>("id == $0", "categories_metadata").first().find()
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
