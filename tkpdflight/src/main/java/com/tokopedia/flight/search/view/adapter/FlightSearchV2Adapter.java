package com.tokopedia.flight.search.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapterV2;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.adapter.viewholder.EmptyResultViewHolder;
import com.tokopedia.flight.search.view.model.EmptyResultViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchV2Adapter extends BaseListAdapterV2<FlightSearchViewModel, FilterSearchAdapterTypeFactory> {

    private OnBaseFlightSearchAdapterListener onBaseFlightSearchAdapterListener;
    private FilterSearchAdapterTypeFactory filterSearchAdapterTypeFactory;
    private ErrorNetworkModel errorNetworkModel;
    private boolean inFilterMode;


    public FlightSearchV2Adapter(FilterSearchAdapterTypeFactory filterSearchAdapterTypeFactory) {
        super(filterSearchAdapterTypeFactory);
        setDefaultErrorNetwork();
        setDefaultLoading();
        this.filterSearchAdapterTypeFactory = filterSearchAdapterTypeFactory;
    }

    private void setDefaultLoading() {
        LoadingModel itemLoading = new LoadingModel();
        itemLoading.setFullScreen(true);
        loadingModel = itemLoading;
    }

    public void setOnBaseFlightSearchAdapterListener(OnBaseFlightSearchAdapterListener onBaseFlightSearchAdapterListener) {
        this.onBaseFlightSearchAdapterListener = onBaseFlightSearchAdapterListener;
    }

    private void setDefaultErrorNetwork() {
        errorNetworkModel = new ErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(R.drawable.ic_flight_empty_state);
    }

    public void setErrorMessage(String message) {
        if (errorNetworkModel != null)
            errorNetworkModel.setErrorMessage(message);
    }

    public void setInFilterMode(boolean b) {
        inFilterMode = b;
    }

    public void showErrorNetwork() {
        visitables.clear();
        visitables.add(errorNetworkModel);
        notifyDataSetChanged();
    }

    public Visitable getEmptyViewModel() {
        EmptyResultViewModel emptyResultViewModel;
        if (inFilterMode) {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_flight_empty_state);
            emptyResultViewModel.setContentRes(R.string.flight_there_is_zero_flight_for_the_filter);
            emptyResultViewModel.setButtonTitleRes(R.string.reset_filter);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    onBaseFlightSearchAdapterListener.onResetFilterClicked();
                }
            });


        } else {
            emptyResultViewModel = new EmptyResultViewModel();
            emptyResultViewModel.setIconRes(R.drawable.ic_flight_empty_state);
            emptyResultViewModel.setContentRes(R.string.flight_there_is_no_flight_available);
            emptyResultViewModel.setButtonTitleRes(R.string.change_date);
            emptyResultViewModel.setCallback(new EmptyResultViewHolder.Callback() {
                @Override
                public void onEmptyContentItemTextClicked() {

                }

                @Override
                public void onEmptyButtonClicked() {
                    onBaseFlightSearchAdapterListener.onChangeDateClicked();
                }
            });
        }
        return emptyResultViewModel;
    }


    public interface OnBaseFlightSearchAdapterListener {
        void onRetryClicked();

        void onResetFilterClicked();

        void onChangeDateClicked();

        void onDetailClicked(FlightSearchViewModel flightSearchViewModel);
    }
}
