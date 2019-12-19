package com.tencent.wework.api;

import com.tencent.wework.api.model.BaseMessage;

/**
 * Created by hu on 16/5/30.
 */
public interface IWWAPIEventHandler {
    void handleResp(BaseMessage resp);
}
