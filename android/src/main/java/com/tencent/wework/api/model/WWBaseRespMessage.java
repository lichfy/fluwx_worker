package com.tencent.wework.api.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

/**
 * Created by hu on 16/5/30.
 */
public abstract class WWBaseRespMessage extends BaseMessage {
    public String transaction;
    public String appName;
    public String appPkg;
    public int sdkVer;


    public String descPkg;

    public void toBundle(Bundle var1) {
        var1.putString("_wwapi_basersp_transaction", transaction);
        try {
            var1.putString("_wwapi_basersp_appbundle", mContext.getPackageName());
            var1.putString("_wwapi_basersp_appname", mContext.getString(mContext.getApplicationInfo().labelRes));
        } catch (Throwable e) {

        }
        var1.putInt("_wwobject_sdkVer", SDK_VER);
    }

    @Override
    public boolean checkArgs() {
        return !TextUtils.isEmpty(descPkg);
    }

    public void fromBundle(Bundle var1) {
        this.transaction = var1.getString("_wwapi_basersp_transaction");
        this.appName = var1.getString("_wwapi_basersp_appname");
        this.appPkg = var1.getString("_wwapi_basersp_appbundle");
        sdkVer = var1.getInt("_wwobject_sdkVer", 0);
    }

    public Uri toUri() {
        Uri uri = new Uri.Builder().appendQueryParameter("wwtr", transaction == null ? "" : transaction).
                appendQueryParameter("wwver", String.valueOf(sdkVer)).build();
        return uri;
    }

    public void fromUri(Uri var1) {
        if (var1 == null) {
            return;
        }
        try {
            transaction = var1.getQueryParameter("wwtr");
            sdkVer = Integer.parseInt(var1.getQueryParameter("wwver"));
        } catch (Throwable e) {
        }
    }

    public abstract Intent toIntent();

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_MEDIA = 2000;
    public static final int TYPE_AUTH = 2001;
}
