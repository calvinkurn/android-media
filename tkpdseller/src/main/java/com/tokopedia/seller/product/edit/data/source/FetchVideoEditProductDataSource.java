package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.product.model.goldmerchant.ProductVideoData;
import com.tokopedia.core.product.model.goldmerchant.Video;
import com.tokopedia.core.product.model.goldmerchant.VideoData;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by normansyahputa on 5/3/17.
 */

public class FetchVideoEditProductDataSource implements Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>> {

    GoldMerchantService goldMerchantService;
    String productId;

    public FetchVideoEditProductDataSource(GoldMerchantService goldMerchantService) {
        this.goldMerchantService = goldMerchantService;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public Observable<UploadProductInputDomainModel> call(UploadProductInputDomainModel uploadProductInputDomainModel) {

        if (productId == null || productId.isEmpty())
            throw new IllegalArgumentException("product Id must be passed");

        Observable<Response<ProductVideoData>> fetchVideo = goldMerchantService.getApi().fetchVideo(productId);

        return Observable.zip(Observable.just(uploadProductInputDomainModel), fetchVideo, new Func2<UploadProductInputDomainModel, Response<ProductVideoData>, UploadProductInputDomainModel>() {
            @Override
            public UploadProductInputDomainModel call(UploadProductInputDomainModel uploadProductInputDomainModel, Response<ProductVideoData> productVideoDataResponse) {

                if (productVideoDataResponse.body() != null && productVideoDataResponse.body().getData() != null) {
                    List<String> videoIds = new ArrayList<>();
                    VideoData videoData = productVideoDataResponse.body().getData().get(0);
                    for (Video video : videoData.getVideo()) {
                        videoIds.add(video.getUrl());
                    }
                    uploadProductInputDomainModel.setProductVideos(videoIds);
                }

                return uploadProductInputDomainModel;
            }
        });
    }
}
