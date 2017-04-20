package com.tokopedia.seller.product.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.seller.R;
import com.tokopedia.seller.product.view.adapter.ImageSelectorAdapter;
import com.tokopedia.seller.product.view.model.ImageSelectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hendry on 4/7/2017.
 */

public class ImagesSelectView extends FrameLayout {

    public static final int DEFAULT_LIMIT = 5;
    OnCheckResolutionListener onCheckResolutionListener;
    private int recyclerViewPadding;
    private Drawable addPictureDrawable;
    private ImageSelectorAdapter imageSelectorAdapter;
    private String primaryImageString;
    private int imageLimit;
    private String titleString;

    public ImagesSelectView(Context context) {
        super(context);
        init();
    }

    public ImagesSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ImagesSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        applyAttrs(attrs);
        init();
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImagesSelectView);
        recyclerViewPadding = (int) a.getDimension(R.styleable.ImagesSelectView_recyclerviewPadding, 0);
        addPictureDrawable = a.getDrawable(R.styleable.ImagesSelectView_addPictureSrc);
        primaryImageString = a.getString(R.styleable.ImagesSelectView_primaryImageString);
        imageLimit = a.getInt(R.styleable.ImagesSelectView_imageLimit, DEFAULT_LIMIT);
        titleString = a.getString(R.styleable.ImagesSelectView_titleString);
        a.recycle();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_images_select_view, this);

        imageSelectorAdapter = new ImageSelectorAdapter(new ArrayList<ImageSelectModel>(),
                imageLimit, addPictureDrawable, null, primaryImageString);

        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        if (TextUtils.isEmpty(titleString)) {
            textViewTitle.setVisibility(View.GONE);
        }
        else {
            textViewTitle.setText(titleString);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setPadding(recyclerViewPadding, recyclerViewPadding, recyclerViewPadding, recyclerViewPadding);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // disable animation
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // add margin between Items
        recyclerView.addItemDecoration(
                new HorizontalItemDecoration(
                        getContext().getResources().getDimensionPixelOffset(R.dimen.margin_vs)));

        recyclerView.setAdapter(imageSelectorAdapter);
    }

    public void setOnImageSelectionListener (ImageSelectorAdapter.OnImageSelectionListener listener) {
        imageSelectorAdapter.setOnImageSelectionListener(listener);
    }

    public void setOnCheckResolutionListener(OnCheckResolutionListener listener) {
        this.onCheckResolutionListener = listener;
    }

    public void addImageString(String imageUrl){
        if (successHandleResolution(imageUrl)){
            imageSelectorAdapter.addImage(new ImageSelectModel(imageUrl));
        }

    }

    private void handleResolutionFromStringList(List<String> uriList) {
        for (int i = uriList.size() - 1; i >= 0; i--) {
            String uri = uriList.get(i);
            if (!successHandleResolution(uri)) {
                uriList.remove(i);
            }
        }
    }

    private void handleResolutionFromList(List<ImageSelectModel> imageSelectModelList) {
        for (int i = imageSelectModelList.size() - 1; i >= 0; i--) {
            ImageSelectModel imageSelectModel = imageSelectModelList.get(i);
            if (!successHandleResolution(imageSelectModel.getUri())) {
                imageSelectModelList.remove(i);
            }
        }
    }

    public boolean successHandleResolution(String localUri) {
        if (onCheckResolutionListener == null ||
                onCheckResolutionListener.isResolutionCorrect(localUri)) {
            return true;
        }
        else { // resolution is not correct
            onCheckResolutionListener.resolutionCheckFailed(localUri);
            return false;
        }
    }

    public void addImage(ImageSelectModel imageSelectModel){
        if (successHandleResolution(imageSelectModel.getUri())){
            imageSelectorAdapter.addImage(imageSelectModel);
        }
    }

    public void addImages(List<ImageSelectModel> imageSelectModelList){
        handleResolutionFromList(imageSelectModelList);
        if (imageSelectModelList.size() > 0) {
            imageSelectorAdapter.addImages(imageSelectModelList);
        }
    }

    public void addImagesString(List<String> imageStringList){
        handleResolutionFromStringList(imageStringList);

        if (imageStringList.size() > 0) {
            List<ImageSelectModel> imageSelectModelList = new ArrayList<>();
            for (int i = 0, sizei = imageStringList.size(); i < sizei; i++) {
                imageSelectModelList.add(new ImageSelectModel(imageStringList.get(i)));
            }
            imageSelectorAdapter.addImages(imageSelectModelList);
        }
    }

    public void setImage(List<ImageSelectModel> imageSelectModelList){
        handleResolutionFromList(imageSelectModelList);

        if (imageSelectModelList.size() > 0) {
            imageSelectorAdapter.setImage(imageSelectModelList);
        }
    }

    public void changeImagePath (String path) {
        if (successHandleResolution(path)) {
            imageSelectorAdapter.changeImagePath(path);
        }
    }

    public void changeImagePath (String path, int position) {
        if (successHandleResolution(path)) {
            imageSelectorAdapter.changeImagePath(path, position);
        }
    }

    public void changeImageDesc (String description) {
        imageSelectorAdapter.changeImageDesc(description);
    }

    public void changeImageDesc (String description, int position) {
        imageSelectorAdapter.changeImageDesc(description, position);
    }

    public void changeImagePrimary (boolean isPrimary) {
        imageSelectorAdapter.changeImagePrimary(isPrimary);
    }

    public void changeImagePrimary (boolean isPrimary, int position) {
        imageSelectorAdapter.changeImagePrimary(isPrimary, position);
    }

    public void changeImage (ImageSelectModel imageSelectModel) {
        if (successHandleResolution(imageSelectModel.getUri())) {
            imageSelectorAdapter.changeImage(imageSelectModel);
        }
    }

    public void changeImage (ImageSelectModel imageSelectModel, int position) {
        if (successHandleResolution(imageSelectModel.getUri())) {
            imageSelectorAdapter.changeImage(imageSelectModel, position);
        }
    }

    public ImageSelectModel getPrimaryImage () {
        return imageSelectorAdapter.getPrimaryImage();
    }

    public int getPrimaryImageIndex () {
        return imageSelectorAdapter.getPrimaryImageIndex();
    }

    public ImageSelectModel getSelectedImage () {
        return imageSelectorAdapter.getSelectedImage();
    }

    public ImageSelectModel getImageAt (int position) {
        return getImageList().get(position);
    }

    public List<ImageSelectModel> getImageList(){
        return imageSelectorAdapter.getImageSelectModelList();
    }

    public int getSelectedImageIndex () {
        return imageSelectorAdapter.getSelectedImageIndex();
    }

    public void removeImage() {
        imageSelectorAdapter.removeSelected();
    }

    public void removeImage(int position) {
        imageSelectorAdapter.removeSelected(position);
    }

    public int getRemainingEmptySlot() {
        return imageLimit - imageSelectorAdapter.getImageSelectModelList().size();
    }

    public interface OnCheckResolutionListener {
        boolean isResolutionCorrect(String uri);

        void resolutionCheckFailed(String uri);
    }
}
