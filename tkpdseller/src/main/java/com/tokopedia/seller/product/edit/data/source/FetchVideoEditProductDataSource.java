package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.product.model.goldmerchant.ProductVideoData;
import com.tokopedia.core.product.model.goldmerchant.Video;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/3/17.
 */

public class FetchVideoEditProductDataSource {

    private GoldMerchantService goldMerchantService;

    @Inject
    public FetchVideoEditProductDataSource(GoldMerchantService goldMerchantService) {
        this.goldMerchantService = goldMerchantService;
    }

    public Observable<List<Video>> fetchVideos(String productId) {
        return goldMerchantService.getApi().fetchVideo(productId).flatMap(new Func1<Response<ProductVideoData>,
                Observable<List<Video>>>() {
            @Override
            public Observable<List<Video>> call(Response<ProductVideoData> productVideoDataResponse) {
                ProductVideoData productVideoData = productVideoDataResponse.body();
                if (productVideoData != null && productVideoData.getData() != null &&
                        productVideoData.getData().get(0) != null && productVideoData.getData().get(0).getVideo() != null
                        && productVideoData.getData().get(0).getVideo().size() > 0) {
                    return Observable.just(productVideoData.getData().get(0).getVideo());
                } else {
                    return Observable.just((List<Video>)new ArrayList<Video>());
                }
            }
        });
    }

}
