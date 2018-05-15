package com.tokopedia.seller.product.edit.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.seller.R;
import com.tokopedia.seller.product.edit.constant.CurrencyTypeDef;
import com.tokopedia.seller.product.edit.view.activity.ProductAddWholesaleActivity;
import com.tokopedia.seller.product.edit.view.adapter.WholesaleAddAdapter;
import com.tokopedia.seller.product.edit.view.model.edit.ProductWholesaleViewModel;
import com.tokopedia.seller.product.edit.view.model.wholesale.WholesaleModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yoshua on 02/05/18.
 */

public class ProductAddWholesaleFragment extends BaseDaggerFragment implements WholesaleAddAdapter.Listener {

    public static final String EXTRA_PRODUCT_WHOLESALE = "EXTRA_PRODUCT_WHOLESALE";
    public static final String SAVE_PRODUCT_WHOLESALE = "SAVE_PRODUCT_WHOLESALE";
    public static final String RUPIAH_CURRENCY = "Rp ";
    public static final String USD_CURRENCY = "US$ ";

    private static final int MAX_WHOLESALE = 5;
    private static final int DEFAULT_QTY_WHOLESALE = 2;
    private static final int DEFAULT_ADD_QTY = 1;
    private static final int DEFAULT_LESS_PRICE_RP = 1;
    private static final double DEFAULT_LESS_PRICE_USD = 0.01;

    private WholesaleAddAdapter wholesaleAdapter;
    private TextView textViewAddWholesale, textMainPrice;
    private Button buttonSave;
    private ArrayList<ProductWholesaleViewModel> productWholesaleViewModelList;
    private ArrayList<ProductWholesaleViewModel> productWholesaleViewModelListTemp;
    private double productPrice;
    private boolean officialStore;
    private boolean hasVariant;
    @CurrencyTypeDef
    private int currencyType;

    public static ProductAddWholesaleFragment newInstance() {
        Bundle args = new Bundle();
        ProductAddWholesaleFragment fragment = new ProductAddWholesaleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Intent activityIntent = getActivity().getIntent();

        productWholesaleViewModelList = activityIntent.getParcelableArrayListExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_WHOLESALE_LIST);
        productWholesaleViewModelListTemp = activityIntent.getParcelableArrayListExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_WHOLESALE_LIST);
        productPrice = activityIntent.getDoubleExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_MAIN_PRICE, 0);
        officialStore = activityIntent.getBooleanExtra(ProductAddWholesaleActivity.EXTRA_OFFICIAL_STORE, false);
        hasVariant = activityIntent.getBooleanExtra(ProductAddWholesaleActivity.EXTRA_HAS_VARIANT, false);
        initCurrency(activityIntent.getIntExtra(ProductAddWholesaleActivity.EXTRA_PRODUCT_CURRENCY, 1));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_add_wholesale, container, false);

        RecyclerView recyclerViewWholesale = root.findViewById(R.id.recycler_view_wholesale);
        recyclerViewWholesale.setLayoutManager(new LinearLayoutManager(recyclerViewWholesale.getContext(), LinearLayoutManager.VERTICAL, false));
        wholesaleAdapter = new WholesaleAddAdapter(productPrice, officialStore);
        wholesaleAdapter.setListener(this);
        wholesaleAdapter.setHasStableIds(true);
        recyclerViewWholesale.setAdapter(wholesaleAdapter);
        recyclerViewWholesale.setNestedScrollingEnabled(false);

        buttonSave =  root.findViewById(R.id.button_save);
        textMainPrice = root.findViewById(R.id.text_main_price);

        textViewAddWholesale = root.findViewById(R.id.text_view_add_wholesale);
        textViewAddWholesale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WholesaleModel lastItem = wholesaleAdapter.getLastItem();
                WholesaleModel newWholesale;
                switch (currencyType) {
                    case CurrencyTypeDef.TYPE_USD:
                        newWholesale = new WholesaleModel(DEFAULT_QTY_WHOLESALE, productPrice - DEFAULT_LESS_PRICE_USD);
                        if(lastItem!=null)
                            newWholesale = new WholesaleModel(lastItem.getQtyMin() + DEFAULT_ADD_QTY, lastItem.getQtyPrice() - DEFAULT_LESS_PRICE_USD);
                        break;
                    default:
                    case CurrencyTypeDef.TYPE_IDR:
                        newWholesale = new WholesaleModel(DEFAULT_QTY_WHOLESALE, productPrice - DEFAULT_LESS_PRICE_RP);
                        if(lastItem!=null)
                            newWholesale = new WholesaleModel(lastItem.getQtyMin() + DEFAULT_ADD_QTY, lastItem.getQtyPrice() - DEFAULT_LESS_PRICE_RP);
                        break;

                }

                wholesaleAdapter.addItem(newWholesale);
                updateWholesaleButton();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasVariant) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                            .setTitle(getString(R.string.dialog_add_wholesale_title))
                            .setMessage(getString(R.string.dialog_add_wholesale_message))
                            .setPositiveButton(getString(R.string.label_add), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    exitWholesaleActivity();
                                }
                            }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    AlertDialog dialog = alertDialogBuilder.create();
                    dialog.show();
                } else{
                    exitWholesaleActivity();
                }
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVE_PRODUCT_WHOLESALE)) {
                productWholesaleViewModelList = savedInstanceState.getParcelableArrayList(SAVE_PRODUCT_WHOLESALE);
            }
        }

        renderData(productWholesaleViewModelList, productPrice);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        productWholesaleViewModelList = wholesaleAdapter.getProductWholesaleViewModels();
        outState.putParcelableArrayList(SAVE_PRODUCT_WHOLESALE, productWholesaleViewModelList);
    }

    public void exitWholesaleActivity(){
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_PRODUCT_WHOLESALE, wholesaleAdapter.getProductWholesaleViewModels());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    public void renderData(ArrayList<ProductWholesaleViewModel> productWholesaleViewModelArrayList, double productPrice){
        String currencyString = CurrencyFormatUtil.convertPriceValue(productPrice, true);
        setWholesalePrice(productWholesaleViewModelArrayList);
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                textMainPrice.setText(USD_CURRENCY + currencyString);
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                textMainPrice.setText(RUPIAH_CURRENCY + currencyString);
                break;

        }
        if (hasVariant){
            final Snackbar snackbar = SnackbarManager.make(getActivity(),
                    getContext().getString(R.string.addproduct_wholesale_notice_variant),
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(getContext().getString(R.string.understand), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        snackbar.show();
                    }
                }, 500);
            } else  {
                snackbar.show();
            }
        }
    }

    @Override
    public int getCurrencyType() {
        return currencyType;
    }

    private void initCurrency(@CurrencyTypeDef int currencyType){
        switch (currencyType) {
            case CurrencyTypeDef.TYPE_USD:
                this.currencyType = CurrencyTypeDef.TYPE_USD;
                break;
            default:
            case CurrencyTypeDef.TYPE_IDR:
                this.currencyType = CurrencyTypeDef.TYPE_IDR;
                break;
        }
    }

    public void setWholesalePrice(List<ProductWholesaleViewModel> wholesalePrice) {
        if(wholesalePrice!=null)
            wholesaleAdapter.addAllWholeSalePrice(wholesalePrice);

        updateWholesaleButton();
    }

    private void updateWholesaleButton() {
        textViewAddWholesale.setVisibility(wholesaleAdapter.getItemCount() < MAX_WHOLESALE ? View.VISIBLE : View.GONE);
    }

    @Override
    public void notifySizeChanged(int currentSize) {
        updateWholesaleButton();
    }

    @Override
    public void setButtonSubmit(boolean state) {
        if(state)
            buttonSave.setEnabled(true);
        else
            buttonSave.setEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_wholesale, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_delete_wholesale) {
            if(hasVariant) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle)
                        .setTitle(getString(R.string.dialog_delete_wholesale_title))
                        .setMessage(getString(R.string.dialog_delete_wholesale_message))
                        .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                wholesaleAdapter.removeAll();
                                wholesaleAdapter.notifyDataSetChanged();
                                notifySizeChanged(wholesaleAdapter.getItemSize());
                                exitWholesaleActivity();
                            }
                        }).setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            } else {
                wholesaleAdapter.removeAll();
                wholesaleAdapter.notifyDataSetChanged();
                notifySizeChanged(wholesaleAdapter.getItemSize());
                exitWholesaleActivity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isAnyWholesaleChange() {
        productWholesaleViewModelList = wholesaleAdapter.getProductWholesaleViewModels();
        if (productWholesaleViewModelListTemp == null) {
            productWholesaleViewModelListTemp = new ArrayList<>();
        }

        boolean state = new Gson().toJson(productWholesaleViewModelList).equalsIgnoreCase(new Gson().toJson(productWholesaleViewModelListTemp));

        return state;
    }
}
