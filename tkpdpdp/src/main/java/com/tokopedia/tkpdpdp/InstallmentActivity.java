package com.tokopedia.tkpdpdp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.TextView;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customView.WrapContentViewPager;
import com.tokopedia.core.product.model.etalase.MonthsInstallmentItem;
import com.tokopedia.core.product.model.productdetail.ProductInstallment;
import com.tokopedia.core.product.model.productdetail.Terms;
import com.tokopedia.tkpdpdp.adapter.ViewPagerAdapter;
import com.tokopedia.tkpdpdp.fragment.InstallmentMonthsFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstallmentActivity extends TActivity {

    public static final String KEY_INSTALLMENT_DATA = "WHOLESALE_DATA";
    public static final String MONTHS_3 = "3 Bulan";
    public static final String MONTHS_6 = "6 Bulan";
    public static final String MONTHS_12 = "12 Bulan";

    @BindView(R2.id.tabs)
    TabLayout tabs;
    @BindView(R2.id.viewpager)
    WrapContentViewPager viewPager;
    @BindView(R2.id.simple_top_bar_title)
    TextView topBarTitle;

    ArrayList<MonthsInstallmentItem> monthsInstallmentItemss3 = new ArrayList<>();
    ArrayList<MonthsInstallmentItem> monthsInstallmentItemss6 = new ArrayList<>();
    ArrayList<MonthsInstallmentItem> monthsInstallmentItemss12 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installment);
        hideToolbar();
        ButterKnife.bind(this);
        setupData();
        setupViewPager();
        setupTopbar();
        tabs.setupWithViewPager(viewPager);

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InstallmentMonthsFragment(monthsInstallmentItemss3), MONTHS_3);
        adapter.addFragment(new InstallmentMonthsFragment(monthsInstallmentItemss6), MONTHS_6);
        adapter.addFragment(new InstallmentMonthsFragment(monthsInstallmentItemss12), MONTHS_12);
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
                    monthsInstallmentItemss3.add(new MonthsInstallmentItem(terms.getRule3Months().getPercentage(),
                            terms.getRule3Months().getMinPurchase(), productInstallment.getIcon()));
                }
                if (terms != null && terms.getRule6Months() != null) {
                    monthsInstallmentItemss6.add(new MonthsInstallmentItem(terms.getRule6Months().getPercentage(),
                            terms.getRule6Months().getMinPurchase(), productInstallment.getIcon()));
                }
                if (terms != null && terms.getRule12Months() != null) {
                    monthsInstallmentItemss12.add(new MonthsInstallmentItem(terms.getRule12Months().getPercentage(),
                            terms.getRule12Months().getMinPurchase(), productInstallment.getIcon()));
                }
            }
        }
    }

    @OnClick(R2.id.simple_top_bar_close_button)
    public void onCloseButtonClick() {
        finish();
    }


}
