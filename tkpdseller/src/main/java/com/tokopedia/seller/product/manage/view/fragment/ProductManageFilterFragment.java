package com.tokopedia.seller.product.manage.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.tokopedia.seller.common.widget.builder.CheckedBottomSheetBuilder;
import com.tokopedia.seller.product.category.view.activity.CategoryPickerActivity;
import com.tokopedia.seller.product.etalase.view.activity.EtalasePickerActivity;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.ProductManageConstant;
import com.tokopedia.seller.product.manage.view.model.ProductManageFilterModel;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

public class ProductManageFilterFragment extends TkpdBaseV4Fragment {

    private LabelView etalaseLabelView;
    private LabelView categoryLabelView;
    private LabelView conditionLabelView;
    private LabelView catalogLabelView;
    private LabelView productPictureLabelView;
    private Button submitButton;

    private ProductManageFilterModel productManageFilterModel;

    @Override
    protected String getScreenName() {
        return getClass().getSimpleName();
    }

    public static Fragment createInstance(ProductManageFilterModel productManageFilterModel) {
        ProductManageFilterFragment productManageFilterFragment = new ProductManageFilterFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ProductManageConstant.EXTRA_FILTER_SELECTED, productManageFilterModel);
        productManageFilterFragment.setArguments(bundle);
        return productManageFilterFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialFilterModel();
    }

    private void initialFilterModel() {
        if (getArguments() != null && getArguments().getParcelable(ProductManageConstant.EXTRA_FILTER_SELECTED) != null) {
            productManageFilterModel = getArguments().getParcelable(ProductManageConstant.EXTRA_FILTER_SELECTED);
        } else {
            productManageFilterModel = new ProductManageFilterModel();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_manage_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        etalaseLabelView = (LabelView) view.findViewById(R.id.etalase);
        categoryLabelView = (LabelView) view.findViewById(R.id.category);
        conditionLabelView = (LabelView) view.findViewById(R.id.condition);
        catalogLabelView = (LabelView) view.findViewById(R.id.catalog);
        productPictureLabelView = (LabelView) view.findViewById(R.id.product_picture);
        submitButton = (Button) view.findViewById(R.id.button_submit);

        updateView();

        etalaseLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEtalaseOption();
            }
        });
        categoryLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryOption();
            }
        });
        conditionLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConditionOption();
            }
        });
        catalogLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCatalogOption();
            }
        });
        productPictureLabelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProductPictureOption();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitFilter();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_manage_product_filter, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.tkpd_main_green)), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_reset) {
            productManageFilterModel.reset();
            updateView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateView() {
        etalaseLabelView.setContent(productManageFilterModel.getEtalaseProductOptionName());
        categoryLabelView.setContent(productManageFilterModel.getCategoryName());
        switch (productManageFilterModel.getConditionProductOption()) {
            case ConditionProductOption.NOT_USED:
                conditionLabelView.setContent(getString(R.string.product_manage_title_both_condition_option_menu));
                break;
            case ConditionProductOption.NEW:
                conditionLabelView.setContent(getString(R.string.product_manage_title_new_condition_menu));
                break;
            case ConditionProductOption.USED:
                conditionLabelView.setContent(getString(R.string.product_manage_title_old_condition_menu));
                break;
            default:
                conditionLabelView.setContent(getString(R.string.product_manage_title_both_condition_option_menu));
                break;
        }
        switch (productManageFilterModel.getCatalogProductOption()) {
            case CatalogProductOption.NOT_USED:
                catalogLabelView.setContent(getString(R.string.product_manage_title_both_catalog_option_menu));
                break;
            case CatalogProductOption.WITH_CATALOG:
                catalogLabelView.setContent(getString(R.string.product_manage_title_with_catalog_menu));
                break;
            case CatalogProductOption.WITHOUT_CATALOG:
                catalogLabelView.setContent(getString(R.string.product_manage_title_without_catalog_menu));
                break;
            default:
                catalogLabelView.setContent(getString(R.string.product_manage_title_both_catalog_option_menu));
                break;
        }
        switch (productManageFilterModel.getPictureStatusOption()) {
            case PictureStatusProductOption.NOT_USED:
                productPictureLabelView.setContent(getString(R.string.product_manage_title_both_picture_option_menu));
                break;
            case PictureStatusProductOption.WITH_IMAGE:
                productPictureLabelView.setContent(getString(R.string.product_manage_title_with_picture_menu));
                break;
            case PictureStatusProductOption.WITHOUT_IMAGE:
                productPictureLabelView.setContent(getString(R.string.product_manage_title_without_picture_menu));
                break;
            default:
                productPictureLabelView.setContent(getString(R.string.product_manage_title_both_picture_option_menu));
                break;
        }
    }

    private void showEtalaseOption() {
        Intent intent = EtalasePickerActivity.createInstance(getActivity(), 0);
        startActivityForResult(intent, ProductManageConstant.REQUEST_CODE_ETALASE);
    }

    private void onSubmitFilter() {
        Intent intent = new Intent();
        intent.putExtra(ProductManageConstant.EXTRA_FILTER_SELECTED, productManageFilterModel);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ProductManageConstant.REQUEST_CODE_CATEGORY:
                if (resultCode == Activity.RESULT_OK) {
                    long categoryId = data.getLongExtra(CategoryPickerActivity.CATEGORY_RESULT_ID, -1);
                    String categoryName = data.getStringExtra(CategoryPickerActivity.CATEGORY_RESULT_ID);
                    productManageFilterModel.setCategoryId(String.valueOf(categoryId));
                    productManageFilterModel.setCategoryName(categoryName);
                    updateView();
                }
                break;
            case ProductManageConstant.REQUEST_CODE_ETALASE:
                if (resultCode == Activity.RESULT_OK) {
                    long etalaseId = data.getIntExtra(EtalasePickerActivity.ETALASE_ID, -1);
                    String etalaseName = data.getStringExtra(EtalasePickerActivity.ETALASE_NAME);
                    productManageFilterModel.setEtalaseProductOption(String.valueOf(etalaseId));
                    productManageFilterModel.setEtalaseProductOptionName(etalaseName);
                    updateView();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showProductPictureOption() {
        showBottomSheetOption(productPictureLabelView.getTitle(), R.menu.menu_product_manage_filter_picture_option,
                productPictureLabelView.getContent(), new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.both_picture_option_menu) {
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.NOT_USED);
                } else if (itemId == R.id.without_picture_option_menu) {
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITHOUT_IMAGE);
                } else if (itemId == R.id.with_picture_option_menu) {
                    productManageFilterModel.setPictureStatusOption(PictureStatusProductOption.WITH_IMAGE);
                }
                updateView();
            }
        });
    }

    private void showCatalogOption() {
        showBottomSheetOption(catalogLabelView.getTitle(), R.menu.menu_product_manage_filter_catalog_option,
                catalogLabelView.getContent(), new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.both_catalog_option_menu) {
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.NOT_USED);
                } else if (itemId == R.id.without_catalog_option_menu) {
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITHOUT_CATALOG);
                } else if (itemId == R.id.with_catalog_option_menu) {
                    productManageFilterModel.setCatalogProductOption(CatalogProductOption.WITH_CATALOG);
                }
                updateView();
            }
        });
    }

    private void showConditionOption() {
        showBottomSheetOption(conditionLabelView.getTitle(), R.menu.menu_product_manage_filter_condition_option,
                conditionLabelView.getContent(), new BottomSheetItemClickListener() {
            @Override
            public void onBottomSheetItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.both_condtion_option_menu) {
                    productManageFilterModel.setConditionProductOption(ConditionProductOption.NOT_USED);
                } else if (itemId == R.id.new_condition_option_menu) {
                    productManageFilterModel.setConditionProductOption(ConditionProductOption.NEW);
                } else if (itemId == R.id.old_condition_option_menu) {
                    productManageFilterModel.setConditionProductOption(ConditionProductOption.USED);
                }
                updateView();
            }
        });
    }

    private void showCategoryOption() {
        long categoryId;
        if (productManageFilterModel.getCategoryId() != null && !productManageFilterModel.getCategoryId().isEmpty()) {
            categoryId = Long.parseLong(productManageFilterModel.getCategoryId());
        } else {
            categoryId = 0;
        }
        CategoryPickerActivity.start(this, getActivity(), ProductManageConstant.REQUEST_CODE_CATEGORY, categoryId);
    }

    private void showBottomSheetOption(
            String title, @MenuRes int menu, String titleSelected, BottomSheetItemClickListener bottomSheetItemClickListener) {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(title)
                .setMenu(menu);

        ((CheckedBottomSheetBuilder) bottomSheetBuilder).setSelection(titleSelected);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(bottomSheetItemClickListener)
                .createDialog();
        bottomSheetDialog.show();
    }
}