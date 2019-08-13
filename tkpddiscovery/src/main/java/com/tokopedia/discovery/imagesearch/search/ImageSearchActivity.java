package com.tokopedia.discovery.imagesearch.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryActivity;
import com.tokopedia.discovery.newdiscovery.base.RedirectionListener;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductViewModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.discovery.search.view.DiscoverySearchView;
import com.tokopedia.imagepicker.picker.gallery.type.GalleryType;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerEditorBuilder;
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerTabTypeDef;
import com.tokopedia.imagepicker.picker.main.builder.ImageRatioTypeDef;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_BRIGHTNESS;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CONTRAST;
import static com.tokopedia.imagepicker.picker.main.builder.ImageEditActionTypeDef.ACTION_CROP;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchActivity extends DiscoveryActivity
        implements ImageSearchContract.View, RedirectionListener {

    private static final int REQUEST_CODE_IMAGE = 2390;
    private static final String EXTRA_PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    @Inject
    ImageSearchPresenter searchPresenter;

    private SearchComponent searchComponent;

    public static Intent newInstance(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ImageSearchActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static void moveTo(AppCompatActivity activity,
                              ProductViewModel productViewModel) {
        if (activity != null) {
            Intent intent = new Intent(activity, ImageSearchActivity.class);
            intent.putExtra(EXTRA_PRODUCT_VIEW_MODEL, productViewModel);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<ImageRatioTypeDef> imageRatioTypeDefArrayList = new ArrayList<>();

        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.ORIGINAL);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_1_1);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_3_4);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_4_3);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_16_9);
        imageRatioTypeDefArrayList.add(ImageRatioTypeDef.RATIO_9_16);

        ImagePickerEditorBuilder imagePickerEditorBuilder = new ImagePickerEditorBuilder
                (new int[]{ACTION_CROP, ACTION_BRIGHTNESS, ACTION_CONTRAST},
                        false,
                        imageRatioTypeDefArrayList);

        ImagePickerBuilder builder = new ImagePickerBuilder(getString(R.string.choose_image),
                new int[]{ImagePickerTabTypeDef.TYPE_GALLERY, ImagePickerTabTypeDef.TYPE_CAMERA}, GalleryType.IMAGE_ONLY, ImagePickerBuilder.DEFAULT_MAX_IMAGE_SIZE_IN_KB,
                ImagePickerBuilder.IMAGE_SEARCH_MIN_RESOLUTION, null, true,
                imagePickerEditorBuilder, null);

        Intent intent = ImageSearchImagePickerActivity.getIntent(this, builder);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
        overridePendingTransition(0, 0);
    }

    private void proceed(Bundle savedInstanceState) {
        initInjector();
        setPresenter(searchPresenter);
        searchPresenter.attachView(this);
        searchPresenter.setDiscoveryView(this);

        ProductViewModel productViewModel =
                getIntent().getParcelableExtra(EXTRA_PRODUCT_VIEW_MODEL);

        String searchQuery = getIntent().getStringExtra(BrowseProductRouter.EXTRAS_SEARCH_TERM);

        if (productViewModel != null) {
            setLastQuerySearchView(productViewModel.getQuery());
            loadSection(productViewModel);

            setToolbarTitle(getString(R.string.image_search_title));
        } else if (!TextUtils.isEmpty(searchQuery)) {
            createAndSetSearchParameter(searchQuery);
            onProductQuerySubmit();
        } else {
            searchView.showSearch(true, false);
        }

        super.initView();
    }

    private void createAndSetSearchParameter(String searchQuery) {
        SearchParameter searchParameter = new SearchParameter();
        searchParameter.setSearchQuery(searchQuery);

        this.searchParameter = searchParameter;
    }

    private void initInjector() {
        searchComponent =
                DaggerSearchComponent.builder()
                        .appComponent(getApplicationComponent())
                        .build();

        searchComponent.inject(this);
    }

    private void loadSection(ProductViewModel productViewModel) {
        addFragment(R.id.container, ImageSearchProductListFragment.newInstance(productViewModel));
    }

    private void addFragment(int containerViewId, ImageSearchProductListFragment fragment) {
        if (!isFinishing() && !fragment.isAdded()) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected int getLayoutRes() {
        return super.getLayoutRes();
    }

    @Override
    public void performNewProductSearch(String queryParams) {

    }

    @Override
    public void showSearchInputView() {
        searchView.showSearch(true, DiscoverySearchView.TAB_DEFAULT_SUGGESTION);
        searchView.setFinishOnClose(false);
    }

    @Override
    protected void onDestroy() {
        searchPresenter.detachView();
        super.onDestroy();
    }
}
