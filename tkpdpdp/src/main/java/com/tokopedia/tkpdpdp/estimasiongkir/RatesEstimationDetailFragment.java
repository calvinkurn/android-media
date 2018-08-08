package com.tokopedia.tkpdpdp.estimasiongkir;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdpdp.R;
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationComponent;
import com.tokopedia.tkpdpdp.estimasiongkir.listener.RatesEstimationDetailView;
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.RatesEstimationDetailPresenter;

import javax.inject.Inject;

public class RatesEstimationDetailFragment extends BaseDaggerFragment implements RatesEstimationDetailView{
    @Inject RatesEstimationDetailPresenter presenter;

    public static RatesEstimationDetailFragment createInstance(){
        return new RatesEstimationDetailFragment();
    }

    @Override
    protected void initInjector() {
        getComponent(RatesEstimationComponent.class).inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rates_estimation_detail, container, false);
    }
}
