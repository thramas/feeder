package com.pukingminion.feeder;

import android.content.Context;
import android.os.Handler;

/**
 * Created by samarth on 12/23/16.
 */

public class MyApplication{

    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;
    public static Context mContext;

    public MyApplication() {
        super();
    }

    public static void initContexualData(Context context,Context present) {
        applicationContext = context;
        applicationHandler = new Handler(MyApplication.applicationContext.getMainLooper());
        mContext = present;
    }
}
