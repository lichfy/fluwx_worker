package com.tencent.wework.api.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by hu on 16/8/1.
 * 消息合并转发
 */
public class WWMediaMergedConvs extends WWMediaMessage.WWMediaObject {
    private static final String TAG = "WWAPI.WWMediaConv";
    /**
     * 消息列表 可以是 这里定义的各种消息和集合
     */
    public List<WWMediaConversation> messages = new ArrayList<WWMediaConversation>();

    public boolean addItem(WWMediaConversation wc){
        if(wc != null && wc.checkArgs()){
            messages.add(wc);
            return true;
        }
        return false;
    }

	@Override
	public int getType() {
		return WWMediaMessage.WWMediaObject.TYPE_MERGED_CONVERSATION;
	}

	@Override
	public boolean checkArgs() {
		if (!super.checkArgs()) {
			return false;
		}
		if (this.messages == null || this.messages.size() == 0) {
			Log.d(TAG, "checkArgs fail, all arguments are null");
			return false;
		}
		boolean ret = true;
		for (BaseMessage msg : messages) {
			if (!msg.checkArgs()) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	public void toBundle(Bundle var1) {
		var1.putInt("_wwmergedconvobject_messageslen", messages.size());
		for (int i = 0; i < messages.size(); i++) {
			var1.putBundle("_wwmergedconvobject_messages" + i, BaseMessage.pack(messages.get(i)));
		}
		super.toBundle(var1);
	}

	public void fromBundle(Bundle var1) {
		int len = var1.getInt("_wwmergedconvobject_messageslen");
		for (int i = 0; i < len; i++) {
			BaseMessage msg = BaseMessage.parse(var1.getBundle("_wwmergedconvobject_messages" + i));
			if (msg != null && msg instanceof WWMediaConversation) {
				messages.add((WWMediaConversation) msg);
			} else {
				Log.d(TAG, "fromBundle " + (msg==null?"null":msg));
			}
		}
		super.fromBundle(var1);
	}
}
