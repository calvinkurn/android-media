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

        void onItemClick(int position, ImageSelectModel imageSelectModel);
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
    private int currentPrimaryImageIndex = -1;

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
        if (imageSelectModelList.size() == 0) {
            imageSelectModel.setPrimary(true);
            currentPrimaryImageIndex = 0;
        }
        imageSelectModelList.add(imageSelectModel);
        notifyItemChanged(imageSelectModelList.size() - 1);
        notifyNextAddProductIfNeeded();

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

        switchPrimaryImageIfNeeded(prevSize, imageSelectModelList);

        notifyItemChanged(prevSize);
        notifyItemRangeInserted(prevSize + 1, imageSelectModelList.size());
        notifyNextAddProductIfNeeded();

        scrollToEnd();
    }

    public void setImageString(@NonNull List<String> imageStringList) {
        this.imageSelectModelList = new ArrayList<>();
        for (int i = 0; i < imageStringList.size(); i++) {
            imageSelectModelList.add(new ImageSelectModel(imageStringList.get(i)));
        }
        currentPrimaryImageIndex = -1;
        switchPrimaryImageIfNeeded(0, imageSelectModelList);
        notifyDataSetChanged();
    }

    public void setImage(@NonNull ArrayList<ImageSelectModel> imageSelectModelList) {
        this.imageSelectModelList = imageSelectModelList;
        currentPrimaryImageIndex = -1;
        switchPrimaryImageIfNeeded(0, imageSelectModelList);
        notifyDataSetChanged();
    }

    public void setCurrentSelectedIndex(int currentSelectedIndex) {
        this.currentSelectedIndex = currentSelectedIndex;
    }

    private void switchPrimaryImageIfNeeded(int prevSize, @NonNull List<ImageSelectModel> imageSelectModelList) {
        int previousPrimaryImageIndex = currentPrimaryImageIndex;
        for (int i = 0, sizei = imageSelectModelList.size(); i < sizei; i++) {
            ImageSelectModel imageSelectModel = imageSelectModelList.get(i);
            if (imageSelectModel.isPrimary()) {
                if (currentPrimaryImageIndex != previousPrimaryImageIndex &&
                        currentPrimaryImageIndex != prevSize + 1) { // it comes from previous iteration, set revious imagePrimary to false
                    imageSelectModelList.get(currentPrimaryImageIndex - prevSize).setPrimary(false);
                }
                // set the index image primary to this
                currentPrimaryImageIndex = prevSize + i;
            }
        }
        //primary index is moved
        if (previousPrimaryImageIndex > -1 && currentPrimaryImageIndex != previousPrimaryImageIndex) {
            notifyItemChanged(previousPrimaryImageIndex);
        }
        // no primary yet, set to 0
        if (currentPrimaryImageIndex == -1 && imageSelectModelList.size() > 0) {
            imageSelectModelList.get(0).setPrimary(true);
            currentPrimaryImageIndex = prevSize;
        }
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
        } else { // local Uri
            ImageHandler.loadImageFromFileFitCenter(
                    holder.itemView.getContext(),
                    holder.imageView,
                    new File(imageSelectModel.getUriOrPath())
            );
        }


        if (currentPrimaryImageIndex == position) {
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
                onImageSelectionListener.onItemClick(position, imageSelectModelList.get(position));
            }
        }
    }

    public void notifyNextAddProductIfNeeded() {
        if (imageSelectModelList.size() < limit) {
            notifyItemInserted(imageSelectModelList.size());
        }
    }

    public void removeSelected() {
        removeSelected(currentSelectedIndex);
    }

    public void removeSelected(int position) {
        if (position < 0 || position >= imageSelectModelList.size()) return;
        // if the removed position is actually primary image, remove primary index,
        // else just decrease index by 1
        if (currentPrimaryImageIndex == position) {
            currentPrimaryImageIndex = -1;
        } else {
            currentPrimaryImageIndex--;
        }
        imageSelectModelList.remove(position);
        notifyItemRemoved(position);

        // because it is removed, no selected index anymore
        currentSelectedIndex = -1;

        // if there is no primary image selected (maybe because it is removed), make the first image primary
        if (currentPrimaryImageIndex == -1) {
            switchPrimaryImageIfNeeded(0, this.imageSelectModelList);
            if (currentPrimaryImageIndex > -1) {
                notifyItemChanged(currentPrimaryImageIndex);
            }
        }
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
            unselectPreviousPrimaryIfExist(currentPrimaryImageIndex);
            currentPrimaryImageIndex = position;
            ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
            imageSelectModel.setPrimary(true);

            movePrimaryToFirst();
        } else { // if make it to not primary, make the first position primary
            if (currentPrimaryImageIndex == position && currentPrimaryImageIndex != 0) {
                currentPrimaryImageIndex = 0;
                imageSelectModelList.get(0).setPrimary(true);
                notifyItemChanged(0);
            }
            ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
            imageSelectModel.setPrimary(false);
            notifyItemChanged(position);
        }
    }

    private void movePrimaryToFirst(){
        ImageSelectModel model = imageSelectModelList.remove(currentPrimaryImageIndex);
        imageSelectModelList.add(0,model);
        currentPrimaryImageIndex = 0;
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

    private void unselectPreviousPrimaryIfExist(int previousPrimaryIndex) {
        if (previousPrimaryIndex > -1) {
            imageSelectModelList.get(previousPrimaryIndex).setPrimary(false);
            notifyItemChanged(previousPrimaryIndex);
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
        changeImagePrimary(imageSelectModelAfter.isPrimary());
        notifyItemChanged(position);
    }

    public ImageSelectModel getPrimaryImage() {
        if (currentPrimaryImageIndex < 0) return null;
        return imageSelectModelList.get(currentPrimaryImageIndex);
    }

    public int getPrimaryImageIndex() {
        return currentPrimaryImageIndex;
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
