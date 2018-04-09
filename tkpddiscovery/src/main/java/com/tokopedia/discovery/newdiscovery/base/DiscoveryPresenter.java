package com.tokopedia.discovery.newdiscovery.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    private GetProductUseCase getProductUseCase;
    private GetImageSearchUseCase getImageSearchUseCase;
    private final int MAX_WIDTH = 600;
    private final int MAX_HEIGHT = 600;

    public DiscoveryPresenter(GetProductUseCase getProductUseCase) {
        this.getProductUseCase = getProductUseCase;
    }

    public DiscoveryPresenter(GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        this.getProductUseCase = getProductUseCase;
        this.getImageSearchUseCase = getImageSearchUseCase;
    }

    @Override
    public void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStore) {
        super.requestProduct(searchParameter, forceSearch, requestOfficialStore);
        getProductUseCase.execute(
                GetProductUseCase.createInitializeSearchParam(searchParameter, forceSearch, requestOfficialStore),
                new DefaultSearchSubscriber(searchParameter, forceSearch, getBaseDiscoveryView(), false)
        );
    }


    @Override
    public void requestImageSearchProduct(SearchParameter imageSearchProductParameter) {
        super.requestImageSearchProduct(imageSearchProductParameter);
        getImageSearchUseCase.execute(
                GetImageSearchUseCase.initializeSearchRequestParam(imageSearchProductParameter),
                new DefaultSearchSubscriber(imageSearchProductParameter, false, getBaseDiscoveryView(), true)
        );
    }

    @Override
    public void requestImageSearch(String imagePath) {


        super.requestImageSearch(imagePath);

        File imgFile = new File(imagePath);

        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        myBitmap = ImageHandler.resizeImage(myBitmap, MAX_WIDTH, MAX_HEIGHT);
        try {
            myBitmap = ImageHandler.RotatedBitmap(myBitmap, imagePath);
        } catch (IOException exception) {

        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        getImageSearchUseCase.requestImageSearch(byteArray, new DefaultImageSearchSubscriber(getBaseDiscoveryView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductUseCase.unsubscribe();
    }

}
