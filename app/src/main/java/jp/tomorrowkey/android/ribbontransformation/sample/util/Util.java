package jp.tomorrowkey.android.ribbontransformation.sample.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public final class Util {

    private Util() {
    }

    public static final void deleteOrThrow(@NonNull File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        boolean result = file.delete();
        if (!result) {
            throw new IOException("Failed to delete a file, file=" + file.getAbsolutePath());
        }
    }

    public static final void closeQuietly(@Nullable Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }

    public static final File newBitmapFile(Context context, int width, int height, int color) {
        String fileName = String.format(Locale.getDefault(), "%dx%d_%s.png", width, height, Util.convertToColorCode(color));
        String filePath = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
        File file = new File(filePath);

        try {
            Util.deleteOrThrow(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(color);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(outputStream);
        }

        return file;
    }

    public static final String convertToColorCode(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return String.format(Locale.getDefault(), "%02x%02x%02x%02x", alpha, red, green, blue);
    }

}
