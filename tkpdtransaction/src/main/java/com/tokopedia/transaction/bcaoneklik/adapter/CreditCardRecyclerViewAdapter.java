package com.tokopedia.transaction.bcaoneklik.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;

import java.util.List;

/**
 * Created by kris on 8/23/17. Tokopedia
 */

public class CreditCardRecyclerViewAdapter extends RecyclerView.Adapter<
        CreditCardRecyclerViewAdapter.CreditCardAdapterViewHolder
        >{

    private List<CreditCardModelItem> listCreditCard;
    private CreditCardItemListener listener;

    public CreditCardRecyclerViewAdapter(List<CreditCardModelItem> creditCardModelItems,
                                         CreditCardItemListener listener) {
        listCreditCard = creditCardModelItems;
        this.listener = listener;
    }

    @Override
    public CreditCardAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_card_list_adapter, parent, false);
        return new CreditCardAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CreditCardAdapterViewHolder holder, int position) {
        holder.cardNumber.setText(listCreditCard.get(position).getMaskedNumber());
        ImageHandler.LoadImage(holder.cardImage, listCreditCard.get(position).getCardTypeImage());
    }

    @Override
    public int getItemCount() {
        return listCreditCard.size();
    }

    class CreditCardAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView cardNumber;

        private ImageView cardImage;

        CreditCardAdapterViewHolder(View itemView) {
            super(itemView);
            cardNumber = (TextView) itemView.findViewById(R.id.card_number);
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
        }
    }

    private View.OnClickListener onDeleteClickedListener(final String tokenId, final String cardId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteButtonClicked(tokenId, cardId);
            }
        };
    }

    public interface CreditCardItemListener {
        void onDeleteButtonClicked(String tokenId, String cardId);
    }
}
