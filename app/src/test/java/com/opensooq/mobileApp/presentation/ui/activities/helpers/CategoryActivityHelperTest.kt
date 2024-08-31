
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.presentation.ui.activities.CategoryActivity
import com.opensooq.mobileApp.presentation.ui.activities.helpers.CategoryActivityHelper
import com.opensooq.mobileApp.presentation.ui.adapters.CategoriesAdapter
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CategoryActivityHelperTest {

    @MockK
    private lateinit var mockActivity: CategoryActivity

    @MockK
    private lateinit var mockRecyclerView: RecyclerView

    @MockK
    private lateinit var mockSearchView: SearchView

    @MockK
    private lateinit var mockTextView: TextView

    @MockK
    private lateinit var mockAdapter: CategoriesAdapter

    @MockK
    private lateinit var mockViewModel: CategoriesViewModel

    private lateinit var helper: CategoryActivityHelper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        helper = CategoryActivityHelper(mockActivity)

        every { mockActivity.findViewById<RecyclerView>(R.id.recyclerView) } returns mockRecyclerView
        every { mockRecyclerView.layoutManager = any<LinearLayoutManager>() } returns Unit
        every { mockRecyclerView.adapter = any<CategoriesAdapter>() } returns Unit
        every { mockActivity.findViewById<SearchView>(R.id.search_view_cat) } returns mockSearchView
        every { mockActivity.findViewById<TextView>(R.id.selected_category) } returns mockTextView
    }

    @Test
    fun `setupActivity initializes the activity components`() {
        // Given
        val type = CategoryActivity.TYPE_CATEGORY
        val categoryId = 0
        every { mockActivity.intent.getStringExtra(CategoryActivity.TYPE) } returns type
        every { mockActivity.intent.getIntExtra(CategoryActivity.CATEGORY_ID, 0) } returns categoryId

        // When
        val (returnedType, returnedCategoryId, searchView) = helper.setupActivity()

        // Then
        assertEquals(type, returnedType)
        assertEquals(categoryId, returnedCategoryId)
        assertEquals(mockSearchView, searchView)
        verify(exactly = 1) { mockRecyclerView.layoutManager = any<LinearLayoutManager>() }
        verify(exactly = 1) { mockRecyclerView.adapter = any<CategoriesAdapter>() }
    }

//    @Test
//    fun `setupTextViewAndSearchView sets the correct query hints and visibility`() {
//        // Given
//        val typeCategory = CategoryActivity.TYPE_CATEGORY
//        val typeSubCategory = CategoryActivity.TYPE_SUBCATEGORY
//        every { mockActivity.getString(R.string.search_for_category_or_subcategory) } returns "Search for category or subcategory"
//        every { mockActivity.getString(R.string.search_for_subcategory) } returns "Search for subcategory"
//
//        // When
//        setupTextViewAndSearchView(typeCategory, mockTextView, mockSearchView)
//        setupTextViewAndSearchView(typeSubCategory, mockTextView, mockSearchView)
//
//        // Then
//        verify { mockTextView.visibility = View.GONE }
//        verify { mockSearchView.queryHint = "Search for category or subcategory" }
//
//        verify { mockTextView.visibility = View.VISIBLE }
//        verify { mockSearchView.queryHint = "Search for subcategory" }
//    }

//    @Test
//    fun `loadCategoriesAndSub loads categories when type is CATEGORY`(): Unit = runTest {
//        // Given
//        val type = CategoryActivity.TYPE_CATEGORY
//        val categoryId = 0
//        val mockScope = CoroutineScope(Dispatchers.Main)
//        every { JsonUtils.loadJsonFromAsset(mockActivity, "categoriesAndsubCategories.json") } returns "{}"
//        every { mockViewModel.checkAndCacheJson(any(), any(), any()) } returns Unit
//
//        // When
//        helper.loadCategoriesAndSub(type, categoryId, mockScope)
//
//        // Then
//        verify { JsonUtils.loadJsonFromAsset(mockActivity, "categoriesAndsubCategories.json") }
//    }

//    @Test
//    fun `loadCategoriesAndSub loads subcategories when type is SUBCATEGORY`() = runTest {
//        // Given
//        val type = CategoryActivity.TYPE_SUBCATEGORY
//        val categoryId = 1
//        val mockScope = CoroutineScope(Dispatchers.Main)
//        every { mockViewModel.getSubCategories(categoryId) } returns Unit
//
//        // When
//        helper.loadCategoriesAndSub(type, categoryId, mockScope)
//
//        // Then
//        verify { mockViewModel.getSubCategories(categoryId) }
//    }

    @Test
    fun `performSearch sets up query text listener on search view`() {
        // Given
        val type = CategoryActivity.TYPE_CATEGORY
        every { mockSearchView.setOnQueryTextListener(any()) } returns Unit

        // When
        helper.performSearch(mockSearchView, type)

        // Then
        verify { mockSearchView.setOnQueryTextListener(any()) }
    }

//    @Test
//    fun `onItemClicked starts FilterActivity when subcategory item is clicked`() {
//        // Given
//        val subCategoryItem = CategoryItem.SubCategoryItem(mockk())
//        every { mockActivity.startActivity(any()) } returns Unit
//
//        // When
//        onItemClicked(subCategoryItem)
//
//        // Then
//        verify { mockActivity.startActivity(any<Intent>()) }
//    }

    @Test
    fun `clearAndCloseKeyboard clears the search view and hides the keyboard`() {
        // Given
        every { mockSearchView.setQuery("", false) } returns Unit
        every { mockSearchView.clearFocus() } returns Unit
        every { mockActivity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager } returns mockk()

        // When
        helper.clearAndCloseKeyboard()

        // Then
        verify { mockSearchView.setQuery("", false) }
        verify { mockSearchView.clearFocus() }
    }

    @Test
    fun `updateUIOnResume hides or shows TextView based on type`() {
        // Given
        every { mockActivity.intent.getStringExtra(CategoryActivity.TYPE) } returns CategoryActivity.TYPE_CATEGORY

        // When
        helper.updateUIOnResume()

        // Then
        verify { mockTextView.visibility = View.GONE }

        // Given
        every { mockActivity.intent.getStringExtra(CategoryActivity.TYPE) } returns CategoryActivity.TYPE_SUBCATEGORY

        // When
        helper.updateUIOnResume()

        // Then
        verify { mockTextView.visibility = View.VISIBLE }
    }

//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
//    internal fun setupTextViewAndSearchView(type: String, textView: TextView, searchView: SearchView) {
//        if (type == CategoryActivity.TYPE_CATEGORY) {
//            textView.visibility = View.GONE
//            searchView.queryHint = activity.getString(R.string.search_for_category_or_subcategory)
//        } else {
//            val selectedCategoryName = activity.intent.getStringExtra("SELECTED_CATEGORY_NAME")
//            textView.visibility = View.VISIBLE
//            textView.text = selectedCategoryName
//            searchView.queryHint = activity.getString(R.string.search_for_subcategory)
//        }
//    }
//    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
//    internal fun onItemClicked(item: CategoryItem) {
//        when (item) {
//            is CategoryItem.MainCategoryItem -> {
//                val intent = Intent(activity, CategoryActivity::class.java).apply {
//                    putExtra(CategoryActivity.TYPE, CategoryActivity.TYPE_SUBCATEGORY)
//                    putExtra(CategoryActivity.CATEGORY_ID, item.category.id)
//                    putExtra("SELECTED_CATEGORY_NAME", item.category.labelEn) // Pass the selected category name
//                }
//                activity.startActivity(intent)
//            }
//            is CategoryItem.SubCategoryItem -> {
//                val intent = Intent(activity, FilterActivity::class.java).apply {
//                    putExtra("SUBCATEGORY_ID", item.subCategory.id)
//                }
//                activity.startActivity(intent)
//            }
//        }
//    }
}
