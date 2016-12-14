package com.tokopedia.inbox.rescenter.shipping.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.inbox.R;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.inbox.rescenter.shipping.model.InputShippingParamsModel;
import com.tokopedia.inbox.rescenter.shipping.view.InputShippingRefNumView;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingFragmentImpl;
import com.tokopedia.inbox.rescenter.shipping.presenter.InputShippingFragmentPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hangnadi on 12/13/16.
 */
public class InputShippingFragment extends BasePresenterFragment<InputShippingFragmentPresenter>
        implements InputShippingRefNumView {

    private static final String EXTRA_PARAM_MODEL = "params_model";

    @BindView(R2.id.ref_number)
    EditText shippingRefNum;
    @BindView(R2.id.spinner_kurir)
    Spinner spinnerKurir;
    @BindView(R2.id.error_spinner)
    TextView errorSpinner;
    @BindView(R2.id.list_upload_proof)
    RecyclerView listAttachment;

    @OnClick(R2.id.confirm_button)
    public void onConfirmButtonClick() {

    }

    public static Fragment newInstance(InputShippingParamsModel model) {
        InputShippingFragment fragment = new InputShippingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PARAM_MODEL, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
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
        presenter = new InputShippingFragmentImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_input_shipping_ref_num;
    }

    @Override
    protected void initView(View view) {

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
}
