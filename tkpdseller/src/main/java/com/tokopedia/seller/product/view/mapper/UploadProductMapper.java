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
    public static UploadProductInputDomainModel map(UploadProductInputViewModel viewModel) {
        UploadProductInputDomainModel domainModel = new UploadProductInputDomainModel();
        domainModel.setProductPhotos(mapProductPhoto(viewModel.getProductPhotos()));
        domainModel.setProductWholesaleList(mapWholesaleDraftToDomain(viewModel.getProductWholesaleList()));
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
        return domainModel;
    }

    private static List<ProductWholesaleDomainModel> mapWholesaleDraftToDomain(List<ProductWholesaleViewModel> productWholesaleList) {
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

    private static ProductPhotoListDomainModel mapProductPhoto(ProductPhotoListViewModel productPhotos) {
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
}
