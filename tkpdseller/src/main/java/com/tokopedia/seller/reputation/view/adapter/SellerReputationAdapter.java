package com.tokopedia.seller.reputation.view.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.seller.R;
import com.tokopedia.seller.gmstat.utils.DateHeaderFormatter;
import com.tokopedia.seller.gmstat.utils.DateUtilHelper;
import com.tokopedia.seller.lib.datepicker.constant.DatePickerConstant;
import com.tokopedia.seller.reputation.view.helper.ReputationHeaderViewHelper;
import com.tokopedia.seller.reputation.view.model.EmptyListModel;
import com.tokopedia.seller.reputation.view.model.EmptySeparatorModel;
import com.tokopedia.seller.reputation.view.model.ReputationReviewModel;
import com.tokopedia.seller.reputation.view.model.SetDateHeaderModel;
import com.tokopedia.seller.topads.view.model.TypeBasedModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vishal.gupta on 20/02/2017.
 *         modify by normansyahputra 16-03-2017
 */
public class SellerReputationAdapter extends BaseLinearRecyclerViewAdapter {
    public static final int SET_DATE_MODEL_POSITION = 0;
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
                        .inflate(R.layout.listview_seller_reputation, viewGroup, false);
                return new ViewHolder(itemLayoutView);
            case SetDateHeaderModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.widget_header_reputation, viewGroup, false);
                SellerDateHeaderViewHolder viewHolder2 = new SellerDateHeaderViewHolder(itemLayoutView);
                viewHolder2.setFragment(fragment);
                return viewHolder2;
            case EmptySeparatorModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.empty_separator_model, viewGroup, false);
                return new EmptySeparatorViewHolder(itemLayoutView);
            case EmptyListModel.TYPE:
                itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.deisgn_retry_reputation, viewGroup, false);
                EmptyListViewHolder emptyListViewHolder = new EmptyListViewHolder(itemLayoutView);
                emptyListViewHolder.setFragment(fragment);
                return emptyListViewHolder;
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
                ((SellerDateHeaderViewHolder) holder).bindData((SetDateHeaderModel) list.get(position));
                break;
            case EmptySeparatorModel.TYPE:
                break;
            case EmptyListModel.TYPE:
                ((EmptyListViewHolder) holder).bindData((EmptyListModel) list.get(position));
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
                String information = data.getInformation();
                int i = information.indexOf("I");

                holder.description.setText(information.substring(0, i));
                holder.tvInvoice.setText(information.substring(i));
                holder.penaltyScore.setText(decimalFormat.format(data.getPenaltyScore()));
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

    public void insertHeader(SetDateHeaderModel headerModel) {
        list.add(0, headerModel);
        notifyItemInserted(0);
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
            TypeBasedModel typeBasedModel = list.get(SET_DATE_MODEL_POSITION);
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

        TextView tvInvoice;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.tv_date);
            description = (TextView) itemView.findViewById(R.id.tv_note);
            penaltyScore = (TextView) itemView.findViewById(R.id.tv_score);
            tvInvoice = (TextView) itemView.findViewById(R.id.tv_invoice);

        }
    }

    public class SellerDateHeaderViewHolder extends RecyclerView.ViewHolder {

        private final ReputationHeaderViewHelper reputationViewHelper;
        private SetDateHeaderModel setDateHeaderModel;
        private Fragment fragment;
        private DateHeaderFormatter dateHeaderFormatter;

        public SellerDateHeaderViewHolder(View itemView) {
            super(itemView);
            dateHeaderFormatter = new DateHeaderFormatter(
                    itemView.getResources().getStringArray(R.array.month_names_abrev)
            );
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
                reputationViewHelper.setSelectionType(DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE);
                reputationViewHelper.onClick(fragment, true);
                return;
            }
        }

        public void bindData(SetDateHeaderModel setDateHeaderModel) {
            this.setDateHeaderModel = setDateHeaderModel;

            reputationViewHelper.bindDate(
                    dateHeaderFormatter,
                    setDateHeaderModel.getsDate(),
                    setDateHeaderModel.geteDate(),
                    DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE,
                    DatePickerConstant.SELECTION_TYPE_PERIOD_DATE
            );
        }
    }

    public class EmptySeparatorViewHolder extends RecyclerView.ViewHolder {

        public EmptySeparatorViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class EmptyListViewHolder extends RecyclerView.ViewHolder {

        DateUtilHelper dateUtilHelper;
        Fragment fragment;
        private LinearLayout containerClick;

        public EmptyListViewHolder(View itemView) {
            super(itemView);

            dateUtilHelper = new DateUtilHelper(itemView.getContext());
            containerClick = (LinearLayout) itemView.findViewById(R.id.reputation_container_change_date);
            containerClick.setVisibility(View.INVISIBLE);
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public void bindData(EmptyListModel emptyListModel) {
            if (containerClick.getVisibility() != View.VISIBLE)
                return;

            dateUtilHelper.setsDate(emptyListModel.getSetDateHeaderModel().getsDate());
            dateUtilHelper.seteDate(emptyListModel.getSetDateHeaderModel().geteDate());

            containerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFragment() != null) {
                        dateUtilHelper.setSelectionType(DatePickerConstant.SELECTION_TYPE_CUSTOM_DATE);
                        dateUtilHelper.onClick(fragment, true);
                    }
                }
            });
        }
    }

}
