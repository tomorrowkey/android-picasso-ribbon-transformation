package jp.tomorrowkey.android.picasso.ribbontransform.util;

import android.content.Context;

public final class Util {

    private Util() {
    }

    public static final float dpToPx(Context context, int px) {
        return context.getResources().getDisplayMetrics().density * px;
    }

}
