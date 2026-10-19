package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ISlider {

    private final Ui ui;

    public ISlider(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        int min = item.min != null ? item.min : 0;
        int max = item.max != null ? item.max : 100;

        LinearLayout card = ui.card(false, false);
        card.setPadding(card.getPaddingLeft(), 0, card.getPaddingRight(), ui.dp(10));

        LinearLayout row = ui.row();
        row.setMinimumHeight(ui.dp(40));
        row.addView(ui.label(item.name, item.description), ui.weight());
        TextView value = ui.text("", Theme.TEXT_BODY, Theme.ACCENT, ui.medium);
        row.addView(value, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        card.addView(row);

        SliderView slider = new SliderView(ui.context, min, max);
        card.addView(slider, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        int saved = Preferences.loadPrefInt(item.name, item.id);
        slider.setValue(saved == 0 ? min : saved);
        value.setText(String.valueOf(slider.getValue()));
        slider.setOnChangeListener(v -> {
            value.setText(String.valueOf(v));
            Preferences.changeFeatureInt(item.name, item.id, v);
        });
        return card;
    }
}
