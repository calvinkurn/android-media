package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.data.entity.response.Form;
import com.tokopedia.events.data.entity.response.SeatLayoutItem;
import com.tokopedia.events.data.entity.response.seatlayoutresponse.EventSeatLayoutResonse;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.GetEventSeatLayoutUseCase;
import com.tokopedia.events.domain.model.request.cart.CartItem;
import com.tokopedia.events.domain.model.request.cart.CartItems;
import com.tokopedia.events.domain.model.request.cart.Configuration;
import com.tokopedia.events.domain.model.request.cart.EntityAddress;
import com.tokopedia.events.domain.model.request.cart.EntityPackageItem;
import com.tokopedia.events.domain.model.request.cart.EntityPassengerItem;
import com.tokopedia.events.domain.model.request.cart.MetaData;
import com.tokopedia.events.domain.model.request.cart.OtherChargesItem;
import com.tokopedia.events.domain.model.request.cart.TaxPerQuantityItem;
import com.tokopedia.events.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.contractor.SeatSelectionContract;
import com.tokopedia.events.view.customview.SeatLayoutInfo;
import com.tokopedia.events.view.mapper.SeatLayoutResponseToSeatLayoutViewModelMapper;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by naveengoyal on 1/16/18.
 */

public class SeatSelectionPresenter extends BaseDaggerPresenter<SeatSelectionContract.SeatSelectionView>
        implements SeatSelectionContract.Presenter {


    public static String EXTRA_PACKAGEVIEWMODEL = "packageviewmodel";
    public static String EXTRA_SEATSELECTEDMODEL = "selectedseatviewmodel";

    GetEventSeatLayoutUseCase getSeatLayoutUseCase;
    private SeatLayoutViewModel seatLayoutViewModel;
    PostVerifyCartUseCase postVerifyCartUseCase;
    String url;
    String title;
    SeatLayoutInfo seatLayoutInfo;
    PackageViewModel selectedpkgViewModel;
    String eventTitle;
    int maxTickets;
    ProfileUseCase profileUseCase;
    ProfileModel profileModel;
    String promocode;
    String email;
    String number;
    List<String> selectedSeats = new ArrayList<>();
    List<String> rowIds;
    SelectedSeatViewModel selectedSeatViewModel;
    int quantity;


    @Inject
    public SeatSelectionPresenter(GetEventSeatLayoutUseCase seatLayoutUseCase, PostVerifyCartUseCase postVerifyCartUseCase) {
        this.getSeatLayoutUseCase = seatLayoutUseCase;
        this.postVerifyCartUseCase = postVerifyCartUseCase;
    }

    public void initialize() {
//        getView().showProgressBar();
        GlobalCacheManager profileCache = new GlobalCacheManager();

        ProfileSourceFactory profileSourceFactory = new ProfileSourceFactory(
                getView().getActivity(),
                new PeopleService(),
                new ProfileMapper(),
                profileCache,
                new AnalyticsCacheHandler(),
                new SessionHandler(getView().getActivity())
        );

        ProfileRepository profileRepository = new ProfileRepositoryImpl(profileSourceFactory);

        profileUseCase = new ProfileUseCase(
                new JobExecutor(),
                new UIThread(),
                profileRepository
        );
    }


    public void getProfile() {
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
                intent.removeExtra(SessionView.MOVE_TO_CART_KEY);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.EVENTS_CART);
                getView().getActivity().startActivity(intent);
            }

            @Override
            public void onNext(ProfileModel model) {
                profileModel = model;
                email = profileModel.getProfileData().getUserInfo().getUserEmail();
                number = profileModel.getProfileData().getUserInfo().getUserPhone();
            }
        });
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void attachView(SeatSelectionContract.SeatSelectionView view) {
        super.attachView(view);
        selectedpkgViewModel = getView().getActivity().getIntent().getParcelableExtra(EventBookTicketPresenter.EXTRA_PACKAGEVIEWMODEL);
        seatLayoutViewModel = getView().getActivity().getIntent().getParcelableExtra(EventBookTicketPresenter.EXTRA_SEATLAYOUTVIEWMODEL);
        eventTitle = getView().getActivity().getIntent().getStringExtra("EventTitle");
        maxTickets = selectedpkgViewModel.getMaxQty();
        url = selectedpkgViewModel.getFetchSectionUrl();
//           url =      "https://booking-staging.tokopedia.com/v1/api/seat-layout/category/1/product/904/schedule/1487/group/10909/package/84622";
        getView().setEventTitle(eventTitle);
    }


    public void getSeatSelectionDetails() {
        getView().renderSeatSelection(selectedpkgViewModel.getSalesPrice(), maxTickets, seatLayoutViewModel);
    }

    @Override
    public void validateSelection() {

    }

    public void setTicketPrice(int numOfTickets) {
        quantity = numOfTickets;
        getView().setTicketPrice(numOfTickets);

    }

    public void setSelectedSeatText(List<String> selectedSeatList, List<String> rowIds) {
        selectedSeats = selectedSeatList;
        this.rowIds = rowIds;
        getView().setSelectedSeatText(selectedSeatList, rowIds);
    }

    public void verifySeatSelection(final SelectedSeatViewModel selectedSeatViewModel) {
        this.selectedSeatViewModel = selectedSeatViewModel;
        postVerifyCartUseCase.setCartItems(convertPackageToCartItem(selectedpkgViewModel), true);
        postVerifyCartUseCase.execute(RequestParams.EMPTY, new Subscriber<VerifyCartResponse>() {
            @Override
            public void onCompleted() {
                Intent reviewTicketIntent = new Intent(getView().getActivity(), ReviewTicketActivity.class);
                reviewTicketIntent.putExtra(EXTRA_PACKAGEVIEWMODEL, selectedpkgViewModel);
                reviewTicketIntent.putExtra(EXTRA_SEATSELECTEDMODEL, selectedSeatViewModel);
                getView().navigateToActivityRequest(reviewTicketIntent, 100);

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("Naveen", " On Error" + throwable.getMessage());

            }

            @Override
            public void onNext(VerifyCartResponse verifyCartResponse) {
                Log.d("Naveen", "on Next" + verifyCartResponse.getStatus().getMessage().getMessage());
            }
        });
    }


    private CartItems convertPackageToCartItem(PackageViewModel packageViewModel) {
        Configuration config = new Configuration();
        config.setPrice(packageViewModel.getSalesPrice());
        com.tokopedia.events.domain.model.request.cart.SubConfig sub = new com.tokopedia.events.domain.model.request.cart.SubConfig();
        sub.setName(profileModel.getProfileData().getUserInfo().getUserName());
        config.setSubConfig(sub);
        MetaData meta = new MetaData();
        meta.setEntityCategoryId(packageViewModel.getCategoryId());
        meta.setEntityCategoryName("");
        meta.setEntityGroupId(packageViewModel.getProductGroupId());
        List<EntityPackageItem> entityPackages = new ArrayList<>();
        EntityPackageItem packageItem = new EntityPackageItem();
        packageItem.setPackageId(packageViewModel.getId());
        packageItem.setAreaCode(selectedSeatViewModel.getAreaCodes());
        packageItem.setDescription("");
        packageItem.setQuantity(quantity);
        packageItem.setPricePerSeat(selectedSeatViewModel.getPrice());
        packageItem.setSeatId(selectedSeatViewModel.getSeatIds());
        packageItem.setSeatRowId(selectedSeatViewModel.getSeatRowIds());
        packageItem.setSeatPhysicalRowId(selectedSeatViewModel.getPhysicalRowIds());
        packageItem.setSessionId("");
        packageItem.setProductId(packageViewModel.getProductId());
        packageItem.setGroupId(packageViewModel.getProductGroupId());
        packageItem.setScheduleId(packageViewModel.getProductScheduleId());
        entityPackages.add(packageItem);
        meta.setEntityPackages(entityPackages);
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setEntityScheduleId(packageViewModel.getProductScheduleId());
        if (packageViewModel.getForms() != null) {
            List<EntityPassengerItem> passengerItems = new ArrayList<>();

            for (Form form : packageViewModel.getForms()) {
                EntityPassengerItem passenger = new EntityPassengerItem();
                passenger.setId(form.getId());
                passenger.setProductId(form.getProductId());
                passenger.setName(form.getName());
                passenger.setTitle(form.getTitle());
                passenger.setValue(form.getValue());
                passenger.setElementType(form.getElementType());
                passenger.setRequired(String.valueOf(form.getRequired()));
                passenger.setValidatorRegex(form.getValidatorRegex());
                passenger.setErrorMessage(form.getErrorMessage());
                passengerItems.add(passenger);
            }

            meta.setEntityPassengers(passengerItems);
        }
        EntityAddress address = new EntityAddress();
        address.setAddress("");
        address.setName("");
        address.setCity("");
        address.setEmail(this.email);
        address.setMobileNumber(this.number);
        address.setLatitude("");
        address.setLongitude("");
        meta.setEntityAddress(address);
        meta.setCitySearched("");
        meta.setEntityEndTime("");
        meta.setEntityStartTime("");
        meta.setTotalTaxAmount(0);
        meta.setTotalOtherCharges(0);
        meta.setTotalTicketPrice(packageViewModel.getSelectedQuantity() * packageViewModel.getSalesPrice());
        meta.setEntityImage("");
        List<OtherChargesItem> otherChargesItems = new ArrayList<>();
        OtherChargesItem otherCharges = new OtherChargesItem();
        otherCharges.setConvFee(packageViewModel.getConvenienceFee());
        otherChargesItems.add(otherCharges);
        meta.setOtherCharges(otherChargesItems);
        List<TaxPerQuantityItem> taxPerQuantityItems = new ArrayList<>();
        meta.setTaxPerQuantity(taxPerQuantityItems);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setMetaData(meta);
        cartItem.setConfiguration(config);
        cartItem.setQuantity(packageViewModel.getSelectedQuantity());
        cartItem.setProductId(packageViewModel.getProductId());


        cartItems.add(cartItem);
        CartItems cart = new CartItems();
        cart.setCartItems(cartItems);
        cart.setPromocode(promocode);

//todo tax per quantity
        return cart;
    }

    public void setSelectedSeatViewModel() {
        getView().setSelectedSeatModel();
    }
}
