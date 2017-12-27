package com.tokopedia.seller.opportunity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.opportunity.adapter.viewmodel.SimpleCheckListItemModel;

import java.util.ArrayList;

/**
 * Created by nisie on 4/20/17.
 */

public class OpportunitySortAdapter extends RecyclerView.Adapter<OpportunitySortAdapter.ViewHolder> {

    ArrayList<SimpleCheckListItemModel> list;

    public interface SimpleCheckListListener {
        void onItemSelected(int adapterPosition, SimpleCheckListItemModel simpleCheckListItemModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView checkImage;
        TextView title;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.checkImage = (ImageView) itemView.findViewById(R.id.check);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.mainView = itemView.findViewById(R.id.main);

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        list.get(getAdapterPosition()).setSelected(
                                checkImage.getVisibility() == View.VISIBLE);
                        notifyItemChanged(getAdapterPosition());
                        listener.onItemSelected(getAdapterPosition(),
                                list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    SimpleCheckListListener listener;

    public static OpportunitySortAdapter createInstance(ArrayList<SimpleCheckListItemModel> list,
                                                        SimpleCheckListListener listener) {
        return new OpportunitySortAdapter(list, listener);
    }

    public OpportunitySortAdapter(ArrayList<SimpleCheckListItemModel> list, SimpleCheckListListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_simple_check_item, parent, false));
    }

    @Override

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.checkImage.setVisibility(list.get(position).isSelected() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}