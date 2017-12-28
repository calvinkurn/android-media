package com.tokopedia.seller.shop.open.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.router.OnActivityResultListener;
import com.tokopedia.core.router.logistic.LogisticRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.model.ShopOpenStepperModel;
 import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.view.holder.LocationHeaderViewHolder;
import com.tokopedia.seller.shop.open.view.holder.LocationMapViewHolder;
import com.tokopedia.seller.shop.open.view.holder.LocationShippingViewHolder;
import com.tokopedia.seller.shop.open.view.model.DestinationViewModel;
import com.tokopedia.seller.shop.open.view.model.GoogleLocationViewModel;
import com.tokopedia.seller.shop.open.view.model.LocationViewModel;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.setting.data.model.response.Shipment;
import com.tokopedia.seller.shop.setting.data.model.response.UserData;
import com.tokopedia.seller.shop.setting.domain.interactor.ShopOpenSaveLocationUseCase;
import com.tokopedia.seller.shopsettings.shipping.customview.ShippingHeaderLayout;

import javax.inject.Inject;

import rx.Subscriber;


/**
 * @author normansyahputa
 */
public class ShopOpenMandatoryLocationFragment extends BaseDaggerFragment {

    public static final int REQUEST_CODE_ADDRESS = 1234;
    public static final int REQUEST_CODE__EDIT_ADDRESS = 1235;
    public static final int REQUEST_CODE_GOOGLE_MAP = 1236;

    protected ShopOpenStepperModel stepperModel;
    protected StepperListener<ShopOpenStepperModel> stepperListener;
    private LogisticRouter logisticRouter;
    private static final String TAG = "ShopOpenMandatoryLocati";
    private LocationShippingViewHolder locationShippingViewHolder;
    private LocationMapViewHolder locationMapViewHolder;

    @Inject
    ShopOpenSaveLocationUseCase shopOpenSaveLocationUseCase;

    public static ShopOpenMandatoryLocationFragment getInstance(){
        return new ShopOpenMandatoryLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_open_location, container, false);
        initView(root);
        return root;
    }

    private void initView(View root){
        LocationHeaderViewHolder locationHeaderViewHolder = new LocationHeaderViewHolder(root, new LocationHeaderViewHolder.ViewHolderListener() {
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
                if(logisticRouter == null)
                    return;

                logisticRouter.navigateToEditAddressActivityRequest(
                        ShopOpenMandatoryLocationFragment.this,
                        REQUEST_CODE__EDIT_ADDRESS
                );
            }
        });

        locationMapViewHolder = new LocationMapViewHolder(root, new LocationMapViewHolder.ViewHolderListener3() {
            @Override
            public void navigateToGeoLocationActivityRequest(String generatedMap) {
                if(logisticRouter == null)
                    return;

                logisticRouter.navigateToGeoLocationActivityRequest(
                        ShopOpenMandatoryLocationFragment.this,
                        REQUEST_CODE_GOOGLE_MAP,
                        generatedMap
                );
            }
        });

        root.findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleLocationViewModel googleLocationViewModel = locationMapViewHolder.getGoogleLocationViewModel();

                RequestParams requestParams = ShopOpenSaveLocationUseCase.createRequestParams(
                        googleLocationViewModel == null ? "" : googleLocationViewModel.getLongitude(),
                        googleLocationViewModel == null ? "" : googleLocationViewModel.getLatitude(),
                        "",
                        locationShippingViewHolder.getLocationComplete(),
                        locationShippingViewHolder.getDistrictName(),
                        locationMapViewHolder.getManualAddress(),
                        locationShippingViewHolder.getPostalCode(),
                        locationShippingViewHolder.getDistrictId()
                );

                shopOpenSaveLocationUseCase.execute(requestParams, new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "berhasilkah ? -> "+e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.d(TAG, "berhasilkah ? -> "+aBoolean);

                        if(aBoolean != null || aBoolean){
                            updateStepperModel();

                            if(stepperListener != null) {
                                stepperListener.goToNextPage(null);
                            }
                        }

                    }
                });
            }
        });

        if(stepperListener.getStepperModel()!=null){
            Shipment shipment = stepperListener.getStepperModel().getResponseIsReserveDomain().getShipment();
            UserData userData = stepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();

            locationShippingViewHolder.updateDistrictId(Integer.toString(shipment.getDistrictId()));
            locationShippingViewHolder.updateZipCodes(Integer.toString(shipment.getPostal()));
            locationShippingViewHolder.updateLocationData(userData.getLocComplete(), userData.getLocation());

            GoogleLocationViewModel googleLocationViewModel
                    = new GoogleLocationViewModel();
            googleLocationViewModel.setGeneratedAddress(shipment.getAddrStreet());
            googleLocationViewModel.setManualAddress(shipment.getAddrStreet());
            googleLocationViewModel.setLongitude(shipment.getLongitude());
            googleLocationViewModel.setLatitude(shipment.getLatitude());
            googleLocationViewModel.setCheckSum(shipment.getGeolocationChecksum());

            locationMapViewHolder.setLocationText(googleLocationViewModel);
        }
    }

    private void updateStepperModel(){
        if(stepperListener.getStepperModel() != null){
            GoogleLocationViewModel googleLocationViewModel = locationMapViewHolder.getGoogleLocationViewModel();

            ResponseIsReserveDomain responseIsReserveDomain = stepperListener.getStepperModel().getResponseIsReserveDomain();

            Shipment shipment = responseIsReserveDomain.getShipment();
            shipment.setAddrStreet(googleLocationViewModel.getGeneratedAddress());
            shipment.setLongitude(googleLocationViewModel.getLongitude());
            shipment.setLatitude(googleLocationViewModel.getLatitude());
            shipment.setGeolocationChecksum(googleLocationViewModel.getCheckSum());
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

    private void logShipment(Shipment shipment){
        String addrStreet = shipment.getAddrStreet(); // manual address
        String longitude = shipment.getLongitude();
        String latitude = shipment.getLatitude();
        String geolocationChecksum = shipment.getGeolocationChecksum();

        int postal = shipment.getPostal();
        int districtId = shipment.getDistrictId();
        Log.d(TAG, String.format(
                "%s %s %s %d %d %s", addrStreet, longitude, latitude, postal, districtId, geolocationChecksum
        ));
    }

    private void logUserData(UserData userData){
        // location complete district
        String location = userData.getLocation();
        String locComplete = userData.getLocComplete();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null){
            switch (requestCode){
                case REQUEST_CODE_ADDRESS:
                case REQUEST_CODE__EDIT_ADDRESS:
                case REQUEST_CODE_GOOGLE_MAP:
                    logisticRouter.onActivityResultChooseAddress(requestCode, data, new OnActivityResultListener() {
                        @Override
                        public void onActivityResult(Object rawData) {
                            Log.d(TAG, rawData.toString());
                            if(rawData instanceof DestinationViewModel){
                                DestinationViewModel address = (DestinationViewModel)rawData;
                                locationShippingViewHolder.updateDistrictId(address.getDistrictId()+"");
                                locationShippingViewHolder.updateLocationData(
                                        address.getProvinceName(),
                                        address.getCityName(),
                                        address.getDistrictName());
                            }else if(rawData instanceof LocationViewModel){
                                LocationViewModel locationViewModel = (LocationViewModel) rawData;
                                locationShippingViewHolder.initializeZipCodes(locationViewModel.getZipCodes());
                                locationShippingViewHolder.updateDistrictId(locationViewModel.getDistrictId()+"");
                                locationShippingViewHolder.updateLocationData(
                                        locationViewModel.getProvinceName(),
                                        locationViewModel.getCityName(),
                                        locationViewModel.getDistrictName()
                                );
                            }else if(rawData instanceof GoogleLocationViewModel){
                                GoogleLocationViewModel googleLocationViewModel = (GoogleLocationViewModel) rawData;
                                locationMapViewHolder.setLocationText(googleLocationViewModel);
                            }
                        }
                    });
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

        if(context.getApplicationContext() instanceof LogisticRouter){
            logisticRouter = (LogisticRouter)context.getApplicationContext();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null && getArguments() != null) {
            setupArguments(getArguments());

            Shipment shipment = stepperListener.getStepperModel().getResponseIsReserveDomain().getShipment();
            UserData userData = stepperListener.getStepperModel().getResponseIsReserveDomain().getUserData();
            logShipment(shipment);
            logUserData(userData);
        }
    }

    protected void setupArguments(Bundle arguments) {
        stepperModel = arguments.getParcelable(BaseStepperActivity.STEPPER_MODEL_EXTRA);
    }

    @Override
    protected void initInjector() {
        getComponent(ShopOpenDomainComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
