package com.tencent.wework.api;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.tencent.wework.api.model.BaseMessage;
import com.tencent.wework.api.model.WWBaseMessage;
import com.tencent.wework.api.model.WWBaseRespMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hu on 16/5/30.
 */
final public class WWAPIImpl implements IWWAPI {
    private Context context;
    private String schema;
    private Map<String, IWWAPIEventHandler> callbacks = new HashMap<String, IWWAPIEventHandler>();
    private BroadcastReceiver mReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if(!schema.equals(intent.getScheme())){
                    return;
                }
                final BaseMessage msg = BaseMessage.parseUri(intent.getData());
                if (msg instanceof WWBaseRespMessage) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                callbacks.get(((WWBaseRespMessage) msg).transaction).handleResp(msg);
                                callbacks.remove(((WWBaseRespMessage) msg).transaction);
                            } catch (Throwable e) {
                            }
                        }
                    });

                }
            } catch (Throwable e) {
            }
        }
    };

    public WWAPIImpl(Context var0) {
        context = var0;
    }

    @Override
    public boolean registerApp(String var1) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme(var1);
        intentFilter.addAction(var1);
        context.registerReceiver(mReciver, intentFilter);
        schema = var1;
        return true;
    }

    @Override
    public void unregisterApp() {

    }

    @Override
    public boolean handleIntent(Intent intent, IWWAPIEventHandler handler) {
        return false;
    }

    @Override
    public boolean isWWAppInstalled() {
        return isWW("com.tencent.wework");
    }

    @Override
    public boolean isWWAppSupportAPI() {
        return getWWAppSupportAPI() >= 100;
    }

    @Override
    public int getWWAppSupportAPI() {
        try {
            PackageInfo var1;
            var1 = context.getPackageManager().getPackageInfo("com.tencent.wework", PackageManager.GET_META_DATA);
            return var1 == null ? 0 : var1.versionCode;
        } catch (Throwable var2) {
            return 0;
        }
    }

    @Override
    public boolean openWWApp() {
        try {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage("com.tencent.wework"));
            return true;
        } catch (Throwable var2) {
            return false;
        }
    }

    @Override
    public boolean sendMessage(BaseMessage var1) {
        Intent i = new Intent("com.tencent.wework.apihost");
        i.setClassName("com.tencent.wework", "com.tencent.wework.apihost.WWAPIActivity");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        try {
            var1.setContext(context);
            Bundle b = BaseMessage.pack(var1);
            i.putExtras(b);
            i.putExtra("PendingIntent", PendingIntent.getBroadcast(context, 0, new Intent(context, mReciver.getClass()), PendingIntent.FLAG_UPDATE_CURRENT));
            context.startActivity(i);
            return true;
        } catch (Throwable var2) {
            return false;
        }
    }

    @Override
    public boolean sendMessage(BaseMessage var1, IWWAPIEventHandler callback) {
        if (sendMessage(var1)) {
            if (var1 instanceof WWBaseMessage) {
                callbacks.put(((WWBaseMessage) var1).transaction, callback);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detach() {
        context.unregisterReceiver(mReciver);
    }

    @SuppressLint("PackageManagerGetSignatures")
    private String getSignature(String pkg) {
        try {
            PackageInfo var1;
            var1 = context.getPackageManager().getPackageInfo(pkg, PackageManager.GET_SIGNATURES);
            return MD5Encode(var1.signatures[0].toByteArray());
        } catch (Throwable var2) {
            System.out.println("get sign failed:");
            var2.printStackTrace();
        }
        return "";
    }

    private static String MD5Encode(byte[] toencode) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(toencode);
            return HexEncode(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String HexEncode(byte[] toencode) {
        StringBuilder sb = new StringBuilder(toencode.length * 2);
        for (byte b : toencode) {
            sb.append(Integer.toHexString((b & 0xf0) >>> 4));
            sb.append(Integer.toHexString(b & 0x0f));
        }
        return sb.toString().toUpperCase();
    }

    private boolean isWW(String pkg) {
        try {
            System.out.println("pkg signature:" + getSignature(pkg));
            return pkg.equals("com.tencent.wework") && getSignature(pkg).equals("011A40266C8C75D181DDD8E4DDC50075");
        } catch (Throwable var2) {
            return false;
        }
    }
}
