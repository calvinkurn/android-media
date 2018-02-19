package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.ValidateResponse;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.EventSeatLayoutResonse;
import com.tokopedia.events.domain.GetEventSeatLayoutUseCase;
import com.tokopedia.events.domain.model.request.verify.ValidateShow;
import com.tokopedia.events.domain.postusecase.PostValidateShowUseCase;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.activity.SeatSelectionActivity;
import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.mapper.SeatLayoutResponseToSeatLayoutViewModelMapper;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 28/11/17.
 */
public class EventBookTicketPresenter
        extends BaseDaggerPresenter<EventBookTicketContract.EventBookTicketView>
        implements EventBookTicketContract.Presenter {

    private PackageViewModel selectedPackageViewModel;
    private GetEventSeatLayoutUseCase getSeatLayoutUseCase;
    private ProfileUseCase profileUseCase;
    private SeatLayoutViewModel seatLayoutViewModel;
    private int mSelectedPackage = -1;
    private int mSelectedSchedule = 0;
    private AddTicketAdapter.TicketViewHolder selectedViewHolder;
    private List<SchedulesViewModel> schedulesList;
    private PostValidateShowUseCase postValidateShowUseCase;
    private String dateRange;
    private String eventTitle;
    private int hasSeatLayout;
    private FragmentAddTickets mChildFragment;
    private int px;


    public static String EXTRA_PACKAGEVIEWMODEL = "packageviewmodel";
    public static String EXTRA_SEATLAYOUTVIEWMODEL = "seatlayoutviewmodel";

    @Inject
    public EventBookTicketPresenter(GetEventSeatLayoutUseCase seatLayoutUseCase, PostValidateShowUseCase useCase, ProfileUseCase profileCase) {
        this.getSeatLayoutUseCase = seatLayoutUseCase;
        this.postValidateShowUseCase = useCase;
        this.profileUseCase = profileCase;
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
        hasSeatLayout = getView().getActivity().getIntent().getIntExtra(EventsDetailsPresenter.EXTRA_SEATING_PARAMETER, 0);
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
                                validateSelection();
                            }
                        });
            }

            @Override
            public void onNext(ValidateResponse objectResponse) {
                if (objectResponse.getStatus() != 400) {
                    if (hasSeatLayout == 1 && seatLayoutViewModel.getArea() != null) {
                        Intent reviewTicketIntent = new Intent(getView().getActivity(), SeatSelectionActivity.class);
                        reviewTicketIntent.putExtra(EXTRA_PACKAGEVIEWMODEL, selectedPackageViewModel);
                        reviewTicketIntent.putExtra(EXTRA_SEATLAYOUTVIEWMODEL, seatLayoutViewModel);
                        reviewTicketIntent.putExtra("EventTitle", eventTitle);
                        getView().navigateToActivityRequest(reviewTicketIntent, 100);
                    } else {
                        Intent reviewTicketIntent = new Intent(getView().getActivity(), ReviewTicketActivity.class);
                        reviewTicketIntent.putExtra(EXTRA_PACKAGEVIEWMODEL, selectedPackageViewModel);
                        getView().navigateToActivityRequest(reviewTicketIntent, 100);
                    }
                } else {
                    getView().showMessage(objectResponse.getMessageError());
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode) {
        if (requestCode == getView().getRequestCode()) {
            if (SessionHandler.isV4Login(getView().getActivity())) {
                getProfile();
            }
        }
    }

    private void getProfile() {
        getView().showProgressBar();
        profileUseCase.execute(RequestParams.EMPTY, new Subscriber<ProfileModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("ProfileUseCase", "ON ERROR");
                throwable.printStackTrace();
                Intent intent = ((TkpdCoreRouter) getView().getActivity().getApplication()).
                        getLoginIntent(getView().getActivity());
                getView().navigateToActivityRequest(intent, getView().getRequestCode());
                getView().hideProgressBar();
            }

            @Override
            public void onNext(ProfileModel model) {
                getView().hideProgressBar();
                if (hasSeatLayout == 1)
                    getSeatSelectionDetails();
                else
                    validateSelection();
            }
        });
    }

    @Override
    public void attachView(EventBookTicketContract.EventBookTicketView view) {
        super.attachView(view);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getView().getActivity().getResources().getDisplayMetrics());
    }

    @Override
    public void payTicketsClick(String title) {
        eventTitle = title;
        ValidateShow validateShow = new ValidateShow();
        validateShow.setQuantity(selectedPackageViewModel.getSelectedQuantity());
        validateShow.setGroupId(selectedPackageViewModel.getProductGroupId());
        validateShow.setPackageId(selectedPackageViewModel.getId());
        validateShow.setScheduleId(selectedPackageViewModel.getProductScheduleId());
        validateShow.setProductId(selectedPackageViewModel.getProductId());
        postValidateShowUseCase.setValidateShowModel(validateShow);
        getProfile();
    }

    public void addTickets(int index, PackageViewModel packageVM, AddTicketAdapter.TicketViewHolder ticketViewHolder) {
        if (mSelectedPackage != -1 && mSelectedPackage != index) {
            selectedPackageViewModel.setSelectedQuantity(0);
            selectedViewHolder.setTvTicketCnt(selectedPackageViewModel.getSelectedQuantity());
            selectedViewHolder.setTicketViewColor(getView().getActivity().getResources().getColor(R.color.white));
            mSelectedPackage = index;
            selectedPackageViewModel = packageVM;
            selectedViewHolder = ticketViewHolder;
            scrollToLastIfNeeded();
        } else if (mSelectedPackage == -1) {
            mSelectedPackage = index;
            selectedPackageViewModel = packageVM;
            selectedViewHolder = ticketViewHolder;
            scrollToLastIfNeeded();
        }
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount < selectedPackageViewModel.getAvailable() && selectedCount < selectedPackageViewModel.getMaxQty()) {
            selectedPackageViewModel.setSelectedQuantity(++selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            selectedViewHolder.setTicketViewColor(getView().getActivity().getResources().getColor(R.color.light_green));
        } else {
            selectedViewHolder.toggleMaxTicketWarning(View.VISIBLE);
        }
        getView().showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice(), selectedPackageViewModel.getDisplayName());
    }

    public void removeTickets() {
        int selectedCount = selectedPackageViewModel.getSelectedQuantity();
        if (selectedCount != 0) {
            selectedPackageViewModel.setSelectedQuantity(--selectedCount);
            selectedViewHolder.setTvTicketCnt(selectedCount);
            selectedViewHolder.toggleMaxTicketWarning(View.INVISIBLE);
            getView().showPayButton(selectedCount, selectedPackageViewModel.getSalesPrice(), selectedPackageViewModel.getDisplayName());
        }
        if (selectedCount == 0) {
            selectedViewHolder.setTicketViewColor(getView().getActivity().getResources().getColor(R.color.white));
            mSelectedPackage = -1;
            selectedViewHolder = null;
            mChildFragment.setDecorationHeight(0);
            getView().hidePayButton();
        }
    }

    public void onPageChange(int scheduleIndex) {
        mSelectedSchedule = scheduleIndex;
        mSelectedPackage = -1;
    }

    public String getDateArray() {
        return dateRange;
    }


    private void getSeatSelectionDetails() {
        RequestParams params = RequestParams.create();
        params.putString("seatlayouturl",selectedPackageViewModel.getFetchSectionUrl());
        getView().showProgressBar();
        getSeatLayoutUseCase.execute(params, new Subscriber<List<SeatLayoutItem>>() {
            @Override
            public void onCompleted() {
                Log.d("Naveen", " on Completed");
            }

            @Override
            public void onError(Throwable throwable) {
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getSeatSelectionDetails();
                            }
                        });
//                Log.d("Naveen", " on Error" + throwable.getMessage());
            }

            @Override
            public void onNext(List<SeatLayoutItem> response) {
                getView().hideProgressBar();
                seatLayoutViewModel = convertResponseToViewModel(convertoSeatLayoutResponse(response.get(0)));
                validateSelection();
            }

        });

    }

    private EventSeatLayoutResonse convertoSeatLayoutResponse(SeatLayoutItem responseEntity) {

        String data = responseEntity.getLayout();
        Gson gson = new Gson();
        EventSeatLayoutResonse seatLayoutResponse = gson.fromJson(data, EventSeatLayoutResonse.class);
        return seatLayoutResponse;
    }


    private SeatLayoutViewModel convertResponseToViewModel(EventSeatLayoutResonse response) {
        seatLayoutViewModel = new SeatLayoutViewModel();
        seatLayoutViewModel = SeatLayoutResponseToSeatLayoutViewModelMapper.map(response, seatLayoutViewModel);

        return seatLayoutViewModel;
    }

    public void setChildFragment(FragmentAddTickets childFragment) {
        mChildFragment = childFragment;
    }

    private void scrollToLastIfNeeded() {
        mChildFragment.setDecorationHeight(getView().getButtonLayoutHeight() + px);
        if (mSelectedPackage == schedulesList.get(mSelectedSchedule).getPackages().size() - 1)
            mChildFragment.scrollToLast();
    }

}
