package com.tencent.wework.api;

import android.content.Intent;

import com.tencent.wework.api.model.BaseMessage;

public interface IWWAPI {
    /**
     *
     * @param schema
     * @return
     */
    boolean registerApp(String schema);

    void unregisterApp();

    boolean handleIntent(Intent var1, IWWAPIEventHandler var2);

    /**
     *
     * @return 是否安装了企业微信
     */
    boolean isWWAppInstalled();

    /**
     *
     * @return 是否支持api
     */
    boolean isWWAppSupportAPI();

    /**
     *
     * @return 安装的企业微信版本
     */
    int getWWAppSupportAPI();

    /**
     * 打开企业微信
     * @return
     */
    boolean openWWApp();

    /**
     *
     * @param 发送的消息
     * @return 消息是否合法
     */
    boolean sendMessage(BaseMessage var1);

    /**
     *
     * @param var1 发送的包
     * @param callback 回包
     * @return 消息是否合法
     */
    boolean sendMessage(BaseMessage var1, IWWAPIEventHandler callback);
    void detach();
}
