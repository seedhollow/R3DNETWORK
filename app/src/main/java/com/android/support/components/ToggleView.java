package com.android.support.components;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class ToggleView extends View {

    public interface OnChangeListener {
        void onChange(boolean checked);
    }

    private final Paint trackOff = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trackOn = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint knob = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private final ArgbEvaluator argb = new ArgbEvaluator();

    private boolean checked;
    private float progress;
    private ValueAnimator animator;
    private OnChangeListener listener;

    public ToggleView(Context context) {
        super(context);
        CornerPathEffect corners = new CornerPathEffect(dp(3));
        trackOff.setColor(Theme.TRACK_OFF);
        trackOff.setPathEffect(corners);
        trackOn.setPathEffect(corners);
        knob.setPathEffect(corners);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

    public boolean isChecked() {
        return checked;
    }

    /** Sets the state without animating or notifying the listener. */
    public void setChecked(boolean checked) {
        this.checked = checked;
        if (animator != null) animator.cancel();
        progress = checked ? 1f : 0f;
        invalidate();
    }

    public void toggle() {
        checked = !checked;
        if (animator != null) animator.cancel();
        animator = ValueAnimator.ofFloat(progress, checked ? 1f : 0f);
        animator.setDuration(180);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(a -> {
            progress = (float) a.getAnimatedValue();
            invalidate();
        });
        animator.start();
        if (listener != null) listener.onChange(checked);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(dp(42), widthMeasureSpec), resolveSize(dp(18), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        trackOn.setShader(new LinearGradient(0, 0, w, 0, Theme.ACCENT, Theme.ACCENT_LIGHT, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth(), h = getHeight();
        float slant = h * 0.38f;

        parallelogram(0, 0, w, h, slant);
        if (progress < 1f) canvas.drawPath(path, trackOff);
        if (progress > 0f) {
            trackOn.setAlpha((int) (255 * progress));
            canvas.drawPath(path, trackOn);
        }

        float inset = dp(2.5f);
        float knobW = w * 0.5f;
        float knobH = h - inset * 2;
        float travel = w - knobW - inset * 2;
        float x = inset + travel * progress;
        knob.setColor((int) argb.evaluate(progress, Theme.KNOB_OFF, Theme.ACCENT_KNOB));
        parallelogram(x, inset, knobW, knobH, knobH * 0.38f);
        canvas.drawPath(path, knob);
    }

    private void parallelogram(float x, float y, float w, float h, float slant) {
        path.reset();
        path.moveTo(x + slant, y);
        path.lineTo(x + w, y);
        path.lineTo(x + w - slant, y + h);
        path.lineTo(x, y + h);
        path.close();
    }

    private int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
