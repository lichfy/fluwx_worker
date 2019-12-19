package com.tencent.wework.api.model;

import android.os.Bundle;
import android.util.Log;

import com.tencent.wework.api.model.WWMediaMessage.WWMediaObject;

/**
 * Created by hu on 16/5/30.
 * 链接
 */
public class WWMediaLink extends WWMediaObject {
    private static final String TAG = "WWAPI.WWMediaLink";
    private static final int LENGTH_LIMIT = 10240;
    /**
     * 链接网址
     */
    public String webpageUrl;
    /**
     * 链接图标
     */
    public String thumbUrl;

    @Override
    public int getType() {
        return WWMediaObject.TYPE_URL;
    }

    @Override
    public boolean checkArgs() {
        if(!super.checkArgs()){
            return false;
        }
        if (webpageUrl != null && webpageUrl.length() != 0 && webpageUrl.length() <= LENGTH_LIMIT) {
            return true;
        } else {
            Log.d(TAG, "checkArgs fail, webpageUrl is invalid");
            return false;
        }
    }

    public void toBundle(Bundle var1) {
        var1.putString("_wwwebpageobject_thumbUrl", thumbUrl);
        var1.putString("_wwwebpageobject_webpageUrl", webpageUrl);
        super.toBundle(var1);
    }

    public void fromBundle(Bundle var1) {
        thumbUrl = var1.getString("_wwwebpageobject_thumbUrl");
        webpageUrl = var1.getString("_wwwebpageobject_webpageUrl");
        super.fromBundle(var1);
    }
}
