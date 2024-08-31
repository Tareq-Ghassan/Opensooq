import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.models.SearchFlow
import com.opensooq.mobileApp.data.models.SubCategory
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass

class RealmDatabaseTest {

    @Before
    fun setUp() {
        // Ensure Realm is not initialized before each test
        RealmDatabase.close()
    }

    @After
    fun tearDown() {
        // Clean up after each test
        RealmDatabase.close()
    }

    @Test
    fun `init initializes the Realm instance correctly`() {
        RealmDatabase.init()
        val realmInstance = RealmDatabase.getInstance()
        assertNotNull(realmInstance)
    }

    @Test(expected = IllegalStateException::class)
    fun `getInstance throws IllegalStateException if Realm is not initialized`() {
        RealmDatabase.getInstance()
    }

    @Test
    fun `close closes the Realm instance`() {
        RealmDatabase.init()
        RealmDatabase.close()
        assertThrows(IllegalStateException::class.java) {
            RealmDatabase.getInstance()
        }
    }

    @Test
    fun `init does not reinitialize if already initialized`() {
        RealmDatabase.init()
        val realmInstance1 = RealmDatabase.getInstance()
        RealmDatabase.init()
        val realmInstance2 = RealmDatabase.getInstance()
        assertSame(realmInstance1, realmInstance2)
    }

    @Test
    fun `Realm is configured with correct schema`() {
        RealmDatabase.init()
        val config = RealmDatabase.getInstance().configuration

        val expectedClasses: Set<KClass<*>> = setOf(
            MainCategory::class,
            SubCategory::class,
            SearchFlow::class,
            FieldLabel::class,
            Fields::class,
            Options::class,
            Metadata::class
        )

        val actualClasses = config.schema
        expectedClasses.forEach {
            assertTrue("Schema does not contain ${it.simpleName}", actualClasses.contains(it))
        }
    }
}
