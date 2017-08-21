package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.FilterViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 8/21/17.
 */

public class InboxReputationFilterTitleAdapter
        extends RecyclerView.Adapter<InboxReputationFilterTitleAdapter.ViewHolder> {

    private final Context context;

    public interface FilterListener {
        void onTitleClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView filterTitle;
        ImageView redDot;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            filterTitle = (TextView) itemView.findViewById(R.id.title_filter);
            redDot = (ImageView) itemView.findViewById(R.id.red_circle);
            mainView = itemView.findViewById(R.id.main_view);

            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (FilterViewModel item : list) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            notifyItemChanged(item.getPosition());
                        }
                    }
                    list.get(getAdapterPosition()).setSelected(true);
                    notifyItemChanged(getAdapterPosition());
                    listener.onTitleClicked(getAdapterPosition());
                }
            });
        }
    }

    ArrayList<FilterViewModel> list;
    FilterListener listener;


    public static InboxReputationFilterTitleAdapter createInstance(Context context, FilterListener listener) {
        return new InboxReputationFilterTitleAdapter(context, listener);
    }

    public InboxReputationFilterTitleAdapter(Context context, FilterListener listener) {
        list = new ArrayList<>();
        this.listener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_filter_title, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list.get(position).isSelected())
            holder.mainView.setBackgroundColor(MethodChecker.getColor(context, R.color.white));
        else
            holder.mainView.setBackgroundColor(MethodChecker.getColor(context, R.color.transparent));

        if (list.get(position).isActive())
            holder.redDot.setVisibility(View.VISIBLE);
        else
            holder.redDot.setVisibility(View.INVISIBLE);

        holder.filterTitle.setText(list.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<FilterViewModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
