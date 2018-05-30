package cn.edu.tsinghua.cs.energytool;

import android.app.Application;
import android.content.Context;

/**
 * EnergyToolApplication
 * Created by zhangdi on 6/23/15.
 */
public class EnergyToolApplication extends Application {
    private static Context context;

    public static Context getAppContext() {
        return EnergyToolApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EnergyToolApplication.context = getApplicationContext();
    }
}
