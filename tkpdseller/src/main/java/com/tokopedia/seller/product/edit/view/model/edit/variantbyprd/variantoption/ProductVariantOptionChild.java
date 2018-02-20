package com.tokopedia.seller.product.edit.view.model.edit.variantbyprd.variantoption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;

import java.util.List;


public class ProductVariantOptionChild {
    @SerializedName(value="pvo", alternate={"id"})
    @Expose
    private int id; // id for this variant option

    @SerializedName("vuv")
    @Expose
    private int vuv; //variant option id, ex: 19: Ungu

    @SerializedName("value")
    @Expose
    private int value; //ex: "Ungu"

    @SerializedName("t_id")
    @Expose
    private int tId; // temporary ID for submit

    @SerializedName("cstm")
    @Expose
    private String customName; // for submit only. custom name for variant Option. ex; merah delima

    @SerializedName("image")
    @Expose
    private List<ProductPictureViewModel> productPictureViewModelList;

    // TODO FROM CATALOG
    @SerializedName("hex")
    @Expose
    private String hex; // ex; "#bf00ff"

    @SerializedName("picture")
    @Expose
    private ProductVariantOptionChildOriPicture productVariantOptionChildOriPicture;

}
