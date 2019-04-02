package com.tokopedia.discovery.newdiscovery.domain.subscriber;


import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileContract;
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileListViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GetProfileListSubscriberTest {
    @Mock
    ProfileContract.View view;

    private GetProfileListSubscriber getProfileListSubscriber;

    @Before
    public void setUp(){
        this.getProfileListSubscriber = new GetProfileListSubscriber(
                view
        );
    }

    @Test
    public void onNext_profileListViewModel_renderedToView() {
        ProfileListViewModel profileListViewModel = Mockito.mock(ProfileListViewModel.class);
        getProfileListSubscriber.onNext(profileListViewModel);

        verify(view).onSuccessGetProfileListData(profileListViewModel);
    }

    @Test
    public void onError_profileListViewModel_renderedToView() {
        Throwable error = Mockito.mock(Throwable.class);

        getProfileListSubscriber.onError(error);

        verify(view).onErrorGetProfileListData(error);
    }
}