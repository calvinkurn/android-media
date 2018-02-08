package com.tokopedia.inbox.rescenter.detailv2.view.animation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.inbox.R;

/**
 * Created by yfsx on 07/12/17.
 */

public class GlowingView extends BaseView {

    private ImageView ivAnimation1, ivAnimation2;
    private Animation animation1, animation2;
    private Context context;

    public GlowingView(Context context) {
        super(context);
    }

    public GlowingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutView(), this, true);
        this.context = context;
        ivAnimation1 = (ImageView) view.findViewById(R.id.ivAnimation1);
        ivAnimation2 = (ImageView) view.findViewById(R.id.ivAnimation2);
    }

    @Override
    public void setListener(Object listener) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.view_glowing;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {
        animation1 = AnimationUtils.loadAnimation(context, R.anim.anim_shrink);
        animation1.setRepeatCount(Animation.INFINITE);
        ivAnimation1.startAnimation(animation1);


        animation2 = AnimationUtils.loadAnimation(context, R.anim.anim_reverse_shrink);
        animation2.setRepeatCount(Animation.INFINITE);
        ivAnimation2.startAnimation(animation2);
    }

    public void stopAnimation() {
        ivAnimation1.clearAnimation();
        ivAnimation2.clearAnimation();
    }
}
