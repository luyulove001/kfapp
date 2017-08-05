package com.xxl.kfapp.activity.home.jmkd;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.ImageShower;
import com.xxl.kfapp.activity.home.register.RegisterKfsOneActivity;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.adapter.TextAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.FeeListVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.model.response.TextVo;
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

public class JmkdFivePrepayActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.rv_fee)
    RecyclerView rvFee;
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
    @Bind(R.id.tv_endtime)
    TextView tvEndtime;
    @Bind(R.id.lyt_watch)
    LinearLayout lytWatch;
    @Bind(R.id.iv_prepay)
    ImageView ivPrepay;

    private ProgressAdapter progressAdapter;
    private TextAdapter txtAdapter;
    private String imgWatch;
    private SlideFromBottomPopup mSlidePopup;
    private String path, applyid, shopid;
    private int amount = 0;
    private Uri imgUri;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_jmkd_five_prepay);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        mTitleBar.setTitle("开店申请");
        mTitleBar.setBackOnclickListener(this);
        lytWatch.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        applyid = PreferenceUtils.getPrefString(getApplicationContext(), "applyid", "1");
        path = Environment.getExternalStorageDirectory().getPath() + "/myPic/";
        initInfoRecycleView();
        initTextRV();
        doGetShopFeeList(shopid, applyid);
    }

    private void initTextRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        rvFee.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                doUpdateShopApplyInfo(applyid);
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
        pRecyclerView.smoothScrollToPosition(4);
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
                vo.setTag(1);
            } else if (i == 5) {
                vo.setName("装修设备");
            } else if (i == 6) {
                vo.setName("加盟成功");
            }
            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
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

    private void doGetShopFeeList(String shopid, String applyid) {
        String token = PreferenceUtils.getPrefString(getApplicationContext(), "token", "1234567890");
        OkGo.<String>post(Urls.baseUrl + Urls.getShopFeeList)
                .tag(this)
                .params("token", token)
                .params("shopid", shopid)
                .params("applyid", applyid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                List<TextVo> textVos = new ArrayList<>();
                                TextVo textVo;
                                amount = 0;
                                FeeListVo feeListVo = mGson.fromJson(json.getString("data"), FeeListVo.class);
                                for (FeeListVo.Fee fee : feeListVo.getFeelst()) {
                                    textVo = new TextVo();
                                    textVo.setTxt1(fee.getCostname());
                                    textVo.setTxt2(fee.getAmount() + fee.getUnit());
                                    textVos.add(textVo);
                                    amount += Integer.valueOf(fee.getAmount());
                                }
                                textVo = new TextVo();
                                textVo.setTxt1("合计需付款");
                                textVo.setTxt2(amount + "元");
                                textVos.add(textVo);
                                txtAdapter = new TextAdapter(textVos);
                                rvFee.setAdapter(txtAdapter);
                                tvCompanyname.setText(feeListVo.getCompanyname());
                                tvCompanyaccount.setText(feeListVo.getCompanyaccount());
                                tvEndtime.setText(feeListVo.getEnddata());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

    private void doUpdateShopApplyInfo(String applyid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateShopApplyInfo)
                .tag(this)
                .params("token", token)
                .params("prepayway", "3")
                .params("prepay", amount)
                .params("applyid", applyid)
                .params("prepaycert", imgWatch)
                .params("prepaychecksts", "0")
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
