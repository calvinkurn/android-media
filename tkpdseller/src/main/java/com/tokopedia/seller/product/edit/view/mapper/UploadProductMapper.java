package com.tokopedia.seller.product.edit.view.mapper;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.upload.ImageProductInputViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductPhotoListViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.util.ProductVariantViewConverter;

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
        domainModel.setProductChangeWholesale(viewModel.getProductChangeWholesale());
        domainModel.setProductChangeCatalog(viewModel.getProductChangeCatalog());
        domainModel.setProductCatalogId(viewModel.getProductCatalogId());
        domainModel.setProductDepartmentId(viewModel.getProductDepartmentId());
        domainModel.setProductCondition(viewModel.getProductCondition());
        domainModel.setProductEtalaseId(viewModel.getProductEtalaseId());
        domainModel.setProductEtalaseName(viewModel.getProductEtalaseName());
        domainModel.setProductMinOrder(viewModel.getProductMinOrder());
        domainModel.setProductMustInsurance(viewModel.getProductMustInsurance());
        domainModel.setProductPrice(viewModel.getProductPrice());
        domainModel.setProductPriceCurrency(viewModel.getProductPriceCurrency());
        domainModel.setProductReturnable(viewModel.getProductReturnable());
        domainModel.setProductUploadTo(viewModel.getProductUploadTo());
        domainModel.setProductInvenageSwitch(viewModel.getProductInvenageSwitch());
        domainModel.setProductInvenageValue(viewModel.getProductInvenageValue());
        domainModel.setProductWeight(viewModel.getProductWeight());
        domainModel.setProductWeightUnit(viewModel.getProductWeightUnit());
        domainModel.setPoProcessType(viewModel.getPoProcessType());
        domainModel.setPoProcessValue(viewModel.getPoProcessValue());
        domainModel.setServerId(viewModel.getServerId());
        domainModel.setProductStatus(viewModel.getProductStatus());
        domainModel.setProductId(viewModel.getProductId());
        domainModel.setNameEditable(viewModel.getProductNameEditable());
        domainModel.setProductVariantDataSubmit(viewModel.getProductVariantDataSubmit());
        domainModel.setVariantStringSelection(viewModel.getVariantStringSelection());
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
        //set original product default picture
        for (int i=0, sizei = productPhotos.getPhotos().size(); i<sizei; i++) {
            if (! productPhotos.getPhotos().get(i).canDelete()) {
                domainModel.setOriginalProductDefaultPicture(i);
                break;
            }
        }
        return domainModel;
    }

    private static List<ImageProductInputDomainModel> mapPhotosViewToDomain(List<ImageProductInputViewModel> photos) {
        List<ImageProductInputDomainModel> domainModels = new ArrayList<>();
        for (ImageProductInputViewModel viewModel : photos){
            ImageProductInputDomainModel domainModel = new ImageProductInputDomainModel();
            domainModel.setImagePath(viewModel.getImagePath());
            domainModel.setStatus(viewModel.getStatus());
            domainModel.setUrl(viewModel.getUrl());
            domainModel.setDescription(viewModel.getImageDescription());
            domainModel.setPicId(viewModel.getPicId());
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
        viewModel.setProductNameEditable(domainModel.getNameEditable());
        viewModel.setProductDescription(domainModel.getProductDescription());
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
        viewModel.setProductInvenageSwitch(domainModel.getProductInvenageSwitch());
        viewModel.setProductInvenageValue(domainModel.getProductInvenageValue());
        viewModel.setProductWeight(domainModel.getProductWeight());
        viewModel.setProductWeightUnit(domainModel.getProductWeightUnit());
        viewModel.setPoProcessType(domainModel.getPoProcessType());
        viewModel.setPoProcessValue(domainModel.getPoProcessValue());
        viewModel.setServerId(domainModel.getServerId());
        viewModel.setProductId(domainModel.getProductId());
        ProductVariantDataSubmit productVariantDataSubmit = domainModel.getProductVariantDataSubmit();
        viewModel.setProductVariantData(productVariantDataSubmit);
        viewModel.setVariantStringSelection(domainModel.getVariantStringSelection());
        return viewModel;
    }

    private static List<ProductWholesaleViewModel> mapWholesaleDomainToView(List<ProductWholesaleDomainModel> productWholesaleList) {
        List<ProductWholesaleViewModel> wholesaleDomainModels
                = new ArrayList<>();
        for (ProductWholesaleDomainModel price : productWholesaleList) {
            ProductWholesaleViewModel productWholesaleDomainModel
                    = new ProductWholesaleViewModel();
            productWholesaleDomainModel.setPrice(Double.valueOf(price.getPrice()));
            productWholesaleDomainModel.setQtyMax(price.getQtyMax());
            productWholesaleDomainModel.setQtyMin(price.getQtyMin());

            wholesaleDomainModels.add(productWholesaleDomainModel);
        }

        return wholesaleDomainModels;
    }

    private static ProductPhotoListViewModel mapProductPhotoDomainToView(ProductPhotoListDomainModel productPhotos) {
        ProductPhotoListViewModel viewModel = new ProductPhotoListViewModel();
        viewModel.setProductDefaultPicture(productPhotos.getProductDefaultPicture());
        viewModel.setPhotos(mapPhotosListDomainToView(productPhotos.getPhotos()));
        //disallow primary image to delete.
        int originalProductDefaultPic = productPhotos.getOriginalProductDefaultPicture();
        if (!viewModel.getPhotos().isEmpty()) {
            if (originalProductDefaultPic > -1) {
                viewModel.getPhotos().get(originalProductDefaultPic).setCanDelete(false);
            } else {
                viewModel.getPhotos().get(productPhotos.getProductDefaultPicture()).setCanDelete(false);
            }
        }
        return viewModel;
    }

    private static List<ImageProductInputViewModel> mapPhotosListDomainToView(List<ImageProductInputDomainModel> photos) {
        List<ImageProductInputViewModel> viewModels = new ArrayList<>();
        for (ImageProductInputDomainModel domainModel : photos) {
            ImageProductInputViewModel viewModel = new ImageProductInputViewModel();
            viewModel.setImagePath(domainModel.getImagePath());
            viewModel.setUrl(domainModel.getUrl());
            viewModel.setStatus(domainModel.getStatus());
            viewModel.setImageDescription(domainModel.getDescription());
            viewModel.setPicId(domainModel.getPicId());
            viewModels.add(viewModel);
        }
        return viewModels;
    }
}
