package com.tencent.wework.api.model;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by hu on 16/5/30.
 */
public class WWMediaMessage extends WWBaseMessage {

    private static final String TAG = "WWAPI.WWMediaMessage";
    public static final int THUMB_LENGTH_LIMIT = 32768;
    private static final int TITLE_LENGTH_LIMIT = 512;
    private static final int DESCRIPTION_LENGTH_LIMIT = 1024;
    private static final int MEDIA_TAG_NAME_LENGTH_LIMIT = 64;
    private static final int MESSAGE_ACTION_LENGTH_LIMIT = 2048;
    private static final int MESSAGE_EXT_LENGTH_LIMIT = 2048;
    public String title;
    public String description;
    public byte[] thumbData;
    @Override
    public int getType() {
        return BaseMessage.TYPE_SHARE_MESSAGE;
    }

    public void toBundle(Bundle var1) {
        super.toBundle(var1);
        var1.putString("_wwobject_title", title);
        var1.putString("_wwobject_description", description);
        var1.putByteArray("_wwobject_thumbdata", thumbData);
    }

    public void fromBundle(Bundle var1) {
        super.fromBundle(var1);
        title = var1.getString("_wwobject_title");
        description = var1.getString("_wwobject_description");
        var1.putByteArray("_wwobject_thumbdata", thumbData);
    }

    public final void setThumbImage(Bitmap var1) {
        try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            var1.compress(Bitmap.CompressFormat.JPEG, 85, var2);
            this.thumbData = var2.toByteArray();
            var2.close();
        } catch (Exception var3) {
            var3.printStackTrace();
            Log.d(TAG, "put thumb failed");
        }
    }

    public boolean checkArgs() {
        if(this.thumbData != null && this.thumbData.length > THUMB_LENGTH_LIMIT) {
            Log.d(TAG, "checkArgs fail, thumbData is invalid");
            return false;
        } else if(this.title != null && this.title.length() > TITLE_LENGTH_LIMIT) {
            Log.d(TAG, "checkArgs fail, title is invalid");
            return false;
        } else if(this.description != null && this.description.length() > DESCRIPTION_LENGTH_LIMIT) {
            Log.d(TAG, "checkArgs fail, description is invalid");
            return false;
        }else {
            return true;
        }
    }

    public static abstract class WWMediaObject extends WWMediaMessage {
        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_TEXT = 1;
        public static final int TYPE_IMAGE = 2;
        public static final int TYPE_URL = 5;
        public static final int TYPE_FILE = 6;
        public static final int TYPE_VIDEO = 7;
        public static final int TYPE_CONVERSATION = 8;
        public static final int TYPE_MERGED_CONVERSATION = 9;
        public static final int TYPE_LOC = 10;

        protected int getFileSize(String var1) {
            File var2;
            return var1 != null && var1.length() != 0 ? (!(var2 = new File(var1)).exists() ? 0 : (int) var2.length()) : 0;
        }
    }
}
