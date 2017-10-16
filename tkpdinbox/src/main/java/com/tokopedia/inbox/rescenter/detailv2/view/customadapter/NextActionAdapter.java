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

    public NextActionAdapter(Context context) {
        this.context = context;
    }

    public void populateAdapter(List<NextActionDetailStepDomain> stepList) {
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
        setView(holder, currentStepStatus);
        if (position == 0) {
            holder.ivIndicatorArrow.setVisibility(View.GONE);
        } else {
            holder.ivIndicatorArrow.setVisibility(View.VISIBLE);
        }
    }


    private void setView(ItemHolder holder, int currentStepStatus) {
        if (currentStepStatus == STEP_BEFORE_CURRENT) {
            setTextStyle(holder.tvAction, false);
            holder.ivIndicatorArrow.setBackgroundColor(context.getResources().getColor(R.color.red_200));
            holder.ivIndicatorCircle.setBackgroundColor(context.getResources().getColor(R.color.black));
        } else if (currentStepStatus == STEP_CURRENT) {
            setTextStyle(holder.tvAction, true);
            holder.ivIndicatorArrow.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.ivIndicatorCircle.setBackgroundColor(context.getResources().getColor(R.color.green_200));
        } else if (currentStepStatus == STEP_AFTER_CURRENT) {
            setTextStyle(holder.tvAction, false);
            holder.ivIndicatorArrow.setBackgroundColor(context.getResources().getColor(R.color.blue));
            holder.ivIndicatorCircle.setBackgroundColor(context.getResources().getColor(R.color.grey));
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
