package com.tokopedia.seller.product.manage.view.fragment;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.core.app.TkpdBaseV4Fragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.bottomsheet.BottomSheetBuilder;
import com.tokopedia.seller.common.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.seller.common.widget.LabelView;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageFilterFragment extends TkpdBaseV4Fragment {

    private LabelView etalase;
    private LabelView category;
    private LabelView status;
    private LabelView condition;
    private LabelView catalog;
    private LabelView productPicture;
    private Button buttonSubmit;

    private ProductManageFilterModel productManageFilterModel;

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_activation_resent, container);
        setHasOptionsMenu(true);

        etalase = (LabelView) view.findViewById(R.id.etalase);
        category = (LabelView) view.findViewById(R.id.category);
        status = (LabelView) view.findViewById(R.id.status);
        condition = (LabelView) view.findViewById(R.id.condition);
        catalog = (LabelView) view.findViewById(R.id.catalog);
        productPicture = (LabelView) view.findViewById(R.id.product_picture);
        buttonSubmit = (Button) view.findViewById(R.id.button_submit);

        etalase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEtalaseOption();
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryOption();
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStatusOption();
            }
        });
        condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConditionOption();
            }
        });
        catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatalogOption();
            }
        });
        productPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductPictureOption();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitFilter();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter_manage_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.menu_reset){

        }
        return super.onOptionsItemSelected(item);
    }

    private void showEtalaseOption(){

    }

    private void onSubmitFilter() {

    }

    private void showProductPictureOption() {
        showBottomSheetOption(productPicture.getTitle(), R.menu.menu_product_manage_filter_picture_option,new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.both_picture_option_menu){
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.NOT_USED);
                }else if(itemId == R.id.without_picture_option_menu){
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITHOUT_IMAGE);
                }else if(itemId == R.id.with_picture_option_menu){
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITH_IMAGE);
                }
            }
        });
    }

    private void showCatalogOption() {
        showBottomSheetOption(catalog.getTitle(), R.menu.menu_product_manage_filter_catalog_option, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.both_catalog_option_menu){
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.NOT_USED);
                }else if(itemId == R.id.without_catalog_option_menu){
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITHOUT_CATALOG);
                }else if(itemId == R.id.with_catalog_option_menu){
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITH_CATALOG);
                }
            }
        });
    }

    private void showConditionOption() {
        showBottomSheetOption(condition.getTitle(), R.menu.menu_product_manage_filter_catalog_option, new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.both_catalog_option_menu){
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.NOT_USED);
                }else if(itemId == R.id.without_catalog_option_menu){
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITHOUT_CATALOG);
                }else if(itemId == R.id.with_catalog_option_menu){
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITH_CATALOG);
                }
            }
        });
    }

    private void showStatusOption() {

    }

    private void showCategoryOption() {

    }

    private void showBottomSheetOption(String title, @MenuRes int menu, BottomSheetItemClickListener bottomSheetItemClickListener) {
        BottomSheetBuilder bottomSheetBuilder = new BottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(title)
                .setMenu(menu);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(bottomSheetItemClickListener)
                .createDialog();
        bottomSheetDialog.show();
    }
}
