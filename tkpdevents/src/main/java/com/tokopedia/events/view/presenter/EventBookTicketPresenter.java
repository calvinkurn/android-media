package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
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

import retrofit2.Response;
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
        EventsDetailsViewModel dataModel = getView().getActivity().getIntent().getParcelableExtra(EventsDetailsPresenter.EXTRA_EVENT_VIEWMODEL);
        getView().renderFromDetails(dataModel);
        schedulesList = dataModel.getSchedulesViewModels();
    }

    @Override
    public void validateSelection() {

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
        postValidateShowUseCase.execute(RequestParams.EMPTY, new Subscriber<ValidateResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("BookTicketPresenter", "onError");
                throwable.printStackTrace();
            }

            @Override
            public void onNext(ValidateResponse objectResponse) {
                if(objectResponse.getStatus()!=400){
                    Intent reviewTicketIntent = new Intent(getView().getActivity(), ReviewTicketActivity.class);
                    reviewTicketIntent.putExtra(EXTRA_PACKAGEVIEWMODEL, selectedPackageViewModel);
                    getView().navigateToActivityRequest(reviewTicketIntent, 100);
                } else {
                    getView().showMessage(objectResponse.getMessageError());
                }

            }
        });

    }

    public void addTickets(int index, PackageViewModel packageVM, AddTicketAdapter.TicketViewHolder ticketViewHolder) {
        if (mSelectedPackage != -1 && mSelectedPackage != index) {
            selectedPackageViewModel.setSelectedQuantity(0);
            selectedViewHolder.setTvTicketCnt(selectedPackageViewModel.getSelectedQuantity());
            selectedViewHolder.setTvTicketCntColor(getView().getActivity().getResources().getColor(R.color.black_38));
            selectedViewHolder.setTvTicketNameColor(getView().getActivity().getResources().getColor(R.color.black_38));
            selectedViewHolder.setTicketSalePriceColor(getView().getActivity().getResources().getColor(R.color.black_38));
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
            selectedViewHolder.setTvTicketCntColor(getView().getActivity().getResources().getColor(R.color.black_70));
            selectedViewHolder.setTvTicketNameColor(getView().getActivity().getResources().getColor(R.color.black_70));
            selectedViewHolder.setTicketSalePriceColor(getView().getActivity().getResources().getColor(R.color.black_70));
        }
        getView().showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice());
    }

    public void removeTickets() {
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount != 0) {
            selectedPackageViewModel.setSelectedQuantity(--selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            getView().showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice());
        }
        if (selectedCount == 0) {
            selectedViewHolder.setTvTicketCntColor(getView().getActivity().getResources().getColor(R.color.black_38));
            selectedViewHolder.setTvTicketNameColor(getView().getActivity().getResources().getColor(R.color.black_38));
            selectedViewHolder.setTicketSalePriceColor(getView().getActivity().getResources().getColor(R.color.black_38));
            getView().hidePayButton();
        }
    }

    public void onPageChange(int scheduleIndex) {
        mSelectedSchedule = scheduleIndex;
        mSelectedPackage = -1;
    }
}
