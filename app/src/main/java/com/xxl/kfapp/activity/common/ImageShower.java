package com.xxl.kfapp.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.widget.ImageLoadingDialog;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageShower extends BaseActivity {
	@Bind(R.id.mImageView)
	ImageView mImageView;
	@Bind(R.id.mTitleBar)
	TitleBar mTitleBar;

	@Override
	protected void initArgs(Intent intent) {

	}

	@Override
	protected void initView(Bundle bundle) {
		setContentView(R.layout.imageshower);
		ButterKnife.bind(this);
		mTitleBar.setTitle("图片显示");
		mTitleBar.setBackOnclickListener(this);
	}

	@Override
	protected void initData() {
		final ImageLoadingDialog dialog = new ImageLoadingDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
			}
		}, 1000);
		Glide.with(getApplicationContext()).load(getIntent().getStringExtra("image")).into(mImageView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
