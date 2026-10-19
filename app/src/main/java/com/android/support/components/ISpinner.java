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
public class ISpinner {

    private final Ui ui;

    public ISpinner(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        if (item.options == null || item.options.length == 0) return null;
        String[] options = item.options;
        int[] selected = {clamp(Preferences.loadPrefInt(item.name, item.id), options.length)};

        LinearLayout card = ui.card(false, false);

        LinearLayout header = ui.row();
        header.setClickable(true);
        header.addView(ui.label(item.name, item.description), ui.weight());
        TextView value = ui.text(Ui.html(options[selected[0]]), Theme.TEXT_BODY, Theme.TEXT_DIM, ui.regular);
        header.addView(value, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        IconView chevron = ui.icon("chevron-down", 14, Theme.TEXT_DIM);
        ((LinearLayout.LayoutParams) chevron.getLayoutParams()).leftMargin = ui.dp(8);
        header.addView(chevron);
        card.addView(header);

        LinearLayout list = new LinearLayout(ui.context);
        list.setOrientation(LinearLayout.VERTICAL);
        list.setVisibility(View.GONE);
        list.setPadding(0, 0, 0, ui.dp(6));
        card.addView(list, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));

        View divider = new View(ui.context);
        divider.setBackgroundColor(Theme.DIVIDER);
        list.addView(divider, new LinearLayout.LayoutParams(MATCH_PARENT, Math.max(1, ui.dp(0.7f))));

        TextView[] labels = new TextView[options.length];
        IconView[] checks = new IconView[options.length];
        for (int i = 0; i < options.length; i++) {
            final int index = i;
            LinearLayout option = new LinearLayout(ui.context);
            option.setOrientation(LinearLayout.HORIZONTAL);
            option.setGravity(Gravity.CENTER_VERTICAL);
            option.setMinimumHeight(ui.dp(40));
            option.setClickable(true);

            labels[i] = ui.text(Ui.html(options[i]), Theme.TEXT_BODY, Theme.TEXT, ui.regular);
            option.addView(labels[i], ui.weight());
            checks[i] = ui.icon("check", 16, Theme.ACCENT);
            option.addView(checks[i]);

            option.setOnClickListener(v -> {
                selected[0] = index;
                paint(labels, checks, index);
                value.setText(Ui.html(options[index]));
                list.setVisibility(View.GONE);
                chevron.setRotation(0);
                Preferences.changeFeatureInt(item.name, item.id, index);
            });
            list.addView(option, new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        paint(labels, checks, selected[0]);

        header.setOnClickListener(v -> {
            boolean open = list.getVisibility() != View.VISIBLE;
            list.setVisibility(open ? View.VISIBLE : View.GONE);
            chevron.animate().rotation(open ? 180 : 0).setDuration(150).start();
        });
        return card;
    }

    private static void paint(TextView[] labels, IconView[] checks, int selected) {
        for (int i = 0; i < labels.length; i++) {
            boolean on = i == selected;
            labels[i].setTextColor(on ? Theme.ACCENT : Theme.TEXT);
            checks[i].setVisibility(on ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private static int clamp(int index, int count) {
        return Math.max(0, Math.min(count - 1, index));
    }
}
