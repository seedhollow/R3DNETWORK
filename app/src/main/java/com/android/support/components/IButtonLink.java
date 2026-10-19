package com.android.support.components;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class IButtonLink {

    private final Ui ui;

    public IButtonLink(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        LinearLayout card = ui.card(true, true);
        card.addView(ui.label(item.name, null), ui.weight());
        IconView icon = ui.icon("external", 16, Theme.TEXT_DIM);
        ((LinearLayout.LayoutParams) icon.getLayoutParams()).leftMargin = ui.dp(12);
        card.addView(icon);

        String url = item.description;
        card.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ui.context.startActivity(intent);
            } catch (Exception e) {
                Log.e("R3DNetwork", "Cannot open link: " + url, e);
            }
        });
        return card;
    }
}
