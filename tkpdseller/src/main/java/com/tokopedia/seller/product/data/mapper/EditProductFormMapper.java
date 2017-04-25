package com.tokopedia.seller.product.data.mapper;

import android.text.TextUtils;

import com.tokopedia.seller.product.data.source.cloud.model.editproductform.DataEditProductForm;
import com.tokopedia.seller.product.data.source.cloud.model.editproductform.EditProductFormServiceModel;
import com.tokopedia.seller.product.data.source.cloud.model.editproductform.Etalase;
import com.tokopedia.seller.product.data.source.cloud.model.editproductform.ProductEditForm;
import com.tokopedia.seller.product.data.source.cloud.model.editproductform.ProductImage;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

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
        domainModel.setproductId(product.getProductId());
        domainModel.setProductPriceCurrency(Integer.parseInt(product.getProductCurrencyId()));
        domainModel.setProductDescription(product.getProductShortDesc());
        domainModel.setProductPrice(Double.parseDouble(product.getProductPrice()));
        domainModel.setProductMinOrder(Integer.parseInt(product.getProductMinOrder()));
        domainModel.setNameEditable(Integer.parseInt(product.getProductNameEditable()));
        domainModel.setProductWeight(Integer.parseInt(product.getProductWeight()));
        domainModel.setProductName(product.getProductName());

        domainModel.setProductWholesaleList(mapWholesale(data.getWholesalePriceList()));
        domainModel.setProductPhotos(mapPhotos(data.getProductImageList()));

        if (data.getPreorder() != null) {
            domainModel.setPoProcessType(data.getPreorder().getPreorderProcessTimeType());
            domainModel.setPoProcessValue(data.getPreorder().getPreorderProcessTime());
        }
        return domainModel;
    }

    private ProductPhotoListDomainModel mapPhotos(List<ProductImage> productImages) {
        ProductPhotoListDomainModel domainModels = new ProductPhotoListDomainModel();
        domainModels.setProductDefaultPicture(0);
        domainModels.setPhotos(mapListPhotos(productImages));
        return domainModels;
    }

    private List<ImageProductInputDomainModel> mapListPhotos(List<ProductImage> productImages) {
        List<ImageProductInputDomainModel> domainModels = new ArrayList<>();
        for (ProductImage image : productImages){
            ImageProductInputDomainModel domainModel = new ImageProductInputDomainModel();
            domainModel.setPicId(image.getImageId());
            domainModel.setUrl(image.getImageSrc());
            domainModel.setDescription(image.getImageDescription());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

    private List<ProductWholesaleDomainModel> mapWholesale(List<Object> wholesalePrice) {
        return null;
    }


}
