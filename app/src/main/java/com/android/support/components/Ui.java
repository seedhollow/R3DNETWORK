package com.android.support.components;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.support.entity.FeatureEntity;

import org.lsposed.lsparanoid.Obfuscate;

import java.util.List;


@Obfuscate
public class Ui {

    public interface Host {
        void openPage(String title, List<FeatureEntity> children);

        void beginEdit(EditText editText);

        void endEdit();
    }

    public final Context context;
    public final Host host;
    public final Typeface regular;
    public final Typeface medium;
    public final Typeface bold;

    public Ui(Context context, Typeface customFont, Host host) {
        this.context = context;
        this.host = host;
        if (Theme.USE_CUSTOM_FONT && customFont != null) {
            regular = customFont;
            medium = Typeface.create(customFont, Typeface.BOLD);
            bold = medium;
        } else {
            regular = Typeface.create("sans-serif", Typeface.NORMAL);
            medium = Typeface.create("sans-serif-medium", Typeface.NORMAL);
            bold = Typeface.create("sans-serif", Typeface.BOLD);
        }
    }

    public int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    @SuppressWarnings("deprecation")
    public static Spanned html(String text) {
        if (text == null) text = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        }
        return Html.fromHtml(text);
    }

    public GradientDrawable round(int color, float radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    public Drawable cardBackground(boolean pressable) {
        if (!pressable) return round(Theme.CARD_BG, Theme.CARD_RADIUS);
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, round(Theme.CARD_PRESSED, Theme.CARD_RADIUS));
        states.addState(new int[]{}, round(Theme.CARD_BG, Theme.CARD_RADIUS));
        return states;
    }

    public TextView text(CharSequence value, float sizeSp, int color, Typeface typeface) {
        TextView textView = new TextView(context);
        textView.setText(value);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
        textView.setTextColor(color);
        textView.setTypeface(typeface);
        textView.setIncludeFontPadding(false);
        return textView;
    }

    /** A rounded card. Horizontal cards are single rows, vertical cards hold a header row plus extra content. */
    public LinearLayout card(boolean horizontal, boolean pressable) {
        LinearLayout card = new LinearLayout(context);
        card.setOrientation(horizontal ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        card.setBackground(cardBackground(pressable));
        card.setMinimumHeight(dp(Theme.CARD_HEIGHT));
        card.setPadding(dp(Theme.CARD_PADDING), 0, dp(Theme.CARD_PADDING), 0);
        if (horizontal) card.setGravity(Gravity.CENTER_VERTICAL);
        card.setLayoutParams(cardParams());
        card.setClickable(pressable);
        return card;
    }

    public LinearLayout.LayoutParams cardParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        params.bottomMargin = dp(Theme.CARD_GAP);
        return params;
    }

    /** A full-height row used as the first line of vertical cards. */
    public LinearLayout row() {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setMinimumHeight(dp(Theme.CARD_HEIGHT));
        return row;
    }

    public LinearLayout.LayoutParams weight() {
        return new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f);
    }

    public IconView icon(String name, int sizeDp, int color) {
        IconView icon = new IconView(context, name);
        icon.setColor(color);
        icon.setLayoutParams(new LinearLayout.LayoutParams(dp(sizeDp), dp(sizeDp)));
        return icon;
    }

    /**
     * Builds the left side of a row: the label, an optional "?" badge, and the
     * description that the badge reveals. Returned view should take the row's weight.
     */
    public LinearLayout label(String name, String description) {
        LinearLayout column = new LinearLayout(context);
        column.setOrientation(LinearLayout.VERTICAL);
        column.setPadding(0, dp(12), 0, dp(12));

        LinearLayout line = new LinearLayout(context);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = text(html(name), Theme.TEXT_BODY, Theme.TEXT, regular);
        line.addView(title, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0f));
        column.addView(line);

        if (description != null && !description.isEmpty()) {
            TextView desc = text(html(description), Theme.TEXT_SMALL, Theme.TEXT_DIM, regular);
            desc.setLineSpacing(dp(2), 1f);
            desc.setVisibility(View.GONE);
            LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            descParams.topMargin = dp(2);
            descParams.bottomMargin = dp(6);
            column.addView(desc, descParams);

            IconView help = icon("help", 15, Theme.TEXT_DIM);
            LinearLayout.LayoutParams helpParams = new LinearLayout.LayoutParams(dp(15 + 12), dp(15 + 12));
            help.setLayoutParams(helpParams);
            help.setPadding(dp(6), dp(6), dp(6), dp(6));
            help.setOnClickListener(v -> {
                boolean show = desc.getVisibility() != View.VISIBLE;
                desc.setVisibility(show ? View.VISIBLE : View.GONE);
                help.setColor(show ? Theme.ACCENT : Theme.TEXT_DIM);
            });
            // pull the badge's padding back so it sits close to the label
            column.setPadding(0, dp(6), 0, dp(6));
            line.addView(help);
        }
        return column;
    }
}
