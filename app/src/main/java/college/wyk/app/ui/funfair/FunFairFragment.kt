package college.wyk.app.ui.funfair

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import college.wyk.app.R
import college.wyk.app.commons.landingActivity
import college.wyk.app.model.funfair.FunFairData
import com.onlylemi.mapview.library.MapView
import com.onlylemi.mapview.library.MapViewListener
import com.onlylemi.mapview.library.layer.MarkLayer
import kotlinx.android.synthetic.main.fragment_funfair.*
import java.io.IOException

class FunFairFragment : Fragment() {

    lateinit var mapView: MapView
    lateinit var markLayer: MarkLayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_funfair, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Fun Fair Map"
        landingActivity.updateToolbar(toolbar)

        mapView = custom_map_view

        val bitmap = BitmapFactory.decodeStream(resources.assets.open("funfair/G.png"))
        mapView.loadMap(bitmap)
        mapView.setMinZoom(0.2f)
        mapView.setMaxZoom(1.4f)
        mapView.setMapViewListener(object : MapViewListener {

            override fun onMapLoadSuccess() = onMapLoaded()

            override fun onMapLoadFail() = Unit

        })
    }

    fun onMapLoaded() {
        val marks = FunFairData.marks.map { it.first }
        val markNames = FunFairData.marks.map { it.second }
        markLayer = MarkLayer(mapView, marks, markNames)
        markLayer.setMarkIsClickListener({ index ->
            Log.i("WYK", "$index")
        })
        mapView.addLayer(markLayer)
        mapView.currentRotateDegrees = 0f
        mapView.refresh()

        toolbar.title = "Fun Fair Map"
        landingActivity.updateToolbar(toolbar)
    }

}