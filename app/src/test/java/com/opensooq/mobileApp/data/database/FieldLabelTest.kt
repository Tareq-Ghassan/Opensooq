import com.google.common.truth.Truth.assertThat
import com.opensooq.mobileApp.data.models.FieldLabel
import org.junit.Test

class FieldLabelTest {

    @Test
    fun `FieldLabel default values`() {
        // Given
        val fieldLabel = FieldLabel()

        // Then
        assertThat(fieldLabel.fieldName).isEmpty()
        assertThat(fieldLabel.labelAr).isEmpty()
        assertThat(fieldLabel.labelEn).isEmpty()
    }

    @Test
    fun `FieldLabel set and get properties`() {
        // Given
        val fieldLabel = FieldLabel()

        // When
        fieldLabel.fieldName = "Field1"
        fieldLabel.labelAr = "اسم الحقل"
        fieldLabel.labelEn = "Field Name"

        // Then
        assertThat(fieldLabel.fieldName).isEqualTo("Field1")
        assertThat(fieldLabel.labelAr).isEqualTo("اسم الحقل")
        assertThat(fieldLabel.labelEn).isEqualTo("Field Name")
    }

    @Test
    fun `FieldLabel equality based on properties`() {
        // Given
        val fieldLabel1 = FieldLabel().apply {
            fieldName = "Field1"
            labelAr = "اسم الحقل"
            labelEn = "Field Name"
        }

        val fieldLabel2 = FieldLabel().apply {
            fieldName = "Field1"
            labelAr = "اسم الحقل"
            labelEn = "Field Name"
        }

        // Then
        assertThat(fieldLabel1.fieldName).isEqualTo(fieldLabel2.fieldName)
        assertThat(fieldLabel1.labelAr).isEqualTo(fieldLabel2.labelAr)
        assertThat(fieldLabel1.labelEn).isEqualTo(fieldLabel2.labelEn)
    }

    @Test
    fun `FieldLabel inequality based on different properties`() {
        // Given
        val fieldLabel1 = FieldLabel().apply {
            fieldName = "Field1"
            labelAr = "اسم الحقل"
            labelEn = "Field Name"
        }

        val fieldLabel2 = FieldLabel().apply {
            fieldName = "Field2"
            labelAr = "اسم آخر"
            labelEn = "Another Field Name"
        }

        // Then
        assertThat(fieldLabel1.fieldName).isNotEqualTo(fieldLabel2.fieldName)
        assertThat(fieldLabel1.labelAr).isNotEqualTo(fieldLabel2.labelAr)
        assertThat(fieldLabel1.labelEn).isNotEqualTo(fieldLabel2.labelEn)
    }
}
