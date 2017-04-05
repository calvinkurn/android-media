package com.tokopedia.seller.product.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.tokopedia.seller.product.view.adapter.etalase.EtalasePickerAdapter;
import com.tokopedia.seller.product.view.model.etalase.MyEtalaseViewModel;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/5/17.
 */

public class EtalasePickerFragment extends BaseDaggerFragment implements EtalasePickerView{
    public static final String TAG = "EtalasePicker";

    @Inject
    EtalasePickerPresenter presenter;

    private EtalasePickerAdapter adapter;

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

        setupRecyclerView(view);

        presenter.attachView(this);

        initVar();

        return view;
    }

    private void setupRecyclerView(View view) {
        RecyclerView etalaseRecyclerView = (RecyclerView) view.findViewById(R.id.etalase_picker_recycler_view);
        etalaseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new EtalasePickerAdapter();
        etalaseRecyclerView.setAdapter(adapter);
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
        adapter.showLoading(true);
    }

    @Override
    public void dismissLoading() {
        adapter.showLoading(false);
    }

    @Override
    public void renderEtalaseList(List<MyEtalaseViewModel> etalases) {
        adapter.renderData(etalases);
    }
}
