package com.tokopedia.seller.product.variant.data.model.varianthelper;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.Option;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantDatum;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.VariantOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VarianStatus;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantSubmitOption;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantUnitSubmit;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.VariantData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hendry on 8/16/2017.
 */

public class ProductVariantHelper {
    public static final String DELIMITER = ":";
    private List<ProductVariantByCatModel> productVariantByCatModel;
    private ProductVariantByPrdModel productVariantByPrdModel;

    private SparseArray<String> variantIdMap; // 1->warna, 7->ukuran umur bayi
    private HashMap<String, Integer> variantIdInverseMap; // warna->1, ukuran umur bayi -> 7

    private SparseArray<String> unitIdMap; // 0->"1:", 10-> "7:umur bayi"
    private HashMap<String, Integer> unitIdInverseMap; // "1:"->0, "7:umur bayi" -> 10

    private SparseArray<String> unitValuesIdMap; // 1->"1:0:Putih" 109->"7:10:0-3 Bulan"
    private HashMap<String, Integer> unitValuesIdInverseMap; // "1:0:Putih"->1 "7:10:0-3 Bulan"->109

    // for submit
    private HashMap<String, Integer> tempIdInverseMap; // "1:0:custom merah" -> 1

    // we need this to map the pvo to codename first time, will be reinverted when submit to server when edit prod
    private SparseArray<String> productOptionIdMap;  //2194115->"1:0:custom hijau" (from server)

    public ProductVariantHelper(@NonNull List<ProductVariantByCatModel> productVariantByCatModel,
                                @NonNull ProductVariantByPrdModel productVariantByPrdModel) {
        this.productVariantByCatModel = productVariantByCatModel;
        this.productVariantByPrdModel = productVariantByPrdModel;
        traverseVariantByCat(productVariantByCatModel);
        traverseSelection(productVariantByPrdModel);
    }

    /**
     * Traverse the Product VariantUnitSubmit By Prd Model to populate the productMap to retrieve later
     *
     * @param productVariantByPrdModel
     */
    public void traverseSelection(ProductVariantByPrdModel productVariantByPrdModel) {
        traverseSummary(productVariantByPrdModel);
        traverseDetail(productVariantByPrdModel);
    }

    /**
     * Traverse the Product VariantUnitSubmit By Cat Model to populate the hashmap to retrieve later
     *
     * @param productVariantByCatModelList
     */
    public void traverseVariantByCat(List<ProductVariantByCatModel> productVariantByCatModelList) {
        if (productVariantByCatModelList == null || productVariantByCatModelList.size() == 0) {
            return;
        }
        variantIdMap = new SparseArray<>();
        variantIdInverseMap = new DefaultHashMap<>(0);
        unitIdMap = new SparseArray<>();
        unitIdInverseMap = new DefaultHashMap<>(0);
        unitValuesIdMap = new SparseArray<>();
        unitValuesIdInverseMap = new DefaultHashMap<>(0);

        for (int i = 0, sizei = productVariantByCatModelList.size(); i < sizei; i++) {
            ProductVariantByCatModel productVariantByCatModel = productVariantByCatModelList.get(i);
            int variantId = productVariantByCatModel.getVariantId();
            String variantName = productVariantByCatModel.getName();

            variantIdMap.put(variantId, variantName);
            variantIdInverseMap.put(variantName, variantId);

            List<ProductVariantUnit> productVariantUnitList = productVariantByCatModel.getUnitList();
            for (int j = 0, sizej = productVariantUnitList.size(); j < sizej; j++) {
                ProductVariantUnit productVariantUnit = productVariantUnitList.get(j);
                int unitId = productVariantUnit.getUnitId();
                String unitLongName = variantId + DELIMITER + productVariantUnit.getName();

                unitIdMap.put(unitId, unitLongName);
                unitIdInverseMap.put(unitLongName, unitId);
                List<ProductVariantValue> productVariantValueList = productVariantUnit.getProductVariantValueList();
                for (int k = 0, sizek = productVariantValueList.size(); k < sizek; k++) {
                    ProductVariantValue productVariantValue = productVariantValueList.get(k);
                    int valueId = productVariantValue.getValueId();
                    String valueLongName = variantId + DELIMITER + unitId + DELIMITER + productVariantValue.getValue();

                    unitValuesIdMap.put(valueId, valueLongName);
                    unitValuesIdInverseMap.put(valueLongName, valueId);
                }

            }
        }
    }

    /**
     * populate the map for id and name
     */
    private void traverseSummary(ProductVariantByPrdModel productVariantByPrdModel) {
        productOptionIdMap = new SparseArray<>();
        List<VariantOption> variantOptionList = productVariantByPrdModel.getVariantOptionList();
        for (int i = 0, sizei = variantOptionList.size(); i < sizei; i++) {
            VariantOption variantOption = variantOptionList.get(i);
            int variantId = variantIdInverseMap.get(variantOption.getName());
            int unitId = unitIdInverseMap.get(variantId + DELIMITER + variantOption.getUnitName());
            List<Option> optionList = variantOption.getOptionList();
            for (int j = 0, sizej = optionList.size(); j < sizej; j++) {
                Option option = optionList.get(j);
                int pvoId = option.getPvoId();
                if (pvoId <= 0) {
                    // must be a created new. just continue
                    return;
                }
                String optionValue = variantId + DELIMITER + unitId + DELIMITER + option.getValue();
                productOptionIdMap.put(pvoId, optionValue);
            }
        }
    }

    /**
     * add the vcodename for productvariant by prd
     */
    private void traverseDetail(ProductVariantByPrdModel productVariantByPrdModel) {
        List<VariantDatum> variantDataList = productVariantByPrdModel.getVariantDataList();
        for (int i = 0, sizei = variantDataList.size(); i < sizei; i++) {
            VariantDatum variantDatum = variantDataList.get(i);
            List<String> codeNameList = mapVCodeToList(variantDatum.getvCode());
            variantDatum.setvCodeName(codeNameList);
        }
    }

    /**
     * return "1::custom hijau" from pvoid;
     */
    private String getValueFromPvoId(int pvoId) {
        if (productOptionIdMap.indexOfKey(pvoId) >= 0) {
            return productOptionIdMap.get(pvoId);
        }
        return "";
    }

    /**
     * map the "2194115:2193654" to List of "1::hijau" "6:7:merah"
     */
    private List<String> mapVCodeToList(String vCode) {
        List<String> variantCombinationList = new ArrayList<>();
        String[] vCodeSplit = vCode.split(DELIMITER);
        for (String aVCodeSplit : vCodeSplit) {
            variantCombinationList.add(
                    getValueFromPvoId(Integer.parseInt(aVCodeSplit)));
        }
        return variantCombinationList;
    }

    public ProductVariantSubmit generateProductVariantSubmit() {
        if (productVariantByPrdModel.getVariantOptionList() == null ||
                productVariantByPrdModel.getVariantOptionList().size() == 0) {
            // Means all the variants have been removed
            return null;
        }
        ProductVariantSubmit productVariantSubmit = new ProductVariantSubmit();
        VariantData variantData = new VariantData();

        variantData.setVariantUnitSubmitList(generateUnitSubmitList());
        variantData.setVarianStatusList(getVariantStatusSubmitList());

        productVariantSubmit.setVariantData(variantData);
        return productVariantSubmit;
    }

    private List<VariantUnitSubmit> generateUnitSubmitList() {
        int posUnitCounter = 1;
        int tIdCounter = 1;
        tempIdInverseMap = new HashMap<>();

        // Maps the unit and unit value
        List<VariantUnitSubmit> variantUnitSubmitList = new ArrayList<>();
        List<VariantOption> variantOptionSourceList = productVariantByPrdModel.getVariantOptionList();
        for (int i = 0, sizei = variantOptionSourceList.size();
             i < sizei; i++) {
            VariantOption varianOptionSource = variantOptionSourceList.get(i);
            VariantUnitSubmit variantUnitSubmit = new VariantUnitSubmit();
            int variantId = variantIdInverseMap.get(varianOptionSource.getName());
            variantUnitSubmit.setVariantId(variantId);
            int unitId = unitIdInverseMap.get(variantId + DELIMITER + varianOptionSource.getUnitName());
            variantUnitSubmit.setVariantUnitId(unitId);
            variantUnitSubmit.setPosition(posUnitCounter);
            posUnitCounter++;
            variantUnitSubmit.setProductVariant(varianOptionSource.getPvId());

            //set options
            List<VariantSubmitOption> variantSubmitOptionList = new ArrayList<>();
            List<Option> optionSourceList = varianOptionSource.getOptionList();
            for (int k = 0, sizek = optionSourceList.size(); k < sizek; k++) {
                Option optionSource = optionSourceList.get(k);
                VariantSubmitOption variantSubmitOption = new VariantSubmitOption();
                variantSubmitOption.setProductVariantOptionId(optionSource.getPvoId());
                String optionValue = optionSource.getValue();
                String keyForMap = variantId + DELIMITER + unitId + DELIMITER + optionValue;
                int varUnitValId = unitValuesIdInverseMap.get(keyForMap);
                variantSubmitOption.setVariantUnitValueId(varUnitValId);
                if (varUnitValId == 0) {
                    variantSubmitOption.setCustomText(optionValue);
                } else {
                    variantSubmitOption.setCustomText("");
                }
                variantSubmitOption.setTemporaryId(tIdCounter);
                // add to map, to enable inverse lookup later
                tempIdInverseMap.put(keyForMap, tIdCounter);

                tIdCounter++;

                variantSubmitOptionList.add(variantSubmitOption);
            }
            variantUnitSubmit.setVariantSubmitOptionList(variantSubmitOptionList);
            variantUnitSubmitList.add(variantUnitSubmit);
        }
        return variantUnitSubmitList;
    }

    private List<VarianStatus> getVariantStatusSubmitList() {
        List<VarianStatus> varianStatusList = new ArrayList<>();
        // add the metrics status here
        List<VariantDatum> variantSourceDataList = productVariantByPrdModel.getVariantDataList();
        for (int i = 0, sizei = variantSourceDataList.size(); i < sizei; i++) {
            VariantDatum variantSourceDatum = variantSourceDataList.get(i);
            VarianStatus varianStatus = new VarianStatus();
            varianStatus.setPvd(variantSourceDatum.getPvdId());
            varianStatus.setStatus(variantSourceDatum.getStatus());
            List<Integer> optList = new ArrayList<>();
            List<String> codeNameSourceList = variantSourceDatum.getvCodeName();
            for (int k = 0, sizek = codeNameSourceList.size(); k < sizek; k++) {
                String codeNameSource = codeNameSourceList.get(k);
                int optTId = tempIdInverseMap.get(codeNameSource);
                optList.add(optTId);
            }
            varianStatus.setOptionList(optList);
            varianStatusList.add(varianStatus);
        }
        return varianStatusList;
    }


}
