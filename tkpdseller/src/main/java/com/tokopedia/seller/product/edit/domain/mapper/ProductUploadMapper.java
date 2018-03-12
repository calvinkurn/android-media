package com.tokopedia.seller.product.edit.domain.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.seller.product.common.utils.CollectionAdapterUtils;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCatalogViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreOrderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by nathan on 2/28/18.
 */

public class ProductUploadMapper {

    /**
     * Compare product from server and from draft, delete unused param. Cannot send all because use patch method
     * @param productFromServer
     * @param productFromDraft
     * @return
     */
    public ProductViewModel convertUnusedParamToNull(ProductViewModel productFromServer, ProductViewModel productFromDraft) {
        ProductPreOrderViewModel productPreorderViewModel = productFromDraft.getProductPreorder();
        ProductCatalogViewModel productCatalogViewModel = productFromDraft.getProductCatalog();
        ProductPictureViewModel productDraftSizeChart = productFromDraft.getProductSizeChart();
        if (productFromServer.getProductPreorder() == null &&
                productPreorderViewModel.getPreorderProcessTime() == 0 &&
                productPreorderViewModel.getPreorderStatus() == 0 &&
                productPreorderViewModel.getPreorderTimeUnit() == 0) {
            productFromDraft.setProductPreorder(null);
        }
        if (productFromServer.getProductCatalog() == null &&
                productCatalogViewModel.getCatalogId() == 0 &&
                productCatalogViewModel.getCatalogStatus() == 0) {
            productFromDraft.setProductCatalog(null);
        }
        if (productFromServer.getProductSizeChart() == null &&
                productDraftSizeChart == null) {
            productFromDraft.setProductSizeChart(null);
        }
        return productFromDraft;
    }

    /**
     * Convert ProductViewModel to String json and remove unused param
     * @param productViewModel
     * @return
     */
    public String removeUnusedParam(ProductViewModel productViewModel) {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Collection.class, new CollectionAdapterUtils()).create();
        return gson.toJson(productViewModel);
    }

    public List<VariantPictureViewModel> getVariantPictureViewModelList(ProductViewModel productViewModel) {
        List<VariantPictureViewModel> variantPictureViewModelListTemp = new ArrayList<>();
        if (productViewModel.getProductVariant() != null && productViewModel.getProductVariant().getVariantOptionParent() != null) {
            List<ProductVariantOptionParent> variantOptionParentList = productViewModel.getProductVariant().getVariantOptionParent();
            for (ProductVariantOptionParent variantOptionParent : variantOptionParentList) {
                List<ProductVariantOptionChild> variantOptionChildList = variantOptionParent.getProductVariantOptionChild();
                for (ProductVariantOptionChild productVariantOptionChild : variantOptionChildList) {
                    variantPictureViewModelListTemp.addAll(productVariantOptionChild.getProductPictureViewModelList());
                }
            }
        }
        return variantPictureViewModelListTemp;
    }
}