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

//    private void sortAllVisitableBasedOnChecked(){
//
////        Collections.sort(visitables, new Comparator<Visitable>() {
////            @Override
////            public int compare(Visitable visitable, Visitable t1) {
////                boolean isFirstChecked = isChecked(visitable);
////                boolean isSecondChecked = isChecked(t1);
////                if(isFirstChecked && !isSecondChecked) {
////                    return -1;
////                }
////                else if(!isFirstChecked && isSecondChecked) {
////                    return 1;
////                }
////                return 0;
////            }
////        });
//    }
//
//    private boolean isChecked(Visitable element) {
//        AttachProductItemViewModel item = (AttachProductItemViewModel)element;
//        return productIds.contains(item.getProductId());
//    }
//
//    @Override
//    public void addElement(List<? extends Visitable> visitables) {
////        Iterator<? extends Visitable> iterator = visitables.iterator();
////        while(iterator.hasNext()){
////            AttachProductItemViewModel itemViewModel = (AttachProductItemViewModel) iterator.next();
////            if(!productIds.contains(itemViewModel.getProductId())){
////                this.visitables.add(itemViewModel);
////            }
////        }
////        this.visitables.addAll(visitables);
////        sortAllVisitableBasedOnChecked();
////        notifyDataSetChanged();
//    }
//
//    @Override
//    public void addMoreData(List<? extends Visitable> data) {
//        addElement(data);
//    }

    public List<AttachProductItemViewModel> getCheckedDataList(){
//        ArrayList<AttachProductItemViewModel> productItemViewModels = new ArrayList<>();
//        int totalChecked = getCheckedCount();
//        for (Visitable visitable: visitables){
//            if(totalChecked == 0){
//                break;
//            }
//            else if(isChecked(visitable)){
//                productItemViewModels.add((AttachProductItemViewModel)visitable);
//                totalChecked--;
//            }
//        }
        return checkedList;
    }
}
