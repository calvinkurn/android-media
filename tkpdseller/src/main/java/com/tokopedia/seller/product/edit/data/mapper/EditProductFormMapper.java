package com.tokopedia.seller.product.edit.data.mapper;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.constant.UploadToTypeDef;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.DataEditProductForm;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.EditProductFormServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.ProductEditForm;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.ProductImage;
import com.tokopedia.seller.product.edit.data.source.cloud.model.editproductform.WholesalePrice;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormMapper implements Func1<EditProductFormServiceModel, UploadProductInputDomainModel> {
    @Inject
    public EditProductFormMapper() {
    }

    @Override
    public UploadProductInputDomainModel call(EditProductFormServiceModel serviceModel) {
        UploadProductInputDomainModel domainModel = new UploadProductInputDomainModel();
        DataEditProductForm data = serviceModel.getData();
        ProductEditForm product = data.getProduct();
        domainModel.setProductWeightUnit(Integer.parseInt(product.getProductWeightUnit()));
        if (!TextUtils.isEmpty(product.getProductEtalaseId())) {
            domainModel.setProductEtalaseId(Long.parseLong(product.getProductEtalaseId()));
        } else {
            domainModel.setProductEtalaseId(-1);
        }
        domainModel.setProductEtalaseName(product.getProductEtalase());
        domainModel.setProductCatalogId(data.getCatalog().getCatalogId());
        domainModel.setProductCatalogName(data.getCatalog().getCatalogName());
        domainModel.setProductCondition(Integer.parseInt(product.getProductCondition()));
        domainModel.setProductDepartmentId(Integer.parseInt(product.getProductDepartmentId()));
        domainModel.setProductMustInsurance(Integer.parseInt(product.getProductMustInsurance()));
        domainModel.setProductId(product.getProductId());
        domainModel.setProductPriceCurrency(Integer.parseInt(product.getProductCurrencyId()));
        domainModel.setProductDescription(product.getProductShortDesc());
        domainModel.setProductPrice(Double.parseDouble(product.getProductPrice()));
        domainModel.setProductMinOrder(Integer.parseInt(product.getProductMinOrder()));
        domainModel.setNameEditable(Integer.parseInt(product.getProductNameEditable()));
        domainModel.setProductWeight(Integer.parseInt(product.getProductWeight()));
        domainModel.setProductName(product.getProductName());
        domainModel.setProductReturnable(data.getInfo().getProductReturnable());
        @UploadToTypeDef int uploadTo = Integer.parseInt(product.getProductUploadTo());
        if (uploadTo == UploadToTypeDef.TYPE_WAREHOUSE) {
            uploadTo = UploadToTypeDef.TYPE_NOT_ACTIVE;
        }
        domainModel.setProductUploadTo(uploadTo);
        if (product.getProductInvenageSwitch() != null) {
            domainModel.setProductInvenageSwitch(Integer.parseInt(product.getProductInvenageSwitch()));
        }
        if (product.getProductInvenageValue() != null) {
            domainModel.setProductInvenageValue(Integer.parseInt(product.getProductInvenageValue()));
        }
        domainModel.setProductWholesaleList(mapWholesale(data.getWholesalePriceList()));
        domainModel.setProductPhotos(mapPhotos(data.getProductImageList()));

        domainModel.setProductStatus(ProductStatus.EDIT);

        if (data.getPreorder() != null) {
            domainModel.setPoProcessType(data.getPreorder().getPreorderProcessTimeType());
            domainModel.setPoProcessValue(data.getPreorder().getPreorderProcessTime());
        }
        return domainModel;
    }

    private ProductPhotoListDomainModel mapPhotos(List<ProductImage> productImages) {
        ProductPhotoListDomainModel domainModels = new ProductPhotoListDomainModel();
        List<ImageProductInputDomainModel> domainModelList = new ArrayList<>();
        for (int i = 0; i < productImages.size(); i ++){
            ImageProductInputDomainModel domainModel = new ImageProductInputDomainModel();
            ProductImage image = productImages.get(i);
            domainModel.setPicId(image.getImageId());
            domainModel.setUrl(image.getImageSrc());
            domainModel.setDescription(image.getImageDescription());
            domainModelList.add(domainModel);
            if (image.getImagePrimary() == 1){
                domainModels.setProductDefaultPicture(i);
            }
        }
        domainModels.setPhotos(domainModelList);
        return domainModels;
    }

    private List<ProductWholesaleDomainModel> mapWholesale(List<WholesalePrice> wholesalePrice) {
        List<ProductWholesaleDomainModel> wholesaleDomainModels
                = new ArrayList<>();
        for (WholesalePrice price : wholesalePrice) {
            ProductWholesaleDomainModel productWholesaleDomainModel
                    = new ProductWholesaleDomainModel();
            productWholesaleDomainModel.setPrice(Double.valueOf(price.getWholesalePrice()));
            productWholesaleDomainModel.setQtyMax(price.getWholesaleMax());
            productWholesaleDomainModel.setQtyMin(price.getWholesaleMin());

            wholesaleDomainModels.add(productWholesaleDomainModel);
        }

        return wholesaleDomainModels;
    }


}
