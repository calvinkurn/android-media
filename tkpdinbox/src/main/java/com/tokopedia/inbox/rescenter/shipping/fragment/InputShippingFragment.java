package com.tokopedia.inbox.rescenter.shipping.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.shipping.customadapter.ShippingSpinnerAdapter;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsModel;
import com.tokopedia.inbox.rescenter.shipping.model.ResCenterKurir;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingFragmentImpl;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingFragmentPresenter;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingRefNumView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingFragment extends BasePresenterFragment<InputShippingFragmentPresenter>
        implements InputShippingRefNumView {

    public static final String EXTRA_PARAM_MODEL = "params_model";

    @BindView(R2.id.ref_number)
    EditText shippingRefNum;
    @BindView(R2.id.spinner_kurir)
    Spinner shippingSpinner;
    @BindView(R2.id.error_spinner)
    TextView errorSpinner;
    @BindView(R2.id.list_upload_proof)
    RecyclerView listAttachment;
    @BindView(R2.id.loading)
    View loadingView;
    @BindView(R2.id.main_view)
    View mainView;

    private InputShippingParamsModel paramsModel;

    public static Fragment newInstance(InputShippingParamsModel model) {
        InputShippingFragment fragment = new InputShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PARAM_MODEL, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R2.id.confirm_button)
    public void onConfirmButtonClick() {

    }

    @Override
    public InputShippingParamsModel getParamsModel() {
        return paramsModel;
    }

    @Override
    public void setParamsModel(InputShippingParamsModel paramsModel) {
        this.paramsModel = paramsModel;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.onFirstTimeLaunched();
    }

    @Override
    public void onSaveState(Bundle state) {
        presenter.onSaveState(state);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        presenter.onRestoreState(savedState);
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new InputShippingFragmentImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        paramsModel = arguments.getParcelable(EXTRA_PARAM_MODEL);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_input_shipping_ref_num;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public void renderSpinner(List<ResCenterKurir.Kurir> shippingList) {
        ShippingSpinnerAdapter shippingAdapter = new ShippingSpinnerAdapter(
                context,
                android.R.layout.simple_spinner_item,
                shippingList
        );
        shippingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shippingSpinner.setAdapter(shippingAdapter);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showTimeOutMessage(NetworkErrorHelper.RetryClickedListener listener) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), listener);
    }

    @Override
    public void showErrorMessage(String message) {
        if (message != null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, null);
        } else {
            showTimeOutMessage(null);
        }
    }

    @Override
    public void showLoading(boolean isVisible) {
        loadingView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMainPage(boolean isVisible) {
        mainView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
