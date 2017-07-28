package com.xxl.kfapp.activity.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.TimePickerDialog;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopPriceActivity extends BaseActivity {

    @Bind(R.id.btn_change_normal)
    Button btnChangeNormal;
    @Bind(R.id.btn_change_active)
    Button btnChangeActive;
    @Bind(R.id.tv_begintime)
    TextView tvBeginTime;
    @Bind(R.id.tv_endtime)
    TextView tvEndTime;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private TimePickerDialog mBeginTimePickerDialog,mEndTimePickerDialog;//开始日期弹出框，结束日期弹出框
    String token;
    String price;//价格
    String beginDate,endDate;//开始日期，结束日期
    EditText etvPrice;//非促销时段价格
    EditText etvUpdatePrice;//促销时段价格
    @Override
    protected void initArgs(Intent intent) {
        intent = getIntent();
        price = intent.getStringExtra("price");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_price);
        ButterKnife.bind(this);
        mTitleBar.setTitle("票价设置");
        mTitleBar.setBackOnclickListener(this);
        btnChangeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePriceDialog();
            }
        });
        mBeginTimePickerDialog = new TimePickerDialog(this);
        mBeginTimePickerDialog.setTimePickerDialogInterface(new TimePickerDialog.TimePickerDialogInterface() {
            @Override
            public void positiveListener() {
                String year = String.valueOf(mBeginTimePickerDialog.getYear());
                String month = String.valueOf(mBeginTimePickerDialog.getMonth());
                String day = String.valueOf(mBeginTimePickerDialog.getDay());
                beginDate = year + "-" + month + "-" + day;
                tvBeginTime.setText(beginDate);
            }

            @Override
            public void negativeListener() {

            }
        });

        mEndTimePickerDialog = new TimePickerDialog(this);
        mEndTimePickerDialog.setTimePickerDialogInterface(new TimePickerDialog.TimePickerDialogInterface() {
            @Override
            public void positiveListener() {
                String year = String.valueOf(mEndTimePickerDialog.getYear());
                String month = String.valueOf(mEndTimePickerDialog.getMonth());
                String day = String.valueOf(mEndTimePickerDialog.getDay());
                endDate = year + "-" + month + "-" + day;
                tvEndTime.setText(endDate);
                showUpdatePriceDialog();
            }
            @Override
            public void negativeListener() {

            }
        });

        btnChangeActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeginTimePickerDialog.showDatePickerDialog();
            }
        });

    }

    @Override
    protected void initData() {
        token = PreferenceUtils.getPrefString(this.getApplicationContext(), "token", "1234567890");
    }

    /**
     * 修改价格接口
     */
    private void changePrice(){
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopPrice)
                .tag(this)
                .params("token", token)
                .params("shopid", "6")
                .params("price",btnChangeNormal.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("修改成功");
                                finish();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 修改促销价格和日期接口
     */
    private void changePriceAndDate(){

        OkGo.<String>get(Urls.baseUrl + Urls.updateShopPrice)
                .tag(this)
                .params("token", token)
                .params("shopid", "6")
                .params("startdate",tvBeginTime.getText().toString())
                .params("enddate",tvEndTime.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("修改成功");
                                finish();
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 非促销时段价格修改弹出框
     */
    private void showChangePriceDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("票价设置");
        View myLayout = View.inflate(this, R.layout.dlg_change_price, null);
        etvPrice = (EditText) myLayout.findViewById(R.id.etv_price);
        builder.setView(myLayout);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(etvPrice.getText().toString().equals("非促销时段票价设定") && etvPrice.getText().toString().equals("")){
                    ToastShow("请输入价格");
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    btnChangeNormal.setText(etvPrice.getText().toString());
                    showConfirmDialog();
                }

            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 促销时段价格修改
     */
    private void showUpdatePriceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("票价设置");
        View myLayout = View.inflate(this, R.layout.dlg_update_price, null);
        etvUpdatePrice = (EditText) myLayout.findViewById(R.id.etv_update_price);
        builder.setView(myLayout);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(etvUpdatePrice.getText().toString().equals("促销时段票价设定") && etvUpdatePrice.getText().toString().equals("")){
                    ToastShow("请输入价格");
                    try {
                        Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                        field.setAccessible(true);
                        field.set(dialog, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    tvPrice.setText(etvUpdatePrice.getText().toString());
                }

            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

    }

    /* @setTitle 设置对话框标题
        * @setMessage 设置对话框消息提示
        * setXXX方法返回Dialog对象，因此可以链式设置属性
        */
    private void showConfirmDialog(){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("非促销票价修改确认");
        normalDialog.setMessage("您是否确定修改非促销价格?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changePrice();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

    /**
     * 促销价格和时段确认
     */
    private void showConfirmTimeOrPriceDialog(){
        final AlertDialog.Builder normalDialog =  new AlertDialog.Builder(this);
        normalDialog.setTitle("促销票价修改确认");
        normalDialog.setMessage("您是否确定修改促销价格与时段?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changePriceAndDate();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }

}
