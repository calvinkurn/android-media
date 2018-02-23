package com.tokopedia.events.view.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.network.NetworkErrorHelper;
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

    private PackageViewModel checkoutData;
    private PostVerifyCartUseCase postVerifyCartUseCase;
    private PostPaymentUseCase postPaymentUseCase;
    private ProfileUseCase profileUseCase;
    private ProfileModel profileModel;
    private String promocode;
    private String email;
    private String number;
    private boolean isPromoCodeCase;
    private SelectedSeatViewModel selectedSeatViewModel;
    private ArrayList<String> hints = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();
    private RequestParams paymentparams;

    @Inject
    public EventReviewTicketPresenter(PostVerifyCartUseCase usecase, PostPaymentUseCase payment, ProfileUseCase profileUseCase) {
        this.postVerifyCartUseCase = usecase;
        this.postPaymentUseCase = payment;
        this.profileUseCase = profileUseCase;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void proceedToPayment() {
        isPromoCodeCase = false;
        if (getView().validateAllFields()) {
            verifyCart();
        } else {
            getView().showMessage("Silahkan Isi Data Pelanggan Tambahan");
        }

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
    public boolean validateEditText(EditText view) {
        String regex = (String) view.getTag();
        int index = hints.indexOf(view.getHint().toString());
        if (view.getText() == null || view.getText().length() == 0 || !validateStringWithRegex(view.getText().toString(), regex)) {
            view.setError(errors.get(index));
            return false;
        } else {
            updateForm(view.getHint().toString(), view.getText().toString());
            return true;
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
        config.setPrice(packageViewModel.getSalesPrice() * checkoutData.getSelectedQuantity());
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

        if (packageViewModel.getForms() != null) {
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
        final RequestParams params = RequestParams.create();
        params.putObject("checkoutdata",convertPackageToCartItem(checkoutData));
        params.putBoolean("ispromocodecase",!isPromoCodeCase);
        postVerifyCartUseCase.execute(params, new Subscriber<VerifyCartResponse>() {
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

                if (!isPromoCodeCase) {
                    if ("failure".equals(verifyCartResponse.getStatus().getResult())) {
                        getView().hideProgressBar();
                        getView().showMessage("Silahkan Isi Data Pelanggan Tambahan");
                    } else {
                        paymentparams = RequestParams.create();
                        paymentparams.putObject("verfiedcart",verifyCartResponse.getCart());
                        getPaymentLink();
                    }
                } else {
                    String errorMsg = verifyCartResponse.getCart().getPromocodeFailureMessage();
                    if (errorMsg != null &&
                            errorMsg.length() > 0) {
                        getView().hideProgressBar();
                        getView().hideSuccessMessage();
                        getView().showPromoSuccessMessage(errorMsg,
                                getView().getActivity().getResources().getColor(R.color.red_a700));
                        promocode = "";
                    } else {
                        String successMsg = verifyCartResponse.getCart().getPromocodeSuccessMessage();
                        if (successMsg != null && successMsg.length() > 0) {
                            getView().hideProgressBar();
                            getView().showPromoSuccessMessage(getView().getActivity().getResources().getString(R.string.promo_success_msg),
                                    getView().getActivity().getResources().getColor(R.color.black_54));
                            getView().showCashbackMessage(successMsg);
                        }
                    }
                }
            }
        });
    }

    private void getPaymentLink() {
        postPaymentUseCase.execute(paymentparams, new Subscriber<CheckoutResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("PaymentLinkUseCase", "ON ERROR");
                throwable.printStackTrace();
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
