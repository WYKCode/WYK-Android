package college.wyk.app.ui.funfair

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
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
import com.onlylemi.mapview.library.layer.MapBaseLayer
import com.onlylemi.mapview.library.layer.MarkLayer
import kotlinx.android.synthetic.main.fragment_funfair.*

class FunFairFragment : Fragment() {

    lateinit var mapView: MapView
    lateinit var markLayer: MarkLayer

    var currentFloor = 1
    val floorNames = listOf("LG", "G/F", "1/F", "2/F")
    val floorFiles = listOf("LG.png", "G.png", "1.png", "2.png")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_funfair, container, false)

        mapView = view.findViewById(R.id.custom_map_view) as MapView
        loadMap(mapView, currentFloor)

        return view
    }

    fun loadMap(mapView: MapView, floor: Int) {

        description_card?.visibility = View.GONE

        val bitmap = BitmapFactory.decodeStream(resources.assets.open("funfair/" + floorFiles[floor]))
        mapView.loadMap(bitmap)
        mapView.setMinZoom(1.1f)
        mapView.setMaxZoom(2.1f)
        mapView.setMapViewListener(object : MapViewListener {

            override fun onMapLoadSuccess() = onMapLoaded(mapView)

            override fun onMapLoadFail() = Unit

        })
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = "Floor Plan: ${floorNames[currentFloor]}"
        landingActivity.updateToolbar(toolbar)

        toolbar.inflateMenu(R.menu.funfair)
        toolbar.setOnMenuItemClickListener { item ->
            when {
                item.itemId == R.id.switch_floor -> {
                    newThemedDialog(context)
                            .title("Switch floor")
                            .items(floorNames)
                            .itemsCallbackSingleChoice(currentFloor) { dialog: MaterialDialog, view: View, which: Int, text: CharSequence ->
                                currentFloor = which
                                toolbar.title = "Floor Plan: ${floorNames[currentFloor]}"
                                loadMap(mapView, currentFloor)
                                true
                            }
                            .positiveText("Browse")
                            .show()
                    true
                }
                /*item.itemId == R.id.legend -> {
                    *//*newThemedDialog(context)
                            .title("Input")
                            .input("Coordinate...", null, false) { materialDialog: MaterialDialog, charSequence: CharSequence ->
                                var x: Float = 0f
                                var y: Float = 0f
                                charSequence.split(",").let { x = it[0].toFloat(); y = it[1].toFloat() }
                                mapView.translate(x, y)
                                mapView.refresh()
                            }
                            .positiveText("Go")
                            .show()*//*
                    true
                }*/
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    fun onMapLoaded(mapView: MapView) {

        val marks = FunFairData.marks[currentFloor].map { it.first }
        val markNames = FunFairData.marks[currentFloor].map { it.second }
        markLayer = MarkLayer(mapView, marks, markNames)
        markLayer.setMarkIsClickListener { index ->
            location_description.text = FunFairData.marks[currentFloor][index].second
            location_description.alpha = 1f
            description_card.visibility = View.VISIBLE
            mapView.mapCenterWithPoint(FunFairData.marks[currentFloor][index].first.x, FunFairData.marks[currentFloor][index].first.y)
            mapView.refresh()
        }
        markLayer.setMarkUnClickListener {
            location_description.text = "Select a marker..."
            location_description.alpha = .7f
            description_card.visibility = View.GONE
        }
        val removals = mutableListOf<MapBaseLayer>()
        mapView.layers.filter { it is MarkLayer }.forEach { removals += it }
        mapView.layers -= removals
        mapView.addLayer(markLayer)
        mapView.currentZoom = 1.3f
        mapView.refresh()

        activity.runOnUiThread {
            Handler().postDelayed({
                mapView.currentZoom = 1.3f
                mapView.refresh()
                activity.runOnUiThread {
                    Handler().postDelayed({
                        mapView.currentRotateDegrees = 0f
                        mapView.translate(FunFairData.centers[currentFloor].x, FunFairData.centers[currentFloor].y)
                        mapView.refresh()
                    }, 5)
                }
            }, 5)
        }
    }

}