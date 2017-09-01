package com.xxl.kfapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xxl.kfapp.R;

import talex.zsw.baselibrary.view.Animation.ViewAnimator;

/**
 * 项目包名: widget
 * 作用: 顶部有图片的TextView
 * 作者: XNN
 * 日期: 2016 16/4/6 10:38 
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class IconText extends RelativeLayout {
    private TextView mTextView;
    private ImageView mImageView;
    private View mFlag;
    private RelativeLayout mRelativeLayout;

    public static final int DEFAULT_TEXT_SIZE = 9;
    public static final int DEFAULT_TEXT_COLOR = 0xff404040;
    public static final int DEFAULT_SELECTED_COLOR = 0x00000000;
    public static final int DEFAULT_UNSELECTED_COLOR = 0x00000000;

    private String text;
    private int textSelectedColor, textUnselectedColor;
    private float textSize;
    private int padding;
    private int mIconHeight;
    private Drawable selectedIcon, unselectedIcon;
    private boolean selected = false;
    private int selectedColor;
    private int unselectedColor;

    public IconText(Context context) {
        this(context, null);
    }

    public IconText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.widget_icontext, this);

        mTextView = (TextView) rootView.findViewById(R.id.mTextView);
        mImageView = (ImageView) rootView.findViewById(R.id.mImageView);
        mFlag = rootView.findViewById(R.id.mFlag);
        mRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.mRelativeLayout);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconText);
        text = typedArray.getString(R.styleable.IconText_android_text);
        textSize = typedArray.getDimension(
                R.styleable.IconText_IT_text_size, 0);
        textSelectedColor = typedArray.getColor(R.styleable.IconText_IT_text_selected_color,
                DEFAULT_TEXT_COLOR);
        textUnselectedColor = typedArray.getColor(R.styleable.IconText_IT_text_unselected_color,
                DEFAULT_TEXT_COLOR);

        selected = typedArray.getBoolean(R.styleable.IconText_IT_selected, false);
        selectedColor = typedArray.getColor(R.styleable.IconText_IT_selected_color,
                DEFAULT_SELECTED_COLOR);
        unselectedColor = typedArray.getColor(R.styleable.IconText_IT_unselected_color,
                DEFAULT_UNSELECTED_COLOR);

        //		padding = typedArray.getDimensionPixelSize(R.styleable.IconText_IT_padding, 10);
        //		mIconHeight = typedArray.getDimensionPixelSize(R.styleable.IconText_IT_icon_height, 80);
        selectedIcon = typedArray.getDrawable(R.styleable.IconText_IT_selected_icon);
        unselectedIcon = typedArray.getDrawable(R.styleable.IconText_IT_unselected_icon);

        mTextView.setText(text);
        if (textSize != 0) {
            //			mTextView.setTextSize(textSize);
        }
        //		mRelativeLayout.setPadding(padding, padding, padding, padding);

        setSelected(selected);

        //		LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
        //		params.height=mIconHeight;
        //		mImageView.setLayoutParams(params);


        typedArray.recycle();
    }

    public void setText(String string) {
        mTextView.setText(string);
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    public void setTextSize(float size) {
        mTextView.setTextSize(size);
    }

    public void setPadding(int padding) {
        //		mImageView.setPadding(0, 0, 0, padding);
    }

    public void setIcon(int res) {
        mImageView.setImageResource(res);
    }

    public boolean getSelected() {
        return selected;
    }

    public void toggle(boolean doAnim) {
        boolean flag = !selected;
        setSelected(flag);
    }

    public void setFlag(boolean selected) {
        if (selected) mFlag.setVisibility(VISIBLE);
        else mFlag.setVisibility(GONE);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            if (selectedIcon != null) {
                mImageView.setImageDrawable(selectedIcon);
            }
            ViewAnimator.animate(mTextView).textColor(textUnselectedColor, textSelectedColor)
                    .andAnimate(mRelativeLayout).backgroundColor(unselectedColor, selectedColor)
                    .andAnimate(mImageView).scale(1.0f, 1.2f, 1.0f)
                    .duration(200).start();
        } else {
            if (unselectedIcon != null) {
                mImageView.setImageDrawable(unselectedIcon);
            }
            ViewAnimator.animate(mTextView).textColor(textUnselectedColor, textUnselectedColor)
                    .andAnimate(mRelativeLayout).backgroundColor(unselectedColor, unselectedColor)
                    .andAnimate(mImageView).scale(1.0f, 1.0f)
                    .duration(200).start();
        }
    }
}
