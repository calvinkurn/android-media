package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.events.R;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.domain.model.request.verify.ValidateShow;
import com.tokopedia.events.domain.postusecase.PostValidateShowUseCase;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 28/11/17.
 */
public class EventBookTicketPresenter
        extends BaseDaggerPresenter<EventBookTicketContract.EventBookTicketView>
        implements EventBookTicketContract.Presenter {

    PackageViewModel selectedPackageViewModel;
    int mSelectedPackage = -1;
    int mSelectedSchedule = 0;
    AddTicketAdapter.TicketViewHolder selectedViewHolder;
    List<SchedulesViewModel> schedulesList;
    PostValidateShowUseCase postValidateShowUseCase;
    String dateRange;
    String seatingURL;

    public static String EXTRA_PACKAGEVIEWMODEL = "packageviewmodel";

    @Inject
    public EventBookTicketPresenter(PostValidateShowUseCase useCase) {
        this.postValidateShowUseCase = useCase;
    }


    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void getTicketDetails() {
        EventsDetailsViewModel dataModel = getView().
                getActivity().
                getIntent().
                getParcelableExtra(EventsDetailsPresenter.EXTRA_EVENT_VIEWMODEL);
        seatingURL = getView().
                getActivity().
                getIntent().
                getStringExtra(EventsDetailsPresenter.EXTRA_SEATING_URL);
        this.dateRange = dataModel.getTimeRange();
        getView().renderFromDetails(dataModel);
        if (dataModel.getSeatMapImage() != null && !dataModel.getSeatMapImage().isEmpty())
            getView().renderSeatmap(dataModel.getSeatMapImage());
        else
            getView().hideSeatmap();
        if (!dataModel.getTimeRange().contains("1970"))
            getView().initTablayout();
        schedulesList = dataModel.getSchedulesViewModels();
    }

    @Override
    public void validateSelection() {
        postValidateShowUseCase.execute(RequestParams.EMPTY, new Subscriber<ValidateResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("BookTicketPresenter", "onError");
                throwable.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                payTicketsClick();
                            }
                        });
            }

            @Override
            public void onNext(ValidateResponse objectResponse) {
                if (objectResponse.getStatus() != 400) {
                    Intent reviewTicketIntent = new Intent(getView().getActivity(), ReviewTicketActivity.class);
                    reviewTicketIntent.putExtra(EXTRA_PACKAGEVIEWMODEL, selectedPackageViewModel);
                    getView().navigateToActivityRequest(reviewTicketIntent, 100);
                } else {
                    getView().showMessage(objectResponse.getMessageError());
                }
                getView().hideProgressBar();

            }
        });
    }

    @Override
    public void attachView(EventBookTicketContract.EventBookTicketView view) {
        super.attachView(view);
    }

    public void payTicketsClick() {
        ValidateShow validateShow = new ValidateShow();
        validateShow.setQuantity(selectedPackageViewModel.getSelectedQuantity());
        validateShow.setGroupId(selectedPackageViewModel.getProductGroupId());
        validateShow.setPackageId(selectedPackageViewModel.getId());
        validateShow.setScheduleId(selectedPackageViewModel.getProductScheduleId());
        validateShow.setProductId(selectedPackageViewModel.getProductId());
        postValidateShowUseCase.setValidateShowModel(validateShow);
        getView().showProgressBar();
        validateSelection();

    }

    public void addTickets(int index, PackageViewModel packageVM, AddTicketAdapter.TicketViewHolder ticketViewHolder) {
        if (mSelectedPackage != -1 && mSelectedPackage != index) {
            selectedPackageViewModel.setSelectedQuantity(0);
            selectedViewHolder.setTvTicketCnt(selectedPackageViewModel.getSelectedQuantity());
            selectedViewHolder.setTicketViewColor(getView().getActivity().getResources().getColor(R.color.white));
            mSelectedPackage = index;
            selectedPackageViewModel = packageVM;
            selectedViewHolder = ticketViewHolder;
        } else if (mSelectedPackage == -1) {
            mSelectedPackage = index;
            selectedPackageViewModel = packageVM;
            selectedViewHolder = ticketViewHolder;
        }
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount < selectedPackageViewModel.getMaxQty()) {
            selectedPackageViewModel.setSelectedQuantity(++selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            selectedViewHolder.setTicketViewColor(getView().getActivity().getResources().getColor(R.color.light_green));
        }
        getView().showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice(),selectedPackageViewModel.getDisplayName());
    }

    public void removeTickets() {
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount != 0) {
            selectedPackageViewModel.setSelectedQuantity(--selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            getView().showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice(),selectedPackageViewModel.getDisplayName());
        }
        if (selectedCount == 0) {
            selectedViewHolder.setTicketViewColor(getView().getActivity().getResources().getColor(R.color.white));
            mSelectedPackage = -1;
            selectedViewHolder = null;
            getView().hidePayButton();
        }
    }

    public void onPageChange(int scheduleIndex) {
        mSelectedSchedule = scheduleIndex;
        mSelectedPackage = -1;
    }

    public String[] getDateArray() {
        String[] date = new String[3];
        date[0] = dateRange.substring(0, 3);//day
        //Sat, 14 Apr 2018 - Sat, 14 Apr 2018
        date[1] = dateRange.substring(5, 7).trim();//date
        date[2] = dateRange.substring(8, 11);//month
        return date;
    }
}
