import android.content.Context
import android.content.res.AssetManager
import com.opensooq.mobileApp.utils.JsonUtils
import org.json.JSONException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream
import java.io.InputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class JsonUtilsTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockAssetManager: AssetManager

    @Before
    fun setUp() {
        // Initialize the mocks
        MockitoAnnotations.initMocks(this)
        // Set the mock AssetManager to be returned when assets are called on the mock context
        Mockito.`when`(mockContext.assets).thenReturn(mockAssetManager)
    }

    @Test
    fun `loadJsonFromAsset returns valid JSON string`() {
        // Given a valid JSON string
        val expectedJson = """{"key":"value"}"""
        val inputStream: InputStream = ByteArrayInputStream(expectedJson.toByteArray())

        // When opening the JSON file using the mocked context
        Mockito.`when`(mockAssetManager.open("valid.json")).thenReturn(inputStream)

        // Then the JSON should be correctly loaded from the asset
        val json = JsonUtils.loadJsonFromAsset(mockContext, "valid.json")
        assertNotNull(json)
        assertEquals(expectedJson, json)
    }

    @Test
    fun `loadJsonFromAsset returns null on exception`() {
        // Given that an exception is thrown when trying to open the JSON file
        Mockito.`when`(mockAssetManager.open("invalid.json")).thenThrow(RuntimeException::class.java)

        // When attempting to load the JSON, it should return null
        val json = JsonUtils.loadJsonFromAsset(mockContext, "invalid.json")
        assertNull(json)
    }

    @Test
    fun `parseCategories returns list of MainCategory`() {
        // Given a JSON string representing categories
        val json = """
            {
                "result": {
                    "data": {
                        "items": [
                            {
                                "id": 1,
                                "name": "Category1",
                                "order": 1,
                                "parent_id": 0,
                                "label": "Category1",
                                "label_en": "Category1_EN",
                                "has_child": 1,
                                "reporting_name": "Report1",
                                "icon": "Icon1",
                                "label_ar": "Category1_AR",
                                "subCategories": [
                                    {
                                        "id": 2,
                                        "name": "SubCategory1",
                                        "order": 1,
                                        "parent_id": 1,
                                        "label": "SubCategory1",
                                        "label_en": "SubCategory1_EN",
                                        "has_child": 0,
                                        "reporting_name": "Report2",
                                        "icon": "Icon2",
                                        "label_ar": "SubCategory1_AR"
                                    }
                                ]
                            }
                        ]
                    }
                }
            }
        """
        // When parsing the categories JSON string
        val categories = JsonUtils.parseCategories(json)

        // Then it should return a list of MainCategory with the correct data
        assertNotNull(categories)
        assertEquals(1, categories.size)
        assertEquals(1, categories[0].id)
        assertEquals("Category1", categories[0].name)
        assertEquals(1, categories[0].subCategories.size)
        assertEquals("SubCategory1", categories[0].subCategories[0].name)
    }

    @Test(expected = JSONException::class)
    fun `parseCategories throws JSONException on malformed JSON`() {
        // Given a malformed JSON string
        val malformedJson = """{ "result": { "data": { "items": [ { "id": 1 """

        // When parsing, it should throw a JSONException
        JsonUtils.parseCategories(malformedJson)
    }

    @Test
    fun `parseFieldLabels returns list of FieldLabel`() {
        // Given a JSON string representing field labels
        val json = """
            {
                "result": {
                    "data": {
                        "fields_labels": [
                            {
                                "field_name": "Field1",
                                "label_ar": "Field1_AR",
                                "label_en": "Field1_EN"
                            }
                        ]
                    }
                }
            }
        """
        // When parsing the field labels JSON string
        val fieldLabels = JsonUtils.parseFieldLabels(json)

        // Then it should return a list of FieldLabel with the correct data
        assertNotNull(fieldLabels)
        assertEquals(1, fieldLabels.size)
        assertEquals("Field1", fieldLabels[0].fieldName)
        assertEquals("Field1_AR", fieldLabels[0].labelAr)
    }

    @Test
    fun `parseSearchFlow returns list of SearchFlow`() {
        // Given a JSON string representing search flow
        val json = """
            {
                "result": {
                    "data": {
                        "search_flow": [
                            {
                                "category_id": 1,
                                "order": ["step1", "step2"]
                            }
                        ]
                    }
                }
            }
        """
        // When parsing the search flow JSON string
        val searchFlows = JsonUtils.parseSearchFlow(json)

        // Then it should return a list of SearchFlow with the correct data
        assertNotNull(searchFlows)
        assertEquals(1, searchFlows.size)
        assertEquals(1, searchFlows[0].categoryId)
        assertEquals(2, searchFlows[0].order.size)
        assertEquals("step1", searchFlows[0].order[0])
    }

    @Test
    fun `parseFields returns list of Fields`() {
        // Given a JSON string representing fields
        val json = """
            {
                "result": {
                    "data": {
                        "fields": [
                            {
                                "id": 1,
                                "name": "Field1",
                                "data_type": "String",
                                "parent_id": 0
                            }
                        ]
                    }
                }
            }
        """
        // When parsing the fields JSON string
        val fields = JsonUtils.parseFields(json)

        // Then it should return a list of Fields with the correct data
        assertNotNull(fields)
        assertEquals(1, fields.size)
        assertEquals(1, fields[0].id)
        assertEquals("Field1", fields[0].name)
        assertEquals("String", fields[0].dataType)
        assertNull(fields[0].parentName)
    }

    @Test
    fun `parseOptions returns list of Options`() {
        // Given a JSON string representing options
        val json = """
            {
                "result": {
                    "data": {
                        "options": [
                            {
                                "id": "1",
                                "field_id": "Field1",
                                "label": "Option1",
                                "label_en": "Option1_EN",
                                "value": "Value1",
                                "has_child": "1",
                                "order": "1"
                            }
                        ]
                    }
                }
            }
        """
        // When parsing the options JSON string
        val options = JsonUtils.parseOptions(json)

        // Then it should return a list of Options with the correct data
        assertNotNull(options)
        assertEquals(1, options.size)
        assertEquals("1", options[0].id)
        assertEquals("Option1_EN", options[0].labelEn)
        assertEquals("1", options[0].hasChild)
    }

    @Test(expected = JSONException::class)
    fun `parseOptions throws JSONException on malformed JSON`() {
        // Given a malformed JSON string
        val malformedJson = """{ "result": { "data": { "options": [ { "id": "1" """

        // When parsing, it should throw a JSONException
        JsonUtils.parseOptions(malformedJson)
    }
}
