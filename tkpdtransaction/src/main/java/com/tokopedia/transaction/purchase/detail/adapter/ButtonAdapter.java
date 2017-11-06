package com.tokopedia.transaction.purchase.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.ButtonAttribute;

import java.util.List;

/**
 * Created by kris on 11/3/17. Tokopedia
 */

public class ButtonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_HOLDER_WHITE_BUTTON = R.layout.order_detail_buyer_button;
    private static final int TYPE_HOLDER_GREEN_BUTTON = R.layout.order_detail_buyer_button_green;

    private List<ButtonAttribute> buttonAttributes;

    public ButtonAdapter(List<ButtonAttribute> buttonAttributes) {
        this.buttonAttributes = buttonAttributes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if(viewType == TYPE_HOLDER_GREEN_BUTTON) {
            return new GreenButtonAdapterViewHolder(view);
        } else return new WhiteButtonAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if(type == TYPE_HOLDER_GREEN_BUTTON)
            ((GreenButtonAdapterViewHolder)holder).bindGreenButtonViewHolder(
                    buttonAttributes.get(position).getButtonText()
            );
        else ((WhiteButtonAdapterViewHolder)holder).bindWhiteButtonViewHolder(
                buttonAttributes.get(position).getButtonText()
        );
    }

    @Override
    public int getItemViewType(int position) {
        if(buttonAttributes.get(position).getButtonColorMode()
                == ButtonAttribute.GREEN_COLOR_MODE) {
            return TYPE_HOLDER_GREEN_BUTTON;
        } else return TYPE_HOLDER_WHITE_BUTTON;
    }

    @Override
    public int getItemCount() {
        return buttonAttributes.size();
    }

    private class WhiteButtonAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView buttonText;

        WhiteButtonAdapterViewHolder(View itemView) {
            super(itemView);
            buttonText = (TextView) itemView.findViewById(R.id.white_button_text);
        }

        private void bindWhiteButtonViewHolder(String text) {
            buttonText.setText(text);
        }
    }

    private class GreenButtonAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView greenButtonText;

        GreenButtonAdapterViewHolder(View itemView) {
            super(itemView);
            greenButtonText = (TextView) itemView.findViewById(R.id.green_button_text);
        }

        private void bindGreenButtonViewHolder(String text) {
            greenButtonText.setText(text);
        }
    }

}
