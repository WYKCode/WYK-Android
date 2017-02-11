package college.wyk.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import college.wyk.app.model.sns.SnsStack
import java.util.*

class WykApplicationKotlin : Application() {

    var snsStacks = HashMap<String, SnsStack>()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }

    companion object {

        lateinit var instance: WykApplication

    }

}