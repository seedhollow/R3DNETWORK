package com.android.support;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class BaseMenu {

    public static final String TAG = "R3DNetwork"; //Tag for logcat
    Typeface typeface;
    Context getContext;

    public BaseMenu(Context context) {
        getContext = context;
        Preferences.context = context;
        InitTypeFace(getContext);
    }

    protected native byte[] LoadFontData(Context context);

    protected native String GetFeatureList();
    private void InitTypeFace(Context context){
        typeface = Typeface.DEFAULT; //Init to default, if errors occurs, prevent crashes
        byte[] fontData = LoadFontData(context);
        if (fontData != null) {
            File tempFontFile = null;
            try {
                tempFontFile = File.createTempFile("font_file", ".ttf", context.getCacheDir());
                try (FileOutputStream fos = new FileOutputStream(tempFontFile)) {
                    fos.write(fontData);
                }
                if (tempFontFile.exists()) {
                    typeface = Typeface.createFromFile(tempFontFile);
                } else {
                    Log.e(TAG, "Failed to create or find font file.");
                }
            } catch (IOException e) {
                Log.e(TAG, "Error occurred loading Font,Default will be used",e);
            } finally {
                if (tempFontFile != null && tempFontFile.exists()) {
                    tempFontFile.delete();
                }
            }
        }
    }

    public void ShowMenu() {

    }

    public void KillMenu() {

    }

    public void onDestroy() {
        KillMenu();
    }
}
