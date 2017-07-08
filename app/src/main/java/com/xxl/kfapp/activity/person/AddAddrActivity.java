package com.xxl.kfapp.activity.person;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.utils.AddressPickTask;
import com.xxl.kfapp.widget.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class AddAddrActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.lyt_address)
    RelativeLayout lytAddress;
    @Bind(R.id.location)
    TextView tvLocation;
    private AddrVo vo;

    @Override
    protected void initArgs(Intent intent) {
        vo = (AddrVo) intent.getSerializableExtra("addrVo");
        if (vo == null) {
            vo = new AddrVo();
        }
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.ac_add_modify_address);
        ButterKnife.bind(this);
        mTitleBar.setTitle("编辑地址");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.setRightTV("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("addrVo", vo);
                setResult(RESULT_OK, i);
            }
        });
        lytAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressPicker();
            }
        });
    }

    @Override
    protected void initData() {

    }

    public void onAddressPicker() {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(new AddressPickTask.Callback() {
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
}
