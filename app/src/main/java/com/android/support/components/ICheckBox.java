package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.support.Preferences;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ICheckBox {

    private final Context context;
    private final Typeface typeface;

    public ICheckBox(Context context, Typeface typeface) {
        this.context = context;
        this.typeface = typeface;
    }

    public void add(LinearLayout mContent, final int featNum, final String featName, final String featDesc, boolean switchedOn) {
        LinearLayout mLayoutContainer = new LinearLayout(context);
        mLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLayoutContainer.setPadding(10, 10, 10, 10);
        mLayoutContainer.setBackgroundColor(Color.parseColor("#00000000"));
        mLayoutContainer.setGravity(Gravity.CENTER_VERTICAL);

        // ========== Left text area ==========
        LinearLayout mTextContainer = new LinearLayout(context);
        mTextContainer.setOrientation(LinearLayout.VERTICAL);
        mTextContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        // Feature title
        final TextView textView = new TextView(context);
        textView.setText(Html.fromHtml(featName));
        textView.setTextColor(Colors.TEXT_COLOR_2);
        textView.setTypeface(typeface);
        textView.setTextSize(15);
        mTextContainer.addView(textView);

        // Optional description below the title
        if (featDesc != null && !featDesc.isEmpty()) {
            final TextView descView = new TextView(context);
            descView.setText(Html.fromHtml(featDesc));
            descView.setTextColor(Colors.TEXT_COLOR_2);
            descView.setTypeface(typeface);
            descView.setTextSize(12);
            descView.setPadding(0, 5, 0, 5);
            mTextContainer.addView(descView);
        }

        // ========== Checkbox ==========
        final CheckBox checkBox = new CheckBox(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        layoutParams.setMargins(10, 0, 0, 0);
        checkBox.setLayoutParams(layoutParams);
        checkBox.setTextColor(Colors.TEXT_COLOR_2);
        checkBox.setButtonTintList(ColorStateList.valueOf(Colors.CheckBoxColor));

        boolean isOn = Preferences.loadPrefBool(featName, featNum, switchedOn);
        checkBox.setChecked(isOn);
        mLayoutContainer.setBackgroundColor(isOn ? Color.parseColor("#40EC4857") : Color.parseColor("#00000000"));

        mLayoutContainer.setOnClickListener(v -> {
            checkBox.performClick();
        });

        checkBox.setOnCheckedChangeListener((v, isChecked) -> {
            Preferences.changeFeatureBool(featName, featNum, isChecked);
            mLayoutContainer.setBackgroundColor(isChecked ? Color.parseColor("#40EC4857") : Color.parseColor("#00000000"));
        });

        // Add to main container
        mLayoutContainer.addView(mTextContainer);
        mLayoutContainer.addView(checkBox);
        mContent.addView(mLayoutContainer);
    }
}
