import com.google.common.truth.Truth.assertThat
import com.opensooq.mobileApp.data.models.CategoryItem
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import org.junit.Test

class CategoryItemTest {

    @Test
    fun `MainCategoryItem should hold correct main category`() {
        // Given
        val mainCategory = MainCategory().apply {
            id = 1
            name = "Electronics"
        }

        // When
        val categoryItem = CategoryItem.MainCategoryItem(mainCategory)

        // Then
        assertThat(categoryItem.category.id).isEqualTo(1)
        assertThat(categoryItem.category.name).isEqualTo("Electronics")
    }

    @Test
    fun `SubCategoryItem should hold correct subcategory`() {
        // Given
        val subCategory = SubCategory().apply {
            id = 2
            name = "Smartphones"
        }

        // When
        val categoryItem = CategoryItem.SubCategoryItem(subCategory)

        // Then
        assertThat(categoryItem.subCategory.id).isEqualTo(2)
        assertThat(categoryItem.subCategory.name).isEqualTo("Smartphones")
    }

    @Test
    fun `MainCategoryItem equality based on properties`() {
        // Given
        val mainCategory1 = MainCategory().apply {
            id = 1
            name = "Electronics"
        }
        val mainCategory2 = MainCategory().apply {
            id = 1
            name = "Electronics"
        }

        // When
        val categoryItem1 = CategoryItem.MainCategoryItem(mainCategory1)
        val categoryItem2 = CategoryItem.MainCategoryItem(mainCategory2)

        // Then
        assertThat(categoryItem1.category.id).isEqualTo(categoryItem2.category.id)
        assertThat(categoryItem1.category.name).isEqualTo(categoryItem2.category.name)
    }

    @Test
    fun `SubCategoryItem equality based on properties`() {
        // Given
        val subCategory1 = SubCategory().apply {
            id = 2
            name = "Smartphones"
        }
        val subCategory2 = SubCategory().apply {
            id = 2
            name = "Smartphones"
        }

        // When
        val categoryItem1 = CategoryItem.SubCategoryItem(subCategory1)
        val categoryItem2 = CategoryItem.SubCategoryItem(subCategory2)

        // Then
        assertThat(categoryItem1.subCategory.id).isEqualTo(categoryItem2.subCategory.id)
        assertThat(categoryItem1.subCategory.name).isEqualTo(categoryItem2.subCategory.name)
    }
}
