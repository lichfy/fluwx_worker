package com.tencent.wework.api.model;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;

/**
 * Created by hu on 16/5/30.
 */
public abstract class WWBaseMessage extends BaseMessage {
    public String transaction;
    public String appId;
    public String agentId;
    public String appName;
    public String appPkg;
    public int sdkVer;

    public void toBundle(Bundle var1) {
        var1.putString("_wwapi_basereq_transaction", transaction = String.valueOf(SystemClock.uptimeMillis()));
        var1.putString("_wwapi_basereq_openid", this.appId);
        var1.putString("_wwapi_basereq_agentid", this.agentId);
        try {
            var1.putString("_wwapi_basereq_appbundle", mContext.getPackageName());
            var1.putString("_wwapi_basereq_appname", mContext.getString(mContext.getApplicationInfo().labelRes));
        }catch (Throwable e){

        }
        var1.putInt("_wwobject_sdkVer", SDK_VER);
    }

    @Override
    public boolean checkArgs() {
        return true;
    }

    public void fromBundle(Bundle var1) {
        this.transaction = var1.getString("_wwapi_basereq_transaction");
        this.appId = var1.getString("_wwapi_basereq_openid");
        this.agentId = var1.getString("_wwapi_basereq_agentid");
        this.appName = var1.getString("_wwapi_basereq_appname");
        this.appPkg = var1.getString("_wwapi_basereq_appbundle");
        sdkVer = var1.getInt("_wwobject_sdkVer", 0);
    }

    public Uri toUri() {
        return null;
    }

    public void fromUri(Uri var1) {

    }

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_MEDIA = 1000;
    public static final int TYPE_AUTH = 1001;
}
