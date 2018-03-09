package com.tokopedia.shop.product.view.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.shop.R;

/**
 * Created by normansyahputa on 3/9/18.
 */

public class ShopWarningTickerView extends FrameLayout {

    private TextView tvAction;
    private TextView tvDescription;
    private TextView tvTitle;
    private ImageView ivIcon;
    private View container;

    public ShopWarningTickerView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShopWarningTickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShopWarningTickerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ShopWarningTickerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dashboard_shop_close, this, false);
        container = view.findViewById(R.id.vg_shop_close);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        tvAction = (TextView) view.findViewById(R.id.tv_action);
        this.addView(view);
    }

    public void setIcon(@DrawableRes int imageResourceId) {
        this.ivIcon.setImageResource(imageResourceId);
    }

    public void setTitle(String titleString) {
        if (!TextUtils.isEmpty(titleString)) {
            tvTitle.setText(titleString);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    public void setDescription(String descriptionString) {
        if (!TextUtils.isEmpty(descriptionString)) {
            tvDescription.setText(MethodChecker.fromHtml(descriptionString));
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
    }

    public void setAction(String actionString, View.OnClickListener onClickListener) {
        if (TextUtils.isEmpty(actionString) || onClickListener == null) {
            tvAction.setVisibility(View.GONE);
        } else {
            tvAction.setText(actionString);
            tvAction.setOnClickListener(onClickListener);
            tvAction.setVisibility(View.VISIBLE);
        }

    }

    public void setTickerColor(int color) {
        if (color == 0) {
            //set to default
            container.getBackground().clearColorFilter();
        } else {
            container.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
