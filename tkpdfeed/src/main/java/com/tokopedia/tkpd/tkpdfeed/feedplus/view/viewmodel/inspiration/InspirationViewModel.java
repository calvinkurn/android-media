package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenfredian on 5/18/17.
 */

public class InspirationViewModel implements Visitable<FeedPlusTypeFactory> {

    private String title;
    protected ArrayList<InspirationProductViewModel> listProduct;
    private int rowNumber;
    private String source;

    public InspirationViewModel(String title,
                                ArrayList<InspirationProductViewModel> listProduct,
                                String source) {
        this.title = title;
        this.listProduct = listProduct;
        this.source = source;
    }

    @Override
    public int type(FeedPlusTypeFactory favoriteTypeFactory) {
        return favoriteTypeFactory.type(this);
    }

    public ArrayList<InspirationProductViewModel> getListProduct() {
        return listProduct;
    }

    public void setListProduct(ArrayList<InspirationProductViewModel> listProduct) {
        this.listProduct = listProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Object> getListProductAsObjectDataLayer(String eventLabel) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < getListProduct().size(); i++) {
            InspirationProductViewModel viewModel = getListProduct().get(i);
            list.add(
                    DataLayer.mapOf(
                            "name", viewModel.getName(),
                            "id", viewModel.getProductId(),
                            "price", viewModel.getPrice(),
                            "brand", "",
                            "category", "",
                            "variant", "",
                            "list", String.format("feed - product %d - %s", i, eventLabel),
                            "position", i
                    )
            );
        }
        return list;
    }
}
