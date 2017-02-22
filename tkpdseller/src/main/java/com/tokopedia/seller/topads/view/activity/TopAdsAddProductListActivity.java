package com.tokopedia.seller.topads.view.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.listener.AddProductListInterface;
import com.tokopedia.seller.topads.utils.ViewUtils;
import com.tokopedia.seller.topads.view.fragment.ChipsTopAdsSelectionFragment;
import com.tokopedia.seller.topads.view.fragment.TopAdsAddProductListFragment;
import com.tokopedia.seller.topads.view.helper.BottomSheetHelper;
import com.tokopedia.seller.topads.view.helper.NumberOfChooseFooterHelper;

import javax.inject.Inject;



public class TopAdsAddProductListActivity extends BaseActivity
        implements AddProductListInterface {

    public static final String TAG = "TopAdsAddPListAct";

    @Inject
    ImageHandler imageHandler;

    private RelativeLayout fragmentContainer;
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
    private View nextButton;
    private int[] nextButtonHeight;
    private int nextButtonRealHeight;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            removePaddingBottom();
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    float v = slideOffset;
                    Log.d(TAG, "nextButtonRealHeight " + nextButtonRealHeight + " "
                            + (nextButtonRealHeight * v + " slideOffset " + slideOffset));
                    nextButton.animate().translationY((nextButtonRealHeight * v)).setDuration(0).start();
                }
            };
//    private BaseComponent component;

    private void removePaddingBottom() {
        findViewById(R.id.bottom_sheet_container).setPadding(0, 0, 0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.setTranslucentStatusBar(getWindow());
        inject();
        setContentView(R.layout.activity_top_ads_add_product_list_container);

        bottomSheetSelection = findViewById(R.id.bottom_sheet_selection);
        numberOfChooseFooterHelper = new NumberOfChooseFooterHelper(bottomSheetSelection);
        numberOfChooseFooterHelper.bindData(10, expandedOnClick);

        nextButton = findViewById(R.id.top_ads_next);
        nextButtonHeight = getCoords(nextButton);

        ViewTreeObserver vto = nextButton.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                nextButtonRealHeight = nextButton.getMeasuredHeight();
                ViewTreeObserver obs = nextButton.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentContainer = (RelativeLayout)
                findViewById(R.id.activity_top_ads_add_product_list);
        getBottomSheetBehaviourFromParent();

        fetchIntent(getIntent().getExtras());
        fetchSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // if fragment not created transact fragment.
        if (isFragmentCreated(TopAdsAddProductListFragment.TAG)) {
            inflateNewFragment(
                    getContainerId(),
                    TopAdsAddProductListFragment.newInstance(),
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

    private void inject() {
//        component = BaseApplication.get(this).getComponent();
//        component.inject(this);
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

        }
    }

    private int[] getCoords(View view) {
        int[] coords = {0, 0};
        view.getLocationOnScreen(coords);
        int absoluteTop = coords[1];
        int absoluteBottom = coords[1] + view.getHeight();
        int[] result = {absoluteTop, absoluteBottom};
        return result;
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
        return imageHandler;
    }

//    @Override
//    public BaseComponent baseComponent() {
//        return component;
//    }

    @Override
    public void onChecked(int position, ProductDomain data) {
        ChipsTopAdsSelectionFragment chipsTopAdsSelectionFragment
                = getChipsTopAdsSelectionFragment();
        if(chipsTopAdsSelectionFragment != null)
            chipsTopAdsSelectionFragment.onChecked(position, data);

        bottomSheetHelper.showBottomSheet();
    }

    @Override
    public void onUnChecked(int position, ProductDomain data) {
        ChipsTopAdsSelectionFragment chipsTopAdsSelectionFragment
                = getChipsTopAdsSelectionFragment();
        if(chipsTopAdsSelectionFragment != null)
            chipsTopAdsSelectionFragment.onUnChecked(position, data);
        bottomSheetHelper.dismissBottomSheet();
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

    private ChipsTopAdsSelectionFragment getChipsTopAdsSelectionFragment(){
        Fragment fragmentByTag
                = getFragmentManager().findFragmentByTag(ChipsTopAdsSelectionFragment.TAG);
        if(fragmentByTag != null
                && fragmentByTag instanceof ChipsTopAdsSelectionFragment){
            return (ChipsTopAdsSelectionFragment)fragmentByTag;
        }

        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
