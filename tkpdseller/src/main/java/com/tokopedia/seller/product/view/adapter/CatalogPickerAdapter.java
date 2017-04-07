package com.tokopedia.seller.product.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.customadapter.BaseLinearRecyclerViewAdapter;
import com.tokopedia.core.customadapter.LoadingDataBinder;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.data.source.cloud.model.catalogdata.Catalog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hendry s on 4/5/17.
 */

public class CatalogPickerAdapter extends BaseLinearRecyclerViewAdapter{
    private List<Catalog> catalogViewModelList;
    private int maxRows;

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_NO_USE_CATALOG = 2;

    private int selectedCatalogId;
    private int selectedPosition = -1;
    private int noCatalogCount;

    public CatalogPickerAdapter(List<Catalog> catalogViewModelList, int selectedCatalogId, int maxRows) {
        this.catalogViewModelList = new ArrayList<>();
        Catalog notUseCatalog = new Catalog();
        notUseCatalog.setCatalogId(-1);
        this.catalogViewModelList.add(notUseCatalog);
        if (selectedCatalogId == -1) {
            selectedPosition = 0;
        }
        noCatalogCount = 1;

        if (catalogViewModelList!= null) {
            this.catalogViewModelList.addAll(catalogViewModelList);
        }
        this.selectedCatalogId = selectedCatalogId;
        this.maxRows = maxRows;
    }

    public int getSelectedCatalogId() {
        return selectedCatalogId;
    }

    public String getSelectedCatalogName() {
        if (selectedPosition> -1) {
            return catalogViewModelList.get(selectedPosition).getCatalogName();
        }
        return "";
    }

    public void addMoreData(List<Catalog> catalogViewModelList){
        int prevSize = this.catalogViewModelList.size();
        int addedSize = catalogViewModelList.size();
        if (addedSize == 0) return;

        this.catalogViewModelList.addAll(catalogViewModelList);

        // position for selected is not selected, perhaps when load more, the selectedItem is there
        if (selectedPosition < 0) {
            for (int i=prevSize, sizei = this.catalogViewModelList.size(); i<sizei; i++) {
                Catalog catalog = this.catalogViewModelList.get(i);
                if (catalog.getCatalogId() == selectedCatalogId) {
                    // found the selected position!
                    selectedPosition = i;
                    break;
                }
            }
        }

        this.notifyItemRangeChanged(prevSize, addedSize);
    }

    public void setMaxRows(int maxRows) {
        if (this.maxRows != maxRows) {
            this.maxRows = maxRows;
        }
    }

    private class CatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CatalogViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // get previous position
            int prevSelectedPosition = -1;
            if (selectedPosition > -1) {
                prevSelectedPosition = selectedPosition;
            }
            // update the selectedId and position
            selectedPosition = position;
            selectedCatalogId = catalogViewModelList.get(position).getCatalogId();

            // notify the UI
            if (prevSelectedPosition > -1) {
                notifyItemChanged(prevSelectedPosition);
            }
            notifyItemChanged(selectedPosition);
        }
    }


    private class ItemViewHolder extends CatalogViewHolder implements View.OnClickListener {
        ImageView imageCatalog;
        TextView textCatalogName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imageCatalog = (ImageView) itemView.findViewById(R.id.image_catalog);
            textCatalogName = (TextView) itemView.findViewById(R.id.text_catalog_name);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NO_USE_CATALOG: {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_no_use_catalog, parent, false);
                return new CatalogViewHolder(view);
            }
            case VIEW_TYPE_ITEM: {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_simple_catalog, parent, false);
                return new ItemViewHolder(view);
            }
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    public boolean hasNextData (){
        return this.maxRows > getNonEmptyCatalogListSize();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_NO_USE_CATALOG:
                holder.itemView.setSelected(selectedPosition == position);
                break;
            case VIEW_TYPE_ITEM:
                Catalog catalogViewModel = catalogViewModelList.get(position);
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                new ImageHandler(itemViewHolder.imageCatalog.getContext())
                        .loadImage(itemViewHolder.imageCatalog, catalogViewModel.getCatalogImage());
                itemViewHolder.textCatalogName.setText(catalogViewModel.getCatalogName());
                holder.itemView.setSelected(selectedPosition == position);
                break;
            default:
                super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (catalogViewModelList == null || catalogViewModelList.size() == 0) {
            return super.getItemViewType(position); // might be empty or retry
        }
        else if (getNonEmptyCatalogListSize() == 0){ // all is no catalog
            if(isLoading()){
                return VIEW_LOADING;
            }else if(isRetry()) {
                return VIEW_RETRY;
            }else{
                return VIEW_TYPE_NO_USE_CATALOG;
            }
        }
        else if (position == catalogViewModelList.size()) {
            return VIEW_LOADING; // last position is loading for load more
        }
        else {
            if (catalogViewModelList.get(position).getCatalogId() == -1) {
                return VIEW_TYPE_NO_USE_CATALOG;
            }
            else {
                return VIEW_TYPE_ITEM; //else is catalog Item
            }
        }
    }

    @Override
    public int getItemCount() {
        if (catalogViewModelList == null || catalogViewModelList.size() == 0) {
            return isEmpty()?1:0;
        }
        // no catalog and we want to retry or loading
        else if (getNonEmptyCatalogListSize() == 0){
            if (isRetry())return 1;
            else if (isLoading())return 1;
            else return noCatalogCount;
        }
        // catalog is more than 1
        else {
            return catalogViewModelList.size() + (isLoading() ?1 : 0); // last item for loadmore
        }
    }

    public int getNonEmptyCatalogListSize(){
        if (catalogViewModelList == null || catalogViewModelList.size() == 0) {
            return 0;
        }
        else {
            return catalogViewModelList.size() - noCatalogCount;
        }
    }

    @Override
    public void showLoading(boolean isLoading) {
        int isLoadingInt = isLoading? 1: 0;
        if (loading == isLoadingInt) return;

        //setloadFull to false
        ((LoadingDataBinder) getDataBinder(BaseLinearRecyclerViewAdapter.VIEW_LOADING))
                .setIsFullScreen(false);

        loading = isLoadingInt;
        if (catalogViewModelList== null) {
            notifyDataSetChanged();
        }
        else {
            if (isLoading) {
                notifyItemInserted(catalogViewModelList.size() + 1);
            }
            else {
                notifyItemRemoved(catalogViewModelList.size() + 1);
            }
        }
    }
}
