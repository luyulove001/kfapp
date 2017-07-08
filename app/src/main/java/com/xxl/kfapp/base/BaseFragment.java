package com.xxl.kfapp.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import talex.zsw.baselibrary.util.ACache;
import talex.zsw.baselibrary.util.StringUtils;
import talex.zsw.baselibrary.view.SweetAlertDialog.SweetAlertDialog;


/**
 * 描述:BaseFragment
 */
public abstract class BaseFragment extends Fragment {
    protected abstract void initArgs(Bundle bundle);

    protected abstract void initView(Bundle bundle);

    protected abstract void initData();

    private InputMethodManager mInputMethodManager;
    private LayoutInflater inflater;
    private ViewGroup container;
    public View mView;
    public ACache mACache;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        if (mACache == null)
        {
            mACache = ACache.get(getActivity());
        }
        try {
            initArgs(getArguments());
            initView(savedInstanceState);
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mInputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        hideInputMethod();
        return mView;
    }

    protected void setContentView(int layout) {
        mView = inflater.inflate(layout, container, false);
    }

    /**
     * 显示键盘
     */
    public void showInputMethod(EditText editText) {
        mInputMethodManager.showSoftInput(editText,
                InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    public void hideInputMethod() {
        try {
            //noinspection ConstantConditions
            mInputMethodManager.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ignored) {

        }
    }

    /**
     * @return 获取屏幕宽度
     */
    public int getScrnWeight() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * @return 获取屏幕高度
     */
    public int getScrnHeight() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * 提示信息
     */
    public void ToastShow(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 公共的加载提示对话框
     */
    public SweetAlertDialog sweetAlertDialog;

    /**
     * 成功 错误 警告 异常 提示框(只用来更改提示信息和状态类型和设置是否可以返回取消对话框)
     */
    public void sweetDialog(String information, int type, boolean cancelable) {
        if (sweetAlertDialog == null || !sweetAlertDialog.isShowing()) {
            sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }
        //0正常,1错误,2成功,3警告,5进度条
        sweetAlertDialog.setTitleText(information);
        sweetAlertDialog.changeAlertType(type);
        sweetAlertDialog.setConfirmText("确定");
        sweetAlertDialog.setCancelable(cancelable);//不让点击返回按钮取消对话框
        sweetAlertDialog.show();
    }

    public void sweetContextDialog(String information, String context, int type, boolean cancelable) {
        if (sweetAlertDialog == null || !sweetAlertDialog.isShowing()) {
            sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        }
        //0正常,1错误,2成功,3警告,5进度条
        sweetAlertDialog.setTitleText(information);
        sweetAlertDialog.setContentText(context);
        sweetAlertDialog.changeAlertType(type);
        sweetAlertDialog.setConfirmText("确定");
        sweetAlertDialog.setCancelable(cancelable);//不让点击返回按钮取消对话框
        sweetAlertDialog.show();
    }

    public void sweetDialogCustom(int type, String title, String content, String confirmText,
                                  String cancelText, SweetAlertDialog.OnSweetClickListener confirmListener,
                                  SweetAlertDialog.OnSweetClickListener cancelListener) {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
            sweetAlertDialog.changeAlertType(type);
        } else {
            sweetAlertDialog = new SweetAlertDialog(getActivity(), type);
            sweetAlertDialog.setCancelable(false);
        }
        if (!StringUtils.isBlank(title)) {
            sweetAlertDialog.setTitleText(title);
        }
        if (!StringUtils.isBlank(content)) {
            sweetAlertDialog.setContentText(content);
        }
        if (!StringUtils.isBlank(confirmText)) {
            sweetAlertDialog.setConfirmText(confirmText);
        }
        if (!StringUtils.isBlank(cancelText)) {
            sweetAlertDialog.setCancelText(cancelText);
        }
        if (confirmListener != null) {
            sweetAlertDialog.setConfirmClickListener(confirmListener);
        }
        if (confirmListener != null) {
            sweetAlertDialog.setCancelClickListener(cancelListener);
        }
        if (!sweetAlertDialog.isShowing()) {
            sweetAlertDialog.show();
        }
    }

    /**
     * 改变对话框内容
     */
    public void editDialog(String information, int type, boolean cancelable) {
        if (sweetAlertDialog != null) {
            sweetAlertDialog.setTitleText(information);
            sweetAlertDialog.changeAlertType(type);
            sweetAlertDialog.setConfirmText("确定");
            sweetAlertDialog.setCancelable(cancelable);//不让点击返回按钮取消对话框
            sweetAlertDialog.show();
        }
    }



    /**
     * 关闭提示框
     */
    public void closeDialog() {
        if (sweetAlertDialog != null) {
            sweetAlertDialog.dismiss();
        }
    }

}
