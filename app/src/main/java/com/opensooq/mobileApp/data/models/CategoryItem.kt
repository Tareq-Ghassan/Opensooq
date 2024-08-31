package com.opensooq.mobileApp.data.models

/**
 * `CategoryItem` is a sealed class that represents different types of category items
 * in the application. It can either be a `MainCategoryItem` or a `SubCategoryItem`.
 */
sealed class CategoryItem {

    /**
     * Represents an item of type `MainCategory`.
     *
     * @property category The main category associated with this item.
     */
    data class MainCategoryItem(val category: MainCategory) : CategoryItem()

    /**
     * Represents an item of type `SubCategory`.
     *
     * @property subCategory The subcategory associated with this item.
     */
    data class SubCategoryItem(val subCategory: SubCategory) : CategoryItem()
}
