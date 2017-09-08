package com.xxl.kfapp.activity.home.kfs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mobstat.StatService;
import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CaledarTopViewChangeListener;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarTopView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.xxl.kfapp.R;
import com.xxl.kfapp.base.BaseActivity;
import com.xxl.kfapp.model.response.SignListVo;
import com.xxl.kfapp.utils.PreferenceUtils;
import com.xxl.kfapp.utils.Urls;
import com.xxl.kfapp.widget.TitleBar;
import com.xxl.kfapp.zxing.CaptureActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import talex.zsw.baselibrary.util.klog.KLog;

public class SignInActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.mTitleBar)
    TitleBar mTitleBar;
    @Bind(R.id.btn_sign)
    Button btnSign;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.iv_previous)
    ImageView ivPrevious;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;
    @Bind(R.id.tv_sign_in)
    TextView tvSignIn;
    @Bind(R.id.tv_sign_out)
    TextView tvSignOut;
    @Bind(R.id.tv_in_state)
    TextView tvInState;
    @Bind(R.id.tv_out_state)
    TextView tvOutState;
    @Bind(R.id.tv_sign_time)
    TextView tvSignTime;
    @Bind(R.id.tv_sign_success)
    TextView tvSignSuccess;
    @Bind(R.id.tv_success)
    TextView tvSuccess;
    @Bind(R.id.ll_sign_info)
    LinearLayout llSignInfo;
    @Bind(R.id.ll_sign_result)
    LinearLayout llSignResult;

    private SignListVo signListVo;
    public int signFlag;//1 正常，2 迟到，3 早退
    private int[] date;
    private String shopid, month;
    private HashMap<String, SignListVo.SignVo> map;
    private boolean isSignIn, signOutSuccess;

    @Override
    protected void initArgs(Intent intent) {
        shopid = intent.getStringExtra("shopid");
        isSignIn = intent.getBooleanExtra("isSignIn", true);
    }


    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void initView(Bundle bundle) {
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mTitleBar.setTitle("签到");
        mTitleBar.setBackOnclickListener(this);
        llSignInfo.setOnClickListener(this);
        btnSign.setOnClickListener(this);
        ivPrevious.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        date = CalendarUtil.getYMD(new Date());
        llSignResult.setVisibility(View.GONE);
        tvInState.setVisibility(View.GONE);
        tvOutState.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        map = new HashMap<>();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_sign:
                if (signOutSuccess) {
                    ToastShow("您当天已经签退，无法继续签到");
                } else {
                    intent.setClass(SignInActivity.this, CaptureActivity.class);
                    intent.putExtra("isSignIn", isSignIn);
                    intent.putExtra("shopid", shopid);
                    intent.putExtra("startfrom", "SignIn");
                    startActivity(intent);
                }
                break;
            case R.id.iv_previous:
                break;
            case R.id.iv_next:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String today = new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(Calendar.getInstance().getTime());
        getBarberSignInfo(today, shopid);
        StatService.onResume(this);
    }

    private void initView() {
        initCalendarView();
        String mouth = date[0] + "-" + (date[1] > 9 ? date[1] : "0" + date[1]);
        tvDate.setText(mouth);
        String day = mouth + "-" + (date[2] > 9 ? date[2] : "0" + date[2]);
        if (isSignIn) {
            btnSign.setText("签到");
            tvSignSuccess.setText("签到成功");
            tvSuccess.setText("恭喜你，今日上班签到成功！");
            if (map != null) {
                if (map.containsKey(day)) {
                    if (!TextUtils.isEmpty(map.get(day).getFromtime())) {
                        tvSignTime.setText("今日签到时间：" + map.get(day).getFromtime());
                        llSignResult.setVisibility(View.VISIBLE);
                        btnSign.setVisibility(View.GONE);
                    } else if (!TextUtils.isEmpty(map.get(day).getEndtime())) {
                        signOutSuccess = true;
                    }
                } else {
                    btnSign.setVisibility(View.VISIBLE);
                    llSignResult.setVisibility(View.GONE);
                }
            }
        } else {
            btnSign.setText("签退");
            tvSignSuccess.setText("签退成功");
            tvSuccess.setText("恭喜你，今日上班签退成功！");
            if (map != null) {
                if (map.containsKey(day) && !TextUtils.isEmpty(map.get(day).getEndtime())) {
                    tvSignTime.setText("今日签退时间：" + map.get(day).getFromtime());
                    llSignResult.setVisibility(View.VISIBLE);
                    btnSign.setVisibility(View.GONE);
                } else {
                    btnSign.setVisibility(View.VISIBLE);
                    llSignResult.setVisibility(View.GONE);
                }
            }
        }
        initDaySignState(day);

    }

    private void initDaySignState(String day) {
        if (map == null)
            return;
        if (map.containsKey(day)) {
            SignListVo.SignVo vo = map.get(day);
            tvSignIn.setText(vo.getFromtime());
            tvSignOut.setText(vo.getEndtime());
            tvInState.setVisibility(View.VISIBLE);
            tvOutState.setVisibility(View.VISIBLE);
            if ("1".equals(vo.getFromsts())) {
                tvInState.setText("正常");
                tvInState.setBackgroundResource(R.drawable.bg_shape_corner_blue);
            } else if ("2".equals(vo.getFromsts())) {
                tvInState.setText("迟到");
                tvInState.setBackgroundResource(R.drawable.bg_shape_corner_red);
            } else {
                tvInState.setVisibility(View.GONE);
                tvSignIn.setText("还没有记录");
            }
            if ("1".equals(vo.getEndsts())) {
                tvOutState.setText("正常");
                tvOutState.setBackgroundResource(R.drawable.bg_shape_corner_blue);
            } else if ("3".equals(vo.getEndsts())) {
                tvOutState.setText("早退");
                tvOutState.setBackgroundResource(R.drawable.bg_shape_corner_red);
            } else {
                tvOutState.setVisibility(View.GONE);
                tvSignOut.setText("还没有记录");
            }
        } else {
            tvOutState.setVisibility(View.GONE);
            tvSignOut.setText("还没有记录");
            tvInState.setVisibility(View.GONE);
            tvSignIn.setText("还没有记录");
        }
    }

    @SuppressWarnings("deprecation")
    private void initCalendarView() {
        mCalendarDateView.setVisibility(View.VISIBLE);
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_xiaomi, null);
                }

                //TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                View flag = convertView.findViewById(R.id.sign_flag);
                text.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                //chinaText.setText(bean.chinaDay);

                if (bean.year == date[0] && bean.moth == date[1] && bean.day == date[2]) {
                    text.setText("今天");
                    text.setTextColor(Color.WHITE);
                }
                String ym = bean.year + "-" + (bean.moth > 9 ? bean.moth : "0" + bean.moth)
                        + "-" + (bean.day > 9 ? bean.day : "0" + bean.day);
                if (signListVo != null && map != null) {
                    if (map.containsKey(ym)) {
                        flag.setVisibility(View.VISIBLE);
                        GradientDrawable bgShape = (GradientDrawable) flag.getBackground();
                        if ("1".equals(map.get(ym).getFromsts()) && "1".equals(map.get(ym).getEndsts())) {
                            bgShape.setColor(getResources().getColor(R.color.text_cyan));
                        } else {
                            bgShape.setColor(getResources().getColor(R.color.primary_text));
                        }
                    }
                }
                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                month = bean.year + "-" + (bean.moth > 9 ? bean.moth : "0" + bean.moth);
                tvDate.setText(month);
                TextView tv = (TextView) view.findViewById(R.id.text);
                tv.setTextColor(Color.WHITE);
                initDaySignState(month + "-" + (bean.day > 9 ? bean.day : "0" + bean.day));
            }
        });

        mCalendarDateView.setCaledarTopViewChangeListener(new CaledarTopViewChangeListener() {
            @Override
            public void onLayoutChange(CalendarTopView topView) {
                ToastShow(month);
            }
        });
    }

    /**
     * @param date   yyyy-MM
     * @param shopid shopid
     */
    private void getBarberSignInfo(String date, String shopid) {
        String token = PreferenceUtils.getPrefString(getAppApplication(), "token", "1234567890");
        OkGo.<String>get(Urls.baseUrl + Urls.getBarberSignInfo)
                .tag(this)
                .params("token", token)
                .params("ym", date)
                .params("shopid", shopid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        try {
                            com.alibaba.fastjson.JSONObject json = JSON.parseObject(response.body());
                            String code = json.getString("code");
                            if (code.equals("100000")) {
                                KLog.i(response.body());
                                if (!TextUtils.isEmpty(json.getString("data"))) {
                                    signListVo = mGson.fromJson(json.getString("data"), SignListVo.class);
                                    setMapData(signListVo);
                                    initView();
                                }
                            } else {
                                sweetDialog(json.getString("msg"), 1, false);
                            }
                        } catch (com.alibaba.fastjson.JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setMapData(SignListVo signListVo) {
        for (SignListVo.SignVo vo : signListVo.getSignlst()) {
            map.put(vo.getSigndate(), vo);
        }
    }
}
