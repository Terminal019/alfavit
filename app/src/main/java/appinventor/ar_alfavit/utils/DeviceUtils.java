package appinventor.ar_alfavit.utils;

import android.content.Context;

import appinventor.ar_alfavit.R;


public class DeviceUtils {

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

}