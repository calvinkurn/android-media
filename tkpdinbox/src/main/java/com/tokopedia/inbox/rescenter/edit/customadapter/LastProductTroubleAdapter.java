package com.tokopedia.inbox.rescenter.edit.customadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core2.R2;
import com.tokopedia.inbox.rescenter.edit.model.passdata.EditResCenterFormData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 8/30/16.
 */
public class LastProductTroubleAdapter extends RecyclerView.Adapter<LastProductTroubleAdapter.ViewHolder> {

    private final List<EditResCenterFormData.LastProductTrouble> list;

    private Context context;

    public LastProductTroubleAdapter(List<EditResCenterFormData.LastProductTrouble> lastProductTrouble) {
        this.list = lastProductTrouble;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.title_product)
        public TextView productView;
        @BindView(R2.id.title_trouble)
        public TextView troubleView;
        @BindView(R2.id.title_remark)
        public TextView remarkView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_summary_product_edit_rescenter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productView.setText(getItem(position).getPtProductName());
        holder.troubleView.setText(
                context.getString(R.string.title_summary_trouble_product)
                        .replace("X123", String.valueOf(getItem(position).getPtQuantity()))
                        .replace("Y123", getItem(position).getPtTroubleName()));
        holder.remarkView.setText(getItem(position).getPtSolutionRemark());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public EditResCenterFormData.LastProductTrouble getItem(int position) {
        return list.get(position);
    }
}
