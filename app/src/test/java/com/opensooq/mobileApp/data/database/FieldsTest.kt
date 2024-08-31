import com.google.common.truth.Truth.assertThat
import com.opensooq.mobileApp.data.models.Fields
import org.junit.Test

class FieldsTest {

    @Test
    fun `Fields default values`() {
        // Given
        val field = Fields()

        // Then
        assertThat(field.id).isEqualTo(0)
        assertThat(field.name).isEmpty()
        assertThat(field.dataType).isEmpty()
        assertThat(field.parentId).isEqualTo(0)
        assertThat(field.parentName).isNull()
    }

    @Test
    fun `Fields set and get properties`() {
        // Given
        val field = Fields()

        // When
        field.id = 1
        field.name = "Field1"
        field.dataType = "String"
        field.parentId = 0
        field.parentName = null

        // Then
        assertThat(field.id).isEqualTo(1)
        assertThat(field.name).isEqualTo("Field1")
        assertThat(field.dataType).isEqualTo("String")
        assertThat(field.parentId).isEqualTo(0)
        assertThat(field.parentName).isNull()
    }

    @Test
    fun `Fields with non-null parentName`() {
        // Given
        val field = Fields()

        // When
        field.id = 2
        field.name = "Field2"
        field.dataType = "Integer"
        field.parentId = 1
        field.parentName = "ParentField"

        // Then
        assertThat(field.id).isEqualTo(2)
        assertThat(field.name).isEqualTo("Field2")
        assertThat(field.dataType).isEqualTo("Integer")
        assertThat(field.parentId).isEqualTo(1)
        assertThat(field.parentName).isEqualTo("ParentField")
    }

    @Test
    fun `Fields equality based on properties`() {
        // Given
        val field1 = Fields().apply {
            id = 1
            name = "Field1"
            dataType = "String"
            parentId = 0
            parentName = null
        }

        val field2 = Fields().apply {
            id = 1
            name = "Field1"
            dataType = "String"
            parentId = 0
            parentName = null
        }

        // Then
        assertThat(field1.id).isEqualTo(field2.id)
        assertThat(field1.name).isEqualTo(field2.name)
        assertThat(field1.dataType).isEqualTo(field2.dataType)
        assertThat(field1.parentId).isEqualTo(field2.parentId)
        assertThat(field1.parentName).isEqualTo(field2.parentName)
    }

    @Test
    fun `Fields inequality based on different properties`() {
        // Given
        val field1 = Fields().apply {
            id = 1
            name = "Field1"
            dataType = "String"
            parentId = 0
            parentName = null
        }

        val field2 = Fields().apply {
            id = 2
            name = "Field2"
            dataType = "Integer"
            parentId = 1
            parentName = "ParentField"
        }

        // Then
        assertThat(field1.id).isNotEqualTo(field2.id)
        assertThat(field1.name).isNotEqualTo(field2.name)
        assertThat(field1.dataType).isNotEqualTo(field2.dataType)
        assertThat(field1.parentId).isNotEqualTo(field2.parentId)
        assertThat(field1.parentName).isNotEqualTo(field2.parentName)
    }
}
