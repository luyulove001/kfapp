package com.xxl.kfapp.utils;

import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;


public class PayUtil {
    private Context mContext;
    private GetRequest<String> request;

    public PayUtil(Context context, String applyid, String quantity, String price, String amount, String paytype,
                   String ordertype) {
        mContext = context;
        String token = PreferenceUtils.getPrefString(mContext, "token", "1234567890");
        request = OkGo.<String>get(Urls.baseUrl + Urls.createUserOrder)
                .params("token", token)
                .params("applyid", applyid)
                .params("quantity", quantity)
                .params("price", price)
                .params("amount", amount)
                .params("paytype", paytype)
                .params("ordertype", ordertype);
    }

}
