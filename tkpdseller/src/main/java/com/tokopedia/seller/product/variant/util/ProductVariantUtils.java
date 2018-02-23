package com.tokopedia.seller.product.variant.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.tokopedia.seller.product.variant.constant.ProductVariantConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantOption;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.Option;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.VariantDatum;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.VariantOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantCombinationSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantOptionSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantUnitSubmit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/19/17.
 */
@Deprecated
public class ProductVariantUtils {

    private static final String SPLIT_DELIMITER = ":"; // this depends on the api.

    /**
     * Convert previous variant data from server to cached variant data
     *
     * @param productVariantByPrdModel
     * @return
     */
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

            //set options
            List<ProductVariantOptionSubmit> productVariantOptionSubmitList = new ArrayList<>();
            List<Option> optionSourceList = varianOptionSource.getOptionList();
            for (int k = 0, sizek = optionSourceList.size(); k < sizek; k++) {
                Option optionSource = optionSourceList.get(k);
                ProductVariantOptionSubmit productVariantOptionSubmit = new ProductVariantOptionSubmit();
                String optionValue = optionSource.getValue();
                int pvoId = optionSource.getPvoId();
                long varUnitValId = optionSource.getVuvId();
                productVariantOptionSubmit.setVariantUnitValueId(varUnitValId);
                if (varUnitValId == 0) {
                    productVariantOptionSubmit.setCustomText(optionValue);
                } else {
                    productVariantOptionSubmit.setCustomText("");
                }
                productVariantOptionSubmit.setTemporaryId(tIdCounter);
                productVariantOptionSubmit.setPictureItemList(optionSource.getPicture());
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

    /**
     * Get ProductVariantOption from list of ProductVariantUnit based on id
     * eg "unit_id": 3,"name": "UK","short_name": "UK","values": [
     * {"value_id": 20,"value": "14","hex_code": "","icon": ""},{"value_id": 21,"value": "16","hex_code": "","icon": ""}]},
     * {"unit_id": 2,"name": "US","short_name": "US","values": [{"value_id": 8,"value": "0","hex_code": "","icon": ""}]}]
     * option Id = 20, return {"value_id": 20,"value": "14","hex_code": "","icon": ""}
     *
     * @param optionId
     * @param productVariantUnitList
     * @return
     */
    public static ProductVariantOption getProductVariantValueByVariantUnitList(long optionId, List<ProductVariantUnit> productVariantUnitList) {
        for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
            ProductVariantOption productVariantOption = getProductVariantValue(optionId, productVariantUnit.getProductVariantOptionList());
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

    /**
     * Update variant combination list value from new variant option list value (new/removed option value from variant option picker)
     * -Find removed variant option list, remove from combination list
     * -Find added variant option list, add to combination list based on how many level applied
     * -Remove unnecessary variant combination level
     *
     * @param oldVariantOptionSubmitList
     * @param newVariantOptionSubmitList
     * @param variantCombinationSubmitList
     * @param otherLevelVariantOptionSubmitList
     * @return
     */
    public static List<ProductVariantCombinationSubmit> getAddedVariantCombinationList(
            List<ProductVariantOptionSubmit> oldVariantOptionSubmitList, List<ProductVariantOptionSubmit> newVariantOptionSubmitList,
            List<ProductVariantCombinationSubmit> variantCombinationSubmitList,
            List<ProductVariantOptionSubmit> otherLevelVariantOptionSubmitList) {
        if (variantCombinationSubmitList == null) {
            variantCombinationSubmitList = new ArrayList<>();
        }
        List<ProductVariantOptionSubmit> removedVariantOptionList = getDiffVariantOptionList(oldVariantOptionSubmitList, newVariantOptionSubmitList);
        for (ProductVariantOptionSubmit productVariantOptionSubmit : removedVariantOptionList) {
            variantCombinationSubmitList = getRemovedVariantCombinationListByOptionId(productVariantOptionSubmit.getTemporaryId(), variantCombinationSubmitList);
        }
        List<ProductVariantOptionSubmit> addedVariantOptionList = getDiffVariantOptionList(newVariantOptionSubmitList, oldVariantOptionSubmitList);
        List<ProductVariantCombinationSubmit> addedProductVariantCombinationSubmitList;
        if (otherLevelVariantOptionSubmitList == null || otherLevelVariantOptionSubmitList.size() <= 0) {
            // Only 1 level, remove combination with 2 level
            addedProductVariantCombinationSubmitList = getGeneratedVariantCombinationList(addedVariantOptionList);
            variantCombinationSubmitList = getRemovedLevelVariantCombination(ProductVariantConstant.VARIANT_LEVEL_TWO_VALUE, variantCombinationSubmitList);
        } else {
            addedProductVariantCombinationSubmitList = getGeneratedVariantCombinationList(addedVariantOptionList, otherLevelVariantOptionSubmitList);
            variantCombinationSubmitList = getRemovedLevelVariantCombination(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantCombinationSubmitList);
        }
        if (addedProductVariantCombinationSubmitList != null) {
            variantCombinationSubmitList.addAll(addedProductVariantCombinationSubmitList);
        }
        return variantCombinationSubmitList;
    }

    /**
     * Get list of differentiate variant option list
     *
     * @param searchFromVariantOptionSubmitList
     * @param compareToVariantOptionSubmitList1
     * @return
     */
    private static List<ProductVariantOptionSubmit> getDiffVariantOptionList(
            List<ProductVariantOptionSubmit> searchFromVariantOptionSubmitList, List<ProductVariantOptionSubmit> compareToVariantOptionSubmitList1) {
        List<ProductVariantOptionSubmit> diffVariantOptionSubmitList = new ArrayList<>();
        for (ProductVariantOptionSubmit productVariantOptionSubmit : searchFromVariantOptionSubmitList) {
            if (getVariantOptionByOptionId(productVariantOptionSubmit.getTemporaryId(), compareToVariantOptionSubmitList1) == null) {
                diffVariantOptionSubmitList.add(productVariantOptionSubmit);
            }
        }
        return diffVariantOptionSubmitList;
    }


    /**
     * Remove unused variant combination that changed from level 1 to 2 or 2 to 1
     * eg [{"st":1,"opt":[3,1]},{"st":1,"opt":[4,1]},{"st":1,"opt":[3]},{"st":1,"opt":[4]}]
     * level = 2, return [{"st":1,"opt":[3,1]},{"st":1,"opt":[4,1]}]
     *
     * @param level
     * @param variantCombinationSubmitList
     * @return
     */
    private static List<ProductVariantCombinationSubmit> getRemovedLevelVariantCombination(int level, List<ProductVariantCombinationSubmit> variantCombinationSubmitList) {
        if (variantCombinationSubmitList == null) {
            variantCombinationSubmitList = new ArrayList<>();
        }
        int variantCombinationSize = variantCombinationSubmitList.size();
        for (int i = variantCombinationSize - 1; i >= 0; i--) {
            ProductVariantCombinationSubmit productVariantCombinationSubmit = variantCombinationSubmitList.get(i);
            if (productVariantCombinationSubmit.getOptionList().size() == level) {
                variantCombinationSubmitList.remove(i);
            }
        }
        return variantCombinationSubmitList;
    }

    /**
     * Generate default variant combination based on variant unit submit lv 1
     *
     * @param variantOptionSubmitList
     * @return
     */
    private static List<ProductVariantCombinationSubmit> getGeneratedVariantCombinationList(List<ProductVariantOptionSubmit> variantOptionSubmitList) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = new ArrayList<>();
        if (variantOptionSubmitList == null) {
            return productVariantCombinationSubmitList;
        }
        for (ProductVariantOptionSubmit productVariantOptionSubmitLv1 : variantOptionSubmitList) {
            ProductVariantCombinationSubmit productVariantCombinationSubmit = generateVariantCombination(productVariantOptionSubmitLv1.getTemporaryId());
            productVariantCombinationSubmitList.add(productVariantCombinationSubmit);
        }
        return productVariantCombinationSubmitList;
    }

    /**
     * Generate default variant combination based on variant unit submit lv 1 and lv 2
     *
     * @param productVariantUnitSubmitLv1List
     * @param productVariantUnitSubmitLv2List
     * @return
     */
    private static List<ProductVariantCombinationSubmit> getGeneratedVariantCombinationList(
            List<ProductVariantOptionSubmit> productVariantUnitSubmitLv1List, List<ProductVariantOptionSubmit> productVariantUnitSubmitLv2List) {
        List<ProductVariantCombinationSubmit> productVariantCombinationSubmitList = new ArrayList<>();
        for (ProductVariantOptionSubmit productVariantOptionSubmitLv1 : productVariantUnitSubmitLv1List) {
            for (ProductVariantOptionSubmit productVariantOptionSubmitLv2 : productVariantUnitSubmitLv2List) {
                ProductVariantCombinationSubmit productVariantCombinationSubmit =
                        generateVariantCombination(productVariantOptionSubmitLv1.getTemporaryId(), productVariantOptionSubmitLv2.getTemporaryId());
                productVariantCombinationSubmitList.add(productVariantCombinationSubmit);
            }
        }
        return productVariantCombinationSubmitList;
    }

    /**
     * Get removed unnecessary variant unit
     * eg [
     * {"v":2,"vu":0,"pos":1,"pv":null,"opt":[{"pvo":0,"vuv":22,"t_id":1,"cstm":""},{"pvo":0,"vuv":23,"t_id":2,"cstm":""}]},
     * {"v":3,"vu":3,"pos":2,"pv":null,"opt":[]}]
     * return [{"v":2,"vu":0,"pos":1,"pv":null,"opt":[{"pvo":0,"vuv":22,"t_id":1,"cstm":""},{"pvo":0,"vuv":23,"t_id":2,"cstm":""}]}]
     *
     * @param variantUnitSubmitList
     * @return
     */
    public static List<ProductVariantUnitSubmit> getValidatedVariantUnitList(List<ProductVariantUnitSubmit> variantUnitSubmitList) {
        int variantUnitSubmitSize = variantUnitSubmitList.size();

        for (int i = variantUnitSubmitSize - 1; i >= 0; i--) {
            ProductVariantUnitSubmit variantUnitSubmit = variantUnitSubmitList.get(i);
            if (!isVariantUnitValid(variantUnitSubmit) || !isVariantLevelOneNotEmpty(variantUnitSubmitList)) {
                variantUnitSubmitList.remove(i);
            }
        }
        return variantUnitSubmitList;
    }

    /**
     * Update or add new Variant unit to variant unit list
     *
     * @param variantUnitSubmitList
     * @param productVariantUnitSubmit
     * @return
     */
    public static List<ProductVariantUnitSubmit> getUpdatedVariantUnitListPosition(
            List<ProductVariantUnitSubmit> variantUnitSubmitList, ProductVariantUnitSubmit productVariantUnitSubmit) {
        int variantUnitSubmitSize = variantUnitSubmitList.size();
        for (int i = 0; i < variantUnitSubmitSize; i++) {
            ProductVariantUnitSubmit productVariantUnitSubmitTemp = variantUnitSubmitList.get(i);
            if (productVariantUnitSubmitTemp.getPosition() == productVariantUnitSubmit.getPosition()) {
                variantUnitSubmitList.set(i, productVariantUnitSubmit);
                return variantUnitSubmitList;
            }
        }
        // Variant unit not found, add it
        variantUnitSubmitList.add(productVariantUnitSubmit);
        return variantUnitSubmitList;
    }

    /**
     * Check if variant unit is empty or not
     *
     * @param productVariantUnitSubmit
     * @return
     */
    public static boolean isVariantUnitValid(ProductVariantUnitSubmit productVariantUnitSubmit) {
        return productVariantUnitSubmit.getProductVariantOptionSubmitList().size() > 0;
    }

    /**
     * Get variant name list based on variant option list
     * eg
     * variant option: [{"pvo":0,"vuv":22,"t_id":1,"cstm":""},{"pvo":0,"vuv":23,"t_id":2,"cstm":""}]
     * variant unit {"variant_id": 2,"name": "Warna","identifier": "colour","status": 2,"has_unit": 0,"units":
     * [{"unit_id": 0,"name": "","short_name": "","values": [{"value_id": 22,"value": "Putih","hex_code": "#ffffff","icon": ""},{"value_id": 23,"value": "Hitam","hex_code": "#000000","icon": ""}]}
     * return ["Putih", "Hitam"]
     *
     * @param productVariantOptionSubmitList
     * @param productVariantUnitList
     * @return
     */
    public static List<String> getVariantOptionNameList(List<ProductVariantOptionSubmit> productVariantOptionSubmitList, List<ProductVariantUnit> productVariantUnitList) {
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
     * Get all text from server and custom text
     *
     * @param productVariantOptionSubmitList
     * @param productVariantUnitList
     * @return
     */
    public static List<String> getAllVariantOptionNameList(long unitId, List<ProductVariantOptionSubmit> productVariantOptionSubmitList, List<ProductVariantUnit> productVariantUnitList) {
        List<String> titleList = getVariantOptionNameList(unitId, productVariantUnitList);
        List<String> titleCacheList = getVariantOptionNameList(productVariantOptionSubmitList, productVariantUnitList);
        for (String text : titleCacheList) {
            if (!titleList.contains(text)) {
                titleList.add(text);
            }
        }
        return titleList;
    }

    /**
     * Get name list of variant option from server
     *
     * @param productVariantUnitList
     * @return
     */
    private static List<String> getVariantOptionNameList(long unitId, List<ProductVariantUnit> productVariantUnitList) {
        List<String> titleList = new ArrayList<>();
        for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
            if (productVariantUnit.getUnitId() == unitId) {
                for (ProductVariantOption productVariantOption : productVariantUnit.getProductVariantOptionList()) {
                    titleList.add(productVariantOption.getValue());
                }
            }
        }
        return titleList;
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
        for (Long optionIdTemp : optionList) {
            if (optionIdTemp == optionId) {
                return true;
            }
        }
        return false;
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
    public static long getParingId(long optionId, List<Long> optionList) {
        if (isVariantCombinationContainOptionId(optionId, optionList)) {
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
        List<ProductVariantUnitSubmit> variantUnitList = variantData.getProductVariantUnitSubmitList();
        variantUnitList = getRemovedVariantUnitListByOptionId(optionId, variantUnitList);
        variantUnitList = getValidatedVariantUnitList(variantUnitList);
        variantData.setProductVariantUnitSubmitList(variantUnitList);
        variantData.setProductVariantCombinationSubmitList(getRemovedVariantCombinationListByOptionId(optionId, variantData.getProductVariantCombinationSubmitList()));
        return variantData;
    }

    /**
     * if option list empty at level 1, then the others need to reset
     * for example level 1 empty, level 2, 3, 4 need to be removed
     *
     * @param variantUnitSubmitList
     * @return true if option list at level 1 not empty
     */
    private static boolean isVariantLevelOneNotEmpty(List<ProductVariantUnitSubmit> variantUnitSubmitList) {
        ProductVariantUnitSubmit productVariantUnitSubmit = ProductVariantUtils.getVariantUnitByLevel(ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE, variantUnitSubmitList);
        return productVariantUnitSubmit != null &&
                productVariantUnitSubmit.getProductVariantOptionSubmitList() != null &&
                productVariantUnitSubmit.getProductVariantOptionSubmitList().size() > 0;
    }

    private static ProductVariantUnitSubmit getVariantUnitByLevel(int variantLevel, List<ProductVariantUnitSubmit> variantUnitSubmitList) {
        for (ProductVariantUnitSubmit productVariantUnitSubmit : variantUnitSubmitList) {
            if (productVariantUnitSubmit.getPosition() == variantLevel) {
                return productVariantUnitSubmit;
            }
        }
        return null;
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
        ProductVariantUnitSubmit variantUnitSubmit = getVariantUnitByOptionId(optionId, variantUnitSubmitList);
        ProductVariantOptionSubmit variantOptionSubmit = getVariantOptionByOptionId(optionId, variantUnitSubmit.getProductVariantOptionSubmitList());
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
    public static ProductVariantUnitSubmit getVariantUnitByOptionId(long optionId, List<ProductVariantUnitSubmit> variantUnitSubmitList) {
        for (ProductVariantUnitSubmit productVariantUnitSubmit : variantUnitSubmitList) {
            ProductVariantOptionSubmit productVariantOptionSubmit = getVariantOptionByOptionId(optionId, productVariantUnitSubmit.getProductVariantOptionSubmitList());
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
    private static ProductVariantOptionSubmit getVariantOptionByOptionId(long optionId, List<ProductVariantOptionSubmit> variantOptionSubmitList) {
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

    public static ArrayList<ProductVariantOptionSubmit> getProductVariantOptionSubmitLv1(ProductVariantDataSubmit productVariantDataSubmit) {
        if (productVariantDataSubmit == null || productVariantDataSubmit.getProductVariantUnitSubmitList() == null ||
                productVariantDataSubmit.getProductVariantUnitSubmitList().size() == 0) {
            return null;
        }
        List<ProductVariantUnitSubmit> productVariantUnitSubmitList =
                productVariantDataSubmit.getProductVariantUnitSubmitList();

        for (int i = 0, sizei = productVariantUnitSubmitList.size(); i < sizei; i++) {
            ProductVariantUnitSubmit productVariantUnitSubmit = productVariantUnitSubmitList.get(i);
            if (productVariantUnitSubmit.getPosition() != ProductVariantConstant.VARIANT_LEVEL_ONE_VALUE) {
                continue;
            }
            return cloneVarianOptionSubmitList(productVariantUnitSubmit.getProductVariantOptionSubmitList());
        }
        return null;
    }

    private static ArrayList<ProductVariantOptionSubmit> cloneVarianOptionSubmitList(
            List<ProductVariantOptionSubmit> productVariantOptionSubmitList) {
        ArrayList<ProductVariantOptionSubmit> cloneProductVariantOptionSubmitList = new ArrayList<>();
        for (int i = 0, sizei = productVariantOptionSubmitList.size(); i < sizei; i++) {
            ProductVariantOptionSubmit sourceProductVariantOptionSubmit = productVariantOptionSubmitList.get(i);
            ProductVariantOptionSubmit newProductVariantOptionSubmit = new ProductVariantOptionSubmit();
            newProductVariantOptionSubmit.setVariantUnitValueId(sourceProductVariantOptionSubmit.getVariantUnitValueId());
            newProductVariantOptionSubmit.setCustomText(sourceProductVariantOptionSubmit.getCustomText());
            newProductVariantOptionSubmit.setPictureItemList(sourceProductVariantOptionSubmit.getPictureItemList());
            newProductVariantOptionSubmit.setTemporaryId(sourceProductVariantOptionSubmit.getTemporaryId());
            cloneProductVariantOptionSubmitList.add(newProductVariantOptionSubmit);
        }
        return cloneProductVariantOptionSubmitList;
    }

    public static boolean hasCustomVariant(List<ProductVariantUnitSubmit> productVariantUnitSubmitList, int position) {
        for (int i = 0; i < productVariantUnitSubmitList.size(); i++) {
            ProductVariantUnitSubmit productVariantUnitSubmit = productVariantUnitSubmitList.get(i);
            if (productVariantUnitSubmit.getPosition() == position) {
                return hasCustomVariant(productVariantUnitSubmit);
            }
        }
        return false;
    }

    private static boolean hasCustomVariant(@NonNull ProductVariantUnitSubmit productVariantUnitSubmit) {
        List<ProductVariantOptionSubmit> productVariantOptionSubmitList = productVariantUnitSubmit.getProductVariantOptionSubmitList();
        for (int i = 0, sizei = productVariantOptionSubmitList.size(); i < sizei; i++) {
            ProductVariantOptionSubmit productVariantOptionSubmit = productVariantOptionSubmitList.get(i);
            long vuvId = productVariantOptionSubmit.getVariantUnitValueId();
            String customText = productVariantOptionSubmit.getCustomText();
            if (vuvId == 0 && !TextUtils.isEmpty(customText)) {
                return true;
            }
        }
        return false;
    }
}