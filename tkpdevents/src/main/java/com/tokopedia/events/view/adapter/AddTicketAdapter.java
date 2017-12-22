package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.events.R;
import com.tokopedia.events.R2;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.utils.CurrencyUtil;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pranaymohapatra on 27/11/17.
 */

public class AddTicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PackageViewModel> packageViewModelList;
    Context mContext;
    EventBookTicketPresenter mPresenter;


    public AddTicketAdapter(Context context, List<PackageViewModel> data, EventBookTicketPresenter presenter) {
        packageViewModelList = data;
        mContext = context;
        mPresenter = presenter;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.add_tickets_layout, parent, false);
        TicketViewHolder holder = new TicketViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TicketViewHolder) holder).setViewHolder(packageViewModelList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return packageViewModelList.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.tv_ticket_name)
        TextView tvTicketName;
        @BindView(R2.id.tv_ticket_maxprice)
        TextView tvTicketMaxprice;
        @BindView(R2.id.ticket_sale_price)
        TextView ticketSalePrice;
        @BindView(R2.id.btn_decrement)
        ImageButton btnDecrement;
        @BindView(R2.id.tv_ticket_cnt)
        TextView tvTicketCnt;
        @BindView(R2.id.btn_increment)
        ImageButton btnIncrement;
        @BindView(R2.id.iv_sold_out)
        ImageView ivSoldOut;
        @BindView(R2.id.button_layout)
        View buttonLayout;

        PackageViewModel holderViewModel;
        int index;


        public TicketViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void setViewHolder(PackageViewModel viewModel, int position) {
            this.holderViewModel = viewModel;
            this.index = position;
            tvTicketName.setText(viewModel.getDisplayName());
            tvTicketMaxprice.setText(CurrencyUtil.convertToCurrencyString(viewModel.getMrp()));
            ticketSalePrice.setText(CurrencyUtil.convertToCurrencyString(viewModel.getSalesPrice()));
            tvTicketCnt.setText(String.valueOf(viewModel.getSelectedQuantity()));
            if (holderViewModel.getSelectedQuantity() > 0)
                btnDecrement.setVisibility(View.VISIBLE);
            else
                btnDecrement.setVisibility(View.INVISIBLE);
            if(holderViewModel.getAvailable()>0) {
                ivSoldOut.setVisibility(View.INVISIBLE);
                buttonLayout.setVisibility(View.VISIBLE);
            }
            else {
                ivSoldOut.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.INVISIBLE);
            }

        }

        @OnClick(R2.id.btn_increment)
        void onClickIncrement() {
            mPresenter.addTickets(index, holderViewModel, this);
            if (holderViewModel.getSelectedQuantity() > 0)
                btnDecrement.setVisibility(View.VISIBLE);
            else
                btnDecrement.setVisibility(View.INVISIBLE);
        }

        @OnClick(R2.id.btn_decrement)
        void onClickDecrement() {
            mPresenter.removeTickets();
            if (holderViewModel.getSelectedQuantity() > 0)
                btnDecrement.setVisibility(View.VISIBLE);
            else
                btnDecrement.setVisibility(View.INVISIBLE);
        }

        public void setTvTicketCnt(int count) {
            tvTicketCnt.setText(String.valueOf(count));
        }

        public void setTvTicketNameColor(int color) {
            tvTicketName.setTextColor(color);
        }

        public void setTicketSalePriceColor(int color) {
            ticketSalePrice.setTextColor(color);
        }

        public void setTvTicketCntColor(int color) {
            tvTicketCnt.setTextColor(color);
        }
    }
}
