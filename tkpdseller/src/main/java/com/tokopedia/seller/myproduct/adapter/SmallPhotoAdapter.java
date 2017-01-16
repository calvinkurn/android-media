package com.tokopedia.seller.myproduct.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.core.myproduct.model.constant.ImageModelType;
import com.tokopedia.core.util.MethodChecker;


import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tokopedia.core.myproduct.model.constant.ImageModelType.ACTIVE;
import static com.tokopedia.core.myproduct.model.constant.ImageModelType.INACTIVE;
import static com.tokopedia.core.myproduct.model.constant.ImageModelType.SELECTED;
import static com.tokopedia.core.myproduct.model.constant.ImageModelType.UNSELECTED;

/**
 * Created by noiz354 on 4/8/16.
 */
public class SmallPhotoAdapter extends RecyclerView.Adapter<SmallPhotoAdapter.ViewHolder> {

    public interface SmallPhotoAdapterTouch {
        void movePosition(int position);
    }

    List<ImageModel> datas;

    SmallPhotoAdapterTouch smallPhotoAdapterTouch;

    public SmallPhotoAdapter(List<ImageModel> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_small_gallery_item, parent, false);

        if (viewType == SELECTED.getType() + ACTIVE.getType()) {
            // centang hidupkan
            ViewStub selected = (ViewStub) itemLayoutView.findViewById(R.id.small_selected);
            selected.inflate();
        } else if (viewType == UNSELECTED.getType() + ACTIVE.getType()) {
            // do nothing
        } else if (viewType == SELECTED.getType() + INACTIVE.getType()) {
            // centang hidupkan
            ViewStub selected = (ViewStub) itemLayoutView.findViewById(R.id.small_selected);
            selected.inflate();
            // hidupkan masking
            ViewStub smallInActive = (ViewStub) itemLayoutView.findViewById(R.id.small_inactive);
            smallInActive.inflate();
        } else {
            // hidupkan masking
            ViewStub smallInActive = (ViewStub) itemLayoutView.findViewById(R.id.small_inactive);
            smallInActive.inflate();
        }


        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(datas.get(position), position, getSmallPhotoAdapterTouch());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        for (int viewType : datas.get(position).getTypes()) {
            count += viewType;
        }
        return count;
    }

    public SmallPhotoAdapterTouch getSmallPhotoAdapterTouch() {
        return smallPhotoAdapterTouch;
    }

    public void setSmallPhotoAdapterTouch(SmallPhotoAdapterTouch smallPhotoAdapterTouch) {
        this.smallPhotoAdapterTouch = smallPhotoAdapterTouch;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.border_small_imageview_layout)
        FrameLayout borderSmallImageViewLayout;
        @BindView(R2.id.picture_small_gallery_imageview)
        ImageView pictureSmallGalleryImageView;

        SmallPhotoAdapterTouch smallPhotoAdapterTouch;

        public SmallPhotoAdapterTouch getSmallPhotoAdapterTouch() {
            return smallPhotoAdapterTouch;
        }

        public void setSmallPhotoAdapterTouch(SmallPhotoAdapterTouch smallPhotoAdapterTouch) {
            this.smallPhotoAdapterTouch = smallPhotoAdapterTouch;
        }

        // datas
        ImageModel imageModel;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(ImageModel imageModel, int position
                , SmallPhotoAdapterTouch smallPhotoAdapterTouch) {

            setSmallPhotoAdapterTouch(smallPhotoAdapterTouch);
            this.imageModel = imageModel;
            this.position = position;

            if (imageModel.getPath() == null)
                pictureSmallGalleryImageView.setImageResource(imageModel.getResId());
            else if (!PhotoAdapter.isValidURL(imageModel.getPath()))// assumed from file path
                ImageHandler.loadImageFit2(itemView.getContext()
                        , pictureSmallGalleryImageView
                        , MethodChecker.getUri(MainApplication.getAppContext(), new File(imageModel.getPath())).toString());
            else {// assumed from url
                ImageHandler.loadImageFit2(itemView.getContext(),
                        pictureSmallGalleryImageView, imageModel.getPath());
            }
        }

        @OnClick(R2.id.border_small_imageview_layout)
        public void HolderClick() {
            if (getSmallPhotoAdapterTouch() != null)
                getSmallPhotoAdapterTouch().movePosition(position);
        }
    }

    public void changePicture(int position, ImageModel imageModel) {
        imageModel.clearAll();
        imageModel.setType(ImageModelType.UNSELECTED.getType());
        imageModel.setType(ImageModelType.ACTIVE.getType());
        datas.set(position, imageModel);
        notifyDataSetChanged();
    }
}
