package com.tencent.wework.api.model;

/**
 * Created by hu on 16/5/30.
 * 视频
 */
public class WWMediaVideo extends WWMediaFile {
    private static final String TAG = "WWAPI.WWMediaVideo";

    @Override
    public int getType() {
        return WWMediaObject.TYPE_VIDEO;
    }
}
