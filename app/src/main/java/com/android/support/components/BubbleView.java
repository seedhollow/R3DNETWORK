package com.android.support.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class BubbleView extends View {

    private final Paint background = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint label = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint image = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Matrix matrix = new Matrix();
    private final String text;
    private final Bitmap icon;

    /** icon may be null, then the text is drawn. */
    public BubbleView(Context context, Typeface typeface, String text, Bitmap icon) {
        super(context);
        this.text = text;
        this.icon = icon;
        background.setColor(Theme.PANEL_BG);
        ring.setStyle(Paint.Style.STROKE);
        ring.setStrokeWidth(dp(1.6f));
        ring.setColor(Theme.ACCENT);
        label.setColor(Theme.TEXT);
        label.setTypeface(Typeface.create(typeface, Typeface.BOLD_ITALIC));
        label.setTextAlign(Paint.Align.CENTER);
        if (icon != null) {
            image.setShader(new BitmapShader(icon, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = dp(Theme.BUBBLE_SIZE);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (icon == null) return;
        // center-crop the icon into the bubble
        float scale = Math.max(w / (float) icon.getWidth(), h / (float) icon.getHeight());
        matrix.setScale(scale, scale);
        matrix.postTranslate((w - icon.getWidth() * scale) / 2f, (h - icon.getHeight() * scale) / 2f);
        image.getShader().setLocalMatrix(matrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float s = getWidth(), c = s / 2f;

        if (icon != null) {
            canvas.drawCircle(c, c, c, image);
            return;
        }

        canvas.drawCircle(c, c, c, background);
        canvas.drawCircle(c, c, c - ring.getStrokeWidth() * 1.5f, ring);

        label.setTextSize(s * 0.26f);
        float baseline = c - (label.descent() + label.ascent()) / 2f;
        canvas.drawText(text, c, baseline, label);
    }

    private int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
