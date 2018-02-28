package com.tokopedia.seller.product.edit.domain.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.seller.product.common.utils.CollectionAdapterUtils;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCatalogViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreOrderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.Collection;

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
}