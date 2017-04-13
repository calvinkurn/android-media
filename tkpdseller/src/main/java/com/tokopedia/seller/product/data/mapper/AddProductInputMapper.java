package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductSubmitInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductValidationInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.EditProductInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductPhotoListServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductPhotoServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductWholesaleServiceModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductInputMapper {
    public static AddProductValidationInputServiceModel mapValidation(UploadProductInputDomainModel domainModel) {

        AddProductValidationInputServiceModel serviceModel = new AddProductValidationInputServiceModel();
        serviceModel.setProductWholesale(mapWholesale(domainModel.getProductPriceOne()));
        serviceModel.setProductCatalogId(domainModel.getProductCatalogId());
        serviceModel.setProductCondition(domainModel.getProductCondition());
        serviceModel.setProductDepartmentId(domainModel.getProductDepartmentId());
        serviceModel.setProductDescription(domainModel.getProductDescription());
        serviceModel.setProductEtalaseId(domainModel.getProductEtalaseId());
        serviceModel.setProductEtalaseName(domainModel.getProductEtalaseName());
        serviceModel.setProductMinOrder(domainModel.getProductMinOrder());
        serviceModel.setProductMustInsurance(domainModel.getProductMustInsurance());
        serviceModel.setProductName(domainModel.getProductName());
        serviceModel.setProductPhotos(mapPhotoModel(domainModel.getProductPhotos()));
        serviceModel.setProductPrice(domainModel.getProductPrice());
        serviceModel.setProductPriceCurrency(domainModel.getProductPriceCurrency());
        serviceModel.setProductReturnable(domainModel.getProductReturnable());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        serviceModel.setProductWeight(domainModel.getProductWeight());
        serviceModel.setProductWeightUnit(domainModel.getProductWeightUnit());
        serviceModel.setPoProcessType(domainModel.getPoProcessType());
        serviceModel.setPoProcessValue(domainModel.getPoProcessValue());
        serviceModel.setProductVideo(domainModel.getProductVideos());
        serviceModel.setServerId(domainModel.getServerId());

        return serviceModel;
    }

    public static AddProductPictureInputServiceModel mapPicture(AddProductPictureInputDomainModel domainModel) {
        AddProductPictureInputServiceModel serviceModel = new AddProductPictureInputServiceModel();
        serviceModel.setProductPhoto(mapPhotoModel(domainModel.getProductPhotos()));
        serviceModel.setServerId(domainModel.getServerId());
        return serviceModel;
    }

    public static AddProductSubmitInputServiceModel mapSubmit(AddProductSubmitInputDomainModel domainModel) {
        AddProductSubmitInputServiceModel serviceModel = new AddProductSubmitInputServiceModel();
        serviceModel.setPostKey(domainModel.getPostKey());
        serviceModel.setFileUploaded(domainModel.getFileUploadedTo());
        serviceModel.setProductEtalaseName(domainModel.getProductEtalaseName());
        serviceModel.setProductEtalseId(domainModel.getProductEtalaseId());
        serviceModel.setProductUploadTo(domainModel.getProductUploadTo());
        return serviceModel;
    }

    public static EditProductInputServiceModel mapEdit(UploadProductInputDomainModel domainModel) {
        EditProductInputServiceModel serviceModel = new EditProductInputServiceModel();

        return serviceModel;
    }

    private static ProductPhotoListServiceModel mapPhotoModel(ProductPhotoListDomainModel photos) {
        ProductPhotoListServiceModel serviceModel = new ProductPhotoListServiceModel();
        serviceModel.setProductDefaultPhoto(photos.getProductDefaultPicture());
        serviceModel.setPhotosServiceModelList(mapPhotos(photos.getPhotos()));

        return serviceModel;
    }

    private static List<ProductPhotoServiceModel> mapPhotos(List<ImageProductInputDomainModel> photos) {
        List<ProductPhotoServiceModel> serviceModelList = new ArrayList<>();
        for (ImageProductInputDomainModel domainModel : photos){
            ProductPhotoServiceModel serviceModel = new ProductPhotoServiceModel();
            serviceModel.setUrl(domainModel.getUrl());
            serviceModel.setDescription(domainModel.getDescription());
            serviceModelList.add(serviceModel);
        }
        return serviceModelList;
    }

    private static List<ProductWholesaleServiceModel> mapWholesale(List<ProductWholesaleDomainModel> wholesaleDomainModelList) {
        List<ProductWholesaleServiceModel> serviceModelList = new ArrayList<>();
        for (ProductWholesaleDomainModel domainModel : wholesaleDomainModelList){
            ProductWholesaleServiceModel serviceModel = new ProductWholesaleServiceModel();
            serviceModel.setPrice(domainModel.getPrice());
            serviceModel.setQtyMax(domainModel.getQtyMax());
            serviceModel.setQtyMin(domainModel.getQtyMin());
            serviceModelList.add(serviceModel);
        }
        return serviceModelList;
    }
}
