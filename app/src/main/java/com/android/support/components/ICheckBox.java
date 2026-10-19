package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.view.View;
import android.widget.LinearLayout;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ICheckBox {

    private final Ui ui;

    public ICheckBox(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        LinearLayout card = ui.card(true, true);
        card.addView(ui.label(item.name, item.description), ui.weight());

        MarkView mark = new MarkView(ui.context, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        params.leftMargin = ui.dp(12);
        card.addView(mark, params);

        mark.setChecked(Preferences.loadPrefBool(item.name, item.id, item.enabled));
        card.setOnClickListener(v -> {
            mark.setChecked(!mark.isChecked());
            Preferences.changeFeatureBool(item.name, item.id, mark.isChecked());
        });
        return card;
    }
}
