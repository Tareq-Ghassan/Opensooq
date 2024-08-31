import com.opensooq.mobileApp.data.models.SubCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class SubCategoryTest {

    @Test
    fun `test SubCategory properties`() {
        // Given
        val subCategory = SubCategory().apply {
            id = 1
            name = "Smartphones"
            order = 2
            parentId = 10
            label = "Smartphones and Gadgets"
            labelEn = "Smartphones"
            hasChild = true
            reportingName = "SmartphonesCategory"
            icon = "https://example.com/icon.png"
            labelAr = "الهواتف الذكية"
        }

        // Then
        assertEquals(1, subCategory.id)
        assertEquals("Smartphones", subCategory.name)
        assertEquals(2, subCategory.order)
        assertEquals(10, subCategory.parentId)
        assertEquals("Smartphones and Gadgets", subCategory.label)
        assertEquals("Smartphones", subCategory.labelEn)
        assertEquals(true, subCategory.hasChild)
        assertEquals("SmartphonesCategory", subCategory.reportingName)
        assertEquals("https://example.com/icon.png", subCategory.icon)
        assertEquals("الهواتف الذكية", subCategory.labelAr)
    }

    @Test
    fun `test SubCategory equality`() {
        // Given
        val subCategory1 = SubCategory().apply {
            id = 1
            name = "Smartphones"
            order = 2
            parentId = 10
            label = "Smartphones and Gadgets"
            labelEn = "Smartphones"
            hasChild = true
            reportingName = "SmartphonesCategory"
            icon = "https://example.com/icon.png"
            labelAr = "الهواتف الذكية"
        }

        val subCategory2 = SubCategory().apply {
            id = 1
            name = "Smartphones"
            order = 2
            parentId = 10
            label = "Smartphones and Gadgets"
            labelEn = "Smartphones"
            hasChild = true
            reportingName = "SmartphonesCategory"
            icon = "https://example.com/icon.png"
            labelAr = "الهواتف الذكية"
        }

        // Then
        assertEquals(subCategory1, subCategory2)
    }
}
