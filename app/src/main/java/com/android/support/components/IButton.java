package com.android.support.components;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class IButton {

    private final Ui ui;

    public IButton(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        LinearLayout card = ui.card(true, true);
        card.setGravity(Gravity.CENTER);
        TextView label = ui.text(Ui.html(item.name), Theme.TEXT_BODY, Theme.ACCENT, ui.medium);
        card.addView(label);
        card.setOnClickListener(v -> Preferences.changeFeatureInt(item.name, item.id, 0));
        return card;
    }
}
