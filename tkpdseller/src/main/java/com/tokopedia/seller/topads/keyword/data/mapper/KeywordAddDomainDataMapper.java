package com.tokopedia.seller.topads.keyword.data.mapper;

import com.tokopedia.seller.topads.data.model.response.PageDataResponse;
import com.tokopedia.seller.topads.keyword.data.model.cloud.KeywordAddResponseDatum;
import com.tokopedia.seller.topads.keyword.data.model.cloud.request.keywordadd.AddKeywordRequest;
import com.tokopedia.seller.topads.keyword.data.model.cloud.request.keywordadd.KeywordAddRequestDatum;
import com.tokopedia.seller.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum;
import com.tokopedia.seller.topads.keyword.domain.model.keywordadd.AddKeywordDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author normansyahputa on 5/18/17.
 */

public class KeywordAddDomainDataMapper
        implements Func1<PageDataResponse<List<KeywordAddResponseDatum>>,
        AddKeywordDomainModel> {

    public static final String DEFAULT_TOGGLE = "1";
    public static final String DEFAULT_STATUS = "1";
    public static final String DEFAULT_SOURCE = "dashboard_user_add_keyword_positive";

    @Inject
    public KeywordAddDomainDataMapper() {
    }

    /**
     * DATA MODEL       DOMAIN MODEL
        keywordTag      => tag
        groupId         => groupId
        keywordTypeId   => type
        groupId         => groupId
        toggle;
        status
     */
    @Override
    public AddKeywordDomainModel call(PageDataResponse<List<KeywordAddResponseDatum>> responseList) {
        List<AddKeywordDomainModelDatum> addKeywordDomainModelDatumList = new ArrayList<>();
        for (KeywordAddResponseDatum datum : responseList.getData()) {
            addKeywordDomainModelDatumList.add( new AddKeywordDomainModelDatum(
                    datum.getKeywordTag(),
                    Integer.parseInt(datum.getKeywordTypeId()),
                    datum.getGroupId(),
                    datum.getShopId()));
        }
        return new AddKeywordDomainModel(addKeywordDomainModelDatumList);
    }

    /**
     * DATA MODEL       DOMAIN MODEL
     keywordTag      <= tag
     groupId         <= groupId
     keywordTypeId   <= type
     groupId         <= groupId
     toggle          <= 1
     status          <= 1
     source          <= "android"
     */

    public static AddKeywordRequest convertDomainToRequestData(AddKeywordDomainModel addKeywordDomainModel){
        AddKeywordRequest addKeywordRequest = new AddKeywordRequest();
        List<AddKeywordDomainModelDatum> domainModelList = addKeywordDomainModel.getAddKeywordDomainModelDatumList();
        List<KeywordAddRequestDatum> datumList = new ArrayList<>();
        for (int i = 0, sizei = domainModelList.size(); i<sizei;i++){
            AddKeywordDomainModelDatum domainModel = domainModelList.get(i);
            KeywordAddRequestDatum datum = new KeywordAddRequestDatum();
            datum.setGroupId(domainModel.getGroupId());
            datum.setKeywordTag(domainModel.getKeywordTag());
            datum.setKeywordTypeId(String.valueOf(domainModel.getKeyWordTypeId()));
            datum.setShopId(domainModel.getShopId());
            datum.setToggle(DEFAULT_TOGGLE);
            datum.setStatus(DEFAULT_STATUS);
            datum.setSource(DEFAULT_SOURCE);
            datumList.add(datum);
        }
        addKeywordRequest.setData(datumList);
        return addKeywordRequest;
    }

}
