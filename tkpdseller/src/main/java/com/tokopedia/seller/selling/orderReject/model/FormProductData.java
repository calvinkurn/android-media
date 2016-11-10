package com.tokopedia.seller.selling.orderReject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Toped10 on 7/11/2016.
 */
@Parcel
public class FormProductData {

    /**
     * shop_has_terms : 355585
     * product_returnable : 1
     */

    /**
     * product_must_insurance : 1
     * product_short_desc : data datum
     * product_weight_unit : 1
     * product_etalase : Notebook1
     * product_name : BS_021
     * product_currency_id : 1
     * product_weight : 100
     * product_status : 1
     * product_price : 1000
     * product_condition : 1
     * product_min_order : 1
     * product_id : 40671091
     * product_currency : idr
     * product_department_tree : 1
     * product_department_id : 36
     * product_url : https://www.tokopedia.com/qc47/bs021
     * product_etalase_id : 1090552
     */

    @SerializedName("product")
    @Expose
    ProductBean product;

    @SerializedName("shop_is_gold")
    @Expose
    int shop_is_gold;

    public int getShop_is_gold() {
        return shop_is_gold;
    }

    public void setShop_is_gold(int shop_is_gold) {
        this.shop_is_gold = shop_is_gold;
    }

    /**
     * etalase_num_product : 10
     * etalase_name : Notebook1
     * etalase_total_product : 10
     * etalase_id : 1090552
     */



    public ProductBean getProduct() {
        return product;
    }

    public void setProduct(ProductBean product) {
        this.product = product;
    }


    @Parcel
    public static class ProductBean {
        @SerializedName("product_must_insurance")
        @Expose
        String product_must_insurance;

        @SerializedName("product_short_desc")
        @Expose
        String product_short_desc;

        @SerializedName("product_weight_unit")
        @Expose
        String product_weight_unit;

        @SerializedName("product_etalase")
        @Expose
        String product_etalase;

        @SerializedName("product_name")
        @Expose
        String product_name;

        @SerializedName("product_currency_id")
        @Expose
        String product_currency_id;

        @SerializedName("product_weight")
        @Expose
        String product_weight;

        @SerializedName("product_status")
        @Expose
        String product_status;

        @SerializedName("product_price")
        @Expose
        String product_price;

        @SerializedName("product_condition")
        @Expose
        String product_condition;

        @SerializedName("product_min_order")
        @Expose
        String product_min_order;

        @SerializedName("product_id")
        @Expose
        String product_id;

        @SerializedName("product_currency")
        @Expose
        String product_currency;

        @SerializedName("product_department_tree")
        @Expose
        String product_department_tree;

        @SerializedName("product_department_id")
        @Expose
        String product_department_id;

        @SerializedName("product_url")
        @Expose
        String product_url;

        @SerializedName("product_etalase_id")
        @Expose
        String product_etalase_id;

        public String getProduct_must_insurance() {
            return product_must_insurance;
        }

        public void setProduct_must_insurance(String product_must_insurance) {
            this.product_must_insurance = product_must_insurance;
        }

        public String getProduct_short_desc() {
            return product_short_desc;
        }

        public void setProduct_short_desc(String product_short_desc) {
            this.product_short_desc = product_short_desc;
        }

        public String getProduct_weight_unit() {
            return product_weight_unit;
        }

        public void setProduct_weight_unit(String product_weight_unit) {
            this.product_weight_unit = product_weight_unit;
        }

        public String getProduct_etalase() {
            return product_etalase;
        }

        public void setProduct_etalase(String product_etalase) {
            this.product_etalase = product_etalase;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_currency_id() {
            return product_currency_id;
        }

        public void setProduct_currency_id(String product_currency_id) {
            this.product_currency_id = product_currency_id;
        }

        public String getProduct_weight() {
            return product_weight;
        }

        public void setProduct_weight(String product_weight) {
            this.product_weight = product_weight;
        }

        public String getProduct_status() {
            return product_status;
        }

        public void setProduct_status(String product_status) {
            this.product_status = product_status;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_condition() {
            return product_condition;
        }

        public void setProduct_condition(String product_condition) {
            this.product_condition = product_condition;
        }

        public String getProduct_min_order() {
            return product_min_order;
        }

        public void setProduct_min_order(String product_min_order) {
            this.product_min_order = product_min_order;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_currency() {
            return product_currency;
        }

        public void setProduct_currency(String product_currency) {
            this.product_currency = product_currency;
        }

        public String getProduct_department_tree() {
            return product_department_tree;
        }

        public void setProduct_department_tree(String product_department_tree) {
            this.product_department_tree = product_department_tree;
        }

        public String getProduct_department_id() {
            return product_department_id;
        }

        public void setProduct_department_id(String product_department_id) {
            this.product_department_id = product_department_id;
        }

        public String getProduct_url() {
            return product_url;
        }

        public void setProduct_url(String product_url) {
            this.product_url = product_url;
        }

        public String getProduct_etalase_id() {
            return product_etalase_id;
        }

        public void setProduct_etalase_id(String product_etalase_id) {
            this.product_etalase_id = product_etalase_id;
        }
    }
}
