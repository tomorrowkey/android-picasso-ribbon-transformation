package jp.tomorrowkey.android.ribbontransformation.sample.model;

import android.content.Context;
import android.graphics.Color;

import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.tomorrowkey.android.picasso.ribbontransform.RibbonTransformation;
import jp.tomorrowkey.android.ribbontransformation.sample.util.Util;

public class ThumbnailImage {

    int width;

    int height;

    int color;

    File bitmapFile;

    Transformation transformation;

    private ThumbnailImage(int width, int height, int color, Transformation transformation) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.transformation = transformation;
    }

    public File getBitmapFile() {
        return bitmapFile;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public ThumbnailImage prepareBitmap(Context context) {
        this.bitmapFile = Util.newBitmapFile(context, width, height, color);
        return this;
    }

    public static List<ThumbnailImage> load(Context context) {
        List<ThumbnailImage> list = new ArrayList<>();
        list.add(new ThumbnailImage(200, 200, Color.WHITE, new RibbonTransformation.Builder(context).build()));
        list.add(new ThumbnailImage(100, 100, Color.WHITE, newBuilder(context).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.RIGHT_BOTTOM).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.LEFT_BOTTOM).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.LEFT_TOP).build()));
        list.add(new ThumbnailImage(200, 400, Color.WHITE, newBuilder(context).build()));
        list.add(new ThumbnailImage(200, 400, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.RIGHT_BOTTOM).build()));
        list.add(new ThumbnailImage(200, 400, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.LEFT_BOTTOM).build()));
        list.add(new ThumbnailImage(200, 400, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.LEFT_TOP).build()));
        list.add(new ThumbnailImage(400, 200, Color.WHITE, newBuilder(context).build()));
        list.add(new ThumbnailImage(400, 200, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.RIGHT_BOTTOM).build()));
        list.add(new ThumbnailImage(400, 200, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.LEFT_BOTTOM).build()));
        list.add(new ThumbnailImage(400, 200, Color.WHITE, newBuilder(context).position(RibbonTransformation.Position.LEFT_TOP).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).textBold(false).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).ribbonWidth(40).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).ribbonMargin(40).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).ribbonMargin(0).ribbonWidth(40).build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).text("新!!").build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).ribbonMargin(40).text("abcdefg").build()));
        list.add(new ThumbnailImage(200, 200, Color.WHITE, newBuilder(context).textShadow(0.1f, 0, 1, Color.WHITE).build()));
        return list;
    }

    private static RibbonTransformation.Builder newBuilder(Context context) {
        return new RibbonTransformation.Builder(context)
                .ribbonColor(Color.rgb(0xfd, 0xD8, 0x35))
                .ribbonMargin(10)
                .ribbonWidth(20)
                .textColor(Color.BLACK)
                .text("NEW!")
                .textBold(true)
                .textSizeInDp(12);
    }

}
