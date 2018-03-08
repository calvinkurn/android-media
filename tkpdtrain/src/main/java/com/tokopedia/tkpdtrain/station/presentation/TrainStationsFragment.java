package com.tokopedia.tkpdtrain.station.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.station.di.TrainStationsComponent;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationAdapterTypeFactory;
import com.tokopedia.tkpdtrain.station.presentation.adapter.TrainStationTypeFactory;
import com.tokopedia.tkpdtrain.station.presentation.contract.TrainStationsContract;
import com.tokopedia.tkpdtrain.station.presentation.presenter.TrainStationsPresenter;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainStationsFragment extends BaseSearchListFragment<Visitable, TrainStationTypeFactory> implements TrainStationsContract.View {


    @Inject
    TrainStationsPresenter presenter;

    private boolean isFirstTime = true;

    public static TrainStationsFragment newInstance() {
        return new TrainStationsFragment();
    }

    public TrainStationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train_stations, container, false);
        view.requestFocus();
        return view;
    }

    @Override
    public void loadData(int page) {
        if (isFirstTime) {
            presenter.actionOnInitialLoad();
        }
    }

    @Override
    protected TrainStationTypeFactory getAdapterTypeFactory() {
        return new TrainStationAdapterTypeFactory();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TrainStationsComponent.class).inject(this);
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    @Override
    public void onItemClicked(Visitable visitable) {

    }

    @Override
    public void renderList(Visitable visitable) {
        getAdapter().addElement(visitable);
    }
}
