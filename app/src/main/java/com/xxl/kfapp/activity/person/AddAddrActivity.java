package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.GetRequest;
import com.xxl.kfapp.R;
import com.xxl.kfapp.activity.common.FindOrRegisterActivity;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.utils.AddressPickTask;
import com.xxl.kfapp.utils.AddressPickTaskAll;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import okhttp3.Request;
import okhttp3.ResponseBody;
import talex.zsw.baselibrary.util.klog.KLog;

public class AddAddrActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.lyt_address)
    RelativeLayout lytAddress;
    @Bind(R.id.location)
    TextView tvLocation;
    @Bind(R.id.et_detail)
    EditText etDetail;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.ll_nickname)
    LinearLayout llNickname;
    @Bind(R.id.ll_phone)
    LinearLayout llPhone;


    private AddrVo vo;
    private String hasAddress;
    private boolean hasAddr;

    @Override
    protected void initArgs(Intent intent) {
        vo = (AddrVo) intent.getSerializableExtra("addrVo");
        hasAddress = intent.getStringExtra("hasAddress");
        hasAddr = "1".equals(hasAddress);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.ac_add_modify_address);
        ButterKnife.bind(this);
        mTitleBar.setTitle("编辑地址");
        mTitleBar.setBackOnclickListener(this);
        if (hasAddr) {
            llNickname.setVisibility(View.GONE);
            llPhone.setVisibility(View.GONE);
        } else {
            llNickname.setVisibility(View.VISIBLE);
            llPhone.setVisibility(View.VISIBLE);
        }
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vo.setAddress(etDetail.getText().toString());
                vo.setUsername(etName.getText().toString());
                vo.setPhone(etPhone.getText().toString());
                if (TextUtils.isEmpty(vo.getAddress())) {
                    ToastShow("请填写详细地址");
                } else if (TextUtils.isEmpty(vo.getAddareacode())) {
                    ToastShow("请先选择地区");
                } else {
                    Intent i = new Intent();
                    i.putExtra("addrVo", vo);
                    setResult(RESULT_OK, i);
                    if (hasAddr)
                        finish();
                }
                if (!hasAddr) {
                    if (TextUtils.isEmpty(vo.getUsername())) {
                        ToastShow("请先填写联系人姓名");
                    } else if (TextUtils.isEmpty(vo.getPhone())) {
                        ToastShow("请先填写联系人手机号");
                    } else if (!FindOrRegisterActivity.checkPhoneNumber(vo.getPhone())) {
                        ToastShow("联系人手机号格式不正确");
                    } else {
                        doUpdateAddr(vo);
                    }
                }
            }
        });
        mTitleBar.getvTvRight().setTextColor(getResources().getColor(R.color.white));
        lytAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressPicker();
            }
        });
    }

    @Override
    protected void initData() {
        if (vo == null) {
            vo = new AddrVo();
        } else {
            etDetail.setText(vo.getAddress());
            tvLocation.setText(vo.getAddprovincename() + vo.getAddcityname() + vo.getAddareaname());
            etName.setText(vo.getUsername());
            etPhone.setText(vo.getPhone());
        }
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
                    ToastShow(province.getAreaName() + city.getAreaName());
                    tvLocation.setText(province.getAreaName() + city.getAreaName());
                    vo.setAddprovincecode(province.getAreaId());
                    vo.setAddprovincename(province.getAreaName());
                    vo.setAddcitycode(province.getAreaId());
                    vo.setAddcityname(province.getAreaName());
                    vo.setAddareacode(city.getAreaId());
                    vo.setAddareaname(city.getAreaName());
                } else {
                    ToastShow(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    tvLocation.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    vo.setAddprovincecode(province.getAreaId());
                    vo.setAddprovincename(province.getAreaName());
                    vo.setAddcitycode(city.getAreaId());
                    vo.setAddcityname(city.getAreaName());
                    vo.setAddareacode(county.getAreaId());
                    vo.setAddareaname(county.getAreaName());
                }
            }
        });
        task.execute("浙江", "杭州", "滨江");
    }

    private void doUpdateAddr(AddrVo vo) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        GetRequest<String> request = OkGo.<String>get(Urls.baseUrl + Urls.updateMemberAddress)
                .tag(this)
                .params("token", token).params("addrid", vo.getAddrid())
                .params("addprovincecode", vo.getAddprovincecode())
                .params("addprovincename", vo.getAddprovincename())
                .params("addcitycode", vo.getAddcitycode())
                .params("addcityname", vo.getAddcityname())
                .params("addareacode", vo.getAddareacode())
                .params("addareaname", vo.getAddareaname())
                .params("address", vo.getAddress())
                .params("username", vo.getUsername())
                .params("phone", vo.getPhone());
        if (!TextUtils.isEmpty(vo.getAddrid())) {
            request.params("addrid", vo.getAddrid());
        }
        request.execute(new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                try {
                    JSONObject json = JSON.parseObject(response.body());
                    String code = json.getString("code");
                    if (code.equals("100000")) {
                        KLog.i(response.body());
                        setResult(RESULT_OK);
                        ToastShow("地址保存成功");
                        finish();
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
