package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.fragment.DetailResChatFragment;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDetailStepDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yoasfs on 16/10/17.
 */

public class NextActionAdapter extends RecyclerView.Adapter<NextActionAdapter.ItemHolder> {

    public static final int STEP_BEFORE_CURRENT = 0;
    public static final int STEP_CURRENT = 1;
    public static final int STEP_AFTER_CURRENT = 2;

    private Context context;
    private List<NextActionDetailStepDomain> stepList = new ArrayList<>();
    private HashMap<NextActionDetailStepDomain, Integer> stepMap = new HashMap<>();
    private int resolutionStatus;

    public NextActionAdapter(Context context) {
        this.context = context;
    }

    public void populateAdapter(List<NextActionDetailStepDomain> stepList, int resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
        this.stepList = stepList;
        int itemPos = STEP_BEFORE_CURRENT;
        for (NextActionDetailStepDomain step : stepList) {
            if (itemPos == STEP_BEFORE_CURRENT) {
                if (step.getStatus() == 1) {
                    itemPos = STEP_CURRENT;
                }
            } else if (itemPos == STEP_CURRENT) {
                if (step.getStatus() == 0) {
                    itemPos = STEP_AFTER_CURRENT;
                }
            }
            stepMap.put(step, itemPos);
        }
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_next_action, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        NextActionDetailStepDomain currentStep = stepList.get(position);
        int currentStepStatus = stepMap.get(currentStep);
        holder.tvAction.setText(currentStep.getName());
        setView(holder, currentStepStatus, position < stepList.size() - 1 ? stepList.get(position + 1) : null);
    }


    private void setView(ItemHolder holder, int currentStepStatus, NextActionDetailStepDomain nextStep) {
        if (resolutionStatus == DetailResChatFragment.STATUS_CANCEL || resolutionStatus == DetailResChatFragment.STATUS_FINISHED) {
            setTextStyle(holder.tvAction, false);
            holder.ivIndicatorArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_arrow_past));
            holder.ivIndicatorCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_past));
            holder.ivIndicatorArrow.setVisibility(nextStep == null ? View.GONE : View.VISIBLE);
        } else {
            if (currentStepStatus == STEP_BEFORE_CURRENT) {
                setTextStyle(holder.tvAction, false);
                holder.ivIndicatorArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_arrow_past));
                holder.ivIndicatorCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_past));
            } else if (currentStepStatus == STEP_CURRENT) {
                setTextStyle(holder.tvAction, true);
                holder.ivIndicatorArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_arrow_past));
                holder.ivIndicatorCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_current));
                holder.ivIndicatorArrow.setImageDrawable(nextStep != null && stepMap.get(nextStep) == STEP_AFTER_CURRENT ?
                        context.getResources().getDrawable(R.drawable.ic_arrow_dotted) :
                        context.getResources().getDrawable(R.drawable.ic_step_arrow_past));
            } else if (currentStepStatus == STEP_AFTER_CURRENT) {
                setTextStyle(holder.tvAction, false);
                holder.ivIndicatorArrow.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_arrow_dotted));
                holder.ivIndicatorCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_step_next));
                holder.ivIndicatorArrow.setVisibility(nextStep == null ? View.GONE : View.VISIBLE);
            }
        }
    }

    private void setTextStyle(TextView textView, boolean isCurrentTypeface) {
        if (isCurrentTypeface) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.fontxl));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(context.getResources().getColor(R.color.black_70b));
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.fonts));
            textView.setTypeface(null, Typeface.NORMAL);
            textView.setTextColor(context.getResources().getColor(R.color.black_54));
        }
    }

    @Override
    public int getItemCount() {
        return stepMap.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView ivIndicatorArrow, ivIndicatorCircle;
        TextView tvAction;

        public ItemHolder(View itemView) {
            super(itemView);
            ivIndicatorArrow = (ImageView) itemView.findViewById(R.id.iv_indicator_arrow);
            ivIndicatorCircle = (ImageView) itemView.findViewById(R.id.iv_indicator_circle);
            tvAction = (TextView) itemView.findViewById(R.id.tv_action);
        }
    }
}
