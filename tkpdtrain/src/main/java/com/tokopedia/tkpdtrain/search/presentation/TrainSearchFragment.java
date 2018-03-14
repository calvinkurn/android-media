package com.tokopedia.tkpdtrain.search.presentation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.search.di.TrainSearchComponent;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainSchedule;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainSearchFragment extends BaseDaggerFragment implements TrainSearchContract.View {

    @Inject
    TrainSearchPresenter presenter;

    public static TrainSearchFragment newInstance(){
        return new TrainSearchFragment();
    }

    public TrainSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_train_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getTrainSchedules();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TrainSearchComponent.class).inject(this);
    }

    @Override
    public void showSearchResult(List<TrainSchedule> schedules) {

    }
}
