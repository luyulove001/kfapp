package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.ImageShower;
import com.xxl.kfapp.activity.home.register.RegisterKfsOneActivity;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AppConfigVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.ShopApplyStatusVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.SlideFromBottomPopup;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class JmkdSixDeviceActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.mScrollView)
    ScrollView mScrollView;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.tv_companyaccount)
    TextView tvCompanyaccount;
    @Bind(R.id.tv_companyname)
    TextView tvCompanyname;
    @Bind(R.id.lyt_watch)
    LinearLayout lytWatch;
    @Bind(R.id.iv_prepay)
    ImageView ivPrepay;
    @Bind(R.id.ll_btn1)
    LinearLayout llBtn1;
    @Bind(R.id.ll_trans)
    LinearLayout llTrans;
    @Bind(R.id.next1)
    Button next1;
    @Bind(R.id.tv_checking)
    TextView tvChecking;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.ll_btn)
    LinearLayout llBtn;
    @Bind(R.id.tv_fixedreason)
    TextView tvFixedReason;
    @Bind(R.id.tv_customreason)
    TextView tvCustomReason;
    @Bind(R.id.lyt_reason_shsb)
    LinearLayout lytReasonShsb;
    @Bind(R.id.ll_checking)
    LinearLayout llChecking;
    @Bind(R.id.ll_check)
    LinearLayout llCheck;

    private ProgressAdapter progressAdapter;
    private String imgWatch, gusertel, gusername, guseraddr;
    private SlideFromBottomPopup mSlidePopup;
    private String path, applyid;
    private int amount = 0;
    private Uri imgUri;
    private ShopApplyStatusVo applyStatusVo;

    @Override
    protected void initArgs(Intent intent) {
        amount = intent.getIntExtra("amount", 0);
        gusertel = intent.getStringExtra("phone");
        gusername = intent.getStringExtra("nickname");
        guseraddr = intent.getStringExtra("address");

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_six_device);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        lytWatch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        next1.setOnClickListener(this);
        initDrawables();
    }

    @Override
    protected void initData() {
        path = Environment.getExternalStorageDirectory().getPath() + "/myPic/";
        initInfoRecycleView();
        AppConfigVo vo = (AppConfigVo) mACache.getAsObject("appConfig");
        tvCompanyname.setText(vo.getTranscompanyname());
        tvCompanyaccount.setText(vo.getTransbankinfo());
        doGetShopApplyStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                doUpdateShopApplyInfo();
                break;
            case R.id.lyt_watch:
                if (!TextUtils.isEmpty(imgWatch)) {
                    Intent i = new Intent(this, ImageShower.class);
                    i.putExtra("image", imgWatch);
                    startActivity(i);
                } else {
                    showHeadPopup();
                }
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.next1:
                startActivity(new Intent(JmkdSixDeviceActivity.this, JmkdSixActivity.class));
                finish();
                break;
        }
    }

    /**
     * 初始化progress列表
     */
    private void initInfoRecycleView() {

        progressAdapter = new ProgressAdapter(new ArrayList<ProgressVo>(), getScrnWeight() / 4);
        pRecyclerView.setAdapter(progressAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        pRecyclerView.setLayoutManager(layoutManager);
        setData();
        pRecyclerView.smoothScrollToPosition(6);
    }

    private void setData() {
        List<ProgressVo> progressVos = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请加盟");
                vo.setTag(2);
            } else if (i == 1) {
                vo.setName("审核");
                vo.setTag(2);
            } else if (i == 2) {
                vo.setName("阅读协议");
                vo.setTag(2);
            } else if (i == 3) {
                vo.setName("品牌保证金");
                vo.setTag(2);
            } else if (i == 4) {
                vo.setName("选址");
                vo.setTag(2);
            } else if (i == 5) {
                vo.setName("装修设备");
                vo.setTag(1);
            } else if (i == 6) {
                vo.setName("加盟成功");
            }
            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }

    private void doGetShopApplyStatus() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getShopApplyStatus)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                KLog.i(response.body());
                                llCheck.setVisibility(View.VISIBLE);
                                applyStatusVo = mGson.fromJson(json.getString("data"), ShopApplyStatusVo.class);
                                applyid = applyStatusVo.getApplyid();
                                if (TextUtils.isEmpty(gusertel)) {
                                    if (!TextUtils.isEmpty(applyStatusVo.getDevicechecksts())
                                            && !"null".equals(applyStatusVo.getDevicechecksts())) {
                                        if ("0".equals(applyStatusVo.getDevicechecksts())) {
                                            AppConfigVo appConfig = (AppConfigVo) mACache.getAsObject("appConfig");
                                            tvChecking.setText("设备费凭证审核中，一般" + appConfig.getTranscheckdays()
                                                    + "个工作日，请耐心等待");
                                            llBtn.setVisibility(View.GONE);
                                            llBtn1.setVisibility(View.GONE);
                                            llTrans.setVisibility(View.GONE);
                                        } else if ("2".equals(applyStatusVo.getDevicechecksts())) {
                                            llBtn1.setVisibility(View.VISIBLE);
                                            llBtn.setVisibility(View.GONE);
                                            llTrans.setVisibility(View.GONE);
                                            tvTips.setVisibility(View.VISIBLE);
                                            lytReasonShsb.setVisibility(View.VISIBLE);
                                            tvChecking.setText("真遗憾，您的审核未通过");
                                            tvChecking.setCompoundDrawablesRelative(fair, null, null, null);
                                            if (!TextUtils.isEmpty(applyStatusVo.getDevicereason())) {
                                                tvFixedReason.setText(applyStatusVo.getDevicereason());
                                                tvFixedReason.setVisibility(View.VISIBLE);
                                            }
                                            //if (!TextUtils.isEmpty(applyStatusVo.getCustomreason()))
                                            //tvCustomReason.setText(applyStatusVo.getCustomreason());
                                            next1.setText("重新上传");
                                        }
                                    } else {
                                        showTrans();
                                    }
                                } else {
                                    showTrans();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showTrans() {
        llBtn.setVisibility(View.VISIBLE);
        llBtn1.setVisibility(View.GONE);
        llChecking.setVisibility(View.GONE);
        llTrans.setVisibility(View.VISIBLE);
    }

    private Drawable pass, fair;

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        pass = getResources().getDrawable(R.mipmap.sh_cg);
        fair = getResources().getDrawable(R.mipmap.sh_sb);
        pass.setBounds(0, 0, pass.getMinimumWidth(), pass.getMinimumHeight());
        fair.setBounds(0, 0, fair.getMinimumWidth(), fair.getMinimumHeight());
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
                switch (v.getId()) {
                    case R.id.tx_1:
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "head.jpg")));
                        mSlidePopup.dismiss();
                        startActivityForResult(intent2, 2);//采用ForResult打开
                        break;
                    case R.id.tx_2:
                        Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        mSlidePopup.dismiss();
                        startActivityForResult(intent1, 1);
                        break;
                    case R.id.tx_3:
                        mSlidePopup.dismiss();
                        break;
                }
            }
        });
        mSlidePopup.showPopupWindow();
    }


    private void doUploadImage(String path) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
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
                                imgWatch = json.getJSONObject("data").getString("path");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doUpdateShopApplyInfo() {
        if (TextUtils.isEmpty(imgWatch)) {
            ToastShow("请先上传凭证照片");
        } else {
            String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
            OkGo.<String>get(Urls.baseUrl + Urls.updateShopApplyInfo)
                    .tag(this)
                    .params("token", token)
                    .params("devpayway", "3")
                    .params("deviceamount", amount)
                    .params("applyid", applyid)
                    .params("devicecert", imgWatch)
                    .params("devicecertsts", "0")
                    .params("devicechecksts", "0")
                    .params("gusertel", gusertel)
                    .params("gusername", gusername)
                    .params("guseraddr", guseraddr)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            try {
                                JSONObject json = new JSONObject(response.body());
                                String code = json.getString("code");
                                if (!code.equals("100000")) {
                                    sweetDialog(json.getString("msg"), 1, false);
                                } else {
                                    KLog.d(json.getString("data"));
                                    ToastShow(json.getString("msg"));
                                    showChecking();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void showChecking() {
        llChecking.setVisibility(View.VISIBLE);
        llTrans.setVisibility(View.GONE);
        llBtn1.setVisibility(View.GONE);
        llBtn.setVisibility(View.GONE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap head = extras.getParcelable("data");
                    if (head != null) {
                        //上传服务器代码
                        doUploadImage(RegisterKfsOneActivity.getRealFilePath(this, imgUri));
                        setPicToView(head);//保存在SD卡中
                        ivPrepay.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 调用系统的裁剪
     */
    public void cropPhoto(Uri uri) {
        imgUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + System.currentTimeMillis() + ".jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}