package com.tokopedia.ride.bookingride.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.ApplyPromoDependencyInjection;
import com.tokopedia.ride.bookingride.domain.ApplyPromoUseCase;
import com.tokopedia.ride.bookingride.view.ApplyPromoContract;
import com.tokopedia.ride.common.ride.domain.model.ApplyPromo;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * Created by alvarisi on 4/25/17.
 */


public class ApplyPromoFragment extends BaseFragment implements ApplyPromoContract.View {
    @BindView(R2.id.et_promo)
    EditText promoEditText;
    @BindView(R2.id.tv_submit_promo)
    TextView submitPromoTextView;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R2.id.ll_promo_layout)
    LinearLayout promoLayout;

    ApplyPromoContract.Presenter presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = ApplyPromoDependencyInjection.createPresenter(getActivity());
        presenter.attachView(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_apply_promo;
    }

    public static Fragment newInstance() {
        return new ApplyPromoFragment();
    }

    @OnClick(R2.id.tv_submit_promo)
    public void actionSubmitPromo() {
        presenter.actionApplyPromo();
    }

    @Override
    public String getPromo() {
        return promoEditText.getText().toString();
    }

    @Override
    public RequestParams getApplyPromoParams() {
        //TODO : change param when api created
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ApplyPromoUseCase.PARAM_USER_ID, userId);
        requestParams.putString(ApplyPromoUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(ApplyPromoUseCase.PARAM_HASH, hash);
        requestParams.putString(ApplyPromoUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(ApplyPromoUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        requestParams.putString(ApplyPromoUseCase.PARAM_PROMO, getPromo());
        return requestParams;
    }

    @Override
    public void hideApplyPromoLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessApplyPromo(ApplyPromo applyPromo) {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onFailedApplyPromo(ApplyPromo applyPromo) {
        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public void showApplyPromoLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedApplyPromo(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public void showApplyPromoLayout() {
        promoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideApplyPromoLayout() {
        promoLayout.setVisibility(View.GONE);
    }
}
