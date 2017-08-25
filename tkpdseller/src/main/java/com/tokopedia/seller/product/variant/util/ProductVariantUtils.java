package com.tokopedia.seller.product.variant.util;

import android.text.TextUtils;
import android.util.SparseIntArray;

import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.Option;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantDatum;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantData;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantStatus;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantSubmitOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.view.model.ProductVariantDetailViewModel;
import com.tokopedia.seller.product.variant.view.model.ProductVariantManageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/19/17.
 */

public class ProductVariantUtils {

    private static final long NOT_AVAILABLE_OPTION_ID = Long.MIN_VALUE;
    private static final String VARIANT_TITLE_SEPARATOR = ",";
    private static final String SPLIT_DELIMITER = ":"; // this depends on the api.

    /**
     * Get all variant option for variant detail view
     *
     * @param level
     * @param variantUnitSubmitList
     * @param productVariantUnitList
     * @return
     */
    public static List<ProductVariantDetailViewModel> getProductVariantValueListForVariantDetail(
            int level, List<VariantUnitSubmit> variantUnitSubmitList, List<ProductVariantUnit> productVariantUnitList) {
        List<ProductVariantDetailViewModel> productVariantDetailViewModelList = new ArrayList<>();
        List<VariantSubmitOption> variantSubmitOptionList = getAllVariantSubmitOptionListByLevel(level, variantUnitSubmitList);
        for (VariantSubmitOption variantSubmitOption : variantSubmitOptionList) {
            String title = "";
            // If name already on custom text, add custom text on title list
            if (!TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
                title = variantSubmitOption.getCustomText();
            } else {
                // If not, search and mapping with variant value list from server
                ProductVariantValue productVariantValueTemp = getProductVariantValueByVariantUnit(
                        variantSubmitOption.getVariantUnitValueId(), productVariantUnitList);
                if (productVariantValueTemp != null) {
                    title = productVariantValueTemp.getValue();
                }
            }
            ProductVariantDetailViewModel productVariantDetailViewModel = new ProductVariantDetailViewModel(variantSubmitOption.getTemporaryId(), title);
            productVariantDetailViewModelList.add(productVariantDetailViewModel);
        }
        return productVariantDetailViewModelList;
    }

    
    public static String getMultipleVariantOptionTitle(int level, VariantUnitSubmit variantUnitSubmit,
                                                       List<ProductVariantByCatModel> productVariantByCatModelList) {
        String title = "";
        if (variantUnitSubmit == null) {
            return title;
        }
        return getMultipleVariantOptionTitle(level, variantUnitSubmit.getVariantSubmitOptionList(), productVariantByCatModelList);
    }

    public static String getMultipleVariantOptionTitle(int level, List<VariantSubmitOption> variantSubmitOptionList,
                                                       List<ProductVariantByCatModel> productVariantByCatModelList) {
        String title = "";
        for (VariantSubmitOption variantSubmitOption : variantSubmitOptionList) {
            title = getAdditionalTitleText(title, getTitle(level, variantSubmitOption, productVariantByCatModelList));
        }
        return title;
    }

    private static String getTitle(int level, VariantSubmitOption variantSubmitOption, List<ProductVariantByCatModel> productVariantByCatModelList) {
        String title = "";
        if (variantSubmitOption == null) {
            return title;
        }
        if (TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
            // Check variant option title from server
            ProductVariantValue productVariantValue = getProductVariantByCatModelByVariantId(
                    level, variantSubmitOption.getVariantUnitValueId(), productVariantByCatModelList);
            if (productVariantValue != null) {
                title = productVariantValue.getValue();
            }
        } else {
            // Get custom name
            title = variantSubmitOption.getCustomText();
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

    private static ProductVariantValue getProductVariantByCatModelByVariantId(
            long level, long optionId, List<ProductVariantByCatModel> productVariantByCatModelList) {
        for (ProductVariantByCatModel productVariantByCatModel : productVariantByCatModelList) {
            if (level == getVariantPositionByStatus(productVariantByCatModel.getStatus())) {
                List<ProductVariantUnit> productVariantUnitList = productVariantByCatModel.getUnitList();
                for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
                    List<ProductVariantValue> productVariantValueList = productVariantUnit.getProductVariantValueList();
                    for (ProductVariantValue productVariantValue : productVariantValueList) {
                        if (productVariantValue.getValueId() == optionId) {
                            return productVariantValue;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static ProductVariantValue getProductVariantValueByVariantUnit(long unitValueId, List<ProductVariantUnit> productVariantUnitList) {
        for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
            ProductVariantValue productVariantValue = getProductVariantValue(unitValueId, productVariantUnit.getProductVariantValueList());
            if (productVariantValue != null) {
                return productVariantValue;
            }
        }
        return null;
    }

    public static ProductVariantValue getProductVariantValue(long unitValueId, List<ProductVariantValue> productVariantValueList) {
        for (ProductVariantValue productVariantValue : productVariantValueList) {
            if (productVariantValue.getValueId() == unitValueId) {
                return productVariantValue;
            }
        }
        return null;
    }

    public static ProductVariantManageViewModel getProductVariantManageViewModel(
            VariantSubmitOption variantSubmitOption, VariantUnitSubmit variantUnitSubmit, List<ProductVariantByCatModel> productVariantByCatModelList) {
        ProductVariantManageViewModel productVariantManageViewModel = new ProductVariantManageViewModel();
        productVariantManageViewModel.setTemporaryId(variantSubmitOption.getTemporaryId());
        productVariantManageViewModel.setTitle(getTitle(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOption, productVariantByCatModelList));
        productVariantManageViewModel.setContent(getMultipleVariantOptionTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, variantUnitSubmit, productVariantByCatModelList));
        if (TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
            // Check variant option title from server
            ProductVariantValue productVariantValue = getProductVariantByCatModelByVariantId(
                    ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOption.getVariantUnitValueId(), productVariantByCatModelList);
            if (productVariantValue != null) {
                productVariantManageViewModel.setHexCode(productVariantValue.getHexCode());
                productVariantManageViewModel.setImageUrl(productVariantValue.getIcon());
            }
        }
        return productVariantManageViewModel;
    }

    public static List<ProductVariantManageViewModel> getProductVariantManageViewModelListOneLevel() {
        return null;
    }

    public static List<ProductVariantManageViewModel> getProductVariantManageViewModelListTwoLevel(
            List<VariantUnitSubmit> variantUnitSubmitList, List<VariantStatus> variantStatusList, List<ProductVariantByCatModel> productVariantByCatModelList) {
        List<ProductVariantManageViewModel> productVariantManageViewModelList = new ArrayList<>();
        VariantUnitSubmit variantUnitSubmitLv1 = getVariantUnitSubmit(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantUnitSubmitList);
        for (VariantSubmitOption variantSubmitOptionLv1 : variantUnitSubmitLv1.getVariantSubmitOptionList()) {
            ProductVariantManageViewModel productVariantManageViewModel = new ProductVariantManageViewModel();
            productVariantManageViewModel.setTemporaryId(variantSubmitOptionLv1.getTemporaryId());
            productVariantManageViewModel.setTitle(getTitle(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOptionLv1, productVariantByCatModelList));

            List<VariantSubmitOption> variantSubmitOptionList = getPairingVariantSubmitOptionListBy(variantSubmitOptionLv1.getTemporaryId(), variantUnitSubmitList, variantStatusList);
            String contentText = getMultipleVariantOptionTitle(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, variantSubmitOptionList, productVariantByCatModelList);

            productVariantManageViewModel.setContent(contentText);
            if (TextUtils.isEmpty(variantSubmitOptionLv1.getCustomText())) {
                // Check variant option title from server
                ProductVariantValue productVariantValue = getProductVariantByCatModelByVariantId(
                        ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOptionLv1.getVariantUnitValueId(), productVariantByCatModelList);
                if (productVariantValue != null) {
                    productVariantManageViewModel.setHexCode(productVariantValue.getHexCode());
                    productVariantManageViewModel.setImageUrl(productVariantValue.getIcon());
                }
            }
            productVariantManageViewModelList.add(productVariantManageViewModel);
        }
        return productVariantManageViewModelList;
    }

    private static List<VariantSubmitOption> getPairingVariantSubmitOptionListBy(
            long optionIdSelected, List<VariantUnitSubmit> variantUnitSubmitList, List<VariantStatus> variantStatusList) {
        List<VariantSubmitOption> variantSubmitOptionList = new ArrayList<>();
        for (VariantStatus variantStatus : variantStatusList) {
            for (Long optionIdTemp : variantStatus.getOptionList()) {
                if (optionIdTemp == optionIdSelected) {
                    long pairingId = getParingId(optionIdSelected, variantStatus.getOptionList());
                    // Check pairing id
                    if (pairingId >= 0) {
                        VariantSubmitOption variantSubmitOption = getVariantSubmitOptionById(pairingId, variantUnitSubmitList);
                        variantSubmitOptionList.add(variantSubmitOption);
                    }
                }
            }
        }
        return variantSubmitOptionList;
    }

    private static VariantSubmitOption getVariantSubmitOptionById(long optionId, List<VariantUnitSubmit> variantUnitSubmitList) {
        for (VariantUnitSubmit variantUnitSubmit : variantUnitSubmitList) {
            for (VariantSubmitOption variantSubmitOption : variantUnitSubmit.getVariantSubmitOptionList()) {
                if (variantSubmitOption.getTemporaryId() == optionId) {
                    return variantSubmitOption;
                }
            }
        }
        return null;
    }

    public static VariantUnitSubmit getVariantUnitSubmit(int level, List<VariantUnitSubmit> variantUnitSubmitList) {
        int variantUnitSubmitSize = variantUnitSubmitList.size();
        for (int i = 0; i < variantUnitSubmitSize; i++) {
            VariantUnitSubmit variantUnitSubmitTemp = variantUnitSubmitList.get(i);
            if (variantUnitSubmitTemp.getPosition() == level) {
                return variantUnitSubmitTemp;
            }
        }
        return null;
    }

    public static List<VariantStatus> getVariantStatusList(VariantUnitSubmit variantUnitSubmit) {
        List<VariantStatus> variantStatusList = new ArrayList<>();
        if (variantUnitSubmit == null) {
            return variantStatusList;
        }
        for (VariantSubmitOption variantSubmitOptionLv1 : variantUnitSubmit.getVariantSubmitOptionList()) {
            List<Long> optionList = new ArrayList<>();
            optionList.add(variantSubmitOptionLv1.getTemporaryId());
            VariantStatus variantStatus = new VariantStatus();
            variantStatus.setOptionList(optionList);
            variantStatusList.add(variantStatus);
        }
        return variantStatusList;
    }

    public static List<VariantStatus> getVariantStatusList(VariantUnitSubmit variantUnitSubmitLv1, VariantUnitSubmit variantUnitSubmitLv2) {
        List<VariantStatus> variantStatusList = new ArrayList<>();
        if (variantUnitSubmitLv1 == null || variantUnitSubmitLv2 == null) {
            return variantStatusList;
        }
        for (VariantSubmitOption variantSubmitOptionLv1 : variantUnitSubmitLv1.getVariantSubmitOptionList()) {
            for (VariantSubmitOption variantSubmitOptionLv2 : variantUnitSubmitLv2.getVariantSubmitOptionList()) {
                List<Long> optionList = new ArrayList<>();
                optionList.add(variantSubmitOptionLv1.getTemporaryId());
                optionList.add(variantSubmitOptionLv2.getTemporaryId());
                VariantStatus variantStatus = new VariantStatus();
                variantStatus.setOptionList(optionList);
                variantStatusList.add(variantStatus);
            }
        }
        return variantStatusList;
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

    public static int getVariantPositionByStatus(int status) {
        switch (status) {
            case ProductVariantConstant.VARIANT_STATUS_ONE:
                return ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE;
            case ProductVariantConstant.VARIANT_STATUS_TWO:
                return ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE;
        }
        return -1;
    }

    public static VariantData generateProductVariantSubmit(ProductVariantByPrdModel productVariantByPrdModel) {
        if (productVariantByPrdModel.getVariantOptionList() == null ||
                productVariantByPrdModel.getVariantOptionList().size() == 0) {
            // Means all the variants have been removed
            return null;
        }
        VariantData variantData = new VariantData();

        // generate Unit Submit List
        int tIdCounter = 1;
        SparseIntArray tempIdInverseMap = new SparseIntArray();

        // Maps the unit and unit value
        List<VariantUnitSubmit> variantUnitSubmitList = new ArrayList<>();
        List<VariantOption> variantOptionSourceList = productVariantByPrdModel.getVariantOptionList();
        for (int i = 0, sizei = variantOptionSourceList.size();
             i < sizei; i++) {
            VariantOption varianOptionSource = variantOptionSourceList.get(i);
            VariantUnitSubmit variantUnitSubmit = new VariantUnitSubmit();
            variantUnitSubmit.setVariantId(varianOptionSource.getvId());
            variantUnitSubmit.setVariantUnitId(varianOptionSource.getVuId());
            variantUnitSubmit.setPosition(varianOptionSource.getPosition());
            variantUnitSubmit.setProductVariant(varianOptionSource.getPvId());

            //set options
            List<VariantSubmitOption> variantSubmitOptionList = new ArrayList<>();
            List<Option> optionSourceList = varianOptionSource.getOptionList();
            for (int k = 0, sizek = optionSourceList.size(); k < sizek; k++) {
                Option optionSource = optionSourceList.get(k);
                VariantSubmitOption variantSubmitOption = new VariantSubmitOption();
                variantSubmitOption.setProductVariantOptionId(optionSource.getPvoId());
                String optionValue = optionSource.getValue();
                int pvoId = optionSource.getPvoId();
                long varUnitValId = optionSource.getVuvId();
                variantSubmitOption.setVariantUnitValueId(varUnitValId);
                variantSubmitOption.setProductVariantOptionId(pvoId);
                if (varUnitValId == 0) {
                    variantSubmitOption.setCustomText(optionValue);
                } else {
                    variantSubmitOption.setCustomText("");
                }
                variantSubmitOption.setTemporaryId(tIdCounter);
                // add to map, to enable inverse lookup later
                tempIdInverseMap.put(pvoId, tIdCounter);

                tIdCounter++;

                variantSubmitOptionList.add(variantSubmitOption);
            }
            variantUnitSubmit.setVariantSubmitOptionList(variantSubmitOptionList);
            variantUnitSubmitList.add(variantUnitSubmit);
        }
        variantData.setVariantUnitSubmitList(variantUnitSubmitList);

        // generate the variant status, merah+ukuran39=status true; putih+ukuran 41 status true; etc

        List<VariantStatus> variantStatusList = new ArrayList<>();
        // add the metrics status here
        List<VariantDatum> variantSourceDataList = productVariantByPrdModel.getVariantDataList();
        for (int i = 0, sizei = variantSourceDataList.size(); i < sizei; i++) {
            VariantDatum variantSourceDatum = variantSourceDataList.get(i);
            VariantStatus variantStatus = new VariantStatus();
            variantStatus.setPvd(variantSourceDatum.getPvdId());
            variantStatus.setStatus(variantSourceDatum.getStatus());
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

            variantStatus.setOptionList(optList);
            variantStatusList.add(variantStatus);
        }
        variantData.setVariantStatusList(variantStatusList);
        return variantData;
    }

    public static List<String> getTitleList(List<VariantSubmitOption> variantSubmitOptionList, List<ProductVariantUnit> productVariantUnitList) {
        List<String> titleList = new ArrayList<>();
        for (VariantSubmitOption variantSubmitOption : variantSubmitOptionList) {
            // If name already on custom text, add custom text on title list
            if (!TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
                titleList.add(variantSubmitOption.getCustomText());
            } else {
                // If not, search and mapping with variant value list from server
                ProductVariantValue productVariantValue = getProductVariantValueByVariantUnit(variantSubmitOption.getVariantUnitValueId(), productVariantUnitList);
                if (productVariantValue != null) {
                    titleList.add(productVariantValue.getValue());
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
     * @param variantUnitSubmitList
     * @return
     */
    public static List<VariantSubmitOption> getAllVariantSubmitOptionListByLevel(int level, List<VariantUnitSubmit> variantUnitSubmitList) {
        List<VariantSubmitOption> variantSubmitOptionList = new ArrayList<>();
        for (VariantUnitSubmit variantUnitSubmit : variantUnitSubmitList) {
            if (variantUnitSubmit.getPosition() == level) {
                variantSubmitOptionList.addAll(variantUnitSubmit.getVariantSubmitOptionList());
            }
        }
        return variantSubmitOptionList;
    }

    /**
     * Get list of pairing option id from varian status list
     * eg "product_variant": [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]},{"st": 1,"opt": [3,2]},{"st": 1,"opt": [4,2]}
     * optionId = 3: return [1,2]
     *
     * @param optionId
     * @param variantStatusList
     * @return
     */
    public static List<Long> getSelectedOptionIdList(long optionId, List<VariantStatus> variantStatusList) {
        List<Long> optionIdList = new ArrayList<>();
        for (VariantStatus variantStatus : variantStatusList) {
            if (isVariantStatusContainOptionId(optionId, variantStatus.getOptionList())) {
                optionIdList.add(getParingId(optionId, variantStatus.getOptionList()));
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
     * @param variantStatusList
     * @return
     */
    public static boolean isContainVariantStatusByOptionId(long optionId, List<VariantStatus> variantStatusList) {
        List<VariantStatus> variantStatusListTemp = getSelectedVariantStatusList(optionId, variantStatusList);
        return variantStatusListTemp.size() > 0;
    }

    /**
     * Get all selected variant status list
     * eg "product_variant": [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]},{"st": 1,"opt": [3,2]},{"st": 1,"opt": [4,2]}
     * optionId = 1: return [{"st": 1,"opt": [3,1]},{"st": 1,"opt": [4,1]}]
     *
     * @param optionId
     * @param variantStatusList
     * @return
     */
    public static List<VariantStatus> getSelectedVariantStatusList(long optionId, List<VariantStatus> variantStatusList) {
        List<VariantStatus> variantStatusListTemp = new ArrayList<>();
        for (VariantStatus variantStatus : variantStatusList) {
            if (isVariantStatusContainOptionId(optionId, variantStatus.getOptionList())) {
                variantStatusListTemp.add(variantStatus);
            }
        }
        return variantStatusListTemp;
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
    public static boolean isVariantStatusContainOptionId(long optionId, List<Long> optionList) {
        long pairingOptionId = getParingId(optionId, optionList);
        return pairingOptionId != NOT_AVAILABLE_OPTION_ID;
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
        return NOT_AVAILABLE_OPTION_ID;
    }
}