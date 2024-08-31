import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.presentation.ui.activities.CategoryActivity
import com.opensooq.mobileApp.presentation.ui.activities.helpers.CategoryActivityHelper
import io.mockk.*
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoryActivityTest {

    private lateinit var mockActivity: CategoryActivity
    private lateinit var mockHelper: CategoryActivityHelper
    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setUp() {
        mockActivity = mockk(relaxed = true)
        mockHelper = mockk(relaxed = true)

        lifecycle = LifecycleRegistry(mockActivity as LifecycleOwner)
        every { mockActivity.lifecycle } returns lifecycle

//        every { mockActivity.helper } returns mockHelper

        every { mockActivity.setSupportActionBar(any()) } just Runs
        every { mockActivity.supportActionBar } returns mockk<ActionBar> {
            every { setDisplayHomeAsUpEnabled(true) } just Runs
            every { title = any() } just Runs
        }

        // Mock intent behavior
        every { mockActivity.intent } returns mockk {
            every { getStringExtra(CategoryActivity.TYPE) } returns CategoryActivity.TYPE_CATEGORY
            every { getIntExtra(CategoryActivity.CATEGORY_ID, 0) } returns 0
        }
    }

    @Test
    fun `test onCreate initializes the helper and sets up the UI`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        verify { mockHelper.setupActivity() }
    }

    @Test
    fun `test onPause clears and closes the keyboard`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        verify { mockHelper.clearAndCloseKeyboard() }
    }

    @Test
    fun `test onResume updates the UI`() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        verify { mockHelper.updateUIOnResume() }
    }

    @Test
    fun `test setupActionBar sets the correct title and back button`() {
        val mockToolbar = mockk<Toolbar>()
        every { mockActivity.findViewById<Toolbar>(R.id.toolbar) } returns mockToolbar

        mockActivity.setupActionBar()

        verify { mockActivity.setSupportActionBar(mockToolbar) }
        verify { mockActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true) }
        verify { mockActivity.supportActionBar?.title = "Categories" }
    }

    @Test
    fun `test onOptionsItemSelected handles home button`() {
        val mockMenuItem = mockk<MenuItem> {
            every { itemId } returns android.R.id.home
        }
        every { mockActivity.onBackPressedDispatcher.onBackPressed() } just Runs

        val result = mockActivity.onOptionsItemSelected(mockMenuItem)

        verify { mockActivity.onBackPressedDispatcher.onBackPressed() }
        assertEquals(true, result)
    }

    @Test
    fun `test onCreateOptionsMenu inflates menu for category type`() {
        val mockMenu = mockk<Menu>(relaxed = true)
        val mockMenuInflater = mockk<MenuInflater>()
        every { mockActivity.menuInflater } returns mockMenuInflater
        every { mockMenuInflater.inflate(R.menu.category_menu, mockMenu) } just Runs

        val result = mockActivity.onCreateOptionsMenu(mockMenu)

        verify { mockMenuInflater.inflate(R.menu.category_menu, mockMenu) }
        assertEquals(true, result)
    }

    @Test
    fun `test onCreateOptionsMenu does not inflate menu for subcategory type`() {
        every { mockActivity.intent.getStringExtra(CategoryActivity.TYPE) } returns CategoryActivity.TYPE_SUBCATEGORY
        val mockMenu = mockk<Menu>(relaxed = true)
        val mockMenuInflater = mockk<MenuInflater>(relaxed = true)
        every { mockActivity.menuInflater } returns mockMenuInflater

        val result = mockActivity.onCreateOptionsMenu(mockMenu)

        verify(exactly = 0) { mockMenuInflater.inflate(R.menu.category_menu, mockMenu) }
        assertEquals(true, result)
    }
}
