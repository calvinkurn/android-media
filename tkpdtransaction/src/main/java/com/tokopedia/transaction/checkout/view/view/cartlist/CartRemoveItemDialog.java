package com.tokopedia.transaction.checkout.view.view.cartlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.cartlist.CartItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aghny A. Putra on 19/02/18.
 */

public class CartRemoveItemDialog extends DialogFragment {
    public static final String DIALOG_FRAGMENT_TAG = CartRemoveItemDialog.class.getSimpleName();

    private static final String DATA_UPDATED = "data_updated";
    private static final String DATA_REMOVED = "data_removed";

    private CartItemRemoveCallbackAction callbackAction;

    public static CartRemoveItemDialog newInstance(List<CartItemData> removedItemDataList,
                                                   List<CartItemData> updatedItemDataList,
                                                   CartItemRemoveCallbackAction callbackAction) {

        CartRemoveItemDialog frag = new CartRemoveItemDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA_REMOVED, (ArrayList<? extends Parcelable>) removedItemDataList);
        args.putParcelableArrayList(DATA_UPDATED, (ArrayList<? extends Parcelable>) updatedItemDataList);
        frag.setArguments(args);
        frag.setCallbackAction(callbackAction);
        return frag;
    }

    private void setCallbackAction(CartItemRemoveCallbackAction callbackAction) {
        this.callbackAction = callbackAction;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<CartItemData> dataRemoved = getArguments().getParcelableArrayList(DATA_REMOVED);
        final List<CartItemData> dataUpdated = getArguments().getParcelableArrayList(DATA_UPDATED);

        final boolean isListNotNull = dataRemoved != null;
        final boolean hasSingleElement = isListNotNull && dataRemoved.size() == 1;

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Hapus Barang")
                .setMessage(getMessage(hasSingleElement, dataRemoved))
                .setPositiveButton("Tambah Wishlist",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isListNotNull) {
                                    if (hasSingleElement) {
                                        callbackAction.onDeleteSingleItemWithWishListClicked(dataRemoved.get(0), dataUpdated);
                                    } else {
                                        callbackAction.onDeleteMultipleItemWithWishListClicked(dataRemoved, dataUpdated);
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
                                    callbackAction.onDeleteSingleItemClicked(dataRemoved.get(0), dataUpdated);
                                } else {
                                    callbackAction.onDeleteMultipleItemClicked(dataRemoved, dataUpdated);
                                }

                                dismiss();
                            }
                        })
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextColor(ContextCompat.getColor(getActivity(), R.color.medium_green));
                btnPositive.setAllCaps(false);

                Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_54));
                btnNegative.setAllCaps(false);
            }
        });

        return dialog;
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

        void onDeleteSingleItemClicked(CartItemData removedCartItem, List<CartItemData> updatedCartItems);

        void onDeleteSingleItemWithWishListClicked(CartItemData removedCartItem, List<CartItemData> updatedCartItems);

        void onDeleteMultipleItemClicked(List<CartItemData> removedCartItems, List<CartItemData> updatedCartItems);

        void onDeleteMultipleItemWithWishListClicked(List<CartItemData> removedCartItems, List<CartItemData> updatedCartItems);

    }

}
