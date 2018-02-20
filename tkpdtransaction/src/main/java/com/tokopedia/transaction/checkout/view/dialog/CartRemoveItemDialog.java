package com.tokopedia.transaction.checkout.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.view.data.CartItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 19/02/18.
 */

public class CartRemoveItemDialog extends DialogFragment {

    private static final String DATA = "data";

    private CartItemRemoveCallbackAction callbackAction;

    public static CartRemoveItemDialog newInstance(List<CartItemData> cartItemDataList,
                                                   CartItemRemoveCallbackAction callbackAction) {

        CartRemoveItemDialog frag = new CartRemoveItemDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA, (ArrayList<? extends Parcelable>) cartItemDataList);
        frag.setArguments(args);
        frag.setCallbackAction(callbackAction);
        return frag;
    }

    private void setCallbackAction(CartItemRemoveCallbackAction callbackAction) {
        this.callbackAction = callbackAction;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<CartItemData> cartItemDataList = getArguments().getParcelableArrayList(DATA);
        final boolean isListNotNull = cartItemDataList != null;
        final boolean hasSingleElement = isListNotNull && cartItemDataList.size() == 1;

        return new AlertDialog.Builder(getActivity())
                .setTitle("Hapus Barang")
                .setMessage(getMessage(hasSingleElement, cartItemDataList))
                .setPositiveButton("Tambah Wishlist", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isListNotNull) {
                            if (hasSingleElement) {
                                callbackAction.onDeleteSingleItemWithWishListClicked(cartItemDataList.get(0));
                            } else {
                                callbackAction.onDeleteMultipleItemWithWishListClicked(cartItemDataList);
                            }
                            dismiss();
                        }
                    }
                })
                .setNegativeButton(hasSingleElement ? "Hapus" : "Hapus Semua",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (hasSingleElement) {
                                    callbackAction.onDeleteSingleItemClicked(cartItemDataList.get(0));
                                } else {
                                    callbackAction.onDeleteMultipleItemClicked(cartItemDataList);
                                }
                                dismiss();
                            }
                        })
                .create();
    }

    private String getMessage(boolean isRemoveSingle, List<CartItemData> cartItemDataList) {
        if (isRemoveSingle) {
            CartItemData cartItemData = cartItemDataList.get(0);
            return getString(R.string.delete_single_item_message_dialog,
                    cartItemData.getOriginData().getProductName(),
                    cartItemData.getOriginData().getWeightFormatted());
        }

        return getString(R.string.detele_all_item_message_dialog);
    }

    public interface CartItemRemoveCallbackAction {

        void onDeleteSingleItemClicked(CartItemData cartItemData);

        void onDeleteSingleItemWithWishListClicked(CartItemData cartItemData);

        void onDeleteMultipleItemClicked(List<CartItemData> cartItemDataList);

        void onDeleteMultipleItemWithWishListClicked(List<CartItemData> cartItemDataList);
    }

}
