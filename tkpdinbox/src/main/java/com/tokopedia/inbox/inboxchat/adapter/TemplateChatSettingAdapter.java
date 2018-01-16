package com.tokopedia.inbox.inboxchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.inboxchat.fragment.TemplateChatFragment;
import com.tokopedia.inbox.inboxchat.helper.ItemTouchHelperAdapter;
import com.tokopedia.inbox.inboxchat.helper.OnStartDragListener;
import com.tokopedia.inbox.inboxchat.presenter.TemplateChatContract;
import com.tokopedia.inbox.inboxchat.viewholder.ItemTemplateChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stevenfredian on 11/29/17.
 */

public class TemplateChatSettingAdapter extends RecyclerView.Adapter<AbstractViewHolder>
        implements ItemTouchHelperAdapter {

    private final TemplateChatSettingTypeFactory typeFactory;
    private final TemplateChatContract.View view;
    private List<Visitable> list;
    private ArrayList<String> listString;

    public TemplateChatSettingAdapter(TemplateChatSettingTypeFactory typeFactory, TemplateChatContract.View view) {
        this.view = view;
        this.typeFactory = typeFactory;
        this.list = new ArrayList<>();
        this.listString = new ArrayList<>();
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(list.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type(typeFactory);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Visitable> getList() {
        return list;
    }

    public ArrayList<String> getListString() {
        this.listString.clear();
        for (int i = 0; i < list.size() - 1; i++) {
            String temp = ((TemplateChatModel) list.get(i)).getMessage();
            listString.add(temp);
        }
        return listString;
    }

    public void setList(List<Visitable> list) {
        this.list.clear();
        if(list != null){
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public void onReallyMoved(int from, int to) {
        view.reArrange(from, to);
    }

    public void startDrag(ItemTemplateChatViewHolder ini) {

    }

    public void edit(int index, String string) {
        if(list!=null && list.size()>0) {
            ((TemplateChatModel) list.get(index)).setMessage(string);
            notifyItemChanged(index);
        }
    }

    public void delete(int index) {
        if(list!=null && list.size()>0) {
            list.remove(index);
            notifyItemRemoved(index);
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size() - 1);
            list.add(new TemplateChatModel(false, list.size()));
            notifyItemInserted(list.size());
        }
    }

    public void add(String string) {
        if(list!=null && list.size()>0) {
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size() - 1);
            list.add(new TemplateChatModel(string));
            list.add(new TemplateChatModel(false, list.size()));
            notifyItemRangeInserted(list.size(), 2);
        }
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void revertArrange(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
