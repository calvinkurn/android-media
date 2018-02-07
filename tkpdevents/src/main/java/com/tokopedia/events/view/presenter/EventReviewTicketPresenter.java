package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

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
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.events.R;
import com.tokopedia.events.data.entity.response.Form;
import com.tokopedia.events.data.entity.response.checkoutreponse.CheckoutResponse;
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
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SelectedSeatViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.events.view.activity.ReviewTicketActivity.PAYMENT_REQUEST_CODE;

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
    String promocode;
    String email;
    String number;
    boolean isPromoCodeCase;
    SelectedSeatViewModel selectedSeatViewModel;
    ArrayList<String> hints = new ArrayList<>();
    ArrayList<String> errors = new ArrayList<>();

    @Inject
    public EventReviewTicketPresenter(PostVerifyCartUseCase usecase, PostPaymentUseCase payment) {
        this.postVerifyCartUseCase = usecase;
        this.postPaymentUseCase = payment;
    }

    @Override
    public void initialize() {
        getView().showProgressBar();
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
        isPromoCodeCase = false;
        verifyCart();
    }

    @Override
    public void updatePromoCode(String code) {
        this.promocode = code;
        if (code.length() > 0) {
            isPromoCodeCase = true;
            verifyCart();
        } else {
            getView().hideSuccessMessage();
        }
    }

    @Override
    public void validateEditText(EditText view) {
        String regex = (String) view.getTag();
        int index = hints.indexOf(view.getHint().toString());
        if (view.getText().length() > 0 && !validateStringWithRegex(view.getText().toString(), regex)) {
            view.setError(errors.get(index));
        } else {
            updateForm(view.getHint().toString(), view.getText().toString());
        }
    }

    @Override
    public void updateEmail(String mail) {
        this.email = mail;
    }

    @Override
    public void updateNumber(String umber) {
        this.number = umber;
    }

    @Override
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
                getView().hideProgressBar();
            }

            @Override
            public void onNext(ProfileModel model) {
                profileModel = model;
                email = profileModel.getProfileData().getUserInfo().getUserEmail();
                number = profileModel.getProfileData().getUserInfo().getUserPhone();
                getView().setEmailID(profileModel.getProfileData().getUserInfo().getUserEmail());
                getView().setPhoneNumber(number);
                getView().hideProgressBar();
            }
        });
    }

    @Override
    public void clickEmailIcon() {
        getView().showEmailTooltip();
    }

    @Override
    public void clickMoreinfoIcon() {
        getView().showMoreinfoTooltip();
    }

    @Override
    public void clickDismissTooltip() {
        getView().hideTooltip();
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
        if (selectedSeatViewModel != null) {
            packageItem.setAreaCode(selectedSeatViewModel.getAreaCodes());
            packageItem.setSeatId(selectedSeatViewModel.getSeatIds());
            packageItem.setSeatRowId(selectedSeatViewModel.getSeatRowIds());
            packageItem.setSeatPhysicalRowId(selectedSeatViewModel.getPhysicalRowIds());
            packageItem.setQuantity(selectedSeatViewModel.getQuantity());
            packageItem.setPricePerSeat(selectedSeatViewModel.getPrice());
        } else {
            packageItem.setAreaCode(new ArrayList<String>());
            packageItem.setSeatId(new ArrayList<String>());
            packageItem.setSeatRowId(new ArrayList<String>());
            packageItem.setSeatPhysicalRowId(new ArrayList<String>());
            packageItem.setQuantity(packageViewModel.getSelectedQuantity());
            packageItem.setPricePerSeat(packageViewModel.getSalesPrice());
        }
        packageItem.setDescription("");

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

    @Override
    public void attachView(EventReviewTicketsContractor.EventReviewTicketsView view) {
        super.attachView(view);
        getView().showProgressBar();
        Intent intent = view.getActivity().getIntent();
        this.checkoutData = intent.getParcelableExtra(EventBookTicketPresenter.EXTRA_PACKAGEVIEWMODEL);
        this.selectedSeatViewModel = intent.getParcelableExtra(SeatSelectionPresenter.EXTRA_SEATSELECTEDMODEL);
        getView().renderFromPackageVM(checkoutData, selectedSeatViewModel);
        getAndInitForms();
    }

    public void verifyCart() {
        getView().showProgressBar();
        postVerifyCartUseCase.setCartItems(convertPackageToCartItem(checkoutData), false);
        postVerifyCartUseCase.execute(RequestParams.EMPTY, new Subscriber<VerifyCartResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("ReviewTicketPresenter", "onError");
                throwable.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                verifyCart();
                            }
                        });
            }

            @Override
            public void onNext(VerifyCartResponse verifyCartResponse) {
                Log.d("ReviewTicketPresenter", verifyCartResponse.toString());

                if ("Kode Promo tidak ditemukan".equals(verifyCartResponse.getCart().getPromocodeFailureMessage())) {
                    getView().hideProgressBar();
                    getView().showPromoSuccessMessage("Kode Promo tidak ditemukan",
                            getView().getActivity().getResources().getColor(R.color.red_a700));
                } else {
                    getView().hideProgressBar();
                    getView().showPromoSuccessMessage(getView().getActivity().getResources().getString(R.string.promo_success_msg),
                            getView().getActivity().getResources().getColor(R.color.black_54));
                    String cashBackDiscount = "Total Discount : "
                            + verifyCartResponse.getCart().getPromocodeDiscount()
                            + " and Total Cashback : " + verifyCartResponse.getCart().getPromocodeCashback();
                    getView().showCashbackMessage(cashBackDiscount);
                }

                if (!isPromoCodeCase) {
                    if ("failure".equals(verifyCartResponse.getStatus().getResult())) {
                        getView().hideProgressBar();
                        getView().showMessage("Silahkan Isi Data Pelanggan Tambahan");
                    } else {
                        postPaymentUseCase.setVerfiedCart(verifyCartResponse.getCart());
                        getPaymentLink();
                    }
                }
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
                Log.d("PaymentLinkUseCase", "ON ERROR");
                throwable.printStackTrace();
//                getView().hideProgressBar();
//                getView().showMessage("Kode Promo tidak ditemukan");
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(),
                        getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                getPaymentLink();
                            }
                        });
            }

            @Override
            public void onNext(CheckoutResponse checkoutResponse) {

                com.tokopedia.payment.model.PaymentPassData paymentPassData = new com.tokopedia.payment.model.PaymentPassData();
                paymentPassData.setQueryString(checkoutResponse.getQueryString());
                paymentPassData.setRedirectUrl(checkoutResponse.getRedirectUrl());
                paymentPassData.setCallbackSuccessUrl(checkoutResponse.getCallbackUrlSuccess());
                paymentPassData.setCallbackFailedUrl(checkoutResponse.getCallbackUrlFailed());
                paymentPassData.setTransactionId(checkoutResponse.getParameter().getTransactionId());
                getView().navigateToActivityRequest(com.tokopedia.payment.activity.TopPayActivity.
                                createInstance(getView().getActivity().getApplicationContext(), paymentPassData),
                        PAYMENT_REQUEST_CODE);
//
//                com.tokopedia.payment.model.PaymentPassData paymentPassData = new com.tokopedia.payment.model.PaymentPassData();
//                paymentPassData.queryString =
//                        "amount=1000000&currency=IDR&customer_email=tkpd.qc%2B47%40gmail.com&customer_name=Cincin+Seller&fee=&items%5Bname%5D=cita+citata+Concert++-+dummy+x+1&items%5Bprice%5D=1000000&items%5Bquantity%5D=1&language=id-ID&merchant_code=tokopediapulsa&nid=&profile_code=PULSA_EVENT&signature=9c6dc817243d807e2a728fad43f3116420f5f10e&state=0&transaction_code=&transaction_date=2017-12-28T07%3A41%3A55Z&transaction_id=1000038823&user_defined_value=%7B%22user_id%22%3A3045010%2C%22voucher_code%22%3A%22%22%2C%22device%22%3A1%2C%22product_id%22%3A614%2C%22price%22%3A1000000%2C%22client_number%22%3A%22%22%2C%22payment_description%22%3A%22%22%2C%22promo_code_id%22%3A0%2C%22discount_amount%22%3A0%2C%22cashback_amount%22%3A0%2C%22cashback_voucher_amount%22%3A0%2C%22cashback_top_cash_amount%22%3A0%2C%22va_code%22%3A%22085729640914%22%2C%22refresh_token%22%3A%22GfDjW5HpRuyqLHR8zVRHFg%22%2C%22access_token%22%3A%22AXXJ-JvwTuuarYhrVurgqA%22%2C%22fingerprint_id%22%3A%22%22%2C%22hide_header_flag%22%3Afalse%2C%22category_code%22%3A%2223%22%2C%22product_code%22%3A%22TICKETEVENT%22%7D";
//                paymentPassData.redirectUrl = "https://pay-staging.tokopedia.com/v2/payment";
//                paymentPassData.callbackSuccessUrl = "https://pulsa-staging.tokopedia.com/checkout/back-to-app";
//                paymentPassData.transactionId = "1000038823";
//                paymentPassData.callbackFailedUrl = "https://pulsa-staging.tokopedia.com/checkout";
//                getView().navigateToActivityRequest(com.tokopedia.payment.activity.TopPayActivity.
//                                createInstance(getView().getActivity(), paymentPassData),
//                        com.tokopedia.payment.activity.TopPayActivity.REQUEST_CODE);

//                PaymentPassData paymentPassData = new PaymentPassData();
//                paymentPassData.convertToPaymenPassData(checkoutResponse);
//                getView().navigateToActivityRequest(TopPayActivity.
//                        createInstance(getView().getActivity(), paymentPassData), TopPayActivity.REQUEST_CODE);
                getView().hideProgressBar();

            }
        });
    }

    private void getAndInitForms() {
        List<Form> forms = checkoutData.getForms();
        if (forms == null)
            return;
        ArrayList<String> regex = new ArrayList<>();
        for (Form form : forms) {
            hints.add(form.getTitle());
            regex.add(form.getValidatorRegex());
            errors.add(form.getErrorMessage());
        }
        String[] t = new String[1];
        String[] u = new String[1];
        String[] hint = hints.toArray(t);
        String[] validatorRegex = regex.toArray(u);
        getView().initForms(hint, validatorRegex);
    }


    private boolean validateStringWithRegex(String field, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }

    private void updateForm(String key, String value) {
        List<Form> forms = checkoutData.getForms();
        for (Form form : forms) {
            if (form.getTitle().equals(key))
                form.setValue(value);
        }
    }


}
