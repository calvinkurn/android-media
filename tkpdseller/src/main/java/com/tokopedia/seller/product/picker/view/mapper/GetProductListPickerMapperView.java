package com.tokopedia.seller.product.picker.view.mapper;

import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.view.model.ProductListPickerViewModel;
import com.tokopedia.seller.product.picker.view.model.ProductListSellerModelView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 9/7/17.
 */

public class GetProductListPickerMapperView {
    @Inject
    public GetProductListPickerMapperView() {
    }

    public ProductListSellerModelView transform(ProductListSellerModel productListSellerModel) {
        if(productListSellerModel!= null && productListSellerModel.getData() != null) {
            ProductListSellerModelView productListSellerModelView = new ProductListSellerModelView();
            boolean hasNextPage = checkNextPage(productListSellerModel);
            productListSellerModelView.setHasNextPage(hasNextPage);
            if(productListSellerModel.getData().getList() != null){
                List<ProductListPickerViewModel> products = transformListProduct(productListSellerModel.getData().getList());
                productListSellerModelView.setProductListPickerViewModels(products);
            }else{
                productListSellerModelView.setProductListPickerViewModels(new ArrayList<ProductListPickerViewModel>());
            }
            if(productListSellerModelView.getProductListPickerViewModels().isEmpty()){
                productListSellerModelView.setHasNextPage(false);
            }
            return productListSellerModelView;
        }else{
            throw new RuntimeException("Data tidak ditemukan");
        }
    }

    private List<ProductListPickerViewModel> transformListProduct(List<ProductListSellerModel.Product> list) {
        List<ProductListPickerViewModel> productListPickerViewModels = new ArrayList<>();
        for(ProductListSellerModel.Product product : list){
            ProductListPickerViewModel productListPickerViewModel = new ProductListPickerViewModel();
            productListPickerViewModel.setId(product.getProductId());
            productListPickerViewModel.setImageUrl(product.getProductImage());
            productListPickerViewModel.setTitle(product.getProductName());
            productListPickerViewModel.setProductPrice(product.getProductCurrency() + " " + product.getProductNormalPrice());
            productListPickerViewModel.setProductStatus(product.getProductStatus());
            productListPickerViewModels.add(productListPickerViewModel);
        }
        return productListPickerViewModels;
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
