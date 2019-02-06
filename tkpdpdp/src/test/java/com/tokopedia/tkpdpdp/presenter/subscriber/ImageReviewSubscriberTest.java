package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.gallery.viewmodel.ImageReviewItem;
import com.tokopedia.gallery.viewmodel.ImageReviewListModel;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageReviewSubscriberTest {

    @Mock
    ProductDetailView viewListener;

    @Mock
    ImageReviewListModel imageReviewListModel;

    @Mock
    List<ImageReviewItem> imageReviewItems;

    ImageReviewSubscriber imageReviewSubscriber;

    @Before
    public void setUp(){
        imageReviewSubscriber = new ImageReviewSubscriber(viewListener);
    }

    @Test
    public void nullImageReviewItems_onNext_onImageReviewLoadedNotCalled(){
        imageReviewSubscriber.onNext(null);
        verify(viewListener, never()).onImageReviewLoaded(imageReviewItems);
    }

    @Test
    public void emptyImageReviewItems_onNext_onImageReviewLoadedNotCalled(){
        when(imageReviewItems.size()).thenReturn(0);
        when(imageReviewListModel.getImageReviewItemList()).thenReturn(imageReviewItems);

        imageReviewSubscriber.onNext(imageReviewListModel);
        verify(viewListener, never()).onImageReviewLoaded(imageReviewItems);
    }

    @Test
    public void imageReviewItemsNotNullAndNotEmpty_onNext_onImageReviewLoadedCalled(){
        when(imageReviewItems.size()).thenReturn(1);
        when(imageReviewListModel.getImageReviewItemList()).thenReturn(imageReviewItems);

        imageReviewSubscriber.onNext(imageReviewListModel);
        verify(viewListener).onImageReviewLoaded(imageReviewItems);
    }
}