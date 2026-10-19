package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.view.View;
import android.widget.LinearLayout;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ISwitch {

    private final Ui ui;

    public ISwitch(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        LinearLayout card = ui.card(true, true);
        card.addView(ui.label(item.name, item.description), ui.weight());

        ToggleView toggle = new ToggleView(ui.context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.leftMargin = ui.dp(12);
        card.addView(toggle, params);

        toggle.setChecked(Preferences.loadPrefBool(item.name, item.id, item.enabled));
        toggle.setOnChangeListener(checked -> Preferences.changeFeatureBool(item.name, item.id, checked));
        card.setOnClickListener(v -> toggle.toggle());
        return card;
    }
}
