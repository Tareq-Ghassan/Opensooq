package com.opensooq.mobileApp.data.repositories

import android.content.Context
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Metadata
import com.opensooq.mobileApp.data.models.SubCategory
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

class CategoriesRepository(private val realm: Realm, private val context: Context) {

    fun checkAndUpdateCategories(categoriesJson: String ) {
        val newHash = calculateHash(categoriesJson)

        // Check if the metadata exists
        val metadata = realm.query<Metadata>("id == $0", "categories_metadata").first().find()

        if (metadata == null || metadata.jsonHash != newHash) {
            // Data is new or has changed, so update it
            realm.writeBlocking {
                // Store or update the categories and subcategories
                val jsonObject = JSONObject(categoriesJson)
                val resultObject = jsonObject.getJSONObject("result")
                val dataObject = resultObject.getJSONObject("data")
                val categoriesArray = dataObject.getJSONArray("items")

                for (i in 0 until categoriesArray.length()) {
                    val categoryObj = categoriesArray.getJSONObject(i)

                    val category = query<MainCategory>("id == $0", categoryObj.getInt("id")).first().find()
                    if (category == null) {
                        copyToRealm(MainCategory().apply {
                            id = categoryObj.getInt("id")
                            name = categoryObj.getString("name")
                            order = categoryObj.getInt("order")
                            parentId = categoryObj.getInt("parent_id")
                            label = categoryObj.getString("label")
                            labelEn = categoryObj.getString("label_en")
                            hasChild = categoryObj.getInt("has_child") == 1
                            reportingName = categoryObj.getString("reporting_name")
                            icon = categoryObj.getString("icon")
                            labelAr = categoryObj.getString("label_ar")
                        })
                    } else {
                        category.name = categoryObj.getString("name")
                        category.order = categoryObj.getInt("order")
                        category.parentId = categoryObj.getInt("parent_id")
                        category.label = categoryObj.getString("label")
                        category.labelEn = categoryObj.getString("label_en")
                        category.hasChild = categoryObj.getInt("has_child") == 1
                        category.reportingName = categoryObj.getString("reporting_name")
                        category.icon = categoryObj.getString("icon")
                        category.labelAr = categoryObj.getString("label_ar")
                    }

                    // Process subcategories
                    if (categoryObj.has("subCategories")) {
                        val subCategoriesArray = categoryObj.getJSONArray("subCategories")
                        for (j in 0 until subCategoriesArray.length()) {
                            val subCategoryObj = subCategoriesArray.getJSONObject(j)
                            val subCategory = query<SubCategory>("id == $0", subCategoryObj.getInt("id")).first().find()
                            if (subCategory == null) {
                                copyToRealm(SubCategory().apply {
                                    id = subCategoryObj.getInt("id")
                                    name = subCategoryObj.getString("name")
                                    order = subCategoryObj.getInt("order")
                                    parentId = subCategoryObj.getInt("parent_id")
                                    label = subCategoryObj.getString("label")
                                    labelEn = subCategoryObj.getString("label_en")
                                    hasChild = subCategoryObj.getInt("has_child") == 0
                                    reportingName = subCategoryObj.getString("reporting_name")
                                    icon = subCategoryObj.getString("icon")
                                    labelAr = subCategoryObj.getString("label_ar")
                                })
                            } else {
                                subCategory.name = subCategoryObj.getString("name")
                                subCategory.order = subCategoryObj.getInt("order")
                                subCategory.parentId = subCategoryObj.getInt("parent_id")
                                subCategory.label = subCategoryObj.getString("label")
                                subCategory.labelEn = subCategoryObj.getString("label_en")
                                subCategory.hasChild = subCategoryObj.getInt("has_child") == 0
                                subCategory.reportingName = subCategoryObj.getString("reporting_name")
                                subCategory.icon = subCategoryObj.getString("icon")
                                subCategory.labelAr = subCategoryObj.getString("label_ar")
                            }
                        }
                    }
                }

                // Update or insert the metadata with the new hash
                val metadataToUpdate = metadata ?: Metadata().apply { id = "categories_metadata" }
                metadataToUpdate.jsonHash = newHash
                copyToRealm(metadataToUpdate)
            }
        }
    }

    fun getCategories() : MutableList<MainCategory>{
        val catList:MutableList<MainCategory>  = mutableListOf()
        catList.addAll(realm.query<MainCategory>().find())
        return catList
    }

    private fun calculateHash(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
