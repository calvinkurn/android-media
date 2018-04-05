package com.tokopedia.loyalty.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.PromoDetailComponent;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.mapper.PromoDataMapper;
import com.tokopedia.loyalty.view.presenter.PromoDetailPresenter;
import com.tokopedia.loyalty.view.analytics.PromoDetailAnalytics;
import com.tokopedia.loyalty.view.view.IPromoDetailView;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoDetailFragment extends BaseDaggerFragment
        implements IPromoDetailView, RefreshHandler.OnRefreshHandlerListener {

    private static final String ARG_EXTRA_PROMO_FLAG = "flag";
    private static final String ARG_EXTRA_PROMO_DATA = "promo_data";
    private static final String ARG_EXTRA_PROMO_SLUG = "slug";

    private static final int DETAIL_PROMO_FROM_DATA = 0;
    private static final int DETAIL_PROMO_FROM_SLUG = 1;

    private RefreshHandler refreshHandler;

    private RelativeLayout rlContainerLayout;
    private TextView tvPromoDetailAction;
    private RecyclerView rvPromoDetailView;
    private LinearLayout llPromoDetailBottomLayout;
    private BottomSheetView bottomSheetInfoPromoCode;

    private String promoSlug;
    private OnFragmentInteractionListener actionListener;

    @Inject PromoDetailAnalytics promoDetailAnalytics;
    @Inject PromoDetailPresenter promoDetailPresenter;
    @Inject PromoDetailAdapter promoDetailAdapter;
    @Inject PromoDataMapper promoDataMapper;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(PromoDetailComponent.class).inject(this);
    }

    public PromoDetailFragment() {
        // Required empty public constructor
    }

    public static PromoDetailFragment newInstance(PromoData promoData) {
        PromoDetailFragment fragment = new PromoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EXTRA_PROMO_FLAG, DETAIL_PROMO_FROM_DATA);
        args.putParcelable(ARG_EXTRA_PROMO_DATA, promoData);
        fragment.setArguments(args);
        return fragment;
    }

    public static PromoDetailFragment newInstance(String slug) {
        PromoDetailFragment fragment = new PromoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EXTRA_PROMO_FLAG, DETAIL_PROMO_FROM_SLUG);
        args.putString(ARG_EXTRA_PROMO_SLUG, slug);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int flag = getArguments().getInt(ARG_EXTRA_PROMO_FLAG);

            if (flag == DETAIL_PROMO_FROM_DATA) {
                PromoData promoData = getArguments().getParcelable(ARG_EXTRA_PROMO_DATA);
                if (promoData != null) this.promoSlug = promoData.getSlug();
            } else if (flag == DETAIL_PROMO_FROM_SLUG) {
                this.promoSlug = getArguments().getString(ARG_EXTRA_PROMO_SLUG);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_detail, container, false);

        this.refreshHandler = new RefreshHandler(getActivity(), view, this);

        this.rlContainerLayout = view.findViewById(R.id.container);
        this.llPromoDetailBottomLayout = view.findViewById(R.id.ll_promo_detail_bottom_layout);
        this.tvPromoDetailAction = view.findViewById(R.id.tv_promo_detail_action);
        this.rvPromoDetailView = view.findViewById(R.id.rv_promo_detail_view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.llPromoDetailBottomLayout.setVisibility(View.VISIBLE);
        this.rvPromoDetailView.setAdapter(promoDetailAdapter);
        this.rvPromoDetailView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.rvPromoDetailView.setHasFixedSize(true);

        this.promoDetailAdapter.setAdapterListener(getAdapterActionListener());

        this.promoDetailPresenter.attachView(this);
        this.refreshHandler.startRefresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PromoDetailFragment.OnFragmentInteractionListener) {
            this.actionListener = (PromoDetailFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.promoDetailPresenter.detachView();
        this.actionListener = null;
    }

    @Override
    public void renderPromoDetail(PromoData promoData) {
        this.refreshHandler.finishRefresh();

        View errorView = this.rlContainerLayout.findViewById(com.tokopedia.core.R.id.main_retry);
        if (errorView != null) errorView.setVisibility(View.GONE);

        this.promoDetailAdapter.setPromoDetail(promoDataMapper.convert(promoData));
        this.promoDetailAdapter.notifyDataSetChanged();
        setFragmentLayout(promoData);
    }

    @Override
    public void renderErrorShowingPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorNoConnectionGetPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorTimeoutConnectionGetPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    @Override
    public void renderErrorHttpGetPromoDetail(String message) {
        handleErrorEmptyState(message);
    }

    private PromoDetailAdapter.OnAdapterActionListener getAdapterActionListener() {
        return new PromoDetailAdapter.OnAdapterActionListener() {
            @Override
            public void onItemPromoShareClicked(PromoData promoData) {
                actionListener.onSharePromo(promoData);
            }

            @Override
            public void onItemPromoCodeCopyClipboardClicked(String promoCode) {
                String message = getString(R.string.voucher_code_copy_to_clipboard);

                if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                UnifyTracking.eventPromoListClickCopyToClipboardPromoCode(promoCode);
                ClipboardManager clipboard = (ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("CLIP_DATA_LABEL_VOUCHER_PROMO", promoCode);

                if (clipboard != null) clipboard.setPrimaryClip(clip);
            }

            @Override
            public void onItemPromoCodeTooltipClicked() {
                if (bottomSheetInfoPromoCode == null) {
                    bottomSheetInfoPromoCode = new BottomSheetView(getActivity());

                    bottomSheetInfoPromoCode.renderBottomSheet(new BottomSheetView.BottomSheetField
                            .BottomSheetFieldBuilder()
                            .setTitle(getString(R.string.bottom_sheet_title_promo_tooltips))
                            .setBody(getString(R.string.bottom_sheet_body_promo_tooltips))
                            .setImg(R.drawable.ic_promo)
                            .build());

                    bottomSheetInfoPromoCode.setListener(new BottomSheetView.ActionListener() {
                        @Override
                        public void clickOnTextLink(String url) {

                        }

                        @Override
                        public void clickOnButton(String url, String appLink) {
                        }
                    });
                }

                bottomSheetInfoPromoCode.show();
            }

            @Override
            public void onWebViewLinkClicked(String url) {
                if (getActivity().getApplication() instanceof TkpdCoreRouter) {
                    TkpdCoreRouter tkpdCoreRouter = (TkpdCoreRouter) getActivity().getApplication();
                    tkpdCoreRouter.actionOpenGeneralWebView(getActivity(), url);
                }
            }
        };
    }

    private void setFragmentLayout(final PromoData promoData) {
        this.tvPromoDetailAction.setText(promoData.getCtaText());
        this.tvPromoDetailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String appLink = promoData.getAppLink();
                String redirectUrl = promoData.getPromoLink();

                if (getActivity().getApplication() instanceof TkpdCoreRouter) {
                    TkpdCoreRouter tkpdCoreRouter = (TkpdCoreRouter) getActivity().getApplication();

                    if (!TextUtils.isEmpty(appLink) && tkpdCoreRouter.isSupportedDelegateDeepLink(appLink)) {
                        tkpdCoreRouter.actionApplinkFromActivity(getActivity(), appLink);
                    } else {
                        tkpdCoreRouter.actionOpenGeneralWebView(getActivity(), redirectUrl);
                    }
                }
            }
        });
    }

    private void handleErrorEmptyState(String message) {
        if (refreshHandler.isRefreshing()) refreshHandler.finishRefresh();

        NetworkErrorHelper.showEmptyState(getActivity(), this.rlContainerLayout, message,
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        refreshHandler.startRefresh();
                    }
                });
    }

    @Override
    public void onRefresh(View view) {
        this.promoDetailPresenter.getPromoDetail(promoSlug);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {

        void onSharePromo(PromoData promoData);

    }
}