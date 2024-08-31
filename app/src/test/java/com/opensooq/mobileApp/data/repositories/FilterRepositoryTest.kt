import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.models.SearchFlow
import com.opensooq.mobileApp.data.repositories.FilterRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmQuery
import io.realm.kotlin.query.RealmResults
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test

class FilterRepositoryTest {

    @MockK
    private lateinit var realm: Realm

    private lateinit var filterRepository: FilterRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        filterRepository = FilterRepository(realm)
    }

    @Test
    fun `getSearchFlow returns SearchFlow when found`() {
        // Given
        val subcategoryId = 123
        val mockSearchFlow = mockk<SearchFlow>()
        val mockQuery = mockk<RealmQuery<SearchFlow>>()
        val mockResults = mockk<RealmResults<SearchFlow>>()

        every { realm.query<SearchFlow>("categoryId == $0", subcategoryId) } returns mockQuery
        every { mockQuery.first().find() } returns mockSearchFlow

        // When
        val result = filterRepository.getSearchFlow(subcategoryId)

        // Then
        assertNotNull(result)
        assertEquals(mockSearchFlow, result)
    }

    @Test
    fun `getSearchFlow returns null when not found`() {
        // Given
        val subcategoryId = 123
        val mockQuery = mockk<RealmQuery<SearchFlow>>()
        val mockResults = mockk<RealmResults<SearchFlow>>()

        every { realm.query<SearchFlow>("categoryId == $0", subcategoryId) } returns mockQuery
        every { mockQuery.first().find() } returns null

        // When
        val result = filterRepository.getSearchFlow(subcategoryId)

        // Then
        assertNull(result)
    }

    @Test
    fun `getAllFieldLabels returns list of FieldLabel`() {
        // Given
        val mockFieldLabels = mockk<RealmResults<FieldLabel>>()
        every { realm.query<FieldLabel>().find() } returns mockFieldLabels

        // When
        val result = filterRepository.getAllFieldLabels()

        // Then
        assertNotNull(result)
        assertEquals(mockFieldLabels, result)
    }

    @Test
    fun `getAllFields returns list of Fields`() {
        // Given
        val mockFields = mockk<RealmResults<Fields>>()
        every { realm.query<Fields>().find() } returns mockFields

        // When
        val result = filterRepository.getAllFields()

        // Then
        assertNotNull(result)
        assertEquals(mockFields, result)
    }

    @Test
    fun `getOptionsByFieldId returns list of Options`() {
        // Given
        val fieldId = "field123"
        val mockOptions = mockk<RealmResults<Options>>()
        val mockQuery = mockk<RealmQuery<Options>>()

        every { realm.query<Options>("fieldId == $0", fieldId) } returns mockQuery
        every { mockQuery.find() } returns mockOptions

        // When
        val result = filterRepository.getOptionsByFieldId(fieldId)

        // Then
        assertNotNull(result)
        assertEquals(mockOptions, result)
    }
}
