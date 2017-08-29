package com.tokopedia.seller.base.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drew.lang.annotations.SuppressWarnings;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemPickerType;
import com.tokopedia.seller.base.view.listener.BasePickerItemCacheList;
import com.tokopedia.seller.base.view.listener.BasePickerItemSearchList;
import com.tokopedia.seller.base.view.listener.BasePickerMultipleItem;

/**
 * Created by nathan on 8/2/17.
 */

public abstract class BasePickerMultipleItemActivity<T extends ItemPickerType> extends BaseToolbarActivity implements BasePickerMultipleItem<T> {

    private static final int ARROW_DEGREE = 180;

    private static final String CONTAINER_SEARCH_LIST_TAG = "CONTAINER_SEARCH_LIST_TAG";
    private static final String CONTAINER_CACHE_LIST_TAG = "CONTAINER_CACHE_LIST_TAG";

    public static final String EXTRA_INTENT_PICKER_ITEM_LIST = "EXTRA_INTENT_PICKER_ITEM_LIST";
    public static final String EXTRA_INTENT_PICKER_ITEM_SELECTED_LIST = "EXTRA_INTENT_PICKER_ITEM_SELECTED_LIST";

    protected View bottomSheetHeaderView;
    protected TextView bottomSheetTitleTextView;
    protected TextView bottomSheetContentTextView;

    private View bottomSheetContainerView;
    private View shadowView;
    private ImageView arrowImageView;
    private View footerView;
    private View containerListView;

    private BottomSheetBehavior bottomSheetBehavior;

    protected abstract Fragment getInitialSearchListFragment();

    protected abstract Fragment getInitialCacheListFragment();

    protected abstract Intent getDefaultIntentResult();

    protected Fragment getSearchListFragment() {
        return getSupportFragmentManager().findFragmentByTag(CONTAINER_SEARCH_LIST_TAG);
    }

    protected Fragment getCacheListFragment() {
        return getSupportFragmentManager().findFragmentByTag(CONTAINER_CACHE_LIST_TAG);
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        bottomSheetContainerView = findViewById(R.id.layout_bottom_sheet_container);

        bottomSheetTitleTextView = (TextView) findViewById(R.id.text_view_bottom_sheet_title);
        bottomSheetContentTextView = (TextView) findViewById(R.id.text_view_bottom_sheet_content);
        arrowImageView = (ImageView) findViewById(R.id.image_view_arrow);
        View submitButton = findViewById(R.id.button_submit);
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
        containerListView = findViewById(R.id.layout_container_list);
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
                submitButtonClicked();
            }
        });
    }

    @Override
    protected void setupFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment cacheListFragment = getInitialCacheListFragment();
            fragmentTransaction.replace(R.id.layout_container_cache, cacheListFragment, CONTAINER_CACHE_LIST_TAG);
            Fragment searchListFragment = getInitialSearchListFragment();
            fragmentTransaction.replace(R.id.layout_container_list, searchListFragment, CONTAINER_SEARCH_LIST_TAG);
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

    @CallSuper
    @Override
    @java.lang.SuppressWarnings("unchecked")
    public void addItemFromSearch(T t) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_CACHE_LIST_TAG);
        ((BasePickerItemCacheList<T>) fragment).addItem(t);
    }


    @CallSuper
    @Override
    @java.lang.SuppressWarnings("unchecked")
    public void removeItemFromSearch(T t) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_CACHE_LIST_TAG);
        ((BasePickerItemCacheList<T>) fragment).removeItem(t);
    }

    @CallSuper
    @Override
    @java.lang.SuppressWarnings("unchecked")
    public void removeItemFromCache(T t) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CONTAINER_SEARCH_LIST_TAG);
        ((BasePickerItemSearchList) fragment).deselectItem(t);
    }

    protected void submitButtonClicked() {
        Intent intent = getDefaultIntentResult();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public void showFooterAndInfo(boolean show) {
        bottomSheetContainerView.setVisibility(show ? View.VISIBLE : View.GONE);
        footerView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (!show) {
            int containerBottomMargin = 0;
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) containerListView.getLayoutParams();
            p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, containerBottomMargin);
            containerListView.requestLayout();
        } else {
            showBottomSheetInfo(true);
        }
    }

    public void showBottomSheetInfo(boolean show) {
        int peekHeight = (int) (getResources().getDimension(R.dimen.base_picker_multiple_item_bottom_sheet_header_height));
        int containerBottomMargin = (int) getResources().getDimension(R.dimen.base_picker_multiple_item_footer_height);
        if (show) {
            peekHeight += getResources().getDimension(R.dimen.base_picker_multiple_item_footer_height);
            containerBottomMargin += getResources().getDimension(R.dimen.base_picker_multiple_item_bottom_sheet_header_height) -
                    getResources().getDimension(R.dimen.base_picker_multiple_item_bottom_sheet_header_shadow_height);
        }
        if (bottomSheetBehavior.getPeekHeight() == peekHeight) {
            return;
        }
        bottomSheetBehavior.setPeekHeight(peekHeight);
        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) containerListView.getLayoutParams();
        p.setMargins(p.leftMargin, p.topMargin, p.rightMargin, containerBottomMargin);
        containerListView.requestLayout();
    }
}