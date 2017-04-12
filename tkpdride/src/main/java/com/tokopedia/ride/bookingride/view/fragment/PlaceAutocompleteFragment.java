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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.PlaceAutoCompleteDependencyInjection;
import com.tokopedia.ride.bookingride.domain.GetPeopleAddressesUseCase;
import com.tokopedia.ride.bookingride.domain.model.PeopleAddressPaging;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompleteContract;
import com.tokopedia.ride.bookingride.view.adapter.EndlessRecyclerViewScrollListener;
import com.tokopedia.ride.bookingride.view.adapter.ItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.PlaceAutoCompleteAdapter;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

public class PlaceAutocompleteFragment extends BaseFragment implements PlaceAutoCompleteContract.View,
        GoogleApiClient.OnConnectionFailedListener,
        ItemClickListener {
    private static final String TAG = "addressautocomplete";

    private PlaceAutoCompleteAdapter mAdapter;
    private PlaceAutoCompleteContract.Presenter mPresenter;
    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private PeopleAddressPaging peopleAddressPaging;
    private boolean isMarketPlaceSource;

    @BindView(R2.id.cabs_autocomplete_edit_text)
    EditText mAutocompleteEditText;
    @BindView(R2.id.layout_loader_cross)
    FrameLayout mLoaderCrossFrameLayout;
    @BindView(R2.id.cabs_autocomplete_back_icon)
    ImageView mBackIconImageView;
    @BindView(R2.id.crux_cabs_auto_detect_container)
    RelativeLayout mAutoDetectLocationRelativeLayout;
    @BindView(R2.id.cabs_autocomplete_home_box)
    RelativeLayout mAutoCompleteHomeRelativeLayout;
    @BindView(R2.id.cabs_autocomplete_work_box)
    RelativeLayout mAutoCompleteWorkRelativeLayout;
    @BindView(R2.id.cabs_autocomplete_recycler_view)
    RecyclerView mAutoCompleteRecylerView;

    OnFragmentInteractionListener mOnFragmentInteractionListener;

    public interface OnFragmentInteractionListener {
        void onLocationSelected(PlacePassViewModel placeId);
    }

    public static Fragment newInstance() {
        return new PlaceAutocompleteFragment();
    }

    public PlaceAutocompleteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        peopleAddressPaging = new PeopleAddressPaging();
        peopleAddressPaging.setPage(1);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = PlaceAutoCompleteDependencyInjection.createPresenter();
        mPresenter.attachView(this);
        mPresenter.initialize();

        mAutocompleteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    CommonUtils.dumper("Executed OTC M");
                    setActiveMarketplaceSource();
                    mPresenter.actionGetPeopleAddresses(false);
                }
                else {
                    setActiveGooglePlaceSource();
                    CommonUtils.dumper("Executed OTC G");
                    mPresenter.actionQueryPlacesByKeyword(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                CommonUtils.dumper("af : " + mAutocompleteEditText.getText().toString());
            }
        });

        PlaceAutoCompleteTypeFactory placeAutoCompleteTypeFactory = new PlaceAutoCompleteAdapterTypeFactory(this);
        mAdapter = new PlaceAutoCompleteAdapter(placeAutoCompleteTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAutoCompleteRecylerView.setLayoutManager(layoutManager);
        mAutoCompleteRecylerView.setHasFixedSize(true);

        mEndlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                actionOnLoadMoreDetected(page + 1);
            }
        };
        mAutoCompleteRecylerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
        mAutoCompleteRecylerView.setAdapter(mAdapter);
    }

    private void actionOnLoadMoreDetected(int page) {
        if (isMarketPlaceSource) {
            if (!TextUtils.isEmpty(peopleAddressPaging.getNextUrl()) && !String.valueOf(peopleAddressPaging.getNextUrl()).equalsIgnoreCase("0")) {
                peopleAddressPaging.setPage(page);
                setActiveMarketplaceSource();
                mPresenter.actionGetPeopleAddresses(true);
            }
        }
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
        requestParams.putInt(GetPeopleAddressesUseCase.PARAM_PAGE, peopleAddressPaging.getPage());
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
    public void setPagingConfiguration(PeopleAddressPaging paging) {
        peopleAddressPaging.setNextUrl(paging.getNextUrl());
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
        if (peopleAddressPaging == null) peopleAddressPaging = new PeopleAddressPaging();
        peopleAddressPaging.setPage(1);
        peopleAddressPaging.setNextUrl(String.valueOf(0));
    }

    @Override
    public boolean isActiveMarketPlaceSource() {
        return isMarketPlaceSource;
    }

    @Override
    public boolean isActiveGooglePlaceSource() {
        return !isMarketPlaceSource;
    }
}
