package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.analytics.handler.AnalyticsCacheHandler;
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
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
import com.tokopedia.events.data.entity.response.verifyresponse.Cart;
import com.tokopedia.events.data.entity.response.verifyresponse.VerifyCartResponse;
import com.tokopedia.events.domain.model.request.cart.CartItem;
import com.tokopedia.events.domain.model.request.cart.CartItems;
import com.tokopedia.events.domain.model.request.cart.Configuration;
import com.tokopedia.events.domain.model.request.cart.EntityAddress;
import com.tokopedia.events.domain.model.request.cart.EntityPackageItem;
import com.tokopedia.events.domain.model.request.cart.EntityPassengerItem;
import com.tokopedia.events.domain.model.request.cart.MetaData;
import com.tokopedia.events.domain.model.request.cart.OtherChargesItem;
import com.tokopedia.events.domain.model.request.cart.TaxPerQuantityItem;
import com.tokopedia.events.domain.postusecase.PostPaymentUseCase;
import com.tokopedia.events.domain.postusecase.PostVerifyCartUseCase;
import com.tokopedia.events.view.activity.TopPayActivity;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.PaymentPassData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 29/11/17.
 */

public class EventReviewTicketPresenter
        extends BaseDaggerPresenter<EventReviewTicketsContractor.EventReviewTicketsView>
        implements EventReviewTicketsContractor.Presenter {

    PackageViewModel checkoutData;
    PostVerifyCartUseCase postVerifyCartUseCase;
    PostPaymentUseCase postPaymentUseCase;
    ProfileUseCase profileUseCase;
    ProfileModel profileModel;

    @Inject
    public EventReviewTicketPresenter(PostVerifyCartUseCase usecase, PostPaymentUseCase payment) {
        this.postVerifyCartUseCase = usecase;
        this.postPaymentUseCase = payment;
    }

    @Override
    public void initialize() {
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

    @Override
    public void onDestroy() {

    }

    @Override
    public void proceedToPayment() {
        profileUseCase.execute(RequestParams.EMPTY, new Subscriber<ProfileModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(ProfileModel model) {
                profileModel = model;
                postVerifyCartUseCase.setCartItems(convertPackageToCartItem(checkoutData));
                verifyCart();

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
        packageItem.setAreaCode(new ArrayList<Object>());
        packageItem.setDescription("");
        packageItem.setQuantity(packageViewModel.getSelectedQuantity());
        packageItem.setPricePerSeat(packageViewModel.getSalesPrice());
        packageItem.setSeatId(new ArrayList<Object>());
        packageItem.setSeatRowId(new ArrayList<Object>());
        packageItem.setSessionId("");
        packageItem.setProductId(packageViewModel.getProductId());
        packageItem.setGroupId(packageViewModel.getProductGroupId());
        packageItem.setScheduleId(packageViewModel.getProductScheduleId());
        entityPackages.add(packageItem);
        meta.setEntityPackages(entityPackages);
        meta.setTotalTicketCount(packageViewModel.getSelectedQuantity());
        meta.setEntityProductId(packageViewModel.getProductId());
        meta.setEntityScheduleId(packageViewModel.getProductScheduleId());
        List<EntityPassengerItem> passengerItems = new ArrayList<>();
        EntityPassengerItem passenger = new EntityPassengerItem();
        passenger.setId(packageViewModel.getForm().getId());
        passenger.setProductId(packageViewModel.getForm().getProductId());
        passenger.setName(packageViewModel.getForm().getName());
        passenger.setTitle(packageViewModel.getForm().getTitle());
        passenger.setValue(packageViewModel.getForm().getValue());
        passenger.setElementType(packageViewModel.getForm().getElementType());
        passenger.setRequired(String.valueOf(packageViewModel.getForm().getRequired()));
        passenger.setValidatorRegex(packageViewModel.getForm().getValidatorRegex());
        passenger.setErrorMessage(packageViewModel.getForm().getErrorMessage());
        passengerItems.add(passenger);
        meta.setEntityPassengers(passengerItems);
        EntityAddress address = new EntityAddress();
        address.setAddress("");
        address.setName("");
        address.setCity("");
        address.setEmail(profileModel.getProfileData().getUserInfo().getUserEmail());
        address.setMobileNumber(profileModel.getProfileData().getUserInfo().getUserPhone());
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
        cart.setPromocode("");
        Log.d("Session Handler", "AccessToken " + SessionHandler.getAccessToken());
        Log.d("Session Handler", "GTM Login ID " + SessionHandler.getGTMLoginID(getView().getActivity()));
        Log.d("Session Handler", "Login ID " + SessionHandler.getLoginID(getView().getActivity()));
        Log.d("Session Handler", "Login Name " + SessionHandler.getLoginName(getView().getActivity()));
        Log.d("Session Handler", "Phone Number " + SessionHandler.getPhoneNumber());
        Log.d("Session Handler", "Temp Login Name " + SessionHandler.getTempLoginName(getView().getActivity()));
        Log.d("Session Handler", "UUID " + SessionHandler.getUUID(getView().getActivity()));
        Log.d("Session Handler", "Refresh Token " + SessionHandler.getRefreshToken(getView().getActivity()));
        Log.d("Session Handler", "Temp Login Session " + SessionHandler.getTempLoginSession(getView().getActivity()));
        Log.d("Session Handler", "Shop ID " + SessionHandler.getShopID(getView().getActivity()));
        Log.d("Session Handler", "" + SessionHandler.getShopDomain(getView().getActivity()));

//todo tax per quantity
        return cart;
    }

    @Override
    public void attachView(EventReviewTicketsContractor.EventReviewTicketsView view) {
        super.attachView(view);
        Intent intent = view.getActivity().getIntent();
        this.checkoutData = intent.getParcelableExtra(EventBookTicketPresenter.EXTRA_PACKAGEVIEWMODEL);
        getView().renderFromPackageVM(checkoutData);
    }

    public void verifyCart() {

        postVerifyCartUseCase.execute(RequestParams.EMPTY, new Subscriber<VerifyCartResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("ReviewTicketPresenter", "onError");
                throwable.printStackTrace();
            }

            @Override
            public void onNext(VerifyCartResponse verifyCartResponse) {
                Log.d("ReviewTicketPresenter", verifyCartResponse.toString());
                postPaymentUseCase.setVerfiedCart(verifyCartResponse.getCart());
                getPaymentLink();
            }
        });
    }

    public void getPaymentLink() {
        postPaymentUseCase.execute(RequestParams.EMPTY, new Subscriber<CheckoutResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(CheckoutResponse checkoutResponse) {
//                com.tokopedia.payment.model.PaymentPassData paymentPassData = new com.tokopedia.payment.model.PaymentPassData();
//
//                paymentPassData.queryString = checkoutResponse.getQueryString();
//                paymentPassData.redirectUrl = checkoutResponse.getRedirectUrl();
//                paymentPassData.callbackSuccessUrl = checkoutResponse.getCallbackUrlSuccess();
//                paymentPassData.transactionId = checkoutResponse.getParameter().getTransactionId();
//                paymentPassData.callbackFailedUrl = checkoutResponse.getCallbackUrlFailed();
//                getView().navigateToActivityRequest(com.tokopedia.payment.activity.TopPayActivity.
//                                createInstance(getView().getActivity(), paymentPassData),
//                        com.tokopedia.payment.activity.TopPayActivity.REQUEST_CODE);

                PaymentPassData paymentPassData = new PaymentPassData();
                paymentPassData.convertToPaymenPassData(checkoutResponse);
                getView().navigateToActivityRequest(TopPayActivity.
                        createInstance(getView().getActivity(),paymentPassData),TopPayActivity.REQUEST_CODE);

            }
        });
    }


}
