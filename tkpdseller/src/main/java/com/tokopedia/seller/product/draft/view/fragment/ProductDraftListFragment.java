package com.tokopedia.seller.product.draft.view.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.fragment.BaseListFragment;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.product.manage.item.common.di.component.ProductComponent;
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder;
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity;
import com.tokopedia.product.manage.item.main.base.view.service.UploadProductService;
import com.tokopedia.product.manage.item.main.draft.data.model.ProductDraftViewModel;
import com.tokopedia.product.manage.item.main.draft.view.activity.ProductDraftAddActivity;
import com.tokopedia.product.manage.item.main.draft.view.activity.ProductDraftEditActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.product.draft.di.component.DaggerProductDraftListComponent;
import com.tokopedia.seller.product.draft.di.module.ProductDraftListModule;
import com.tokopedia.seller.product.draft.view.adapter.ProductDraftAdapter;
import com.tokopedia.seller.product.draft.view.adapter.ProductEmptyDataBinder;
import com.tokopedia.seller.product.draft.view.listener.ProductDraftListView;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ResolutionImageException;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;
import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST;

/**
 * Created by Hendry on 6/19/2017.
 */

public class ProductDraftListFragment extends BaseListFragment<BlankPresenter, ProductDraftViewModel>
        implements ProductEmptyDataBinder.Callback, ProductDraftListView {

    public static final int REQUEST_CODE_ADD_IMAGE = 9001;
    public static final int INSTAGRAM_SELECT_REQUEST_CODE = 9002;

    @Inject
    ProductDraftListPresenter productDraftListPresenter;

    private BroadcastReceiver draftBroadCastReceiver;
    private TkpdProgressDialog progressDialog;
    private MenuItem menuDelete;

    OnProductDraftListFragmentListener onProductDraftListFragmentListener;

    public interface OnProductDraftListFragmentListener {
        void saveValidImagesToDraft(ArrayList<String> localPaths, @NonNull ArrayList<String> imageDescriptionList);
    }

    public static ProductDraftListFragment newInstance() {
        return new ProductDraftListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        final ProductDraftAdapter adapter = new ProductDraftAdapter();
        adapter.setOnDraftDeleteListener(new ProductDraftAdapter.OnDraftDeleteListener() {
            @Override
            public void onDelete(final ProductDraftViewModel draftViewModel, final int position) {
                String message;
                if (TextUtils.isEmpty(draftViewModel.getProductName())) {
                    message = getString(R.string.product_draft_dialog_delete_message);
                } else {
                    message = getString(R.string.product_draft_dialog_delete_name_message, draftViewModel.getProductName());
                }
                AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                        .setMessage(MethodChecker.fromHtml(message))
                        .setPositiveButton(getString(R.string.action_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.confirmDelete(position);
                                productDraftListPresenter.deleteProductDraft(draftViewModel.getProductDraftId());
                                // update total item value so scrolllistener won't retrieve next page.
                                totalItem--;
                                if (totalItem == 0) {
                                    // go to empty state if all data has been deleted
                                    resetPageAndSearch();
                                }
                                eventDraftProductClicked(AppEventTracking.EventLabel.DELETE_DRAFT);
                            }
                        }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                // no op
                            }
                        }).create();
                dialog.show();
            }
        });
        return adapter;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_product_draft_list;
    }

    @Override
    protected void initInjector() {
        super.initInjector();
        DaggerProductDraftListComponent
                .builder()
                .productDraftListModule(new ProductDraftListModule())
                .productComponent(getComponent(ProductComponent.class))
                .build()
                .inject(this);
        productDraftListPresenter.attachView(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_product_draft_list, menu);
        menuDelete = menu.findItem(R.id.menu_delete);
        menuDelete.setVisible(totalItem > 0);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSearchLoaded(@NonNull List<ProductDraftViewModel> list, int totalItem) {
        super.onSearchLoaded(list, totalItem);
        if (menuDelete != null) {
            menuDelete.setVisible(totalItem > 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle)
                    .setMessage(getString(R.string.product_draft_delete_all_draft_dialog_message))
                    .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            productDraftListPresenter.clearAllDraftData();
                        }
                    }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            // no op, just dismiss
                        }
                    });
            AlertDialog dialog = alertDialogBuilder.create();
            dialog.show();

            return true;
        } else if (item.getItemId() == R.id.add_product_menu) {
            item.getSubMenu().findItem(R.id.label_view_add_image).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(ProductAddNameCategoryActivity.Companion.createInstance(getActivity()));
                    return true;
                }
            });
            item.getSubMenu().findItem(R.id.label_view_import_from_instagram).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = AddProductImagePickerBuilder.createPickerIntentInstagramImport(getContext());
                    startActivityForResult(intent, INSTAGRAM_SELECT_REQUEST_CODE);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(ProductDraftViewModel productDraftViewModel) {
        Intent intent;
        if (productDraftViewModel.isEdit()) {
            intent = ProductDraftEditActivity.Companion.createInstance(getActivity(), productDraftViewModel.getProductDraftId());
        } else {
            intent = ProductDraftAddActivity.Companion.createInstance(getActivity(), productDraftViewModel.getProductDraftId());
        }
        eventDraftProductClicked(AppEventTracking.EventLabel.EDIT_DRAFT);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case INSTAGRAM_SELECT_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK &&
                        intent != null) {
                    ArrayList<String> imageUrlOrPathList = intent.getStringArrayListExtra(PICKER_RESULT_PATHS);
                    ArrayList<String> imageDescList = intent.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST);
                    if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                        onProductDraftListFragmentListener.saveValidImagesToDraft(imageUrlOrPathList, imageDescList);
                    }
                }
                break;
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog.NORMAL_PROGRESS);
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isProgress()) {
            progressDialog.showDialog();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isProgress()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        resetPageAndSearch();
        registerDraftReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterDraftReceiver();
    }

    private void registerDraftReceiver() {
        if (draftBroadCastReceiver == null) {
            draftBroadCastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(UploadProductService.ACTION_DRAFT_CHANGED)) {
                        resetPageAndSearch();
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                draftBroadCastReceiver, new IntentFilter(UploadProductService.ACTION_DRAFT_CHANGED));
    }

    private void unregisterDraftReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(draftBroadCastReceiver);
    }

    @Override
    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        ProductEmptyDataBinder emptyGroupAdsDataBinder = new ProductEmptyDataBinder(adapter);
        emptyGroupAdsDataBinder.setEmptyTitleText(getString(R.string.product_draft_draft_product_empty));
        emptyGroupAdsDataBinder.setEmptyContentText(null);
        emptyGroupAdsDataBinder.setEmptyButtonItemText(getString(R.string.product_draft_add_product));
        emptyGroupAdsDataBinder.setCallback(this);
        return emptyGroupAdsDataBinder;
    }

    @Override
    protected void searchForPage(int page) {
        // page is always 0 for draft
        if (isMyServiceRunning(UploadProductService.class)) {
            productDraftListPresenter.fetchAllDraftData();
        } else {
            productDraftListPresenter.fetchAllDraftDataWithUpdateUploading();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    @Override
    public void onEmptyContentItemTextClicked() {
        // no op
    }

    @Override
    public void onEmptyButtonClicked() {
        eventDraftProductClicked(AppEventTracking.EventLabel.ADD_PRODUCT);
        startActivity(new Intent(getActivity(), ProductAddNameCategoryActivity.class));
    }

    public void eventDraftProductClicked(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.CLICK_DRAFT_PRODUCT,
                AppEventTracking.Category.DRAFT_PRODUCT,
                AppEventTracking.Action.CLICK,
                label);
    }

    @Override
    protected void showViewEmptyList() {
        super.showViewEmptyList();
    }

    @Override
    protected void showViewSearchNoResult() {
        super.showViewSearchNoResult();
    }

    @Override
    protected void showViewList(@NonNull List list) {
        super.showViewList(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        productDraftListPresenter.detachView();
    }

    @Override
    public void onSaveBulkDraftSuccess(List<Long> draftProductIdList) {
        hideProgressDialog();
        if (draftProductIdList.size() == 1) {
            ProductDraftAddActivity.Companion.createInstance(getActivity(), draftProductIdList.get(0));
        } else {
            resetPageAndSearch();
            CommonUtils.UniversalToast(getActivity(), getString(R.string.product_draft_instagram_save_success, draftProductIdList.size()));
        }
    }

    @Override
    public void onErrorSaveBulkDraft(Throwable throwable) {
        hideProgressDialog();
        if (throwable instanceof ResolutionImageException) {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.product_instagram_draft_error_save_resolution));
        } else {
            NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.product_instagram_draft_error_save_unknown));
        }
    }

    @Override
    public void hideDraftLoading() {
        hideProgressDialog();
    }

    @Override
    public void onSuccessDeleteAllDraft() {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.product_draft_success_delete_draft));
        resetPageAndSearch();
    }

    @Override
    public void onErrorDeleteAllDraft() {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), getString(R.string.product_draft_error_delete_draft));
    }

    @Override
    protected void onAttachListener(Context context) {
        super.onAttachListener(context);
        onProductDraftListFragmentListener = (OnProductDraftListFragmentListener) context;
    }
}