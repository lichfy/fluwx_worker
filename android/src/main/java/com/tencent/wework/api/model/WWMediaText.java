package com.tencent.wework.api.model;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by hu on 16/5/30.
 * 文本
 */
public class WWMediaText extends WWMediaMessage.WWMediaObject {
    private static final String TAG = "WWAPI.WWMediaText";
    private static final int LENGTH_LIMIT = 10240;
    /**
     * 文本
     */
    public String text;

    public WWMediaText(){
    }

    public WWMediaText(String txt){
        text = txt;
    }

    @Override
    public int getType() {
        return WWMediaMessage.WWMediaObject.TYPE_TEXT;
    }

    @Override
    public boolean checkArgs() {
        if(!super.checkArgs()){
            return false;
        }
        if (this.text != null && this.text.length() != 0 && this.text.length() <= LENGTH_LIMIT) {
            return true;
        } else {
            Log.d(TAG, "checkArgs fail, text is invalid");
            return false;
        }
    }

    public void toBundle(Bundle var1) {
        var1.putString("_wwtextobject_text", text);
        super.toBundle(var1);
    }

    public void fromBundle(Bundle var1) {
        text = var1.getString("_wwtextobject_text");
        super.fromBundle(var1);
    }
}
