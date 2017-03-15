package com.tokopedia.seller.myproduct;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.ArrayFragmentStatePagerAdapter;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.seller.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.instoped.model.InstagramMediaModelParc;
import com.tokopedia.seller.myproduct.dialog.DialogFragmentImageAddProduct;
import com.tokopedia.seller.myproduct.fragment.ImageChooserDialog;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.seller.myproduct.adapter.SmallPhotoAdapter;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.fragment.ChooserDialogFragment;
import com.tokopedia.seller.myproduct.fragment.ChooserFragment;
import com.tokopedia.core.myproduct.model.ImageModel;
import com.tokopedia.seller.myproduct.model.SimpleTextModel;
import com.tokopedia.core.myproduct.model.constant.ImageModelType;
import com.tokopedia.seller.myproduct.presenter.ProductSocMedPresenter;
import com.tokopedia.seller.myproduct.utils.AddProductType;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.myproduct.service.ProductService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tokopedia.core.instoped.InstagramMediaModelUtil.convertTo;
import static com.tokopedia.core.myproduct.utils.VerificationUtils.validateAllInstoped;

/**
 * Created by m.normansyah on 4/7/16.
 */
public class ProductSocMedActivity extends BaseProductActivity implements
        ProductSocMedPresenter,
        ChooserFragment.OnListFragmentInteractionListener,
        DownloadResultSender,
        DialogFragmentImageAddProduct.DFIAListener,
        ImageChooserDialog.SelectWithImage
{
    public static final String DEFAULT_HTTP = "http://www.glamour.com/images/fashion/2016/03/Iskra-02-main.jpg";
    Toolbar toolbar;
    RecyclerView productsSocMedThumnNail;
    ViewPager productSocMedViewPager;

    PagerAdapter2 pagerAdapter;
    SmallPhotoAdapter adapter;

    ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
//                    recalculateView();

                    for(int i = 0; i< imageModels.size(); i++){
                        if(position==i)
                            continue;

                        ImageModel img = imageModels.get(i);
                        boolean isSelected = img.getTypes().contains(ImageModelType.SELECTED.getType());
                        img.clearAll();
                        if(isSelected){
                            img.setType(ImageModelType.SELECTED.getType());
                        }else{
                            img.setType(ImageModelType.UNSELECTED.getType());
                        }
                        img.setType(ImageModelType.INACTIVE.getType());
                        // set to list data
                        imageModels.set(i, img);
                    }

                    ImageModel imageModel = imageModels.get(position);
                    boolean isSelected = imageModel.getTypes().contains(ImageModelType.SELECTED.getType());
                    imageModel.clearAll();
                    if(isSelected){
                        imageModel.setType(ImageModelType.SELECTED.getType());
                    }else{
                        imageModel.setType(ImageModelType.UNSELECTED.getType());
                    }
                    imageModel.setType(ImageModelType.ACTIVE.getType());
                    imageModels.set(position, imageModel);

                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onPageScrollStateChanged(int state) {


                }
            };

    SparseArray<InstagramMediaModel> instagramMediaModelSparseArray;

    private List<ImageModel> imageModels = new ArrayList<ImageModel>(){
        {
            ImageModel temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.UNSELECTED.getType());
            temp.setType(ImageModelType.INACTIVE.getType());
            add(temp);

            temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.UNSELECTED.getType());
            temp.setType(ImageModelType.ACTIVE.getType());
            add(temp);

            temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.SELECTED.getType());
            temp.setType(ImageModelType.INACTIVE.getType());
            add(temp);

            temp = new ImageModel(DEFAULT_HTTP);
            temp.setType(ImageModelType.SELECTED.getType());
            temp.setType(ImageModelType.ACTIVE.getType());
            add(temp);
        }
    };

    private List<InstagramMediaModel> images = new ArrayList<>();

    TkpdProgressDialog tkpdProgressDialog;
    private BroadcastReceiver addProductReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_socmed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        productsSocMedThumnNail = (RecyclerView) findViewById(R.id.products_soc_med_thumbnail);
        productSocMedViewPager = (ViewPager) findViewById(R.id.product_soc_med_viewpager);

        toolbar.setTitle(R.string.title_activity_add_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        if(intent != null ) {
            instagramMediaModelSparseArray
                    = Parcels.unwrap(intent.getParcelableExtra(GalleryActivity.PRODUCT_SOC_MED_DATA));

            //[START] convert instagram model to new models
            imageModels = new ArrayList<>();
            images.addAll(fromSparseArray(instagramMediaModelSparseArray));

            for (int i = 0; i < instagramMediaModelSparseArray.size(); i++) {
                String standardResolution = instagramMediaModelSparseArray.get(
                        instagramMediaModelSparseArray.keyAt(i)).standardResolution;
                ImageModel imageModel = new ImageModel(
                        standardResolution
                );
                imageModels.add(imageModel);
            }
        }
        //[END] convert instagram model to new models

        recreateAddProducts(images);

        //[START] set selection for first index
        ImageModel imageModel = imageModels.get(0);
        imageModel.clearAll();
        imageModel.setType(ImageModelType.UNSELECTED.getType());
        imageModel.setType(ImageModelType.ACTIVE.getType());
        imageModels.set(0, imageModel);
        //[START] set selection for first index

        adapter = new SmallPhotoAdapter(imageModels);
        adapter.setSmallPhotoAdapterTouch(new SmallPhotoAdapter.SmallPhotoAdapterTouch() {
            @Override
            public void movePosition(int position) {
                if(pagerAdapter != null && pagerAdapter.getCount() > 0 && position <= pagerAdapter.getCount()-1){
                    productSocMedViewPager.setCurrentItem(position);
                }
            }
        });
        productsSocMedThumnNail.setAdapter(adapter);
        productsSocMedThumnNail.setItemAnimator(new DefaultItemAnimator());
        productsSocMedThumnNail.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        addProductReceiver = getProductServiceReceiver();

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(addProductReceiver);
    }

    private BroadcastReceiver getProductServiceReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle resultData = intent.getExtras();
                int resultCode = resultData.getInt(TkpdState.ProductService.STATUS_FLAG);
                int type = resultData.getInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.INVALID_TYPE);
                Fragment fragment = null;
                switch(type){
                    case TkpdState.ProductService.ADD_PRODUCT:
                    case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                        // default position is "0"
                        int position = resultData.getInt(TkpdState.ProductService.PRODUCT_POS, 0);
                        fragment = getFragment(position);
                        break;
                    default:
                        throw new UnsupportedOperationException("please pass type when want to process it !!!");
                }

                //check if Fragment implement necessary interface
                if(fragment!=null && fragment instanceof BaseView && type != TkpdState.ProductService.INVALID_TYPE){
                    switch (resultCode) {
                        case TkpdState.ProductService.STATUS_RUNNING:
                            switch(type) {
                                case TkpdState.ProductService.ADD_PRODUCT:
                                case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                                    showProgress(false);
                                    ((BaseView) fragment).setData(type, resultData);
                                    break;
                            }
                            break;
                        case TkpdState.ProductService.STATUS_DONE:
                            break;
                        case TkpdState.ProductService.STATUS_ERROR:
                            switch(type){

                            }

                    }// end of status download service
                }
            }
        };
    }

    private List<InstagramMediaModel> fromSparseArray(SparseArray<InstagramMediaModel> data){
        List<InstagramMediaModel> modelList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            InstagramMediaModel rawData = data.get(
                    data.keyAt(i));
            modelList.add(rawData);
        }
        return modelList;
    }


    private void recreateAddProducts(List<InstagramMediaModel> girlImages) {
        List<InstagramMediaModelParc> items = convertTo(girlImages);
        pagerAdapter = new PagerAdapter2(getSupportFragmentManager(), items);
        productSocMedViewPager.setAdapter(pagerAdapter);
//        productSocMedViewPager.setOffscreenPageLimit(2);
        productSocMedViewPager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //[START] test for getting fragment inside viewpager
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getFragment(0).WarningDialog();
//            }
//        }, 3000);
        //[END] test for getting fragment inside viewpager
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null){
            int position = data.getIntExtra(ProductActivity.ADD_PRODUCT_IMAGE_LOCATION
                    , ProductActivity.ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            Log.d(TAG, messageTAG + imageUrl+" & "+position);
            AddProductFragment adf = getFragment(productSocMedViewPager.getCurrentItem());
            if(adf != null && CommonUtils.checkNotNull(imageUrl)){
                if (imageUrl.startsWith("http")) {
                    List<String> toDownloadList = new ArrayList<String>();
                    toDownloadList.add(imageUrl);
                    adf.addImageAfterSelectDownload(toDownloadList, position);
                }
                else {
                    adf.addImageAfterSelect(imageUrl, position);
                }
            }
            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryBrowser.IMAGE_URLS);
            if(adf != null && checkCollectionNotNull(imageUrls)){
                if (imageUrls.get(0).startsWith("http")) {
                    adf.addImageAfterSelectDownload(imageUrls, position);
                }
                else {
                    adf.addImageAfterSelect(imageUrls);
                }
            }
        }
    }

    @Override
    public AddProductFragment getFragment(int position){
        return (AddProductFragment) pagerAdapter.findFragmentByPosition(position);
    }

    @Override
    public int getCurrentFragmentPosition() {
        return productSocMedViewPager.getCurrentItem();
    }

    @Override
    public void removeFragment(int position) {

        // [START] remove fragment and recreates fragment
        imageModels.remove(position);
        images.remove(position);

        adapter.notifyDataSetChanged();
        pagerAdapter.remove(position);
        if(productSocMedViewPager.getChildCount() == countUploaded()){
            finish();
        }
    }

    private int countUploaded() {
        int count = 0;
        for (ImageModel imageModel : imageModels){
            ArrayList<Integer> types = imageModel.getTypes();
            if (types.contains(ImageModelType.SELECTED.getType())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void noitfyCompleted(int position) {
        ImageModel imageModel = imageModels.get(position);
        imageModel.clearAll();
        imageModel.setType(ImageModelType.SELECTED.getType());
        imageModel.setType(ImageModelType.ACTIVE.getType());

        imageModels.set(position, imageModel);

        Pair<Boolean, String> booleanStringPair = validateAllInstoped(this, imageModels);

        if(booleanStringPair.getModel1()){
            goToManageProduct();
        }

        adapter.notifyItemChanged(position);

        if(checkAllPictureUploadedAndMoveToNextProduct(position)){
            goToManageProduct();
        }

    }

    private void goToManageProduct() {
        Intent intent = new Intent(this, ManageProduct.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putBoolean(ManageProduct.SNACKBAR_CREATE,true);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private boolean checkAllPictureUploadedAndMoveToNextProduct(int position) {

        for(int i = position; i < imageModels.size(); i++){
            ArrayList<Integer> types = imageModels.get(i).getTypes();
            if (!types.contains(ImageModelType.SELECTED.getType())) {
                productSocMedViewPager.setCurrentItem(i);
                return false;
            }
        }

        for (int i = 0 ; i < position; i ++ ){
            ArrayList<Integer> types = imageModels.get(i).getTypes();
            if (!types.contains(ImageModelType.SELECTED.getType())) {
                productSocMedViewPager.setCurrentItem(i);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onListFragmentInteraction(SimpleTextModel item) {
        AddProductFragment adf = getFragment(productSocMedViewPager.getCurrentItem());
        Fragment dialog = getSupportFragmentManager()
                .findFragmentByTag(ChooserDialogFragment.FRAGMENT_TAG);
        switch (((ChooserDialogFragment)dialog).getType()){
            case ProductActivity.ADD_PRODUCT_CATEGORY:
                if(adf != null ){
                    adf.addCategoryAfterSelectV2(item);
                }
                break;
            case ProductActivity.ADD_PRODUCT_CHOOSE_ETALASE:
                if(adf != null){
                    adf.addEtalaseAfterSelect(item);
                }
                break;
        }
        if(dialog!=null){
            ((ChooserDialogFragment)dialog).dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(addProductReceiver, new IntentFilter(TkpdState.ProductService.BROADCAST_ADD_PRODUCT));
        // analytic below : https://phab.tokopedia.com/T18496
        ScreenTracking.sendScreen(this, new ScreenTracking.IOpenScreenAnalytics() {
            @Override
            public String getScreenName() {
                return AppScreen.SCREEN_INSTOPED;
            }
        });
    }

    @Override
    public void onLongClick() {
        Log.e(TAG, "onLongClick not implemented yet");
    }

    @Override
    public void sendDataToInternet(int type, Bundle data) {
        switch (type){
            case TkpdState.ProductService.ADD_PRODUCT:
            case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                ProductService.startDownload(this, data, type);
                break;
            default :
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    @Override
    public void editImage(int action, int position, int fragmentPosition, boolean isPrimary) {
        Fragment fragment = getFragment(fragmentPosition);
        switch (action){
            case DialogFragmentImageAddProduct.DELETE_IMAGE:
                Log.d(TAG, "image delete : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT){
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>(){{add(getString(R.string.error_delete_primary_image));}});
                } else {
                    ((AddProductFragment) fragment).removeImageSelected(position);
                }
                break;
            case DialogFragmentImageAddProduct.CHANGE_IMAGE:
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT){
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>(){{add(getString(R.string.error_change_primary_image));}});
                } else {
                    int emptyPicture = 6 - ((AddProductFragment)fragment).countPicture();
                    Log.i(TAG, messageTAG + " max photo will get : " + emptyPicture);
                    GalleryActivity.moveToImageGallery(this, position, emptyPicture);
                }
                break;
            case DialogFragmentImageAddProduct.ADD_DESCRIPTION:
                ((AddProductFragment) fragment).showImageDescDialog(position);
                break;
            case DialogFragmentImageAddProduct.CHANGE_TO_PRIMARY:
                ((AddProductFragment) fragment).setSelectedImageAsPrimary(position);
                break;
            case DialogFragmentImageAddProduct.PRIMARY_IMAGE:
                Log.d(TAG, "image default : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT){
                    ((AddProductFragment) fragment).showImageDescDialog(position);
                } else {
                    ((AddProductFragment) fragment).setSelectedImageAsPrimary(position);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void itemSelected(int index) {
        Fragment fragment = getFragment(productSocMedViewPager.getCurrentItem());
        if(fragment instanceof AddProductFragment){
            ((AddProductFragment)fragment).setProductCatalog(index);
        }
    }

    @Override
    public String getScreenName() {
        return "";
    }

    protected static class PagerAdapter2 extends ArrayFragmentStatePagerAdapter<InstagramMediaModelParc> {

        public PagerAdapter2(FragmentManager fm, List<InstagramMediaModelParc> items) {
            super(fm, items);
        }

        @Override
        public Fragment getFragment(InstagramMediaModelParc item, int position) {
            return AddProductFragment.newInstance3(AddProductType.ADD_FROM_SOCIAL_MEDIA.getType()
                    , convertTo(item), position);
        }
    }

    public void showProgress(boolean isShow) {
        if(isShow){
            tkpdProgressDialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
            tkpdProgressDialog.showDialog();
        }else if(tkpdProgressDialog != null && tkpdProgressDialog.isProgress()){
            tkpdProgressDialog.dismiss();
        }
    }


    public void changePicture(int position, ImageModel imageModel){
        adapter.changePicture(position,imageModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Log.d(TAG, messageTAG + " R.id.home !!!");
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, messageTAG + " android.R.id.home !!!");
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setMessage(getString(com.tokopedia.seller.R.string.dialog_cancel_add_product));

            myAlertDialog.setPositiveButton(getString(R.string.positive_button_dialog), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            myAlertDialog.setNegativeButton(getString(R.string.negative_button_dialog), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            Dialog dialog = myAlertDialog.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
