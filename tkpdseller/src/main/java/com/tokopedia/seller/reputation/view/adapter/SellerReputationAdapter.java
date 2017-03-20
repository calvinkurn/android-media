package com.tokopedia.seller.reputation.view.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.reputation.view.helper.ReputationHeaderViewHelper;
import com.tokopedia.seller.reputation.view.model.ReputationReviewModel;
import com.tokopedia.seller.reputation.view.model.SetDateHeaderModel;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author vishal.gupta on 20/02/2017.
 *         modify by normansyahputra 16-03-2017
 */
public class SellerReputationAdapter extends BaseLinearRecyclerViewAdapter {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final String TAG = "SellerReputationAdapter";
    private final Context context;
    private ArrayList<TypeBasedModel> list;
    private Fragment fragment;

    public SellerReputationAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    public static SellerReputationAdapter createInstance(Context context) {
        return new SellerReputationAdapter(context);
    }

    public void setFragment(@Nullable Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ReputationReviewModel.VIEW_DEPOSIT:
                View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.listview_seller_reputation, null);
                return new ViewHolder(itemLayoutView);
            case SetDateHeaderModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.widget_header_gmstat, null);
                ViewHolder2 viewHolder2 = new ViewHolder2(itemLayoutView);
                viewHolder2.setFragment(fragment);
                return viewHolder2;
            default:
                return super.onCreateViewHolder(viewGroup, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ReputationReviewModel.VIEW_DEPOSIT:
                bindDeposit((ViewHolder) holder, position);
                break;
            case SetDateHeaderModel.TYPE:
                ((ViewHolder2) holder).bindData((SetDateHeaderModel) list.get(position));
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void bindDeposit(ViewHolder holder, int position) {

        if (position >= 0 && position < getDataSize()) {
            if (list.get(position) != null && list.get(position) instanceof ReputationReviewModel) {
                ReputationReviewModel reputationReviewModel =
                        (ReputationReviewModel) list.get(position);
                Log.d(TAG, String.format("bindDeposit %d %s", position, reputationReviewModel.getData().toString()));
                ReputationReviewModel.Data data = reputationReviewModel.getData();
                holder.date.setText(data.getDate());
                holder.description.setText(data.getInformation());
                holder.penaltyScore.setText(decimalFormat.format(data.getPenaltyScore()));

                if (data.getPenaltyScore() > 0) {
                    holder.penaltyScore.setTextColor(context.getResources().getColor(R.color.tkpd_light_green));
                } else {
                    holder.penaltyScore.setTextColor(context.getResources().getColor(R.color.tkpd_prod_price));
                }
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isLastItemPosition(position) && (list.isEmpty() || isLoading() || isRetry())) {
            return super.getItemViewType(position);
        } else {
            return list.get(position).getType();
        }

    }

    private boolean isLastItemPosition(int position) {
        return position == list.size();
    }

    private View.OnClickListener onNotesClicked(final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Salin", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String textHolderNote = holder.description.getText().toString();
                        ClipboardManager clipBoard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text", textHolderNote);
                        clipBoard.setPrimaryClip(clip);
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        };
    }

    @Override
    public int getItemCount() {
        return list.size() + super.getItemCount();
    }

    public int getDataSize() {
        return (list != null) ? list.size() : 0;
    }

    public void clear() {
        showEmptyFull(false);
        showEmpty(false);
        showLoadingFull(false);
        showLoading(false);
        showRetry(false);
        showRetryFull(false);
        list.clear();
    }

    public void addAllWithoutNotify(List<TypeBasedModel> datas) {
        list.addAll(datas);
    }

    public void notifyHeaderChange(SetDateHeaderModel headerModel) {
        list.set(0, headerModel);
        notifyItemChanged(0);
    }

    public SetDateHeaderModel getHeaderModel() {
        if (getDataSize() > 0) {
            TypeBasedModel typeBasedModel = list.get(0);
            if (typeBasedModel != null && typeBasedModel instanceof SetDateHeaderModel) {
                SetDateHeaderModel setDateHeaderModel = (SetDateHeaderModel) typeBasedModel;
                return setDateHeaderModel;
            }
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;

        TextView description;

        TextView penaltyScore;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tv_date);
            description = (TextView) itemView.findViewById(R.id.tv_note);
            penaltyScore = (TextView) itemView.findViewById(R.id.tv_score);

            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private final ReputationHeaderViewHelper reputationViewHelper;
        private SetDateHeaderModel setDateHeaderModel;
        private Fragment fragment;

        public ViewHolder2(View itemView) {
            super(itemView);
            reputationViewHelper = new ReputationHeaderViewHelper(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickHeader();
                }
            });
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public void onClickHeader() {
            if (fragment != null) {
                reputationViewHelper.onClick(fragment);
                return;
            }

            if (reputationViewHelper != null) {
                if (itemView.getContext() != null
                        && itemView.getContext() instanceof Activity) {
                    reputationViewHelper.onClick((Activity) itemView.getContext());
                }
            }
        }

        public void bindData(SetDateHeaderModel setDateHeaderModel) {
            this.setDateHeaderModel = setDateHeaderModel;

            reputationViewHelper.bindDate(
                    setDateHeaderModel.getsDate(),
                    setDateHeaderModel.geteDate(),
                    0,
                    DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
            );
        }
    }

}
