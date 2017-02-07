package college.wyk.app.ui.welfare

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.landingActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.nlopez.smartlocation.SmartLocation
import kotlinx.android.synthetic.main.fragment_welfare.*

class WelfareFragment : Fragment(), OnMapReadyCallback {

    lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_welfare, container, false)

        val mapContainer = view.findViewById(R.id.map_container) as MapView
        mapContainer.onCreate(savedInstanceState)
        mapContainer.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Welfare Map"
        landingActivity.updateToolbar(toolbar)
    }

    fun onLocatePermissionGranted() {
        my_location.visibility = View.VISIBLE
        my_location.setOnClickListener {
            SmartLocation.with(context).location().oneFix().start { onLocated(it) }
        }
    }

    fun onLocated(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val school = LatLng(22.314245, 114.173275)
        googleMap.addMarker(MarkerOptions().position(school).title("Wah Yan College, Kowloon"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(school))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17f))
        googleMap.setMinZoomPreference(10f)
        googleMap.setMaxZoomPreference(20f)

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onLocatePermissionGranted()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 500)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            500 -> {
                if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    onLocatePermissionGranted()
                }
            }
        }
    }

    override fun onResume() {
        map_container?.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        map_container?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_container?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_container?.onLowMemory()
    }

}