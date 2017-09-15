package com.tokopedia.topads.keyword.view.mapper;

import android.text.TextUtils;

import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.topads.keyword.domain.model.EditTopAdsKeywordDetailDomainModel;
import com.tokopedia.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.topads.keyword.view.model.KeywordAd;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailMapper {
    public static TopAdsKeywordEditDetailInputDomainModel mapViewToDomain(KeywordAd viewModel) {
        TopAdsKeywordEditDetailInputDomainModel domainModel = new TopAdsKeywordEditDetailInputDomainModel();

        domainModel.setKeywordId(viewModel.getId());
        domainModel.setGroupId(viewModel.getGroupId());
        domainModel.setKeywordTag(viewModel.getName());
        domainModel.setKeywordTypeId(viewModel.getKeywordTypeId());
        domainModel.setPriceBid(getPrice(viewModel.getPriceBidFmt()));
        domainModel.setToggle(viewModel.getStatusToogle());

        return domainModel;
    }

    private static double getPrice(String price) {
        String valueString = CurrencyFormatHelper
                .removeCurrencyPrefix(price);
        valueString = CurrencyFormatHelper.RemoveNonNumeric(valueString);
        if (TextUtils.isEmpty(valueString)) {
            return 0;
        }
        return Double.parseDouble(valueString);
    }

    public static KeywordAd mapDomainToView(EditTopAdsKeywordDetailDomainModel domainModel) {
        KeywordAd viewModel = new KeywordAd();
        viewModel.setKeywordTypeId(domainModel.getKeywordTypeId());
        viewModel.setPriceBidFmt(domainModel.getPriceBid());
        viewModel.setGroupId(domainModel.getGroupId());
        viewModel.setId(domainModel.getKeywordId());
        viewModel.setKeywordTag(domainModel.getKeywordTag());
        return viewModel;
    }
}
