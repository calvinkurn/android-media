package com.tokopedia.inbox.attachproduct.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Hendri on 15/02/18.
 */

public class AttachProductListAdapter extends BaseListAdapter<AttachProductItemViewModel,AttachProductListAdapterTypeFactory>
{
    private HashSet<Integer> productIds;
    private ArrayList<AttachProductItemViewModel> checkedList;

    public AttachProductListAdapter(AttachProductListAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
        productIds = new HashSet<>();
        checkedList = new ArrayList<>();
    }

    @Override
    public List<AttachProductItemViewModel> getData() {
        return super.getData();
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(holder instanceof BaseCheckableViewHolder){
            ((BaseCheckableViewHolder) holder).getCheckable()
                    .setChecked(productIds.contains(getDataRow(position).getProductId()));
            ((BaseCheckableViewHolder) holder).itemView.setSelected(productIds.contains(getDataRow(position).getProductId()));
        }
    }

    private AttachProductItemViewModel getDataRow(int position){
        AttachProductItemViewModel item = (AttachProductItemViewModel)visitables.get(position);
        return item;
    }

    public void itemChecked(boolean isChecked,int position){
        AttachProductItemViewModel product = getDataRow(position);
        int productId = product.getProductId();
        if(isChecked) {
            productIds.add(productId);
            addToCheckedDataList(product);
        }
        else {
            productIds.remove(productId);
            removeFromCheckedDataList(productId);
        }
    }

    private void addToCheckedDataList(AttachProductItemViewModel productItemViewModel){
        checkedList.add(productItemViewModel);
    }

    private void removeFromCheckedDataList(int productId){
        Iterator<AttachProductItemViewModel> iterator = checkedList.iterator();
        while(iterator.hasNext()){
            AttachProductItemViewModel itemViewModel = iterator.next();
            if(itemViewModel.getProductId() == productId){
                iterator.remove();
                return;
            }
        }
    }

    public int getCheckedCount(){
        return productIds.size();
    }

    public boolean isChecked(int position){
        return productIds.contains(getDataRow(position).getProductId());
    }


    public List<AttachProductItemViewModel> getCheckedDataList(){
        return checkedList;
    }
}
