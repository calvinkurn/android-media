package com.tokopedia.events.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.events.R;

import at.blogc.android.views.ExpandableTextView;


public class AppBarFlexiImageScrollsActivity extends AppCompatActivity {

    ExpandableTextView expandableDetailsTV;
    ExpandableTextView expandableTncTV;
    TextView buttonToggle;
    View buttonTNC;
    View buttonShowLayout;
    ImageView seatingLayout;
    View buttonBook;

    ImageView location;
    ImageView address;

    ImageView arrow1;
    ImageView arrow2;

    TextView tvLoc;
    TextView tvAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("The 90s Festival");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        //final ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = findViewById(R.id.appbarlayout);
        appBarLayout.addOnOffsetChangedListener(new ToolbarElevationOffsetListener(this, toolbar));

        expandableDetailsTV = this.findViewById(R.id.expandabletextview);
        expandableTncTV = this.findViewById(R.id.tv_tnc_expandable);
        buttonToggle = this.findViewById(R.id.seemorebutton);
        buttonTNC = this.findViewById(R.id.btn_terms_conditions);
        arrow2 = buttonTNC.findViewById(R.id.iv_arrow);
        buttonShowLayout = this.findViewById(R.id.btn_show_seating);
        arrow1 = buttonShowLayout.findViewById(R.id.iv_arrow);
        seatingLayout = this.findViewById(R.id.imgv_seating_layout);
        buttonBook = this.findViewById(R.id.btn_book);
        location = findViewById(R.id.event_location).findViewById(R.id.image_holder_small);
        tvLoc = findViewById(R.id.event_location).findViewById(R.id.textview_holder_small);
        address = findViewById(R.id.event_address).findViewById(R.id.image_holder_small);
        tvAddress = findViewById(R.id.event_address).findViewById(R.id.textview_holder_small);
        location.setImageResource(R.drawable.ic_placeholder);
        address.setImageResource(R.drawable.ic_skyline);
        tvLoc.setText("Kemayoran");
        tvAddress.setText("Jalan Benyamin Suaeb No. 1");
        buttonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BookTicketActivity.class);
               // startActivity(intent);
            }
        });
        buttonShowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seatingLayout.getVisibility() != View.VISIBLE) {
                    seatingLayout.setVisibility(View.VISIBLE);
                    arrow1.animate().rotation(180f);

                }
                else {
                    arrow1.animate().rotation(0f);
                    seatingLayout.setVisibility(View.GONE);
                }
            }
        });
        buttonTNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //buttonTNC.setText(expandableDetailsTV.isExpanded() ? R.string.expand : R.string.collapse);
                if(expandableTncTV.getVisibility() == View.GONE) {
                    expandableTncTV.setVisibility(View.VISIBLE);
                    arrow2.animate().rotation(180f);
                }

                else {
                    expandableTncTV.setVisibility(View.GONE);
                    arrow2.animate().rotation(0f);
                }
                expandableTncTV.toggle();
            }
        });


        // set interpolators for both expanding and collapsing animations
        expandableDetailsTV.setInterpolator(new OvershootInterpolator());

// or set them separately
        expandableDetailsTV.setExpandInterpolator(new OvershootInterpolator());
        expandableDetailsTV.setCollapseInterpolator(new OvershootInterpolator());

// toggle the ExpandableTextView
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                buttonToggle.setText(expandableDetailsTV.isExpanded() ? R.string.expand : R.string.collapse);
                expandableDetailsTV.toggle();
            }
        });

// but, you can also do the checks yourself
//        buttonToggle.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(final View v)
//            {
//                if (expandableTextView.isExpanded())
//                {
//                    expandableTextView.collapse();
//                    buttonToggle.setText(R.string.expand);
//                }
//                else
//                {
//                    expandableTextView.expand();
//                    buttonToggle.setText(R.string.collapse);
//                }
//            }
//        });

// listen for expand / collapse events
        expandableDetailsTV.addOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d("Pranay", "ExpandableTextView expanded");
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d("Pranay", "ExpandableTextView collapsed");
            }
        });
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