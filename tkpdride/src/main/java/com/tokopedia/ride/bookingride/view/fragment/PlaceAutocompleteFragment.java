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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompleteContract;
import com.tokopedia.ride.bookingride.view.PlaceAutoCompletePresenter;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.bookingride.view.adapter.PlaceAutoCompleteAdapter;
import com.tokopedia.ride.bookingride.view.adapter.ItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.PlaceAutoCompeleteViewModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class PlaceAutocompleteFragment extends BaseFragment implements PlaceAutoCompleteContract.View,
        GoogleApiClient.OnConnectionFailedListener,
        ItemClickListener {
    private static final String TAG = "addressautocomplete";

    private PlaceAutoCompleteAdapter mAdapter;

    private PlaceAutoCompleteContract.Presenter mPresenter;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new PlaceAutoCompletePresenter();
        mPresenter.attachView(this);
        mPresenter.initialize();

        mAutocompleteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                mPresenter.actionQueryPlacesByKeyword(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        mPresenter.onPlaceSelected(address.getAddressId());
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
    public void renderPlacesList(ArrayList<Visitable> visitables) {
        mAdapter.clearData();
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
}
