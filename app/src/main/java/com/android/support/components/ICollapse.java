package com.android.support.components;

import android.view.View;
import android.widget.LinearLayout;

import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

import java.util.ArrayList;

@Obfuscate
public class ICollapse {

    private final Ui ui;

    public ICollapse(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        LinearLayout card = ui.card(true, true);
        card.addView(ui.label(item.name, item.description), ui.weight());
        IconView chevron = ui.icon("chevron-right", 16, Theme.TEXT_DIM);
        ((LinearLayout.LayoutParams) chevron.getLayoutParams()).leftMargin = ui.dp(12);
        card.addView(chevron);

        // the children list identifies the sub-page in Menu's page cache, so it must be the same instance every time
        if (item.children == null) item.children = new ArrayList<>();
        card.setOnClickListener(v -> ui.host.openPage(item.name, item.children));
        return card;
    }
}
