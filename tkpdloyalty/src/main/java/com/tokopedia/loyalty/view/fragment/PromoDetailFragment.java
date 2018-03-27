package com.tokopedia.loyalty.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
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

public class PromoDetailFragment extends BaseDaggerFragment
        implements PromoDetailAdapter.OnAdapterActionListener {

    private static final String ARG_EXTRA_PROMO_DATA = "promo_data";

    private TextView tvPromoDetailAction;
    private CardView cvPromoDetailBottomContainer;
    private RecyclerView rvPromoDetailView;
    private LinearLayout llPromoDetailBottomLayout;

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
            PromoData promoData = getArguments().getParcelable(ARG_EXTRA_PROMO_DATA);
            this.promoDetailObjectList = promoDataMapper.convert(promoData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo_detail, container, false);

        this.tvPromoDetailAction = view.findViewById(R.id.tv_promo_detail_action);
        this.cvPromoDetailBottomContainer = view.findViewById(R.id.cv_promo_detail_bottom_container);
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
        this.promoDetailAdapter.notifyDataSetChanged();

        this.cvPromoDetailBottomContainer.setVisibility(View.VISIBLE);
        this.llPromoDetailBottomLayout.setVisibility(View.GONE);
        this.tvPromoDetailAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(PromoData promoData) {
        if (this.actionListener != null) {
            this.actionListener.onSelectPromo(promoData);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSelectPromo(PromoData promoData);
    }
}