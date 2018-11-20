package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.core.network.entity.wishlistCount.WishlistCountResponse;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.tkpdpdp.domain.gql.ImageReviewGqlResponse;
import com.tokopedia.tkpdpdp.helper.GqlHelper;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import retrofit2.Response;
import rx.Subscriber;

public class ImageReviewSubscriber extends Subscriber<GraphqlResponse> {
    private final ProductDetailView viewListener;

    public ImageReviewSubscriber(ProductDetailView viewListener){
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(GraphqlResponse graphqlResponse) {
        ImageReviewGqlResponse gqlResponse = graphqlResponse.getData(ImageReviewGqlResponse.class);
        viewListener.onImageReviewLoaded(
                GqlHelper.convertToImageReviewItemList(gqlResponse)
        );
    }
}