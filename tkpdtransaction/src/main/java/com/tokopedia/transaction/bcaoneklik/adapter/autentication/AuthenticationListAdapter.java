package com.tokopedia.transaction.bcaoneklik.adapter.autentication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorLogicModel;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;

import java.util.List;

/**
 * Created by kris on 10/10/17. Tokopedia
 */

public class AuthenticationListAdapter extends RecyclerView.Adapter<AuthenticationListAdapter.AuthenticationViewHolder>{

    private AuthenticatorPageModel model;

    private List<AuthenticatorLogicModel> logicList;

    public AuthenticationListAdapter(AuthenticatorPageModel model,
                                     List<AuthenticatorLogicModel> logicList) {
        this.model = model;
        this.logicList = logicList;
    }

    @Override
    public AuthenticationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.credit_card_authenticator_adapter, parent, false);
        return new AuthenticationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AuthenticationViewHolder holder, int position) {
        holder.authenticationTitle.setText(logicList.get(position).getAuhtenticationTitle());
        holder.authenticationDescription.setText(logicList.get(position)
                .getAuthenticationDescription());
        holder.selectedRadioButton.setOnClickListener(onRadioButtonClickedListener(holder));
        holder.rootLayout.setOnClickListener(onRadioButtonClickedListener(holder));
        holder.selectedRadioButton.setChecked(logicList.get(position).isSelected());
        CommonUtils.dumper("PORING state = " + String.valueOf(model.getState()));
    }

    @Override
    public int getItemCount() {
        return logicList.size();
    }

    class AuthenticationViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup rootLayout;

        private CheckBox selectedRadioButton;

        private TextView authenticationTitle;

        private TextView authenticationDescription;

        AuthenticationViewHolder(View itemView) {
            super(itemView);

            rootLayout = (ViewGroup) itemView.findViewById(R.id.authentication_adapter_root_view);

            selectedRadioButton = (CheckBox) itemView.findViewById(R.id.selection_radio_button);

            authenticationTitle = (TextView) itemView.findViewById(R.id.authentincation_title);

            authenticationDescription = (TextView) itemView.findViewById(R.id.authentincation_description);

        }
    }

    private View.OnClickListener onRadioButtonClickedListener(final AuthenticationViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setState(logicList.get(holder.getAdapterPosition()).getStateWhenSelected());
                for(int i = 0; i <logicList.size(); i++) {
                    if(i == holder.getAdapterPosition()) {
                        logicList.get(i).setSelected(true);
                    } else logicList.get(i).setSelected(false);
                }
                notifyDataSetChanged();
            }
        };
    }

}
