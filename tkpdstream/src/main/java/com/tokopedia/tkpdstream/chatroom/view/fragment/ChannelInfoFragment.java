package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.TextFormatter;
import com.tokopedia.tkpdstream.vote.view.model.VoteInfoViewModel;

/**
 * @author by milhamj on 20/03/18.
 */

public class ChannelInfoFragment extends BaseDaggerFragment
        implements ChannelInfoFragmentListener.View {
    private ChannelViewModel channelViewModel;

    private View rootView;
    private TextView actionButton;
    private ImageView image;
    private ImageView profile;
    private TextView title;
    private TextView subtitle;
    private TextView name;
    private TextView participant;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_channel_info, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setViewListener();
        populateData();
    }

    @Override
    protected String getScreenName() {
        //TODO milhamj screen name
        return null;
    }

    @Override
    protected void initInjector() {
        StreamComponent streamComponent = DaggerStreamComponent
                .builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication())
                                .getBaseAppComponent())
                .build();

        DaggerChatroomComponent.builder()
                .streamComponent(streamComponent)
                .build()
                .inject(this);
    }

    @Override
    public void renderData(ChannelViewModel channelViewModel) {
        this.channelViewModel = channelViewModel;
        populateData();
    }

    private void initView(View view) {
        actionButton = view.findViewById(R.id.action_button);
        image = view.findViewById(R.id.product_image);
        profile = view.findViewById(R.id.prof_pict);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        name = view.findViewById(R.id.name);
        participant = view.findViewById(R.id.participant);
    }

    private void setViewListener() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO milhamj
            }
        });
    }

    private void populateData() {
        if (rootView == null || channelViewModel == null) {
            return;
        }

        participant.setText(TextFormatter.format(String.valueOf(channelViewModel.getParticipant())));
        name.setText(channelViewModel.getAdminName());
        title.setText(channelViewModel.getTitle());
        subtitle.setText(channelViewModel.getDescription());
        ImageHandler.loadImage2(image,
                channelViewModel.getImage(),
                R.drawable.loading_page);
        ImageHandler.loadImageCircle2(profile.getContext(),
                profile,
                channelViewModel.getAdminPicture(),
                R.drawable.loading_page);
    }

    private boolean checkPollValid(boolean hasPoll, VoteInfoViewModel voteInfoViewModel) {
        return (hasPoll
                && voteInfoViewModel != null
                && voteInfoViewModel.getStartTime() != 0
                && voteInfoViewModel.getEndTime() != 0
                && voteInfoViewModel.getStartTime() < voteInfoViewModel.getEndTime());
    }
}
