package com.tokopedia.tkpdstream.chatroom.view.adapter.tab;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tkpdstream.R;
import com.tokopedia.tkpdstream.chatroom.view.viewmodel.tab.TabViewModel;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author by nisie on 3/21/18.
 */

public class GroupChatTabAdapter extends RecyclerView.Adapter<GroupChatTabAdapter.ViewHolder> {

    public interface TabListener {
        void onTabClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        View highlight;
        View mainLayout;
        View dot;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            highlight = itemView.findViewById(R.id.highlight);
            mainLayout = itemView.findViewById(R.id.main_layout);
            dot = itemView.findViewById(R.id.dot);

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() < listTab.size()) {
                        tabListener.onTabClicked(getAdapterPosition());
                    }
                }
            });
        }
    }


    public static GroupChatTabAdapter createInstance(TabListener tabListener, List<TabViewModel>
            listTab) {
        return new GroupChatTabAdapter(tabListener, listTab);
    }

    public GroupChatTabAdapter(TabListener tabListener, List<TabViewModel> listTab) {
        this.tabListener = tabListener;
        this.listTab = listTab;
    }

    private List<TabViewModel> listTab;
    private final TabListener tabListener;
    private int currentActiveTab;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groupchat_tab_indicator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(listTab.get(position).getTitle());
        if (listTab.get(position).isActive()) {
            holder.title.setTextColor(MethodChecker.getColor(holder.title.getContext(), R.color.tkpd_main_green));
            holder.highlight.setVisibility(View.VISIBLE);
        } else {
            holder.title.setTextColor(MethodChecker.getColor(holder.title.getContext(), R.color.black_38));
            holder.highlight.setVisibility(View.INVISIBLE);
        }

        if (listTab.get(position).isUpdated()) {
            holder.dot.setVisibility(View.VISIBLE);
        } else {
            holder.dot.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listTab.size();
    }

    public void setActiveFragment(int position) {
        if (position < listTab.size()) {
            listTab.get(currentActiveTab).setActive(false);
            listTab.get(position).setActive(true);
            currentActiveTab = position;
            notifyDataSetChanged();
        }
    }


    public void change(int position, boolean update) {
        listTab.get(position).setUpdated(update);
        notifyItemChanged(position);
    }

    public void add(int position, TabViewModel tabViewModel) {
        this.listTab.add(position, tabViewModel);
    }

    public void remove(int position) {
        this.listTab.remove(position);
    }

    public TabViewModel getItemAt(int position){
        return listTab.get(position);
    }

}
