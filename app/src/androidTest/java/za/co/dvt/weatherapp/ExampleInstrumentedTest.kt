package za.co.dvt.weatherapp

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import za.co.dvt.locationtracker.LocationData
import za.co.dvt.locationtracker.LocationSharedPref

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private var locationPref: LocationSharedPref? = null
    private lateinit var context: Context

    @Before
    fun setUp() {
        context =  InstrumentationRegistry.getInstrumentation().targetContext
        locationPref = LocationSharedPref(context)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("za.co.dvt.weatherapp", appContext.packageName)
    }

    @Test
    fun shouldStoreLocationCoordinates() {
        val locationData = LocationData("0.870411", "32.467987")
        locationPref?.storeCurrentLocation(locationData)
        assertNotNull(locationPref?.getStoredLocationData()?.latitude)
    }
}