package com.tokopedia.seller.product.variant.util;

import android.text.TextUtils;
import android.util.SparseIntArray;

import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.Option;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantDatum;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantCombinationSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;
import com.tokopedia.seller.product.variant.view.model.ProductVariantManageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/19/17.
 */

public class ProductVariantUtils {

    private static final int NOT_AVAILABLE_POSITION = Integer.MIN_VALUE;

    private static final String VARIANT_TITLE_SEPARATOR = ",";
    private static final String SPLIT_DELIMITER = ":"; // this depends on the api.

    public static List<ProductVariantManageViewModel> getProductVariantManageViewModelListOneLevel() {
        return null;
    }

    public static List<ProductVariantManageViewModel> getProductVariantManageViewModelListTwoLevel(
            List<ProductVariantUnitSubmit> productVariantUnitSubmitList, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList, List<ProductVariantByCatModel> productVariantByCatModelList) {
        List<ProductVariantManageViewModel> productVariantManageViewModelList = new ArrayList<>();
        ProductVariantUnitSubmit productVariantUnitSubmitLv1 = getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantUnitSubmitList);
        for (ProductVariantOptionSubmit productVariantOptionSubmitLv1 : productVariantUnitSubmitLv1.getProductVariantOptionSubmitList()) {
            ProductVariantManageViewModel productVariantManageViewModel = new ProductVariantManageViewModel();
            productVariantManageViewModel.setTemporaryId(productVariantOptionSubmitLv1.getTemporaryId());
            productVariantManageViewModel.setTitle(getTitle(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantOptionSubmitLv1, productVariantByCatModelList));

            List<ProductVariantOptionSubmit> productVariantOptionSubmitList = getPairingVariantSubmitOptionListBy(productVariantOptionSubmitLv1.getTemporaryId(), productVariantUnitSubmitList, productVariantCombinationSubmitList);
            String contentText = getMultipleVariantOptionTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, productVariantOptionSubmitList, productVariantByCatModelList);

            productVariantManageViewModel.setContent(contentText);
            if (TextUtils.isEmpty(productVariantOptionSubmitLv1.getCustomText())) {
                // Check variant option title from server
                ProductVariantOption productVariantOption = getProductVariantByCatModelByVariantId(
                        ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantOptionSubmitLv1.getVariantUnitValueId(), productVariantByCatModelList);
                if (productVariantOption != null) {
                    productVariantManageViewModel.setHexCode(productVariantOption.getHexCode());
                    productVariantManageViewModel.setImageUrl(productVariantOption.getIcon());
                }
            }
            productVariantManageViewModelList.add(productVariantManageViewModel);
        }
        return productVariantManageViewModelList;
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
                ProductVariantOption productVariantOptionTemp = getProductVariantValueByVariantUnitList(
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

    private static String getTitle(int level, ProductVariantOptionSubmit productVariantOptionSubmit, List<ProductVariantByCatModel> productVariantByCatModelList) {
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

    private static String getAdditionalTitleText(String title, String text) {
        if (!TextUtils.isEmpty(title)) {
            title += VARIANT_TITLE_SEPARATOR + " ";
        }
        title += text;
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

    /**
     * Get ProductVariantOption from list of ProductVariantUnit based on id
     * eg "unit_id": 3,"name": "UK","short_name": "UK","values": [
     * {"value_id": 20,"value": "14","hex_code": "","icon": ""},{"value_id": 21,"value": "16","hex_code": "","icon": ""}]},
     * {"unit_id": 2,"name": "US","short_name": "US","values": [{"value_id": 8,"value": "0","hex_code": "","icon": ""}]}]
     * option Id = 20, return {"value_id": 20,"value": "14","hex_code": "","icon": ""}
     *
     * @param unitValueId
     * @param productVariantUnitList
     * @return
     */
    public static ProductVariantOption getProductVariantValueByVariantUnitList(long unitValueId, List<ProductVariantUnit> productVariantUnitList) {
        for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
            ProductVariantOption productVariantOption = getProductVariantValue(unitValueId, productVariantUnit.getProductVariantOptionList());
            if (productVariantOption != null) {
                return productVariantOption;
            }
        }
        return null;
    }

    /**
     * Get ProductVariantOption from list of ProductVariantOption based on id
     * eg [{"value_id": 22,"value": "Putih","hex_code": "#ffffff","icon": ""},{"value_id": 23,"value": "Hitam","hex_code": "#000000","},{"value_id": 24,"value": "Abu-abu","hex_code": "#a9a9a9","icon": ""}]
     * option Id = 22, return {"value_id": 22,"value": "Putih","hex_code": "#ffffff","icon": ""}
     *
     * @param unitValueId
     * @param productVariantOptionList
     * @return
     */
    public static ProductVariantOption getProductVariantValue(long unitValueId, List<ProductVariantOption> productVariantOptionList) {
        for (ProductVariantOption productVariantOption : productVariantOptionList) {
            if (productVariantOption.getValueId() == unitValueId) {
                return productVariantOption;
            }
        }
        return null;
    }

    public static ProductVariantManageViewModel getProductVariantManageViewModel(
            ProductVariantOptionSubmit productVariantOptionSubmit, ProductVariantUnitSubmit productVariantUnitSubmit, List<ProductVariantByCatModel> productVariantByCatModelList) {
        ProductVariantManageViewModel productVariantManageViewModel = new ProductVariantManageViewModel();
        productVariantManageViewModel.setTemporaryId(productVariantOptionSubmit.getTemporaryId());
        productVariantManageViewModel.setTitle(getTitle(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantOptionSubmit, productVariantByCatModelList));
        productVariantManageViewModel.setContent(getMultipleVariantOptionTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, productVariantUnitSubmit.getProductVariantOptionSubmitList(), productVariantByCatModelList));
        if (TextUtils.isEmpty(productVariantOptionSubmit.getCustomText())) {
            // Check variant option title from server
            ProductVariantOption productVariantOption = getProductVariantByCatModelByVariantId(
                    ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, productVariantOptionSubmit.getVariantUnitValueId(), productVariantByCatModelList);
            if (productVariantOption != null) {
                productVariantManageViewModel.setHexCode(productVariantOption.getHexCode());
                productVariantManageViewModel.setImageUrl(productVariantOption.getIcon());
            }
        }
        return productVariantManageViewModel;
    }

    private static List<ProductVariantOptionSubmit> getPairingVariantSubmitOptionListBy(
            long optionIdSelected, List<ProductVariantUnitSubmit> productVariantUnitSubmitList, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList) {
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
        for (ProductVariantCombinationSubmit productVariantCombinationSubmit : productVariantCombinationSubmitList) {
            for (Long optionIdTemp : productVariantCombinationSubmit.getOptionList()) {
                if (optionIdTemp == optionIdSelected) {
                    long pairingId = getParingId(optionIdSelected, productVariantCombinationSubmit.getOptionList());
                    // Check pairing id
                    if (pairingId >= 0) {
                        ProductVariantOptionSubmit productVariantOptionSubmit = getVariantSubmitOptionById(pairingId, productVariantUnitSubmitList);
                        productVariantOptionSubmitList.add(productVariantOptionSubmit);
                    }
                }
            }
        }
        return productVariantOptionSubmitList;
    }

    private static ProductVariantOptionSubmit getVariantSubmitOptionById(long optionId, List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        for (ProductVariantUnitSubmit productVariantUnitSubmit : productVariantUnitSubmitList) {
            for (ProductVariantOptionSubmit productVariantOptionSubmit : productVariantUnitSubmit.getProductVariantOptionSubmitList()) {
                if (productVariantOptionSubmit.getTemporaryId() == optionId) {
                    return productVariantOptionSubmit;
                }
            }
        }
        return null;
    }

    public static ProductVariantUnitSubmit getVariantUnitSubmit(int level, List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        int variantUnitSubmitSize = productVariantUnitSubmitList.size();
        for (int i = 0; i < variantUnitSubmitSize; i++) {
            ProductVariantUnitSubmit productVariantUnitSubmitTemp = productVariantUnitSubmitList.get(i);
            if (productVariantUnitSubmitTemp.getPosition() == level) {
                return productVariantUnitSubmitTemp;
            }
        }
        return null;
    }

    public static List<ProductVariantCombinationSubmit> getVariantStatusList(ProductVariantUnitSubmit productVariantUnitSubmit) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = new ArrayList<>();
        if (productVariantUnitSubmit == null) {
            return productVariantCombinationSubmitList;
        }
        for (ProductVariantOptionSubmit productVariantOptionSubmitLv1 : productVariantUnitSubmit.getProductVariantOptionSubmitList()) {
            List<Long> optionList = new ArrayList<>();
            optionList.add(productVariantOptionSubmitLv1.getTemporaryId());
            ProductVariantCombinationSubmit productVariantCombinationSubmit = new ProductVariantCombinationSubmit();
            productVariantCombinationSubmit.setOptionList(optionList);
            productVariantCombinationSubmitList.add(productVariantCombinationSubmit);
        }
        return productVariantCombinationSubmitList;
    }

    public static List<ProductVariantCombinationSubmit> getVariantStatusList(ProductVariantUnitSubmit productVariantUnitSubmitLv1, ProductVariantUnitSubmit productVariantUnitSubmitLv2) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = new ArrayList<>();
        if (productVariantUnitSubmitLv1 == null || productVariantUnitSubmitLv2 == null) {
            return productVariantCombinationSubmitList;
        }
        for (ProductVariantOptionSubmit productVariantOptionSubmitLv1 : productVariantUnitSubmitLv1.getProductVariantOptionSubmitList()) {
            for (ProductVariantOptionSubmit productVariantOptionSubmitLv2 : productVariantUnitSubmitLv2.getProductVariantOptionSubmitList()) {
                List<Long> optionList = new ArrayList<>();
                optionList.add(productVariantOptionSubmitLv1.getTemporaryId());
                optionList.add(productVariantOptionSubmitLv2.getTemporaryId());
                ProductVariantCombinationSubmit productVariantCombinationSubmit = new ProductVariantCombinationSubmit();
                productVariantCombinationSubmit.setOptionList(optionList);
                productVariantCombinationSubmitList.add(productVariantCombinationSubmit);
            }
        }
        return productVariantCombinationSubmitList;
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

    public static ProductVariantDataSubmit generateProductVariantSubmit(ProductVariantByPrdModel productVariantByPrdModel) {
        if (productVariantByPrdModel.getVariantOptionList() == null ||
                productVariantByPrdModel.getVariantOptionList().size() == 0) {
            // Means all the variants have been removed
            return null;
        }
        ProductVariantDataSubmit productVariantDataSubmit = new ProductVariantDataSubmit();

        // generate Unit Submit List
        int tIdCounter = 1;
        SparseIntArray tempIdInverseMap = new SparseIntArray();

        // Maps the unit and unit value
        List<ProductVariantUnitSubmit> productVariantUnitSubmitList = new ArrayList<>();
        List<VariantOption> variantOptionSourceList = productVariantByPrdModel.getVariantOptionList();
        for (int i = 0, sizei = variantOptionSourceList.size();
             i < sizei; i++) {
            VariantOption varianOptionSource = variantOptionSourceList.get(i);
            ProductVariantUnitSubmit productVariantUnitSubmit = new ProductVariantUnitSubmit();
            productVariantUnitSubmit.setVariantId(varianOptionSource.getvId());
            productVariantUnitSubmit.setVariantUnitId(varianOptionSource.getVuId());
            productVariantUnitSubmit.setPosition(varianOptionSource.getPosition());
            productVariantUnitSubmit.setProductVariant(varianOptionSource.getPvId());

            //set options
            List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
            List<Option> optionSourceList = varianOptionSource.getOptionList();
            for (int k = 0, sizek = optionSourceList.size(); k < sizek; k++) {
                Option optionSource = optionSourceList.get(k);
                ProductVariantOptionSubmit productVariantOptionSubmit = new ProductVariantOptionSubmit();
                productVariantOptionSubmit.setProductVariantOptionId(optionSource.getPvoId());
                String optionValue = optionSource.getValue();
                int pvoId = optionSource.getPvoId();
                long varUnitValId = optionSource.getVuvId();
                productVariantOptionSubmit.setVariantUnitValueId(varUnitValId);
                productVariantOptionSubmit.setProductVariantOptionId(pvoId);
                if (varUnitValId == 0) {
                    productVariantOptionSubmit.setCustomText(optionValue);
                } else {
                    productVariantOptionSubmit.setCustomText("");
                }
                productVariantOptionSubmit.setTemporaryId(tIdCounter);
                // add to map, to enable inverse lookup later
                tempIdInverseMap.put(pvoId, tIdCounter);

                tIdCounter++;

                productVariantOptionSubmitList.add(productVariantOptionSubmit);
            }
            productVariantUnitSubmit.setProductVariantOptionSubmitList(productVariantOptionSubmitList);
            productVariantUnitSubmitList.add(productVariantUnitSubmit);
        }
        productVariantDataSubmit.setProductVariantUnitSubmitList(productVariantUnitSubmitList);

        // generate the variant status, merah+ukuran39=status true; putih+ukuran 41 status true; etc

        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = new ArrayList<>();
        // add the metrics status here
        List<VariantDatum> variantSourceDataList = productVariantByPrdModel.getVariantDataList();
        for (int i = 0, sizei = variantSourceDataList.size(); i < sizei; i++) {
            VariantDatum variantSourceDatum = variantSourceDataList.get(i);
            ProductVariantCombinationSubmit productVariantCombinationSubmit = new ProductVariantCombinationSubmit();
            productVariantCombinationSubmit.setPvd(variantSourceDatum.getPvdId());
            productVariantCombinationSubmit.setStatus(variantSourceDatum.getStatus());
            List<Long> optList = new ArrayList<>();

            List<Long> sourceOptionList = variantSourceDatum.getOptionIdList();
            // TODO to remove the vcode, split by ":" will no longer used.
            if (sourceOptionList == null || sourceOptionList.size() == 0) {
                String vSourceCode = variantSourceDatum.getvCode();
                String[] vCodeSplit = vSourceCode.split(SPLIT_DELIMITER);
                for (String aVCodeSplit : vCodeSplit) {
                    long optTId = tempIdInverseMap.get(Integer.parseInt(aVCodeSplit));
                    optList.add(optTId);
                }
            } else {
                for (long sourceOptionId : sourceOptionList) {
                    long optTId = tempIdInverseMap.get((int) sourceOptionId);
                    optList.add(optTId);
                }
            }

            productVariantCombinationSubmit.setOptionList(optList);
            productVariantCombinationSubmitList.add(productVariantCombinationSubmit);
        }
        productVariantDataSubmit.setProductVariantCombinationSubmitList(productVariantCombinationSubmitList);
        return productVariantDataSubmit;
    }

    public static List<String> getTitleList(List<ProductVariantOptionSubmit> productVariantOptionSubmitList, List<ProductVariantUnit> productVariantUnitList) {
        List<String> titleList = new ArrayList<>();
        for (ProductVariantOptionSubmit productVariantOptionSubmit : productVariantOptionSubmitList) {
            // If name already on custom text, add custom text on title list
            if (!TextUtils.isEmpty(productVariantOptionSubmit.getCustomText())) {
                titleList.add(productVariantOptionSubmit.getCustomText());
            } else {
                // If not, search and mapping with variant value list from server
                ProductVariantOption productVariantOption = getProductVariantValueByVariantUnitList(productVariantOptionSubmit.getVariantUnitValueId(), productVariantUnitList);
                if (productVariantOption != null) {
                    titleList.add(productVariantOption.getValue());
                }
            }
        }
        return titleList;
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
    public static List<ProductVariantOptionSubmit> getAllVariantSubmitOptionListByLevel(int level, List<ProductVariantUnitSubmit> productVariantUnitSubmitList) {
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
        for (ProductVariantUnitSubmit productVariantUnitSubmit : productVariantUnitSubmitList) {
            if (productVariantUnitSubmit.getPosition() == level) {
                productVariantOptionSubmitList.addAll(productVariantUnitSubmit.getProductVariantOptionSubmitList());
            }
        }
        return productVariantOptionSubmitList;
    }

    /**
     * Get list of pairing option id from varian status list
     * eg "product_variant": [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]},{"st": 1,"opt": [3,2]},{"st": 1,"opt": [4,2]}
     * optionId = 3: return [1,2]
     *
     * @param optionId
     * @param productVariantCombinationSubmitList
     * @return
     */
    public static List<Long> getSelectedOptionIdList(long optionId, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList) {
        List<Long> optionIdList = new ArrayList<>();
        for (ProductVariantCombinationSubmit productVariantCombinationSubmit : productVariantCombinationSubmitList) {
            if (isVariantCombinationContainOptionId(optionId, productVariantCombinationSubmit.getOptionList())) {
                optionIdList.add(getParingId(optionId, productVariantCombinationSubmit.getOptionList()));
            }
        }
        return optionIdList;
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
    public static List<ProductVariantCombinationSubmit> getSelectedVariantStatusList(long optionId, List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitListTemp = new ArrayList<>();
        for (ProductVariantCombinationSubmit productVariantCombinationSubmit : productVariantCombinationSubmitList) {
            if (isVariantCombinationContainOptionId(optionId, productVariantCombinationSubmit.getOptionList())) {
                productVariantCombinationSubmitListTemp.add(productVariantCombinationSubmit);
            }
        }
        return productVariantCombinationSubmitListTemp;
    }


    /**
     * Check if Variant status contain option ID
     * eg "opt": [3,1]
     * is contain 3 ? true
     * is contain 6 ? false
     *
     * @param optionId
     * @param optionList
     * @return
     */
    public static boolean isVariantCombinationContainOptionId(long optionId, List<Long> optionList) {
        long pairingOptionId = getParingId(optionId, optionList);
        return pairingOptionId != ProductVariantConstant.NOT_AVAILABLE_OPTION_ID;
    }

    /**
     * Get pairing id from optionList
     * eg "opt": [3,1]
     * option Id = 3, return 1
     *
     * @param optionId
     * @param optionList
     * @return
     */
    private static long getParingId(long optionId, List<Long> optionList) {
        boolean containOptionId = false;
        for (Long optionIdTemp : optionList) {
            if (optionIdTemp == optionId) {
                containOptionId = true;
                break;
            }
        }
        if (containOptionId) {
            for (Long optionIdTemp : optionList) {
                if (optionIdTemp != optionId) {
                    return optionIdTemp;
                }
            }
        }
        return ProductVariantConstant.NOT_AVAILABLE_OPTION_ID;
    }

    /**
     * Remove variant option and variant combination data by option id
     *
     * @param optionId
     * @param variantData
     * @return
     */
    public static ProductVariantDataSubmit getRemovedVariantDataByOptionId(long optionId, ProductVariantDataSubmit variantData) {
        variantData.setProductVariantUnitSubmitList(getRemovedVariantUnitListByOptionId(optionId, variantData.getProductVariantUnitSubmitList()));
        variantData.setProductVariantCombinationSubmitList(getRemovedVariantCombinationListByOptionId(optionId, variantData.getProductVariantCombinationSubmitList()));
        return variantData;
    }

    /**
     * Remove variant option by option id and return removed variant list
     * eg [{"v": 2,"vu": 0,"pos": 1,"pv": null,"opt":[{"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""},{"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}]},
     * {"v": 3,"vu": 3,"pos": 2,"pv": null,"opt": [{"pvo": 0,"vuv": 15,"t_id": 3,"cstm": ""},{"pvo": 0,"vuv": 16,"t_id": 4,"cstm": ""}]}
     * option id(t_id) = 2,
     * return
     * [{"v": 2,"vu": 0,"pos": 1,"pv": null,"opt":[{"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""}]},
     * {"v": 3,"vu": 3,"pos": 2,"pv": null,"opt": [{"pvo": 0,"vuv": 15,"t_id": 3,"cstm": ""},{"pvo": 0,"vuv": 16,"t_id": 4,"cstm": ""}]}
     *
     * @param optionId
     * @param variantUnitSubmitList
     * @return
     */
    private static List<ProductVariantUnitSubmit> getRemovedVariantUnitListByOptionId(long optionId, List<ProductVariantUnitSubmit> variantUnitSubmitList) {
        ProductVariantUnitSubmit variantUnitSubmit = getVariantUnitFromOptionId(optionId, variantUnitSubmitList);
        ProductVariantOptionSubmit variantOptionSubmit = getVariantOptionFromOptionId(optionId, variantUnitSubmit.getProductVariantOptionSubmitList());
        variantUnitSubmit.getProductVariantOptionSubmitList().remove(variantOptionSubmit);
        return variantUnitSubmitList;
    }

    /**
     * Remove combination option based on option id
     * eg [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]},{"st": 1,"opt": [3,2]},{"st": 1,"opt": [4,2]}
     * optionId = 4, return [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [3,2]}}
     *
     * @param optionId
     * @param variantCombinationSubmitList
     * @return
     */
    private static List<ProductVariantCombinationSubmit> getRemovedVariantCombinationListByOptionId(long optionId, List<ProductVariantCombinationSubmit> variantCombinationSubmitList) {
        int variantCombinationSize = variantCombinationSubmitList.size();
        for (int i = variantCombinationSize - 1; i >= 0; i--) {
            ProductVariantCombinationSubmit variantCombination = variantCombinationSubmitList.get(i);
            if (isVariantCombinationContainOptionId(optionId, variantCombination.getOptionList())) {
                variantCombinationSubmitList.remove(i);
            }
        }
        return variantCombinationSubmitList;
    }

    /**
     * Get variant unit from variant unit list based on option id
     * eg [{"v": 2,"vu": 0,"pos": 1,"pv": null,"opt":[{"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""},{"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}]},
     * {"v": 3,"vu": 3,"pos": 2,"pv": null,"opt": [{"pvo": 0,"vuv": 15,"t_id": 3,"cstm": ""},{"pvo": 0,"vuv": 16,"t_id": 4,"cstm": ""}]}
     * option id(t_id) = 2,
     * return
     * {"v": 2,"vu": 0,"pos": 1,"pv": null,"opt":[{"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""},{"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}]}
     *
     * @param optionId
     * @param variantUnitSubmitList
     * @return
     */
    private static ProductVariantUnitSubmit getVariantUnitFromOptionId(long optionId, List<ProductVariantUnitSubmit> variantUnitSubmitList) {
        for (ProductVariantUnitSubmit productVariantUnitSubmit : variantUnitSubmitList) {
            ProductVariantOptionSubmit productVariantOptionSubmit = getVariantOptionFromOptionId(optionId, productVariantUnitSubmit.getProductVariantOptionSubmitList());
            if (productVariantOptionSubmit != null) {
                return productVariantUnitSubmit;
            }
        }
        return null;
    }

    /**
     * Get variant option from variant option list based on option id
     * eg {"pvo": 0,"vuv": 22,"t_id": 1,"cstm": ""},{"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}]
     * option id(t_id) = 2, return {"pvo": 0,"vuv": 23,"t_id": 2,"cstm": ""}
     *
     * @param optionId
     * @param variantOptionSubmitList
     * @return
     */
    private static ProductVariantOptionSubmit getVariantOptionFromOptionId(long optionId, List<ProductVariantOptionSubmit> variantOptionSubmitList) {
        for (ProductVariantOptionSubmit productVariantOptionSubmit : variantOptionSubmitList) {
            if (productVariantOptionSubmit.getTemporaryId() == optionId) {
                return productVariantOptionSubmit;
            }
        }
        return null;
    }

    /**
     * Remove and update variant combination list based on option id level 1 and param add (for level 1 only)
     * eg [{"st":1,"opt":[1]},{"st":1,"opt":[2]},{"st":1,"opt":[3]},{"st":1,"opt":[4]}]
     * option id lv 1 = 2, add = false
     * return [{"st":1,"opt":[1]},{"st":1,"opt":[3]},{"st":1,"opt":[4]}]
     *
     * @param optionIdLv1
     * @param currentVariantCombinationList
     * @return
     */
    public static List<ProductVariantCombinationSubmit> getUpdatedVariantCombinationList(
            long optionIdLv1, boolean add, List<ProductVariantCombinationSubmit> currentVariantCombinationList) {
        currentVariantCombinationList = getRemovedVariantCombinationListByOptionId(optionIdLv1, currentVariantCombinationList);
        if (add) {
            ProductVariantCombinationSubmit variantCombination = generateVariantCombination(optionIdLv1);
            List<ProductVariantCombinationSubmit> variantCombinationList = new ArrayList<>();
            variantCombinationList.add(variantCombination);
            currentVariantCombinationList.addAll(variantCombinationList);
        }

        return currentVariantCombinationList;
    }

    /**
     * Remove and update variant combination list based on option id level 1 and list of option id level 2
     * eg [{"st":1,"opt":[3,1]},{"st":1,"opt":[4,1]},{"st":1,"opt":[3,2]},{"st":1,"opt":[4,2]}]
     * option id lv 1 = 2, option id lv 2 list = [3]
     * return [{"st":1,"opt":[3,1]},{"st":1,"opt":[4,1]},{"st":1,"opt":[3,2]}]
     *
     * @param optionIdLv1
     * @param optionIdLv2List
     * @param currentVariantCombinationList
     * @return
     */
    public static List<ProductVariantCombinationSubmit> getUpdatedVariantCombinationList(
            long optionIdLv1, List<Long> optionIdLv2List, List<ProductVariantCombinationSubmit> currentVariantCombinationList) {
        currentVariantCombinationList = getRemovedVariantCombinationListByOptionId(optionIdLv1, currentVariantCombinationList);
        List<ProductVariantCombinationSubmit> variantCombinationList = getGeneratedVariantCombinationList(optionIdLv1, optionIdLv2List);
        currentVariantCombinationList.addAll(variantCombinationList);
        return currentVariantCombinationList;
    }

    /**
     * Generate variant combination list based on option lv 2 list
     * eg option lv 1 = 1, option lv 2 list = [4,5]
     * return [{"st":1,"opt":[1,4]},{"st":1,"opt":[1,5]}]
     *
     * @param optionIdLv1
     * @param optionIdLv2List
     * @return
     */
    private static List<ProductVariantCombinationSubmit> getGeneratedVariantCombinationList(long optionIdLv1, List<Long> optionIdLv2List) {
        List<ProductVariantCombinationSubmit> variantCombinationList = new ArrayList<>();
        for (long optionIdLv2 : optionIdLv2List) {
            ProductVariantCombinationSubmit variantCombination = generateVariantCombination(optionIdLv1, optionIdLv2);
            variantCombinationList.add(variantCombination);
        }
        return variantCombinationList;
    }

    /**
     * Generate variant combination from option lv 1 and lv 2
     * eg option lv 1 = 1
     * return {"st":1,"opt":[1]}
     *
     * @param optionIdLv1
     * @return
     */
    private static ProductVariantCombinationSubmit generateVariantCombination(long optionIdLv1) {
        ProductVariantCombinationSubmit variantCombination = new ProductVariantCombinationSubmit();
        variantCombination.setStatus(ProductVariantConstant.VARIANT_COMBINATION_STATUS_AVAILABLE);
        List<Long> optionIdList = new ArrayList<>();
        optionIdList.add(optionIdLv1);
        variantCombination.setOptionList(optionIdList);
        return variantCombination;
    }

    /**
     * Generate variant combination from option lv 1 and lv 2
     * eg option lv 1 = 1, option lv 2 = 2
     * return {"st":1,"opt":[1,2]}
     *
     * @param optionIdLv1
     * @param optionIdLv2
     * @return
     */
    private static ProductVariantCombinationSubmit generateVariantCombination(long optionIdLv1, long optionIdLv2) {
        ProductVariantCombinationSubmit variantCombination = new ProductVariantCombinationSubmit();
        variantCombination.setStatus(ProductVariantConstant.VARIANT_COMBINATION_STATUS_AVAILABLE);
        List<Long> optionIdList = new ArrayList<>();
        optionIdList.add(optionIdLv1);
        optionIdList.add(optionIdLv2);
        variantCombination.setOptionList(optionIdList);
        return variantCombination;
    }
}