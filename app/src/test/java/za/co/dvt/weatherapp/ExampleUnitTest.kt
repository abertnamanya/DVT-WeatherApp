package za.co.dvt.weatherapp

import android.content.Context
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import za.co.dvt.locationtracker.LocationData
import za.co.dvt.locationtracker.LocationSharedPref

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    private lateinit var locationPref: LocationSharedPref
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        locationPref =LocationSharedPref(context)
    }

    @Test
    fun shouldStoreLocationCoordinates() {
        val locationData = LocationData("0.870411", "32.467987")
        locationPref.storeCurrentLocation(locationData)
        var longitude = locationPref.getStoredLocationData().longitude
        println(longitude+"abertnamanya")
//        assertNotNull(locationPref.getStoredLocationData().latitude)
    }
}

