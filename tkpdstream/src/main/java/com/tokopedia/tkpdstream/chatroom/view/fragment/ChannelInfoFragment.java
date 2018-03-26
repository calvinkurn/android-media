package com.tokopedia.tkpdstream.chatroom.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.channel.view.model.ChannelViewModel;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.ChannelPartnerAdapter;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.util.TextFormatter;

/**
 * @author by milhamj on 20/03/18.
 */

public class ChannelInfoFragment extends BaseDaggerFragment
        implements ChannelInfoFragmentListener.View {
    public static final String ARGS_CI_VIEW_MODEL = "CI_VIEW_MODEL";

    private ChannelViewModel channelViewModel;

    private View rootView;
    private ImageView profile;
    private TextView title;
    private TextView subtitle;
    private TextView name;
    private TextView participant;
    private RecyclerView channelPartners;
    private ChannelPartnerAdapter channelPartnerAdapter;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChannelInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            channelViewModel = savedInstanceState.getParcelable(ARGS_CI_VIEW_MODEL);
        }
    }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARGS_CI_VIEW_MODEL, channelViewModel);
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
        KeyboardHandler.DropKeyboard(getContext(), getView());
        profile = view.findViewById(R.id.prof_pict);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        name = view.findViewById(R.id.name);
        participant = view.findViewById(R.id.participant);
        channelPartners = view.findViewById(R.id.channel_partners);
    }

    private void setViewListener() {
    }

    private void populateData() {
        if (rootView == null || channelViewModel == null) {
            return;
        }

        participant.setText(TextFormatter.format(String.valueOf(channelViewModel.getParticipant())));
        name.setText(channelViewModel.getAdminName());
        title.setText(channelViewModel.getTitle());
        subtitle.setText(channelViewModel.getDescription());

        ImageHandler.loadImageCircle2(profile.getContext(),
                profile,
                channelViewModel.getAdminPicture(),
                R.drawable.loading_page);

        if (!channelViewModel.getChannelPartnerViewModels().isEmpty()) {
            channelPartners.setNestedScrollingEnabled(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);
            channelPartners.setLayoutManager(linearLayoutManager);

            channelPartnerAdapter = ChannelPartnerAdapter.createInstance();
            channelPartnerAdapter.setList(channelViewModel.getChannelPartnerViewModels());
            channelPartners.setAdapter(channelPartnerAdapter);
        }
    }
}
