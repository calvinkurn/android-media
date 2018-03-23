package com.tokopedia.seller.product.variant.view.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.VerticalLabelView;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.view.adapter.ProductVariantDetailLevel1ListAdapter;
import com.tokopedia.seller.product.variant.view.widget.VariantImageView;

import java.util.List;

/**
 * Created by hendry on 4/3/17.
 */

public class ProductVariantDetailLevel1ListFragment extends BaseVariantImageFragment
        implements ProductVariantDetailLevel1ListAdapter.OnProductVariantDetailLevel1ListAdapterListener {

    private OnProductVariantDataManageFragmentListener listener;

    private ProductVariantDetailLevel1ListAdapter productVariantDetailLevel1ListAdapter;

    private VariantImageView variantImageView;

    public interface OnProductVariantDataManageFragmentListener {
        void onSubmitVariant();
        List<ProductVariantCombinationViewModel> getProductVariantCombinationViewModelList();
        String getVariantName();
        String getVariantValue();
        void goToLeaf(ProductVariantCombinationViewModel productVariantCombinationViewModel);
        @CurrencyTypeDef int getCurrencyType();
        ProductVariantOptionChild getProductVariantChild();
        boolean needRetainImage();
        void onImageChanged();
    }

    public static ProductVariantDetailLevel1ListFragment newInstance() {
        return new ProductVariantDetailLevel1ListFragment();
    }

    @Override
    public boolean needRetainImage() {
        return listener.needRetainImage();
    }

    @Override
    public ProductVariantOptionChild getProductVariantOptionChild() {
        return listener.getProductVariantChild();
    }

    @Override
    public void refreshImageView() {
        refreshInitialVariantImage();
        listener.onImageChanged();
    }

    private void refreshInitialVariantImage(){
        ProductVariantOptionChild childLvl1Model = listener.getProductVariantChild();
        List<VariantPictureViewModel> productPictureViewModelList = childLvl1Model.getProductPictureViewModelList();
        VariantPictureViewModel pictureViewModel = null;
        if (productPictureViewModelList != null && productPictureViewModelList.size() > 0) {
            pictureViewModel = productPictureViewModelList.get(0);
        }

        variantImageView.setImage(pictureViewModel, childLvl1Model.getHex());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        productVariantDetailLevel1ListAdapter = new ProductVariantDetailLevel1ListAdapter(getContext(),
                listener.getProductVariantCombinationViewModelList(),
                this, listener.getCurrencyType());
    }

    public void refreshData(){
        productVariantDetailLevel1ListAdapter.setProductVariantCombinationViewModelList(
                listener.getProductVariantCombinationViewModelList());
        productVariantDetailLevel1ListAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_variant_level1_list, container, false);

        VerticalLabelView lvTitle = view.findViewById(R.id.lv_title);
        lvTitle.setTitle(listener.getVariantName());
        lvTitle.setSummary(listener.getVariantValue());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        variantImageView = view.findViewById(R.id.variant_image_view);
        refreshInitialVariantImage();

        variantImageView.setOnImageClickListener(new VariantImageView.OnImageClickListener() {
            @Override
            public void onImageVariantClicked() {
                if (getProductVariantOptionChild().getProductPictureViewModelList() == null ||
                        getProductVariantOptionChild().getProductPictureViewModelList().size() == 0) {
                    showAddImageDialog();
                } else {
                    showEditImageDialog(getProductVariantOptionChild().getProductPictureViewModelList().get(0).getUriOrPath());
                }
            }
        });

        recyclerView.setAdapter(productVariantDetailLevel1ListAdapter);

        return view;
    }

    @Override
    public void onClick(ProductVariantCombinationViewModel productVariantCombinationViewModel) {
        listener.goToLeaf(productVariantCombinationViewModel);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachListener(activity);
        }
    }

    @TargetApi(23)
    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        onAttachListener(context);
    }

    protected void onAttachListener(Context context) {
        listener = (OnProductVariantDataManageFragmentListener) context;
    }

}