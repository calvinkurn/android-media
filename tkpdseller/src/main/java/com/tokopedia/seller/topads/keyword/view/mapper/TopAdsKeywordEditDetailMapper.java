package com.tokopedia.seller.topads.keyword.view.mapper;

import com.tokopedia.seller.topads.keyword.domain.model.TopAdsKeywordEditDetailInputDomainModel;
import com.tokopedia.seller.topads.keyword.view.model.TopAdsKeywordEditDetailViewModel;

/**
 * @author sebastianuskh on 5/29/17.
 */

public class TopAdsKeywordEditDetailMapper {
    public static TopAdsKeywordEditDetailInputDomainModel mapViewToDomain(TopAdsKeywordEditDetailViewModel viewModel) {
        TopAdsKeywordEditDetailInputDomainModel domainModel = new TopAdsKeywordEditDetailInputDomainModel();

        domainModel.setKeywordId(viewModel.getKeywordId());
        domainModel.setGroupId(viewModel.getGroupId());
        domainModel.setKeywordTag(viewModel.getKeywordTag());
        domainModel.setKeywordTypeId(viewModel.getKeywordTypeId());
        domainModel.setPriceBid(viewModel.getPriceBid());
        domainModel.setToggle(viewModel.getToggle());

        return domainModel;
    }
}
