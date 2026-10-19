package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ITextView {

    private final Ui ui;

    public ITextView(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        TextView text = ui.text(Ui.html(item.description), Theme.TEXT_SMALL + 0.5f, Theme.TEXT_DIM, ui.regular);
        text.setLineSpacing(ui.dp(3), 1f);
        text.setPadding(ui.dp(4), ui.dp(4), ui.dp(4), 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.bottomMargin = ui.dp(12);
        text.setLayoutParams(params);
        return text;
    }
}
