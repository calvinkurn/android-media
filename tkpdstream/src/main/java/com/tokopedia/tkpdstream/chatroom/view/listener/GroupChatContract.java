package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;
import android.support.annotation.Nullable;

import com.sendbird.android.OpenChannel;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdstream.chatroom.domain.usecase.LoginGroupChatUseCase;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.common.analytics.EEPromotion;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

import java.util.List;

/**
 * @author by nisie on 3/21/18.
 */

public interface GroupChatContract {

    interface View extends CustomerView {

        Context getContext();

        void onErrorGetChannelInfo(String errorMessage);

        void onSuccessGetChannelInfo(ChannelInfoViewModel channelInfoViewModel);

        void updateVoteViewModel(VoteInfoViewModel voteInfoViewModel, String voteType);

        void setChannelHandler();

        void showInfoDialog();

        void updateSprintSaleData(SprintSaleAnnouncementViewModel messageItem);

        void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem, String voteType);

        @Nullable
        SprintSaleViewModel getSprintSaleViewModel();

        @Nullable
        ChannelInfoViewModel getChannelInfoViewModel();

        void eventClickComponentEnhancedEcommerce(String componentType, String campaignName, String
                attributeName, List<EEPromotion> list);

        void eventViewComponentEnhancedEcommerce(String componentType, String campaignName, String
                attributeName, List<EEPromotion> list);

        String generateAttributeApplink(String applink,
                                        String attributeBanner);

        void vibratePhone();

        void onSuccessRefreshChannelInfo(ChannelInfoViewModel channelInfoViewModel);

        String getAttributionTracking(String attributePartnerLogo);
    }

    interface Presenter extends CustomerPresenter<GroupChatContract.View> {

        void getChannelInfo(String channelUuid);

        void logoutChannel(OpenChannel mChannel);

        void enterChannel(String userId, String channelUrl, String userName, String userAvatar,
                          LoginGroupChatUseCase.LoginGroupChatListener
                                  loginGroupChatListener, String sendBirdToken);

        void refreshChannelInfo(String channelUuid);
    }
}
