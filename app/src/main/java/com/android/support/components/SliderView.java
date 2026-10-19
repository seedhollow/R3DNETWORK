package com.android.support.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class SliderView extends View {

    public interface OnChangeListener {
        void onChange(int value);
    }

    private final Paint track = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint thumb = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final Path path = new Path();

    private int min, max, value;
    private OnChangeListener listener;

    public SliderView(Context context, int min, int max) {
        super(context);
        this.min = min;
        this.max = Math.max(max, min);
        this.value = min;
        track.setColor(Theme.TRACK_OFF);
        thumb.setColor(Theme.KNOB_OFF);
        thumb.setPathEffect(new CornerPathEffect(dp(2)));
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public void setValue(int value) {
        this.value = Math.max(min, Math.min(max, value));
        invalidate();
    }

    public int getValue() {
        return value;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), resolveSize(dp(28), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        fill.setShader(new LinearGradient(0, 0, w, 0, Theme.ACCENT, Theme.ACCENT_LIGHT, Shader.TileMode.CLAMP));
    }

    private float thumbWidth() {
        return dp(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth(), h = getHeight();
        float tw = thumbWidth();
        float left = tw / 2f, right = w - tw / 2f;
        float fraction = max == min ? 0f : (value - min) / (float) (max - min);
        float cx = left + (right - left) * fraction;
        float th = dp(4), cy = h / 2f;

        rect.set(left, cy - th / 2f, right, cy + th / 2f);
        canvas.drawRoundRect(rect, th / 2f, th / 2f, track);
        rect.set(left, cy - th / 2f, cx, cy + th / 2f);
        canvas.drawRoundRect(rect, th / 2f, th / 2f, fill);

        float kh = dp(16), slant = kh * 0.3f;
        path.reset();
        path.moveTo(cx - tw / 2f + slant, cy - kh / 2f);
        path.lineTo(cx + tw / 2f, cy - kh / 2f);
        path.lineTo(cx + tw / 2f - slant, cy + kh / 2f);
        path.lineTo(cx - tw / 2f, cy + kh / 2f);
        path.close();
        canvas.drawPath(path, thumb);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                // fall through
            case MotionEvent.ACTION_MOVE:
                updateFromTouch(event.getX());
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updateFromTouch(float x) {
        float tw = thumbWidth();
        float fraction = (x - tw / 2f) / (getWidth() - tw);
        fraction = Math.max(0f, Math.min(1f, fraction));
        int next = min + Math.round(fraction * (max - min));
        if (next != value) {
            value = next;
            invalidate();
            if (listener != null) listener.onChange(value);
        }
    }

    private int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
