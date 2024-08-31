import com.google.common.truth.Truth.assertThat
import org.junit.Test
import com.opensooq.mobileApp.data.models.Metadata

class MetadataTest {

    @Test
    fun `Metadata default values`() {
        // Given
        val metadata = Metadata()

        // Then
        assertThat(metadata.id).isEqualTo("categories_metadata")
        assertThat(metadata.jsonHash).isEmpty()
    }

    @Test
    fun `Metadata set and get properties`() {
        // Given
        val metadata = Metadata()

        // When
        metadata.id = "new_metadata_id"
        metadata.jsonHash = "some_hash_value"

        // Then
        assertThat(metadata.id).isEqualTo("new_metadata_id")
        assertThat(metadata.jsonHash).isEqualTo("some_hash_value")
    }

    @Test
    fun `Metadata equality based on properties`() {
        // Given
        val metadata1 = Metadata().apply {
            id = "categories_metadata"
            jsonHash = "hash1"
        }
        val metadata2 = Metadata().apply {
            id = "categories_metadata"
            jsonHash = "hash1"
        }

        // Then
        assertThat(metadata1.id).isEqualTo(metadata2.id)
        assertThat(metadata1.jsonHash).isEqualTo(metadata2.jsonHash)
    }

    @Test
    fun `Metadata inequality based on different properties`() {
        // Given
        val metadata1 = Metadata().apply {
            id = "categories_metadata"
            jsonHash = "hash1"
        }
        val metadata2 = Metadata().apply {
            id = "categories_metadata"
            jsonHash = "hash2"
        }

        // Then
        assertThat(metadata1.jsonHash).isNotEqualTo(metadata2.jsonHash)
    }
}
