package college.wyk.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.HashMap;

import college.wyk.app.model.sns.SnsStack;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class WykApplication extends MultiDexApplication {

    public static WykApplication instance;
    public HashMap<String, SnsStack> snsStacks = new HashMap<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

}
