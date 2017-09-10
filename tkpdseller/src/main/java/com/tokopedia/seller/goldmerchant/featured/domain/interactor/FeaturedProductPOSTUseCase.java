package com.tokopedia.seller.goldmerchant.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.featured.data.model.PostFeaturedProductModel;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductPOSTDomainModel;
import com.tokopedia.seller.goldmerchant.featured.repository.FeaturedProductRepository;
import com.tokopedia.seller.goldmerchant.featured.view.adapter.model.FeaturedProductModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductPOSTUseCase extends UseCase<FeaturedProductPOSTDomainModel> {

    public static final String FEATURED_PRODUCT_MODEL_PARAM = "FEATURED_PRODUCT_MODEL_PARAM";

    private FeaturedProductRepository featuredProductRepository;

    @Inject
    public FeaturedProductPOSTUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            FeaturedProductRepository featuredProductRepository
    ) {
        super(threadExecutor, postExecutionThread);
        this.featuredProductRepository = featuredProductRepository;
    }

    private static PostFeaturedProductModel createRawParam(List<FeaturedProductModel> featuredProductModels) {
        PostFeaturedProductModel postFeaturedProductModel =
                new PostFeaturedProductModel();

        ArrayList<PostFeaturedProductModel.ItemsFeatured> itemsFeatureds = new ArrayList<>();
        for (int i = 1; i <= featuredProductModels.size(); i++) {
            FeaturedProductModel featuredProductModel = featuredProductModels.get(i - 1);

            PostFeaturedProductModel.ItemsFeatured itemsFeatured
                    = new PostFeaturedProductModel.ItemsFeatured();
            itemsFeatured.setType(1);
            itemsFeatured.setOrder(i);
            itemsFeatured.setProductId(featuredProductModel.getProductId());

            itemsFeatureds.add(itemsFeatured);
        }

        postFeaturedProductModel.setItemsFeatured(itemsFeatureds);

        return postFeaturedProductModel;
    }

    public static RequestParams createParam(List<FeaturedProductModel> featuredProductModels) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putObject(FEATURED_PRODUCT_MODEL_PARAM, createRawParam(featuredProductModels));

        return requestParams;
    }

    @Override
    public Observable<FeaturedProductPOSTDomainModel> createObservable(RequestParams requestParams) {
        return featuredProductRepository.postFeatureProductData(requestParams);
    }
}
