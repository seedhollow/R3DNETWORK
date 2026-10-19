package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;


@Obfuscate
public class IInputText {

    interface Commit {
        /** Returns the text to display after committing, e.g. a clamped number. */
        String apply(String raw);
    }

    private final Ui ui;

    public IInputText(Ui ui) {
        this.ui = ui;
    }

    public View build(FeatureEntity item) {
        String saved = Preferences.loadPrefString(item.name, item.id);
        return row(item, saved, InputType.TYPE_CLASS_TEXT, "Tap to edit", 64, raw -> {
            Preferences.changeFeatureString(item.name, item.id, raw);
            return raw;
        });
    }

    View row(FeatureEntity item, String initial, int inputType, String hint, int maxLength, Commit commit) {
        LinearLayout card = ui.card(true, true);
        card.addView(ui.label(item.name, item.description), ui.weight());

        EditText field = new EditText(ui.context) {
            @Override
            public boolean onKeyPreIme(int keyCode, KeyEvent event) {
                // Back closes the keyboard, also leave edit mode so the overlay gives focus back to the game
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    ui.host.endEdit();
                }
                return super.onKeyPreIme(keyCode, event);
            }
        };
        field.setBackground(null);
        field.setPadding(0, 0, 0, 0);
        field.setSingleLine(true);
        field.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        field.setTextSize(TypedValue.COMPLEX_UNIT_SP, Theme.TEXT_BODY);
        field.setTextColor(Theme.TEXT_DIM);
        field.setHintTextColor(Theme.TEXT_FAINT);
        field.setTypeface(ui.regular);
        field.setHint(hint);
        field.setMaxWidth(ui.dp(140));
        field.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        field.setInputType(inputType);
        field.setImeOptions(EditorInfo.IME_ACTION_DONE | EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        field.setFocusable(false);
        field.setText(initial == null ? "" : initial);

        LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        fieldParams.leftMargin = ui.dp(12);
        card.addView(field, fieldParams);

        IconView pencil = ui.icon("edit", 14, Theme.TEXT_FAINT);
        ((LinearLayout.LayoutParams) pencil.getLayoutParams()).leftMargin = ui.dp(8);
        card.addView(pencil);

        String[] committed = {field.getText().toString()};
        Runnable save = () -> {
            String raw = field.getText().toString();
            if (raw.equals(committed[0])) return;
            String shown = commit.apply(raw);
            committed[0] = shown;
            if (!shown.equals(raw)) field.setText(shown);
        };

        View.OnClickListener edit = v -> {
            pencil.setColor(Theme.ACCENT);
            field.setTextColor(Theme.TEXT);
            ui.host.beginEdit(field);
        };
        card.setOnClickListener(edit);
        field.setOnClickListener(edit);

        field.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) return;
            pencil.setColor(Theme.TEXT_FAINT);
            field.setTextColor(Theme.TEXT_DIM);
            save.run();
        });
        field.setOnEditorActionListener((v, actionId, event) -> {
            save.run();
            ui.host.endEdit();
            return true;
        });
        return card;
    }
}
