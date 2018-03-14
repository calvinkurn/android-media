package com.tokopedia.seller.product.edit.domain.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.seller.product.common.utils.CollectionAdapterUtils;
import com.tokopedia.seller.product.edit.view.model.edit.ProductBrandViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCatalogViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCategoryViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductEtalaseViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreOrderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.VariantPictureViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
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
     *
     * @param productFromServer
     * @param productFromDraft
     * @return
     */
    public ProductViewModel convertUnusedParamToNull(ProductViewModel productFromServer, ProductViewModel productFromDraft) {
        //if the previous object is null, and the updated object is empty (but not null), then set to null
        // this is to make sure the server will not delete the non-existent attribute.

        ProductPreOrderViewModel productPreorderViewModel = productFromDraft.getProductPreorder();
        if (productFromServer.getProductPreorder() == null &&
                productPreorderViewModel != null &&
                productPreorderViewModel.getPreorderStatus() == 0) {
            productFromDraft.setProductPreorder(null);
        }

        ProductCatalogViewModel productCatalogViewModel = productFromDraft.getProductCatalog();
        if (productFromServer.getProductCatalog() == null &&
                productCatalogViewModel != null &&
                productCatalogViewModel.getCatalogId() == 0) {
            productFromDraft.setProductCatalog(null);
        }

        ProductPictureViewModel productDraftSizeChart = productFromDraft.getProductSizeChart();
        if (productFromServer.getProductSizeChart() == null &&
                productDraftSizeChart == null) {
            productFromDraft.setProductSizeChart(null);
        }

        List<ProductWholesaleViewModel> productWholesaleList = productFromDraft.getProductWholesale();
        if (productFromServer.getProductWholesale() == null &&
                productWholesaleList != null &&
                productWholesaleList.size() == 0) {
            productFromDraft.setProductWholesale(null);
        }

        ProductCategoryViewModel productCategoryViewModel = productFromDraft.getProductCategory();
        if (productFromServer.getProductCategory() == null &&
                productCategoryViewModel != null &&
                productCategoryViewModel.getCategoryId() <= 0) {
            productFromDraft.setProductCategory(null);
        }

        ProductEtalaseViewModel productEtalaseViewModel = productFromDraft.getProductEtalase();
        if (productFromServer.getProductEtalase() == null &&
                productEtalaseViewModel.getEtalaseId() <= 0) {
            productFromDraft.setProductEtalase(null);
        }

        List<ProductVideoViewModel> productVideoViewModel = productFromDraft.getProductVideo();
        if (productFromServer.getProductVideo() == null &&
                productVideoViewModel != null &&
                productVideoViewModel.size() == 0) {
            productFromDraft.setProductVideo(null);
        }

        ProductBrandViewModel productBrandViewModel = productFromDraft.getProductBrand();
        if (productFromServer.getProductBrand() == null &&
                productBrandViewModel != null &&
                productBrandViewModel.getBrandId() <= 0) {
            productFromDraft.setProductBrand(null);
        }

        ProductVariantViewModel productVariantViewModel = productFromDraft.getProductVariant();
        if (productFromServer.getProductVariant() == null &&
                productVariantViewModel != null &&
                !productVariantViewModel.hasSelectedVariant()) {
            productFromDraft.setProductVariant(null);
        }

        return productFromDraft;
    }

    /**
     * Convert ProductViewModel to String json and remove unused param
     *
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