package college.wyk.app.ui.settings

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.widget.LinearLayout
import college.wyk.app.R

class SettingsActivity : AppCompatPreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = findViewById(android.R.id.list).parent.parent.parent as LinearLayout
        val toolbar = LayoutInflater.from(this).inflate(R.layout.toolbar, root, false) as Toolbar
        toolbar.title = "Settings"
        root.addView(toolbar, 0)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        addPreferencesFromResource(R.xml.preferences)
    }

}
