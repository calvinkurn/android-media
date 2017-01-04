package com.tokopedia.seller.instoped;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.seller.instoped.fragment.InstagramMediaFragment;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.myproduct.ProductSocMedActivity;
import com.tokopedia.core.myproduct.presenter.ProductSocMedPresenter;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by sebastianuskh on 1/3/17.
 */

public class InstopedActivity extends AppCompatActivity implements InstagramActivityListener {

    private static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";
    private String FRAGMENT;
    private Unbinder unbinder;
    private FragmentManager supportFragmentManager;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    public static void startInstopedActivity(Context context){
        Intent moveToProductActivity = new Intent(context, InstopedActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TO_SHOW, InstagramAuth.TAG);
        moveToProductActivity.putExtras(bundle);
        context.startActivity(moveToProductActivity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.isFinishing()) {
            return;
        }

        fetchExtras(getIntent());
        setContentView(R.layout.activity_instoped);
        unbinder = ButterKnife.bind(this);
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
                auth.getMedias(supportFragmentManager);
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
                selectedModel.size();
                //[START] move to productSocMedActivity
                Intent moveToProductSocMed = new Intent(InstopedActivity.this, ProductSocMedActivity.class);
                moveToProductSocMed.putExtra(
                        ProductSocMedPresenter.PRODUCT_SOC_MED_DATA,
                        Parcels.wrap(selectedModel)
                );
                InstopedActivity.this.startActivity(moveToProductSocMed);
                InstopedActivity.this.finish();
                //[END] move to productSocMedActivity
            }
        };    }

}
