package com.tokopedia.transaction.bcaoneklik.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.activity.CreditCardDetailActivity;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.PaymentSettingModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickUserModel;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.PaymentListModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.CreditCardModelItem;
import com.tokopedia.transaction.bcaoneklik.presenter.ListPaymentTypePresenter;

import java.util.List;

/**
 * Created by kris on 10/4/17. Tokopedia
 * Modified by aghny on 8/1/18
 */

public class PaymentSettingMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HOLDER_CREDIT_CARD_HEADER =
            R.layout.credit_card_header_adapter;
    private static final int TYPE_HOLDER_CREDIT_CARD_ITEM =
            R.layout.credit_card_list_adapter;
    private static final int TYPE_HOLDER_CREDIT_CARD_FOOTER =
            R.layout.credit_card_footer_adapter;
    private static final int TYPE_HOLDER_BCA_ONE_CLICK_HEADER =
            R.layout.bca_one_click_header;
    private static final int TYPE_HOLDER_BCA_ONE_CLICK_ITEM =
            R.layout.bca_one_click_adapter;

    private static final int NUMBER_OF_HEADERS = 2;
    private static final int NUMBER_OF_FOOTER = 1;
    private static final int CREDIT_CARD_HEADER_SIZE = 1;
    private static final int BCA_ONE_CLICK_HEADER_SIZE = 1;

    private static final String VISA = "visa";
    private static final String MASTERCARD = "mastercard";
    private static final String JCB = "jcb";

    private static final String KEY_CC_ITEM = "credit_card_item";

    private ListPaymentTypePresenter presenter;

    private PaymentSettingModel model;

    private ListPaymentTypeView mainView;

    private Context context;

    public PaymentSettingMainAdapter(ListPaymentTypePresenter presenter,
                                     ListPaymentTypeView mainView) {
        this.presenter = presenter;
        this.mainView = mainView;
    }

    public void updateData(PaymentSettingModel model) {
        this.model = model;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);

        if (viewType == TYPE_HOLDER_CREDIT_CARD_HEADER)
            return new CreditCardAdderViewHolder(view);
        else if (viewType == TYPE_HOLDER_CREDIT_CARD_ITEM)
            return new CreditCardListViewHolder(view);
        else if (viewType == TYPE_HOLDER_CREDIT_CARD_FOOTER)
            return new CreditCardFooterViewHolder(view);
        else if (viewType == TYPE_HOLDER_BCA_ONE_CLICK_HEADER)
            return new BcaOneClickHeaderViewHolder(view);
        else if (viewType == TYPE_HOLDER_BCA_ONE_CLICK_ITEM)
            return new BcaOneClickListViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int type = getItemViewType(position);
        if (type == TYPE_HOLDER_CREDIT_CARD_HEADER) {
            ((CreditCardAdderViewHolder)holder)
                    .bindCreditCardHeader(model.getCreditCardResponse().getCreditCardList());
        } else if (type == TYPE_HOLDER_CREDIT_CARD_ITEM) {
            CreditCardModelItem creditCardItem = model
                    .getCreditCardResponse()
                    .getCreditCardList().get(position - CREDIT_CARD_HEADER_SIZE);
            ((CreditCardListViewHolder) holder).bindCreditCardItem(creditCardItem);
        } else if (type == TYPE_HOLDER_CREDIT_CARD_FOOTER) {
            ((CreditCardFooterViewHolder)holder).bindCreditCardHeader();
        } else if (type == TYPE_HOLDER_BCA_ONE_CLICK_HEADER) {
            ((BcaOneClickHeaderViewHolder) holder).bindBcaHeaderView(
                    model.getBcaOneClickModel()
            );
        } else {
            BcaOneClickUserModel bcaOneClickItem = model
                    .getBcaOneClickModel()
                    .getBcaOneClickUserModels()
                    .get(getBcaOneClickItemIndex(
                            adaptBcaOneClickPosition(position))
                    );
            ((BcaOneClickListViewHolder) holder).bindBcaOneClickItem(bcaOneClickItem);
        }
    }

    private int adaptBcaOneClickPosition(int position) {
        return position - NUMBER_OF_HEADERS - getCreditCardListSize();
    }

    private int getBcaOneClickItemIndex(int position) {
        return CREDIT_CARD_HEADER_SIZE
                + model.getCreditCardResponse().getCreditCardList().size()
                + BCA_ONE_CLICK_HEADER_SIZE
                + position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HOLDER_CREDIT_CARD_HEADER;
        else if (position > 0 && position <= creditCardLastIndex())
            return TYPE_HOLDER_CREDIT_CARD_ITEM;
        else if (position == creditCardLastIndex() + NUMBER_OF_FOOTER)
            return TYPE_HOLDER_CREDIT_CARD_FOOTER;
        else if (position == creditCardLastIndex() + NUMBER_OF_FOOTER + BCA_ONE_CLICK_HEADER_SIZE)
            return TYPE_HOLDER_BCA_ONE_CLICK_HEADER;
        else
            return TYPE_HOLDER_BCA_ONE_CLICK_ITEM;
    }

    private int creditCardLastIndex() {
        return model.getCreditCardResponse().getCreditCardList().size() - 1
                + CREDIT_CARD_HEADER_SIZE;
    }

    @Override
    public int getItemCount() {
        if (model == null)
            return 0;
        else if (bcaOneClickUnavailable())
            return CREDIT_CARD_HEADER_SIZE
                    + NUMBER_OF_FOOTER
                    + getCreditCardListSize();

        else
            return NUMBER_OF_HEADERS
                    + NUMBER_OF_FOOTER
                    + getBcaOneClickListSize()
                    + getCreditCardListSize();
    }

    private boolean bcaOneClickUnavailable() {
        return model.getBcaOneClickModel() == null
                || model.getBcaOneClickModel().getBcaOneClickUserModels() == null;
    }

    private int getCreditCardListSize() {
        return model.getCreditCardResponse().getCreditCardList().size();
    }

    private int getBcaOneClickListSize() {
        if (bcaOneClickUnavailable()) return 0;
        return model.getBcaOneClickModel().getBcaOneClickUserModels().size();
    }

    private class CreditCardAdderViewHolder extends RecyclerView.ViewHolder {

        private View addCreditCardLayout;
        private TextView numberOfCreditCards;

        CreditCardAdderViewHolder(View itemView) {
            super(itemView);

            addCreditCardLayout = itemView.findViewById(R.id.add_credit_card_layout);
            numberOfCreditCards = itemView.findViewById(R.id.credit_card_sub_title);
        }

        void bindCreditCardHeader(List<CreditCardModelItem> listOfCreditCards) {
            if (listOfCreditCards.isEmpty()) addCreditCardLayout.setVisibility(View.VISIBLE);
            else addCreditCardLayout.setVisibility(View.GONE);

            numberOfCreditCards.setText(getTotalSavedCcTitle(listOfCreditCards));
        }

        private String getTotalSavedCcTitle(List<CreditCardModelItem> listOfCreditCardModelItem) {
            return context.getString(R.string.title_saved_credit_card,
                    listOfCreditCardModelItem.size());
        }
    }

    private class CreditCardFooterViewHolder extends RecyclerView.ViewHolder {

        private View authButton;
        private View authFooter;

        CreditCardFooterViewHolder(View itemView) {
            super(itemView);

            authButton = itemView.findViewById(R.id.credit_card_auth);
            authFooter = itemView.findViewById(R.id.credit_card_auth_footer);
        }

        void bindCreditCardHeader() {
            authButton.setOnClickListener(onCreditCardMenuClickedListener());
            authFooter.setOnClickListener(onCreditCardMenuClickedListener());
        }

        private View.OnClickListener onCreditCardMenuClickedListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.checkCreditCardWhiteList();
                }
            };
        }
    }

    private class CreditCardListViewHolder extends RecyclerView.ViewHolder {

        private TextView cardNumber;
        private ImageView cardImage;
        private LinearLayout cardBackground;

        CreditCardListViewHolder(View itemView) {
            super(itemView);

            cardBackground = itemView.findViewById(R.id.ll_cc_container);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardImage = itemView.findViewById(R.id.card_image);
        }

        void bindCreditCardItem(final CreditCardModelItem item) {
            cardBackground.setBackgroundResource(getBackgroundResource(item));
            cardBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CreditCardDetailActivity.class);
                    intent.putExtra(KEY_CC_ITEM, item);
                    ((Activity) context).startActivityForResult(intent,
                            ListPaymentTypeView.CREDIT_CARD_DETAIL_REQUEST_CODE);
                }
            });

            cardNumber.setText(getMaskedNumberSubString(item.getMaskedNumber()));
            ImageHandler.LoadImage(cardImage, item.getCardTypeImage());
        }

        private int getBackgroundResource(CreditCardModelItem item) {
            switch (item.getCardType().toLowerCase()) {
                case VISA:
                    return R.drawable.bg_visa_small;
                case MASTERCARD:
                    return R.drawable.bg_mastercard_small;
                case JCB:
                    return R.drawable.bg_jcb_small;
                default:
                    return R.drawable.bg_expired_small;
            }
        }

        private String getMaskedNumberSubString(String maskedNumber) {
            final int LAST_NUMBERS = 4;
            final String FOUR_STARS = " * * * * ";

            return FOUR_STARS + maskedNumber.substring(maskedNumber.length() - LAST_NUMBERS);
        }
    }

    private class BcaOneClickHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView bcaOneClickRegistrationButton;
        private LinearLayout bcaOneClickRegisterLayout;

        BcaOneClickHeaderViewHolder(View itemView) {
            super(itemView);

            bcaOneClickRegisterLayout = itemView.findViewById(R.id.bca_one_click_register_layout);
            bcaOneClickRegistrationButton = itemView.findViewById(R.id.bca_one_click_register_button);
        }

        void bindBcaHeaderView(PaymentListModel bcaOneClickModel) {
            if (bcaOneClickModel == null || bcaOneClickModel.getBcaOneClickUserModels() == null) {
                bcaOneClickRegisterLayout.setVisibility(View.GONE);
            } else {
                bcaOneClickRegisterLayout.setVisibility(View.VISIBLE);

                if (bcaOneClickModel.getBcaOneClickUserModels().size() < 3)
                    bcaOneClickRegisterLayout.setVisibility(View.VISIBLE);
                else
                    bcaOneClickRegisterLayout.setVisibility(View.GONE);

                bcaOneClickRegistrationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onRequestBcaOneClickRegisterToken();
                    }
                });
            }
        }
    }

    private class BcaOneClickListViewHolder extends RecyclerView.ViewHolder {

        private TextView accountHolderName;
        private TextView dailyLimit;
        private TextView accountNumber;
        private TextView deleteButton;
        private TextView editButton;

        BcaOneClickListViewHolder(View itemView) {
            super(itemView);

            accountHolderName = itemView.findViewById(R.id.account_holder_name);
            dailyLimit = itemView.findViewById(R.id.daily_limit);
            accountNumber = itemView.findViewById(R.id.account_number);
            deleteButton = itemView.findViewById(R.id.delete_button);
            editButton = itemView.findViewById(R.id.edit_button);
        }

        void bindBcaOneClickItem(BcaOneClickUserModel item) {
            final String name = item.getTokenId();
            final String tokenId = item.getTokenId();
            final String credentialType = item.getCredentialType();
            final String credentialNumber = item.getCredentialNo();

            accountHolderName.setText(presenter.getUserLoginAccountName());
            dailyLimit.setText(item.getMaxLimit());
            accountNumber.setText(item.getCredentialNo());
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainView.onShowDeleteBcaOneClickDialog(
                            tokenId,
                            name,
                            credentialNumber
                    );
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onRequestBcaOneClickAccessToken(tokenId,
                            credentialType,
                            credentialNumber,
                            ListPaymentTypeView.EDIT_BCA_ONE_CLICK_REQUEST_CODE);
                }
            });
        }
    }

}
