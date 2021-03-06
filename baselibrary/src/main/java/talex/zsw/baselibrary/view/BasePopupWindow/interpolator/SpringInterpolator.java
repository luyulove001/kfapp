package talex.zsw.baselibrary.view.BasePopupWindow.interpolator;

import android.view.animation.LinearInterpolator;

/**
 * Created by 大灯泡 on 2016/1/28.
 * The expression comes from web:
 * http://inloop.github.io/
 */
public class SpringInterpolator extends LinearInterpolator {
    private float factor;

    public SpringInterpolator() {
        this.factor = .4f;
    }
    public SpringInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
