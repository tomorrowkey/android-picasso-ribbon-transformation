package jp.tomorrowkey.android.picasso.ribbontransform.util;

import android.graphics.Paint;
import android.graphics.Typeface;

public final class PaintBuilder {

    private Paint paint;

    public PaintBuilder() {
        paint = new Paint();
    }

    public PaintBuilder setColor(int color) {
        paint.setColor(color);
        return this;
    }

    public PaintBuilder setTextSize(float textSize) {
        paint.setTextSize(textSize);
        return this;
    }

    public PaintBuilder setAntiAlias(boolean aa) {
        paint.setAntiAlias(aa);
        return this;
    }

    public PaintBuilder setBold(boolean enabled) {
        paint.setTypeface(Typeface.create(paint.getTypeface(), enabled ? Typeface.BOLD : Typeface.NORMAL));
        return this;
    }

    public PaintBuilder setStyle(Paint.Style style) {
        paint.setStyle(style);
        return this;
    }

    public PaintBuilder setShadowLayer(float radius, float dx, float dy, int shadowColor) {
        paint.setShadowLayer(radius, dx, dy, shadowColor);
        return this;
    }

    public Paint build() {
        return paint;
    }
}
