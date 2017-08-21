package com.tokopedia.seller.product.variant.util;

import android.text.TextUtils;

import com.tokopedia.seller.product.variant.constant.ExtraConstant;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantUnit;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantValue;
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
        productVariantManageViewModel.setTitle(getTitle(ExtraConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOption, productVariantByCatModelList));
        productVariantManageViewModel.setContent(getMultipleVariantOptionTitle(ExtraConstant.VARIANT_LEVEL_TWO_VALUE, variantUnitSubmit, productVariantByCatModelList));
        if (TextUtils.isEmpty(variantSubmitOption.getCustomText())) {
            // Check variant option title from server
            ProductVariantValue productVariantValue = getProductVariantByCatModelByVariantId(
                    ExtraConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOption.getVariantUnitValueId(), productVariantByCatModelList);
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
        VariantUnitSubmit variantUnitSubmitLv1 = getVariantUnitSubmit(ExtraConstant.VARIANT_LEVEL_ONE_VALUE, variantUnitSubmitList);
        for (VariantSubmitOption variantSubmitOptionLv1 : variantUnitSubmitLv1.getVariantSubmitOptionList()) {
            ProductVariantManageViewModel productVariantManageViewModel = new ProductVariantManageViewModel();
            productVariantManageViewModel.setTemporaryId(variantSubmitOptionLv1.getTemporaryId());
            productVariantManageViewModel.setTitle(getTitle(ExtraConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOptionLv1, productVariantByCatModelList));

            List<VariantSubmitOption> variantSubmitOptionList = getPairingVariantSubmitOptionListBy(variantSubmitOptionLv1.getTemporaryId(), variantUnitSubmitList, variantStatusList);
            String contentText = getMultipleVariantOptionTitle(ExtraConstant.VARIANT_LEVEL_TWO_VALUE, variantSubmitOptionList, productVariantByCatModelList);

            productVariantManageViewModel.setContent(contentText);
            if (TextUtils.isEmpty(variantSubmitOptionLv1.getCustomText())) {
                // Check variant option title from server
                ProductVariantValue productVariantValue = getProductVariantByCatModelByVariantId(
                        ExtraConstant.VARIANT_LEVEL_ONE_VALUE, variantSubmitOptionLv1.getVariantUnitValueId(), productVariantByCatModelList);
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
            if (level == ExtraConstant.VARIANT_LEVEL_ONE_VALUE && variantByCatModel.getStatus() == ExtraConstant.VARIANT_STATUS_TWO) {
                return variantByCatModel;
            } else if (level == ExtraConstant.VARIANT_LEVEL_TWO_VALUE && variantByCatModel.getStatus() == ExtraConstant.VARIANT_STATUS_ONE) {
                return variantByCatModel;
            }
        }
        return null;
    }

    public static int getVariantPositionByStatus(int status) {
        switch (status) {
            case ExtraConstant.VARIANT_STATUS_ONE:
                return ExtraConstant.VARIANT_LEVEL_TWO_VALUE;
            case ExtraConstant.VARIANT_STATUS_TWO:
                return ExtraConstant.VARIANT_LEVEL_ONE_VALUE;
        }
        return -1;
    }
}
