package com.tokopedia.events.view.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.events.R;
import com.tokopedia.events.R2;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventDetailsActivity extends TActivity {


    @BindView(R2.id.image_holder_small)
    ImageView imageHolderSmall;
    @BindView(R2.id.textview_holder_small)
    TextView textviewHolderSmall;
    @BindView(R2.id.image_text_holder)
    LinearLayout imageTextHolder;
    @BindView(R2.id.expandabletextview)
    ExpandableTextView expandabletextview;
    @BindView(R2.id.seemorebutton)
    TextView seemorebutton;
    @BindView(R2.id.btn_show_seating)
    LinearLayout btnShowSeating;
    @BindView(R2.id.imgv_seating_layout)
    ImageView imgvSeatingLayout;
    @BindView(R2.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R2.id.tv_tnc_expandable)
    ExpandableTextView tvTncExpandable;
    @BindView(R2.id.btn_terms_conditions)
    LinearLayout btnTermsConditions;
    @BindView(R2.id.button_textview)
    TextView buttonTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_activity);
        ButterKnife.bind(this);


//        buttonBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), BookTicketActivity.class);
//                startActivity(intent);
//            }
//        });
        btnShowSeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgvSeatingLayout.getVisibility() != View.VISIBLE) {
                    imgvSeatingLayout.setVisibility(View.VISIBLE);
                    ivArrow.animate().rotation(180f);

                } else {
                    ivArrow.animate().rotation(0f);
                    imgvSeatingLayout.setVisibility(View.GONE);
                }
            }
        });
        btnTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //btnTermsConditions.setText(expandableDetailsTV.isExpanded() ? R.string.expand : R.string.collapse);
                if (tvTncExpandable.getVisibility() == View.GONE) {
                    tvTncExpandable.setVisibility(View.VISIBLE);
                    ivArrow.animate().rotation(180f);
                } else {
                    tvTncExpandable.setVisibility(View.GONE);
                    ivArrow.animate().rotation(0f);
                }
                tvTncExpandable.toggle();
            }
        });


        // set interpolators for both expanding and collapsing animations
        expandabletextview.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandabletextview.setExpandInterpolator(new OvershootInterpolator());
        expandabletextview.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
        seemorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                seemorebutton.setText(expandabletextview.isExpanded() ? R.string.expand : R.string.collapse);
                expandabletextview.toggle();
            }
        });

// but, you can also do the checks yourself
//        seemorebutton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(final View v)
//            {
//                if (expandableTextView.isExpanded())
//                {
//                    expandableTextView.collapse();
//                    seemorebutton.setText(R.string.expand);
//                }
//                else
//                {
//                    expandableTextView.expand();
//                    seemorebutton.setText(R.string.collapse);
//                }
//            }
//        });

// listen for expand / collapse events
       
    }

    public class ToolbarElevationOffsetListener implements AppBarLayout.OnOffsetChangedListener {
        private final Toolbar mToolbar;
        private float mTargetElevation;
        private final AppCompatActivity mActivity;

        public ToolbarElevationOffsetListener(AppCompatActivity appCompatActivity, Toolbar toolbar) {
            mActivity = appCompatActivity;
            mToolbar = toolbar;
            mTargetElevation = mToolbar.getContext().getResources().getDimension(R.dimen.appbar_elevation);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
            offset = Math.abs(offset);
            mTargetElevation = Math.max(mTargetElevation, appBarLayout.getTargetElevation());
            if (offset >= appBarLayout.getTotalScrollRange() - mToolbar.getHeight()) {
                float flexibleSpace = appBarLayout.getTotalScrollRange() - offset;
                float ratio = 1 - (flexibleSpace / mToolbar.getHeight());
                float elevation = ratio * mTargetElevation;
                setToolbarElevation(elevation);
            } else {
                setToolbarElevation(0);
            }

        }

        private void setToolbarElevation(float targetElevation) {
            ActionBar supportActionBar = mActivity.getSupportActionBar();
            if (supportActionBar != null) supportActionBar.setElevation(targetElevation);
            else if (mToolbar != null)
                ViewCompat.setElevation(mToolbar, targetElevation);
        }
    }
}