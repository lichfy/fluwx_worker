package com.tencent.wework.api.model;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Created by hu on 16/5/30.
 * µÇÂ½
 */
public class WWAuthMessage {

    public static final int ERR_CANCEL = -1;
    public static final int ERR_OK = 0;
    public static final int ERR_FAIL = 1;


    public static class Req extends WWBaseMessage {

        public String state;
        public String sch;

        public Req() {
        }

        /**
         *
         *
         * @param stat    state
         * @param schema  callback schema
         */
        public Req(String stat, String schema) {
            state = stat;
            sch = schema;
        }

        @Override
        public int getType() {
            return TYPE_AUTH;
        }

        @Override
        public boolean checkArgs() {
            return !TextUtils.isEmpty(appId) && !TextUtils.isEmpty(agentId) && !TextUtils.isEmpty(sch);
        }

        public void toBundle(Bundle var1) {
            var1.putString("_wwauthmsg_state", state);
            var1.putString("_wwauthmsg_schema", sch);
            super.toBundle(var1);
        }

        public void fromBundle(Bundle var1) {
            state = var1.getString("_wwauthmsg_state");
            sch = var1.getString("_wwauthmsg_schema");
            super.fromBundle(var1);
        }
    }

    public static class Resp extends WWBaseRespMessage {
        public String code;
        public String state;
        public int errCode = ERR_CANCEL;
        public String schema;

        @Override
        public int getType() {
            return TYPE_AUTH;
        }

        public void toBundle(Bundle var1) {
            var1.putString("_wwauthrsp_code", code);
            var1.putString("_wwauthrsp_state", state);
            var1.putInt("_wwauthrsp_err", errCode);
            super.toBundle(var1);
        }

        public void fromBundle(Bundle var1) {
            code = var1.getString("_wwauthrsp_code");
            state = var1.getString("_wwauthrsp_state");
            errCode = var1.getInt("_wwauthrsp_err", ERR_CANCEL);
            super.fromBundle(var1);
        }

        @Override
        public Intent toIntent() {
            Intent intent = new Intent(schema, packUri(this));
            intent.setPackage(descPkg);
            Bundle b = new Bundle();
            toBundle(b);
            intent.putExtras(b);
            return intent;
        }

        public Uri toUri() {
            Uri uri = super.toUri();
            uri = uri.buildUpon().scheme(schema).authority("sso").
                    appendQueryParameter("code", code == null ? "" : code).
                    appendQueryParameter("state", state == null ? "" : state).
                    appendQueryParameter("errcode", String.valueOf(errCode)).
                    build();
            return uri;
        }

        public void fromUri(Uri uri) {
            super.fromUri(uri);
            if (uri == null) {
                return;
            }
            errCode = -100;
            try {
                errCode = Integer.parseInt(uri.getQueryParameter("errcode"));
            } catch (Throwable e) {
            }
            code = uri.getQueryParameter("code");
            state = uri.getQueryParameter("state");
            if (errCode == ERR_CANCEL) {
                return;
            }
            if (TextUtils.isEmpty(code)) {
                errCode = ERR_FAIL;
            } else {
                errCode = ERR_OK;
            }
        }
    }
}
