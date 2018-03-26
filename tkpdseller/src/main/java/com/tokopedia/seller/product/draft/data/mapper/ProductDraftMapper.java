package com.tokopedia.seller.product.draft.data.mapper;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.product.draft.data.source.db.model.ProductDraftModel;
import com.tokopedia.seller.product.edit.constant.FreeReturnTypeDef;
import com.tokopedia.seller.product.edit.constant.ProductInsuranceValueTypeDef;
import com.tokopedia.seller.product.edit.data.source.db.model.ImageProductInputDraftModel;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductWholesaleDraftModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCatalogViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductCategoryViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductEtalaseViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureResultUploadedViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPreOrderViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductVideoViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantcombination.ProductVariantCombinationViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionChild;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.variantoption.ProductVariantOptionParent;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantCombinationSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftMapper implements Func1<ProductDraftDataBase, ProductViewModel> {
    public static final String UTF_8 = "UTF-8";

    private static final int VERSION_PRODUCT_VIEW_MODEL = 1;

    public ProductDraftMapper() {
    }

    @Override
    public ProductViewModel call(ProductDraftDataBase productDraftDataBase) {
        ProductViewModel productViewModel;
        try {
            productViewModel = CacheUtil.convertStringToModel(
                    productDraftDataBase.getData(),
                    ProductViewModel.class
            );
        } catch (Exception e) {
            ProductDraftModel draftModel = CacheUtil.convertStringToModel(
                    productDraftDataBase.getData(),
                    ProductDraftModel.class
            );

            productViewModel = mapDraftToDomain(draftModel);
        }
        return productViewModel;
    }

    /**
     * convert the old-version draft to current model
     */
    private ProductViewModel mapDraftToDomain(ProductDraftModel draftModel) {
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setProductPictureViewModelList(mapPhotosDraftToProductViewModel(draftModel.getProductPhotos().getPhotos()));
        productViewModel.setProductWholesale(null);
        productViewModel.setProductVideo(mapToProductVideo(draftModel.getProductVideos()));
        productViewModel.setProductName(draftModel.getProductName());
        productViewModel.setProductDescription(draftModel.getProductDescription());
        productViewModel.setProductCatalog(generateCatalog(draftModel));
        productViewModel.setProductCategory(generateProductCategoryViewModel(draftModel));
        productViewModel.setProductCondition(draftModel.getProductCondition());
        productViewModel.setProductEtalase(mapToEtalase(draftModel));
        productViewModel.setProductMinOrder(draftModel.getProductMinOrder());
        productViewModel.setProductMustInsurance(mapToInsurance(draftModel.getProductMustInsurance()));
        productViewModel.setProductPrice(draftModel.getProductPrice());
        productViewModel.setProductPriceCurrency(draftModel.getProductPriceCurrency());
        productViewModel.setProductFreeReturn(mapToFreeReturn(draftModel.getProductReturnable()));
        productViewModel.setProductStatus(draftModel.getProductUploadTo());
        productViewModel.setProductStock(draftModel.getProductInvenageValue());
        productViewModel.setProductWeight(draftModel.getProductWeight());
        productViewModel.setProductWeightUnit(draftModel.getProductWeightUnit());
        productViewModel.setProductPreorder(generateProductPreorder(draftModel));
        productViewModel.setProductId(draftModel.getProductId());
        productViewModel.setProductNameEditable(draftModel.getProductNameEditable() != 0);

        productViewModel.setProductVariant(null);
        return productViewModel;
    }

    private ProductVariantViewModel mapProductDraftOldVersion(ProductDraftModel draftModel) {
        if (draftModel == null) {
            return null;
        }
        ProductVariantDataSubmit productVariantDataSubmit = draftModel.getProductVariantDataSubmit();
        if (productVariantDataSubmit == null) {
            return null;
        }
        ProductVariantViewModel productVariantViewModel = new ProductVariantViewModel();

        String level1 = "";
        String level2 = "";
        String variantStringSelection = draftModel.getVariantStringSelection();
        if (variantStringSelection.contains(" ")) { // means it is not "Pilih"
            String[] variantDimensionSplit = variantStringSelection.split("\n");

            if (variantDimensionSplit.length >= 1) {
                String[] dimen1Split = variantDimensionSplit[0].split(" ", 2);
                if (dimen1Split.length > 1) {
                    level1 = dimen1Split[1];
                }
            }
            if (variantDimensionSplit.length >= 2) {
                String[] dimen2Split = variantDimensionSplit[1].split(" ", 2);
                if (dimen2Split.length > 1) {
                    level2 = dimen2Split[1];
                }
            }
        }

        List<ProductVariantUnitSubmit> productVariantUnitSubmitList = productVariantDataSubmit.getProductVariantUnitSubmitList();
        if (productVariantUnitSubmitList != null && productVariantUnitSubmitList.size() > 0) {
            List<ProductVariantOptionParent> productVariantOptionParentList = new ArrayList<>();
            for (int i = 0, sizei = productVariantUnitSubmitList.size(); i < sizei; i++) {
                ProductVariantUnitSubmit productVariantUnitSubmit = productVariantUnitSubmitList.get(i);

                ProductVariantOptionParent productVariantOptionParent = new ProductVariantOptionParent();
                productVariantOptionParent.setPosition(productVariantUnitSubmit.getPosition());
                productVariantOptionParent.setV((int) productVariantUnitSubmit.getVariantId());
                productVariantOptionParent.setVu((int) productVariantUnitSubmit.getVariantUnitId());
                if (i == 0) {
                    productVariantOptionParent.setName(level1);
                } else {
                    productVariantOptionParent.setName(level2);
                }

                List<ProductVariantOptionSubmit> productVariantOptionSubmitList =
                        productVariantUnitSubmit.getProductVariantOptionSubmitList();
                List<ProductVariantOptionChild> productVariantOptionChildList = new ArrayList<>();
                if (productVariantOptionSubmitList != null) {
                    for (int j = 0, sizej = productVariantOptionSubmitList.size(); j < sizej; j++) {
                        ProductVariantOptionSubmit productVariantOptionSubmit = productVariantOptionSubmitList.get(j);

                        ProductVariantOptionChild productVariantOptionChild = new ProductVariantOptionChild(
                                0,
                                (int) (productVariantOptionSubmit.getVariantUnitValueId()),
                                productVariantOptionSubmit.getCustomText(),
                                "",
                                null
                        );
                        productVariantOptionChild.settId((int) productVariantOptionSubmit.getTemporaryId());
                        productVariantOptionChildList.add(productVariantOptionChild);
                    }
                }
                productVariantOptionParent.setProductVariantOptionChild(productVariantOptionChildList);
                productVariantOptionParentList.add(productVariantOptionParent);
            }
            productVariantViewModel.setVariantOptionParent(productVariantOptionParentList);
        }

        List<ProductVariantCombinationViewModel> productVariantCombinationViewModelList = new ArrayList<>();
        if (productVariantDataSubmit.getProductVariantCombinationSubmitList() != null &&
                productVariantDataSubmit.getProductVariantCombinationSubmitList().size() > 0) {
            List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList =
                    productVariantDataSubmit.getProductVariantCombinationSubmitList();
            for (int i = 0, sizei = productVariantCombinationSubmitList.size(); i < sizei; i++) {
                ProductVariantCombinationSubmit productVariantCombinationSubmit = productVariantCombinationSubmitList.get(i);
                if (productVariantCombinationSubmit == null) {
                    continue;
                }
                ProductVariantCombinationViewModel productVariantCombinationViewModel = new ProductVariantCombinationViewModel();
                productVariantCombinationViewModel.setActive(productVariantCombinationSubmit.getStatus() == 1);
                List<Integer> integerList = new ArrayList<>();
                for (int j = 0, sizej = productVariantCombinationSubmit.getOptionList().size(); j < sizej; j++) {
                    integerList.add((int)(long) productVariantCombinationSubmit.getOptionList().get(j));
                }
                productVariantCombinationViewModel.setOpt(integerList);
                productVariantCombinationViewModelList.add(productVariantCombinationViewModel);
            }
        }
        productVariantViewModel.setProductVariant(productVariantCombinationViewModelList);
        return productVariantViewModel;
    }

    private ProductPreOrderViewModel generateProductPreorder(ProductDraftModel draftModel) {
        ProductPreOrderViewModel productPreorderViewModel = new ProductPreOrderViewModel();
        productPreorderViewModel.setPreorderProcessTime(draftModel.getPoProcessValue());
        productPreorderViewModel.setPreorderTimeUnit(draftModel.getPoProcessType());
        return productPreorderViewModel;
    }

    private ProductCategoryViewModel generateProductCategoryViewModel(ProductDraftModel draftModel) {
        ProductCategoryViewModel productCategoryViewModel = new ProductCategoryViewModel();
        productCategoryViewModel.setCategoryId(draftModel.getProductDepartmentId());
        return productCategoryViewModel;
    }

    private @NonNull ProductCatalogViewModel generateCatalog(ProductDraftModel draftModel) {
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
        for (String url : productVideos) {
            ProductVideoViewModel productVideoViewModel = new ProductVideoViewModel(url);
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

    private List<ProductPictureViewModel> mapPhotosDraftToProductViewModel(List<ImageProductInputDraftModel> photos) {
        List<ProductPictureViewModel> productPictureViewModelList = new ArrayList<>();
        for (ImageProductInputDraftModel draftModel : photos) {
            ProductPictureViewModel productPictureViewModel = generateProductPictureModel(draftModel);
            if (productPictureViewModel!= null) {
                productPictureViewModelList.add(productPictureViewModel);
            }
        }
        return productPictureViewModelList;
    }

    private ProductPictureViewModel generateProductPictureModel(ImageProductInputDraftModel draftModel) {
        ProductPictureViewModel productPictureViewModel = null;
        try {
            if (draftModel!= null) {
                productPictureViewModel = new ProductPictureViewModel();
                productPictureViewModel.setDescription(draftModel.getDescription());
                if (TextUtils.isEmpty(draftModel.getPicId())) {
                    productPictureViewModel.setId(0);
                } else {
                    productPictureViewModel.setId(Long.parseLong(draftModel.getPicId()));
                }
                productPictureViewModel.setFilePath(draftModel.getImagePath());
                productPictureViewModel.setUrlThumbnail(draftModel.getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productPictureViewModel;
    }

    public static ProductPictureResultUploadedViewModel generatePicObj(String picObj) throws UnsupportedEncodingException {
        byte[] resultEncoded = Base64.decode(picObj, Base64.DEFAULT);
        Gson gson = new Gson();
        return gson.fromJson(new String(resultEncoded, UTF_8), ProductPictureResultUploadedViewModel.class);
    }

    public static String mapFromDomain(ProductViewModel domainModel) {
        return CacheUtil.convertModelToString(domainModel, new TypeToken<ProductViewModel>() {
        }.getType());
    }
}
