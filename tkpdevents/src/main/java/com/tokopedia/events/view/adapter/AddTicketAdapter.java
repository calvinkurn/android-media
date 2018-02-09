package com.tokopedia.events.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        View v = inflater.inflate(R.layout.add_tickets_layout, parent, false);
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
        @BindView(R2.id.ticket_sale_price)
        TextView ticketSalePrice;
        @BindView(R2.id.btn_decrement)
        ImageButton btnDecrement;
        @BindView(R2.id.tv_ticket_cnt)
        TextView tvTicketCnt;
        @BindView(R2.id.btn_increment)
        ImageButton btnIncrement;
        @BindView(R2.id.tv_sold_out)
        TextView tvSoldOut;
        @BindView(R2.id.button_layout)
        View buttonLayout;
        @BindView(R2.id.tv_ticket_description)
        TextView tickeyDescriptionText;
        @BindView(R2.id.maks_ticket)
        TextView maksTicket;

        PackageViewModel holderViewModel;
        int index;

        View thisView;


        public TicketViewHolder(View view) {
            super(view);
            thisView = view;
            ButterKnife.bind(this, view);
        }

        void setViewHolder(PackageViewModel viewModel, int position) {
            this.holderViewModel = viewModel;
            this.index = position;
            tvTicketName.setText(viewModel.getDisplayName());
//            tvTicketMaxprice.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(viewModel.getMrp()));
            if (viewModel.getDescription() != null && viewModel.getDescription().length() > 0)
                tickeyDescriptionText.setText(viewModel.getDescription());
            else
                tickeyDescriptionText.setVisibility(View.GONE);
            ticketSalePrice.setText("Rp" + " " + CurrencyUtil.convertToCurrencyString(viewModel.getSalesPrice()));
            tvTicketCnt.setText(String.valueOf(viewModel.getSelectedQuantity()));
            if (holderViewModel.getSelectedQuantity() > 0) {
                //btnDecrement.setAlpha(1.0f);
                btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_green));
                btnDecrement.setClickable(true);
            } else {
                //btnDecrement.setAlpha(0.2f);
                btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_grey));
                btnDecrement.setClickable(false);
            }
            if (holderViewModel.getAvailable() > 0) {
                tvSoldOut.setVisibility(View.INVISIBLE);
                buttonLayout.setVisibility(View.VISIBLE);
                setTvTicketNameColor(mContext.getResources().getColor(R.color.black_70));
                setTickeyDescriptionColor(mContext.getResources().getColor(R.color.black_54));
                setTicketSalePriceColor(mContext.getResources().getColor(R.color.price_pdp));
            } else {
                tvSoldOut.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.INVISIBLE);
                setTvTicketNameColor(mContext.getResources().getColor(R.color.black_38));
                setTickeyDescriptionColor(mContext.getResources().getColor(R.color.black_38));
                setTicketSalePriceColor(mContext.getResources().getColor(R.color.black_38));
            }

        }

        @OnClick(R2.id.btn_increment)
        void onClickIncrement() {
            mPresenter.addTickets(index, holderViewModel, this);
            if (holderViewModel.getSelectedQuantity() > 0) {
                btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_green));
                btnDecrement.setClickable(true);
            } else {
                btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_grey));
                btnDecrement.setClickable(false);
            }
            //notifyItemChanged(index,holderViewModel);
        }

        @OnClick(R2.id.btn_decrement)
        void onClickDecrement() {
            mPresenter.removeTickets();
            if (holderViewModel.getSelectedQuantity() > 0) {
                //btnDecrement.setColorFilter(mContext.getResources().getColor(R.color.green_nob));
                btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_green));
                btnDecrement.setClickable(true);
            } else {
                //btnDecrement.setColorFilter(mContext.getResources().getColor(R.color.grey_button));
                btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_grey));
                btnDecrement.setClickable(false);
            }
            //notifyItemChanged(index,holderViewModel);
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

        public void setTickeyDescriptionColor(int color) {
            tickeyDescriptionText.setTextColor(color);
        }

        public void setTicketViewColor(int color) {
//            btnDecrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.minus_button_layerlist_grey));
//            btnDecrement.setClickable(false);
            thisView.setBackgroundColor(color);
        }

        public void toggleMaxTicketWarning(int visibility) {
            if (visibility != maksTicket.getVisibility()) {
                if (visibility == View.VISIBLE) {
                    maksTicket.setVisibility(visibility);
                    btnIncrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.add_button_layerlist_grey));
                    btnIncrement.setClickable(false);
                } else {
                    maksTicket.setVisibility(visibility);
                    btnIncrement.setImageDrawable(mContext.getResources().getDrawable(R.drawable.add_button_layerlist_green));
                    btnIncrement.setClickable(true);
                }
            }
        }
    }
}
