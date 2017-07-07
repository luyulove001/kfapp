package com.xxl.kfapp.activity.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.utils.Md5Algorithm;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.KeyboardWatcher;

public class FindOrRegisterActivity extends BaseActivity implements KeyboardWatcher.OnKeyboardToggleListener
        , View.OnClickListener {
    @Bind(R.id.phone)
    EditText etPhone;
    @Bind(R.id.code)
    EditText etCode;
    @Bind(R.id.nickname)
    EditText etNickname;
    @Bind(R.id.new_pwd)
    EditText etNewPwd;
    @Bind(R.id.confirm_pwd)
    EditText etConfirm;
    @Bind(R.id.confirm_button)
    Button btnConfirm;
    @Bind(R.id.send)
    Button btnSend;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;

    private String phone, signdata, sign, mid, password, newPwd, code;
    private ProgressDialog mDialog;
    private boolean prompt = true, checkUpResult = true;
    private KeyboardWatcher keyboardWatcher;
    private boolean isForgot = false;

    @Override
    protected void initArgs(Intent intent) {
        setContentView(R.layout.ac_find_register);
        ButterKnife.bind(this);
        if ("forgot".equals(intent.getStringExtra("flag"))) {
            isForgot = true;
            mTitleBar.setTitle("忘记密码");
            etNickname.setVisibility(View.GONE);
        } else if ("register".equals(intent.getStringExtra("flag"))) {
            isForgot = false;
            mTitleBar.setTitle(getString(R.string.action_register));
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        mDialog.setCancelable(false);
        keyboardWatcher = KeyboardWatcher.initWith(this).bindKeyboardWatcher(this);
        btnConfirm.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        mTitleBar.setBackOnclickListener(this);
    }

    @Override
    public void initData() {
        mid = "1";
        sign = System.currentTimeMillis() + "";
        signdata = Md5Algorithm.signMD5("mid=" + mid + "&sign=" + sign);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardWatcher.unbindKeyboardWatcher();
    }


    @Override
    public void onKeyboardShown(int keyboardSize) {
        mScrollView.smoothScrollTo(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager()
                .getDefaultDisplay().getHeight());
    }

    @Override
    public void onKeyboardClosed() {
    }

    private void getData() {
        phone = etPhone.getText().toString().trim();
        password = etNewPwd.getText().toString().trim();
        newPwd = etConfirm.getText().toString().trim();
        code = etCode.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();

        if (phone.equals("") && prompt) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }


        if (!checkPhoneNumber(phone) && prompt) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        if (password.equals("") && prompt) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        if (newPwd.equals("") && prompt) {
            Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        if (!newPwd.equals(password) && prompt) {
            Toast.makeText(this, "新密码不一致", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        if (code.equals("") && prompt) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        if (isForgot) {
            doForgotPwd();
        } else {
            if (nickname.equals("") && prompt) {
                Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                checkUpResult = false;
                prompt = false;
            }
            doRegister();
        }
    }

    private void doForgotPwd() {
        if (checkUpResult) {
            mDialog.show();
            btnConfirm.setEnabled(false);
            OkGo.<String>get(Urls.baseUrl + Urls.forgot)
                    .tag(this)
                    .params("use", "2")
                    .params("phone", phone)
                    .params("password", password)
                    .params("identifyingcode", code)
                    .params("mid", "1")
                    .params("sign", "1")
                    .params("signdata", "56a4fa737b067667f430d3dfbd19fe8a")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            mDialog.dismiss();
                            try {
                                JSONObject json = new JSONObject(response.body());
                                String code = json.getString("code");
                                if (code.equals("100000")) {
                                    startActivity(new Intent(FindOrRegisterActivity.this, MainActivity.class));
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

    public static boolean checkPhoneNumber(String mobiles) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobiles);
        b = m.matches();
        return b;
    }

    private void doRegister() {
        if (checkUpResult) {
            mDialog.show();
            btnConfirm.setEnabled(false);
            OkGo.<String>get(Urls.baseUrl + Urls.register)
                    .tag(this)
                    .params("regfrom", "1")
                    .params("account", phone)
                    .params("password", password)
                    .params("identifyingcode", code)
                    .params("mid", "1")
                    .params("sign", "1")
                    .params("signdata", "56a4fa737b067667f430d3dfbd19fe8a")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            mDialog.dismiss();
                            try {
                                JSONObject json = new JSONObject(response.body());
                                String code = json.getString("code");
                                if (code.equals("100000")) {
                                    startActivity(new Intent(FindOrRegisterActivity.this, MainActivity.class));
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

    private void doGetIdentifyCode() {
        phone = etPhone.getText().toString().trim();
        boolean b = true, c = true;
        if (phone.equals("") && b) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            c = false;
            b = false;
        }
        if (!checkPhoneNumber(phone) && b) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            c = false;
            b = false;
        }

        if (c) {
            btnSend.setEnabled(false);
            mDialog.show();
            String use;
            if (isForgot) {
                use = "2";
            } else {
                use = "1";
            }
            OkGo.<String>get(Urls.baseUrl + Urls.getCode)
                    .tag(this)
                    .params("phone", phone)
                    .params("use", use)
                    .params("mid", "1")
                    .params("sign", "1")
                    .params("signdata", "56a4fa737b067667f430d3dfbd19fe8a")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            btnSend.setEnabled(true);
                            mDialog.hide();
                            try {
                                JSONObject json = new JSONObject(response.body());
                                String code = json.getString("code");
                                if (!code.equals("100000")) {
                                    sweetDialog(json.getString("msg"), 1, false);
                                } else {
                                    sweetDialog(json.getString("msg"), 0, false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                doGetIdentifyCode();
                break;
            case R.id.confirm_button:
                getData();
                break;
            default:
                break;
        }
    }
}
