package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.support.Preferences;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ISwitch {

    private final Context context;
    private final Typeface typeface;

    public ISwitch(Context context, Typeface typeface) {
        this.context = context;
        this.typeface = typeface;
    }

    // Added featDesc parameter
    public void add(LinearLayout mContent, final int featNum, final String featName, final String featDesc, boolean swiOn) {

        // ======= Container Layout =======
        LinearLayout mLayoutContainer = new LinearLayout(context);
        mLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLayoutContainer.setPadding(10, 10, 10, 10);
        mLayoutContainer.setBackgroundColor(Color.parseColor("#00000000"));
        mLayoutContainer.setGravity(Gravity.CENTER_VERTICAL);

        // ======= Text Area (title + desc) =======
        LinearLayout mTextContainer = new LinearLayout(context);
        mTextContainer.setOrientation(LinearLayout.VERTICAL);
        mTextContainer.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f));

        // Feature Title
        final TextView textView = new TextView(context);
        textView.setText(Html.fromHtml(featName));
        textView.setTextColor(Colors.TEXT_COLOR_2);
        textView.setTypeface(typeface);
        textView.setTextSize(15);
        mTextContainer.addView(textView);

        // Optional Description
        if (featDesc != null && !featDesc.isEmpty()) {
            final TextView descView = new TextView(context);
            descView.setText(Html.fromHtml(featDesc));
            descView.setTypeface(typeface);
            descView.setTextColor(Colors.TEXT_COLOR_2);
            descView.setTextSize(12);
            descView.setPadding(0, 5, 0, 5);
            mTextContainer.addView(descView);
        }

        // ======= Switch =======
        final Switch mSwitch = new Switch(context);
        LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        switchParams.setMargins(30, 0, 0, 0);
        mSwitch.setLayoutParams(switchParams);
        mSwitch.setTextColor(Colors.TEXT_COLOR_2);
        mSwitch.setTypeface(typeface);

        // Set switch color states
        ColorStateList buttonStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        Color.BLUE,
                        Colors.ToggleON,
                        Colors.ToggleOFF
                }
        );

        try {
            mSwitch.getThumbDrawable().setTintList(buttonStates);
            mSwitch.getTrackDrawable().setTintList(buttonStates);
        } catch (NullPointerException ex) {
            Log.d("R3D NETWORK ID", String.valueOf(ex));
        }

        boolean isOn = Preferences.loadPrefBool(featName, featNum, swiOn);
        mSwitch.setChecked(isOn);
        mLayoutContainer.setBackgroundColor(isOn ? Color.parseColor("#40EC4857") : Color.parseColor("#00000000"));

        // ======= Behavior =======
        mLayoutContainer.setOnClickListener(v -> mSwitch.performClick());

        mSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            Preferences.changeFeatureBool(featName, featNum, isChecked);
            mLayoutContainer.setBackgroundColor(isChecked ? Color.parseColor("#40EC4857") : Color.parseColor("#00000000"));
        });

        // ======= Add views =======
        mLayoutContainer.addView(mTextContainer);
        mLayoutContainer.addView(mSwitch);
        mContent.addView(mLayoutContainer);
    }
}