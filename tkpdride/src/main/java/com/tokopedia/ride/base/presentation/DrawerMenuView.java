package com.tokopedia.ride.base.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.tokopedia.ride.R;

/**
 * Created by alvarisi on 3/13/17.
 */

public class DrawerMenuView extends RelativeLayout {
    AppCompatTextView userName;
    ImageView userPhoto;
    private ActionListener mActionListener;

    public DrawerMenuView(Context context) {
        super(context);
    }

    public DrawerMenuView(Context context, ActionListener actionListener) {
        super(context);
        this.mActionListener = actionListener;
        initView(context);
    }

    public DrawerMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DrawerMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_drawer_menu, this, true);
        ButterKnife.bind(this);

    }

    public void actionHomeSelected() {
        mActionListener.onHomeSelected();
    }

    public interface ActionListener {
        void onHomeSelected();
    }
}