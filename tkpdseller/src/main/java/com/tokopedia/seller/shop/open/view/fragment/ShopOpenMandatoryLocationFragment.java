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

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Address;
import com.tokopedia.core.manage.general.districtrecommendation.domain.model.Token;
import com.tokopedia.core.manage.general.districtrecommendation.view.DistrictRecommendationContract;
import com.tokopedia.core.manage.people.address.ManageAddressConstant;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.logistic.LogisticRouter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.activity.BaseStepperActivity;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.logistic.GetOpenShopLocationPassUseCase;
import com.tokopedia.seller.logistic.GetOpenShopTokenUseCase;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
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
import com.tokopedia.seller.shop.open.view.model.LocationViewModel;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenLocPresenterImpl;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenLocView;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.setting.data.model.response.Shipment;
import com.tokopedia.seller.shop.setting.data.model.response.UserData;
import com.tokopedia.seller.shop.setting.domain.interactor.ShopOpenSaveLocationUseCase;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Subscriber;


/**
 * @author normansyahputa
 */
public class ShopOpenMandatoryLocationFragment extends BaseDaggerFragment implements ShopOpenLocView {

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
    GetOpenShopTokenUseCase getOpenShopTokenUseCase;

    @Inject
    ShopOpenLocPresenterImpl shopOpenLocPresenter;

    RequestParams requestParams;

    public static ShopOpenMandatoryLocationFragment getInstance(){
        return new ShopOpenMandatoryLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop_open_location, container, false);
        requestParams = RequestParams.EMPTY;
        requestParams.putAll((HashMap<String, String>) AuthUtil.generateParams(getActivity()));
        initView(root);
        return root;
    }

    private void initView(View root){
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
                if(logisticRouter == null)
                    return;

                shopOpenLocPresenter.openDistrictRecommendation(requestParams);

            }
        });

        locationMapViewHolder = new LocationMapViewHolder(root, new LocationMapViewHolder.ViewHolderListener3() {
            @Override
            public void navigateToGeoLocationActivityRequest(final String generatedMap) {
                if(logisticRouter == null)
                    return;

                shopOpenLocPresenter.openGoogleMap(requestParams, generatedMap);
            }
        });

        root.findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleLocationViewModel googleLocationViewModel = locationMapViewHolder.getGoogleLocationViewModel();

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

            locationMapViewHolder.setFromReserveDomain(true);

            locationMapViewHolder.setLocationText(googleLocationViewModel);
        }
    }


    @Override
    public void navigateToDistrictRecommendation(Token token){
        logisticRouter.navigateToEditAddressActivityRequest(
                ShopOpenMandatoryLocationFragment.this,
                REQUEST_CODE__EDIT_ADDRESS,
                token
        );
    }

    @Override
    public void navigateToGoogleMap(String generatedMap, LocationPass locationPass){
        logisticRouter.navigateToGeoLocationActivityRequest(
                ShopOpenMandatoryLocationFragment.this,
                REQUEST_CODE_GOOGLE_MAP,
                generatedMap,
                locationPass
        );
    }

    @Override
    public void goToNextPage(Object object) {
        if(stepperListener != null) {
            stepperListener.goToNextPage(null);
        }
    }

    @Override
    public void updateStepperModel(){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null){
            switch (requestCode) {
                case REQUEST_CODE_ADDRESS:
                    if (data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS) != null) {
                        DestinationViewModel address = DestinationViewModel.convertFromBundle(
                                data.getParcelableExtra(ManageAddressConstant.EXTRA_ADDRESS)
                        );
                        locationShippingViewHolder.updateDistrictId(address.getDistrictId()+"");
                        locationShippingViewHolder.updateLocationData(
                                address.getProvinceName(),
                                address.getCityName(),
                                address.getDistrictName());
                    }
                    break;
                case REQUEST_CODE__EDIT_ADDRESS:
                    Address address = data.getParcelableExtra(DistrictRecommendationContract.Constant.INTENT_DATA_ADDRESS);
                    if(address != null){

                        locationShippingViewHolder.initializeZipCodes(address.getZipCodes());
                        locationShippingViewHolder.updateDistrictId(address.getDistrictId()+"");
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

        if(context.getApplicationContext() instanceof LogisticRouter){
            logisticRouter = (LogisticRouter)context.getApplicationContext();
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
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
