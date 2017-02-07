package com.tokopedia.seller.instoped.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.customwidget.SquareImageView;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.seller.R;

import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by Tkpd_Eka on 4/7/2016.
 */
public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  Context context;
    private List<RecyclerViewItem> data;

    public int getLastPostition() {
        return data.size();
    }

    public interface OnItemToggledListener{
        void onSelected(int pos);
        void onUnselected(int pos);
    }

    public interface OnImageClickedListener{
        void onSelected();
        void onUnselected();
    }

    public interface DataSelection{
        boolean isSelected(int position);
    }

    DataSelection dataSelection;

    public DataSelection getDataSelection() {
        return dataSelection;
    }

    public void setDataSelection(DataSelection dataSelection) {
        this.dataSelection = dataSelection;
    }

    public class VHolder extends RecyclerView.ViewHolder {

        public SquareImageView img;
        public View overlay;

        OnImageClickedListener listener;

        public VHolder(View itemView) {
            super(itemView);
            img = (SquareImageView) itemView.findViewById(R.id.img);
            img.setOnClickListener(onImageClicked());
            overlay = itemView.findViewById(R.id.overlay);
        }

        public void setOnImageClickedListener(OnImageClickedListener listener){
            this.listener = listener;
        }

        public void setSelected(boolean selected){
            if(selected){
                overlay.setVisibility(View.VISIBLE);
            }else{
                overlay.setVisibility(View.GONE);
            }
        }

        private View.OnClickListener onImageClicked(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(overlay.getVisibility() == View.GONE) {
                        listener.onSelected();
                    }
                    else {
                        listener.onUnselected();
                    }
                }
            };
        }
    }

//    private List<InstagramMediaModel> models;
    private OnItemToggledListener onItemToggledListener;

    public MediaAdapter(Context context, List<RecyclerViewItem> data) {
        this.context = context;
        this.data = data;
    }

    public void setOnItemToggledListener(OnItemToggledListener onItemToggledListener){
        this.onItemToggledListener = onItemToggledListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instoped_media, parent, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImageHandler.LoadImage(((VHolder) holder).img, ((InstagramMediaModel)(data.get(position))).thumbnail);
        if(checkNotNull(dataSelection)){
            ((VHolder) holder).setSelected(dataSelection.isSelected(position));
        }
        ((VHolder) holder).setOnImageClickedListener(new OnImageClickedListener() {
            @Override
            public void onSelected() {
                onItemToggledListener.onSelected(position);
            }

            @Override
            public void onUnselected() {
                onItemToggledListener.onUnselected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
