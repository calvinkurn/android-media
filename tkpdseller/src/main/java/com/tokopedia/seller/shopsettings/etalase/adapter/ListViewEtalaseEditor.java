package com.tokopedia.seller.shopsettings.etalase.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.core.R;
import com.tokopedia.core.Router;
import com.tokopedia.core.database.manager.DbManager;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.EtalaseDB;
import com.tokopedia.core.network.apiservices.shop.MyShopEtalaseActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.shop.model.editetalase.Data;
import com.tokopedia.core.shopinfo.models.deleteshopnotes.DeleteShopNote;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListViewEtalaseEditor extends BaseAdapter {
    public static final String STUART = "STUART";
    public static final String SHOP_EDITOR = "Shop Editor";
    private ArrayList<String> EtalaseName = new ArrayList<String>();
    private ArrayList<String> EtalaseID = new ArrayList<String>();
    private ArrayList<Integer> TotalProd = new ArrayList<Integer>();
    public Boolean isMain = false;
    private TkpdProgressDialog progressdialog;
    private String IsAllowShop;

    public Activity context;
    public LayoutInflater inflater;
    private LocalCacheHandler Cache;
    private DbManager dbManager;

    public ListViewEtalaseEditor(Activity context, ArrayList<String> EtalaseName, ArrayList<Integer> TotalProd, ArrayList<String> EtalaseID, String IsAllowShop) {
        super();

        this.context = context;
        this.EtalaseName = EtalaseName;
        this.TotalProd = TotalProd;
        this.EtalaseID = EtalaseID;
        this.IsAllowShop = IsAllowShop;
        this.Cache = new LocalCacheHandler(context, TkpdCache.ETALASE_ADD_PROD);
        //this.Image = pImage;
        dbManager = DbManagerImpl.getInstance();

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return EtalaseName.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder {
        TextView EtalaseNameView;
        TextView TotalProductView;
        ImageView EditButton;
        ImageView DeleteButton;
    }

    public void setAllow(String isAllow) {
        IsAllowShop = isAllow;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null || !isMain) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_etalase_editor, null);

            holder.EtalaseNameView = (TextView) convertView.findViewById(R.id.etalase_name);
            holder.TotalProductView = (TextView) convertView.findViewById(R.id.total_prod);
            holder.EditButton = (ImageView) convertView.findViewById(R.id.edit_but);
            holder.DeleteButton = (ImageView) convertView.findViewById(R.id.delete_but);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TotalProd.get(position) == 0) {
            holder.DeleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.DeleteButton.setVisibility(View.GONE);
        }
        if (IsAllowShop.equals("0")) {
            holder.EditButton.setVisibility(View.GONE);
            holder.DeleteButton.setVisibility(View.GONE);
        }
        holder.EditButton.setTag(position);
        holder.DeleteButton.setTag(position);
        holder.DeleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                ShowDeleteDialog(Integer.parseInt(view.getTag().toString()));
            }

        });

        holder.EtalaseNameView.setText(EtalaseName.get(position));
        holder.TotalProductView.setText(Integer.toString(TotalProd.get(position)));
        holder.EditButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                ShowEditDialog(Integer.parseInt(view.getTag().toString()));
            }

        });

        return convertView;
    }

    private void ShowEditDialog(final int position) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompt_new_talk, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.message_talk);
        userInput.setHint(R.string.title_etalase_name);
        userInput.setText(EtalaseName.get(position));

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.title_edit_prod), null)
                .setNegativeButton(context.getString(R.string.title_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                // TODO Auto-generated method stub
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (userInput.getText().toString().trim().length() == 0)
                            userInput.setError(context.getString(R.string.error_field_required));
                        else if (userInput.getText().toString().trim().length() > 2) {
                            if (!EtalaseName.contains(userInput.getText().toString().trim())) {
                                progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
                                progressdialog.showDialog();
                                EditEtalaseV4(position, userInput.getText().toString().trim(), EtalaseID.get(position));
                                alertDialog.dismiss();
                            } else
                                userInput.setError(context.getString(R.string.error_etalase_exist));
                        } else
                            userInput.setError(context.getString(R.string.error_min_3_character));
                    }
                });
            }
        });
        // show it
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();

    }

    private HashMap<String, String> generateParam(String eName, String eID) {
        HashMap<String, String> param = new HashMap<>();
        param.put("etalase_id", eID);
        param.put("etalase_name", eName);
        return param;
    }

    private void EditEtalaseV4(final int position, final String eName, String eID) {
        new MyShopEtalaseActService().getApi().editEtalase(AuthUtil.generateParams(context, generateParam(eName, eID)))
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {
                                ShopSettingCache.DeleteCache(ShopSettingCache.CODE_ETALASE, context);
                                progressdialog.dismiss();
                                LocalCacheHandler.clearCache(context,
                                        TkpdCache.ETALASE_ADD_PROD);

                            }

                            @Override
                            public void onError(Throwable e) {
                                progressdialog.dismiss();
                                Snackbar snackbarError = SnackbarManager.make(context,
                                        context.getString(R.string.error_connection_problem),
                                        Snackbar.LENGTH_LONG);
                                snackbarError.show();
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                TkpdResponse response = responseData.body();

                                progressdialog.dismiss();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response.getStringData());
                                    Gson gson = new GsonBuilder().create();
                                    Data data = gson.fromJson(jsonObject.toString(), Data.class);
                                    if (data.getIsSuccess() == null && response.getErrorMessages().size() > 0) {
                                        String responses = "";
                                        for (int i = 0; i < response.getErrorMessages().size(); i++) {
                                            responses += response.getErrorMessages().get(i) + " ";
                                        }
                                        Snackbar snackbarError = SnackbarManager.make(context,
                                                responses,
                                                Snackbar.LENGTH_LONG);
                                        snackbarError.show();
                                        return;
                                    } else if (data.getIsSuccess() == 1) {
                                        EtalaseName.set(position, eName);
                                        Toast.makeText(context, R.string.message_success_edit_etalase, Toast.LENGTH_LONG).show();
                                        notifyDataSetChanged();
                                    }
                                } catch (JSONException je) {
                                    je.printStackTrace();
                                }
                            }
                        }
                );
    }

    private void ShowDeleteDialog(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setMessage(R.string.dialog_delete_etalase);

        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton(context.getString(R.string.title_yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                progressdialog = new TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS);
                                progressdialog.showDialog();
                                DeleteEtalase(position);
                            }
                        })
                .setNegativeButton(context.getString(R.string.title_no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    private void DeleteEtalase(final int position) {
        Map<String, String> param = new HashMap<>();
        param.put("etalase_id", EtalaseID.get(position));


        new MyShopEtalaseActService().getApi().deleteEtalase(AuthUtil.generateParams(context, param))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                                DeleteShopNote deleteShopNote = new GsonBuilder().create().fromJson(tkpdResponseResponse.body().getStrResponse(), DeleteShopNote.class);
                                if (deleteShopNote.getMessageError() != null && deleteShopNote.getMessageError().size() <= 0) {
                                    Toast.makeText(context, R.string.message_success_deletet_etalase, Toast.LENGTH_LONG).show();
                                    EtalaseDB etalaseDB = dbManager.getEtalase(EtalaseID.get(position));
                                    if (etalaseDB != null) {
                                        etalaseDB.delete();
                                    }
                                    EtalaseName.remove(position);
                                    EtalaseID.remove(position);
                                    TotalProd.remove(position);
                                    notifyDataSetChanged();
                                    if (EtalaseID.size() == 0)
                                        ((EtalaseShopEditor) context).OnEmpty();

                                    LocalCacheHandler.clearCache(context, TkpdCache.ETALASE_ADD_PROD);
                                    Router.clearEtalase(context);
                                } else {
                                    Toast.makeText(context, deleteShopNote.getMessageError().get(0), Toast.LENGTH_LONG).show();
                                }
                                progressdialog.dismiss();
                            }
                        }
                );

    }

}