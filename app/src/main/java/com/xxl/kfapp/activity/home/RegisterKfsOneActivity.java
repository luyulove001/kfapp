package com.xxl.kfapp.activity.home;

import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.ProgressAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.ProgressVo;
import com.xxl.kfapp.widget.TitleBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.DimenUtils;
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

    private ProgressAdapter progressAdapter;
    private List<ProgressVo> progressVos;
    private SweetSheet mSweetSheet;
    private int idPhoto;//判断是正面还是反面，正面1，反面2.

    //=======================图片=====================
    public File tempFile =
            new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    public static final int PHOTO_REQUEST_TAKEPHOTO = 5001;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 5002;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 5003;// 结果
    private Bitmap photo = null;

    @Override
    protected void initArgs(Intent intent) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_registerkfs_one);
        ButterKnife.bind(this);
        next.setOnClickListener(this);
        idPhoto1.setOnClickListener(this);
        idPhoto2.setOnClickListener(this);
        mTitleBar.setTitle("注册快发师申请");
        mTitleBar.setBackOnclickListener(this);
    }

    @Override
    protected void initData() {
        initInfoRecycleView();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.next:
                startActivity(new Intent(this, RegisterKfsTwoActivity.class));
                finish();
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
        }
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
                vo.setName("申请加盟");
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    // 调用系统方法裁剪图片
    public void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 9);

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
            //			photo = getRoundedCornerBitmap(photo);
            //			Drawable drawable = new BitmapDrawable(photo);
            //			bytes = Bitmap2Bytes(photo);
            if (photo != null) {
//                uploadPhoto();
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


}
