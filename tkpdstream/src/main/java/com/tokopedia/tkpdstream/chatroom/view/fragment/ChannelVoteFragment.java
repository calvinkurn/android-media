package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.channel.view.ProgressBarWithTimer;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelVoteContract;
import com.tokopedia.tkpdstream.chatroom.view.presenter.ChannelVotePresenter;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.VoteAnnouncementViewModel;
import com.tokopedia.tkpdstream.common.design.CloseableBottomSheetDialog;
import com.tokopedia.tkpdstream.common.design.SpaceItemDecoration;
import com.tokopedia.tkpdstream.common.util.StreamAnalytics;
import com.tokopedia.tkpdstream.common.util.TextFormatter;
import com.tokopedia.tkpdstream.vote.view.adapter.VoteAdapter;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactory;
import com.tokopedia.tkpdstream.vote.view.adapter.typefactory.VoteTypeFactoryImpl;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteStatisticViewModel;
import com.tokopedia.tkpdstream.vote.view.model.VoteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.tkpdstream.chatroom.view.activity.GroupChatActivity.VOTE;

/**
 * @author by StevenFredian on 20/03/18.
 */

public class ChannelVoteFragment extends BaseDaggerFragment implements ChannelVoteContract.View
        , ProgressBarWithTimer.Listener, ChannelVoteContract.View.VoteOptionListener {

    private RecyclerView voteRecyclerView;

    @Inject
    ChannelVotePresenter presenter;

    @Inject
    StreamAnalytics analytics;

    private View loading;

    private View voteBar;
    private View voteBody;
    private TextView voteTitle;
    private TextView voteParticipant;
    private TextView voteInfoLink;
    private ImageView iconVote;
    private View votedView;
    private TextView voteStatus;
    private CloseableBottomSheetDialog channelInfoDialog;

    private VoteInfoViewModel voteInfoViewModel;
    private VoteAdapter voteAdapter;
    private ProgressBarWithTimer progressBarWithTimer;

    @Override
    protected String getScreenName() {
        return null;
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChannelVoteFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_vote, container, false);

        channelInfoDialog = CloseableBottomSheetDialog.createInstance(getActivity());
        channelInfoDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        loading = view.findViewById(R.id.loading);
        voteRecyclerView = view.findViewById(R.id.vote_list);
        voteBar = view.findViewById(R.id.vote_header);
        voteBody = view.findViewById(R.id.vote_body);
        voteTitle = view.findViewById(R.id.vote_title);
        voteParticipant = view.findViewById(R.id.vote_participant);
        voteInfoLink = view.findViewById(R.id.vote_info_link);
        iconVote = view.findViewById(R.id.icon_vote);
        voteStatus = view.findViewById(R.id.vote_status);
        votedView = view.findViewById(R.id.layout_voted);
        progressBarWithTimer = view.findViewById(R.id.timer);

        KeyboardHandler.DropKeyboard(getActivity(), progressBarWithTimer);
        prepareView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarWithTimer.setListener(this);
        Parcelable temp = getArguments().getParcelable(VOTE);

        showVoteLayout((VoteInfoViewModel) temp);
    }


    private void prepareView() {
        VoteTypeFactory voteTypeFactory = new VoteTypeFactoryImpl(this);
        voteAdapter = VoteAdapter.createInstance(voteTypeFactory);
    }

    public void expand(final View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void collapse(final View v) {
        v.setVisibility(View.GONE);
    }

    public void showVoteLayout(final VoteInfoViewModel model) {
        Log.d("NISNIS", "showVoteLayout");

        this.voteInfoViewModel = model;
        loading.setVisibility(View.GONE);

        voteBody.setVisibility(View.VISIBLE);
        voteBar.setVisibility(View.VISIBLE);

        LinearLayoutManager voteLayoutManager;
        RecyclerView.ItemDecoration itemDecoration = null;
        if (voteInfoViewModel.getVoteOptionType().equals(VoteViewModel.IMAGE_TYPE)) {
            voteLayoutManager = new GridLayoutManager(getActivity(), 2);
            itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_mini), 2);
        } else {
            voteLayoutManager = new LinearLayoutManager(getActivity());
            itemDecoration = new SpaceItemDecoration((int) getActivity().getResources().getDimension(R.dimen.space_between), false);
        }
        voteRecyclerView.addItemDecoration(itemDecoration);
        voteRecyclerView.setLayoutManager(voteLayoutManager);
        voteRecyclerView.setAdapter(voteAdapter);
        voteTitle.setText(voteInfoViewModel.getTitle());

        voteAdapter.addList(voteInfoViewModel.getListOption());

        if (voteInfoViewModel.isVoted()) {
            setVoted();
        }

        voteParticipant.setText(String.format("%s %s", TextFormatter.format(voteInfoViewModel.getParticipant())
                , getActivity().getString(R.string.voter)));

        voteInfoLink.setText(voteInfoViewModel.getVoteInfoStringResId());
        voteInfoLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StreamModuleRouter) getActivity().getApplicationContext()).openRedirectUrl
                        (getActivity(), voteInfoViewModel.getVoteInfoUrl());
            }
        });

        if (this.voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FINISH
                || this.voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_FINISH) {
            progressBarWithTimer.setVisibility(View.GONE);
            progressBarWithTimer.cancel();
            setVoteHasEnded();
        } else if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_CANCELED) {
            hideVoteLayout();
        } else {
            progressBarWithTimer.setVisibility(View.VISIBLE);
            progressBarWithTimer.cancel();
            progressBarWithTimer.setTimer(voteInfoViewModel.getStartTime(), voteInfoViewModel.getEndTime());
        }

    }

//    private void updateVoteViewModel(VoteInfoViewModel model, String voteType) {
//        if (voteInfoViewModel != null) {
//            if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FINISH
//                    || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_FINISH
//                    || voteType.equals(VoteAnnouncementViewModel.POLLING_UPDATE)) {
//                boolean isVoted = voteInfoViewModel.isVoted();
//                List<Visitable> tempListOption = new ArrayList<>();
//                tempListOption.addAll(voteInfoViewModel.getListOption());
//                for (int i = 0; i < voteInfoViewModel.getListOption().size(); i++) {
//                    if (voteInfoViewModel.getListOption().get(i) instanceof VoteViewModel) {
//                        ((VoteViewModel) voteInfoViewModel.getListOption().get(i)).setSelected(
//                                ((VoteViewModel) (tempListOption.get(i))).getSelected());
//                    }
//                }
//                voteInfoViewModel.setVoted(isVoted);
//                voteInfoViewModel = model;
//
//            } else {
//                voteInfoViewModel = model;
//            }
//        }
//    }

    public void hideVoteLayout() {
        voteBar.setVisibility(View.GONE);
        voteBody.setVisibility(View.GONE);
    }

    public void setVoteHasEnded() {
        if (getActivity() != null) {
            progressBarWithTimer.setVisibility(View.GONE);
            voteStatus.setText(R.string.vote_has_ended);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ImageHandler.loadImageWithIdWithoutPlaceholder(iconVote, R.drawable.ic_timer_inactive);
            } else {
                iconVote.setImageResource(R.drawable.ic_timer_inactive);
            }
            voteAdapter.updateStatistic();
        }
    }

    public void setVoteStarted() {
        if (getActivity() != null) {
            progressBarWithTimer.setVisibility(View.VISIBLE);
            voteStatus.setText(R.string.time_remaining);
            voteStatus.setTextColor(MethodChecker.getColor(getActivity(), R.color.medium_green));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ImageHandler.loadImageWithIdWithoutPlaceholder(iconVote, R.drawable.ic_timer);
            } else {
                iconVote.setImageResource(R.drawable.ic_timer);
            }
        }
    }

    public void setVoted() {
        votedView.setVisibility(View.VISIBLE);
    }

//    private void handleVoteAnnouncement(VoteAnnouncementViewModel messageItem) {
//
//        switch (messageItem.getVoteType()) {
//            case VoteAnnouncementViewModel.POLLING_START:
//                votedView.setVisibility(View.GONE);
//                showVoteLayout(messageItem.getVoteInfoViewModel(), messageItem.getVoteType());
//                break;
//            case VoteAnnouncementViewModel.POLLING_UPDATE:
//                showVoteLayout(messageItem.getVoteInfoViewModel(), messageItem.getVoteType());
//                break;
//            case VoteAnnouncementViewModel.POLLING_FINISHED:
//                showVoteLayout(messageItem.getVoteInfoViewModel(), messageItem.getVoteType());
//                break;
//            case VoteAnnouncementViewModel.POLLING_CANCEL:
//                hideVoteLayout();
//                break;
//        }
//    }

    @Override
    public void onVoteOptionClicked(VoteViewModel element) {
        if (voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_ACTIVE
                || voteInfoViewModel.getStatusId() == VoteInfoViewModel.STATUS_FORCE_ACTIVE) {
            boolean voted = (votedView.getVisibility() == View.VISIBLE);
            presenter.sendVote(voteInfoViewModel.getPollId(), voted, element);
//            analytics.eventClickVote(element.getType(), toolbar.getTitle().toString());
        }
    }

    @Override
    public void showHasVoted() {
        View view = getLayoutInflater().inflate(R.layout.has_voted_bottom_sheet_dialog, null);
        TextView title = view.findViewById(R.id.title);
        title.setText(R.string.has_voted);
        channelInfoDialog.setContentView(view);
        channelInfoDialog.show();
    }

    @Override
    public void showSuccessVoted() {

        View view = getLayoutInflater().inflate(R.layout.has_voted_bottom_sheet_dialog, null);
        channelInfoDialog.setContentView(view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                channelInfoDialog.show();
            }
        }, 1000);
    }

    @Override
    public void onSuccessVote(VoteViewModel element, VoteStatisticViewModel voteStatisticViewModel) {
        if (voteInfoViewModel != null) {
            voteAdapter.change(voteInfoViewModel, element, voteStatisticViewModel);
            voteInfoViewModel.setVoted(true);
            voteInfoViewModel.setParticipant(
                    String.valueOf(Integer.parseInt(voteStatisticViewModel.getTotalParticipants())));
            setVoted();

            voteParticipant.setText(String.format("%s %s", TextFormatter.format(voteInfoViewModel.getParticipant())
                    , getActivity().getString(R.string.voter)));
        }
    }


    @Override
    public void onErrorVote(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void onStartTick() {
        setVoteStarted();
    }

    @Override
    public void onFinishTick() {
        setVoteHasEnded();
    }
}
