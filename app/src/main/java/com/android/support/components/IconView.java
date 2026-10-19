package com.android.support.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

import org.lsposed.lsparanoid.Obfuscate;

@Obfuscate
public class IconView extends View {

    private static final float GRID = 24f;
    private static final float STROKE = 1.8f;

    private final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clear = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private final RectF rect = new RectF();
    private String name;

    public IconView(Context context, String name) {
        super(context);
        this.name = name == null ? "" : name;
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(STROKE);
        stroke.setStrokeCap(Paint.Cap.ROUND);
        stroke.setStrokeJoin(Paint.Join.ROUND);
        fill.setStyle(Paint.Style.FILL);
        clear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setColor(Theme.TEXT_DIM);
    }

    public void setColor(int color) {
        stroke.setColor(color);
        fill.setColor(color);
        invalidate();
    }

    public void setIcon(String name) {
        this.name = name == null ? "" : name;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float w = getWidth() - getPaddingLeft() - getPaddingRight();
        float h = getHeight() - getPaddingTop() - getPaddingBottom();
        float size = Math.min(w, h);
        if (size <= 0) return;

        int save = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);
        canvas.translate(getPaddingLeft() + (w - size) / 2f, getPaddingTop() + (h - size) / 2f);
        canvas.scale(size / GRID, size / GRID);
        draw(canvas, name);
        canvas.restoreToCount(save);
    }

    // you can add more icons here, but keep them simple and geometric, so they scale well
    private void draw(Canvas c, String icon) {
        path.reset();
        switch (icon) {
            case "hex":
                polygon(12, 12, 9.5f, 6, -90);
                c.drawPath(path, fill);
                c.drawCircle(12, 12, 3.2f, clear);
                break;
            case "bell-off":
                path.moveTo(5.5f, 17);
                path.lineTo(18.5f, 17);
                path.lineTo(17, 15);
                path.lineTo(17, 10.5f);
                rect.set(7, 5.5f, 17, 15.5f);
                path.arcTo(rect, 0, -180);
                path.lineTo(7, 15);
                path.close();
                c.drawPath(path, stroke);
                rect.set(10, 17.5f, 14, 21);
                c.drawArc(rect, 0, 180, false, stroke);
                c.drawLine(4, 4, 20, 20, stroke);
                break;
            case "wand":
                c.drawLine(4, 20, 14.5f, 9.5f, stroke);
                c.drawLine(14.5f, 9.5f, 16, 8, stroke);
                sparkle(c, 17.5f, 4.5f, 2.2f);
                sparkle(c, 20, 10.5f, 1.5f);
                sparkle(c, 11, 4, 1.3f);
                break;
            case "shield":
                path.moveTo(12, 3);
                path.lineTo(19.5f, 6);
                path.lineTo(19.5f, 11.5f);
                path.cubicTo(19.5f, 16, 16.3f, 19.6f, 12, 21);
                path.cubicTo(7.7f, 19.6f, 4.5f, 16, 4.5f, 11.5f);
                path.lineTo(4.5f, 6);
                path.close();
                c.drawPath(path, stroke);
                break;
            case "eye":
                path.moveTo(2.5f, 12);
                path.cubicTo(5, 7, 8.5f, 5.5f, 12, 5.5f);
                path.cubicTo(15.5f, 5.5f, 19, 7, 21.5f, 12);
                path.cubicTo(19, 17, 15.5f, 18.5f, 12, 18.5f);
                path.cubicTo(8.5f, 18.5f, 5, 17, 2.5f, 12);
                path.close();
                c.drawPath(path, stroke);
                c.drawCircle(12, 12, 3, stroke);
                break;
            case "target":
                c.drawCircle(12, 12, 7.5f, stroke);
                c.drawLine(12, 2, 12, 6.5f, stroke);
                c.drawLine(12, 17.5f, 12, 22, stroke);
                c.drawLine(2, 12, 6.5f, 12, stroke);
                c.drawLine(17.5f, 12, 22, 12, stroke);
                c.drawCircle(12, 12, 1.3f, fill);
                break;
            case "user":
                c.drawCircle(12, 8, 3.8f, stroke);
                path.moveTo(4.5f, 20.5f);
                path.cubicTo(4.5f, 16.5f, 7.8f, 14, 12, 14);
                path.cubicTo(16.2f, 14, 19.5f, 16.5f, 19.5f, 20.5f);
                c.drawPath(path, stroke);
                break;
            case "bolt":
                path.moveTo(13, 2.5f);
                path.lineTo(5, 13.5f);
                path.lineTo(11.5f, 13.5f);
                path.lineTo(10.5f, 21.5f);
                path.lineTo(19, 10);
                path.lineTo(12.5f, 10);
                path.close();
                c.drawPath(path, stroke);
                break;
            case "gear":
                c.drawCircle(12, 12, 6.5f, stroke);
                c.drawCircle(12, 12, 2.5f, stroke);
                float teeth = stroke.getStrokeWidth();
                stroke.setStrokeWidth(2.8f);
                stroke.setStrokeCap(Paint.Cap.BUTT);
                for (int i = 0; i < 8; i++) {
                    double a = Math.toRadians(i * 45);
                    float cos = (float) Math.cos(a), sin = (float) Math.sin(a);
                    c.drawLine(12 + cos * 6.5f, 12 + sin * 6.5f, 12 + cos * 9.5f, 12 + sin * 9.5f, stroke);
                }
                stroke.setStrokeWidth(teeth);
                stroke.setStrokeCap(Paint.Cap.ROUND);
                break;
            case "info":
                c.drawCircle(12, 12, 9, stroke);
                c.drawLine(12, 11, 12, 16.5f, stroke);
                c.drawCircle(12, 7.8f, 1.1f, fill);
                break;
            case "help":
                c.drawCircle(12, 12, 9.5f, stroke);
                path.moveTo(9.5f, 9.6f);
                path.cubicTo(9.5f, 8.1f, 10.6f, 7, 12, 7);
                path.cubicTo(13.4f, 7, 14.5f, 8, 14.5f, 9.4f);
                path.cubicTo(14.5f, 11.3f, 12, 11.4f, 12, 13.6f);
                c.drawPath(path, stroke);
                c.drawCircle(12, 16.9f, 1.15f, fill);
                break;
            case "star":
                path.reset();
                for (int i = 0; i < 10; i++) {
                    double a = Math.toRadians(-90 + i * 36);
                    float r = i % 2 == 0 ? 9.5f : 4.2f;
                    float x = 12 + (float) Math.cos(a) * r, y = 12.8f + (float) Math.sin(a) * r;
                    if (i == 0) path.moveTo(x, y);
                    else path.lineTo(x, y);
                }
                path.close();
                c.drawPath(path, stroke);
                break;
            case "gamepad":
                rect.set(2.5f, 7, 21.5f, 17.5f);
                c.drawRoundRect(rect, 4.5f, 4.5f, stroke);
                c.drawLine(7, 10.2f, 7, 14.2f, stroke);
                c.drawLine(5, 12.2f, 9, 12.2f, stroke);
                c.drawCircle(15.5f, 11, 1.1f, fill);
                c.drawCircle(18, 13.4f, 1.1f, fill);
                break;
            case "sword":
                path.moveTo(19.5f, 4.5f);
                path.lineTo(19, 8);
                path.lineTo(9.5f, 17.5f);
                path.lineTo(6.5f, 14.5f);
                path.lineTo(16, 5);
                path.close();
                c.drawPath(path, stroke);
                c.drawLine(5, 13, 11, 19, stroke);
                c.drawLine(4, 20, 7, 17, stroke);
                break;
            case "chevron-right":
                polyline(c, 9, 5, 16, 12, 9, 19);
                break;
            case "chevron-left":
                polyline(c, 15, 5, 8, 12, 15, 19);
                break;
            case "chevron-down":
                polyline(c, 5, 9, 12, 16, 19, 9);
                break;
            case "external":
                polyline(c, 17, 13.5f, 17, 19, 5, 19, 5, 7, 10.5f, 7);
                polyline(c, 13.5f, 4, 20, 4, 20, 10.5f);
                c.drawLine(20, 4, 11, 13, stroke);
                break;
            case "wifi":
                for (float r : new float[]{4, 8, 12}) {
                    rect.set(12 - r, 20 - r, 12 + r, 20 + r);
                    c.drawArc(rect, 225, 90, false, stroke);
                }
                c.drawCircle(12, 19.2f, 1.5f, fill);
                break;
            case "check":
                polyline(c, 5, 12.5f, 10, 17, 19, 7.5f);
                break;
                case "close":
                polyline(c, 6, 6, 18, 18);
                polyline(c, 6, 18, 18, 6);
                break;
            case "plus":
                c.drawLine(12, 5, 12, 19, stroke);
                c.drawLine(5, 12, 19, 12, stroke);
                break;
            case "minus":
                c.drawLine(5, 12, 19, 12, stroke);
                break;
            case "copy":
                rect.set(6, 6, 18, 18);
                c.drawRoundRect(rect, 1.8f, 1.8f, stroke);
                rect.set(9, 9, 21, 21);
                c.drawRoundRect(rect, 1.8f, 1.8f, stroke);
                break;
            case "edit":
                path.moveTo(14.5f, 5.5f);
                path.lineTo(18.5f, 9.5f);
                path.lineTo(8.5f, 19.5f);
                path.lineTo(4, 20);
                path.lineTo(4.5f, 15.5f);
                path.close();
                c.drawPath(path, stroke);
                break;
            case "grid":
            default:
                for (float[] cell : new float[][]{{3.5f, 3.5f}, {13.5f, 3.5f}, {3.5f, 13.5f}, {13.5f, 13.5f}}) {
                    rect.set(cell[0], cell[1], cell[0] + 7, cell[1] + 7);
                    c.drawRoundRect(rect, 1.8f, 1.8f, stroke);
                }
                break;
        }
    }

    private void polygon(float cx, float cy, float r, int sides, float startDeg) {
        for (int i = 0; i < sides; i++) {
            double a = Math.toRadians(startDeg + i * 360.0 / sides);
            float x = cx + (float) Math.cos(a) * r, y = cy + (float) Math.sin(a) * r;
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.close();
    }

    private void polyline(Canvas c, float... points) {
        path.reset();
        path.moveTo(points[0], points[1]);
        for (int i = 2; i < points.length; i += 2) path.lineTo(points[i], points[i + 1]);
        c.drawPath(path, stroke);
    }

    private void sparkle(Canvas c, float x, float y, float r) {
        c.drawLine(x - r, y, x + r, y, stroke);
        c.drawLine(x, y - r, x, y + r, stroke);
    }
}
