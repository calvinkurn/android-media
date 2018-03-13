package com.tokopedia.discovery.imagesearch.data.mapper;

import com.google.gson.Gson;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.network.exception.RuntimeHttpErrorException;
import com.tokopedia.discovery.imagesearch.domain.model.ImageSearchResultModel;
import com.tokopedia.discovery.imagesearch.domain.usecase.NewImageSearchResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by sachinbansal on 1/10/18.
 */

public class ImageSearchResultMapper implements Func1<Response<String>, ImageSearchResultModel> {

    private final Gson gson;

    public ImageSearchResultMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public ImageSearchResultModel call(Response<String> response) {
        if (response.isSuccessful()) {
            NewImageSearchResponse newImageSearchResponse = gson.fromJson(response.body(), NewImageSearchResponse.class);
            if (newImageSearchResponse != null) {
                return mappingPojoIntoDomain(newImageSearchResponse);
            } else {
                throw new MessageErrorException(response.errorBody().toString());
            }
        } else {
            throw new RuntimeHttpErrorException(response.code());
        }
    }

    private ImageSearchResultModel mappingPojoIntoDomain(NewImageSearchResponse newImageSearchResponse) {
        ImageSearchResultModel imageSearchResultModel = new ImageSearchResultModel();
        imageSearchResultModel.setAuctionArrayList(newImageSearchResponse.getAuctionsArrayList());
        return imageSearchResultModel;
    }
}
