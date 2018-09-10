package hq.demo.net.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by bull on 2018/9/7.
 */

public class FileHelper {


    public static String getFileNameFromUrl(String url) {
        return "" + System.currentTimeMillis();
    }

    private static String picSavePath;
    public static String getPicSavePath(Context context) {
        if (!TextUtils.isEmpty(picSavePath)) {
            return picSavePath;
        }
        picSavePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + getPicSaveRootPath(context)
                + File.separator;
        File file = new File(picSavePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return picSavePath;
    }

    public static String getPicSaveRootPath(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("PicSavePath", "retrofit-image");
    }
}
