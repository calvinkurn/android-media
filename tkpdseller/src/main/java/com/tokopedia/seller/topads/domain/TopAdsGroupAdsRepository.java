package com.tokopedia.seller.topads.domain;

import com.tokopedia.seller.topads.data.model.request.CreateGroupRequest;
import com.tokopedia.seller.topads.data.model.response.DataResponseCreateGroup;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsGroupAdsRepository {
    Observable<List<GroupAd>> searchGroupAds(String string);

    Observable<DataResponseCreateGroup> createGroup(CreateGroupRequest createGroupRequest);
}
