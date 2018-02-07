package com.tokopedia.seller.product.manage.view.mapper;

import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;
import com.tokopedia.seller.product.manage.view.model.ProductManageViewModel;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;
import com.tokopedia.seller.product.picker.view.model.ProductListSellerModelView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class GetProductListManageMapperView {
    @Inject
    public GetProductListManageMapperView() {
    }

    public ProductListManageModelView transform(ProductListSellerModel productListSellerModel) {
        if(productListSellerModel!= null && productListSellerModel.getData() != null) {
            ProductListManageModelView productListManageModelView = new ProductListManageModelView();
            boolean hasNextPage = checkNextPage(productListSellerModel);
            productListManageModelView.setHasNextPage(hasNextPage);
            if(productListSellerModel.getData().getList() != null){
                List<ProductManageViewModel> products = transformListProduct(productListSellerModel.getData().getList());
                productListManageModelView.setProductListPickerViewModels(products);
            }else{
                productListManageModelView.setProductListPickerViewModels(new ArrayList<ProductManageViewModel>());
            }
            if(productListManageModelView.getProductManageViewModels().isEmpty()){
                productListManageModelView.setHasNextPage(false);
            }
            return productListManageModelView;
        }else{
            throw new RuntimeException("Data tidak ditemukan");
        }
    }

    private List<ProductManageViewModel> transformListProduct(List<ProductListSellerModel.Product> list) {
        List<ProductManageViewModel> productManageViewModels = new ArrayList<>();
        for(ProductListSellerModel.Product product : list){
            ProductManageViewModel productListPickerViewModel = new ProductManageViewModel();
            productListPickerViewModel.setId(product.getProductId());
            productListPickerViewModel.setImageUrl(product.getProductImage());
            productListPickerViewModel.setTitle(product.getProductName());
            productListPickerViewModel.setProductPrice(product.getProductNormalPrice());
            productListPickerViewModel.setProductCurrencyId(Integer.parseInt(product.getProductCurrencyId()));
            productListPickerViewModel.setProductUrl(product.getProductUrl());
            productListPickerViewModel.setProductStatus(product.getProductStatus());
            productListPickerViewModel.setProductCurrencySymbol(product.getProductCurrencySymbol());
            productListPickerViewModel.setProductReturnable(product.getProductReturnable());
            productListPickerViewModel.setProductPreorder(product.getProductPreorder());
            productListPickerViewModel.setProductPricePlain(product.getProductNoIdrPrice());
            productListPickerViewModel.setProductWholesale(product.getProductWholesale());
            productListPickerViewModel.setProductCashback(product.getProductCashback());
            productListPickerViewModel.setProductCashbackAmount(product.getProductCashbackAmount());
            productListPickerViewModel.setProductStock(product.getProductStock());
            productListPickerViewModel.setProductUsingStock(product.getProductUsingStock());
            productListPickerViewModel.setProductVariant(product.getProductVariant());
            productManageViewModels.add(productListPickerViewModel);
        }
        return productManageViewModels;
    }

    private boolean checkNextPage(ProductListSellerModel productListSellerModel) {
        if(productListSellerModel.getData().getPaging()!= null &&
                productListSellerModel.getData().getPaging().getUriNext() != null &&
                !productListSellerModel.getData().getPaging().getUriNext().isEmpty() &&
                !productListSellerModel.getData().getPaging().getUriNext().equals("0")){
            return true;
        }else{
            return true;
        }
    }
}
