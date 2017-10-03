package com.tokopedia.seller.product.draft.data.mapper;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.product.edit.data.source.db.model.ImageProductInputDraftModel;
import com.tokopedia.seller.product.draft.data.source.db.model.ProductDraftModel;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductPhotoListDraftModel;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductWholesaleDraftModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftMapper implements Func1<String, UploadProductInputDomainModel> {
    private final long draftId;

    public ProductDraftMapper(long draftId) {
        this.draftId = draftId;
    }

    @Override
    public UploadProductInputDomainModel call(String json) {
        ProductDraftModel draftModel = CacheUtil.convertStringToModel(
                json,
                ProductDraftModel.class
        );

        UploadProductInputDomainModel domainModel = mapDraftToDomain(draftModel);
        domainModel.setId(draftId);
        return domainModel;
    }

    private UploadProductInputDomainModel mapDraftToDomain(ProductDraftModel draftModel) {
        UploadProductInputDomainModel domainModel = new UploadProductInputDomainModel();
        domainModel.setProductPhotos(mapProductPhoto(draftModel.getProductPhotos()));
        domainModel.setProductWholesaleList(mapWholesaleDraftToDomain(draftModel.getProductWholesaleList()));
        domainModel.setProductChangeCatalog(draftModel.getProductChangeCatalog());
        domainModel.setProductChangeWholesale(draftModel.getProductChangeWholesale());
        domainModel.setProductVideos(draftModel.getProductVideos());
        domainModel.setProductName(draftModel.getProductName());
        domainModel.setProductDescription(draftModel.getProductDescription());
        domainModel.setProductChangePhoto(draftModel.getProductChangePhoto());
        domainModel.setProductCatalogId(draftModel.getProductCatalogId());
        domainModel.setProductDepartmentId(draftModel.getProductDepartmentId());
        domainModel.setProductCondition(draftModel.getProductCondition());
        domainModel.setProductEtalaseId(draftModel.getProductEtalaseId());
        domainModel.setProductEtalaseName(draftModel.getProductEtalaseName());
        domainModel.setProductMinOrder(draftModel.getProductMinOrder());
        domainModel.setProductMustInsurance(draftModel.getProductMustInsurance());
        domainModel.setProductPrice(draftModel.getProductPrice());
        domainModel.setProductPriceCurrency(draftModel.getProductPriceCurrency());
        domainModel.setProductReturnable(draftModel.getProductReturnable());
        domainModel.setProductUploadTo(draftModel.getProductUploadTo());
        domainModel.setProductInvenageSwitch(draftModel.getProductInvenageSwitch());
        domainModel.setProductInvenageValue(draftModel.getProductInvenageValue());
        domainModel.setProductWeight(draftModel.getProductWeight());
        domainModel.setProductWeightUnit(draftModel.getProductWeightUnit());
        domainModel.setPoProcessType(draftModel.getPoProcessType());
        domainModel.setPoProcessValue(draftModel.getPoProcessValue());
        domainModel.setServerId(draftModel.getServerId());
        domainModel.setProductStatus(draftModel.getProductStatus());
        domainModel.setProductId(draftModel.getProductId());
        domainModel.setNameEditable(draftModel.getProductNameEditable());
        domainModel.setProductVariantDataSubmit(draftModel.getProductVariantDataSubmit());
        domainModel.setVariantStringSelection(draftModel.getVariantStringSelection());
        return domainModel;
    }

    private List<ProductWholesaleDomainModel> mapWholesaleDraftToDomain(List<ProductWholesaleDraftModel> draftModelList) {
        List<ProductWholesaleDomainModel> domainModels = new ArrayList<>();
        for (ProductWholesaleDraftModel draftModel : draftModelList){
            ProductWholesaleDomainModel domainModel = new ProductWholesaleDomainModel();
            domainModel.setPrice(draftModel.getPrice());
            domainModel.setQtyMax(draftModel.getQtyMax());
            domainModel.setQtyMin(draftModel.getQtyMin());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

    private ProductPhotoListDomainModel mapProductPhoto(ProductPhotoListDraftModel draftModel) {
        ProductPhotoListDomainModel domainModel = new ProductPhotoListDomainModel();
        domainModel.setProductDefaultPicture(draftModel.getProductDefaultPicture());
        domainModel.setPhotos(mapPhotosDraftToDomain(draftModel.getPhotos()));
        domainModel.setOriginalProductDefaultPicture(draftModel.getOriProductDefaultPicture());
        return domainModel;
    }

    private List<ImageProductInputDomainModel> mapPhotosDraftToDomain(List<ImageProductInputDraftModel> photos) {
        List<ImageProductInputDomainModel> domainModels = new ArrayList<>();
        for (ImageProductInputDraftModel draftModel : photos){
            ImageProductInputDomainModel domainModel = new ImageProductInputDomainModel();
            domainModel.setDescription(draftModel.getDescription());
            domainModel.setImagePath(draftModel.getImagePath());
            domainModel.setUrl(draftModel.getUrl());
            domainModel.setPicId(draftModel.getPicId());
            domainModel.setStatus(draftModel.getStatus());
            domainModel.setPicObj(draftModel.getPicObj());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

    public static String mapFromDomain(UploadProductInputDomainModel domainModel) {
        ProductDraftModel productDraft = mapDomainToDraft(domainModel);
        return CacheUtil.convertModelToString(productDraft, new TypeToken<ProductDraftModel>() {
        }.getType());
    }

    private static ProductDraftModel mapDomainToDraft(UploadProductInputDomainModel domainModel) {
        ProductDraftModel draftModel = new ProductDraftModel();
        draftModel.setProductPhotos(mapProductPhoto(domainModel.getProductPhotos()));
        draftModel.setProductWholesaleList(mapWholesaleDomainToDraft(domainModel.getProductWholesaleList()));
        draftModel.setProductChangeWholesale(domainModel.getProductChangeWholesale());
        draftModel.setProductChangeCatalog(domainModel.getProductChangeCatalog());
        draftModel.setProductVideos(domainModel.getProductVideos());
        draftModel.setProductName(domainModel.getProductName());
        draftModel.setProductDescription(domainModel.getProductDescription());
        draftModel.setProductChangePhoto(domainModel.getProductChangePhoto());
        draftModel.setProductCatalogId(domainModel.getProductCatalogId());
        draftModel.setProductDepartmentId(domainModel.getProductDepartmentId());
        draftModel.setProductCondition(domainModel.getProductCondition());
        draftModel.setProductEtalaseId(domainModel.getProductEtalaseId());
        draftModel.setProductEtalaseName(domainModel.getProductEtalaseName());
        draftModel.setProductMinOrder(domainModel.getProductMinOrder());
        draftModel.setProductMustInsurance(domainModel.getProductMustInsurance());
        draftModel.setProductPrice(domainModel.getProductPrice());
        draftModel.setProductPriceCurrency(domainModel.getProductPriceCurrency());
        draftModel.setProductReturnable(domainModel.getProductReturnable());
        draftModel.setProductUploadTo(domainModel.getProductUploadTo());
        draftModel.setProductInvenageSwitch(domainModel.getProductInvenageSwitch());
        draftModel.setProductInvenageValue(domainModel.getProductInvenageValue());
        draftModel.setProductWeight(domainModel.getProductWeight());
        draftModel.setProductWeightUnit(domainModel.getProductWeightUnit());
        draftModel.setPoProcessType(domainModel.getPoProcessType());
        draftModel.setPoProcessValue(domainModel.getPoProcessValue());
        draftModel.setServerId(domainModel.getServerId());
        draftModel.setProductStatus(domainModel.getProductStatus());
        draftModel.setProductId(domainModel.getProductId());
        draftModel.setProductNameEditable(domainModel.getNameEditable());
        draftModel.setSwitchVariant(domainModel.getSwitchVariant());
        draftModel.setProductVariantDataSubmit(domainModel.getProductVariantDataSubmit());
        draftModel.setVariantStringSelection(domainModel.getVariantStringSelection());
        return draftModel;
    }

    private static List<ProductWholesaleDraftModel> mapWholesaleDomainToDraft(List<ProductWholesaleDomainModel> productWholesaleList) {
        if (productWholesaleList == null) {
            return new ArrayList<>();
        }
        List<ProductWholesaleDraftModel> draftModels = new ArrayList<>();
        for (ProductWholesaleDomainModel domainModel : productWholesaleList){
            ProductWholesaleDraftModel draftModel = new ProductWholesaleDraftModel();
            draftModel.setPrice(domainModel.getPrice());
            draftModel.setQtyMax(domainModel.getQtyMax());
            draftModel.setQtyMin(domainModel.getQtyMin());
            draftModels.add(draftModel);
        }
        return draftModels;
    }

    private static ProductPhotoListDraftModel mapProductPhoto(ProductPhotoListDomainModel productPhotos) {
        ProductPhotoListDraftModel draftModel = new ProductPhotoListDraftModel();
        draftModel.setProductDefaultPicture(productPhotos == null? 0: productPhotos.getProductDefaultPicture());
        draftModel.setOriProductDefaultPicture(productPhotos == null? 0:productPhotos.getOriginalProductDefaultPicture());
        draftModel.setPhotos(productPhotos == null?  new ArrayList<ImageProductInputDraftModel>():
                (mapPhotosDomainToDraft(productPhotos.getPhotos())));
        return draftModel;
    }

    private static List<ImageProductInputDraftModel> mapPhotosDomainToDraft(List<ImageProductInputDomainModel> photos) {
        List<ImageProductInputDraftModel> draftModels = new ArrayList<>();
        for (ImageProductInputDomainModel domainModel : photos){
            ImageProductInputDraftModel draftModel = new ImageProductInputDraftModel();
            draftModel.setDescription(domainModel.getDescription());
            draftModel.setUrl(domainModel.getUrl());
            draftModel.setImagePath(domainModel.getImagePath());
            draftModel.setPicId(domainModel.getPicId());
            draftModel.setStatus(domainModel.getStatus());
            draftModel.setPicObj(domainModel.getPicObj());
            draftModels.add(draftModel);
        }
        return draftModels;
    }
}
