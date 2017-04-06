package com.tokopedia.inbox.rescenter.product;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.product.view.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.product.view.model.ProductDetailViewData;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentContract;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentImpl;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailFragment extends BasePresenterFragment<ProductDetailFragmentContract.Presenter>
    implements ProductDetailFragmentContract.ViewListener {

    private static final String EXTRA_PARAM_RESOLUTION_ID = "resolution_id";
    private static final String EXTRA_PARAM_TROUBLE_ID = "trouble_id";
    private static final String EXTRA_PARAM_VIEW_DATA = "view_data";

    View loading;
    View mainView;
    TextView productName;
    TextView productPrice;
    ImageView productImage;
    TextView troubleName;
    TextView troubleReason;
    View attachmentView;
    RecyclerView recyclerViewAttachment;

    private String resolutionID;
    private String troubleID;
    private ProductDetailViewData viewData;

    public static Fragment createInstance(String resolutionID, String troubleID) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM_RESOLUTION_ID, resolutionID);
        bundle.putString(EXTRA_PARAM_TROUBLE_ID, troubleID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getResolutionID() {
        return resolutionID;
    }

    @Override
    public void setResolutionID(String resolutionID) {
        this.resolutionID = resolutionID;
    }

    @Override
    public String getTroubleID() {
        return troubleID;
    }

    @Override
    public void setTroubleID(String troubleID) {
        this.troubleID = troubleID;
    }

    @Override
    public void setLoadingView(boolean show) {
        loading.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMainView(boolean show) {
        mainView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setViewData(ProductDetailViewData viewData) {
        this.viewData = viewData;
    }

    @Override
    public ProductDetailViewData getViewData() {
        return viewData;
    }

    @Override
    protected boolean isRetainInstance() {
        return true;
    }

    @Override
    protected void onFirstTimeLaunched() {
        presenter.onFirstTimeLaunched();
    }

    @Override
    public void onSaveState(Bundle state) {
        state.putString(EXTRA_PARAM_RESOLUTION_ID, getResolutionID());
        state.putString(EXTRA_PARAM_TROUBLE_ID, getTroubleID());
        state.putParcelable(EXTRA_PARAM_VIEW_DATA, getViewData());
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        setResolutionID(savedState.getString(EXTRA_PARAM_RESOLUTION_ID));
        setTroubleID(savedState.getString(EXTRA_PARAM_TROUBLE_ID));
        setViewData((ProductDetailViewData) savedState.getParcelable(EXTRA_PARAM_VIEW_DATA));
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new ProductDetailFragmentImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {
        setResolutionID(arguments.getString(EXTRA_PARAM_RESOLUTION_ID));
        setTroubleID(arguments.getString(EXTRA_PARAM_TROUBLE_ID));
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_detail_product_rescenter;
    }

    @Override
    protected void initView(View view) {
        loading = view.findViewById(R.id.loading);
        mainView = view.findViewById(R.id.main_view);
        productName = (TextView) view.findViewById(R.id.tv_product_name);
        productPrice = (TextView) view.findViewById(R.id.tv_product_price);
        productImage = (ImageView) view.findViewById(R.id.iv_product_image);
        troubleName = (TextView) view.findViewById(R.id.complaint_name);
        troubleReason = (TextView) view.findViewById(R.id.complaint_reason);
        attachmentView = view.findViewById(R.id.view_attachment);
        recyclerViewAttachment = (RecyclerView) view.findViewById(R.id.attachment);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void renderData() {
        productName.setText(viewData.getProductName());
        productPrice.setText(viewData.getProductPrice());
        ImageHandler.loadImage2(
                productImage, viewData.getProductThumbUrl(), R.drawable.remove_thin
        );
        troubleName.setText(viewData.getTrouble());
        troubleReason.setText(viewData.getTroubleReason());
        if (viewData.getAttachment() != null && !viewData.getAttachment().isEmpty()) {
            attachmentView.setVisibility(View.VISIBLE);
            renderAttachment();
        } else {
            attachmentView.setVisibility(View.GONE);
        }
    }

    private void renderAttachment() {
        AttachmentAdapter attachmentAdapter = new AttachmentAdapter(getViewData().getAttachment());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAttachment.setLayoutManager(layoutManager);
        recyclerViewAttachment.setAdapter(attachmentAdapter);
    }

    @Override
    public void onGetProductDetailFailed(String messageError) {
        setLoadingView(false);
        setMainView(false);
        if (messageError == null) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), null);
        } else {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), messageError, null);
        }
    }

    @Override
    public void onGetProductDetailTimeOut() {
        setLoadingView(false);
        setMainView(false);
        NetworkErrorHelper.showEmptyState(
                getActivity(),
                getView(),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        onFirstTimeLaunched();
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setOnDestroyView();
    }
}
