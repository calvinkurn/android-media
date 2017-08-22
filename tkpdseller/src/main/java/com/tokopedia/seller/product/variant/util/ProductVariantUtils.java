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
import com.tokopedia.seller.product.variant.view.model.ProductVariantManageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nathan on 8/19/17.
 */

public class ProductVariantUtils {

    private static final String VARIANT_TITLE_SEPARATOR = ",";
    private static final String SPLIT_DELIMITER = ":"; // this depends on the api.

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

    public static ProductVariantValue getProductVariantValueByVariantUnit(VariantSubmitOption variantSubmitOption, List<ProductVariantUnit> productVariantUnitList) {
        for (ProductVariantUnit productVariantUnit : productVariantUnitList) {
            ProductVariantValue productVariantValue = getProductVariantValue(variantSubmitOption, productVariantUnit.getProductVariantValueList());
            if (productVariantValue != null) {
                return productVariantValue;
            }
        }
        return null;
    }

    public static ProductVariantValue getProductVariantValue(VariantSubmitOption variantSubmitOption, List<ProductVariantValue> productVariantValueList) {
        for (ProductVariantValue productVariantValue : productVariantValueList) {
            if (productVariantValue.getValueId() == variantSubmitOption.getVariantUnitValueId()) {
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
            long optionId, List<VariantUnitSubmit> variantUnitSubmitList, List<VariantStatus> variantStatusList) {
        List<VariantSubmitOption> variantSubmitOptionList = new ArrayList<>();
        for (VariantStatus variantStatus : variantStatusList) {
            for (Long optionIdSelected : variantStatus.getOptionList()) {
                if (optionIdSelected == optionId) {
                    long pairingId = getParingId(optionId, variantStatus.getOptionList());
                    variantSubmitOptionList.add(getVariantSubmitOptionById(pairingId, variantUnitSubmitList));
                }
            }
        }
        return variantSubmitOptionList;
    }

    private static long getParingId(long optionId, List<Long> optionList) {
        for (Long optionIdSelected : optionList) {
            if (optionIdSelected != optionId) {
                return optionIdSelected;
            }
        }
        return -1;
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
            String vSourceCode = variantSourceDatum.getvCode();
            String[] vCodeSplit = vSourceCode.split(SPLIT_DELIMITER);
            for (String aVCodeSplit : vCodeSplit) {
                long optTId = tempIdInverseMap.get(Integer.parseInt(aVCodeSplit));
                optList.add(optTId);
            }
            variantStatus.setOptionList(optList);
            variantStatusList.add(variantStatus);
        }
        variantData.setVariantStatusList(variantStatusList);
        return variantData;
    }

    public static List<String> getTitleList(List<VariantSubmitOption> variantSubmitOptionList, List<ProductVariantUnit> productVariantUnitList) {
        List<String> titleList = new ArrayList<>();
        for (VariantSubmitOption variantSubmitOption: variantSubmitOptionList) {
            // If name already on custom text, add custom text on title list
            if (!TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
                titleList.add(variantSubmitOption.getCustomText());
            } else {
                // If not, search and mapping with variant value list from server
                ProductVariantValue productVariantValue = getProductVariantValueByVariantUnit(variantSubmitOption, productVariantUnitList);
                if (productVariantValue != null) {
                    titleList.add(productVariantValue.getValue());
                }
            }
        }
        return titleList;
    }
}
