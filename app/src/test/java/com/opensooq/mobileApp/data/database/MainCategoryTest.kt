import com.google.common.truth.Truth.assertThat
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import org.junit.Test

class MainCategoryTest {

    @Test
    fun `MainCategory default values`() {
        // Given
        val mainCategory = MainCategory()

        // Then
        assertThat(mainCategory.id).isEqualTo(0)
        assertThat(mainCategory.name).isEmpty()
        assertThat(mainCategory.order).isEqualTo(0)
        assertThat(mainCategory.parentId).isEqualTo(0)
        assertThat(mainCategory.label).isEmpty()
        assertThat(mainCategory.labelEn).isEmpty()
        assertThat(mainCategory.hasChild).isFalse()
        assertThat(mainCategory.reportingName).isEmpty()
        assertThat(mainCategory.icon).isEmpty()
        assertThat(mainCategory.labelAr).isEmpty()
        assertThat(mainCategory.subCategories).isEmpty()
    }

    @Test
    fun `MainCategory set and get properties`() {
        // Given
        val mainCategory = MainCategory()

        // When
        mainCategory.id = 1
        mainCategory.name = "Electronics"
        mainCategory.order = 2
        mainCategory.parentId = 0
        mainCategory.label = "Electronics Label"
        mainCategory.labelEn = "Electronics"
        mainCategory.hasChild = true
        mainCategory.reportingName = "Electronics_Report"
        mainCategory.icon = "url_to_icon"
        mainCategory.labelAr = "الإلكترونيات"

        // Then
        assertThat(mainCategory.id).isEqualTo(1)
        assertThat(mainCategory.name).isEqualTo("Electronics")
        assertThat(mainCategory.order).isEqualTo(2)
        assertThat(mainCategory.parentId).isEqualTo(0)
        assertThat(mainCategory.label).isEqualTo("Electronics Label")
        assertThat(mainCategory.labelEn).isEqualTo("Electronics")
        assertThat(mainCategory.hasChild).isTrue()
        assertThat(mainCategory.reportingName).isEqualTo("Electronics_Report")
        assertThat(mainCategory.icon).isEqualTo("url_to_icon")
        assertThat(mainCategory.labelAr).isEqualTo("الإلكترونيات")
    }

    @Test
    fun `MainCategory subcategories are set correctly`() {
        // Given
        val mainCategory = MainCategory()
        val subCategory = SubCategory().apply {
            id = 1
            name = "Smartphones"
            parentId = 1
        }

        // When
        mainCategory.subCategories.add(subCategory)

        // Then
        assertThat(mainCategory.subCategories).hasSize(1)
        assertThat(mainCategory.subCategories[0].name).isEqualTo("Smartphones")
    }

    @Test
    fun `MainCategory equality based on properties`() {
        // Given
        val mainCategory1 = MainCategory().apply {
            id = 1
            name = "Electronics"
        }
        val mainCategory2 = MainCategory().apply {
            id = 1
            name = "Electronics"
        }

        // Then
        assertThat(mainCategory1.id).isEqualTo(mainCategory2.id)
        assertThat(mainCategory1.name).isEqualTo(mainCategory2.name)
    }

    @Test
    fun `MainCategory inequality based on different properties`() {
        // Given
        val mainCategory1 = MainCategory().apply {
            id = 1
            name = "Electronics"
        }
        val mainCategory2 = MainCategory().apply {
            id = 2
            name = "Clothing"
        }

        // Then
        assertThat(mainCategory1.id).isNotEqualTo(mainCategory2.id)
        assertThat(mainCategory1.name).isNotEqualTo(mainCategory2.name)
    }
}
