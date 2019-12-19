package com.tencent.wework.api.model;

/**
 * Created by hu on 16/5/30.
 * สำฦต
 */
public class WWMediaVideo extends WWMediaFile {
    private static final String TAG = "WWAPI.WWMediaVideo";

    @Override
    public int getType() {
        return WWMediaMessage.WWMediaObject.TYPE_VIDEO;
    }
}
