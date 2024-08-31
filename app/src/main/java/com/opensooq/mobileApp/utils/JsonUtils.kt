package com.opensooq.mobileApp.utils

import android.content.Context
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.models.SearchFlow
import com.opensooq.mobileApp.data.models.SubCategory
import org.json.JSONObject

/**
 * Utility object for handling JSON-related operations in the app.
 */
object JsonUtils {

    /**
     * Loads a JSON file from the assets folder and returns it as a String.
     *
     * @param context The context to use for accessing assets.
     * @param fileName The name of the JSON file to load.
     * @return The content of the JSON file as a String, or null if an error occurs.
     */
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

    /**
     * Parses a JSON string representing categories and returns a list of [MainCategory] objects.
     *
     * @param categoriesJson The JSON string to parse.
     * @return A list of parsed [MainCategory] objects.
     */
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

    /**
     * Parses a single category JSON object and returns a [MainCategory] object.
     *
     * @param categoryObj The JSON object representing a category.
     * @return A [MainCategory] object.
     */
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

    /**
     * Parses a single subcategory JSON object and returns a [SubCategory] object.
     *
     * @param subCategoryObj The JSON object representing a subcategory.
     * @return A [SubCategory] object.
     */
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

    /**
     * Parses a JSON string representing field labels and returns a list of [FieldLabel] objects.
     *
     * @param jsonString The JSON string to parse.
     * @return A list of parsed [FieldLabel] objects.
     */
    fun parseFieldLabels(jsonString: String): List<FieldLabel> {
        val fieldLabels = mutableListOf<FieldLabel>()
        val jsonObject = JSONObject(jsonString)
        val dataObject = jsonObject.getJSONObject("result").getJSONObject("data")
        val fieldsLabelsArray = dataObject.getJSONArray("fields_labels")

        for (i in 0 until fieldsLabelsArray.length()) {
            val fieldLabelObj = fieldsLabelsArray.getJSONObject(i)
            val fieldLabel = FieldLabel().apply {
                fieldName = fieldLabelObj.getString("field_name")
                labelAr = fieldLabelObj.getString("label_ar")
                labelEn = fieldLabelObj.getString("label_en")
            }
            fieldLabels.add(fieldLabel)
        }
        return fieldLabels
    }

    /**
     * Parses a JSON string representing search flows and returns a list of [SearchFlow] objects.
     *
     * @param jsonString The JSON string to parse.
     * @return A list of parsed [SearchFlow] objects.
     */
    fun parseSearchFlow(jsonString: String): List<SearchFlow> {
        val searchFlows = mutableListOf<SearchFlow>()
        val jsonObject = JSONObject(jsonString)
        val dataObject = jsonObject.getJSONObject("result").getJSONObject("data")
        val searchFlowArray = dataObject.getJSONArray("search_flow")

        for (i in 0 until searchFlowArray.length()) {
            val searchFlowObj = searchFlowArray.getJSONObject(i)
            val searchFlow = SearchFlow().apply {
                categoryId = searchFlowObj.getInt("category_id")
                val orderArray = searchFlowObj.getJSONArray("order")
                for (j in 0 until orderArray.length()) {
                    order.add(orderArray.getString(j))
                }
            }
            searchFlows.add(searchFlow)
        }
        return searchFlows
    }

    /**
     * Parses a JSON string representing fields and returns a list of [Fields] objects.
     *
     * @param jsonString The JSON string to parse.
     * @return A list of parsed [Fields] objects.
     */
    fun parseFields(jsonString: String): List<Fields> {
        val fieldLabels = mutableListOf<Fields>()
        val jsonObject = JSONObject(jsonString)
        val dataObject = jsonObject.getJSONObject("result").getJSONObject("data")
        val fieldsArray = dataObject.getJSONArray("fields")

        for (i in 0 until fieldsArray.length()) {
            val fieldObj = fieldsArray.getJSONObject(i)
            val fields = Fields().apply {
                id =fieldObj.getInt("id")
                name =fieldObj.getString("name")
                dataType =fieldObj.getString("data_type")
                parentId = fieldObj.getInt("parent_id")
                parentName = if (fieldObj.has("parent_name") && !fieldObj.isNull("parent_name")) {
                    fieldObj.getString("parent_name")
                } else {
                    null
                }
            }
            fieldLabels.add(fields)
        }
        return fieldLabels
    }

    /**
     * Parses a JSON string representing options and returns a list of [Options] objects.
     *
     * @param jsonString The JSON string to parse.
     * @return A list of parsed [Options] objects.
     */
    fun parseOptions(jsonString: String): List<Options> {
        val options = mutableListOf<Options>()
        val jsonObject = JSONObject(jsonString)
        val dataObject = jsonObject.getJSONObject("result").getJSONObject("data")
        val optionsArray = dataObject.getJSONArray("options")

        for (i in 0 until optionsArray.length()) {
            val optionObj = optionsArray.getJSONObject(i)
            val optionsTemp = Options().apply {
                id =optionObj.getString("id")
                fieldId =optionObj.getString("field_id")
                label =optionObj.getString("label")
                labelEn =optionObj.getString("label_en")
                value =optionObj.getString("value")
                optionImg = if (optionObj.has("option_img") && !optionObj.isNull("option_img")) {
                    optionObj.getString("option_img")
                } else {
                    null
                }
                hasChild =optionObj.getString("has_child")
                parentId = if (optionObj.has("parent_id") && !optionObj.isNull("parent_id")) {
                    optionObj.getString("parent_id")
                } else {
                    null
                }
                order =optionObj.getString("order")

            }
            options.add(optionsTemp)
        }
        return options
    }
}