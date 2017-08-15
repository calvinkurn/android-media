package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.ProductProblem;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.AmountViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.OrderDetailViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.OrderProductViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.OrderViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ProductProblemViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ShippingDetailViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.ShippingViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusInfoViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusTroubleViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.productproblem.StatusViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 14/08/17.
 */

public class ProductProblemFragmentPresenter extends BaseDaggerPresenter<ProductProblem.View> implements ProductProblem.Presenter {
    private ProductProblem.View mainView;

    @Inject
    public ProductProblemFragmentPresenter() {
    }

    @Override
    public void attachView(ProductProblem.View view) {
        this.mainView = view;
        super.attachView(view);
    }

    @Override
    public void populateProductProblem() {
        String jsonArrayStringTemp = "[{\"problem\":{\"type\":1,\"name\":\"Kendala Ongkos Kirim\"},\"status\":[{\"trouble\":[{\"id\":4,\"name\":\"Kendala ongkos kirim\"}]}]},{\"problem\":{\"type\":2,\"name\":\"Kuroko Actio Figure\"},\"order\":{\"detail\":{\"id\":81388,\"returnable\":0},\"product\":{\"name\":\"Kuroko Actio Figure\",\"thumb\":\"http://devel-go.tkpd:3001/image/v1/p/85685/product_m_thumbnail/desktop\",\"variant\":\"Kuroko No Basketball\",\"amount\":{\"idr\":\"Rp. 25.000\",\"integer\":25000},\"quantity\":1},\"shipping\":{\"id\":1,\"name\":\"JNE\",\"detail\":{\"id\":1,\"name\":\"Reguler\"}}},\"status\":[{\"delivered\":true,\"name\":\"Sudah Sampai\",\"trouble\":[{\"id\":1,\"name\":\"Tidak sesuai deskripsi\"},{\"id\":2,\"name\":\"Rusak\"},{\"id\":3,\"name\":\"Tidak lengkap\"},{\"id\":8,\"name\":\"Lainnya\"}]},{\"delivered\":false,\"name\":\"Belum Sampai\",\"trouble\":[{\"id\":5,\"name\":\"Produk belum diterima\"}],\"info\":{\"show\":false,\"date\":\"2017-08-01 09:53:07\"}}]},{\"problem\":{\"type\":2,\"name\":\"Kagami Action Figure\"},\"order\":{\"detail\":{\"id\":81387,\"returnable\":3},\"product\":{\"name\":\"Kagami Action Figure\",\"thumb\":\"http://devel-go.tkpd:3001/image/v1/p/26387919/product_m_thumbnail/desktop\",\"variant\":\"Kagami Action Figure Ready Stock\",\"amount\":{\"idr\":\"Rp. 25.000\",\"integer\":25000},\"quantity\":1},\"shipping\":{\"id\":1,\"name\":\"JNE\",\"detail\":{\"id\":1,\"name\":\"Reguler\"}}},\"status\":[{\"delivered\":true,\"name\":\"Sudah Sampai\",\"trouble\":[{\"id\":1,\"name\":\"Tidak sesuai deskripsi\"},{\"id\":2,\"name\":\"Rusak\"},{\"id\":3,\"name\":\"Tidak lengkap\"},{\"id\":7,\"name\":\"Berubah pikiran\"},{\"id\":8,\"name\":\"Lainnya\"}]},{\"delivered\":false,\"name\":\"Belum Sampai\",\"trouble\":[{\"id\":5,\"name\":\"Produk belum diterima\"}],\"info\":{\"show\":false,\"date\":\"2017-08-01 09:53:07\"}}]}]";
        JSONArray jsonArrayTemp = null;
        try {
            jsonArrayTemp = new JSONArray(jsonArrayStringTemp);
            List<ProductProblemViewModel> productProblemList = new ArrayList<>();
            for (int i = 0; i < jsonArrayTemp.length(); i++) {
                JSONObject objectTemp = jsonArrayTemp.getJSONObject(i);
                ProductProblemViewModel productProblem = new ProductProblemViewModel();
                if (objectTemp.has("problem")) {
                    ProblemViewModel problemViewModel = new ProblemViewModel();
                    JSONObject problemObject = objectTemp.getJSONObject("problem");
                    problemViewModel.setName(problemObject.getString("name"));
                    problemViewModel.setType(problemObject.getInt("type"));
                    productProblem.setProblem(problemViewModel);
                }
                if (objectTemp.has("order")) {
                    OrderViewModel orderViewModel = new OrderViewModel();
                    JSONObject orderObject = objectTemp.getJSONObject("order");
                    if (orderObject.has("detail")) {
                        OrderDetailViewModel orderDetailViewModel = new OrderDetailViewModel();
                        JSONObject orderDetailObject = orderObject.getJSONObject("detail");
                        orderDetailViewModel.setId(orderDetailObject.getInt("id"));
                        orderDetailViewModel.setReturnable(orderDetailObject.getInt("returnable"));
                        orderViewModel.setDetail(orderDetailViewModel);
                    }
                    if (orderObject.has("product")) {
                        OrderProductViewModel orderProductViewModel = new OrderProductViewModel();
                        JSONObject orderProductObject = orderObject.getJSONObject("product");
                        orderProductViewModel.setName(orderProductObject.getString("name"));
                        orderProductViewModel.setQuantity(orderProductObject.getInt("quantity"));
                        orderProductViewModel.setThumb(orderProductObject.getString("thumb"));
                        orderProductViewModel.setVariant(orderProductObject.getString("variant"));
                        if (orderProductObject.has("amount")) {
                            AmountViewModel amountViewModel = new AmountViewModel();
                            JSONObject amountObject = orderProductObject.getJSONObject("amount");
                            amountViewModel.setIdr(amountObject.getString("idr"));
                            amountViewModel.setInteger(amountObject.getInt("integer"));
                            orderProductViewModel.setAmount(amountViewModel);
                        }
                        orderViewModel.setProduct(orderProductViewModel);
                    }
                    if (orderObject.has("shipping")) {
                        ShippingViewModel shippingViewModel = new ShippingViewModel();
                        JSONObject shippingObject = orderObject.getJSONObject("shipping");
                        shippingViewModel.setId(shippingObject.getInt("id"));
                        shippingViewModel.setName(shippingObject.getString("name"));
                        if (shippingObject.has("detail")) {
                            ShippingDetailViewModel shippingDetailViewModel = new ShippingDetailViewModel();
                            JSONObject shippingDetailObject = shippingObject.getJSONObject("detail");
                            shippingDetailViewModel.setName(shippingDetailObject.getString("name"));
                            shippingDetailViewModel.setId(shippingDetailObject.getInt("id"));
                            shippingViewModel.setDetail(shippingDetailViewModel);
                        }
                        orderViewModel.setShipping(shippingViewModel);
                    }
                    productProblem.setOrder(orderViewModel);
                }
                if (objectTemp.has("status")) {
                    List<StatusViewModel> statusViewModelList = new ArrayList<>();
                    JSONArray statusJSONArray = objectTemp.getJSONArray("status");
                    for (int j = 0; j < statusJSONArray.length() ; j++) {
                        StatusViewModel statusViewModel = new StatusViewModel();
                        JSONObject statusObject = statusJSONArray.getJSONObject(j);
                        if (statusObject.has("delivered")) {
                            statusViewModel.setDelivered(statusObject.getBoolean("delivered"));
                        }
                        if (statusObject.has("name")) {
                            statusViewModel.setName(statusObject.getString("name"));
                        }
                        if (statusObject.has("info")) {
                            StatusInfoViewModel statusInfoViewModel = new StatusInfoViewModel();
                            JSONObject statusInfoObject = statusObject.getJSONObject("info");
                            statusInfoViewModel.setDate(statusInfoObject.getString("date"));
                            statusInfoViewModel.setShow(statusInfoObject.getBoolean("show"));
                            statusViewModel.setInfo(statusInfoViewModel);
                        }
                        if (statusObject.has("trouble")) {
                            List<StatusTroubleViewModel> statusTroubleViewModelList = new ArrayList<>();
                            JSONArray statusTroubleJSONArray = statusObject.getJSONArray("trouble");
                            for (int k = 0; k < statusTroubleJSONArray.length(); k++) {
                                StatusTroubleViewModel statusTroubleViewModel = new StatusTroubleViewModel();
                                JSONObject statusTroubleObject = statusTroubleJSONArray.getJSONObject(k);
                                statusTroubleViewModel.setId(statusTroubleObject.getInt("id"));
                                statusTroubleViewModel.setName(statusTroubleObject.getString("name"));
                                statusTroubleViewModelList.add(statusTroubleViewModel);
                            }
                            statusViewModel.setTrouble(statusTroubleViewModelList);
                        }
                        statusViewModelList.add(statusViewModel);
                    }
                    productProblem.setStatusList(statusViewModelList);
                }
                productProblemList.add(productProblem);
            }
            mainView.populateProblemAndProduct(productProblemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
