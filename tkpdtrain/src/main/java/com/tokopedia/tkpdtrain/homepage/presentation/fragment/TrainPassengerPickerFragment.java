package com.tokopedia.tkpdtrain.homepage.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.TrainPassengerPickerView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainPassengerViewModel;
import com.tokopedia.tkpdtrain.homepage.presentation.presenter.TrainPassengerPickerPresenterImpl;
import com.tokopedia.tkpdtrain.homepage.presentation.widget.SelectPassengerView;

/**
 * @author Rizky on 13/03/18.
 */

public class TrainPassengerPickerFragment extends BaseDaggerFragment implements TrainPassengerPickerView {

    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    private SelectPassengerView selectPassengerViewAdult;
    private SelectPassengerView selectPassengerViewInfant;
    private Button buttonSave;

    private TrainPassengerPickerPresenterImpl trainPassengerPickerPresenter;

    private TrainPassengerViewModel trainPassengerViewModel;

    private OnFragmentInteractionListener actionListener;

    public interface OnFragmentInteractionListener {
        void actionSavePassenger(TrainPassengerViewModel passData);
    }

    public static TrainPassengerPickerFragment newInstance(TrainPassengerViewModel passData) {
        TrainPassengerPickerFragment fragment = new TrainPassengerPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_passenger_picker, container, false);

        selectPassengerViewAdult = view.findViewById(R.id.spv_passenger_adult);
        selectPassengerViewInfant = view.findViewById(R.id.spv_passenger_infant);
        buttonSave = view.findViewById(R.id.button_save);

        selectPassengerViewAdult.setOnPassengerCountChangeListener(new SelectPassengerView.OnPassengerCountChangeListener() {
            @Override
            public boolean onChange(int number) {
                trainPassengerPickerPresenter.onAdultPassengerCountChange(number);
                return true;
            }
        });

        selectPassengerViewInfant.setOnPassengerCountChangeListener(new SelectPassengerView.OnPassengerCountChangeListener() {
            @Override
            public boolean onChange(int number) {
                trainPassengerPickerPresenter.onInfantPassengerCountChange(number);
                return true;
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainPassengerPickerPresenter.onSaveButtonClicked();
            }
        });

        trainPassengerPickerPresenter = new TrainPassengerPickerPresenterImpl();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trainPassengerViewModel = getArguments().getParcelable(EXTRA_PASS_DATA);

        trainPassengerPickerPresenter.attachView(this);
        trainPassengerPickerPresenter.initialize();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void onAttachActivity(Context context) {
        if (context instanceof OnFragmentInteractionListener) {
            actionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void renderPassengerView(TrainPassengerViewModel trainPassengerViewModel) {
        this.trainPassengerViewModel = trainPassengerViewModel;
        selectPassengerViewAdult.setValue(trainPassengerViewModel.getAdult());
        selectPassengerViewInfant.setValue(trainPassengerViewModel.getInfant());
    }

    @Override
    public void showTotalPassengerErrorMessage(@StringRes int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showAdultCantBeGreaterThanFourErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showInfantGreaterThanAdultErrorMessage(@StringRes int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showAdultShouldAtleastOneErrorMessage(@StringRes int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void actionNavigateBack(TrainPassengerViewModel currentPassengerPassData) {
        if (actionListener != null) {
            actionListener.actionSavePassenger(currentPassengerPassData);
        }
    }

    @Override
    public TrainPassengerViewModel getCurrentPassengerViewModel() {
        return trainPassengerViewModel;
    }

}
