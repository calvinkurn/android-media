package com.tokopedia.seller.topads.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.model.data.DataCredit;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nathaniel on 12/2/2016.
 */

public class TopAdsCreditAdapter extends RecyclerView.Adapter<TopAdsCreditAdapter.ViewHolder> {

    private List<DataCredit> creditList;
    private int checkedPosition;

    public void setCreditList(List<DataCredit> creditList) {
        this.creditList = creditList;
    }

    public TopAdsCreditAdapter() {
        creditList = new ArrayList<>();
        checkedPosition = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listview_top_ads_credit, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.radioButton.setChecked(position == checkedPosition);
        holder.contentTextView.setText(creditList.get(position).getProductPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R2.id.radio_button)
        public RadioButton radioButton;
        @Bind(R2.id.text_view_content)
        public TextView contentTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}