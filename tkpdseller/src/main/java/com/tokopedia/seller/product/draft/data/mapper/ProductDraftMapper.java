package com.tokopedia.seller.product.draft.data.mapper;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.product.draft.data.source.db.model.ProductDraftModel;
import com.tokopedia.seller.product.edit.constant.FreeReturnTypeDef;
import com.tokopedia.seller.product.edit.constant.ProductInsuranceValueTypeDef;
import com.tokopedia.seller.product.edit.data.source.db.model.ImageProductInputDraftModel;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductPhotoListDraftModel;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductWholesaleDraftModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductWholesaleDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCatalogViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCategoryViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductEtalaseViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureResultUploadedViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreorderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftMapper implements Func1<ProductDraftDataBase, ProductViewModel> {
    public static final String UTF_8 = "UTF-8";
    private final long draftId;

    public ProductDraftMapper(long draftId) {
        this.draftId = draftId;
    }

    @Override
    public ProductViewModel call(ProductDraftDataBase productDraftDataBase) {
        if (productDraftDataBase.getVersion() < ProductDraftDataBase.CURRENT_VERSION) {
            ProductDraftModel draftModel = CacheUtil.convertStringToModel(
                    productDraftDataBase.getData(),
                    ProductDraftModel.class
            );

            ProductViewModel domainModel = mapDraftToDomain(draftModel);
            domainModel.setProductId(draftId);
            return domainModel;
        } else {
            return CacheUtil.convertStringToModel(
                    productDraftDataBase.getData(),
                    ProductViewModel.class
            );
        }
    }

    private ProductViewModel mapDraftToDomain(ProductDraftModel draftModel) {
        ProductViewModel domainModel = new ProductViewModel();
        domainModel.setProductPicture(mapPhotosDraftToDomain(draftModel.getProductPhotos().getPhotos()));
        domainModel.setProductWholesale(mapWholesaleDraftToDomain(draftModel.getProductWholesaleList()));
//        domainModel.setProductChangeCatalog(draftModel.getProductChangeCatalog());
//        domainModel.setProductChangeWholesale(draftModel.getProductChangeWholesale());
        domainModel.setProductVideo(mapToProductVideo(draftModel.getProductVideos()));
        domainModel.setProductName(draftModel.getProductName());
        domainModel.setProductDescription(draftModel.getProductDescription());
//        domainModel.setProductChangePhoto(draftModel.getProductChangePhoto());
        domainModel.setProductCatalog(generateCatalog(draftModel));
        domainModel.setProductCategory(generateProductCategoryViewModel(draftModel));
        domainModel.setProductCondition(draftModel.getProductCondition());
        domainModel.setProductEtalase(mapToEtalase(draftModel));
        domainModel.setProductMinOrder(draftModel.getProductMinOrder());
        domainModel.setProductMustInsurance(mapToInsurance(draftModel.getProductMustInsurance()));
        domainModel.setProductPrice(draftModel.getProductPrice());
        domainModel.setProductPriceCurrency(draftModel.getProductPriceCurrency());
        domainModel.setProductFreeReturn(mapToFreeReturn(draftModel.getProductReturnable()));
//        domainModel.setProductStatus(draftModel.getProductUploadTo());
//        domainModel.setProductInvenageSwitch(draftModel.getProductInvenageSwitch());
//        domainModel.setProductInvenageValue(draftModel.getProductInvenageValue());
        domainModel.setProductWeight(draftModel.getProductWeight());
        domainModel.setProductWeightUnit(draftModel.getProductWeightUnit());
        domainModel.setProductPreorder(generateProductPreorder(draftModel));
//        domainModel.setServerId(draftModel.getServerId());
        domainModel.setProductStatus(draftModel.getProductStatus());
        domainModel.setProductId(Long.parseLong(draftModel.getProductId()));
        domainModel.setProductNameEditable(draftModel.getProductNameEditable() != 0);
//        domainModel.setProductVariantDataSubmit(draftModel.getProductVariantDataSubmit());
//        domainModel.setVariantStringSelection(draftModel.getVariantStringSelection());
        return domainModel;
    }

    private ProductPreorderViewModel generateProductPreorder(ProductDraftModel draftModel) {
        ProductPreorderViewModel productPreorderViewModel= new ProductPreorderViewModel();
        productPreorderViewModel.setPreorderProcessTime(draftModel.getPoProcessValue());
        productPreorderViewModel.setPreorderTimeUnit(draftModel.getPoProcessType());
        return productPreorderViewModel;
    }

    private ProductCategoryViewModel generateProductCategoryViewModel(ProductDraftModel draftModel) {
        ProductCategoryViewModel productCategoryViewModel = new ProductCategoryViewModel();
        productCategoryViewModel.setCategoryId(draftModel.getProductDepartmentId());
        return productCategoryViewModel;
    }

    private ProductCatalogViewModel generateCatalog(ProductDraftModel draftModel) {
        ProductCatalogViewModel productCatalogViewModel = new ProductCatalogViewModel();
        productCatalogViewModel.setCatalogId(draftModel.getProductCatalogId());
        return productCatalogViewModel;
    }

    private boolean mapToFreeReturn(int productReturnable) {
        return productReturnable == FreeReturnTypeDef.TYPE_ACTIVE;
    }

    private boolean mapToInsurance(int productMustInsurance) {
        return productMustInsurance == ProductInsuranceValueTypeDef.TYPE_YES;
    }

    private ProductEtalaseViewModel mapToEtalase(ProductDraftModel draftModel) {
        ProductEtalaseViewModel productEtalaseViewModel = new ProductEtalaseViewModel();
        productEtalaseViewModel.setEtalaseId(draftModel.getProductEtalaseId());
        productEtalaseViewModel.setEtalaseName(draftModel.getProductEtalaseName());
        return productEtalaseViewModel;
    }

    private List<ProductVideoViewModel> mapToProductVideo(List<String> productVideos) {
        List<ProductVideoViewModel> productVideoViewModels = new ArrayList<>();
        for(String url : productVideos){
            ProductVideoViewModel productVideoViewModel = new ProductVideoViewModel();
            productVideoViewModel.setUrl(url);
            productVideoViewModels.add(productVideoViewModel);
        }
        return productVideoViewModels;
    }

    private List<ProductWholesaleViewModel> mapWholesaleDraftToDomain(List<ProductWholesaleDraftModel> draftModelList) {
        List<ProductWholesaleViewModel> domainModels = new ArrayList<>();
        for (ProductWholesaleDraftModel draftModel : draftModelList) {
            ProductWholesaleViewModel domainModel = new ProductWholesaleViewModel();
            domainModel.setPriceValue(draftModel.getPrice());
            domainModel.setMinQty(draftModel.getQtyMin());
            domainModels.add(domainModel);
        }
        return domainModels;
    }

    private List<ProductPictureViewModel> mapPhotosDraftToDomain(List<ImageProductInputDraftModel> photos) {
        List<ProductPictureViewModel> domainModels = new ArrayList<>();
        for (ImageProductInputDraftModel draftModel : photos) {
            ProductPictureViewModel productPictureViewModel = generateProductPictureModel(draftModel);
            domainModels.add(productPictureViewModel);
        }
        return domainModels;
    }

    private ProductPictureViewModel generateProductPictureModel(ImageProductInputDraftModel draftModel) {
        ProductPictureViewModel productPictureViewModel = new ProductPictureViewModel();
        productPictureViewModel.setDescription(draftModel.getDescription());
        try {
            ProductPictureResultUploadedViewModel resultUploadedViewModel = generatePicObj(draftModel.getPicObj());
            productPictureViewModel.setY(Long.parseLong(resultUploadedViewModel.getH()));
            productPictureViewModel.setX(Long.parseLong(resultUploadedViewModel.getW()));
            productPictureViewModel.setFilePath(resultUploadedViewModel.getFilePath());
            productPictureViewModel.setFileName(resultUploadedViewModel.getFileName());
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return productPictureViewModel;
    }

    public static ProductPictureResultUploadedViewModel generatePicObj(String picObj) throws UnsupportedEncodingException {
        byte[] resultEncoded = Base64.decode(picObj, Base64.DEFAULT);
        Gson gson = new Gson();
        ProductPictureResultUploadedViewModel resultUploadedViewModel = gson.fromJson(new String(resultEncoded, UTF_8), ProductPictureResultUploadedViewModel.class);
        return resultUploadedViewModel;
    }

    public static String mapFromDomain(ProductViewModel domainModel) {
        return CacheUtil.convertModelToString(domainModel, new TypeToken<ProductViewModel>() {
        }.getType());
    }
}
