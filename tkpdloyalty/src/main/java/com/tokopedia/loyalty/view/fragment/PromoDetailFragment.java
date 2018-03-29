package com.tokopedia.loyalty.view.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.di.component.PromoDetailComponent;
import com.tokopedia.loyalty.view.adapter.PromoDetailAdapter;
import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.mapper.PromoDataMapper;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 23/03/18
 */

public class PromoDetailFragment extends BaseDaggerFragment {

    private static final String ARG_EXTRA_PROMO_DATA = "promo_data";

    private static final int REQUEST_CODE_PROMO_DETAIL = 118;

    private TextView tvPromoDetailAction;
    private RecyclerView rvPromoDetailView;
    private LinearLayout llPromoDetailBottomLayout;
    private BottomSheetView bottomSheetInfoPromoCode;

    private PromoData promoData;
    private List<Object> promoDetailObjectList;
    private PromoDetailFragment.OnFragmentInteractionListener actionListener;

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
        args.putParcelable(ARG_EXTRA_PROMO_DATA, promoData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.promoData = getArguments().getParcelable(ARG_EXTRA_PROMO_DATA);
            this.promoDetailObjectList = promoDataMapper.convert(this.promoData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo_detail, container, false);

        this.tvPromoDetailAction = view.findViewById(R.id.tv_promo_detail_action);
        this.rvPromoDetailView = view.findViewById(R.id.rv_promo_detail_view);
        this.llPromoDetailBottomLayout = view.findViewById(R.id.ll_promo_detail_bottom_layout);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.rvPromoDetailView.setAdapter(promoDetailAdapter);
        this.rvPromoDetailView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.rvPromoDetailView.setHasFixedSize(true);
        this.rvPromoDetailView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (rvPromoDetailView != null) {
                    boolean isEndOfScrolling = rvPromoDetailView.canScrollVertically(1);
                    llPromoDetailBottomLayout.setVisibility(isEndOfScrolling ? View.GONE : View.VISIBLE);
                }
            }
        });

        this.promoDetailAdapter.setPromoDetail(promoDetailObjectList);
        this.promoDetailAdapter.setAdapterActionListener(getAdapterActionListener());
        this.promoDetailAdapter.notifyDataSetChanged();

        this.llPromoDetailBottomLayout.setVisibility(View.GONE);
        this.tvPromoDetailAction.setText(this.promoData.getCtaText());
        this.tvPromoDetailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = TextUtils.isEmpty(promoData.getAppLink()) ? promoData.getPromoLink()
                        : promoData.getAppLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                startActivityForResult(browserIntent, REQUEST_CODE_PROMO_DETAIL);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PROMO_DETAIL) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(PromoData promoData) {
        if (this.actionListener != null) {
            this.actionListener.onSharePromo(promoData);
        }
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
        this.actionListener = null;
    }

    private PromoDetailAdapter.OnAdapterActionListener getAdapterActionListener() {
        return new PromoDetailAdapter.OnAdapterActionListener() {
            @Override
            public void onItemPromoShareClicked(PromoData promoData) {
                actionListener.onSharePromo(promoData);
            }

            @Override
            public void onItemPromoCodeCopyClipboardClicked(String promoCode) {
                String message = "Kode Voucher telah tersalin";

                if (getView() != null) Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
                else Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                UnifyTracking.eventPromoListClickCopyToClipboardPromoCode(promoCode);
                ClipboardManager clipboard = (ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("CLIP_DATA_LABEL_VOUCHER_PROMO", promoCode);

                if (clipboard != null) clipboard.setPrimaryClip(clip);

                promoDetailAdapter.notifyItemChanged(2);
            }

            @Override
            public void onItemPromoCodeTooltipClicked() {
                UnifyTracking.eventPromoTooltipClickOpenTooltip();
                if (bottomSheetInfoPromoCode == null) {
                    bottomSheetInfoPromoCode = new BottomSheetView(getActivity());

                    bottomSheetInfoPromoCode.renderBottomSheet(new BottomSheetView.BottomSheetField
                            .BottomSheetFieldBuilder()
                            .setTitle("Kode Promo")
                            .setBody("Masukan Kode Promo di halaman pembayaran")
                            .setImg(R.drawable.ic_promo)
                            .build());

                    bottomSheetInfoPromoCode.setListener(new BottomSheetView.ActionListener() {
                        @Override
                        public void clickOnTextLink(String url) {

                        }

                        @Override
                        public void clickOnButton(String url, String appLink) {
                            UnifyTracking.eventPromoTooltipClickCloseTooltip();
                        }
                    });
                }

                bottomSheetInfoPromoCode.show();
            }
        };
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