package com.tokopedia.ride.bookingride.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.RideProductDependencyInjection;
import com.tokopedia.ride.bookingride.domain.model.ProductEstimate;
import com.tokopedia.ride.bookingride.view.UberProductContract;
import com.tokopedia.ride.bookingride.view.adapter.RideProductAdapter;
import com.tokopedia.ride.bookingride.view.adapter.RideProductItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberProductFragment extends BaseFragment implements UberProductContract.View, RideProductItemClickListener {
    OnFragmentInteractionListener mInteractionListener;
    UberProductContract.Presenter mPresenter;

    @BindView(R2.id.ride_product_list)
    RecyclerView mRideProductsRecyclerView;
    @BindView(R2.id.crux_cabs_home_ad)
    LinearLayout mAdsContainerLinearLayout;
    @BindView(R2.id.crux_cabs_ad_title)
    TextView mAdsTitleTextView;
    @BindView(R2.id.crux_cabs_ad_cross)
    ImageView mAdsCrossImageView;
    @BindView(R2.id.empty_product_list)
    View mEmptyProductLinearLayout;
    @BindView(R2.id.tv_error_desc)
    TextView mErrorDescriptionTextView;
    @BindView(R2.id.empty_list_retry)
    TextView mRetryButtonTextView;
    @BindView(R2.id.product_list_progress)
    ProgressBar mProgressBar;
    @BindView(R2.id.layout_progress_and_error_view)
    View mProgreessAndErrorView;

    boolean isCompleteLocations;

    List<RideProductViewModel> rideProductViewModels;
    private PlacePassViewModel source, destination;

    RideProductAdapter mAdapter;

    public interface OnFragmentInteractionListener {
        void onProductClicked(ConfirmBookingViewModel rideProductViewModel);

        void onMinimumTimeEstCalculated(String timeEst);
    }

    public static UberProductFragment newInstance() {
        return new UberProductFragment();
    }

    public UberProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = RideProductDependencyInjection.createPresenter(getActivity().getApplicationContext());
        mPresenter.attachView(this);
        mPresenter.initialize();
        setViewListener();
    }

    private void setViewListener() {
        RideProductTypeFactory placeAutoCompleteTypeFactory = new RideProductAdapterTypeFactory(this);
        mAdapter = new RideProductAdapter(placeAutoCompleteTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRideProductsRecyclerView.setLayoutManager(layoutManager);
        mRideProductsRecyclerView.setHasFixedSize(true);
        mRideProductsRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_uber_product;
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implemented OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implemented OnFragmentInteractionListener");
        }
    }


    @Override
    public void onProductSelected(RideProductViewModel rideProductViewModel) {
        if (isCompleteLocations) {
            ConfirmBookingViewModel confirmBookingViewModel = ConfirmBookingViewModel.createInitial();
            confirmBookingViewModel.setFareId(rideProductViewModel.getFareId());
            confirmBookingViewModel.setSource(source);
            confirmBookingViewModel.setDestination(destination);
            confirmBookingViewModel.setProductId(rideProductViewModel.getProductId());
            confirmBookingViewModel.setPrice(rideProductViewModel.getProductPrice());
            confirmBookingViewModel.setProductImage(rideProductViewModel.getProductImage());
            confirmBookingViewModel.setHeaderTitle(
                    String.format(
                            "%s - Pickup in %s s",
                            rideProductViewModel.getProductName(),
                            rideProductViewModel.getTimeEstimate())
            );
            confirmBookingViewModel.setMaxCapacity(rideProductViewModel.getCapacity());
            mInteractionListener.onProductClicked(confirmBookingViewModel);
        }
    }


    public void updateProductList(PlacePassViewModel source, PlacePassViewModel destination) {
        this.destination = destination;
        this.source = source;
        if (source != null && destination != null) {
            isCompleteLocations = true;
        } else {
            isCompleteLocations = false;
        }
        showProgress();
        mPresenter.actionGetRideProducts(source, destination);
    }

    @Override
    public void renderProductList(List<Visitable> datas) {
        mProgreessAndErrorView.setVisibility(View.GONE);
        mRideProductsRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.clearData();
        mAdapter.setElement(datas);
    }

    @OnClick(R2.id.crux_cabs_ad_cross)
    public void actionCloseAdsButtonClicked() {
        hideAdsBadges();
    }

    @OnClick(R2.id.empty_list_retry)
    public void actionRetry() {

    }

    @Override
    public void showAdsBadges(String message) {
        mAdsContainerLinearLayout.setVisibility(View.VISIBLE);
        mAdsTitleTextView.setText(String.valueOf(message));
    }

    @Override
    public void hideAdsBadges() {
        mAdsContainerLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(int messageResourceID) {
        mProgreessAndErrorView.setVisibility(View.VISIBLE);
        mEmptyProductLinearLayout.setVisibility(View.VISIBLE);
        mErrorDescriptionTextView.setText(messageResourceID);
    }

    @Override
    public void hideErrorMessage(String message) {
        mProgreessAndErrorView.setVisibility(View.GONE);
        mEmptyProductLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void renderFareProduct(Visitable productEstimate,
                                  String productId,
                                  int position,
                                  FareEstimate fareEstimate) {
        mAdapter.setChangedItem(position, productEstimate);
    }

    @Override
    public void actionMinimumTimeEstResult(String timeEst) {
        mInteractionListener.onMinimumTimeEstCalculated(timeEst);
    }
}
