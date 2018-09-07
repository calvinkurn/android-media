package com.tokopedia.inbox.rescenter.product;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.PreviewProductImage;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationAttachmentDomain;
import com.tokopedia.inbox.rescenter.product.di.component.DaggerResolutionProductDetailComponent;
import com.tokopedia.inbox.rescenter.product.di.component.ResolutionProductDetailComponent;
import com.tokopedia.inbox.rescenter.product.di.module.ResolutionProductDetailModule;
import com.tokopedia.inbox.rescenter.product.view.customadapter.AttachmentAdapter;
import com.tokopedia.inbox.rescenter.product.view.model.ProductDetailViewData;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentContract;
import com.tokopedia.inbox.rescenter.product.view.presenter.ProductDetailFragmentImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailFragment extends BaseDaggerFragment
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

    @Inject
    ProductDetailFragmentImpl presenter;

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
        renderData();
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_RESOLUTION_CENTER_PRODUCT_DETAIL;
    }

    @Override
    protected void initInjector() {
        ResolutionDetailComponent resolutionDetailComponent = getComponent(ResolutionDetailComponent.class);
        ResolutionProductDetailComponent resolutionProductDetailComponent =
                DaggerResolutionProductDetailComponent.builder()
                        .resolutionDetailComponent(resolutionDetailComponent)
                        .resolutionProductDetailModule(new ResolutionProductDetailModule(this))
                        .build();
        resolutionProductDetailComponent.inject(this);
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
    public void renderData() {
        productName.setText(viewData.getProductName());
        productPrice.setText(viewData.getProductPrice());
        ImageHandler.LoadImage(productImage, viewData.getProductThumbUrl());
        troubleName.setText(getTroubleText());
        troubleReason.setText(viewData.getTroubleReason());
        if (viewData.getAttachment() != null && !viewData.getAttachment().isEmpty()) {
            attachmentView.setVisibility(View.VISIBLE);
            renderAttachment();
        } else {
            attachmentView.setVisibility(View.GONE);
        }

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private String getTroubleText() {
        String troubleText;
        if (viewData.getQuantity() == 0) {
            troubleText = viewData.getTrouble();
        } else {
            troubleText = getString(R.string.template_product_detail_trouble, viewData.getTrouble(), viewData.getQuantity());
        }
        return troubleText;
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
