package com.tokopedia.seller.shop.open.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationContract;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.seller.LogisticRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.common.exception.TomeException;
import com.tokopedia.seller.shop.open.analytic.ShopOpenTracking;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.util.ShopErrorHandler;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
import com.tokopedia.seller.shop.open.view.holder.LocationHeaderViewHolder;
import com.tokopedia.seller.shop.open.view.holder.LocationMapViewHolder;
import com.tokopedia.seller.shop.open.view.holder.LocationShippingViewHolder;
import com.tokopedia.seller.shop.open.view.model.DestinationViewModel;
import com.tokopedia.seller.shop.open.view.model.GoogleLocationViewModel;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.Shipment;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.UserData;
import com.tokopedia.seller.shop.open.domain.interactor.ShopOpenSaveLocationUseCase;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenLocPresenterImpl;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenLocView;

import java.util.HashMap;

import javax.inject.Inject;


/**
 * @author normansyahputa
 */
public class ShopOpenMandatoryLocationFragment extends BaseDaggerFragment implements ShopOpenLocView {

    public static final int REQUEST_CODE_ADDRESS = 1234;
    public static final int REQUEST_CODE__EDIT_ADDRESS = 1235;
    public static final int REQUEST_CODE_GOOGLE_MAP = 1236;
    public static final String CONST_PINPOINT = "pinpoint";

    protected ShopOpenStepperModel stepperModel;
    protected StepperListener<ShopOpenStepperModel> stepperListener;
    private LogisticRouter logisticRouter;
    private static final String TAG = "ShopOpenMandatoryLocati";
    private LocationShippingViewHolder locationShippingViewHolder;
    private LocationMapViewHolder locationMapViewHolder;

    @Inject
    ShopOpenLocPresenterImpl shopOpenLocPresenter;

    @Inject
    ShopOpenTracking trackingOpenShop;

    RequestParams requestParams;
    private TkpdProgressDialog tkpdProgressDialog;

    public static ShopOpenMandatoryLocationFragment getInstance() {
        return new ShopOpenMandatoryLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_open_location, container, false);
        requestParams = RequestParams.create();
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(getActivity()));
        initView(root);
        return root;
    }

    @Override
    public void dismissProgressDialog() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog.dismiss();
        }
    }

    @Override
    public void showProgressDialog() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS,
                    getString(R.string.title_loading));
        }
        tkpdProgressDialog.showDialog();
    }

    private void initView(View root) {

        new LocationHeaderViewHolder(root, new LocationHeaderViewHolder.ViewHolderListener() {
            @Override
            public void navigateToChooseAddressActivityRequest() {
                if (logisticRouter == null)
                    return;

                logisticRouter.navigateToChooseAddressActivityRequest(
                        ShopOpenMandatoryLocationFragment.this,
                        null,
                        REQUEST_CODE_ADDRESS
                );
            }
        });
        locationShippingViewHolder = new LocationShippingViewHolder(root, new LocationShippingViewHolder.ViewHolderListener2() {
            @Override
            public void navigateToEditAddressActivityRequest() {
                if (logisticRouter == null)
                    return;

                shopOpenLocPresenter.openDistrictRecommendation(requestParams);

            }
        });

        locationMapViewHolder = new LocationMapViewHolder(root, new LocationMapViewHolder.ViewHolderListener() {
            @Override
            public void navigateToGeoLocationActivityRequest(final String generatedMap) {
                if (logisticRouter == null)
                    return;

                shopOpenLocPresenter.openGoogleMap(requestParams, generatedMap);
            }

            @Override
            public void onPinPointSelected() {
                trackingOpenShop.eventOpenShopPinPointSelected();
            }

            @Override
            public void onPinPointDeleted() {
                trackingOpenShop.eventOpenShopPinPointDeleted();
            }
        });

        root.findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNextButtonClicked();
            }
        });
      
        ShopOpenStepperModel stepperModel = stepperListener.getStepperModel();
        if (stepperModel != null) {
            ResponseIsReserveDomain responseIsReserveDomain = stepperModel.getResponseIsReserveDomain();
            if (responseIsReserveDomain!= null){
                Shipment shipment = responseIsReserveDomain.getShipment();
                UserData userData = responseIsReserveDomain.getUserData();
                locationShippingViewHolder.updateLocationData(userData.getLocComplete(), userData.getLocation());

                if (shipment != null) {
                    locationShippingViewHolder.updateDistrictId(Integer.toString(shipment.getDistrictId()));
                  
                  if(shipment.getPostal() != 0)
                    locationShippingViewHolder.updateZipCodes(Integer.toString(shipment.getPostal()));

                    GoogleLocationViewModel googleLocationViewModel
                            = new GoogleLocationViewModel();
                    googleLocationViewModel.setGeneratedAddress(shipment.getAddrStreet());
                    googleLocationViewModel.setManualAddress(shipment.getAddrStreet());
                    googleLocationViewModel.setLongitude(shipment.getLongitude());
                    googleLocationViewModel.setLatitude(shipment.getLatitude());
                    googleLocationViewModel.setCheckSum(shipment.getGeolocationChecksum());


                    locationMapViewHolder.setFromReserveDomain(true);

                    locationMapViewHolder.setLocationText(googleLocationViewModel);
                }
            }
        }
    }

    protected void onNextButtonClicked() {
        GoogleLocationViewModel googleLocationViewModel = locationMapViewHolder.getGoogleLocationViewModel();

        if (!locationShippingViewHolder.isDataInputValid()) {
            return;
        }
        if (!locationMapViewHolder.isDataInputValid()) {
            return;
        }
        RequestParams requestParams = ShopOpenSaveLocationUseCase.createRequestParams(
                googleLocationViewModel == null ? "" : googleLocationViewModel.getLongitude(),
                googleLocationViewModel == null ? "" : googleLocationViewModel.getLatitude(),
                googleLocationViewModel == null ? "" : googleLocationViewModel.getCheckSum(),
                locationShippingViewHolder.getLocationComplete(),
                locationShippingViewHolder.getDistrictName(),
                locationMapViewHolder.getManualAddress(),
                locationShippingViewHolder.getPostalCode(),
                locationShippingViewHolder.getDistrictId()
        );

        shopOpenLocPresenter.submitData(requestParams);
    }


    @Override
    public void navigateToDistrictRecommendation(Token token) {
        logisticRouter.navigateToEditAddressActivityRequest(
                ShopOpenMandatoryLocationFragment.this,
                REQUEST_CODE__EDIT_ADDRESS,
                token
        );
    }

    @Override
    public void navigateToGoogleMap(String generatedMap, LocationPass locationPass) {
        logisticRouter.navigateToGeoLocationActivityRequest(
                ShopOpenMandatoryLocationFragment.this,
                REQUEST_CODE_GOOGLE_MAP,
                generatedMap,
                locationPass
        );
    }

    @Override
    public void goToNextPage(Object object) {
        trackingOpenShop.eventOpenShopLocationNext();
        if (stepperListener != null) {
            stepperListener.goToNextPage(null);
        }
    }

    @Override
    public void updateStepperModel() {
        if (stepperListener.getStepperModel() != null) {
            GoogleLocationViewModel googleLocationViewModel = locationMapViewHolder.getGoogleLocationViewModel();

            ResponseIsReserveDomain responseIsReserveDomain = stepperListener.getStepperModel().getResponseIsReserveDomain();

            Shipment shipment = responseIsReserveDomain.getShipment();
            if (shipment == null) {
                shipment = new Shipment();
            }
            if (googleLocationViewModel != null) {
                shipment.setAddrStreet(googleLocationViewModel.getGeneratedAddress());
                shipment.setLongitude(googleLocationViewModel.getLongitude());
                shipment.setLatitude(googleLocationViewModel.getLatitude());
                shipment.setGeolocationChecksum(googleLocationViewModel.getCheckSum());
            }
            shipment.setDistrictId(Integer.valueOf(locationShippingViewHolder.getDistrictId()));
            shipment.setPostal(Integer.valueOf(locationShippingViewHolder.getPostalCode()));

            UserData userData = responseIsReserveDomain.getUserData();
            userData.setLocComplete(locationShippingViewHolder.getLocationComplete());
            userData.setLocation(locationShippingViewHolder.getDistrictName());

            responseIsReserveDomain.setShipment(shipment);
            responseIsReserveDomain.setUserData(userData);

            stepperListener.getStepperModel().setResponseIsReserveDomain(responseIsReserveDomain);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_ADDRESS:
                    if (data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS) != null) {
                        DestinationViewModel address = DestinationViewModel.convertFromBundle(
                                data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS)
                        );
                        locationShippingViewHolder.updateZipCodes(address.getPostalCode());
                        locationShippingViewHolder.updateDistrictId(address.getDistrictId() + "");
                        locationShippingViewHolder.updateLocationData(
                                address.getProvinceName(),
                                address.getCityName(),
                                address.getDistrictName());

                        trackingOpenShop.eventOpenShopLocationForm(address.getAddressDetail());
                    }
                    break;
                case REQUEST_CODE__EDIT_ADDRESS:
                    Address address = data.getParcelableExtra(DistrictRecommendationContract.Constant.INTENT_DATA_ADDRESS);
                    if (address != null) {
                        locationShippingViewHolder.initializeZipCodes(address.getZipCodes());
                        locationShippingViewHolder.updateDistrictId(address.getDistrictId() + "");
                        locationShippingViewHolder.updateLocationData(
                                address.getProvinceName(),
                                address.getCityName(),
                                address.getDistrictName()
                        );
                    }
                    break;
                case REQUEST_CODE_GOOGLE_MAP:
                    LocationPass locationPass = data.getParcelableExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION);
                    if (locationPass != null && locationPass.getLatitude() != null) {

                        GoogleLocationViewModel locationViewModel = new GoogleLocationViewModel();
                        locationViewModel.setGeneratedAddress(locationPass.getGeneratedAddress());
                        locationViewModel.setLatitude(locationPass.getLatitude());
                        locationViewModel.setLongitude(locationPass.getLongitude());

                        locationMapViewHolder.setLocationText(locationViewModel);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepperListener) {
            this.stepperListener = (StepperListener<ShopOpenStepperModel>) context;
        }

        if (context.getApplicationContext() instanceof LogisticRouter) {
            logisticRouter = (LogisticRouter) context.getApplicationContext();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null) {
            setupArguments(getArguments());
        }
    }

    protected void setupArguments(Bundle arguments) {
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void initInjector() {
        getComponent(ShopOpenDomainComponent.class).inject(this);
        shopOpenLocPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        locationMapViewHolder.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        locationMapViewHolder.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shopOpenLocPresenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onErrorGetReserveDomain(Throwable e) {
        NetworkErrorHelper.showSnackbar(getActivity(), ErrorHandler.getErrorMessage(e, getActivity()));
    }

    private void onErrorGetReserveDomain(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onFailedSaveInfoShop(Throwable t) {
		if (!GlobalConfig.DEBUG) {
        		Crashlytics.logException(t);
        	}
        String errorMessage;
        if (t instanceof TomeException) {
            errorMessage = t.getMessage();
        } else {
            errorMessage = ErrorHandler.getErrorMessage(t, getActivity());
        }

        // set error message
        if (errorMessage.split(",").length > 1) {
            errorMessage = errorMessage.split(",")[0];
        }
        sendErrorTracking(errorMessage);
        if (errorMessage.contains(CONST_PINPOINT)) {
            onErrorGetReserveDomain(errorMessage);
            return;
        }
        onErrorGetReserveDomain(errorMessage);
    }

    private void sendErrorTracking(String errorMessage) {
        trackingOpenShop.eventOpenShopLocationError(errorMessage);
        String generatedErrorMessage = ShopErrorHandler.getGeneratedErrorMessage(errorMessage.toCharArray(),
                locationShippingViewHolder.getLocationComplete(), locationShippingViewHolder.getPostalCode(),
                locationMapViewHolder.getGoogleLocationViewModel() != null ? locationMapViewHolder.getGoogleLocationViewModel().getManualAddress() : "",
                locationMapViewHolder.getManualAddress() != null ? locationMapViewHolder.getGoogleLocationViewModel().getManualAddress() : "");
        trackingOpenShop.eventOpenShopLocationErrorWithData(generatedErrorMessage);
    }
}
