package com.tokopedia.discovery.newdiscovery.data.mapper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.entity.discovery.GuidedSearchResponse;
import com.tokopedia.core.network.entity.discovery.SearchProductResponse;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.newdiscovery.domain.model.BadgeModel;
import com.tokopedia.discovery.newdiscovery.domain.model.LabelModel;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.helper.NetworkParamHelper;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by henrypriyono on 14/02/18.
 */

public class GuidedSearchMapper implements Func1<Response<GuidedSearchResponse>, GuidedSearchViewModel> {

    @Override
    public GuidedSearchViewModel call(Response<GuidedSearchResponse> response) {
        if (response.isSuccessful()) {
            GuidedSearchResponse guidedSearchResponse = response.body();
            if (guidedSearchResponse != null) {
                return mappingPojoIntoDomain(guidedSearchResponse);
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private GuidedSearchViewModel mappingPojoIntoDomain(GuidedSearchResponse guidedSearchResponse) {
        GuidedSearchViewModel model = new GuidedSearchViewModel();
        List<GuidedSearchViewModel.Item> itemList = new ArrayList<>();

        for (GuidedSearchResponse.GuidedSearchItem item : guidedSearchResponse.getData()) {
            itemList.add(mappingGuidedSearchItem(item));
        }
        model.setItemList(itemList);
        return model;
    }

    private GuidedSearchViewModel.Item mappingGuidedSearchItem(GuidedSearchResponse.GuidedSearchItem networkItem) {
        GuidedSearchViewModel.Item viewModelItem = new GuidedSearchViewModel.Item();
        viewModelItem.setKeyword(networkItem.getKeyword());
        viewModelItem.setUrl(networkItem.getUrl());
        return viewModelItem;
    }
}
