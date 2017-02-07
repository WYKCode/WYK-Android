package college.wyk.app

import android.app.Application
import college.wyk.app.model.sns.SnsStack
import com.facebook.drawee.backends.pipeline.Fresco
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import java.util.*

class WykApplication : Application() {

    var snsStacks = HashMap<String, SnsStack>()

    override fun onCreate() {
        super.onCreate()
        instance = this
        Fresco.initialize(this)
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    companion object {

        lateinit var instance: WykApplication

    }

}