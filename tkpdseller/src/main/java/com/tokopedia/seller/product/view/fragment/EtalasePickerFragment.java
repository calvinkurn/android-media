package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.di.component.DaggerEtalasePickerViewComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerComponent;
import com.tokopedia.seller.product.di.component.EtalasePickerViewComponent;
import com.tokopedia.seller.product.di.module.EtalasePickerViewModule;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenter;
import com.tokopedia.seller.topads.data.model.data.Etalase;

import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerFragment extends BaseDaggerFragment implements EtalasePickerView{
    public static final String TAG = "EtalasePicker";

    @Inject
    EtalasePickerPresenter presenter;

    public static EtalasePickerFragment createInstance() {
        return new EtalasePickerFragment();
    }

    @Override
    protected void initInjector() {
        EtalasePickerViewComponent component = DaggerEtalasePickerViewComponent
                .builder()
                .etalasePickerComponent(getComponent(EtalasePickerComponent.class))
                .etalasePickerViewModule(new EtalasePickerViewModule())
                .build();
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.etalase_picker_fragment_layout, container, false);
        presenter.attachView(this);
        initVar();
        return view;
    }

    private void initVar() {
        String shopId = new SessionHandler(getActivity()).getShopID();
        presenter.fetchEtalaseData(shopId);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void renderEtalaseList(List<Etalase> etalases) {

    }
}
