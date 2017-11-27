package com.tokopedia.inbox.rescenter.detailv2.view.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResCenterFragmentView;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonData;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.ButtonViewItem;

import java.util.List;

/**
 * Created by yfsx on 27/11/17.
 */

public class ButtonViewAdapter extends RecyclerView.Adapter<ButtonViewAdapter.Holder> {

    public static final String BUTTON_FINISH_COMPLAINT = "button_finish_complaint";
    public static final String BUTTON_ACCEPT_SOLUTION = "button_accept_solution";
    public static final String BUTTON_CHANGE_SOLUTION = "button_change_solution";
    public static final String BUTTON_APPEAL_SOLUTION = "button_appeal_solution";
    public static final String BUTTON_INPUT_ADDRESS = "button_input_address";
    public static final String BUTTON_INPUT_AWB = "button_input_awb";
    public static final String BUTTON_RECOMPLAINT = "button_recomplaint";
    public static final String BUTTON_REPORT = "button_report";
    public static final String BUTTON_CANCEL = "button_cancel";

    private Context context;
    private List<ButtonViewItem> buttonViewItemList;
    private DetailResCenterFragmentView listener;

    public ButtonViewAdapter(Context context, DetailResCenterFragmentView listener) {
        this.context = context;
        this.listener = listener;
    }

    public List<ButtonViewItem> getButtonViewItemList() {
        return buttonViewItemList;
    }

    public void setButtonViewItemList(List<ButtonViewItem> buttonViewItemList) {
        this.buttonViewItemList = buttonViewItemList;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_layout_button, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ButtonViewItem buttonViewItem = buttonViewItemList.get(position);
        if (position == 0) {
            setGreenButton(holder.btnAction, buttonViewItem.getLabel());
        } else  {
            setWhiteButton(holder.btnAction, buttonViewItem.getLabel());
        }
        holder.btnAction.setOnClickListener(onActionButtonListener(buttonViewItem.getType()));
    }

    private Button.OnClickListener onActionButtonListener(final String buttonType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonType.equals(BUTTON_FINISH_COMPLAINT)) listener.setOnActionFinishResolutionClick();
                else if (buttonType.equals(BUTTON_ACCEPT_SOLUTION)) listener.setOnActionAcceptSolutionClick();
                else if (buttonType.equals(BUTTON_CHANGE_SOLUTION)) listener.setOnActionEditSolutionClick();
                else if (buttonType.equals(BUTTON_APPEAL_SOLUTION)) listener.setOnActionAppealClick();
                else if (buttonType.equals(BUTTON_INPUT_ADDRESS)) listener.setOnActionInputAddressClick();
                else if (buttonType.equals(BUTTON_INPUT_AWB)) listener.setOnActionInputAwbNumberClick();
                else if (buttonType.equals(BUTTON_REPORT)) listener.setOnActionHelpClick();
                else if (buttonType.equals(BUTTON_CANCEL)) listener.setOnActionCancelResolutionClick();
            }
        };
    }

    private void setGreenButton(TextView button, String text) {
        button.setText(text);
        button.setBackgroundColor(MethodChecker.getColor(context, R.color.tkpd_main_green));
        button.setTextColor(MethodChecker.getColor(context, R.color.white));
    }

    private void setWhiteButton(TextView button, String text) {
        button.setText(text);
        button.setBackgroundColor(MethodChecker.getColor(context, R.color.white));
        button.setTextColor(MethodChecker.getColor(context, R.color.tkpd_main_green));

    }

    @Override
    public int getItemCount() {
        return buttonViewItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // pos 0 always green and bottom
        if (buttonViewItemList.size() == 1) return 1;
        else if (buttonViewItemList.size() == 2) return 2;
        else if (buttonViewItemList.size() == 3) {
            if (position == 0) return 1;
            else return 2;
        } else if (buttonViewItemList.size() == 4) {
            if (position == 0 || position == 3) return 1;
            else return 2;
        }
        return super.getItemViewType(position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView btnAction;
        public Holder(View itemView) {
            super(itemView);
            btnAction = (TextView) itemView.findViewById(R.id.btn_action);
        }
    }
}
