package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ICategory {

    private final Ui ui;

    public ICategory(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        return build(item.name);
    }

    public View build(String name) {
        TextView text = ui.text(Ui.html(name), Theme.TEXT_SMALL, Theme.TEXT_DIM, ui.medium);
        text.setLetterSpacing(0.04f);
        text.setPadding(ui.dp(4), ui.dp(10), ui.dp(4), 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.bottomMargin = ui.dp(8);
        text.setLayoutParams(params);
        return text;
    }
}
