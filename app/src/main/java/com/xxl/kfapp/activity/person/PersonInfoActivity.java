package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.home.register.RegisterKfsOneActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.model.response.MemberInfoVo;
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
import talex.zsw.baselibrary.widget.CircleImageView;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.civ_head)
    CircleImageView ivHeadpic;
    @Bind(R.id.lyt_head)
    RelativeLayout lytHead;
    @Bind(R.id.lyt_nickname)
    RelativeLayout lytNickname;
    @Bind(R.id.nickname)
    TextView tvNickname;
    @Bind(R.id.lyt_gender)
    RelativeLayout lytGender;
    @Bind(R.id.gender)
    TextView tvGender;
    @Bind(R.id.job)
    TextView tvJob;
    @Bind(R.id.staffno)
    TextView tvStaffno;
    @Bind(R.id.points)
    TextView tvPoints;
    //=======================图片=====================
    public File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    public static final int PHOTO_REQUEST_TAKEPHOTO = 5001;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 5002;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 5003;// 结果
    private String headpic, sex;
    private SlideFromBottomPopup mSlidePopup;
    private MemberInfoVo memberInfoVo;
    private Uri imgUri;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_head:
                showHeadPopup();
                break;
            case R.id.lyt_nickname:
                startActivityForResult(new Intent(this, RenameActivity.class), 4);
                break;
            case R.id.lyt_gender:
                showGenderPopup();
                break;
        }
    }

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        mTitleBar.setTitle("个人资料");
        mTitleBar.setBackOnclickListener(this);
        lytHead.setOnClickListener(this);
        lytGender.setOnClickListener(this);
        lytNickname.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetMemberInfo();
        StatService.onResume(this);
    }

    private void updateMemberInfo() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateMemberInfo)
                .tag(this)
                .params("token", token)
                .params("headpic", headpic)
                .params("sex", sex)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                ToastShow("修改成功");
                                mACache.put("memberInfoVo", memberInfoVo);//保存个人信息
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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

    /**
     * 设置性别弹出窗
     */
    private void showGenderPopup() {
        mSlidePopup = new SlideFromBottomPopup(this);
        mSlidePopup.setTexts(new String[]{"男", "女", "取消"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        tvGender.setText("男");
                        sex = "1";
                        updateMemberInfo();
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_2:
                        tvGender.setText("女");
                        sex = "2";
                        updateMemberInfo();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
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
            case 4:
                if (resultCode == RESULT_OK)
                {
                    tvNickname.setText(data.getStringExtra("nickname"));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            ivHeadpic.setImageBitmap(photo);
            photo = getRoundedCornerBitmap(photo);
            if (photo != null) {
                doUploadImage(RegisterKfsOneActivity.getRealFilePath(this, imgUri));
            }
        }
    }

    private void doGetMemberInfo() {
        String token = PreferenceUtils.getPrefString(getApplicationContext(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getMemberInfo)
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
                                if (!TextUtils.isEmpty(json.getString("data"))) {
                                    memberInfoVo = mGson.fromJson(json.getString("data"), MemberInfoVo.class);
                                    mACache.put("memberInfoVo", memberInfoVo);//保存个人信息
                                    if (TextUtils.isEmpty(memberInfoVo.getHeadpic())) {
                                        ivHeadpic.setImageResource(R.mipmap.default_head);
                                    } else {
                                        Glide.with(BaseApplication.getContext()).load(memberInfoVo.getHeadpic()).into(ivHeadpic);
                                    }
                                    tvGender.setText("1".equals(memberInfoVo.getSex()) ? "男" : "女");
                                    tvJob.setText("2".equals(memberInfoVo.getJobsts()) ? "老板" : "快发师");
                                    if ("0".equals(memberInfoVo.getJobsts())) tvJob.setText("");
                                    tvNickname.setText(memberInfoVo.getNickname());
                                    tvPoints.setText(memberInfoVo.getIntegrate());
                                    tvStaffno.setText(memberInfoVo.getStaff());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                                memberInfoVo.setHeadpic(headpic);
                                Glide.with(getApplicationContext()).load(headpic).into(ivHeadpic);
                                updateMemberInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取圆形头像
     */
    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap outBitmap =
                Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPX = bitmap.getWidth() / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return outBitmap;
    }
}
