package com.tencent.wework.api;

import android.content.Context;

/**
 * Created by hu on 16/5/30.
 */
public class WWAPIFactory {
    private static final String TAG = "WWAPI.WWAPIFactory";

    public static IWWAPI createWWAPI(Context context) {
        return new WWAPIImpl(context);
    }

    private WWAPIFactory() {
        throw new RuntimeException(this.getClass().getSimpleName() + " should not be instantiated");
    }
}
