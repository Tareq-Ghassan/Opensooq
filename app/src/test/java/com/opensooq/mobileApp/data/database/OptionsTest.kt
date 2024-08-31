import com.google.common.truth.Truth.assertThat
import com.opensooq.mobileApp.data.models.Options
import org.junit.Test

class OptionsTest {

    @Test
    fun `Options default values`() {
        // Given
        val options = Options()

        // Then
        assertThat(options.id).isEmpty()
        assertThat(options.fieldId).isEmpty()
        assertThat(options.label).isEmpty()
        assertThat(options.labelEn).isEmpty()
        assertThat(options.value).isEmpty()
        assertThat(options.optionImg).isNull()
        assertThat(options.hasChild).isEmpty()
        assertThat(options.parentId).isNull()
        assertThat(options.order).isEmpty()
    }

    @Test
    fun `Options set and get properties`() {
        // Given
        val options = Options()

        // When
        options.id = "1"
        options.fieldId = "field1"
        options.label = "Option 1"
        options.labelEn = "Option 1 EN"
        options.value = "Value 1"
        options.optionImg = "http://example.com/image.png"
        options.hasChild = "1"
        options.parentId = "parent1"
        options.order = "1"

        // Then
        assertThat(options.id).isEqualTo("1")
        assertThat(options.fieldId).isEqualTo("field1")
        assertThat(options.label).isEqualTo("Option 1")
        assertThat(options.labelEn).isEqualTo("Option 1 EN")
        assertThat(options.value).isEqualTo("Value 1")
        assertThat(options.optionImg).isEqualTo("http://example.com/image.png")
        assertThat(options.hasChild).isEqualTo("1")
        assertThat(options.parentId).isEqualTo("parent1")
        assertThat(options.order).isEqualTo("1")
    }

    @Test
    fun `Options equality based on properties`() {
        // Given
        val options1 = Options().apply {
            id = "1"
            fieldId = "field1"
            label = "Option 1"
            labelEn = "Option 1 EN"
            value = "Value 1"
            optionImg = "http://example.com/image.png"
            hasChild = "1"
            parentId = "parent1"
            order = "1"
        }
        val options2 = Options().apply {
            id = "1"
            fieldId = "field1"
            label = "Option 1"
            labelEn = "Option 1 EN"
            value = "Value 1"
            optionImg = "http://example.com/image.png"
            hasChild = "1"
            parentId = "parent1"
            order = "1"
        }

        // Then
        assertThat(options1).isEqualTo(options2)
    }

    @Test
    fun `Options inequality based on different properties`() {
        // Given
        val options1 = Options().apply {
            id = "1"
            fieldId = "field1"
            label = "Option 1"
            labelEn = "Option 1 EN"
            value = "Value 1"
            optionImg = "http://example.com/image.png"
            hasChild = "1"
            parentId = "parent1"
            order = "1"
        }
        val options2 = Options().apply {
            id = "2"
            fieldId = "field2"
            label = "Option 2"
            labelEn = "Option 2 EN"
            value = "Value 2"
            optionImg = "http://example.com/another-image.png"
            hasChild = "0"
            parentId = "parent2"
            order = "2"
        }

        // Then
        assertThat(options1).isNotEqualTo(options2)
    }

    @Test
    fun `Options handles null fields correctly`() {
        // Given
        val options = Options().apply {
            id = "1"
            fieldId = "field1"
            label = "Option 1"
            labelEn = "Option 1 EN"
            value = "Value 1"
            optionImg = null
            hasChild = "1"
            parentId = null
            order = "1"
        }

        // Then
        assertThat(options.optionImg).isNull()
        assertThat(options.parentId).isNull()
    }
}
