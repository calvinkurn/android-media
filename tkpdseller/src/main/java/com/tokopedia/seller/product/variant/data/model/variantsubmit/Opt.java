package com.tokopedia.seller.product.variant.data.model.variantsubmit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 8/15/2017.
 */

public class Opt {

    @SerializedName("pvo")
    @Expose
    private int pvo;
    @SerializedName("vuv")
    @Expose
    private int vuv;
    @SerializedName("t_id")
    @Expose
    private int tId;
    @SerializedName("cstm")
    @Expose
    private String cstm;

    /**
     * Product variant option, set with the id got from server if there is any
     * if it is new, set the id as "0"
     * @return Product variant option id from server. 0 if new created
     */
    public int getPvo() {
        return pvo;
    }

    /**
     * Product variant option, set with the id got from server if there is any
     * if it is new, set the id as "0"
     * @param pvo "0" if new, previous id from server if existing
     */
    public void setPvo(int pvo) {
        this.pvo = pvo;
    }

    /**
     * if custom, 0. if not custom, id of the variant unit value
     * @return variant unit value. 0 if custom. id if not custom. example: "1" for "putih"
     */
    public int getVuv() {
        return vuv;
    }

    /**
     * if custom, set to 0
     * if not custom, set with the id of the variant unit value
     * @param vuv 0 if custom, id if not custom. ex: "1" for "putih"
     */
    public void setVuv(int vuv) {
        this.vuv = vuv;
    }

    /**
     * Template id, start from 1, 2, 3, to determine the id when submit product variant
     * @return example: "1", "2", "3"
     */
    public int getTId() {
        return tId;
    }

    /**
     * Template id, start from 1, 2, 3, to determine the id when submit product variant
     * @param tId example: "1", "2", "3"
     */
    public void setTId(int tId) {
        this.tId = tId;
    }

    /**
     * @return example: "Merah kehijauan", "Ukuran 32.5"
     */
    public String getCstm() {
        return cstm;
    }

    /**
     * @param cstm example: "Merah kehijauan", "Ukuran 32.5"
     */
    public void setCstm(String cstm) {
        this.cstm = cstm;
    }

}
