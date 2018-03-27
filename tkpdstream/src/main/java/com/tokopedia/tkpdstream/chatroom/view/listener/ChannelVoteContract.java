package com.tokopedia.tkpdstream.chatroom.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

/**
 * @author by nisie on 2/6/18.
 */

public interface ChannelVoteContract {

    interface View extends CustomerView {

        Context getContext();

        void showHasVoted();

        void showSuccessVoted();

        void onSuccessVote(VoteViewModel element, VoteStatisticViewModel voteStatisticViewModel);

        void onErrorVote(String errorMessage);

        void redirectToLogin();

        interface VoteOptionListener{
            void onVoteOptionClicked(VoteViewModel element);
        }

        interface VoteAnnouncementViewHolderListener {
            void onVoteComponentClicked(String type, String name);
        }
    }

    interface Presenter extends CustomerPresenter<View> {

        void sendVote(UserSession userSession, String pollId, boolean voted, VoteViewModel element);

    }
}
