package com.xxl.kfapp.base;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.xxl.kfapp.utils.AuthResult;
import com.xxl.kfapp.utils.PayResult;

import java.util.Map;

public abstract class PayHandler extends Handler {

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;

    @SuppressWarnings("unchecked")
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SDK_PAY_FLAG: {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                payResult(resultStatus); //具体用到时实现
//                if (TextUtils.equals(resultStatus, "9000")) {
//                    ToastShow("支付成功");
//                } else {
//                    ToastShow("支付失败");
//                }
                break;
            }
            case SDK_AUTH_FLAG: {
                AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                String resultStatus = authResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                    ToastShow("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                } else {
                    ToastShow("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                }
                break;
            }
            default:
                break;
        }
    }
    private void ToastShow(String text) {
        Toast.makeText(BaseApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public abstract void payResult(String resultStatus);
}
