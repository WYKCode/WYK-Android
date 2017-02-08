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
import android.widget.Toast
import college.wyk.app.R
import college.wyk.app.commons.landingActivity
import college.wyk.app.commons.newThemedDialog
import college.wyk.app.model.funfair.FunFairData
import com.onlylemi.mapview.library.MapView
import com.onlylemi.mapview.library.MapViewListener
import com.onlylemi.mapview.library.layer.MarkLayer
import kotlinx.android.synthetic.main.fragment_funfair.*
import java.io.IOException
import android.R.menu
import android.os.Handler
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog




class FunFairFragment : Fragment() {

    lateinit var mapView: MapView
    lateinit var markLayer: MarkLayer

    var currentFloor = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_funfair, container, false)

        mapView = view.findViewById(R.id.custom_map_view) as MapView

        val bitmap = BitmapFactory.decodeStream(resources.assets.open("funfair/G.png"))
        mapView.loadMap(bitmap)
        mapView.setMinZoom(1.1f)
        mapView.setMaxZoom(1.8f)
        mapView.setMapViewListener(object : MapViewListener {

            override fun onMapLoadSuccess() = onMapLoaded()

            override fun onMapLoadFail() = Unit

        })

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Fun Fair Map"
        landingActivity.updateToolbar(toolbar)

        toolbar.inflateMenu(R.menu.funfair)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.switch_floor) {
                MaterialDialog.Builder(context)
                        .title("Switch floor")
                        .items(listOf("LG", "G/F", "1/F", "2/F"))
                        .itemsCallbackSingleChoice(currentFloor) { dialog: MaterialDialog, view: View, which: Int, text: CharSequence ->
                            currentFloor = which
                            Log.i("WYK", "$currentFloor")
                            true
                        }
                        .positiveText("Browse")
                        .show()
                true
            } else {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun onMapLoaded() {
        val marks = FunFairData.marks.map { it.first }
        val markNames = FunFairData.marks.map { it.second }
        markLayer = MarkLayer(mapView, marks, markNames)
        markLayer.setMarkIsClickListener { index ->
            location_description.text = FunFairData.marks[index].second
        }
        markLayer.setMarkUnClickListener {
            location_description.text = "Unselected"
        }
        mapView.addLayer(markLayer)
        mapView.setCurrentRotateDegrees(0f, 1015f, 790f)
        mapView.currentZoom = 1.3f
        mapView.refresh()


        activity.runOnUiThread {
            Handler().postDelayed({
                mapView.currentZoom = 1.3f
                mapView.refresh()
            }, 5)
        }
    }

}