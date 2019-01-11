package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.core.product.model.productdetail.mosthelpful.Review;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MostHelpfulReviewSubscriberTest {
    @Mock
    ProductDetailView viewListener;

    @Mock
    List<Review> mostHelpfulReviews;

    MostHelpfulReviewSubscriber mostHelpfulReviewSubscriber;

    @Before
    public void setUp(){
        mostHelpfulReviewSubscriber = new MostHelpfulReviewSubscriber(viewListener);
    }

    @Test
    public void nullImageReviewItems_onNext_onImageReviewLoadedNotCalled(){
        mostHelpfulReviewSubscriber.onNext(null);
        verify(viewListener, never()).showMostHelpfulReview(mostHelpfulReviews);
    }

    @Test
    public void emptyImageReviewItems_onNext_onImageReviewLoadedNotCalled(){
        when(mostHelpfulReviews.size()).thenReturn(0);

        mostHelpfulReviewSubscriber.onNext(mostHelpfulReviews);
        verify(viewListener, never()).showMostHelpfulReview(mostHelpfulReviews);
    }

    @Test
    public void imageReviewItemsNotNullAndNotEmpty_onNext_onImageReviewLoadedCalled(){
        when(mostHelpfulReviews.size()).thenReturn(1);

        mostHelpfulReviewSubscriber.onNext(mostHelpfulReviews);
        verify(viewListener).showMostHelpfulReview(mostHelpfulReviews);
    }
}