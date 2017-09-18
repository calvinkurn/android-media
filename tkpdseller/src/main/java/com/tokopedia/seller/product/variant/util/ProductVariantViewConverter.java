package com.tokopedia.seller.product.variant.util;

import android.text.TextUtils;

import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantCombinationSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDashboardViewModel;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/27/17.
 */

public class ProductVariantViewConverter {

    private static final int NOT_AVAILABLE_POSITION = Integer.MIN_VALUE;

    private static final String VARIANT_TITLE_SEPARATOR = ",";
    /**
     * Generate variant dashboard view model, converting variant data to dashboard view list
     *
     * @param productVariantUnitSubmitList
     * @param productVariantCombinationSubmitList
     * @param productVariantByCatModelList
     * @return
     */
    public static List<ProductVariantDashboardViewModel> getGeneratedVariantDashboardViewModelList(
            List<ProductVariantUnitSubmit> productVariantUnitSubmitList,
            List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList,
            List<ProductVariantByCatModel> productVariantByCatModelList) {
        List<ProductVariantDashboardViewModel> variantDashboardViewModelList = new ArrayList<>();
        ProductVariantUnitSubmit productVariantUnitSubmit = getVariantUnitSubmitByLevel(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantUnitSubmitList);
        if (productVariantUnitSubmit != null) {
            for (ProductVariantOptionSubmit productVariantOptionSubmitLv1 : productVariantUnitSubmit.getProductVariantOptionSubmitList()) {
                ProductVariantDashboardViewModel productVariantDashboardViewModel = getGeneratedVariantManageViewModel(
                        productVariantOptionSubmitLv1, productVariantUnitSubmitList, productVariantCombinationSubmitList, productVariantByCatModelList);
                variantDashboardViewModelList.add(productVariantDashboardViewModel);
            }
        }
        return variantDashboardViewModelList;
    }

    /**
     * Generate Variant manage view model based on variant option level 1
     * if 2 level, the content will be list of variant option level 2 list
     * if 1 level, the content will be same as level 1 title
     *
     * @param variantOptionLv1
     * @param variantUnitList
     * @param variantCombinationList
     * @param variantByCatModelList
     * @return
     */
    private static ProductVariantDashboardViewModel getGeneratedVariantManageViewModel(
            ProductVariantOptionSubmit variantOptionLv1,
            List<ProductVariantUnitSubmit> variantUnitList,
            List<ProductVariantCombinationSubmit> variantCombinationList,
            List<ProductVariantByCatModel> variantByCatModelList) {
        ProductVariantDashboardViewModel variantManageViewModel = new ProductVariantDashboardViewModel();
        String title = getTitle(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantOptionLv1, variantByCatModelList);
        variantManageViewModel.setTemporaryId(variantOptionLv1.getTemporaryId());
        variantManageViewModel.setTitle(title);
        if (TextUtils.isEmpty(variantOptionLv1.getCustomText())) {
            // Check variant option title from server
            ProductVariantOption productVariantOption = getProductVariantByCatModelByVariantId(
                    ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantOptionLv1.getVariantUnitValueId(), variantByCatModelList);
            if (productVariantOption != null) {
                variantManageViewModel.setHexCode(productVariantOption.getHexCode());
                variantManageViewModel.setIconUrl(productVariantOption.getIcon());
                if (variantOptionLv1.getPictureItemList()!= null &&
                        variantOptionLv1.getPictureItemList().size() > 0) {
                    variantManageViewModel.setImageUrl(variantOptionLv1.getPictureItemList().get(0).getPictureUrl300());
                }
            }
        }
        variantManageViewModel.setStockAvailable(isContainVariantStatusByOptionId(variantOptionLv1.getTemporaryId(), variantCombinationList));
        ProductVariantUnitSubmit variantUnitLv2 = getVariantUnitSubmitByLevel(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, variantUnitList);
        if (variantUnitLv2 != null) {
            // Level 2
            List<ProductVariantOptionSubmit> productVariantOptionSubmitList = getPairingVariantSubmitOptionListBy(
                    variantOptionLv1.getTemporaryId(), variantUnitList, variantCombinationList);
            String contentText = getMultipleVariantOptionTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE,
                    productVariantOptionSubmitList, variantByCatModelList);
            variantManageViewModel.setContent(contentText);
        }
        return variantManageViewModel;
    }

    /**
     * Get all variant option for variant detail view
     *
     * @param level
     * @param productVariantUnitSubmitList
     * @param productVariantUnitList
     * @return
     */
    public static List<ProductVariantDetailViewModel> getProductVariantValueListForVariantDetail(
            int level, List<ProductVariantUnitSubmit> productVariantUnitSubmitList, List<ProductVariantUnit> productVariantUnitList) {
        List<ProductVariantDetailViewModel> productVariantDetailViewModelList = new ArrayList<>();
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = getAllVariantSubmitOptionListByLevel(level, productVariantUnitSubmitList);
        for (ProductVariantOptionSubmit productVariantOptionSubmit : productVariantOptionSubmitList) {
            String title = "";
            // If name already on custom text, add custom text on title list
            if (!TextUtils.isEmpty(productVariantOptionSubmit.getCustomText())) {
                title = productVariantOptionSubmit.getCustomText();
            } else {
                // If not, search and mapping with variant value list from server
                ProductVariantOption productVariantOptionTemp = ProductVariantUtils.getProductVariantValueByVariantUnitList(
                        productVariantOptionSubmit.getVariantUnitValueId(), productVariantUnitList);
                if (productVariantOptionTemp != null) {
                    title = productVariantOptionTemp.getValue();
                }
            }
            ProductVariantDetailViewModel productVariantDetailViewModel = new ProductVariantDetailViewModel(productVariantOptionSubmit.getTemporaryId(), title);
            productVariantDetailViewModelList.add(productVariantDetailViewModel);
        }
        return productVariantDetailViewModelList;
    }

    /**
     * Get all variant title with concat
     * eg 'red, brown, white, black'
     *
     * @param level
     * @param productVariantOptionSubmitList
     * @param productVariantByCatModelList
     * @return
     */
    public static String getMultipleVariantOptionTitle(int level, List<ProductVariantOptionSubmit> productVariantOptionSubmitList,
                                                       List<ProductVariantByCatModel> productVariantByCatModelList) {
        String title = "";
        for (ProductVariantOptionSubmit productVariantOptionSubmit : productVariantOptionSubmitList) {
            title = getAdditionalTitleText(title, getTitle(level, productVariantOptionSubmit, productVariantByCatModelList));
        }
        return title;
    }

    private static String getAdditionalTitleText(String title, String text) {
        if (!TextUtils.isEmpty(title)) {
            title += VARIANT_TITLE_SEPARATOR + " ";
        }
        title += text;
        return title;
    }

    public static String getTitle(int level, ProductVariantOptionSubmit productVariantOptionSubmit, List<ProductVariantByCatModel> productVariantByCatModelList) {
        String title = "";
        if (productVariantOptionSubmit == null) {
            return title;
        }
        if (TextUtils.isEmpty(productVariantOptionSubmit.getCustomText())) {
            // Check variant option title from server
            ProductVariantOption productVariantOption = getProductVariantByCatModelByVariantId(
                    level, productVariantOptionSubmit.getVariantUnitValueId(), productVariantByCatModelList);
            if (productVariantOption != null) {
                title = productVariantOption.getValue();
            }
        } else {
            // Get custom name
            title = productVariantOptionSubmit.getCustomText();
        }
        return title;
    }

    private static ProductVariantOption getProductVariantByCatModelByVariantId(
            long level, long optionId, List<ProductVariantByCatModel> productVariantByCatModelList) {
        for (ProductVariantByCatModel productVariantByCatModel : productVariantByCatModelList) {
            if (level == getVariantPositionByStatus(productVariantByCatModel.getStatus())) {
                List<ProductVariantUnit> productVariantUnitList = productVariantByCatModel.getUnitList();
                for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
                    List<ProductVariantOption> productVariantOptionList = productVariantUnit.getProductVariantOptionList();
                    for (ProductVariantOption productVariantOption : productVariantOptionList) {
                        if (productVariantOption.getValueId() == optionId) {
                            return productVariantOption;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static List<ProductVariantOptionSubmit> getPairingVariantSubmitOptionListBy(
            long optionIdSelected, List<ProductVariantUnitSubmit> productVariantUnitSubmitList, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList) {
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
        for (ProductVariantCombinationSubmit productVariantCombinationSubmit : productVariantCombinationSubmitList) {
            for (Long optionIdTemp : productVariantCombinationSubmit.getOptionList()) {
                if (optionIdTemp == optionIdSelected) {
                    long pairingId = ProductVariantUtils.getParingId(optionIdSelected, productVariantCombinationSubmit.getOptionList());
                    // Check pairing id
                    if (pairingId >= 0) {
                        ProductVariantOptionSubmit productVariantOptionSubmit = getVariantOptionById(pairingId, productVariantUnitSubmitList);
                        productVariantOptionSubmitList.add(productVariantOptionSubmit);
                    }
                }
            }
        }
        return productVariantOptionSubmitList;
    }

    private static ProductVariantOptionSubmit getVariantOptionById(long optionId, List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        for (ProductVariantUnitSubmit productVariantUnitSubmit : productVariantUnitSubmitList) {
            for (ProductVariantOptionSubmit productVariantOptionSubmit : productVariantUnitSubmit.getProductVariantOptionSubmitList()) {
                if (productVariantOptionSubmit.getTemporaryId() == optionId) {
                    return productVariantOptionSubmit;
                }
            }
        }
        return null;
    }

    /**
     * get the position of productVariantByCatModelList
     *
     * @param level                        level, currently has 2 depth
     * @param productVariantByCatModelList list to select
     * @return ProductVariantByCatModel for that level, example: Object for Color, Object for Size
     */
    public static ProductVariantByCatModel getProductVariantByCatModel(int level, List<ProductVariantByCatModel> productVariantByCatModelList) {
        for (ProductVariantByCatModel variantByCatModel : productVariantByCatModelList) {
            if (level == ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE && variantByCatModel.getStatus() == ProductVariantConstant.VARIANT_STATUS_TWO) {
                return variantByCatModel;
            } else if (level == ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE && variantByCatModel.getStatus() == ProductVariantConstant.VARIANT_STATUS_ONE) {
                return variantByCatModel;
            }
        }
        return null;
    }

    /**
     * Get variant position by status
     * eg status 2 means important variant = variant level 1
     * status 1 mean optional means selected sub variant (level 2)
     *
     * @param status
     * @return
     */
    public static int getVariantPositionByStatus(int status) {
        switch (status) {
            case ProductVariantConstant.VARIANT_STATUS_ONE:
                return ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE;
            case ProductVariantConstant.VARIANT_STATUS_TWO:
                return ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE;
        }
        return NOT_AVAILABLE_POSITION;
    }

    /**
     * Get Variant Unit by unit level
     * eg [
     * {"v":2,"vu":0,"pos":1,"pv":null,"opt":[{"pvo":0,"vuv":22,"t_id":1,"cstm":""},{"pvo":0,"vuv":23,"t_id":2,"cstm":""}]},
     * {"v":3,"vu":3,"pos":2,"pv":null,"opt":[{"pvo":0,"vuv":15,"t_id":3,"cstm":""},{"pvo":0,"vuv":16,"t_id":4,"cstm":""}]}]
     * level = 1, return {"v":2,"vu":0,"pos":1,"pv":null,"opt":[{"pvo":0,"vuv":22,"t_id":1,"cstm":""},{"pvo":0,"vuv":23,"t_id":2,"cstm":""}
     *
     * @param level
     * @param productVariantUnitSubmitList
     * @return
     */
    public static ProductVariantUnitSubmit getVariantUnitSubmitByLevel(int level, List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        int variantUnitSubmitSize = productVariantUnitSubmitList.size();
        for (int i = 0; i < variantUnitSubmitSize; i++) {
            ProductVariantUnitSubmit productVariantUnitSubmitTemp = productVariantUnitSubmitList.get(i);
            if (productVariantUnitSubmitTemp.getPosition() == level) {
                return productVariantUnitSubmitTemp;
            }
        }
        return null;
    }

    /**
     * Get all variant submit option by level
     * eg "variant": [
     * {"v": 2,"vu": 0,"pos": 1,"pv": null,"opt":[{"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""},{"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}]},
     * {"v": 3,"vu": 3,"pos": 2,"pv": null,"opt": [{"pvo": 0,"vuv": 15,"t_id": 3,"cstm": ""},{"pvo": 0,"vuv": 16,"t_id": 4,"cstm": ""}]}
     * level = 1, return [{"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""},{"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}]
     *
     * @param level
     * @param productVariantUnitSubmitList
     * @return
     */
    private static List<ProductVariantOptionSubmit> getAllVariantSubmitOptionListByLevel(int level, List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
        for (ProductVariantUnitSubmit productVariantUnitSubmit : productVariantUnitSubmitList) {
            if (productVariantUnitSubmit.getPosition() == level) {
                productVariantOptionSubmitList.addAll(productVariantUnitSubmit.getProductVariantOptionSubmitList());
            }
        }
        return productVariantOptionSubmitList;
    }

    /**
     * Check if variant status list contain selected option id
     * eg "product_variant": [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]},{"st": 1,"opt": [3,2]},{"st": 1,"opt": [4,2]}
     * is contain 3 ? true
     * is contain 6 ? false
     *
     * @param optionId
     * @param productVariantCombinationSubmitList
     * @return
     */
    public static boolean isContainVariantStatusByOptionId(long optionId, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitListTemp = getSelectedVariantStatusList(optionId, productVariantCombinationSubmitList);
        return productVariantCombinationSubmitListTemp.size() > 0;
    }

    /**
     * Get all selected variant status list
     * eg "product_variant": [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]},{"st": 1,"opt": [3,2]},{"st": 1,"opt": [4,2]}
     * optionId = 1: return [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]}]
     *
     * @param optionId
     * @param productVariantCombinationSubmitList
     * @return
     */
    private static List<ProductVariantCombinationSubmit> getSelectedVariantStatusList(long optionId, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitListTemp = new ArrayList<>();
        for (ProductVariantCombinationSubmit productVariantCombinationSubmit : productVariantCombinationSubmitList) {
            if (ProductVariantUtils.isVariantCombinationContainOptionId(optionId, productVariantCombinationSubmit.getOptionList())) {
                productVariantCombinationSubmitListTemp.add(productVariantCombinationSubmit);
            }
        }
        return productVariantCombinationSubmitListTemp;
    }
}
