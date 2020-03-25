package com.tencent.wework.api.model;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by hu on 16/8/1.
 * 单条聊天记录信息
 */
public class WWMediaConversation extends WWMediaMessage.WWMediaObject {
    private static final String TAG = "WWAPI.WWMediaConv";
    private static final int CONTENT_LENGTH_LIMIT = 10485760;
    private static final int PATH_LENGTH_LIMIT = 10240;
    /**
     * 这条消息的发信人昵称
     */
    public String name;
    /**
     * 消息时间
     */
    public long date;//ms
    /**
     * 发信人头像
     */
    public byte[] avatarData;
    /**
     * 发信人头像url
     */
    public String avatarPath;
    public WWMediaMessage.WWMediaObject message;

    public WWMediaConversation() {
    }

    public WWMediaConversation(byte[] avatarData) {
        avatarData = avatarData;
    }

    public WWMediaConversation(Bitmap avatar) {
        try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.JPEG, 85, var2);
            avatarData = var2.toByteArray();
            var2.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public int getType() {
        return WWMediaMessage.WWMediaObject.TYPE_CONVERSATION;
    }

    @Override
    public boolean checkArgs() {
        if(!super.checkArgs()){
            return false;
        }
        if (this.avatarData != null && this.avatarData.length > CONTENT_LENGTH_LIMIT) {
            Log.d(TAG, "checkArgs fail, content is too large");
            return false;
        } else if (this.avatarPath != null && this.avatarPath.length() > PATH_LENGTH_LIMIT) {
            Log.d(TAG, "checkArgs fail, path is invalid");
            return false;
        } else if (this.avatarPath != null && this.getFileSize(this.avatarPath) > CONTENT_LENGTH_LIMIT) {
            Log.d(TAG, "checkArgs fail, image content is too large");
            return false;
        } else {
            return message != null && message.checkArgs();
        }
    }

    public void toBundle(Bundle var1) {
        super.toBundle(var1);
        var1.putString("_wwconvobject_name", name);
        var1.putLong("_wwconvobject_date", date);
        var1.putByteArray("_wwconvobject_avatarData", avatarData);
        var1.putString("_wwconvobject_avatarPath", avatarPath);
        var1.putBundle("_wwconvobject_message", BaseMessage.pack(message));
    }

    public void fromBundle(Bundle var1) {
        super.fromBundle(var1);
        name = var1.getString("_wwconvobject_name");
        date = var1.getLong("_wwconvobject_date");
        avatarData = var1.getByteArray("_wwconvobject_avatarData");
        avatarPath = var1.getString("_wwconvobject_avatarPath");
        try {
            message = (WWMediaObject) BaseMessage.parse(var1.getBundle("_wwconvobject_message"));
        }catch (Throwable e){
            Log.d(TAG, "fromBundle", e);
        }
    }
}
