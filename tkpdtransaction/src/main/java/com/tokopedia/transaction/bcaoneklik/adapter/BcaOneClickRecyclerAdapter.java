package com.tokopedia.transaction.bcaoneklik.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.model.bcaoneclick.BcaOneClickUserModel;

import java.util.List;

/**
 * Created by kris on 8/2/17. Tokopedia
 */

public class BcaOneClickRecyclerAdapter extends RecyclerView.Adapter<BcaOneClickRecyclerAdapter
        .BankListViewHolder> {

    private List<BcaOneClickUserModel> bcaOneClickUserModels;
    private ActionListener listener;

    public BcaOneClickRecyclerAdapter(List<BcaOneClickUserModel> bcaOneClickUserModels,
                                      ActionListener listener) {
        this.bcaOneClickUserModels = bcaOneClickUserModels;
        this.listener = listener;
    }

    @Override
    public BankListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bca_one_click_adapter, parent, false);
        return new BankListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BankListViewHolder holder, int position) {
        final String name = bcaOneClickUserModels.get(position).getTokenId();
        final String tokenId = bcaOneClickUserModels.get(position).getTokenId();
        final String credentialType = bcaOneClickUserModels.get(position).getCredentialType();
        final String credentialNumber = bcaOneClickUserModels.get(position).getCredentialNo();
        holder.accountHolderName.setText(listener.getUserLoginAccountName());
        holder.dailyLimit.setText(bcaOneClickUserModels.get(position).getMaxLimit());
        holder.accountNumber.setText(bcaOneClickUserModels.get(position).getCredentialNo());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelete(tokenId, name, credentialNumber);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(tokenId, credentialType, credentialNumber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bcaOneClickUserModels.size();
    }

    class BankListViewHolder extends RecyclerView.ViewHolder {

        private TextView accountHolderName;

        private TextView dailyLimit;

        private TextView accountNumber;

        private TextView deleteButton;

        private TextView editButton;


        BankListViewHolder(View itemView) {
            super(itemView);
            accountHolderName = (TextView) itemView.findViewById(R.id.account_holder_name);
            dailyLimit = (TextView) itemView.findViewById(R.id.daily_limit);
            accountNumber = (TextView) itemView.findViewById(R.id.account_number);
            deleteButton = (TextView) itemView.findViewById(R.id.delete_button);
            editButton = (TextView) itemView.findViewById(R.id.edit_button);
        }
    }

    public interface ActionListener {
        void onDelete(String tokenId, String name, String credentialNumber);

        void onEdit(String token, String credentialType, String credentialNumber);

        String getUserLoginAccountName();
    }
}
