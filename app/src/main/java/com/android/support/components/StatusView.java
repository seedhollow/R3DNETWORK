package com.android.support.components;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.BatteryManager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.lsposed.lsparanoid.Obfuscate;

import java.util.Date;

@Obfuscate
public class StatusView extends LinearLayout {

    private static final long REFRESH_MS = 15_000; // refresh every 15 seconds for battery and clock updates

    private final BatteryView battery;
    private final TextView clock;
    private final Runnable refresh = new Runnable() {
        @Override
        public void run() {
            update();
            postDelayed(this, REFRESH_MS);
        }
    };

    public StatusView(Ui ui) {
        super(ui.context);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        battery = new BatteryView(ui.context);
        LayoutParams batteryParams = new LayoutParams(ui.dp(21), ui.dp(10));
        batteryParams.leftMargin = ui.dp(8);
        addView(battery, batteryParams);

        clock = ui.text("", Theme.TEXT_SMALL, Theme.TEXT, ui.medium);
        LayoutParams clockParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        clockParams.leftMargin = ui.dp(10);
        addView(clock, clockParams);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(refresh);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(refresh);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE && isAttachedToWindow()) update();
    }

    private void update() {
        Context context = getContext();
        String pattern = DateFormat.is24HourFormat(context) ? "HH:mm" : "h:mm a";
        clock.setText(DateFormat.format(pattern, new Date()));

        try {
            // ACTION_BATTERY_CHANGED is sticky, a null receiver just reads the last value
            Intent status = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (status != null) {
                int level = status.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = status.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                int plugged = status.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
                battery.set(level < 0 ? 1f : level / (float) scale, plugged != 0);
            }
        } catch (Exception ignored) {
        }
    }

    private static class BatteryView extends View {
        private final Paint outline = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF rect = new RectF();
        private float level = 1f;
        private boolean charging;

        BatteryView(Context context) {
            super(context);
            float density = context.getResources().getDisplayMetrics().density;
            outline.setStyle(Paint.Style.STROKE);
            outline.setStrokeWidth(1.2f * density);
            outline.setColor(Theme.TEXT);
        }

        void set(float level, boolean charging) {
            this.level = Math.max(0f, Math.min(1f, level));
            this.charging = charging;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            float w = getWidth(), h = getHeight();
            float half = outline.getStrokeWidth() / 2f;
            float cap = w * 0.08f;
            float bodyRight = w - cap - half;
            float radius = h * 0.28f;

            rect.set(half, half, bodyRight, h - half);
            canvas.drawRoundRect(rect, radius, radius, outline);

            fill.setColor(Theme.TEXT);
            rect.set(bodyRight + half, h * 0.32f, w, h * 0.68f);
            canvas.drawRoundRect(rect, cap, cap, fill);

            float pad = outline.getStrokeWidth() * 1.6f;
            float innerW = bodyRight - pad * 2;
            fill.setColor(charging ? 0xFF4CD964 : level <= 0.15f ? Theme.ACCENT : Theme.TEXT);
            rect.set(pad, pad, pad + innerW * level, h - pad);
            canvas.drawRoundRect(rect, radius / 2f, radius / 2f, fill);
        }
    }
}
