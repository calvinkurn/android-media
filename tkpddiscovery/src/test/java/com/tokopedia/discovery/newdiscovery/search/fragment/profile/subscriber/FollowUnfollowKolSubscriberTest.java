package com.tokopedia.discovery.newdiscovery.search.fragment.profile.subscriber;

import com.tokopedia.discovery.newdiscovery.domain.subscriber.GetProfileListSubscriber;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProfileListUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileListPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel;
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.kolcommon.model.FollowResponseModel;
import com.tokopedia.usecase.RequestParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
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
        FollowResponseModel followResponseModel = Mockito.mock(FollowResponseModel.class);
        followUnfollowKolSubscriber.onNext(followResponseModel);

        verify(followActionListener).onSuccessToggleFollow(anyInt(), anyBoolean());
    }

    @Test
    public void onNext_followResponseModelStatusFailed_actionListenerErrorToggleFollowButton() {
        FollowResponseModel followResponseModel = Mockito.mock(FollowResponseModel.class);
        when(followResponseModel.isSuccess()).thenReturn(false);

        followUnfollowKolSubscriber.onNext(followResponseModel);

        verify(followActionListener).onErrorToggleFollow(anyInt(), anyString());
    }

    @Test
    public void onFailed_followResponseModelStatusFailed_actionListenerErrorToggleFollowButton() {
        followUnfollowKolSubscriber.onError(new Throwable());

        verify(followActionListener).onErrorToggleFollow(anyInt(), anyString());
    }
}