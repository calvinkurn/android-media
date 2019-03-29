package com.tokopedia.discovery.newdiscovery.search.fragment.profile.subscriber;

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener;
import com.tokopedia.kolcommon.model.FollowResponseModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FollowUnfollowKolSubscriberTest {
    private static final int SAMPLE_POSITION = 1;
    private static final boolean SAMPLE_FOLLOW_STATUS = false;
    @Mock
    ProfileContract.View view;

    @Mock
    FollowActionListener followActionListener;

    private FollowUnfollowKolSubscriber followUnfollowKolSubscriber;

    @Before
    public void setUp(){
        this.followUnfollowKolSubscriber = new FollowUnfollowKolSubscriber(
                SAMPLE_POSITION,
                view,
                SAMPLE_FOLLOW_STATUS,
                followActionListener
        );
    }

    @Test
    public void onNext_followResponseModelStatusSuccess_actionListenerSuccessToggleFollowButton() {
        String mockErrorMessage = "mock error message";

        FollowResponseModel followResponseModel = Mockito.mock(FollowResponseModel.class);

        when(followResponseModel.getErrorMessage()).thenReturn(mockErrorMessage);
        when(followResponseModel.isSuccess()).thenReturn(true);

        followUnfollowKolSubscriber.onNext(followResponseModel);

        verify(followActionListener).onSuccessToggleFollow(anyInt(), anyBoolean());
    }

    @Test
    public void onNext_followResponseModelStatusFailed_actionListenerErrorToggleFollowButton() {
        String mockErrorMessage = "mock error message";

        FollowResponseModel followResponseModel = Mockito.mock(FollowResponseModel.class);

        when(followResponseModel.getErrorMessage()).thenReturn(mockErrorMessage);
        when(followResponseModel.isSuccess()).thenReturn(false);

        followUnfollowKolSubscriber.onNext(followResponseModel);

        verify(followActionListener).onErrorToggleFollow(anyInt(), anyString());
    }

    @Test
    public void onFailed_followResponseModelStatusFailed_actionListenerErrorToggleFollowButton() {
        String mockErrorMessage = "mock error message";
        Throwable throwable = Mockito.mock(Throwable.class);

        when(throwable.getMessage()).thenReturn(mockErrorMessage);

        followUnfollowKolSubscriber.onError(throwable);

        verify(followActionListener).onErrorToggleFollow(anyInt(), anyString());
    }
}