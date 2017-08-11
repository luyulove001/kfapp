package com.xxl.kfapp.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.xxl.kfapp.R;


@SuppressLint({"ResourceAsColor", "DrawAllocation"})
public class LinedEditText extends TextView {
//    private Paint mPaint = new Paint();  


    public LinedEditText(Context context) {
        super(context);
        initPaint();
    }


    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }


    public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }


    private void initPaint() {
//        mPaint.setStyle(Paint.Style.STROKE);  
////        mPaint.setColor(0x80000000);   
//      mPaint.setStyle(Paint.Style.STROKE);    
//      mPaint.setColor(R.color.dashed_line_color);           
//        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);    
//        mPaint.setPathEffect(effects);  
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
//       mPaint.setColor(0x80000000);   
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.main_red));
        PathEffect effects = new DashPathEffect(new float[]{6, 6, 6, 6}, 0);
        mPaint.setPathEffect(effects);

        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int spcingHeight = (int) getLineSpacingExtra();
        int count = (height - paddingTop - paddingBottom) / lineHeight;


        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i + 1) + paddingTop - spcingHeight / 2;
            canvas.drawLine(left + paddingLeft, baseline, right - paddingRight, baseline, mPaint);
        }


        super.onDraw(canvas);
    }
}