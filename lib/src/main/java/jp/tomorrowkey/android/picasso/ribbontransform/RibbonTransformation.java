package jp.tomorrowkey.android.picasso.ribbontransform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.squareup.picasso.Transformation;

import jp.tomorrowkey.android.picasso.ribbontransform.util.PaintBuilder;
import jp.tomorrowkey.android.picasso.ribbontransform.util.PathBuilder;
import jp.tomorrowkey.android.picasso.ribbontransform.util.Util;

public class RibbonTransformation implements Transformation {

    Position position;

    float ribbonWidth;

    float ribbonMargin;

    String text;

    String key;

    Paint textPaint;

    Paint ribbonPaint;

    public enum Position {
        RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM, LEFT_TOP
    }

    private RibbonTransformation(Position position, float ribbonWidth, float ribbonMargin, String text, Paint textPaint, Paint ribbonPaint, String key) {
        this.position = position;
        this.ribbonWidth = ribbonWidth;
        this.ribbonMargin = ribbonMargin;
        this.text = text;
        this.textPaint = textPaint;
        this.ribbonPaint = ribbonPaint;
        this.key = key;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int height = source.getHeight();
        int width = source.getWidth();

        Bitmap.createBitmap(source);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(source, 0, 0, null);
        source.recycle();

        drawBackground(canvas, position, ribbonMargin, ribbonWidth, ribbonPaint);
        drawText(canvas, position, text, ribbonMargin, ribbonWidth, textPaint);

        return bitmap;
    }

    private void drawBackground(Canvas canvas, Position position, float ribbonMargin, float ribbonWidth, Paint paint) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Path path = buildRibbonBackgroundPath(width, height, ribbonMargin, ribbonWidth, position);
        canvas.drawPath(path, paint);
    }

    private Path buildRibbonBackgroundPath(int width, int height, float ribbonMargin, float ribbonWidth, Position position) {
        int hypotenuse = (int) Math.hypot(ribbonWidth, ribbonWidth);
        switch (position) {
            case RIGHT_TOP:
                return new PathBuilder()
                        .moveTo(width - (ribbonMargin + hypotenuse), 0)
                        .lineTo(width - ribbonMargin, 0)
                        .lineTo(width, ribbonMargin)
                        .lineTo(width, ribbonMargin + hypotenuse)
                        .build();
            case RIGHT_BOTTOM:
                return new PathBuilder()
                        .moveTo(width, height - (ribbonMargin + hypotenuse))
                        .lineTo(width, height - ribbonMargin)
                        .lineTo(width - ribbonMargin, height)
                        .lineTo(width - (ribbonMargin + hypotenuse), height)
                        .build();
            case LEFT_BOTTOM:
                return new PathBuilder()
                        .moveTo(0, height - (ribbonMargin + hypotenuse))
                        .lineTo(0, height - ribbonMargin)
                        .lineTo(ribbonMargin, height)
                        .lineTo(ribbonMargin + hypotenuse, height)
                        .build();
            case LEFT_TOP: {
                return new PathBuilder()
                        .moveTo(ribbonMargin, 0)
                        .lineTo(ribbonMargin + hypotenuse, 0)
                        .lineTo(0, ribbonMargin + hypotenuse)
                        .lineTo(0, ribbonMargin)
                        .build();
            }
            default:
                throw new RuntimeException("Unknown position, value=" + position);
        }
    }

    private void drawText(Canvas canvas, Position position, String text, float ribbonMargin, float ribbonWidth, Paint paint) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int shortSide = Math.min(width, height);

        rotate(canvas, position, width, height, shortSide);

        float x = computeX(text, shortSide, paint);
        float y = computeY(position, ribbonMargin, ribbonWidth, shortSide, paint);

        canvas.save();
        canvas.drawText(text, x, y, paint);
        canvas.restore();
    }

    private void rotate(Canvas canvas, Position position, int width, int height, int shortSide) {
        switch (position) {
            case RIGHT_TOP: {
                if (height < width) {
                    canvas.translate(width - height, 0);
                }
                canvas.rotate(45, shortSide / 2, shortSide / 2);
                break;
            }
            case RIGHT_BOTTOM: {
                if (height > width) {
                    canvas.translate(0, height - width);
                } else if (height < width) {
                    canvas.translate(width - height, 0);
                }
                canvas.rotate(-45, shortSide / 2, shortSide / 2);
                break;
            }
            case LEFT_BOTTOM: {
                if (height > width) {
                    canvas.translate(0, height - width);
                }
                canvas.rotate(45, shortSide / 2, shortSide / 2);
                break;
            }
            case LEFT_TOP: {
                canvas.rotate(-45, shortSide / 2, shortSide / 2);
                break;
            }
            default:
                throw new RuntimeException("Unknown position, value=" + position);
        }
    }

    private float computeX(String text, int shortSide, Paint paint) {
        float textWidth = paint.measureText(text);
        return (shortSide / 2 - (textWidth / 2));
    }

    private float computeY(Position position, float ribbonMargin, float ribbonWidth, int shortSide, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom + fontMetrics.ascent;
        float middleOfText = fontHeight / 2;

        if (position == Position.RIGHT_TOP || position == Position.LEFT_TOP) {
            double cornerOfSquare = (shortSide / 2) - (shortSide / Math.sqrt(2));
            double topOfRibbon = cornerOfSquare + (ribbonMargin / Math.sqrt(2));
            return (float) (topOfRibbon + (ribbonWidth / 2 - middleOfText));
        } else if (position == Position.RIGHT_BOTTOM || position == Position.LEFT_BOTTOM) {
            double cornerOfSquare = (shortSide / 2) + (shortSide / Math.sqrt(2));
            double bottomOfRibbon = cornerOfSquare - (ribbonMargin / Math.sqrt(2));
            return (float) (bottomOfRibbon - (ribbonWidth / 2 + middleOfText));
        } else {
            throw new RuntimeException("Unknown position, value=" + position);
        }
    }

    @Override
    public String key() {
        return key;
    }

    public static class Builder {
        private static final Position DEFAULT_POSITION = Position.RIGHT_TOP;
        private static final float DEFAULT_RIBBON_MARGIN = 30;
        private static final float DEFAULT_RIBBON_WIDTH = 60;
        private static final String DEFAULT_TEXT = "Ribbon";
        private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
        private static final float DEFAULT_TEXT_SIZE = 24;
        private static final boolean DEFAULT_TEXT_BOLD_ENABLED = true;
        private static final float TEXT_SHADOW_RADIUS = 0;
        private static final float TEXT_SHADOW_DX = 0;
        private static final float TEXT_SHADOW_DY = 0;
        private static final int TEXT_SHADOW_COLOR = Color.WHITE;
        private static final int DEFAULT_RIBBON_COLOR = Color.BLACK;

        private Context context;
        private Position position = DEFAULT_POSITION;
        private float ribbonMargin = DEFAULT_RIBBON_MARGIN;
        private float ribbonWidth = DEFAULT_RIBBON_WIDTH;
        private String text = DEFAULT_TEXT;
        private int textColor = DEFAULT_TEXT_COLOR;
        private float textSize = DEFAULT_TEXT_SIZE;
        private boolean textBold = DEFAULT_TEXT_BOLD_ENABLED;
        private float textShadowRadius = TEXT_SHADOW_RADIUS;
        private float textShadowDx = TEXT_SHADOW_DX;
        private float textShadowDy = TEXT_SHADOW_DY;
        private int textShadowColor = TEXT_SHADOW_COLOR;
        private int ribbonColor = DEFAULT_RIBBON_COLOR;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder position(Position position) {
            this.position = position;
            return this;
        }

        public Builder ribbonMargin(int size) {
            ribbonMargin = Util.dpToPx(context, size);
            return this;
        }

        public Builder ribbonWidth(int size) {
            ribbonWidth = Util.dpToPx(context, size);
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public Builder textSizeInDp(int size) {
            this.textSize = Util.dpToPx(context, size);
            return this;
        }

        public Builder textBold(boolean enabled) {
            this.textBold = enabled;
            return this;
        }

        public Builder textShadow(float radius, int dxInDp, int dyInDp, int color) {
            this.textShadowRadius = radius;
            this.textShadowDx = Util.dpToPx(context, dxInDp);
            this.textShadowDy = Util.dpToPx(context, dyInDp);
            this.textShadowColor = color;
            return this;
        }

        public Builder ribbonColor(int color) {
            this.ribbonColor = color;
            return this;
        }

        public RibbonTransformation build() {
            Paint textPaint = new PaintBuilder()
                    .setColor(textColor)
                    .setTextSize(textSize)
                    .setAntiAlias(true)
                    .setBold(textBold)
                    .setShadowLayer(textShadowRadius, textShadowDx, textShadowDy, textShadowColor)
                    .build();

            Paint ribbonPaint = new PaintBuilder()
                    .setColor(ribbonColor)
                    .setStyle(Paint.Style.FILL)
                    .build();
            return new RibbonTransformation(position, ribbonWidth, ribbonMargin, text, textPaint, ribbonPaint, toString());
        }

        @Override
        public String toString() {
            return "RibbonTransformation{" +
                    "position=" + position +
                    ", ribbonMargin=" + ribbonMargin +
                    ", ribbonWidth=" + ribbonWidth +
                    ", ribbonColor=" + ribbonColor +
                    ", text='" + text + '\'' +
                    ", textColor=" + textColor +
                    ", textSize=" + textSize +
                    ", textBold=" + textBold +
                    ", textShadowRadius=" + textShadowRadius +
                    ", textShadowDx=" + textShadowDx +
                    ", textShadowDy=" + textShadowDy +
                    ", textShadowColor=" + textShadowColor +
                    '}';
        }
    }
}
