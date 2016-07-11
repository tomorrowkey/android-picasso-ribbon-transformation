package jp.tomorrowkey.android.picasso.ribbontransform.util;

import android.graphics.Path;

public class PathBuilder {

    Path path;

    public PathBuilder() {
        path = new Path();
    }

    public PathBuilder moveTo(float x, float y) {
        path.moveTo(x, y);
        return this;
    }

    public PathBuilder lineTo(float x, float y) {
        path.lineTo(x, y);
        return this;
    }

    public Path build() {
        return path;
    }
}
