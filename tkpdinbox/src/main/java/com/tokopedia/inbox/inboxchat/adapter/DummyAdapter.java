package com.tokopedia.inbox.inboxchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.inbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 9/18/17.
 */

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.ViewHolder>{

    private List<Integer> list;

    public DummyAdapter(ArrayList<Integer> objects) {
        this.list = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dummy, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer c = list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(ArrayList<Integer> local) {
        this.list = local;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
