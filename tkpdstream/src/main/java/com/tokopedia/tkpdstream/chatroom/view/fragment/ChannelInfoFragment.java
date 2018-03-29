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
import com.tokopedia.tkpdstream.StreamModuleRouter;
import com.tokopedia.tkpdstream.chatroom.di.DaggerChatroomComponent;
import com.tokopedia.tkpdstream.chatroom.view.adapter.chatroom.ChannelPartnerAdapter;
import com.tokopedia.tkpdstream.chatroom.view.listener.ChannelInfoFragmentListener;
import com.tokopedia.tkpdstream.chatroom.view.listener.GroupChatContract;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerChildViewModel;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.chatroom.ChannelPartnerViewModel;
import com.tokopedia.tkpdstream.common.analytics.EEPromotion;
import com.tokopedia.tkpdstream.common.di.component.DaggerStreamComponent;
import com.tokopedia.tkpdstream.common.di.component.StreamComponent;
import com.tokopedia.tkpdstream.common.analytics.StreamAnalytics;
import com.tokopedia.tkpdstream.common.util.TextFormatter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by milhamj on 20/03/18.
 */

public class ChannelInfoFragment extends BaseDaggerFragment
        implements ChannelInfoFragmentListener.View,
        ChannelInfoFragmentListener.View.ChannelPartnerViewHolderListener {

    @Inject
    StreamAnalytics analytics;

    public static final String ARGS_CI_VIEW_MODEL = "CI_VIEW_MODEL";

    private ChannelInfoViewModel channelInfoViewModel;

    private View rootView;
    private ImageView profile;
    private TextView title;
    private TextView subtitle;
    private TextView name;
    private TextView totalView;
    private RecyclerView channelPartners;

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChannelInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            channelInfoViewModel = savedInstanceState.getParcelable(ARGS_CI_VIEW_MODEL);
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
        outState.putParcelable(ARGS_CI_VIEW_MODEL, channelInfoViewModel);
    }

    @Override
    protected String getScreenName() {
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
    public void renderData(ChannelInfoViewModel channelInfoViewModel) {
        this.channelInfoViewModel = channelInfoViewModel;
        populateData();
    }

    @Override
    public void channelPartnerClicked(ChannelPartnerChildViewModel channelPartnerChildViewModel,
                                      int position) {
        ArrayList<EEPromotion> list = new ArrayList<>();
        list.add(new EEPromotion(channelPartnerChildViewModel.getPartnerId(),
                EEPromotion.NAME_GROUPCHAT, position,
                channelPartnerChildViewModel.getPartnerName(),
                channelPartnerChildViewModel.getPartnerAvatar(),
                ((GroupChatContract.View) getActivity()).getAttributionTracking(StreamAnalytics
                        .ATTRIBUTE_PARTNER_LOGO)
        ));

        ((GroupChatContract.View) getActivity()).eventClickComponentEnhancedEcommerce(StreamAnalytics
                .COMPONENT_PARTNER, channelPartnerChildViewModel.getPartnerName(), StreamAnalytics
                .ATTRIBUTE_PARTNER_LOGO, list);

        StreamModuleRouter router = ((StreamModuleRouter) getActivity().getApplicationContext());
        router.openRedirectUrl(getActivity(), ((GroupChatContract.View) getActivity())
                .generateAttributeApplink(channelPartnerChildViewModel.getPartnerUrl(),
                        StreamAnalytics.ATTRIBUTE_PARTNER_LOGO));
    }

    private void initView(View view) {
        KeyboardHandler.DropKeyboard(getContext(), getView());
        profile = view.findViewById(R.id.prof_pict);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        name = view.findViewById(R.id.name);
        totalView = view.findViewById(R.id.participant);
        channelPartners = view.findViewById(R.id.channel_partners);
    }

    private void setViewListener() {
    }

    private void populateData() {
        if (rootView == null || channelInfoViewModel == null) {
            return;
        }

        trackEEViewPartner(channelInfoViewModel);

        totalView.setText(TextFormatter.format(String.valueOf(channelInfoViewModel.getTotalView())));
        name.setText(channelInfoViewModel.getAdminName());
        title.setText(channelInfoViewModel.getTitle());
        subtitle.setText(channelInfoViewModel.getDescription());

        ImageHandler.loadImageCircle2(profile.getContext(),
                profile,
                channelInfoViewModel.getAdminPicture(),
                R.drawable.loading_page);

        if (channelInfoViewModel.getChannelPartnerViewModels() != null
                && !channelInfoViewModel.getChannelPartnerViewModels().isEmpty()) {
            channelPartners.setNestedScrollingEnabled(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL,
                    false);
            channelPartners.setLayoutManager(linearLayoutManager);

            ChannelPartnerAdapter channelPartnerAdapter =
                    ChannelPartnerAdapter.createInstance(this);
            channelPartnerAdapter.setList(channelInfoViewModel.getChannelPartnerViewModels());
            channelPartners.setAdapter(channelPartnerAdapter);
        }
    }

    private void trackEEViewPartner(ChannelInfoViewModel channelInfoViewModel) {
        ArrayList<EEPromotion> list = new ArrayList<>();
        for (ChannelPartnerViewModel partnerViewModel : channelInfoViewModel
                .getChannelPartnerViewModels()) {

            for (ChannelPartnerChildViewModel channelPartnerChildViewModel : partnerViewModel
                    .getChild()) {
                list.add(new EEPromotion(channelPartnerChildViewModel.getPartnerId(),
                        EEPromotion.NAME_GROUPCHAT, list.size() + 1,
                        channelPartnerChildViewModel.getPartnerName(),
                        channelPartnerChildViewModel.getPartnerAvatar(),
                        ((GroupChatContract.View) getActivity()).getAttributionTracking(StreamAnalytics
                                .ATTRIBUTE_PARTNER_LOGO)
                ));
            }
        }

        ((GroupChatContract.View) getActivity()).eventViewComponentEnhancedEcommerce(StreamAnalytics
                .COMPONENT_PARTNER, StreamAnalytics.VIEW_LOGO, StreamAnalytics.ATTRIBUTE_PARTNER_LOGO, list);
    }
}
