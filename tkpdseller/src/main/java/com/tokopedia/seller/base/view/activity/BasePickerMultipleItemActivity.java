package com.tokopedia.seller.base.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.dashboard.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.dashboard.view.fragment.ChipsTopAdsSelectionFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddProductListFragment;
import com.tokopedia.seller.topads.dashboard.view.helper.BottomSheetHelper;
import com.tokopedia.seller.topads.dashboard.view.helper.NumberOfChooseFooterHelper;
import com.tokopedia.seller.topads.dashboard.view.listener.AddProductListInterface;
import com.tokopedia.seller.topads.dashboard.view.model.TopAdsProductViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by nathan on 8/2/17.
 */

public abstract class BasePickerMultipleItemActivity extends BaseToolbarActivity implements AddProductListInterface {

    private Button submitButton;
    private View bottomSheetContainerLayout;

    private BottomSheetHelper bottomSheetHelper;
    private View.OnClickListener expandedOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (bottomSheetHelper.getState()) {
                case BottomSheetBehavior.STATE_COLLAPSED:
                    bottomSheetHelper.expanded();
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                default:
                    bottomSheetHelper.collapse();
                    break;
            }
        }
    };
    private View bottomSheetSelection;
    private NumberOfChooseFooterHelper numberOfChooseFooterHelper;
    private View footerButtonView;
    private int nextButtonRealHeight;
    private HashSet<TopAdsProductViewModel> selections;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            viewShadowGray.setVisibility(View.VISIBLE);
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            viewShadowGray.setVisibility(View.GONE);
                            removePaddingBottom();
                            break;
                        case BottomSheetBehavior.STATE_DRAGGING:
                            viewShadowGray.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    float v = slideOffset;
                    footerButtonView.animate().translationY((nextButtonRealHeight * v)).setDuration(0).start();

                    numberOfChooseFooterHelper.rotate(v * 180);
                }
            };
    private boolean isExistingGroup;
    private boolean isHideEtalase;
    private int maxNumberSelection;
    private View viewShadowGray;

    private void removePaddingBottom() {
        bottomSheetContainerLayout.setPadding(0, 0, 0, 0);
    }

    @Override
    protected void setupLayout() {
        super.setupLayout();
        submitButton = (Button) findViewById(R.id.button_submit);
        bottomSheetContainerLayout = findViewById(R.id.layout_container_bottom_sheet);

        bottomSheetSelection = findViewById(R.id.bottom_sheet_selection);
        numberOfChooseFooterHelper = new NumberOfChooseFooterHelper(bottomSheetSelection);
        numberOfChooseFooterHelper.bindData(10, expandedOnClick);

        viewShadowGray = findViewById(R.id.view_shadow_gray);

        footerButtonView = findViewById(R.id.top_ads_next);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSelections();
            }
        });
        disableNextButton();

        ViewTreeObserver vto = footerButtonView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                nextButtonRealHeight = footerButtonView.getMeasuredHeight();
                ViewTreeObserver obs = footerButtonView.getViewTreeObserver();

                getBottomSheetBehaviourFromParent();
                if (bottomSheetHelper != null) {
                    bottomSheetHelper.setHeight(nextButtonRealHeight);
                }
                if (selections.size() > 0) {
                    bottomSheetHelper.showBottomSheet();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });
        selections = new HashSet<>();
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_picker_multiple_item;
    }



    @Override
    protected void onResume() {
        super.onResume();
        // if fragment not created transact fragment.
        if (isFragmentCreated(TopAdsAddProductListFragment.TAG)) {
            inflateNewFragment(
                    getContainerId(),
                    TopAdsAddProductListFragment.newInstance(maxNumberSelection),
                    TopAdsAddProductListFragment.TAG
            );
        } else {
            // do nothing
        }

        if (isFragmentCreated(ChipsTopAdsSelectionFragment.TAG)) {
            inflateNewFragment(
                    getChipsContainerId(),
                    ChipsTopAdsSelectionFragment.newInstance(),
                    ChipsTopAdsSelectionFragment.TAG
            );
        } else {
            // do nothing
        }


    }

    private int getContainerId() {
        return R.id.activity_top_ads_add_product_list;
    }

    private int getChipsContainerId() {
        return R.id.chips_container;
    }

    private void inflateNewFragment(@IdRes int containerId, Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, fragment, TAG);
        fragmentTransaction.commit();
    }

    private void fetchSaveInstanceState(Bundle savedInstanceState) {

    }

    private void fetchIntent(Bundle extras) {
        if (extras != null) {
            ArrayList<TopAdsProductViewModel> selectionParcel = extras.getParcelableArrayList(
                    TopAdsExtraConstant.EXTRA_SELECTIONS);

            if (selectionParcel != null) {
                for (TopAdsProductViewModel topAdsProductViewModel : selectionParcel) {
                    selections.add(topAdsProductViewModel);
                }
            }

            numberOfChooseFooterHelper.setSelectionNumber(selections.size());

            isExistingGroup = extras.getBoolean(TopAdsExtraConstant.EXTRA_HIDE_EXISTING_GROUP, false);
            isHideEtalase = extras.getBoolean(TopAdsExtraConstant.EXTRA_HIDE_ETALASE, false);
            maxNumberSelection = extras.getInt(TopAdsExtraConstant.EXTRA_MAX_NUMBER_SELECTION, 50);

            if (selections.size() > 0) {
                enableNextButton();
            }
        }
    }

    @Override
    public String getScreenName() {
        return null;
    }

    /**
     * @param tag tag defined by fragment
     * @return true means fragment is null
     */
    public boolean isFragmentCreated(String tag) {
        return getFragmentManager().findFragmentByTag(tag) == null;
    }

    @Override
    public ImageHandler imageHandler() {
        return new ImageHandler(this);
    }

    @Override
    public void hideBottom() {
        bottomSheetHelper.dismissBottomSheet();
        bottomSheetHelper.collapse();
        hideFooterViewHolder();
    }

    @Override
    public void showBottom() {
        bottomSheetHelper.showBottomSheet();
        bottomSheetHelper.collapse();
        showFooterViewHolder();
    }

    @Override
    public boolean isSelected(TopAdsProductViewModel data) {
        if (selections == null)
            return false;

        return selections.contains(data);
    }

    @Override
    public void removeSelection(TopAdsProductViewModel data) {
        selections.remove(data);

        numberOfChooseFooterHelper.setSelectionNumber(selections.size());
    }

    @Override
    public void addSelection(TopAdsProductViewModel data) {
        selections.add(data);
        numberOfChooseFooterHelper.setSelectionNumber(selections.size());

        if (selections.size() > 0) {
            enableNextButton();
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
    public void disableNextButton() {
        submitButton.setEnabled(false);
    }

    @Override
    public void enableNextButton() {
        submitButton.setEnabled(true);
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
        return bottomSheetHelper.isShown();
    }

    @Override
    public void showNextButton() {
        submitButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissNextButton() {
        submitButton.setVisibility(View.GONE);
    }

    @Override
    public void hideFooterViewHolder() {
        TopAdsAddProductListFragment topAdsAddProductListFragment
                = getTopAdsAddProductListFragment();
        if (topAdsAddProductListFragment != null) {
            topAdsAddProductListFragment.hideFooterViewHolder();
        }
    }

    @Override
    public void showFooterViewHolder() {
        TopAdsAddProductListFragment topAdsAddProductListFragment
                = getTopAdsAddProductListFragment();
        if (topAdsAddProductListFragment != null) {
            topAdsAddProductListFragment.showFooterViewHolder();
        }
    }

    private void returnSelections() {
        Intent intent = new Intent();
        intent.putExtra(TopAdsExtraConstant.EXTRA_SELECTIONS, new ArrayList<>(selections));
        setResult(RESULT_CODE, intent);
        finish();
    }

    @Override
    public void notifyUnchecked(TopAdsProductViewModel data) {
        TopAdsAddProductListFragment topAdsAddProductListFragment
                = getTopAdsAddProductListFragment();

        if (topAdsAddProductListFragment != null) {
            topAdsAddProductListFragment.notifyUnchecked(data);
        }
    }

    @Override
    public void onChecked(int position, TopAdsProductViewModel data) {

        ChipsTopAdsSelectionFragment chipsTopAdsSelectionFragment
                = getChipsTopAdsSelectionFragment();
        if (chipsTopAdsSelectionFragment != null)
            chipsTopAdsSelectionFragment.onChecked(position, data);

        bottomSheetHelper.showBottomSheet();
    }

    @Override
    public void onUnChecked(int position, TopAdsProductViewModel data) {

        ChipsTopAdsSelectionFragment chipsTopAdsSelectionFragment
                = getChipsTopAdsSelectionFragment();
        if (chipsTopAdsSelectionFragment != null)
            chipsTopAdsSelectionFragment.onUnChecked(position, data);
    }

    private void getBottomSheetBehaviourFromParent() {
        final FrameLayout recyclerView = (FrameLayout) findViewById(R.id.chips_container);
        FrameLayout parentThatHasBottomSheetBehavior
                = (FrameLayout) recyclerView.getParent().getParent();
        bottomSheetHelper
                = new BottomSheetHelper(
                BottomSheetBehavior.from(parentThatHasBottomSheetBehavior),
                bottomSheetSelection,
                getActionBarSize()
        );
        bottomSheetHelper.setBottomSheetCallback(bottomSheetCallback);

    }

    private int getActionBarSize() {
        int actionBarHeight = 0;
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    private TopAdsAddProductListFragment getTopAdsAddProductListFragment() {
        Fragment fragmentByTag
                = getFragmentManager().findFragmentByTag(TopAdsAddProductListFragment.TAG);
        if (fragmentByTag != null
                && fragmentByTag instanceof TopAdsAddProductListFragment) {
            return (TopAdsAddProductListFragment) fragmentByTag;
        }

        return null;
    }

    private ChipsTopAdsSelectionFragment getChipsTopAdsSelectionFragment() {
        Fragment fragmentByTag
                = getFragmentManager().findFragmentByTag(ChipsTopAdsSelectionFragment.TAG);
        if (fragmentByTag != null
                && fragmentByTag instanceof ChipsTopAdsSelectionFragment) {
            return (ChipsTopAdsSelectionFragment) fragmentByTag;
        }

        return null;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetHelper.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetHelper.collapse();
        } else {
            super.onBackPressed();
        }
    }

    public int getStatusBarHeight() {
        return 0;
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