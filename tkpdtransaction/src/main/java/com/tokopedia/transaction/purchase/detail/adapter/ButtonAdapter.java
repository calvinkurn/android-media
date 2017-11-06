package com.tokopedia.transaction.purchase.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.purchase.detail.model.ButtonAttribute;

import java.util.List;

import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.ASK_BUYER_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.ASK_SELLER_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.CANCEL_SEARCH_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.COMPLAINT_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.FINISH_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.RECEIVED_PACKAGE_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.REQUEST_CANCEL_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.TRACK_ID;
import static com.tokopedia.transaction.purchase.detail.model.ButtonAttribute.VIEW_COMPLAINT_ID;

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
            return new GreenButtonAdapterViewHolder(view, parent.getContext());
        } else return new WhiteButtonAdapterViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if(type == TYPE_HOLDER_GREEN_BUTTON)
            ((GreenButtonAdapterViewHolder)holder).bindGreenButtonViewHolder(
                    buttonAttributes.get(position).getButtonText(),
                    buttonAttributes.get(position).getId()
            );
        else ((WhiteButtonAdapterViewHolder)holder).bindWhiteButtonViewHolder(
                buttonAttributes.get(position).getButtonText(),
                buttonAttributes.get(position).getId()
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

        private Context context;

        private TextView whiteButton;

        WhiteButtonAdapterViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            whiteButton = (TextView) itemView.findViewById(R.id.white_button_text);
        }

        private void bindWhiteButtonViewHolder(String text, int id) {
            whiteButton.setText(text);
            whiteButton.setOnClickListener(renderOnWhiteButtonClickListener(id));
        }

        private View.OnClickListener renderOnWhiteButtonClickListener(int buttonId) {
            switch (buttonId) {
                case TRACK_ID:
                    return onTrackButtonClicked(context);
                case ASK_SELLER_ID:
                    return onAskSeller(context);
                case ASK_BUYER_ID:
                    return onAskBuyer(context);
                case REQUEST_CANCEL_ID:
                    return onRequestCancellation(context);
                case COMPLAINT_ID:
                    return onComplaint(context);
                case CANCEL_SEARCH_ID:
                    return onCancelSearch(context);
            }
            return onButtonError(context);
        }
    }

    private class GreenButtonAdapterViewHolder extends RecyclerView.ViewHolder {

        private Context context;

        private TextView greenButton;

        GreenButtonAdapterViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            greenButton = (TextView) itemView.findViewById(R.id.green_button_text);
        }

        private void bindGreenButtonViewHolder(String text, int id) {
            greenButton.setText(text);
            greenButton.setOnClickListener(renderOnGreenButtonClickListener(id));
        }

        private View.OnClickListener renderOnGreenButtonClickListener(int buttonId) {
            switch (buttonId) {
                case FINISH_ID:
                    return onFinishButtonClicked(context);
                case VIEW_COMPLAINT_ID:
                    return onViewComplaintClicked(context);
                case RECEIVED_PACKAGE_ID:
                    return onReceivePackage(context);
                case TRACK_ID:
                    return onTrackButtonClicked(context);
                case ASK_SELLER_ID:
                    return onAskSeller(context);
                case ASK_BUYER_ID:
                    return onAskBuyer(context);
            }
            return onButtonError(context);
        }
    }

    private View.OnClickListener onFinishButtonClicked(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Finish Button", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onViewComplaintClicked(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Complaint Button Clicked", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onReceivePackage(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Receive Package", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onTrackButtonClicked(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Receive Package", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onAskSeller(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onAskBuyer(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Ask Seller", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onRequestCancellation(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Request Cancel", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onComplaint(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Complaint", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onCancelSearch(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cancel Search", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private View.OnClickListener onButtonError(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
            }
        };
    }

}
