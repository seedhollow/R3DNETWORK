package com.android.support.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.TypedValue;
import android.view.View;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class MarkView extends View {

    private final boolean round;
    private final Paint outline = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint check = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final Path path = new Path();
    private boolean checked;

    public MarkView(Context context, boolean round) {
        super(context);
        this.round = round;
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(dp(1.6f));
        fill.setColor(Theme.ACCENT);
        check.setStyle(Paint.Style.STROKE);
        check.setStrokeWidth(dp(2f));
        check.setStrokeCap(Paint.Cap.ROUND);
        check.setStrokeJoin(Paint.Join.ROUND);
        check.setColor(0xFFFFFFFF);
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = dp(20);
        setMeasuredDimension(resolveSize(size, widthMeasureSpec), resolveSize(size, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float s = Math.min(getWidth(), getHeight());
        float half = outline.getStrokeWidth() / 2f;
        rect.set(half, half, s - half, s - half);
        outline.setColor(checked ? Theme.ACCENT : Theme.TEXT_FAINT);

        if (round) {
            canvas.drawCircle(s / 2f, s / 2f, s / 2f - half, outline);
            if (checked) canvas.drawCircle(s / 2f, s / 2f, s * 0.25f, fill);
            return;
        }

        float radius = dp(5);
        if (checked) {
            canvas.drawRoundRect(rect, radius, radius, fill);
            path.reset();
            path.moveTo(s * 0.27f, s * 0.52f);
            path.lineTo(s * 0.44f, s * 0.68f);
            path.lineTo(s * 0.74f, s * 0.35f);
            canvas.drawPath(path, check);
        } else {
            canvas.drawRoundRect(rect, radius, radius, outline);
        }
    }

    private int dp(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
}
