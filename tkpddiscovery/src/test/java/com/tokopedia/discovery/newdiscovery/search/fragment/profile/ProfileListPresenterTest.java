package com.tokopedia.discovery.newdiscovery.search.fragment.profile;

import com.tokopedia.discovery.newdiscovery.domain.subscriber.GetProfileListSubscriber;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProfileListUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.subscriber.FollowUnfollowKolSubscriber;
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase;
import com.tokopedia.usecase.RequestParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProfileListPresenterTest {
    @Mock
    ProfileContract.View view;

    @Mock
    GetProfileListUseCase getProfileListUseCase;

    @Mock
    FollowKolPostGqlUseCase followKolPostGqlUseCase;

    @Mock
    FollowActionListener followActionListener;

    private ProfileListPresenter profileListPresenter;

    @Before
    public void setUp(){
        this.profileListPresenter = new ProfileListPresenter(
                getProfileListUseCase,
                followKolPostGqlUseCase
        );
        profileListPresenter.attachView(view);
        profileListPresenter.attachFollowActionListener(followActionListener);
    }

    @Test
    public void onRequestProfileListData_givenQueryAndPage_executeProfileListUseCase() {
        profileListPresenter.requestProfileListData(anyString(),anyInt());

        verify(getProfileListUseCase).execute(any(RequestParams.class), any(GetProfileListSubscriber.class));
    }

    @Test
    public void onHandleFollowAction_givenPositionAndProfileModel_executeFollowKolUseCase() {
        int mockAdapterPosition = 1;
        int mockUserIdToFollow = 14;
        boolean mockFollowed = false;

        profileListPresenter.handleFollowAction(mockAdapterPosition, mockUserIdToFollow, mockFollowed);

        verify(followKolPostGqlUseCase).execute(any(RequestParams.class), any(FollowUnfollowKolSubscriber.class));
    }
}