package com.xxl.kfapp.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.adapter.MenuAdapter;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.AddrVo;
import com.xxl.kfapp.utils.AddressPickTask;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.ListViewDecoration;
import com.xxl.kfapp.widget.TitleBar;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

public class ModifyAddrActivity extends BaseActivity {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.btn_add_addr)
    Button btnAdd;
    @Bind(R.id.mRecyclerView)
    SwipeMenuRecyclerView mMenuRecyclerView;

    private Activity mContext;
    private MenuAdapter mMenuAdapter;
    private List<AddrVo>  vos;
    private String addrId;

    @Override
    protected void initArgs(Intent intent) {
        addrId = getIntent().getStringExtra("addrid");
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_modify_addr);
        ButterKnife.bind(this);
        mContext = this;
        mTitleBar.setTitle("修改地址");
        mTitleBar.setBackOnclickListener(this);
        mTitleBar.getvTvRight().setTextColor(getResources().getColor(R.color.white));
        //侧滑菜单列表
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mMenuRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mMenuRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r1 = new RadioButton(ModifyAddrActivity.this);
                r1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                r1.setText("浙江省杭州市滨江区江南大道xxx号");
                r1.setTextColor(getResources().getColor(R.color.gray));
                r1.setTextSize(getResources().getDimension(R.dimen.x16));
            }
        });

    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.select_orange)// 点击的背景。
                        .setText("编辑")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。

                SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                        .setBackgroundDrawable(R.drawable.bg_btn_red)
                        .setText("删除")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setWidth(width)
                        .setHeight(height);

                swipeRightMenu.addMenuItem(closeItem);
            }
        }
    };

    /**
     * Item点击监听。
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent();
            intent.putExtra("address", vos.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }

            if (menuPosition == 1) {// 删除按钮被点击。
                vos.remove(adapterPosition);
                mMenuAdapter.notifyItemRemoved(adapterPosition);
            }
        }
    };

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
                } else {
                    ToastShow(province.getAreaName() + city.getAreaName() + county.getAreaName());
                }
            }
        });
        task.execute("浙江", "杭州", "滨江");
    }

    @Override
    protected void initData() {
        doGetAddressList();
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    private void doGetAddressList() {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getAddress)
                .tag(this)
                .params("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                JSONArray array = json.getJSONArray("data");
                                vos = new ArrayList<>();
                                for (int i = 0; i < array.size(); i++) {
                                    vos.add(array.getObject(i, AddrVo.class));
                                }
                                mMenuAdapter = new MenuAdapter(vos, addrId);
                                mMenuAdapter.setOnItemClickListener(onItemClickListener);
                                mMenuRecyclerView.setAdapter(mMenuAdapter);
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
