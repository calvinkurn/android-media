package com.tokopedia.seller.base.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.ChipsTopAdsSelectionFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddProductListFragment;
import com.tokopedia.seller.topads.dashboard.view.listener.AddProductListInterface;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public abstract class BasePickerMultipleItemActivity extends BaseToolbarActivity implements AddProductListInterface {

    private static final int ARROW_DEGREE = 180;

    private static final String CONTAINER_SEARCH_LIST_TAG = "CONTAINER_SEARCH_LIST_TAG";
    private static final String CONTAINER_CACHE_LIST_TAG = "CONTAINER_CACHE_LIST_TAG";

    private View bottomSheetContainerView;
    private View shadowView;
    private View bottomSheetHeaderView;
    private TextView bottomSheetTitleTextView;
    private TextView bottomSheetContentTextView;
    private ImageView arrowImageView;
    private View footerView;
    private Button submitButton;

    private HashSet<TopAdsProductViewModel> selections;
    private boolean isExistingGroup;
    private boolean isHideEtalase;
    private int maxNumberSelection;


    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void setupLayout() {
        super.setupLayout();

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
                Intent intent = new Intent();
                intent.putExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, new ArrayList<>(selections));
                setResult(RESULT_CODE, intent);
                finish();
            }
        });
        setSubmitButtonEnabled(false);
        selections = new HashSet<>();
    }

    @Override
    protected void setupFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            //TODO Need to change to v4 fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_container_list, TopAdsAddProductListFragment.newInstance(maxNumberSelection), CONTAINER_SEARCH_LIST_TAG);
            fragmentTransaction.replace(R.id.layout_container_cache, ChipsTopAdsSelectionFragment.newInstance(), CONTAINER_CACHE_LIST_TAG);
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
    public void hideBottom() {
//        bottomSheetHelper.dismissBottomSheet();
//        bottomSheetHelper.collapse();
        hideFooterViewHolder();
    }

    @Override
    public void showBottom() {
//        bottomSheetHelper.showBottomSheet();
//        bottomSheetHelper.collapse();
        showFooterViewHolder();
    }

    @Override
    public boolean isSelected(TopAdsProductViewModel data) {
        if (selections == null) {
            return false;
        }
        return selections.contains(data);
    }

    @Override
    public void removeSelection(TopAdsProductViewModel data) {
        selections.remove(data);
    }

    @Override
    public void addSelection(TopAdsProductViewModel data) {
        selections.add(data);
        if (selections.size() > 0) {
            setSubmitButtonEnabled(true);
            showFooterViewHolder();
        }
    }

    @Override
    public int sizeSelection() {
        return selections.size();
    }

    @Override
    public List<TopAdsProductViewModel> selections() {
        return new ArrayList<>(selections);
    }

    @Override
    public void setSubmitButtonEnabled(boolean enabled) {
        submitButton.setEnabled(enabled);
    }

    @Override
    public boolean isHideEtalase() {
        return isHideEtalase;
    }

    @Override
    public boolean isExistingGroup() {
        return isExistingGroup;
    }

    @Override
    public boolean isSelectionViewShown() {
        return false;
//        return bottomSheetHelper.isShown();
    }

    @Override
    public void setSubmitButtonVisibility(int visibility) {
        submitButton.setVisibility(visibility);
    }

    @Override
    public void hideFooterViewHolder() {
        TopAdsAddProductListFragment topAdsAddProductListFragment = getTopAdsAddProductListFragment();
        if (topAdsAddProductListFragment != null) {
            topAdsAddProductListFragment.hideFooterViewHolder();
        }
    }

    @Override
    public void showFooterViewHolder() {
        TopAdsAddProductListFragment topAdsAddProductListFragment = getTopAdsAddProductListFragment();
        if (topAdsAddProductListFragment != null) {
            topAdsAddProductListFragment.showFooterViewHolder();
        }
    }

    @Override
    public void notifyUnchecked(TopAdsProductViewModel data) {
        TopAdsAddProductListFragment topAdsAddProductListFragment = getTopAdsAddProductListFragment();
        if (topAdsAddProductListFragment != null) {
            topAdsAddProductListFragment.notifyUnchecked(data);
        }
    }

    @Override
    public void onChecked(int position, TopAdsProductViewModel data) {
        ChipsTopAdsSelectionFragment chipsTopAdsSelectionFragment = getChipsTopAdsSelectionFragment();
        if (chipsTopAdsSelectionFragment != null) {
            chipsTopAdsSelectionFragment.onChecked(position, data);
        }
//        bottomSheetBehavior.showBottomSheet();
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {
        ChipsTopAdsSelectionFragment chipsTopAdsSelectionFragment = getChipsTopAdsSelectionFragment();
        if (chipsTopAdsSelectionFragment != null)
            chipsTopAdsSelectionFragment.onUnChecked(position, data);
    }

    private TopAdsAddProductListFragment getTopAdsAddProductListFragment() {
        Fragment fragmentByTag = getFragmentManager().findFragmentByTag(TopAdsAddProductListFragment.TAG);
        if (fragmentByTag != null && fragmentByTag instanceof TopAdsAddProductListFragment) {
            return (TopAdsAddProductListFragment) fragmentByTag;
        }
        return null;
    }

    private ChipsTopAdsSelectionFragment getChipsTopAdsSelectionFragment() {
        Fragment fragmentByTag = getFragmentManager().findFragmentByTag(ChipsTopAdsSelectionFragment.TAG);
        if (fragmentByTag != null && fragmentByTag instanceof ChipsTopAdsSelectionFragment) {
            return (ChipsTopAdsSelectionFragment) fragmentByTag;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}