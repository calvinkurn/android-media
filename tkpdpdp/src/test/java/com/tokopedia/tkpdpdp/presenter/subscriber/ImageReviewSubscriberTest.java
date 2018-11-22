package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.tkpdpdp.domain.gql.ImageReviewGqlResponse;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageReviewSubscriberTest {

    @Mock
    ProductDetailView viewListener;

    @Mock
    GraphqlResponse graphqlResponse;

    @Mock
    ImageReviewGqlResponse imageReviewGqlResponse;

    ImageReviewSubscriber imageReviewSubscriber;

    @Before
    public void setUp(){
        imageReviewSubscriber = new ImageReviewSubscriber(viewListener);
    }

    @Test
    public void gqlResponseWithNullList_onNext_onImageReviewLoadedNotCalled(){
        GraphqlResponse graphqlResponse = new GraphqlResponse()
        when(graphqlResponse.getData(anyObject())).thenReturn(imageReviewGqlResponse);
        when(imageReviewGqlResponse.getProductReviewImageListQuery()).thenReturn(null);

        imageReviewSubscriber.onNext(graphqlResponse);

        verify(viewListener, never()).onImageReviewLoaded(anyList());
    }
}