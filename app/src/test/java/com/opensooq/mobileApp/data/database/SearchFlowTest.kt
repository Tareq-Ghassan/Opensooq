import com.opensooq.mobileApp.data.models.SearchFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class SearchFlowTest {

    private lateinit var searchFlow: SearchFlow

    @Before
    fun setUp() {
        searchFlow = SearchFlow()
    }

    @Test
    fun `test default values`() {
        // Test default values after initialization
        assertEquals(0, searchFlow.categoryId)
        assertEquals(0, searchFlow.order.size)
    }

    @Test
    fun `test categoryId`() {
        // Set and test categoryId
        searchFlow.categoryId = 123
        assertEquals(123, searchFlow.categoryId)
    }

    @Test
    fun `test order list modification`() {
        // Add items to the order list
        searchFlow.order.add("step1")
        searchFlow.order.add("step2")
        searchFlow.order.add("step3")

        assertEquals(3, searchFlow.order.size)
        assertEquals("step1", searchFlow.order[0])
        assertEquals("step2", searchFlow.order[1])
        assertEquals("step3", searchFlow.order[2])
    }

    @Test
    fun `test equality based on categoryId and order`() {
        // Create two SearchFlow instances with the same values
        val searchFlow1 = SearchFlow().apply {
            categoryId = 123
            order.addAll(listOf("step1", "step2"))
        }

        val searchFlow2 = SearchFlow().apply {
            categoryId = 123
            order.addAll(listOf("step1", "step2"))
        }

        assertEquals(searchFlow1, searchFlow2)
    }

    @Test
    fun `test inequality with different categoryId`() {
        // Create two SearchFlow instances with different categoryIds
        val searchFlow1 = SearchFlow().apply {
            categoryId = 123
        }

        val searchFlow2 = SearchFlow().apply {
            categoryId = 456
        }

        assertNotEquals(searchFlow1, searchFlow2)
    }

    @Test
    fun `test inequality with different order`() {
        // Create two SearchFlow instances with different orders
        val searchFlow1 = SearchFlow().apply {
            categoryId = 123
            order.addAll(listOf("step1", "step2"))
        }

        val searchFlow2 = SearchFlow().apply {
            categoryId = 123
            order.addAll(listOf("step2", "step3"))
        }

        assertNotEquals(searchFlow1, searchFlow2)
    }
}
