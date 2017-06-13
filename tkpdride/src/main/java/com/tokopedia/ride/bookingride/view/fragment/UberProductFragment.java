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
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.ride.R;
import com.tokopedia.ride.R2;
import com.tokopedia.ride.base.presentation.BaseFragment;
import com.tokopedia.ride.bookingride.di.RideProductDependencyInjection;
import com.tokopedia.ride.bookingride.domain.GetPromoUseCase;
import com.tokopedia.ride.bookingride.view.UberProductContract;
import com.tokopedia.ride.bookingride.view.adapter.RideProductAdapter;
import com.tokopedia.ride.bookingride.view.adapter.RideProductItemClickListener;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductAdapterTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.factory.RideProductTypeFactory;
import com.tokopedia.ride.bookingride.view.adapter.viewmodel.RideProductViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tokopedia.core.network.retrofit.utils.AuthUtil.md5;

/**
 * A simple {@link Fragment} subclass.
 */
public class UberProductFragment extends BaseFragment implements UberProductContract.View, RideProductItemClickListener {
    OnFragmentInteractionListener mInteractionListener;
    UberProductContract.Presenter mPresenter;
    private static final String EXTRA_SOURCE = "EXTRA_SOURCE";
    private static final String EXTRA_DESTINATION = "EXTRA_DESTINATION";

    @BindView(R2.id.ride_product_list)
    RecyclerView mRideProductsRecyclerView;
    @BindView(R2.id.crux_cabs_home_ad)
    LinearLayout mAdsContainerLinearLayout;
    @BindView(R2.id.crux_cabs_ad_title)
    TextView mAdsTitleTextView;
    @BindView(R2.id.crux_cabs_ad_cross)
    ImageView mAdsCrossImageView;
    @BindView(R2.id.empty_product_list)
    View mErrorLayout;
    @BindView(R2.id.tv_error_desc)
    TextView mErrorDescriptionTextView;
    @BindView(R2.id.empty_list_retry)
    TextView mRetryButtonTextView;
    @BindView(R2.id.product_list_progress)
    ProgressBar mProgressBar;
    @BindView(R2.id.layout_error_view)
    View mErrorView;
    @BindView(R2.id.layout_progress_view)
    View mProgressView;

    boolean isCompleteLocations;

    List<RideProductViewModel> rideProductViewModels;
    private PlacePassViewModel source, destination;

    RideProductAdapter mAdapter;

    public interface OnFragmentInteractionListener {
        void onProductClicked(ConfirmBookingViewModel rideProductViewModel);

        void onMinimumTimeEstCalculated(String timeEst);

        void showEnterDestError();

        void showEnterSourceLocationActiity();

        void actionProductListHeaderClick();

        void actionAdsShowed();

        void actionAdsHidden();
    }

    public static UberProductFragment newInstance() {
        return new UberProductFragment();
    }

    public static UberProductFragment newInstance(PlacePassViewModel source, PlacePassViewModel destination) {
        UberProductFragment fragment = new UberProductFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SOURCE, source);
        bundle.putParcelable(EXTRA_DESTINATION, destination);
        fragment.setArguments(bundle);
        return fragment;
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
        if (getArguments() != null) {
            source = getArguments().getParcelable(EXTRA_SOURCE);
            destination = getArguments().getParcelable(EXTRA_DESTINATION);
            updateProductList(source, destination);
        }
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
            //if fare is not find and product is not estimated, do not handle click event
            if (!rideProductViewModel.isEnabled()) {
                return;
            }

            ConfirmBookingViewModel confirmBookingViewModel = ConfirmBookingViewModel.createInitial();
            confirmBookingViewModel.setFareId(rideProductViewModel.getFareId());
            confirmBookingViewModel.setSource(source);
            confirmBookingViewModel.setDestination(destination);
            confirmBookingViewModel.setProductDisplayName(rideProductViewModel.getProductName());
            confirmBookingViewModel.setProductId(rideProductViewModel.getProductId());
            confirmBookingViewModel.setPriceFmt(rideProductViewModel.getProductPriceFmt());
            confirmBookingViewModel.setPrice(rideProductViewModel.getProductPrice());
            confirmBookingViewModel.setProductImage(rideProductViewModel.getProductImage());
            confirmBookingViewModel.setHeaderTitle(
                    String.format(
                            getString(R.string.confirm_booking_header_title_format),
                            rideProductViewModel.getProductName(),
                            rideProductViewModel.getTimeEstimate())
            );
            confirmBookingViewModel.setMaxCapacity(rideProductViewModel.getCapacity());
            confirmBookingViewModel.setSurgeMultiplier(rideProductViewModel.getSurgeMultiplier());
            confirmBookingViewModel.setSurgeConfirmationHref(rideProductViewModel.getSurgeConfirmationHref());
            confirmBookingViewModel.setCancellationFee(rideProductViewModel.getCancellationFee());
            mInteractionListener.onProductClicked(confirmBookingViewModel);
        } else {
            //show message to enter destianation
            mInteractionListener.showEnterDestError();
        }
    }


    public void updateProductList(PlacePassViewModel source, PlacePassViewModel destination) {
        this.destination = destination;
        this.source = source;
        isCompleteLocations = source != null && destination != null;
        mPresenter.actionGetRideProducts(source, destination);
    }

    @Override
    public void renderProductList(List<Visitable> datas) {
        mAdapter.clearData();
        mAdapter.setElement(datas);
    }

    @Override
    public List<Visitable> getProductList() {
        if (mAdapter == null) {
            return null;
        }

        return mAdapter.getElements();
    }

    @OnClick(R2.id.crux_cabs_ad_cross)
    public void actionCloseAdsButtonClicked() {
        hideAdsBadges();
    }

    @OnClick(R2.id.empty_list_retry)
    public void actionRetry() {
        if (source != null) {
            hideErrorMessageLayout();
            mPresenter.actionGetRideProducts(source, destination);
        } else {
            //open a dialog to enter source location
            mInteractionListener.showEnterSourceLocationActiity();
        }
    }

    @Override
    public void showAdsBadges(String message) {
        mInteractionListener.actionAdsShowed();
        mAdsContainerLinearLayout.setVisibility(View.VISIBLE);
        mAdsTitleTextView.setText(String.valueOf(message));
    }

    @Override
    public void hideAdsBadges() {
        mInteractionListener.actionAdsHidden();
        mAdsContainerLinearLayout.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressView.setVisibility(View.GONE);
    }

    @Override
    public void hideProductList() {
        if (mRideProductsRecyclerView != null) {
            mRideProductsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProductList() {
        if (mRideProductsRecyclerView != null) {
            mRideProductsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorMessage(String message, String btnText) {
        hideProgress();
        mErrorView.setVisibility(View.VISIBLE);
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorDescriptionTextView.setText(message);
        if (btnText != null && !btnText.isEmpty()) {
            mRetryButtonTextView.setVisibility(View.VISIBLE);
            mRetryButtonTextView.setText(btnText);
        } else {
            mRetryButtonTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideErrorMessage() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideErrorMessageLayout() {
        if (mErrorLayout != null) {
            mErrorLayout.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @OnClick(R2.id.layout_cab_booking_header)
    public void actionProductListHeaderClicked() {
        mInteractionListener.actionProductListHeaderClick();
    }

    @Override
    public RequestParams getPromoParams() {
        String deviceId = GCMHandler.getRegistrationId(getActivity());
        String userId = SessionHandler.getLoginID(getActivity());
        String hash = md5(userId + "~" + deviceId);
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetPromoUseCase.PARAM_USER_ID, userId);
        requestParams.putString(GetPromoUseCase.PARAM_DEVICE_ID, deviceId);
        requestParams.putString(GetPromoUseCase.PARAM_HASH, hash);
        requestParams.putString(GetPromoUseCase.PARAM_OS_TYPE, "1");
        requestParams.putString(GetPromoUseCase.PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return requestParams;
    }
}
