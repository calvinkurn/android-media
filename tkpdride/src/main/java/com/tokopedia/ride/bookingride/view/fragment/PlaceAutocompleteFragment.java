package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.analytics.RideGATracking;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.BookingRideComponent;
import com.tokopedia.ride.bookingride.di.DaggerBookingRideComponent;
import com.tokopedia.ride.bookingride.domain.GetPeopleAddressesUseCase;
import com.tokopedia.ride.bookingride.domain.model.Paging;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompleteContract;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompletePresenter;
import com.tokopedia.ride.bookingride.view.adapter.ItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.PlaceAutoCompleteAdapter;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.di.RideComponent;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

public class PlaceAutocompleteFragment extends BaseFragment implements PlaceAutoCompleteContract.View,
        GoogleApiClient.OnConnectionFailedListener,
        ItemClickListener {
    private static final String TAG = "addressautocomplete";
    private static final String SHOW_AUTO_DETECT_LOCATION = "SHOW_AUTO_DETECT_LOCATION";
    private static final String SHOW_SELECT_LOCATION_ON_MAP = "SHOW_SELECT_LOCATION_ON_MAP";
    private static final String SHOW_NEARBY_PLACES = "SHOW_NEARBY_PLACES";
    public static final int REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE = 101;


    @Inject
    PlaceAutoCompletePresenter mPresenter;

    private PlaceAutoCompleteAdapter mAdapter;
    private Paging paging;
    private boolean isMarketPlaceSource;

    @BindView(R2.id.cabs_autocomplete_edit_text)
    EditText mAutocompleteEditText;
    @BindView(R2.id.pb_loader)
    ProgressBar mLoaderCrossFrameLayout;
    @BindView(R2.id.cabs_autocomplete_back_icon)
    ImageView mBackIconImageView;
    @BindView(R2.id.crux_cabs_auto_detect_container)
    RelativeLayout mAutoDetectLocationRelativeLayout;
    @BindView(R2.id.set_location_on_map_container)
    RelativeLayout mSelectLocationOnMapRelativeLayout;
    @BindView(R2.id.cabs_autocomplete_home_box)
    RelativeLayout mAutoCompleteHomeRelativeLayout;
    @BindView(R2.id.cabs_autocomplete_work_box)
    RelativeLayout mAutoCompleteWorkRelativeLayout;
    @BindView(R2.id.cabs_autocomplete_recycler_view)
    RecyclerView mAutoCompleteRecylerView;
    @BindView(R2.id.iv_google_label)
    ImageView googleLabelImageView;
    @BindView(R2.id.iv_cross)
    ImageView clearFieldImageView;

    OnFragmentInteractionListener mOnFragmentInteractionListener;
    private boolean showAutodetectLocation;
    private boolean showSelectLocationOnMap;
    private boolean showNearbyPlaces;

    public interface OnFragmentInteractionListener {
        void onLocationSelected(PlacePassViewModel placeId);

        void onSelectLocationOnMapSelected();
    }

    public static Fragment newInstance(boolean showAutoDetectLocation, boolean showSelectLocationOnMap, boolean showNearbyPlaces) {
        PlaceAutocompleteFragment fragment = new PlaceAutocompleteFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(SHOW_AUTO_DETECT_LOCATION, showAutoDetectLocation);
        arguments.putBoolean(SHOW_SELECT_LOCATION_ON_MAP, showSelectLocationOnMap);
        arguments.putBoolean(SHOW_NEARBY_PLACES, showNearbyPlaces);
        fragment.setArguments(arguments);
        return fragment;
    }

    public PlaceAutocompleteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paging = new Paging();
        paging.setPage(1);

        showAutodetectLocation = getArguments().getBoolean(SHOW_AUTO_DETECT_LOCATION, true);
        showSelectLocationOnMap = getArguments().getBoolean(SHOW_SELECT_LOCATION_ON_MAP, false);
        showNearbyPlaces = getArguments().getBoolean(SHOW_NEARBY_PLACES, false);
    }

    @Override
    protected void initInjector() {
        RideComponent component = getComponent(RideComponent.class);
        BookingRideComponent bookingRideComponent = DaggerBookingRideComponent
                .builder()
                .rideComponent(component)
                .build();
        bookingRideComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.initialize();

        mAutoDetectLocationRelativeLayout.setVisibility(showAutodetectLocation ? View.VISIBLE : View.GONE);
        mSelectLocationOnMapRelativeLayout.setVisibility(mPresenter.isLocationPermissionGranted() && showSelectLocationOnMap ? View.VISIBLE : View.GONE);

        mAutocompleteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CommonUtils.dumper("af : " + mAutocompleteEditText.getText().toString());
                if (TextUtils.isEmpty(s.toString())) {
                    CommonUtils.dumper("Executed OTC M");
                    if (showNearbyPlaces) {
                        setActiveGooglePlaceSource();
                        mPresenter.showNearbyPlaces();
                    } else {
                        setActiveMarketplaceSource();
                        mPresenter.actionGetUserAddressesFromCache();
                    }
                } else {
                    setActiveGooglePlaceSource();
                    CommonUtils.dumper("Executed OTC G");
                    mPresenter.actionQueryPlacesByKeyword(String.valueOf(s.toString()));
                }
            }
        });

        PlaceAutoCompleteTypeFactory placeAutoCompleteTypeFactory = new PlaceAutoCompleteAdapterTypeFactory(this);
        mAdapter = new PlaceAutoCompleteAdapter(placeAutoCompleteTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAutoCompleteRecylerView.setLayoutManager(layoutManager);
        mAutoCompleteRecylerView.setHasFixedSize(true);
        mAutoCompleteRecylerView.setAdapter(mAdapter);
    }

    /**
     * This function handles location alert result, initiated from Activity class
     *
     * @param resultCode
     */
    public void handleLocationAlertResult(int resultCode) {
        mPresenter.handleEnableLocationDialogResult(resultCode);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_autocomplete;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(getView(), connectionResult.getErrorMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPlaceSelected(PlaceAutoCompeleteViewModel address) {
        CommonUtils.closeKeyboard(getActivity(), mAutocompleteEditText.getWindowToken());
        if (showAutodetectLocation) {
            RideGATracking.eventClickSourceRecentAddress(getScreenName(), address.getAddress());
        } else {
            RideGATracking.eventClickDestinationRecentAddress(getScreenName(), address.getAddress());//14
        }

        mPresenter.onPlaceSelected(address);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("activity must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mOnFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("activity must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAutoCompleteLoadingCross() {
        mLoaderCrossFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAutoCompleteLoadingCross() {
        mLoaderCrossFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void showAutoDetectLocationButton() {
        mAutoDetectLocationRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isShowNearbyPlaces() {
        return showNearbyPlaces;
    }

    @Override
    public void hideAutoDetectLocationButton() {
        mAutoDetectLocationRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void showHomeLocationButton() {
        mAutoCompleteHomeRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHomeLocationButton() {
        mAutoCompleteHomeRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void showWorkLocationButton() {
        mAutoCompleteWorkRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWorkLocationButton() {
        mAutoCompleteWorkRelativeLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideListPlaces() {
        mAutoCompleteRecylerView.setVisibility(View.GONE);
    }

    @Override
    public void showListPlaces() {
        mAutoCompleteRecylerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderPlacesList(ArrayList<Visitable> visitables) {
        mAdapter.clearData();
        mAutoCompleteRecylerView.setAdapter(mAdapter);
        mAdapter.setElement(visitables);
    }

    @Override
    public void resetSearch() {
        mAdapter.clearData();
        mAdapter.notifyDataSetChanged();
        mAutocompleteEditText.setText("");
    }

    @OnClick(R2.id.cabs_autocomplete_back_icon)
    public void actionBackIconClicked() {
        RideGATracking.eventBackPress(getScreenName());
        getActivity().finish();
    }

    @Override
    public void onPlaceSelectedFound(PlacePassViewModel placePassViewModel) {
        mOnFragmentInteractionListener.onLocationSelected(placePassViewModel);
    }

    @OnClick(R2.id.crux_cabs_auto_detect_container)
    public void actionAutoDetectButtonClicked() {
        mPresenter.actionAutoDetectLocation();
    }

    @OnClick(R2.id.set_location_on_map_container)
    public void actionSelectLocationOnMapClicked() {
        if (showAutodetectLocation) {
            RideGATracking.eventClickSourceOpenMap(getScreenName());
        } else {
            RideGATracking.eventClickDestinationOpenMap(getScreenName());
        }
        mOnFragmentInteractionListener.onSelectLocationOnMapSelected();
    }

    @OnClick(R2.id.cabs_autocomplete_home_box)
    public void actionHomeButtonClicked() {
        mPresenter.actionHomeLocation();
    }

    @OnClick(R2.id.cabs_autocomplete_work_box)
    public void actionWorkButtonClicked() {
        mPresenter.actionWorkLocation();
    }

    @Override
    public RequestParams getPeopleAddressParam() {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putInt(GetPeopleAddressesUseCase.PARAM_PAGE, paging.getPage());
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_QUERY, "");
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_ORDER_BY, String.valueOf(1));
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_HASH, hash);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }

    @Override
    public void setPagingConfiguration(Paging paging) {
        if (paging != null) {
            this.paging.setNextUrl(paging.getNextUrl());
        }
    }

    @Override
    public void setActiveMarketplaceSource() {
        isMarketPlaceSource = true;
    }

    @Override
    public void setActiveGooglePlaceSource() {
        isMarketPlaceSource = false;
    }

    @Override
    public void renderMorePlacesList(ArrayList<Visitable> addresses) {
        mAutoCompleteRecylerView.setAdapter(mAdapter);
        mAdapter.addElements(addresses);
    }

    @Override
    public void resetMarketplacePaging() {
        if (paging == null) paging = new Paging();
        paging.setPage(1);
        paging.setNextUrl(String.valueOf(0));
    }

    @Override
    public boolean isActiveMarketPlaceSource() {
        return isMarketPlaceSource;
    }

    @Override
    public boolean isActiveGooglePlaceSource() {
        return !isMarketPlaceSource;
    }

    @Override
    public RequestParams getUserAddressParam() {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_HASH, hash);
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetPeopleAddressesUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void hideGoogleLabel() {
        googleLabelImageView.setVisibility(View.GONE);
    }

    @Override
    public void showGoogleLabel() {
        googleLabelImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showClearButton() {
        clearFieldImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideClearButton() {
        clearFieldImageView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorNoInternetConnectionMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendAutoDetectGAEvent(PlacePassViewModel placePassViewModel) {
        RideGATracking.eventClickAutDetectLocation(getScreenName(), placePassViewModel.getAddress()); //9
    }

    @OnClick(R2.id.iv_cross)
    public void actionClearIconClicked() {
        mAutocompleteEditText.setText("");
        hideClearButton();
    }
}
