package com.tencent.wework.api.model;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by hu on 16/5/30.
 * 位置
 */
public class WWMediaLocation extends WWMediaMessage.WWMediaObject {
    private static final String TAG = "WWAPI.WWMediaLocation";
    private static final int LENGTH_LIMIT = 10240;
    public String address;
    public double longitude;
    public double latitude;
    public double zoom;

    public WWMediaLocation(){
    }

    @Override
    public int getType() {
        return WWMediaObject.TYPE_LOC;
    }

    @Override
    public boolean checkArgs() {
        if(!super.checkArgs()){
            return false;
        }
        if (this.address != null && this.address.length() != 0 && this.address.length() <= LENGTH_LIMIT) {
            return true;
        } else {
            Log.d(TAG, "checkArgs fail, text is invalid");
            return false;
        }
    }

    public void toBundle(Bundle var1) {
        var1.putString("_wwlocobject_address", address);
        var1.putDouble("_wwlocobject_longitude", longitude);
        var1.putDouble("_wwlocobject_latitude", latitude);
        var1.putDouble("_wwlocobject_zoom", zoom);
        super.toBundle(var1);
    }

    public void fromBundle(Bundle var1) {
        address = var1.getString("_wwlocobject_address");
        longitude = var1.getDouble("_wwlocobject_longitude");
        latitude = var1.getDouble("_wwlocobject_latitude");
        zoom = var1.getDouble("_wwlocobject_zoom");
        super.fromBundle(var1);
    }
}
