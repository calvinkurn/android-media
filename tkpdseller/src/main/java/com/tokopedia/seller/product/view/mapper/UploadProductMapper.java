package com.tokopedia.seller.product.view.mapper;

import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class UploadProductMapper {
    public static UploadProductInputDomainModel mapViewToDomain(UploadProductInputViewModel viewModel) {
        UploadProductInputDomainModel domainModel = new UploadProductInputDomainModel();
        domainModel.setProductPhotos(mapProductPhotoViewToDomain(viewModel.getProductPhotos()));
        domainModel.setProductWholesaleList(mapWholesaleViewToDomain(viewModel.getProductWholesaleList()));
        domainModel.setProductVideos(viewModel.getProductVideos());
        domainModel.setProductName(viewModel.getProductName());
        domainModel.setProductDescription(viewModel.getProductDescription());
        domainModel.setProductChangePhoto(viewModel.getProductChangePhoto());
        domainModel.setProductCatalogId(viewModel.getProductCatalogId());
        domainModel.setProductDepartmentId(viewModel.getProductDepartmentId());
        domainModel.setProductCondition(viewModel.getProductCondition());
        domainModel.setProductEtalaseId(viewModel.getProductEtalaseId());
        domainModel.setProductMinOrder(viewModel.getProductMinOrder());
        domainModel.setProductMustInsurance(viewModel.getProductMustInsurance());
        domainModel.setProductPrice(viewModel.getProductPrice());
        domainModel.setProductPriceCurrency(viewModel.getProductPriceCurrency());
        domainModel.setProductReturnable(viewModel.getProductReturnable());
        domainModel.setProductUploadTo(viewModel.getProductUploadTo());
        domainModel.setProductWeight(viewModel.getProductWeight());
        domainModel.setProductWeightUnit(viewModel.getProductWeightUnit());
        domainModel.setPoProcessType(viewModel.getPoProcessType());
        domainModel.setPoProcessValue(viewModel.getPoProcessValue());
        domainModel.setServerId(viewModel.getServerId());
        domainModel.setProductStatus(viewModel.getProductStatus());
        return domainModel;
    }

    private static List<ProductWholesaleDomainModel> mapWholesaleViewToDomain(List<ProductWholesaleViewModel> productWholesaleList) {
        List<ProductWholesaleDomainModel> domainModels = new ArrayList<>();
        for (ProductWholesaleViewModel viewModel : productWholesaleList){
            ProductWholesaleDomainModel domainModel = new ProductWholesaleDomainModel();
            domainModel.setQtyMin(viewModel.getQtyMin());
            domainModel.setQtyMax(viewModel.getQtyMax());
            domainModel.setPrice(viewModel.getPrice());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

    private static ProductPhotoListDomainModel mapProductPhotoViewToDomain(ProductPhotoListViewModel productPhotos) {
        ProductPhotoListDomainModel domainModel = new ProductPhotoListDomainModel();
        domainModel.setProductDefaultPicture(productPhotos.getProductDefaultPicture());
        domainModel.setPhotos(mapPhotosViewToDomain(productPhotos.getPhotos()));
        return domainModel;
    }

    private static List<ImageProductInputDomainModel> mapPhotosViewToDomain(List<ImageProductInputViewModel> photos) {
        List<ImageProductInputDomainModel> domainModels = new ArrayList<>();
        for (ImageProductInputViewModel viewModel : photos){
            ImageProductInputDomainModel domainModel = new ImageProductInputDomainModel();
            domainModel.setImagePath(viewModel.getImagePath());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

    public static UploadProductInputViewModel mapDomainToView(UploadProductInputDomainModel domainModel) {
        UploadProductInputViewModel viewModel = new UploadProductInputViewModel();
        viewModel.setProductPhotos(mapProductPhotoDomainToView(domainModel.getProductPhotos()));
        viewModel.setProductWholesaleList(mapWholesaleDomainToView(domainModel.getProductWholesaleList()));
        viewModel.setProductVideos(domainModel.getProductVideos());
        viewModel.setProductName(domainModel.getProductName());
        viewModel.setProductDescription(domainModel.getProductDescription());
        viewModel.setProductChangePhoto(domainModel.getProductChangePhoto());
        viewModel.setProductCatalogId(domainModel.getProductCatalogId());
        viewModel.setProductCatalogName(domainModel.getProductCatalogName());
        viewModel.setProductDepartmentId(domainModel.getProductDepartmentId());
        viewModel.setProductCondition(domainModel.getProductCondition());
        viewModel.setProductEtalaseId(domainModel.getProductEtalaseId());
        viewModel.setProductEtalaseName(domainModel.getProductEtalaseName());
        viewModel.setProductMinOrder(domainModel.getProductMinOrder());
        viewModel.setProductMustInsurance(domainModel.getProductMustInsurance());
        viewModel.setProductPrice(domainModel.getProductPrice());
        viewModel.setProductPriceCurrency(domainModel.getProductPriceCurrency());
        viewModel.setProductReturnable(domainModel.getProductReturnable());
        viewModel.setProductUploadTo(domainModel.getProductUploadTo());
        viewModel.setProductWeight(domainModel.getProductWeight());
        viewModel.setProductWeightUnit(domainModel.getProductWeightUnit());
        viewModel.setPoProcessType(domainModel.getPoProcessType());
        viewModel.setPoProcessValue(domainModel.getPoProcessValue());
        viewModel.setServerId(domainModel.getServerId());
        return viewModel;
    }

    private static List<ProductWholesaleViewModel> mapWholesaleDomainToView(List<ProductWholesaleDomainModel> productWholesaleList) {
        return null;
    }

    private static ProductPhotoListViewModel mapProductPhotoDomainToView(ProductPhotoListDomainModel productPhotos) {
        ProductPhotoListViewModel viewModel = new ProductPhotoListViewModel();
        viewModel.setProductDefaultPicture(productPhotos.getProductDefaultPicture());
        viewModel.setPhotos(mapPhotosListDomainToView(productPhotos.getPhotos()));
        return viewModel;
    }

    private static List<ImageProductInputViewModel> mapPhotosListDomainToView(List<ImageProductInputDomainModel> photos) {
        List<ImageProductInputViewModel> viewModels = new ArrayList<>();
        for (ImageProductInputDomainModel domainModel : photos) {
            ImageProductInputViewModel viewModel = new ImageProductInputViewModel();
            viewModel.setImagePath(domainModel.getImagePath());
            viewModel.setImageDescription(domainModel.getDescription());
            viewModel.setPicId(domainModel.getPicId());
            viewModel.setUrl(domainModel.getUrl());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
