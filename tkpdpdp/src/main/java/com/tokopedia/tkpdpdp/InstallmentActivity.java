package com.tokopedia.tkpdpdp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.core.product.model.etalase.MonthsInstallmentItem;
import com.tokopedia.core.product.model.productdetail.ProductInstallment;
import com.tokopedia.core.product.model.productdetail.Terms;
import com.tokopedia.tkpdpdp.adapter.ViewPagerAdapter;
import com.tokopedia.tkpdpdp.fragment.InstallmentMonthsFragment;

import java.util.ArrayList;

import static com.tokopedia.core.router.productdetail.ProductDetailRouter.EXTRA_PRODUCT_ID;
import static com.tokopedia.core.var.TkpdCache.Key.STATE_ORIENTATION_CHANGED;
import static com.tokopedia.core.var.TkpdCache.PRODUCT_DETAIL;

public class InstallmentActivity extends TActivity {

    public static final String KEY_INSTALLMENT_DATA = "WHOLESALE_DATA";
    public static final String MONTHS_3 = "3 Bulan";
    public static final String MONTHS_6 = "6 Bulan";
    public static final String MONTHS_12 = "12 Bulan";

    private TabLayout tabs;
    private WrapContentViewPager viewPager;
    private TextView topBarTitle;
    private LocalCacheHandler localCacheHandler;

    ArrayList<MonthsInstallmentItem> monthsInstallmentItemss3 = new ArrayList<>();
    ArrayList<MonthsInstallmentItem> monthsInstallmentItemss6 = new ArrayList<>();
    ArrayList<MonthsInstallmentItem> monthsInstallmentItemss12 = new ArrayList<>();

    @Override
    protected void forceRotation() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localCacheHandler = new LocalCacheHandler(InstallmentActivity.this, PRODUCT_DETAIL);
        setContentView(R.layout.activity_installment);
        hideToolbar();
        initView();
        setupData();
        setupViewPager();
        setupTopbar();
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpByConfiguration(newConfig);
    }

    private void setUpByConfiguration(Configuration configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!localCacheHandler.getBoolean(STATE_ORIENTATION_CHANGED).booleanValue()) {
                String productId = getIntent().getParcelableExtra(EXTRA_PRODUCT_ID);
                UnifyTracking.eventPDPOrientationChanged(productId);
                localCacheHandler.putBoolean(STATE_ORIENTATION_CHANGED,Boolean.TRUE);
                localCacheHandler.applyEditor();
            }
        }
    }

    private void initView() {
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewPager = (WrapContentViewPager) findViewById(R.id.viewpager);
        topBarTitle = (TextView) findViewById(R.id.simple_top_bar_title);
        findViewById(R.id.simple_top_bar_close_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        InstallmentActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
                    }
                });
        setUpByConfiguration(getResources().getConfiguration());
    }

    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(
                InstallmentMonthsFragment.newInstance(monthsInstallmentItemss3), MONTHS_3);
        adapter.addFragment(
                InstallmentMonthsFragment.newInstance(monthsInstallmentItemss6), MONTHS_6);
        adapter.addFragment(
                InstallmentMonthsFragment.newInstance(monthsInstallmentItemss12), MONTHS_12);

        viewPager.setAdapter(adapter);
    }

    private void setupTopbar() {
        topBarTitle.setText(getString(R.string.installment_page_title));
    }

    public void setupData() {
        ArrayList<ProductInstallment> productInstallments =
                getIntent().getParcelableArrayListExtra(KEY_INSTALLMENT_DATA);
        if (productInstallments != null) {
            for (ProductInstallment productInstallment : productInstallments) {
                Terms terms = productInstallment.getTerms();
                if (terms != null && terms.getRule3Months() != null) {
                    monthsInstallmentItemss3.add(
                            new MonthsInstallmentItem(terms.getRule3Months().getPrice(),
                                    terms.getRule3Months().getMinPurchase(), productInstallment.getIcon())
                    );
                }
                if (terms != null && terms.getRule6Months() != null) {
                    monthsInstallmentItemss6.add(
                            new MonthsInstallmentItem(terms.getRule6Months().getPrice(),
                                    terms.getRule6Months().getMinPurchase(), productInstallment.getIcon())
                    );
                }
                if (terms != null && terms.getRule12Months() != null) {
                    monthsInstallmentItemss12.add(
                            new MonthsInstallmentItem(terms.getRule12Months().getPrice(),
                                    terms.getRule12Months().getMinPurchase(), productInstallment.getIcon())
                    );
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InstallmentActivity.this.overridePendingTransition(0,com.tokopedia.core.R.anim.push_down);
    }

}