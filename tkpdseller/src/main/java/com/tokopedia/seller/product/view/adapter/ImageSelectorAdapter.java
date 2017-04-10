package com.tokopedia.seller.product.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.model.ImageSelectModel;

import java.io.File;
import java.net.URI;
import java.util.List;

/**
 * Created by hendry on 04/07/2017.
 */
public class ImageSelectorAdapter extends RecyclerView.Adapter<ImageSelectorAdapter.ViewHolder> {
    private List<ImageSelectModel> imageSelectModelList;
    private int limit;
    private Drawable addPictureDrawable;
    private OnImageSelectionListener onImageSelectionListener;
    private String mainPrimaryImageString;

    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_ADD = 2;

    private int currentSelectedIndex = -1;
    private int currentPrimaryImageIndex = -1;

    public ImageSelectorAdapter(@NonNull List<ImageSelectModel> imageSelectModelList,
                                int limit,
                                Drawable addPictureDrawable,
                                OnImageSelectionListener onImageSelectionListener,
                                String mainPrimaryImageString) {
        this.imageSelectModelList = imageSelectModelList;
        this.limit = limit;
        this.addPictureDrawable = addPictureDrawable;
        this.onImageSelectionListener = onImageSelectionListener;
        this.mainPrimaryImageString = mainPrimaryImageString;
    }

    public void setOnImageSelectionListener(OnImageSelectionListener onImageSelectionListener) {
        this.onImageSelectionListener = onImageSelectionListener;
    }

    public interface OnImageSelectionListener {
        void onAddClick();

        void onItemClick(int position, ImageSelectModel imageSelectModel);
    }

    public void addImage(ImageSelectModel imageSelectModel) {
        if (imageSelectModelList.size() == 0) {
            imageSelectModel.setPrimary(true);
            currentPrimaryImageIndex = 0;
        }
        imageSelectModelList.add(imageSelectModel);
        notifyItemChanged(imageSelectModelList.size() - 1);
        notifyNextAddProductIfNeeded();
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
    }

    public void setImage(@NonNull List<ImageSelectModel> imageSelectModelList) {
        this.imageSelectModelList = imageSelectModelList;
        currentPrimaryImageIndex = -1;
        switchPrimaryImageIfNeeded(0, imageSelectModelList);
        notifyDataSetChanged();
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
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_images_select, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (getItemViewType(position) == VIEW_TYPE_ADD) {
            holder.imageView.setImageDrawable(addPictureDrawable);
            if (holder.textViewMainPicture != null) {
                holder.textViewMainPicture.setVisibility(View.INVISIBLE);
            }
            return;
        }
        ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
        if (!isValidURL(imageSelectModel.getUri())) {
            ImageHandler.loadImageFromFileFitCenter(
                    holder.itemView.getContext(),
                    holder.imageView,
                    new File(imageSelectModel.getUri())
            );
        } else {
            ImageHandler.LoadImage(holder.imageView, imageSelectModel.getUri());
        }

        if (currentPrimaryImageIndex == position) {
            if (holder.textViewMainPicture != null) {
                holder.textViewMainPicture.setVisibility(View.VISIBLE);
            }
        } else {
            if (holder.textViewMainPicture != null) {
                holder.textViewMainPicture.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return imageSelectModelList.size() + ((imageSelectModelList.size() < limit) ? 1 : 0);
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
            implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;
        TextView textViewMainPicture;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_picture);
            if (TextUtils.isEmpty(mainPrimaryImageString)) {
                textViewMainPicture = null;
            } else {
                textViewMainPicture = (TextView) itemView.findViewById(R.id.text_mainpicture);
                textViewMainPicture.setText(mainPrimaryImageString);
            }

            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (getItemViewType()) {
                case VIEW_TYPE_ADD:
                    if (onImageSelectionListener != null) {
                        onImageSelectionListener.onAddClick();
                    }
                    break;
                case VIEW_TYPE_ITEM:
                    int position = getAdapterPosition();
                    currentSelectedIndex = position;
                    if (onImageSelectionListener != null) {
                        onImageSelectionListener.onItemClick(position,
                                imageSelectModelList.get(position));
                    }
                    break;
            }
        }

        /**
         * On long click not used, but keep this, it may be used for later
         *
         * @param v
         * @return
         */
        @Override
        public boolean onLongClick(View v) {
            onClick(v);
//            Context context = itemView.getContext();
//            if(context != null && context instanceof MultiSelectInterface){
//                ((AppCompatActivity) context).startSupportActionMode(
//                        ((MultiSelectInterface)context)
//                                .getMultiSelectorCallback(AddProductFragment.FRAGMENT_TAG)
//                );
//            }
//            multiSelector.setSelected(this, true);
            return true;
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
        imageSelectModel.setUri(path);
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
        } else { // if make it to not primary, make the first position primary
            if (currentPrimaryImageIndex == position && currentPrimaryImageIndex != 0) {
                currentPrimaryImageIndex = 0;
                imageSelectModelList.get(0).setPrimary(true);
                notifyItemChanged(0);
            }
        }
        ImageSelectModel imageSelectModel = imageSelectModelList.get(position);
        imageSelectModel.setPrimary(isPrimary);
        notifyItemChanged(position);
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
        imageSelectModelBefore.setUri(imageSelectModelAfter.getUri());
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

    /**
     * check if path is url or not
     *
     * @param urlStr url or sdcard path
     * @return
     */
    public static boolean isValidURL(String urlStr) {
        try {
            URI uri = new URI(urlStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (Exception e) {
            return false;
        }
    }


}
