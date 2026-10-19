package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class IRadioButton {

    private final Ui ui;

    public IRadioButton(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        if (item.options == null || item.options.length == 0) return null;
        String[] options = item.options;
        int selected = Math.max(0, Math.min(options.length - 1, Preferences.loadPrefInt(item.name, item.id)));

        LinearLayout card = ui.card(false, false);
        card.setPadding(card.getPaddingLeft(), 0, card.getPaddingRight(), ui.dp(6));

        LinearLayout header = ui.row();
        header.setMinimumHeight(ui.dp(40));
        header.addView(ui.label(item.name, item.description), ui.weight());
        card.addView(header);

        MarkView[] marks = new MarkView[options.length];
        TextView[] labels = new TextView[options.length];
        for (int i = 0; i < options.length; i++) {
            final int index = i;
            LinearLayout option = new LinearLayout(ui.context);
            option.setOrientation(LinearLayout.HORIZONTAL);
            option.setGravity(Gravity.CENTER_VERTICAL);
            option.setMinimumHeight(ui.dp(36));
            option.setClickable(true);

            marks[i] = new MarkView(ui.context, true);
            LinearLayout.LayoutParams markParams = new LinearLayout.LayoutParams(ui.dp(18), ui.dp(18));
            markParams.rightMargin = ui.dp(12);
            option.addView(marks[i], markParams);

            labels[i] = ui.text(Ui.html(options[i]), Theme.TEXT_BODY, Theme.TEXT, ui.regular);
            option.addView(labels[i], ui.weight());

            option.setOnClickListener(v -> {
                paint(marks, labels, index);
                Preferences.changeFeatureInt(item.name, item.id, index);
            });
            card.addView(option, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        paint(marks, labels, selected);
        return card;
    }

    private static void paint(MarkView[] marks, TextView[] labels, int selected) {
        for (int i = 0; i < marks.length; i++) {
            marks[i].setChecked(i == selected);
            labels[i].setTextColor(i == selected ? Theme.TEXT : Theme.TEXT_DIM);
        }
    }
}
