package com.tokopedia.seller.opportunity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailButtonView;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailProductView;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailStatusView;
import com.tokopedia.seller.opportunity.customview.OppurtunityDetailSummaryView;
import com.tokopedia.seller.opportunity.listener.OppurtunityView;
import com.tokopedia.seller.opportunity.presenter.OppurtunityImpl;
import com.tokopedia.seller.opportunity.presenter.OppurtunityPresenter;

import butterknife.BindView;

/**
 * Created by hangnadi on 2/27/17.
 */

public class OppurtunityDetailFragment extends BasePresenterFragment<OppurtunityPresenter>
        implements OppurtunityView {

    @BindView(R2.id.customview_oppurtunity_detail_button_view)
    OppurtunityDetailButtonView buttonView;
    @BindView(R2.id.customview_oppurtunity_detail_status_view)
    OppurtunityDetailStatusView statusView;
    @BindView(R2.id.customview_oppurtunity_detail_product_view)
    OppurtunityDetailProductView productView;
    @BindView(R2.id.customview_oppurtunity_detail_summary_view)
    OppurtunityDetailSummaryView summaryView;

    public OppurtunityDetailFragment() {
    }

    @Override
    public void onActionDeleteClicked() {

    }

    @Override
    public void onActionSubmitClicked() {

    }

    public static Fragment createInstance() {
        OppurtunityDetailFragment fragment = new OppurtunityDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new OppurtunityImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_oppurtunity_detail;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {
        buttonView.setListener(this);
        statusView.setListener(this);
        productView.setListener(this);
        summaryView.setListener(this);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

}
