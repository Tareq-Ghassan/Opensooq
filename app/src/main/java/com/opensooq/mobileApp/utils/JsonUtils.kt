package com.opensooq.mobileApp.utils

import android.content.Context
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import org.json.JSONObject

object JsonUtils {

    fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
    fun parseCategories(categoriesJson: String): List<MainCategory> {
        val categories = mutableListOf<MainCategory>()
        val jsonObject = JSONObject(categoriesJson)
        val resultObject = jsonObject.getJSONObject("result")
        val dataObject = resultObject.getJSONObject("data")
        val categoriesArray = dataObject.getJSONArray("items")

        for (i in 0 until categoriesArray.length()) {
            val categoryObj = categoriesArray.getJSONObject(i)
            val category = parseCategory(categoryObj)
            categories.add(category)
        }
        return categories
    }

    private fun parseCategory(categoryObj: JSONObject): MainCategory {
        val category = MainCategory().apply {
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
        }

        if (categoryObj.has("subCategories")) {
            val subCategoriesArray = categoryObj.getJSONArray("subCategories")
            for (j in 0 until subCategoriesArray.length()) {
                val subCategory = parseSubCategory(subCategoriesArray.getJSONObject(j))
                category.subCategories.add(subCategory)
            }
        }

        return category
    }

    private fun parseSubCategory(subCategoryObj: JSONObject): SubCategory {
        return SubCategory().apply {
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
        }
    }
}