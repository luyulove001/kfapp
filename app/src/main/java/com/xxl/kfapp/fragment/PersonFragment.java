package com.xxl.kfapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.LoginActivity;
import com.xxl.kfapp.activity.person.ModifyAddrActivity;
import com.xxl.kfapp.activity.person.RenameActivity;
import com.xxl.kfapp.base.BaseApplication;
import com.xxl.kfapp.base.BaseFragment;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.model.response.MemberInfoVo;
import com.xxl.kfapp.widget.SlideFromBottomPopup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.view.PullZoomView.PullToZoomScrollViewEx;
import talex.zsw.baselibrary.view.SweetAlertDialog.SweetAlertDialog;
import talex.zsw.baselibrary.widget.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * 作者：XNN
 * 日期：2017/6/1
 * 作用：我的
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener {
    private TextView tvNickname, tvGender, tvJob, tvJobNum, tvPoints, tvOrder, tvAddress, tvNotify, tv_nickname;
    private RelativeLayout lytNickname, lytGender, lytOrder, lytNotify, lytHead;
    private LinearLayout lytAddress;
    private CircleImageView ivHead;
    private Button btnLogout;
    private SlideFromBottomPopup mSlidePopup;
    private Bitmap head;//头像Bitmap
    private String path;//sd路径
    private AddrVo address;
    private Drawable male, female;

    @Bind(R.id.mScrollView)
    PullToZoomScrollViewEx mScrollView;

    private MemberInfoVo memberInfoVo;

    @Override
    protected void initArgs(Bundle bundle) {

    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.fragment_person);
        ButterKnife.bind(this, mView);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.personal_head_view, null, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.personal_zoom_view, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.personal_content_view, null, false);
        mScrollView.setHeaderView(headView);
        mScrollView.setZoomView(zoomView);
        mScrollView.setScrollContentView(contentView);

        ivHead = (CircleImageView) headView.findViewById(R.id.mHead);
        lytHead = (RelativeLayout) headView.findViewById(R.id.lyt_head);
        tv_nickname = (TextView) headView.findViewById(R.id.tv_nickname);

        tvNickname = (TextView) contentView.findViewById(R.id.nickname);
        tvGender = (TextView) contentView.findViewById(R.id.gender);
        tvJob = (TextView) contentView.findViewById(R.id.job);
        tvJobNum = (TextView) contentView.findViewById(R.id.job_num);
        tvPoints = (TextView) contentView.findViewById(R.id.points);
        tvOrder = (TextView) contentView.findViewById(R.id.order);
        tvAddress = (TextView) contentView.findViewById(R.id.address);
        tvNotify = (TextView) contentView.findViewById(R.id.notification);
        lytNickname = (RelativeLayout) contentView.findViewById(R.id.lyt_nickname);
        lytGender = (RelativeLayout) contentView.findViewById(R.id.lyt_gender);
        lytOrder = (RelativeLayout) contentView.findViewById(R.id.lyt_order);
        lytAddress = (LinearLayout) contentView.findViewById(R.id.lyt_address);
        lytNotify = (RelativeLayout) contentView.findViewById(R.id.lyt_notify);
        btnLogout = (Button) contentView.findViewById(R.id.btn_logout);

        lytNickname.setOnClickListener(this);
        lytGender.setOnClickListener(this);
        lytOrder.setOnClickListener(this);
        lytAddress.setOnClickListener(this);
        lytNotify.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        lytHead.setOnClickListener(this);

        lytOrder.setVisibility(View.GONE);
        initDrawables();
    }

    @Override
    protected void initData() {
        path = Environment.getExternalStorageDirectory().getPath() + "/myHead/";
        setMemberInfo();
    }

    @SuppressWarnings("deprecation")
    private void initDrawables() {
        male = getActivity().getResources().getDrawable(R.mipmap.main_icon_boy);
        female = getActivity().getResources().getDrawable(R.mipmap.main_icon_girl);
        male.setBounds(0, 0, male.getMinimumWidth(), male.getMinimumHeight());
        female.setBounds(0, 0, female.getMinimumWidth(), female.getMinimumHeight());
    }

    private void setMemberInfo() {
        memberInfoVo = (MemberInfoVo) mACache.getAsObject("memberInfoVo");
        Glide.with(BaseApplication.getContext()).load(memberInfoVo.getHeadpic()).into(ivHead);
        tvNickname.setText(memberInfoVo.getNickname());
        if ("1".equals(memberInfoVo.getSex())) {
            tv_nickname.setCompoundDrawables(null, null, male, null);
        } else if ("2".equals(memberInfoVo.getSex())) {
            tv_nickname.setCompoundDrawables(null, null, female, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_nickname:
                startActivityForResult(new Intent(getActivity(), RenameActivity.class), 4);
                break;
            case R.id.lyt_gender:
                showGenderPopup();
                break;
            case R.id.lyt_order:
                break;
            case R.id.lyt_address:
                Intent i = new Intent(getActivity(), ModifyAddrActivity.class);
                if (address != null) {
                    i.putExtra("addrid", address.getAddrid());
                }
                startActivityForResult(i, 5);
                break;
            case R.id.lyt_notify:
                break;
            case R.id.lyt_head:
                showHeadPopup();
                break;
            case R.id.btn_logout:
                sweetDialogCustom(0, "是否确定退出登录", "", "确定退出", "取消", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                }, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        closeDialog();
                    }
                });
                break;
        }
    }

    /**
     * 设置头像弹出窗
     */
    private void showHeadPopup() {
        mSlidePopup = new SlideFromBottomPopup(getActivity());
        mSlidePopup.setTexts(new String[]{"拍照", "相册选择", "取消"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
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

    /**
     * 设置性别弹出窗
     */
    private void showGenderPopup() {
        mSlidePopup = new SlideFromBottomPopup(getActivity());
        mSlidePopup.setTexts(new String[]{"男", "女", "取消"});
        mSlidePopup.setOnItemClickListener(new SlideFromBottomPopup.OnItemClickListener() {
            @Override
            public void onItemClick(View v) {
                switch (v.getId()) {
                    case R.id.tx_1:
                        tvGender.setText("男");
                        mSlidePopup.dismiss();
                        break;
                    case R.id.tx_2:
                        tvGender.setText("女");
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
                    head = extras.getParcelable("data");
                    if (head != null) {
                        //上传服务器代码
                        setPicToView(head);//保存在SD卡中
                        ivHead.setImageBitmap(head);//用ImageView显示出来
                    }
                }
                break;
            case 4:
                if (data != null) {
                    tvNickname.setText(data.getStringExtra("nickname"));
                }
                break;
            case 5:
                if (resultCode == RESULT_OK && data != null) {
                    address = (AddrVo) data.getSerializableExtra("address");
                    tvAddress.setText(address.getDispaddress());
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
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
        String fileName = path + "head.jpg";//图片名字
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
