package com.xxl.kfapp.activity.home.boss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.register.RegisterKfsOneActivity;
import com.xxl.kfapp.activity.person.RenameActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.ShopSetVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.SlideFromBottomPopup;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class ShopSettingActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.tv_shopname)
    TextView tvShopName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_applysts)
    TextView tvApplysts;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.lyt_shopname)
    LinearLayout llShopName;
    @Bind(R.id.lyt_price)
    LinearLayout llPrice;
    @Bind(R.id.lyt_open_time)
    LinearLayout llTime;
    @Bind(R.id.lyt_unbind_shop)
    LinearLayout llUnbindShop;
    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.lyt_pic)
    RelativeLayout lytPic;

    private String startTime, endTime, shopName, price, shopid, shoppic;

    private static final int ShopRename = 1;
    private static final int ShopPrice = 2;
    private static final int ShopBusiness = 3;
    private static final int ShopUnBind = 4;
    private ShopSetVo shopSetVo;

    //=======================图片=====================
    public File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    public static final int PHOTO_REQUEST_TAKEPHOTO = 5001;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 5002;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 5003;// 结果
    private String headpic;
    private SlideFromBottomPopup mSlidePopup;
    private Uri imgUri;

    @Override
    protected void initArgs(Intent intent) {
        intent = getIntent();
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        shopName = intent.getStringExtra("shopName");
        price = intent.getStringExtra("price");
        shopid = intent.getStringExtra("shopid");
        shoppic = intent.getStringExtra("shoppic");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_shop_setting);
        ButterKnife.bind(this);
        mTitleBar.setTitle("店铺设置");
        mTitleBar.setBackOnclickListener(this);

        tvShopName.setText(shopName);
        tvPrice.setText(price + "元");
        tvTime.setText(startTime + "-" + endTime);
        if (!TextUtils.isEmpty(shoppic)  && !"null".equals(shoppic))
            Glide.with(getApplicationContext()).load(shoppic).into(ivPic);

        llShopName.setOnClickListener(this);
        llPrice.setOnClickListener(this);
        llTime.setOnClickListener(this);
        llUnbindShop.setOnClickListener(this);
        lytPic.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.lyt_unbind_shop:
                i.setClass(ShopSettingActivity.this, ShopUnbindActivity.class);
                i.putExtra("shopid", shopid);
                startActivityForResult(i, ShopUnBind);
                break;
            case R.id.lyt_open_time:
                i.setClass(ShopSettingActivity.this, ShopTimeSetActivity.class);
                i.putExtra("shopid", shopid);
                i.putExtra("starttime", startTime);
                i.putExtra("endtime", endTime);
                startActivityForResult(i, ShopBusiness);
                break;
            case R.id.lyt_price:
                i.setClass(ShopSettingActivity.this, ShopPriceActivity.class);
                i.putExtra("shopSetVo", shopSetVo);
                startActivityForResult(i, ShopPrice);
                break;
            case R.id.lyt_shopname:
                i.setClass(ShopSettingActivity.this, RenameActivity.class);
                i.putExtra("shopName", shopName);
                i.putExtra("shopid", shopid);
                startActivityForResult(i, ShopRename);
                break;
            case R.id.lyt_pic:
                showHeadPopup();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShopSetInfo();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    /**
     * 设置头像弹出窗
     */
    private void showHeadPopup() {
        mSlidePopup = new SlideFromBottomPopup(this);
        mSlidePopup.setTexts(new String[]{"拍照", "相册选择", "取消"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                Intent portraitIntent;
                switch (v.getId()) {
                    case R.id.tx_1:
                        mSlidePopup.dismiss();
                        // 调用系统的拍照功能
                        portraitIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        portraitIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(portraitIntent, PHOTO_REQUEST_TAKEPHOTO);
                        break;
                    case R.id.tx_2:
                        portraitIntent = new Intent(Intent.ACTION_PICK, null);
                        portraitIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(portraitIntent, PHOTO_REQUEST_GALLERY);
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_3:
                        mSlidePopup.dismiss();
                        break;
                }
            }
        });
        mSlidePopup.showPopupWindow();
    }

    private void getShopSetInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopSetInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                shopSetVo = mGson.fromJson(json.getString("data"), ShopSetVo.class);
                                if ("1".equals(shopSetVo.getApplysts())) {
                                    tvApplysts.setText("解绑审核中");
                                }
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ShopRename:
                    tvShopName.setText(data.getStringExtra("shopName"));
                    break;
                case ShopPrice:
                    tvPrice.setText(data.getStringExtra("price") + "元");
                    break;
                case ShopBusiness:
                    tvTime.setText(data.getStringExtra("business"));
                    break;
                case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                    startPhotoZoom(Uri.fromFile(tempFile), 150);
                    break;
                case PHOTO_REQUEST_GALLERY:// �当选择从本地获取图片时
                    // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                    if (data != null) {
                        startPhotoZoom(data.getData(), 150);
                    }
                    break;
                case PHOTO_REQUEST_CUT:// 返回的结果
                    if (data != null) setPicToView(data);
                    break;
            }
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss", Locale.CHINA);
        return dateFormat.format(date) + ".jpg";
    }

    // 调用系统方法裁剪图片
    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        imgUri = uri;
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    public void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            ivPic.setImageBitmap(photo);
            if (photo != null) {
                doUploadImage(RegisterKfsOneActivity.getRealFilePath(this, imgUri));
            }
        }
    }

    private void doUploadImage(String path) {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>post(Urls.baseUrl + Urls.uploadForApp)
                .tag(this)
                .params("token", token)
                .params("file", new File(path))
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                KLog.e(response.body());
                                headpic = json.getJSONObject("data").getString("path");
                                Glide.with(getApplicationContext()).load(headpic).into(ivPic);
                                updateShopInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void updateShopInfo() {
        String token = PreferenceUtils.getPrefString(BaseApplication.getContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopInfo)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("shoppic", headpic)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
