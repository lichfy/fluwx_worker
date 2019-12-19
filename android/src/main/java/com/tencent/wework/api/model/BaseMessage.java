package com.tencent.wework.api.model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.wework.api.util.ReflecterHelper;

/**
 * Created by hu on 16/5/30.
 */
public abstract class BaseMessage {
    protected static final int SDK_VER = 2;
    protected Context mContext = null;
    public BaseMessage() {
    }

    public void setContext(Context context){
        mContext = context;
    }

    public abstract int getType();

    public abstract void toBundle(Bundle var1);

    public abstract void fromBundle(Bundle var1);

    public abstract Uri toUri();

    public abstract void fromUri(Uri var1);

    public static BaseMessage parse(Bundle var1) {
        try {
            String className = var1.getString("_wwobject_identifier_");
            if (!TextUtils.isEmpty(className)) {
                BaseMessage ret = (BaseMessage) ReflecterHelper.newInstance(className);
                ret.fromBundle(var1);
                return ret;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bundle pack(BaseMessage bm) {
        if (bm == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bm.toBundle(bundle);
        bundle.putString("_wwobject_identifier_", bm.getClass().getName());
        return bundle;
    }

    public static BaseMessage parseUri(Uri var1) {
        try {
            String className = var1.getQueryParameter("wwoid");
            if (!TextUtils.isEmpty(className)) {
                BaseMessage ret = (BaseMessage) ReflecterHelper.newInstance(className);
                ret.fromUri(var1);
                return ret;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri packUri(BaseMessage bm) {
        if (bm == null) {
            return null;
        }
        Uri uri = bm.toUri();
        if(uri == null){
            return null;
        }
        return uri.buildUpon().appendQueryParameter("wwoid", bm.getClass().getName()).build();
    }

    public abstract boolean checkArgs();

    public static final int TYPE_SHARE_MESSAGE = 0;
}
