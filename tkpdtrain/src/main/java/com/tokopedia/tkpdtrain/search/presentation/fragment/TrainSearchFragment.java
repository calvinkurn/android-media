package com.tokopedia.tkpdtrain.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyResultViewHolder;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.design.button.BottomActionView;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.di.utils.TrainComponentUtils;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.constant.TrainSortOption;
import com.tokopedia.tkpdtrain.search.di.DaggerTrainSearchComponent;
import com.tokopedia.tkpdtrain.search.di.TrainSearchComponent;
import com.tokopedia.tkpdtrain.search.domain.GetScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.adapter.TrainSearchAdapterTypeFactory;
import com.tokopedia.tkpdtrain.search.presentation.contract.TrainSearchContract;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.tkpdtrain.search.presentation.presenter.TrainSearchPresenter;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TrainSearchFragment extends BaseListFragment<TrainScheduleViewModel,
        TrainSearchAdapterTypeFactory> implements TrainSearchContract.View {

    private static final String TAG = TrainSearchFragment.class.getSimpleName();

    protected TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    protected String dateDeparture;
    protected int adultPassanger;
    protected int infantPassanger;
    protected String originCode;
    protected String originCity;
    protected String destinationCode;
    protected String destinationCity;
    private TextView originCodeTv;
    private TextView originCityTv;
    private TextView destinationCodeTv;
    private TextView destinationCityTv;
    private LinearLayout tripInfoLinearLayout;
    protected TrainSearchComponent trainSearchComponent;

    private BottomActionView filterAndSortBottomAction;

    @Inject
    TrainSearchPresenter presenter;

    public TrainSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter.attachView(this);
        View view = inflater.inflate(getLayoutView(), container, false);
        originCodeTv = view.findViewById(R.id.origin_code);
        originCityTv = view.findViewById(R.id.origin_city);
        destinationCodeTv = view.findViewById(R.id.destination_code);
        destinationCityTv = view.findViewById(R.id.destination_city);
        tripInfoLinearLayout = view.findViewById(R.id.layout_trip_info);
        return view;
    }

    protected int getLayoutView() {
        return R.layout.fragment_train_search;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromFragment();
        showLoading();
        presenter.getTrainSchedules(getScheduleVariant());

        setUpButtonActionView(view);
        originCodeTv.setText(originCode);
        originCityTv.setText(originCity);
        destinationCodeTv.setText(destinationCode);
        destinationCityTv.setText(destinationCity);
    }

    @NonNull
    @Override
    protected BaseListAdapter<TrainScheduleViewModel, TrainSearchAdapterTypeFactory> createAdapterInstance() {
        BaseListAdapter<TrainScheduleViewModel, TrainSearchAdapterTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_train_error_network);
        errorNetworkModel.setErrorMessage(getString(R.string.search_no_connection_title));
        errorNetworkModel.setSubErrorMessage(getString(R.string.search_no_connection));
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    protected abstract int getScheduleVariant();

    protected abstract void getDataFromFragment();

    private void setUpButtonActionView(View view) {
        filterAndSortBottomAction = (BottomActionView) view.findViewById(R.id.bottom_action_filter_sort);
        filterAndSortBottomAction.setButton1OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO filter button action
                Toast.makeText(getActivity(), "filter", Toast.LENGTH_SHORT).show();
            }
        });
        filterAndSortBottomAction.setButton2OnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortBottomSheets();
            }
        });
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        //TODO handle if user from filter searching
        EmptyResultViewModel emptyResultViewModel = new EmptyResultViewModel();
        emptyResultViewModel.setIconRes(R.drawable.ic_train_no_result);
        emptyResultViewModel.setContentRes(R.string.search_no_result_default);
        emptyResultViewModel.setButtonTitleRes(R.string.search_reset_button);
        emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
            @Override
            public void onEmptyContentItemTextClicked() {

            }

            @Override
            public void onEmptyButtonClicked() {

            }
        });
        return emptyResultViewModel;
    }

    @Override
    public RequestParams getRequestParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetScheduleUseCase.DATE_SCHEDULE, dateDeparture);
        requestParams.putInt(GetScheduleUseCase.TOTAL_ADULT, adultPassanger);
        requestParams.putInt(GetScheduleUseCase.TOTAL_INFANT, infantPassanger);
        requestParams.putString(GetScheduleUseCase.ORIGIN_CODE, originCode);
        requestParams.putString(GetScheduleUseCase.ORIGIN_CITY, originCity);
        requestParams.putString(GetScheduleUseCase.DEST_CODE, destinationCode);
        requestParams.putString(GetScheduleUseCase.DEST_CITY, destinationCity);
        return requestParams;
    }

    @Override
    public void hideLayoutTripInfo() {
        tripInfoLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutTripInfo() {
        tripInfoLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        if (trainSearchComponent == null) {
            trainSearchComponent = DaggerTrainSearchComponent.builder()
                    .trainComponent(TrainComponentUtils.getTrainComponent(getActivity().getApplication())).build();
        }
        trainSearchComponent
                .inject(this);
    }

    @Override
    protected TrainSearchAdapterTypeFactory getAdapterTypeFactory() {
        return new TrainSearchAdapterTypeFactory(new TrainSearchAdapterTypeFactory.OnTrainSearchListener() {

        });
    }

    @Override
    public void onItemClicked(TrainScheduleViewModel trainScheduleViewModel) {
        //TODO make tap to go to detail schedule page
        Toast.makeText(getActivity(), trainScheduleViewModel.getTrainName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadData(int page) {
    }

    public void showSortBottomSheets() {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem("Urutkan");

        bottomSheetBuilder.addItem(TrainSortOption.EARLIEST_DEPARTURE, "Waktu Keberangkatan Terpagi", null);
        bottomSheetBuilder.addItem(TrainSortOption.LATEST_DEPARTURE, "Waktu Keberangkatan Termalam", null);
        bottomSheetBuilder.addItem(TrainSortOption.SHORTEST_DURATION, "Durasi Terpendek", null);
        bottomSheetBuilder.addItem(TrainSortOption.LONGEST_DURATION, "Durasi Terlama", null);
        bottomSheetBuilder.addItem(TrainSortOption.EARLIEST_ARRIVAL, "Waktu Tiba Terpagi", null);
        bottomSheetBuilder.addItem(TrainSortOption.LATEST_ARRIVAL, "Waktu Tiba Termalam", null);
        bottomSheetBuilder.addItem(TrainSortOption.CHEAPEST, "Harga Termurah", null);
        bottomSheetBuilder.addItem(TrainSortOption.MOST_EXPENSIVE, "Harga Termahal", null);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @SuppressWarnings("WrongConstant")
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        List<String> trains = new ArrayList<>();
                        presenter.getFilteredAndSortedSchedules(0, 200000, "", trains, item.getItemId());
                    }
                })
                .createDialog();
        bottomSheetDialog.show();
    }

}