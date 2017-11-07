package com.tokopedia.inbox.rescenter.detailv2.data.pojo.detailrescenter.v2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yfsx on 07/11/17.
 */
public class LastResponse {

    @SerializedName("sellerAddressResponse")
    private SellerAddressResponse sellerAddressResponse;
    @SerializedName("userAwb")
    private UserAwbResponse userAwb;
    @SerializedName("solution")
    private SolutionBean solution;
    @SerializedName("problem")
    private String problem;
    @SerializedName("status")
    private String status;
    @SerializedName("complainedProducts")
    private List<ComplainedProductsBean> complainedProducts;

    public SellerAddressResponse getSellerAddressResponse() {
        return sellerAddressResponse;
    }

    public void setSellerAddressResponse(SellerAddressResponse sellerAddressResponse) {
        this.sellerAddressResponse = sellerAddressResponse;
    }

    public UserAwbResponse getUserAwb() {
        return userAwb;
    }

    public void setUserAwb(UserAwbResponse userAwb) {
        this.userAwb = userAwb;
    }

    public SolutionBean getSolution() {
        return solution;
    }

    public void setSolution(SolutionBean solution) {
        this.solution = solution;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ComplainedProductsBean> getComplainedProducts() {
        return complainedProducts;
    }

    public void setComplainedProducts(List<ComplainedProductsBean> complainedProducts) {
        this.complainedProducts = complainedProducts;
    }

    public static class SolutionBean {
        /**
         * id : 3
         * name : Retur produk dan kembalikan dana
         * nameCustom :
         * actionBy : 1
         * amount : {"idr":"Rp. 10.000","integer":10000}
         * createTime : 2017-08-10T14:29:30.900425Z
         * createTimeStr : 2017-08-10 14:29:30
         */

        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("nameCustom")
        private String nameCustom;
        @SerializedName("actionBy")
        private int actionBy;
        @SerializedName("amount")
        private SolutionBean.AmountBean amount;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("createTimeStr")
        private String createTimeStr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNameCustom() {
            return nameCustom;
        }

        public void setNameCustom(String nameCustom) {
            this.nameCustom = nameCustom;
        }

        public int getActionBy() {
            return actionBy;
        }

        public void setActionBy(int actionBy) {
            this.actionBy = actionBy;
        }

        public SolutionBean.AmountBean getAmount() {
            return amount;
        }

        public void setAmount(SolutionBean.AmountBean amount) {
            this.amount = amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCreateTimeStr() {
            return createTimeStr;
        }

        public void setCreateTimeStr(String createTimeStr) {
            this.createTimeStr = createTimeStr;
        }

        public static class AmountBean {
            /**
             * idr : Rp. 10.000
             * integer : 10000
             */

            @SerializedName("idr")
            private String idr;
            @SerializedName("integer")
            private int integer;

            public String getIdr() {
                return idr;
            }

            public void setIdr(String idr) {
                this.idr = idr;
            }

            public int getInteger() {
                return integer;
            }

            public void setInteger(int integer) {
                this.integer = integer;
            }
        }
    }

    public static class ComplainedProductsBean {
        /**
         * id : 2419
         * count : 1
         * product : {"name":"Product B","thumb":"https://imagerouter-staging.tokopedia.com/image/v1/p/15126662/product_m_thumbnail/desktop","variant":"","amount":{"idr":"Rp. 1.000","integer":1000},"quantity":0}
         * trouble : {"id":1,"name":"Tidak sesuai deskripsi"}
         */

        @SerializedName("id")
        private int id;
        @SerializedName("count")
        private int count;
        @SerializedName("product")
        private ComplainedProductsBean.ProductBean product;
        @SerializedName("trouble")
        private ComplainedProductsBean.TroubleBean trouble;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ComplainedProductsBean.ProductBean getProduct() {
            return product;
        }

        public void setProduct(ComplainedProductsBean.ProductBean product) {
            this.product = product;
        }

        public ComplainedProductsBean.TroubleBean getTrouble() {
            return trouble;
        }

        public void setTrouble(ComplainedProductsBean.TroubleBean trouble) {
            this.trouble = trouble;
        }

        public static class ProductBean {
            /**
             * name : Product B
             * thumb : https://imagerouter-staging.tokopedia.com/image/v1/p/15126662/product_m_thumbnail/desktop
             * variant :
             * amount : {"idr":"Rp. 1.000","integer":1000}
             * quantity : 0
             */

            @SerializedName("name")
            private String name;
            @SerializedName("thumb")
            private String thumb;
            @SerializedName("variant")
            private String variant;
            @SerializedName("amount")
            private ComplainedProductsBean.ProductBean.AmountBeanX amount;
            @SerializedName("quantity")
            private int quantity;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getVariant() {
                return variant;
            }

            public void setVariant(String variant) {
                this.variant = variant;
            }

            public ComplainedProductsBean.ProductBean.AmountBeanX getAmount() {
                return amount;
            }

            public void setAmount(ComplainedProductsBean.ProductBean.AmountBeanX amount) {
                this.amount = amount;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public static class AmountBeanX {
                /**
                 * idr : Rp. 1.000
                 * integer : 1000
                 */

                @SerializedName("idr")
                private String idr;
                @SerializedName("integer")
                private int integer;

                public String getIdr() {
                    return idr;
                }

                public void setIdr(String idr) {
                    this.idr = idr;
                }

                public int getInteger() {
                    return integer;
                }

                public void setInteger(int integer) {
                    this.integer = integer;
                }
            }
        }

        public static class TroubleBean {
            /**
             * id : 1
             * name : Tidak sesuai deskripsi
             */

            @SerializedName("id")
            private int id;
            @SerializedName("name")
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
