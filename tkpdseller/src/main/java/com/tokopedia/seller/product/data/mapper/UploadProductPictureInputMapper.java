package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.seller.product.constant.ImageStatusTypeDef;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductPhotoListServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.ProductPhotoServiceModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class UploadProductPictureInputMapper {

    @Inject
    public UploadProductPictureInputMapper() {
    }

    public void map(AddProductPictureInputServiceModel serviceModel, UploadProductInputDomainModel domainModel) {
        serviceModel.setProductPhoto(mapPhotoModel(domainModel.getProductPhotos()));
        serviceModel.setServerId(domainModel.getServerId());
        serviceModel.setHostUrl(domainModel.getHostUrl());
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
            if (domainModel.getStatus() == ImageStatusTypeDef.WILL_BE_DELETED ||
                    domainModel.getStatus() == ImageStatusTypeDef.ALREADY_DELETED ||
                    domainModel.getStatus() == ImageStatusTypeDef.WILL_BE_UPLOADED) {
                ProductPhotoServiceModel serviceModel = new ProductPhotoServiceModel();
                serviceModel.setUrl(domainModel.getUrl());
                serviceModel.setPicureId(domainModel.getPicId());
                serviceModel.setDescription(domainModel.getDescription());
                serviceModelList.add(serviceModel);
            }
        }
        return serviceModelList;
    }
}
