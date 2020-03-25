package com.tencent.wework.api.model;

import android.os.Bundle;
import android.util.Log;

import com.tencent.wework.api.model.WWMediaMessage.WWMediaObject;

/**
 * Created by hu on 16/5/30.
 * 文件
 */
public class WWMediaFile extends WWMediaObject {
    private static final String TAG = "WWAPI.WWMediaFile";
    private int contentLengthLimit = 10485760;
    /**
     * 文件二进制数据
     */
    public byte[] fileData;
    /**
     * 文件路径
     */
    public String filePath;
    /**
     * 文件名称
     */
    public String fileName;

    @Override
    public int getType() {
        return WWMediaObject.TYPE_FILE;
    }

    public boolean checkArgs() {
        if(!super.checkArgs()){
            return false;
        }
        if (this.fileData != null && this.fileData.length != 0 || this.filePath != null && this.filePath.length() != 0) {
            if (this.fileData != null && this.fileData.length > this.contentLengthLimit) {
                Log.d(TAG, "checkArgs fail, fileData is too large");
                return false;
            } else if (this.filePath != null && this.getFileSize(this.filePath) > this.contentLengthLimit) {
                Log.d(TAG, "checkArgs fail, fileSize is too large");
                return false;
            } else {
                return true;
            }
        } else {
            Log.d(TAG, "checkArgs fail, both arguments is null");
            return false;
        }
    }

    public void toBundle(Bundle var1) {
        var1.putByteArray("_wwfileobject_fileData", fileData);
        var1.putString("_wwfileobject_filePath", filePath);
        var1.putString("_wwfileobject_fileName", fileName);
        super.toBundle(var1);
    }

    public void fromBundle(Bundle var1) {
        fileData = var1.getByteArray("_wwfileobject_fileData");
        filePath = var1.getString("_wwfileobject_filePath");
        fileName = var1.getString("_wwfileobject_fileName");
        super.fromBundle(var1);
    }

    public void setContentLengthLimit(int var1) {
        this.contentLengthLimit = var1;
    }
}
