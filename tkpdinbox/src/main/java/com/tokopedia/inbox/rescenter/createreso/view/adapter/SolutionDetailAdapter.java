package com.tokopedia.inbox.rescenter.createreso.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.rescenter.createreso.view.typefactory.SolutionRefundTypeFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @author by yfsx on 07/08/18.
 */
public class SolutionDetailAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private List<Visitable> itemList = new ArrayList<>();
    private SolutionRefundTypeFactory typeFactory;

    public SolutionDetailAdapter(SolutionRefundTypeFactory typeFactory,
                                 List<Visitable> itemList) {
        this.itemList = itemList;
        this.typeFactory = typeFactory;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return typeFactory.createViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).type(typeFactory);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
