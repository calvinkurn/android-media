package com.tokopedia.seller.myproduct.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.adapter.ItemImageAndText;
import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.seller.myproduct.fragment.ImageChooserDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sebastianuskh on 8/31/16.
 */

public class ClickToSelectWithImage extends FrameLayout {
    Context context;

    @BindView(R2.id.image_selected)
    ImageView imageSelected;

    @BindView(R2.id.desc_selected)
    TextView descSelected;

    ImageChooserDialog imageChooserDialog;

    public ClickToSelectWithImage(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public ClickToSelectWithImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);

    }

    public ClickToSelectWithImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);

    }

    public ClickToSelectWithImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView(context);
    }

    void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_selector, this, true);
        ButterKnife.bind(this);
    }

    public void setSelection(ItemImageAndText item) {
        ImageHandler.LoadImage(imageSelected, item.getData().first);
        descSelected.setText(item.getData().second);
    }

    public String getSelectedItem() {
        return descSelected.getText().toString();
    }


    public void showDialog(ArrayList<CatalogDataModel.Catalog> catalogs) {

        FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
        Fragment prev = ((Activity) context).getFragmentManager().findFragmentByTag(ImageChooserDialog.TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();

        imageChooserDialog = ImageChooserDialog.newInstance(catalogs);
        imageChooserDialog.setTitle(context.getString(R.string.choose_catalog));
        imageChooserDialog.setSelected(descSelected.getText().toString());
        imageChooserDialog.show(((Activity) context).getFragmentManager(),ImageChooserDialog.TAG);

    }

    public void dismissDialog() {
        if(imageChooserDialog != null)
            imageChooserDialog.dismiss();
    }

    public void clearSelection() {
        ImageHandler.loadImageWithId(imageSelected, R.drawable.ic_add_black_48dp);
        descSelected.setText(context.getString(R.string.choose_catalog_prompt));
    }
}
