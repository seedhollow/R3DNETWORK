package com.android.support.components;

import android.text.InputType;
import android.view.View;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class IInputInt {

    private final IInputText input;

    public IInputInt(IInputText input) {
        this.input = input;
    }

    public View build(FeatureEntity item) {
        int max = item.max != null ? item.max : 0;
        int saved = Preferences.loadPrefInt(item.name, item.id);
        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
        return input.row(item, String.valueOf(saved), type, "0", 10, raw -> {
            int value;
            try {
                value = raw.isEmpty() || raw.equals("-") ? 0 : Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                value = max != 0 ? max : Integer.MAX_VALUE; // more than 10 digits
            }
            if (max != 0 && value > max) value = max;
            Preferences.changeFeatureInt(item.name, item.id, value);
            return String.valueOf(value);
        });
    }
}
