package com.tencent.wework.api.model;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

/**
 * Created by hu on 16/5/30.
 *
 */
public class WWMediaImage extends WWMediaFile {
    private static final String TAG = "WWAPI.WWMediaImage";

    public WWMediaImage() {
    }

    public WWMediaImage(byte[] var1) {
        fileData = var1;
    }

    public WWMediaImage(Bitmap var1) {
        try {
            ByteArrayOutputStream var2 = new ByteArrayOutputStream();
            var1.compress(Bitmap.CompressFormat.JPEG, 85, var2);
            fileData = var2.toByteArray();
            var2.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public int getType() {
        return WWMediaMessage.WWMediaObject.TYPE_IMAGE;
    }
}
