package college.wyk.app.ui.funfair

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import college.wyk.app.R
import college.wyk.app.commons.landingActivity
import college.wyk.app.commons.newThemedDialog
import college.wyk.app.model.funfair.FunFairData
import com.afollestad.materialdialogs.MaterialDialog
import com.onlylemi.mapview.library.MapView
import com.onlylemi.mapview.library.MapViewListener
import com.onlylemi.mapview.library.layer.MarkLayer
import kotlinx.android.synthetic.main.fragment_funfair.*

class FunFairFragment : Fragment() {

    lateinit var mapView: MapView
    lateinit var markLayer: MarkLayer

    var currentFloor = 1
    val floorNames = listOf("LG", "G/F", "1/F", "2/F")
    val floorFiles = listOf("LG.png", "G.png", "G.png", "G.png")

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

        toolbar.title = "Floor Plan: G/F"
        landingActivity.updateToolbar(toolbar)

        // toolbar.inflateMenu(R.menu.funfair)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.switch_floor) {
                newThemedDialog(context)
                        .title("Switch floor")
                        .items(floorNames)
                        .itemsCallbackSingleChoice(currentFloor) { dialog: MaterialDialog, view: View, which: Int, text: CharSequence ->
                            currentFloor = which
                            toolbar.title = "Floor Plan: ${floorNames[currentFloor]}"
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
            location_description.alpha = 1f
            description_card.visibility = View.VISIBLE
        }
        markLayer.setMarkUnClickListener {
            location_description.text = "Select a marker..."
            location_description.alpha = .7f
            description_card.visibility = View.GONE
        }
        mapView.addLayer(markLayer)
        mapView.mapCenterWithPoint(580f / 8f, 724f / 4f)
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