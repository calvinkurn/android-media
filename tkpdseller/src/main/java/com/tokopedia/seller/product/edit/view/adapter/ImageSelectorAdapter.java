package com.tokopedia.seller.product.edit.view.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.view.model.ImageSelectModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hendry on 04/07/2017.
 */
public class ImageSelectorAdapter extends RecyclerView.Adapter<ImageSelectorAdapter.ViewHolder> {

    public interface OnImageSelectionListener {

        void onAddClick(int position);

        void onItemClick(int position, ImageSelectModel imageSelectModel, boolean isPrimary);
    }

    private ArrayList<ImageSelectModel> imageSelectModelList;
    private int limit;
    private int addPictureDrawableRes;
    private OnImageSelectionListener onImageSelectionListener;
    private String mainPrimaryImageString;
    private String addProductString;
    private RecyclerView recyclerView;

    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_ADD = 2;

    private int currentSelectedIndex = -1;

    public void setOnImageSelectionListener(OnImageSelectionListener onImageSelectionListener) {
        this.onImageSelectionListener = onImageSelectionListener;
    }

    public ImageSelectorAdapter(@NonNull ArrayList<ImageSelectModel> imageSelectModelList,
                                int limit,
                                int addPictureDrawableRes,
                                OnImageSelectionListener onImageSelectionListener,
                                String mainPrimaryImageString,
                                String addProductString) {
        this.imageSelectModelList = imageSelectModelList;
        this.limit = limit;
        this.addPictureDrawableRes = addPictureDrawableRes;
        this.onImageSelectionListener = onImageSelectionListener;
        this.mainPrimaryImageString = mainPrimaryImageString;
        this.addProductString = addProductString;
    }

    public void addImage(ImageSelectModel imageSelectModel) {
        imageSelectModelList.add(imageSelectModel);
        notifyDataSetChanged();

        scrollToEnd();
    }

    public void addImages(@NonNull List<ImageSelectModel> imageSelectModelList) {
        int prevSize = this.imageSelectModelList.size();

        // remove until it is fit
        while ((prevSize + imageSelectModelList.size()) > limit) {
            imageSelectModelList.remove(imageSelectModelList.size() - 1);
        }
        if (imageSelectModelList.isEmpty()) return;
        this.imageSelectModelList.addAll(imageSelectModelList);

        notifyDataSetChanged();

        scrollToEnd();
    }

    public void setImageString(@NonNull List<String> imageStringList) {
        this.imageSelectModelList = new ArrayList<>();
        for (int i = 0; i < imageStringList.size(); i++) {
            imageSelectModelList.add(new ImageSelectModel(imageStringList.get(i)));
        }
        notifyDataSetChanged();
    }

    public void setImage(@NonNull ArrayList<ImageSelectModel> imageSelectModelList) {
        this.imageSelectModelList = imageSelectModelList;
        notifyDataSetChanged();
    }

    public void setCurrentSelectedIndex(int currentSelectedIndex) {
        this.currentSelectedIndex = currentSelectedIndex;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_ADD:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_images_add, parent, false);
                return new AddViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_images_select, parent, false);
                return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (! (holder instanceof ItemViewHolder)) {
            return;
        }
        ItemViewHolder itemViewHolder = (ItemViewHolder)holder;

        final ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
        if (imageSelectModel.isValidURL()) {
            if (imageSelectModel.getWidth() != 0 && imageSelectModel.getHeight()!= 0) {
                ImageHandler.loadImageFitCenter(holder.imageView.getContext(), holder.imageView,
                        imageSelectModel.getUriOrPath());
            } else {
                ImageHandler.loadImageWithTarget(holder.imageView.getContext(), imageSelectModel.getUriOrPath(), new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        holder.imageView.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.imageView.setImageBitmap(resource);
                        imageSelectModel.setWidth(resource.getWidth());
                        imageSelectModel.setHeight(resource.getHeight());
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        holder.imageView.setImageDrawable(errorDrawable);
                    }
                });
            }
        } else { // local Uri
            ImageHandler.loadImageFromFileFitCenter(
                    holder.itemView.getContext(),
                    holder.imageView,
                    new File(imageSelectModel.getUriOrPath())
            );
        }

        // position 0 is always primary Image
        if (position == 0) {
            if (itemViewHolder.textViewMainPicture != null) {
                itemViewHolder.textViewMainPicture.setVisibility(View.VISIBLE);
            }
        } else {
            if (itemViewHolder.textViewMainPicture != null) {
                itemViewHolder.textViewMainPicture.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return imageSelectModelList.size() + ((imageSelectModelList.size() < limit) ? 1 : 0);
    }

    public ArrayList<ImageSelectModel> getImageSelectModelList() {
        return imageSelectModelList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == imageSelectModelList.size()) {
            return VIEW_TYPE_ADD;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        protected ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_picture);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class AddViewHolder extends ViewHolder{
        TextView addImageTextView;
        public AddViewHolder(View itemView) {
            super(itemView);
            addImageTextView = (TextView) itemView.findViewById(R.id.text_add_product);
            addImageTextView.setText(addProductString);
            imageView.setImageResource(addPictureDrawableRes);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            currentSelectedIndex = -1;
            if (onImageSelectionListener != null) {
                onImageSelectionListener.onAddClick(position);
            }
        }
    }

    public class ItemViewHolder extends ViewHolder{
        TextView textViewMainPicture;
        public ItemViewHolder(View itemView) {
            super(itemView);
            if (TextUtils.isEmpty(mainPrimaryImageString)) {
                textViewMainPicture = null;
            } else {
                textViewMainPicture = (TextView) itemView.findViewById(R.id.text_mainpicture);
                textViewMainPicture.setText(mainPrimaryImageString);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            currentSelectedIndex = position;
            if (onImageSelectionListener != null && position >= 0 && position < imageSelectModelList.size()) {
                ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
                onImageSelectionListener.onItemClick(position, imageSelectModel, isImageModelPrimary(imageSelectModel));
            }
        }
    }

    public void removeSelected() {
        removeSelected(currentSelectedIndex);
    }

    public void removeSelected(int position) {
        if (position < 0 || position >= imageSelectModelList.size()) return;

        imageSelectModelList.remove(position);

        // because it is removed, no selected index anymore
        currentSelectedIndex = -1;

        notifyDataSetChanged();
    }

    public void changeImagePath(String path) {
        changeImagePath(path, currentSelectedIndex);
    }

    public void changeImagePath(String path, int position) {
        if (position < 0 || position >= imageSelectModelList.size()) return;

        ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
        imageSelectModel.setUriOrPath(path);
        notifyItemChanged(position);
    }

    public void changeImageDesc(String description) {
        changeImageDesc(description, currentSelectedIndex);
    }

    public void changeImageDesc(String description, int position) {
        if (position < 0 || position >= imageSelectModelList.size()) return;

        ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
        imageSelectModel.setDescription(description);
        notifyItemChanged(position);
    }

    public void changeImagePrimary(boolean isPrimary) {
        changeImagePrimary(isPrimary, currentSelectedIndex);
    }

    public void changeImagePrimary(boolean isPrimary, int position) {
        if (position < 0 || position >= imageSelectModelList.size()) return;

        // if make it primary, make the prevous primary to false.
        if (isPrimary) {
            movePrimaryToFirst(position);
        }
        notifyDataSetChanged();
    }

    private void movePrimaryToFirst(int position){
        ImageSelectModel model = imageSelectModelList.remove(position);
        imageSelectModelList.add(0,model);
        notifyDataSetChanged();

        scrollToFirst();
    }

    private void scrollToFirst(){
        if (recyclerView!= null) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(0);
                }
            },500);
        }
    }

    private void scrollToEnd(){
        if (recyclerView!= null) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(getItemCount());
                }
            },500);
        }
    }

    public void changeImage(ImageSelectModel imageSelectModel) {
        changeImage(imageSelectModel, currentSelectedIndex);
    }

    public void changeImage(ImageSelectModel imageSelectModelAfter, int position) {
        if (position < 0 || position >= imageSelectModelList.size()) return;

        ImageSelectModel imageSelectModelBefore = imageSelectModelList.get(position);
        imageSelectModelBefore.setUriOrPath(imageSelectModelAfter.getUriOrPath());
        imageSelectModelBefore.setDescription(imageSelectModelAfter.getDescription());
        changeImagePrimary(isImageModelPrimary(imageSelectModelAfter));
        notifyDataSetChanged();
    }

    private boolean isImageModelPrimary(ImageSelectModel imageSelectModel){
        return imageSelectModelList.indexOf(imageSelectModel) == 0;
    }

    public ImageSelectModel getPrimaryImage() {
        return imageSelectModelList.get(0);
    }

    public ImageSelectModel getSelectedImage() {
        if (currentSelectedIndex < 0) return null;
        return imageSelectModelList.get(currentSelectedIndex);
    }

    public int getSelectedImageIndex() {
        return currentSelectedIndex;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }
}
