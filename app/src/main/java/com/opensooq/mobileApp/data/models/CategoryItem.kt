package com.opensooq.mobileApp.data.models

sealed class CategoryItem {
    data class MainCategoryItem(val category: MainCategory) : CategoryItem()
    data class SubCategoryItem(val subCategory: SubCategory) : CategoryItem()
}
