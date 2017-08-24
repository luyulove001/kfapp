package com.xxl.kfapp.activity.home.register;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.FindOrRegisterActivity;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.BarberInfoVo;
import com.xxl.kfapp.model.response.DictVo;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.utils.AddressPickTaskAll;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import talex.zsw.baselibrary.util.DimenUtils;
import talex.zsw.baselibrary.util.klog.KLog;
import talex.zsw.baselibrary.view.SweetSheet.sweetpick.BlurEffect;
import talex.zsw.baselibrary.view.SweetSheet.sweetpick.CustomDelegate;
import talex.zsw.baselibrary.view.SweetSheet.sweetpick.SweetSheet;


/**
 * 作者：XNN
 * 日期：2017/6/7
 * 作用：注册快发师第一步 资料申请
 */

public class RegisterKfsOneActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.next)
    Button next;
    @Bind(R.id.pRecyclerView)
    RecyclerView pRecyclerView;
    @Bind(R.id.idPhoto1)
    ImageView idPhoto1;
    @Bind(R.id.rLayout)
    RelativeLayout rLayout;
    @Bind(R.id.idPhoto2)
    ImageView idPhoto2;
    @Bind(R.id.et_realname)
    EditText etRealname;
    @Bind(R.id.et_idcard)
    EditText etIdcard;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_adress)
    EditText etAddress;
    @Bind(R.id.tv_education)
    TextView tvEducation;
    @Bind(R.id.et_linkman)
    EditText etLinkman;
    @Bind(R.id.et_linktel)
    EditText etLinktel;
    @Bind(R.id.tv_worklife)
    TextView tvWorklife;
    @Bind(R.id.et_certdisc)
    EditText etCertdisc;

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private SweetSheet mSweetSheet;
    private int idPhoto;//判断是正面还是反面，正面1，反面2.

    private BarberInfoVo barberInfoVo;
    private List<DictVo> eduVos, workVos;
    private boolean isRepeat;

    //=======================图片=====================
    public File tempFile =
            new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    public static final int PHOTO_REQUEST_TAKEPHOTO = 5001;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 5002;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 5003;// 结果
    private Bitmap photo = null;
    private Uri imageUri;//照片uri
    private String cardpos, cardneg;

    @Override
    protected void initArgs(Intent intent) {
        isRepeat = intent.getBooleanExtra("isRepeat", false);
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_registerkfs_one);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        idPhoto1.setOnClickListener(this);
        idPhoto2.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        tvEducation.setOnClickListener(this);
        tvWorklife.setOnClickListener(this);
        mTitleBar.setTitle("注册快发师申请");
        mTitleBar.setBackOnclickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();
        barberInfoVo = new BarberInfoVo();
        eduVos = new ArrayList<>();
        workVos = new ArrayList<>();
        doGetDictList("edu_type");
        if (isRepeat) doGetBarberApplyInfo();
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.next:
                getData();
                break;
            case R.id.idPhoto1:
                idPhoto = 1;
                if (mSweetSheet != null) {
                    mSweetSheet.toggle();
                } else {
                    setupCustomView();
                }
                break;
            case R.id.idPhoto2:
                idPhoto = 2;
                if (mSweetSheet != null) {
                    mSweetSheet.toggle();
                } else {
                    setupCustomView();
                }
                break;
            case R.id.tv_address:
                onAddressPicker();
                break;
            case R.id.tv_worklife:
                onOptionPicker(getStrings(workVos), false);
                break;
            case R.id.tv_education:
                onOptionPicker(getStrings(eduVos), true);
                break;
        }
    }

    private void getData() {
        String idcard = etIdcard.getText().toString();
        String address = etAddress.getText().toString();
        String linkman = etLinkman.getText().toString();
        String linktel = etLinktel.getText().toString();
        String realname = etRealname.getText().toString();
        boolean prompt = true, checkUpResult = true;
        if (TextUtils.isEmpty(realname) && prompt) {
            ToastShow("真实姓名不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(idcard) && prompt) {
            ToastShow("身份证号不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (!checkIDCard(idcard) && prompt) {
            ToastShow("身份证号码格式错误");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(cardpos) && prompt) {
            ToastShow("身份证照片不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(cardneg) && prompt) {
            ToastShow("身份证照片不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(address) && prompt) {
            ToastShow("详细地址不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(linkman) && prompt) {
            ToastShow("紧急联系人不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(linktel) && prompt) {
            ToastShow("紧急联系人手机号不能为空");
            prompt = false;
            checkUpResult = false;
        }
        if (!FindOrRegisterActivity.checkPhoneNumber(linktel) && prompt) {
            ToastShow("紧急联系人号码格式错误");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(barberInfoVo.getEducation()) && prompt) {
            ToastShow("请选择学历");
            prompt = false;
            checkUpResult = false;
        }
        if (TextUtils.isEmpty(barberInfoVo.getWorklife()) && prompt) {
            ToastShow("请选择工作年限");
            prompt = false;
            checkUpResult = false;
        }
        if (checkUpResult) {
            barberInfoVo.setCardid(etIdcard.getText().toString());
            barberInfoVo.setCertdisc(etCertdisc.getText().toString());
            barberInfoVo.setAddress(tvAddress.getText().toString());
            barberInfoVo.setLinkman(etLinkman.getText().toString());
            barberInfoVo.setLinktel(etLinktel.getText().toString());
            barberInfoVo.setRealname(etRealname.getText().toString());
            barberInfoVo.setCardpos(cardpos);
            barberInfoVo.setCardneg(cardneg);
            doInsertBarber(barberInfoVo);
        }
    }

    public static boolean checkIDCard(String idcard) {
        Pattern p;
        Matcher m;
        boolean b;
        String regex = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|" +
                "(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        //        p = Pattern.compile("/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/");
        p = Pattern.compile(regex);
        m = p.matcher(idcard);
        b = m.matches();
        return b;
    }

    private String[] getStrings(List<DictVo> vos) {
        String[] strs = new String[vos.size()];
        for (int i = 0; i < vos.size(); i++) {
            strs[i] = vos.get(i).getContent();
        }
        return strs;
    }

    public void onOptionPicker(String[] strs, final boolean isEdu) {
        OptionPicker picker = new OptionPicker(this, strs);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setShadowColor(Color.RED, 40);
        picker.setSelectedIndex(0);
        picker.setCycleDisable(true);
        picker.setTextSize(14);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                if (isEdu) {
                    tvEducation.setText(item);
                    barberInfoVo.setEducation(index + 1 + "");
                } else {
                    tvWorklife.setText(item);
                    barberInfoVo.setWorklife(index + 1 + "");
                }
            }
        });
        picker.show();
    }

    @Override
    public void onBackPressed() {
        if (mSweetSheet != null && mSweetSheet.isShow()) {
            mSweetSheet.dismiss();
        } else {
            super.onBackPressed();
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
        //        pRecyclerView.smoothScrollToPosition();

    }

    private void setData() {
        progressVos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProgressVo vo = new ProgressVo();
            if (i == 0) {
                vo.setName("申请资料");
                vo.setTag(1);
            } else if (i == 1) {
                vo.setName("审核");
            } else if (i == 2) {
                vo.setName("阅读协议");
            } else if (i == 3) {
                vo.setName("考试");
            } else if (i == 4) {
                vo.setName("申请成功");
            }

            progressVos.add(vo);
        }
        progressAdapter.setNewData(progressVos);
    }


    //=================================身份证照片选择=============================================
    private void setupCustomView() {
        hideInputMethod();
        mSweetSheet = new SweetSheet(rLayout);
        int x = DimenUtils.dpToPx(getResources(), 190);
        CustomDelegate customDelegate =
                new CustomDelegate(true, CustomDelegate.AnimationType.DuangLayoutAnimation, x);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_photo, null, false);
        customDelegate.setCustomView(view);
        mSweetSheet.setDelegate(customDelegate, Color.parseColor("#FFFFFF"),
                Color.parseColor("#FFFFFF"));

        //根据设置不同Effect 来显示背景效果BlurEffect:模糊效果.DimEffect 变暗效果
        mSweetSheet.setBackgroundEffect(new BlurEffect(20));

        view.findViewById(R.id.dIvClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet.dismiss();
            }
        });
        view.findViewById(R.id.dTvPhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet.dismiss();
                Intent portraitIntent;
                // 调用系统的拍照功能
                portraitIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                portraitIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(portraitIntent, PHOTO_REQUEST_TAKEPHOTO);
            }
        });
        view.findViewById(R.id.dTvPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSweetSheet.dismiss();
                Intent portraitIntent;
                portraitIntent = new Intent(Intent.ACTION_PICK, null);
                portraitIntent
                        .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(portraitIntent, PHOTO_REQUEST_GALLERY);
            }
        });
        mSweetSheet.show();
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
                if (data != null) {
                    setPicToView(data);
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
        imageUri = uri;
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

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    // 将进行剪裁后的图片显示到UI界面上
    public void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            photo = bundle.getParcelable("data");
            if (idPhoto == 1) {
                idPhoto1.setImageBitmap(photo);
            } else if (idPhoto == 2) {
                idPhoto2.setImageBitmap(photo);
            }
            photo = getRoundedCornerBitmap(photo);
            //            			photo = getRoundedCornerBitmap(photo);
            //            			Drawable drawable = new BitmapDrawable(photo);
            //            			bytes = Bitmap2Bytes(photo);
            if (photo != null) {
                //                uploadPhoto();
                doUploadImage(getRealFilePath(this, imageUri), idPhoto);
            }
        }
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

    /**
     * 将bitmap转换成byte[]
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    private void doInsertBarber(BarberInfoVo vo) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.insertBarberApply)
                .tag(this)
                .params("token", token)
                .params("realname", vo.getRealname())
                .params("cardid", vo.getCardid())
                .params("cardpos", vo.getCardpos())
                .params("cardneg", vo.getCardneg())
                .params("addprovincecode", vo.getAddprovincecode())
                .params("addcitycode", vo.getAddcitycode())
                .params("addareacode", vo.getAddareacode())
                .params("address", vo.getAddress())
                .params("education", vo.getEducation())
                .params("linkman", vo.getLinkman())
                .params("linktel", vo.getLinktel())
                .params("worklife", vo.getWorklife())
                .params("certdisc", vo.getCertdisc())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                doUpdateApplyStatus();
                                startActivity(new Intent(RegisterKfsOneActivity.this, RegisterKfsTwoActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doUpdateApplyStatus() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.updateApplyStatus)
                .tag(this)
                .params("token", token)
                .params("applysts", "11")
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doGetBarberApplyInfo() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberApplyInfo)
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
                                BarberInfoVo vo = mGson.fromJson(json.getString("data"), BarberInfoVo.class);
                                etRealname.setText(vo.getRealname());
                                etAddress.setText(vo.getAddress());
                                etIdcard.setText(vo.getCardid());
                                etLinkman.setText(vo.getLinkman());
                                etLinktel.setText(vo.getLinktel());
                                etCertdisc.setText(vo.getCertdisc());
                                Glide.with(getApplicationContext()).load(vo.getCardpos()).into(idPhoto1);
                                Glide.with(getApplicationContext()).load(vo.getCardneg()).into(idPhoto2);
                                cardneg = vo.getCardneg();
                                cardpos = vo.getCardpos();
                                barberInfoVo.setAddprovincecode(vo.getAddprovincecode());
                                barberInfoVo.setAddprovincename(vo.getAddprovincename());
                                barberInfoVo.setAddcitycode(vo.getAddcitycode());
                                barberInfoVo.setAddcityname(vo.getAddcityname());
                                barberInfoVo.setAddareacode(vo.getAddareacode());
                                barberInfoVo.setAddareaname(vo.getAddareaname());
                                tvAddress.setText(vo.getAddprovincename() + vo.getAddcityname() + vo.getAddareaname());
                                if (!TextUtils.isEmpty(vo.getWorklife()) && mACache.getAsJSONArray("worklife") !=
                                        null) {
                                    tvWorklife.setText(mACache.getAsJSONArray("worklife")
                                            .getJSONObject(Integer.valueOf(vo.getWorklife()) - 1)
                                            .getString("content"));
                                    barberInfoVo.setWorklife(vo.getWorklife());
                                }
                                if (!TextUtils.isEmpty(vo.getEducation()) && mACache.getAsJSONArray("education") !=
                                        null) {
                                    tvEducation.setText(mACache.getAsJSONArray("education")
                                            .getJSONObject(Integer.valueOf(vo.getEducation()) - 1)
                                            .getString("content"));
                                    barberInfoVo.setEducation(vo.getEducation());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onAddressPicker() {
        AddressPickTaskAll task = new AddressPickTaskAll(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTaskAll.Callback() {
            @Override
            public void onAddressInitFailed() {
                ToastShow("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
                if (county == null) {
                    tvAddress.setText(province.getAreaName() + city.getAreaName());
                    barberInfoVo.setAddprovincecode(province.getAreaId());
                    barberInfoVo.setAddprovincename(province.getAreaName());
                    barberInfoVo.setAddcitycode(province.getAreaId());
                    barberInfoVo.setAddcityname(province.getAreaName());
                    barberInfoVo.setAddareacode(city.getAreaId());
                    barberInfoVo.setAddareaname(city.getAreaName());
                } else {
                    tvAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    barberInfoVo.setAddprovincecode(province.getAreaId());
                    barberInfoVo.setAddprovincename(province.getAreaName());
                    barberInfoVo.setAddcitycode(city.getAreaId());
                    barberInfoVo.setAddcityname(city.getAreaName());
                    barberInfoVo.setAddareacode(county.getAreaId());
                    barberInfoVo.setAddareaname(county.getAreaName());
                }
            }
        });
        task.execute("浙江", "杭州", "滨江");
    }

    private void doGetDictList(final String keyname) {
        OkGo.<String>get(Urls.baseUrl + Urls.getDictList)
                .tag(this)
                .params("keyname", keyname)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        DictVo vo;
                        if ("edu_type".equals(keyname)) {
                            eduVos.clear();
                            doGetDictList("work_life");
                        } else if ("work_life".equals(keyname)) {
                            workVos.clear();
                        }
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.getString("code");
                            if (!code.equals("100000")) {
                                sweetDialog(json.getString("msg"), 1, false);
                            } else {
                                JSONArray array = json.getJSONObject("data").getJSONArray("dicts");
                                for (int i = 0; i < array.length(); i++) {
                                    vo = new DictVo();
                                    vo.setContent(array.getJSONObject(i).getString("content"));
                                    vo.setValue(array.getJSONObject(i).getString("value"));
                                    if ("edu_type".equals(keyname)) {
                                        eduVos.add(vo);
                                        if (mACache.getAsJSONArray("education") == null)
                                            mACache.put("education", array);
                                    } else if ("work_life".equals(keyname)) {
                                        workVos.add(vo);
                                        if (mACache.getAsJSONArray("worklife") == null)
                                            mACache.put("worklife", array);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void doUploadImage(String path, final int idPhoto) {
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
                                ToastShow(response.body());
                                KLog.e(response.body());
                                if (idPhoto == 1) {
                                    cardpos = json.getJSONObject("data").getString("path");
                                } else if (idPhoto == 2) {
                                    cardneg = json.getJSONObject("data").getString("path");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns
                    .DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
