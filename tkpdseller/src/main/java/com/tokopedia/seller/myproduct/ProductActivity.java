package com.tokopedia.seller.myproduct;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.DownloadResultSender;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.fragment.TwitterDialogV4;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.myproduct.dialog.DialogFragmentImageAddProduct;
import com.tokopedia.seller.myproduct.fragment.AddProductFragment;
import com.tokopedia.seller.myproduct.fragment.ChooserDialogFragment;
import com.tokopedia.seller.myproduct.fragment.ChooserFragment;
import com.tokopedia.seller.myproduct.fragment.ImageChooserDialog;
import com.tokopedia.seller.myproduct.model.SimpleTextModel;
import com.tokopedia.seller.myproduct.presenter.AddProductView;
import com.tokopedia.seller.myproduct.presenter.ProductView;
import com.tokopedia.seller.myproduct.utils.AddProductType;
import com.tokopedia.seller.myproduct.utils.UploadPhotoTask;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.CacheInteractorImpl;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.myproduct.service.ProductService;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tkpd.library.utils.CommonUtils.checkCollectionNotNull;
import static com.tkpd.library.utils.CommonUtils.checkNotNull;

/**
 * Created by sebastianusk on 03/01/2017.
 */
@RuntimePermissions
public class ProductActivity extends BaseProductActivity implements
        ProductView,
        ChooserFragment.OnListFragmentInteractionListener,
        DownloadResultReceiver.Receiver,
        DownloadResultSender,
        TwitterDialogV4.TwitterInterface,
        DialogFragmentImageAddProduct.DFIAListener,
        ImageChooserDialog.SelectWithImage
{

    private static final String TAG = "ProductActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    Toolbar toolbar;
    FrameLayout container;

    String FRAGMENT = "";
    int position;
    String imagePathFromImport;
    String[] multiImagePathFromImport;
    boolean isEdit;
    boolean isCopy;
    boolean isModify;
    String productId;
    private long productDb;

    FragmentManager supportFragmentManager;
    public static final String FRAGMENT_TO_SHOW = "FRAGMENT_TO_SHOW";

    public static final String ADD_PRODUCT_IMAGE_LOCATION = "ADD_PRODUCT_IMAGE_LOCATION";
    public static final int ADD_PRODUCT_IMAGE_LOCATION_DEFAULT = 0;


    // currently supported type
    public static final int ADD_PRODUCT_CATEGORY = 0;
    public static final int ADD_PRODUCT_CHOOSE_ETALASE = 1;


    // fragment productActifity, moved there because it is needed for twitter dialog
    Fragment productActifityFragment = null;
    private String messageTAG = "Product";
    private BroadcastReceiver addProductReceiver;
    private String imagePathCamera;
    private boolean broadcastRegistered = false;

    public static File getOutputMediaFile(){
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "Tokopedia" + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + System.currentTimeMillis()/1000L + ".jpg");
        return mediaFile;
    }


    ImageChooserDialog imageChooserDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchSaveInstanceState(savedInstanceState);
        /* Starting Download Service */
        addProductReceiver = getProductServiceReceiver();
        supportFragmentManager = getSupportFragmentManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }

        setContentView(R.layout.activity_product2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.add_product_container);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ProductActivityPermissionsDispatcher.continueOnCreateWithCheck(this);


    }

    @NonNull
    private BroadcastReceiver getProductServiceReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                int resultCode = bundle.getInt(TkpdState.ProductService.STATUS_FLAG, TkpdState.ProductService.STATUS_ERROR);
                int type = bundle.getInt(TkpdState.ProductService.SERVICE_TYPE, TkpdState.ProductService.INVALID_TYPE);
                Fragment fragment = null;
                switch (type) {
                    case TkpdState.ProductService.EDIT_PRODUCT:
                    case TkpdState.ProductService.ADD_PRODUCT:
                    case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                    case TkpdState.ProductService.DELETE_PRODUCT:
                        fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
                        break;
                    default:
                        throw new UnsupportedOperationException("please pass type when want to process it !!!");
                }

                //check if Fragment implement necessary interface
                if (fragment != null && fragment instanceof BaseView && type != TkpdState.ProductService.INVALID_TYPE) {
                    switch (resultCode) {
                        case TkpdState.ProductService.STATUS_RUNNING:
                            switch (type) {
                                case TkpdState.ProductService.ADD_PRODUCT:
                                    ((BaseView) fragment).setData(type, bundle);
                                    break;
                                case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                                case TkpdState.ProductService.EDIT_PRODUCT:
                                case TkpdState.ProductService.DELETE_PRODUCT:
                                    //[START] show progress bar
                                    if (fragment instanceof AddProductFragment) {
//                                boolean showDialog = resultData.getBoolean(ProductService.ADD_PRODUCT_SHOW_DIALOG, false);
                                        ((AddProductView) fragment).showProgress(true);
                                    }
                                    break;
                            }
                            break;
                        case TkpdState.ProductService.STATUS_DONE:
                            switch (type) {
                                case TkpdState.ProductService.ADD_PRODUCT:
                                    break;
                                case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
                                    ((BaseView) fragment).setData(type, bundle);
                                case TkpdState.ProductService.DELETE_PRODUCT:
                                case TkpdState.ProductService.EDIT_PRODUCT:
                                    CacheInteractor cacheInteractor = new CacheInteractorImpl();
                                    cacheInteractor.deleteProductDetail(bundle.getInt(TkpdState.ProductService.PRODUCT_ID));
                                    ((BaseView) fragment).setData(type, bundle);
                                    break;
                            }
                            break;
                        case TkpdState.ProductService.STATUS_ERROR:
                            switch (bundle.getInt(ProductService.NETWORK_ERROR_FLAG, ProductService.INVALID_NETWORK_ERROR_FLAG)) {
                                case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                                    ((BaseView) fragment).onNetworkError(type, " BAD_REQUEST_NETWORK_ERROR !!!");
                                    break;
                                case NetworkConfig.INTERNAL_SERVER_ERROR:
                                    ((BaseView) fragment).onNetworkError(type, " INTERNAL_SERVER_ERROR !!!");
                                    break;
                                case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                                    ((BaseView) fragment).onNetworkError(type, " FORBIDDEN_NETWORK_ERROR !!!");
                                    break;
                                case ProductService.INVALID_NETWORK_ERROR_FLAG:
                                default:
                                    String messageError = bundle.getString(TkpdState.ProductService.MESSAGE_ERROR_FLAG);
                                    if (!messageError.equals(TkpdState.ProductService.INVALID_MESSAGE_ERROR)) {
                                        ((BaseView) fragment).onMessageError(type, messageError);
                                    }

                            }
                            break;
                    }// end of status download service
                }
            }
        };
    }

    private void getImplicitIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (SessionHandler.isV4Login(this)) {
                if (!SessionHandler.getShopID(this).equals("0")) {
                    if (type.startsWith("image/")) {
                        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        processSingleImage(intent, imageUri);
                    }
                } else {
                    CommonUtils.UniversalToast(getBaseContext(),
                            getString(R.string.title_no_shop));

                    finish();
                }
            } else {
                Intent intentLogin = SessionRouter.getLoginActivityIntent(this);
                intentLogin.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                startActivity(intentLogin);
                finish();
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (SessionHandler.isV4Login(this)) {
                if (!SessionHandler.getShopID(this).equals("0")) {
                    if (type.startsWith("image/")) {
                        ArrayList<Uri> imageUris = intent
                                .getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                        if (checkCollectionNotNull(imageUris)) {
                            processMultipleImage(imageUris);
                        }

                        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                        processSingleImage(intent, imageUri);
                    }
                } else {
                    finish();
                    CommonUtils.UniversalToast(getBaseContext(),
                            getString(R.string.title_no_shop));
                }

            } else {
                Intent intentLogin = SessionRouter.getLoginActivityIntent(this);
                intentLogin.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
                startActivity(intentLogin);
                finish();
            }
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    private void processMultipleImage(ArrayList<Uri> imageUris) {
        int imagescount = (imageUris.size() > 5) ? 5 : imageUris.size();
        multiImagePathFromImport = new String[imagescount];
        for (int i = 0; i < imagescount; i++) {
            Uri imageUri = imageUris.get(i);

            FRAGMENT = AddProductFragment.FRAGMENT_TAG;


            if (imageUri.toString().startsWith("content://gmail-ls/")) {// get email attachment from gmail
                multiImagePathFromImport[i] = getPathFromGmail(imageUri);
            } else { // get extras for import from gallery
                multiImagePathFromImport[i] = getRealPathFromURI(this, imageUri);
            }
            Log.d(TAG, messageTAG + " [" + multiImagePathFromImport[i] + "]");
        }
    }

    private void processSingleImage(Intent intent, Uri imageUri) {
        if (imageUri != null) {
            FRAGMENT = AddProductFragment.FRAGMENT_TAG;

            position = intent.getExtras().getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);

            if (imageUri.toString().startsWith("content://gmail-ls/")) {
                imagePathFromImport = getPathFromGmail(imageUri);
            } else {
                // get extras for import from gallery
                imagePathFromImport = getRealPathFromURI(this, imageUri);
            }
            Log.d(TAG, messageTAG + " [" + imagePathFromImport + "]");
        }
    }

    public static String getPath(Context context, Uri contentUri) {

        String res = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } else {
            Log.d(TAG, "Cursor is null");
            return contentUri.getPath();
        }
        return res;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return getPath(inContext, Uri.parse(path));
    }

    public String getPathFromGmail(Uri contentUri) {
        File attach = null;
        try {
            InputStream attachment = getContentResolver().openInputStream(contentUri);
            attach = UploadPhotoTask.writeImageToTkpdPath(attachment);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return attach.getAbsolutePath();
    }

    @Override
    public void WarningDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.dialog_no_memory_card));
        myAlertDialog.setPositiveButton(getString(R.string.title_ok), null);

        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    public void showTwitterDialog() {
        // Create and show the dialog.
        TwitterDialogV4 newFragment = new TwitterDialogV4();
        newFragment.show(supportFragmentManager, TWITTER_DIALOG_V_4);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (supportFragmentManager.findFragmentById(R.id.add_product_container) == null)
            initFragment(FRAGMENT);

        registerReceiver(addProductReceiver, new IntentFilter(TkpdState.ProductService.BROADCAST_ADD_PRODUCT));
        broadcastRegistered = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(broadcastRegistered) {
            unregisterReceiver(addProductReceiver);
        }
    }

    public void fetchSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            FRAGMENT = bundle.getString(FRAGMENT_TO_SHOW);
            position = bundle.getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
        }
    }

    public void fetchExtras(Intent intent) {
        if (intent != null) {
            // set which fragment should be created
            String fragment = intent.getExtras().getString(FRAGMENT_TO_SHOW);
            if (fragment != null) {
                switch (fragment) {
                    case AddProductFragment.FRAGMENT_TAG:
                        FRAGMENT = fragment;
                        break;
                }
            } else {
                FRAGMENT = AddProductFragment.FRAGMENT_TAG;
            }

            position = intent.getExtras().getInt(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);

            // get extras for import from gallery
            if (!checkNotNull(imagePathFromImport))
                imagePathFromImport = intent.getExtras().getString(ManageProduct.IMAGE_GALLERY);

            ArrayList<String> imagePaths = Parcels.unwrap(intent.getExtras().getParcelable(GalleryBrowser.IMAGE_URLS));
            if (checkNotNull(imagePaths) && position == -1) {
                multiImagePathFromImport = new String[imagePaths.size()];
                int count = 0;
                for (String imagePath : imagePaths) {
                    multiImagePathFromImport[count] = imagePath;
                    count++;
                }
            }

            isEdit = intent.getExtras().getBoolean("is_edit", false);
            isCopy = intent.getExtras().getBoolean("is_copy", false);
            isModify = intent.getExtras().getBoolean("is_modify", false);
            productId = intent.getExtras().getString("product_id", "XXX");
            productDb = intent.getExtras().getLong("product_db", -1);

            int notificationId = intent.getIntExtra(ProductService.NOTIFICATION_ID,-1);
            if (notificationId != -1){
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancel(notificationId);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(FRAGMENT_TO_SHOW, FRAGMENT);
        outState.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_TO_SHOW, FRAGMENT);
        outState.putInt(ADD_PRODUCT_IMAGE_LOCATION, position);
    }

    public void initFragment(String FRAGMENT_TAG) {

        switch (FRAGMENT_TAG) {
            case AddProductFragment.FRAGMENT_TAG:
                if (supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG) == null) {
                    if (imagePathFromImport != null && position != -1) {
                        Log.d(TAG, messageTAG + " tambahkan dari gallery [" + imagePathFromImport + "] position [" + position + "]");
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.ADD_FROM_GALLERY.getType(), imagePathFromImport, position);
                    } else if (multiImagePathFromImport != null) {
                        Log.d(TAG, messageTAG + " tambahkan multiple dari gallery [" + multiImagePathFromImport + "]");
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.ADD_MULTIPLE_FROM_GALERY.getType(), multiImagePathFromImport);
                    } else if (isEdit && !productId.equals("XXX")) {
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.EDIT.getType(), productId);
                    } else if (isCopy && !productId.equals("XXX")) {
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.COPY.getType(), productId);
                    }else if(isModify && productDb != -1) {
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.MODIFY.getType(), productDb);
                    }else {
                        // test for add first time
                        productActifityFragment = AddProductFragment.newInstance(AddProductType.ADD.getType());
                    }
                    // test for add using social media
//                    fragment = AddProductFragment.newInstance(AddProductType.ADD_FROM_SOCIAL_MEDIA.getType());
                    moveToFragment(productActifityFragment, true, AddProductFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + AddProductFragment.FRAGMENT_TAG + " is already created");
                }
                break;
            case AddProductFragment.FRAGMENT_EDIT_TAG:
                throw new RuntimeException("not implemented yet");
        }
    }

    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.add_product_container, fragment, TAG);
        if (isAddtoBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Log.d(TAG, messageTAG + " R.id.home !!!");
            onBackPressed();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, messageTAG + " android.R.id.home !!!");
            showExitDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void moveToAddProduct(Context context) {
        if (!checkNotNull(context))
            return;

        Intent intent = new Intent(context, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void showPopup(int type, String title, List<SimpleTextModel> simpleTextModels) {
        showPopup(getSupportFragmentManager(), type, title, simpleTextModels);
    }

    public static void moveToProductShare(ShareData shareData, Context context) {

        context.startActivity(ProductInfoActivity.createInstance(context, shareData));

        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).finish();
        }
    }

    public static void moveToProductShare(Context context) {

        context.startActivity(ProductInfoActivity.createInstance(context));

        if (context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).finish();
        }
    }

    public static void showPopup(FragmentManager fm, int type
            , String title, List<SimpleTextModel> simpleTextModels) {
        DialogFragment dialogFragment;
        switch (type) {
            case ADD_PRODUCT_CATEGORY:
            case ADD_PRODUCT_CHOOSE_ETALASE:
                dialogFragment = ChooserDialogFragment.newInstance(type, title, simpleTextModels);
                dialogFragment.show(fm, ChooserDialogFragment.FRAGMENT_TAG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY && data != null) {
            int position = data.getIntExtra(ADD_PRODUCT_IMAGE_LOCATION, ADD_PRODUCT_IMAGE_LOCATION_DEFAULT);
            String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
            Log.d(TAG, messageTAG + imageUrl + " & " + position);
            Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
            if (fragment != null && fragment instanceof AddProductFragment && checkNotNull(imageUrl)) {
                ((AddProductFragment) fragment).addImageAfterSelect(imageUrl, position);
            }
            ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryBrowser.IMAGE_URLS);
            if (fragment != null && fragment instanceof AddProductFragment && checkCollectionNotNull(imageUrls)) {
                ((AddProductFragment) fragment).addImageAfterSelect(imageUrls.get(0), position);
                imageUrls.remove(0);
                ((AddProductFragment) fragment).addImageAfterSelect(imageUrls);
            }
        }

    }

    public static Intent moveToEditFragment(Context context, boolean isEdit, String productId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_edit", true);
        bundle.putString("product_id", productId);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        Intent addProduct = new Intent(context, ProductActivity.class);
        addProduct.putExtras(bundle);
        return addProduct;
    }

    public static Intent moveToCopyFragment(Context context, boolean isCopy, String productId) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_copy", true);
        bundle.putString("product_id", productId);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        Intent addProduct = new Intent(context, ProductActivity.class);
        addProduct.putExtras(bundle);
        return addProduct;
    }

    public static Intent moveToModifyProduct(Context context, long productDb){
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_modify", true);
        bundle.putLong("product_db", productDb);
        bundle.putString(ProductActivity.FRAGMENT_TO_SHOW, AddProductFragment.FRAGMENT_TAG);
        Intent addProduct = new Intent(context, ProductActivity.class);
        addProduct.putExtras(bundle);
        return addProduct;
    }

    @Override
    public void onListFragmentInteraction(SimpleTextModel item) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        Fragment dialog = supportFragmentManager.findFragmentByTag(ChooserDialogFragment.FRAGMENT_TAG);
        switch (((ChooserDialogFragment) dialog).getType()) {
            case ADD_PRODUCT_CATEGORY:
                if (fragment != null && fragment instanceof AddProductFragment) {
                    //[START] This is old using json reading
//                    ((AddProductFragment) fragment).addCategoryAfterSelect(item);
                    //[END] This is old using json reading

                    ((AddProductFragment) fragment).addCategoryAfterSelectV2(item);
                }
                break;
            case ADD_PRODUCT_CHOOSE_ETALASE:
                if (fragment != null && fragment instanceof AddProductFragment) {
                    ((AddProductFragment) fragment).addEtalaseAfterSelect(item);
                }
                break;
        }
        if (dialog != null) {
            ((ChooserDialogFragment) dialog).dismiss();
        }

    }

    @Override
    public void onLongClick() {
        Log.e(TAG, "onLongClick not implemented yet");
//        throw new RuntimeException("not implemented yet");
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    @Override
    public void sendDataToInternet(int type, Bundle data) {
        switch (type) {
            case TkpdState.ProductService.EDIT_PRODUCT:
            case TkpdState.ProductService.ADD_PRODUCT:
            case TkpdState.ProductService.ADD_PRODUCT_WITHOUT_IMAGE:
            case TkpdState.ProductService.DELETE_PRODUCT:
                ProductService.startDownload(this, data, type);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    @Override
    public void ChangeUI() {
        if (supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG) != null) {
            ((AddProductFragment) supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG)).onLoginTwitter();
//            AddProductFragment.AddProductShare.authorizeTwitter();
        }
    }

    @Override
    public void editImage(int action, int position, int fragmentPosition, boolean isPrimary) {
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        switch (action) {
            case DialogFragmentImageAddProduct.DELETE_IMAGE:
                Log.d(TAG, "image delete : " + position);
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT) {
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>() {{
                        add(getString(R.string.error_delete_primary_image));
                    }});
                } else {
                    ((AddProductFragment) fragment).removeImageSelected(position);
                }
                break;
            case DialogFragmentImageAddProduct.CHANGE_IMAGE:
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT) {
                    ((AddProductFragment) fragment).showMessageError(new ArrayList<String>() {{
                        add(getString(R.string.error_change_primary_image));
                    }});
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
                if (isPrimary && ((AddProductFragment) fragment).addProductType == AddProductType.EDIT) {
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
        Fragment fragment = supportFragmentManager.findFragmentByTag(AddProductFragment.FRAGMENT_TAG);
        if(fragment instanceof AddProductFragment){
            ((AddProductFragment)fragment).setProductCatalog(index);
        }
    }

    @Override
    public String getScreenName() {
        return "";
    }

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() != 0) {
            //TODO: Perform your logic to pass back press here
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnBackPressedListener) {
                    boolean canGoBack = ((OnBackPressedListener) fragment).onBackPressed();
                    if (!canGoBack) {
                        super.onBackPressed();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void continueOnCreate() {
        getImplicitIntent();
        if (this.isFinishing()) {
            return;
        }
        fetchExtras(getIntent());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(this,Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(this,Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(this,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(this,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(this,listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(this,listPermission);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            showExitDialog();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setMessage(getString(R.string.dialog_cancel_add_product));

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
    }
}
