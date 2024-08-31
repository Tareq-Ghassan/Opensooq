import android.content.Context
import android.util.Log
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Metadata
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.security.MessageDigest
import androidx.annotation.VisibleForTesting
import com.opensooq.mobileApp.utils.JsonUtils
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.query.RealmResults

@RunWith(MockitoJUnitRunner::class)
class CategoriesRepositoryTest {

    @MockK
    private lateinit var realm: Realm

    @MockK
    private lateinit var context: Context

    private lateinit var categoriesRepository: CategoriesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this) // Initialize MockK annotations
        categoriesRepository = CategoriesRepository(realm, context)

        // Mock the writeBlocking method to avoid the type mismatch issue
        every { realm.writeBlocking(any<MutableRealm.() -> Unit>()) } just runs
    }
    @Test
    fun `checkAndUpdateCategories updates categories when hashes differ`() {
        // Given
        val categoriesJson = """{"result": {"data": {"items": []}}}"""
        val assignJson = """{"result": {"data": {"search_flow": []}}}"""
        val attributesJson = """{"result": {"data": {"options": []}}}"""

        val categoriesHash = calculateHash(categoriesJson)
        val assignHash = calculateHash(assignJson)
        val attributesHash =calculateHash(attributesJson)

        val mockMetadata = mock(Metadata::class.java)
        `when`(realm.query<Metadata>("id == $0", "categories_metadata").first().find()).thenReturn(mockMetadata)
        `when`(mockMetadata.jsonHash).thenReturn("oldHash")

        verify(exactly = 0) { realm.writeBlocking(any()) }

        // When
        categoriesRepository.checkAndUpdateCategories(categoriesJson, assignJson, attributesJson)

        // Then
        verify(exactly = 1) { realm.writeBlocking(any()) } // Ensure it updates categories, assign, and attributes
    }

    @Test
    fun `checkAndUpdateCategories does not update categories when hashes match`() {
        // Given
        val categoriesJson = """{"result": {"data": {"items": []}}}"""
        val assignJson = """{"result": {"data": {"search_flow": []}}}"""
        val attributesJson = """{"result": {"data": {"options": []}}}"""

        val categoriesHash = calculateHash(categoriesJson)

        val mockMetadata = mock(Metadata::class.java)
        `when`(realm.query<Metadata>("id == $0", "categories_metadata").first().find()).thenReturn(mockMetadata)
        `when`(mockMetadata.jsonHash).thenReturn(categoriesHash)

        // When
        categoriesRepository.checkAndUpdateCategories(categoriesJson, assignJson, attributesJson)

        // Then
        verify(exactly = 0) { realm.writeBlocking(any<MutableRealm.() -> Unit>()) }// Ensure no update occurs
    }

    @Test
    fun `getCategories returns list of MainCategory`() {
        // Given
        val mockRealmResults = mockk<RealmResults<MainCategory>>()
        every { mockRealmResults.toMutableList() } returns mutableListOf(MainCategory(), MainCategory())
        every { realm.query<MainCategory>().find() } returns mockRealmResults

        // When
        val result = categoriesRepository.getCategories()

        // Then
        assertNotNull(result)
        assertEquals(2, result.size)
    }

    @Test
    fun `calculateHash returns correct hash value`() {
        // Given
        val data = "Test Data"
        val expectedHash = MessageDigest.getInstance("SHA-256")
            .digest(data.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }

        // When
        val actualHash = calculateHash(data)

        // Then
        assertEquals(expectedHash, actualHash)
    }

    @Test
    fun `updateCategories writes categories and updates metadata`() {
        // Given
        val categoriesJson = """{"result": {"data": {"items": []}}}"""
        val newHash = calculateHash(categoriesJson)
        val mockMetadata = mock(Metadata::class.java)

        every { realm.writeBlocking(any<MutableRealm.() -> Unit>()) } just Runs

        // When
        updateCategories(categoriesJson, newHash, mockMetadata)

        // Then
        verify { realm.writeBlocking(any<MutableRealm.() -> Unit>()) }
    }

    @Test
    fun `updateSearchFlowAndFieldsLabel writes search flows and field labels and updates metadata`() {
        // Given
        val assignJson = """{"result": {"data": {"search_flow": []}}}"""
        val newHash =calculateHash(assignJson)
        val mockMetadata = mock(Metadata::class.java)

        every { realm.writeBlocking(any<MutableRealm.() -> Unit>()) } just Runs

        // When
       updateSearchFlowAndFieldsLabel(assignJson, newHash, mockMetadata)

        // Then
        verify { realm.writeBlocking(any<MutableRealm.() -> Unit>()) }
    }

    @Test
    fun `updateOptionsAndFields writes options and fields and updates metadata`() {
        // Given
        val attributesJson = """{"result": {"data": {"options": []}}}"""
        val newHash = calculateHash(attributesJson)
        val mockMetadata = mock(Metadata::class.java)

        every { realm.writeBlocking(any<MutableRealm.() -> Unit>()) } just Runs

        // When
        updateOptionsAndFields(attributesJson, newHash, mockMetadata)

        // Then
        verify { realm.writeBlocking(any<MutableRealm.() -> Unit>()) }
    }

    @Test
    fun `getMetadata returns Metadata object when found`() {
        // Given
        val mockMetadata = mock(Metadata::class.java)
        `when`(realm.query<Metadata>("id == $0", "categories_metadata").first().find()).thenReturn(mockMetadata)

        // When
        val result = getMetadata("categories_metadata")

        // Then
        assertNotNull(result)
        assertEquals(mockMetadata, result)
    }

    @Test
    fun `getMetadata returns null when not found`() {
        // Given
        `when`(realm.query<Metadata>("id == $0", "categories_metadata").first().find()).thenReturn(null)

        // When
        val result = getMetadata("categories_metadata")

        // Then
        assertNull(result)
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun calculateHash(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun updateCategories(categoriesJson: String, newHash: String, metadata: Metadata?) {
        realm.writeBlocking {
            val categories = JsonUtils.parseCategories(categoriesJson)
            categories.forEach { category ->
                val existingCategory = query<MainCategory>("id == $0", category.id).first().find()
                if (existingCategory == null) {
                    copyToRealm(category)
                } else {
                    updateCategory(existingCategory, category)
                }
            }
            val metadataToUpdate = metadata ?: Metadata().apply { id = "categories_metadata" }
            metadataToUpdate.jsonHash = newHash

            copyToRealm(metadataToUpdate)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun updateSearchFlowAndFieldsLabel(assignJson: String, newHash: String, metadata: Metadata?) {
        if (assignJson.isEmpty()) {
            Log.e("CategoriesRepository", "assignJson is empty")
            return
        }
        realm.writeBlocking {
            val searchFlowList = JsonUtils.parseSearchFlow(assignJson)
            val fieldLabelsList = JsonUtils.parseFieldLabels(assignJson)

            searchFlowList.forEach { searchFlow ->
                copyToRealm(searchFlow)
            }

            fieldLabelsList.forEach { fieldLabel ->
                copyToRealm(fieldLabel)
            }

            val metadataToUpdate = metadata ?: Metadata().apply { id = "assign_metadata" }
            metadataToUpdate.jsonHash = newHash
            copyToRealm(metadataToUpdate)
        }
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun updateOptionsAndFields(attributesJson: String, newHash: String, metadata: Metadata?) {
        if (attributesJson.isEmpty()) {
            Log.e("CategoriesRepository", "assignJson is empty")
            return
        }
        realm.writeBlocking {
            val optionsList = JsonUtils.parseOptions(attributesJson)
            val fieldList = JsonUtils.parseFields(attributesJson)

            optionsList.forEach { option ->
                copyToRealm(option)
            }

            fieldList.forEach { field ->
                copyToRealm(field)
            }

            val metadataToUpdate = metadata ?: Metadata().apply { id = "attributes_metadata" }
            metadataToUpdate.jsonHash = newHash
            copyToRealm(metadataToUpdate)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getMetadata(metadataId: String): Metadata? {
        return realm.query<Metadata>("id == $0", metadataId).first().find()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal  fun updateCategory(existingCategory: MainCategory, newCategory: MainCategory) {
        existingCategory.apply {
            name = newCategory.name
            order = newCategory.order
            parentId = newCategory.parentId
            label = newCategory.label
            labelEn = newCategory.labelEn
            hasChild = newCategory.hasChild
            reportingName = newCategory.reportingName
            icon = newCategory.icon
            labelAr = newCategory.labelAr

            subCategories.clear()
            subCategories.addAll(newCategory.subCategories)
        }
    }

}
