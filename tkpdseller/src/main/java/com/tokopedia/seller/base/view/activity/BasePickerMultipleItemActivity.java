package com.tokopedia.seller.base.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerItemCacheList;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public abstract class BasePickerMultipleItemActivity<T extends ItemPickerType> extends BaseToolbarActivity implements BasePickerMultipleItem<T> {

    private static final int ARROW_DEGREE = 180;

    public static final String CONTAINER_SEARCH_LIST_TAG = "CONTAINER_SEARCH_LIST_TAG";
    public static final String CONTAINER_CACHE_LIST_TAG = "CONTAINER_CACHE_LIST_TAG";

    public static final String EXTRA_INTENT_PICKER_ITEM_LIST = "EXTRA_INTENT_PICKER_ITEM_LIST";
    public static final String EXTRA_INTENT_PICKER_ITEM_SELECTED_LIST = "EXTRA_INTENT_PICKER_ITEM_SELECTED_LIST";

    private View bottomSheetContainerView;
    private View shadowView;
    private View bottomSheetHeaderView;
    private TextView bottomSheetTitleTextView;
    private TextView bottomSheetContentTextView;
    private ImageView arrowImageView;
    private View footerView;
    private Button submitButton;

    private BottomSheetBehavior bottomSheetBehavior;

    protected abstract Fragment getSearchListFragment();

    protected abstract Fragment getCacheListFragment();

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        bottomSheetContainerView = findViewById(R.id.layout_bottom_sheet_container);

        bottomSheetTitleTextView = (TextView) findViewById(R.id.text_view_bottom_sheet_title);
        bottomSheetContentTextView = (TextView) findViewById(R.id.text_view_bottom_sheet_content);
        arrowImageView = (ImageView) findViewById(R.id.image_view_arrow);
        submitButton = (Button) findViewById(R.id.button_submit);
        bottomSheetHeaderView = findViewById(R.id.layout_bottom_sheet_header);
        bottomSheetHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (bottomSheetBehavior.getState()) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                    default:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
            }
        });
        footerView = findViewById(R.id.layout_footer);
        shadowView = findViewById(R.id.view_shadow_gray);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainerView);
        final float footerHeight = getResources().getDimension(R.dimen.base_picker_multiple_item_footer_height);
        final float shadowHeight = getResources().getDimension(R.dimen.base_picker_multiple_item_bottom_sheet_header_shadow_height);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                ViewGroup.LayoutParams layoutParams = shadowView.getLayoutParams();
                layoutParams.height = (int) (shadowHeight - (shadowHeight * slideOffset));
                shadowView.setLayoutParams(layoutParams);
                footerView.animate().translationY((footerHeight * slideOffset)).setDuration(0).start();
                arrowImageView.setRotation(slideOffset * ARROW_DEGREE);
            }
        });
        int peekHeight = (int) (getResources().getDimension(R.dimen.base_picker_multiple_item_bottom_sheet_header_height) +
                getResources().getDimension(R.dimen.base_picker_multiple_item_footer_height));
        bottomSheetBehavior.setPeekHeight(peekHeight);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void setupFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment searchListFragment = getSearchListFragment();
            fragmentTransaction.replace(R.id.layout_container_list, searchListFragment, CONTAINER_SEARCH_LIST_TAG);
            Fragment cacheListFragment = getCacheListFragment();
            fragmentTransaction.replace(R.id.layout_container_cache, cacheListFragment, CONTAINER_CACHE_LIST_TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_picker_multiple_item;
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    public void addItemFromSearch(T t) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_CACHE_LIST_TAG);
        ((BasePickerItemCacheList<T>) fragment).addItem(t);
    }

    @Override
    public void removeItemFromSearch(T t) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_CACHE_LIST_TAG);
        ((BasePickerItemCacheList<T>) fragment).removeItem(t);
    }

    @Override
    public void removeItemFromCache(T t) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_SEARCH_LIST_TAG);
        ((BasePickerItemSearchList) fragment).deselectItem(t);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}