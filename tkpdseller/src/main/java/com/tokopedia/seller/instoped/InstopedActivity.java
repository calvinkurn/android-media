package com.tokopedia.seller.instoped;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.instoped.fragment.InstagramMediaFragment;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.seller.myproduct.ProductSocMedActivity;
import com.tokopedia.seller.myproduct.presenter.ProductSocMedPresenter;

import org.parceler.Parcels;

import java.util.List;


/**
 * Created by sebastianuskh on 1/3/17.
 */

public class InstopedActivity extends TActivity implements InstagramActivityListener {

    private static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";
    private static final String MAX_RESULT = "MAX_R";
    private String FRAGMENT;
    private FragmentManager supportFragmentManager;
    private Toolbar toolbar;
    private int maxResult;

    public static void startInstopedActivity(Context context){
        Intent moveToProductActivity = new Intent(context, InstopedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        moveToProductActivity.putExtras(bundle);
        context.startActivity(moveToProductActivity);
    }

    public static void startInstopedActivityForResult(Activity activity, int resultCode, int maxResult){
        Intent moveToProductActivity = new Intent(activity, InstopedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        bundle.putInt(MAX_RESULT, maxResult);
        moveToProductActivity.putExtras(bundle);
        activity.startActivityForResult(moveToProductActivity, resultCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.isFinishing()) {
            return;
        }

        fetchExtras(getIntent());
        setContentView(R.layout.activity_instoped);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        supportFragmentManager = getSupportFragmentManager();

    }

    private void fetchExtras(Intent intent) {
        if (intent != null) {
            // set which fragment should be created
            String fragment = intent.getExtras().getString(FRAGMENT_TO_SHOW);
            if (fragment != null) {
                switch (fragment) {
                    case InstagramAuth.TAG:
                        FRAGMENT = fragment;
                        break;
                }
            } else {
                FRAGMENT = InstagramAuth.TAG;
            }
            maxResult = intent.getIntExtra(MAX_RESULT,-1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);
    }

    public void initFragment(String FRAGMENT_TAG) {

        switch (FRAGMENT_TAG) {

            case InstagramAuth.TAG:
                InstagramAuth auth = new InstagramAuth();
                auth.getMedias(supportFragmentManager, maxResult);
                break;
            default :
                throw new RuntimeException("not implemented yet");
        }
    }

    @Override
    public void triggerAppBarAnimation(boolean turnedOn) {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        if(turnedOn) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            params.setScrollFlags(0);
        }
        toolbar.setLayoutParams(params);
    }

    @Override
    public InstagramMediaFragment.OnGetInstagramMediaListener onGetInstagramMediaListener() {
        return new InstagramMediaFragment.OnGetInstagramMediaListener() {
            @Override
            public void onSuccess(SparseArray<InstagramMediaModel> selectedModel) {

                UnifyTracking.eventImageUploadSuccessInstagram();

                // if activity has no caller, continue to build product soc med
                if (getCallingActivity() == null) {
                    selectedModel.size();
                    //[START] move to productSocMedActivity
                    Intent moveToProductSocMed = new Intent(InstopedActivity.this, ProductSocMedActivity.class);
                    moveToProductSocMed.putExtra(
                            GalleryActivity.PRODUCT_SOC_MED_DATA,
                            Parcels.wrap(selectedModel)
                    );
                    InstopedActivity.this.startActivity(moveToProductSocMed);
                    InstopedActivity.this.finish();
                    //[END] move to productSocMedActivity
                }
                else { // activity has caller, just finish it and return the bundle to the caller
                    Intent socMedIntent = new Intent();
                    socMedIntent.putExtra(
                            GalleryActivity.PRODUCT_SOC_MED_DATA,
                            Parcels.wrap(selectedModel)
                    );
                    InstopedActivity.this.setResult(Activity.RESULT_OK, socMedIntent);
                    InstopedActivity.this.finish();
                }
            }
        };    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_INSTOPED;
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() != 0) {
            //TODO: Perform your logic to pass back press here
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnBackPressedListener) {
                    boolean canGoBack = ((OnBackPressedListener) fragment).onBackPressed();
                    if (!canGoBack) {
                        super.onBackPressed();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

}
