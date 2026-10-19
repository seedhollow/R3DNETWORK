package com.android.support.components;

import android.text.InputType;
import android.view.View;

import com.android.support.Preferences;
import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Obfuscate
public class IInputFloat {

    private final IInputText input;

    public IInputFloat(IInputText input) {
        this.input = input;
    }

    public View build(FeatureEntity item) {
        float max = item.maxFloat != null ? item.maxFloat : 0f;
        int decimals = item.decimals != null ? Math.max(0, item.decimals) : 2;
        float saved = Preferences.loadPrefFloat(item.name, item.id);
        int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_NUMBER_FLAG_DECIMAL;

        return input.row(item, format(saved, decimals), type, "0.0", 16, raw -> {
            float value;
            try {
                // some keyboards type a comma as the decimal separator
                String text = raw.replace(',', '.');
                value = text.isEmpty() || text.equals("-") || text.equals(".") || text.equals("-.") ? 0f : Float.parseFloat(text);
            } catch (NumberFormatException e) {
                value = 0f;
            }
            if (Float.isNaN(value) || Float.isInfinite(value)) value = 0f;
            if (max != 0f && value > max) value = max;
            value = round(value, decimals);
            Preferences.changeFeatureFloat(item.name, item.id, value);
            return format(value, decimals);
        });
    }

    private static float round(float value, int decimals) {
        return new BigDecimal(Float.toString(value)).setScale(decimals, RoundingMode.HALF_UP).floatValue();
    }

    /** At most `decimals` digits after the point, without trailing zeros: 1.5, 2, 0.25 */
    private static String format(float value, int decimals) {
        BigDecimal rounded = new BigDecimal(Float.toString(value)).setScale(decimals, RoundingMode.HALF_UP).stripTrailingZeros();
        return rounded.signum() == 0 ? "0" : rounded.toPlainString();
    }
}
