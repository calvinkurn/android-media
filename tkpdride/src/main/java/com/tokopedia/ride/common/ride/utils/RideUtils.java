package com.tokopedia.ride.common.ride.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.activity.RideHomeActivity;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alvarisi on 7/21/17.
 */

public class RideUtils {
    private static final String TIME_SERVER_FORMAT = "dd MMM, yyyy HH:mm";
    private static final String TIME_VIEW_FORMAT = "dd MMM yyyy HH:mm";

    private static final String IND_CURRENCY = "IDR";
    private static final String IND_LOCAL_CURRENCY = "Rp";


    public static String convertTime(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(TIME_SERVER_FORMAT, Locale.US);
        Date dt;
        try {
            dt = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
        SimpleDateFormat outputFormatter = new SimpleDateFormat(TIME_VIEW_FORMAT, Locale.US);
        return outputFormatter.format(dt);
    }

    public static String convertPriceValueToIdrFormat(int price) {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        kursIndonesia.setMaximumFractionDigits(0);
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol(IND_LOCAL_CURRENCY + " ");
        formatRp.setGroupingSeparator('.');
        formatRp.setMonetaryDecimalSeparator('.');
        formatRp.setDecimalSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String result = kursIndonesia.format(price);

        return result.replace(",", ".");
    }


    public static String formatStringToPriceString(String numberString, String currency) {
        try {
            if (currency.equalsIgnoreCase(IND_CURRENCY) || currency.equalsIgnoreCase(IND_LOCAL_CURRENCY)) {
                return RideUtils.convertPriceValueToIdrFormat(Integer.parseInt(numberString));
            } else {
                return formaNumberToPriceString(Float.parseFloat(numberString), currency);
            }
        } catch (Exception ex) {
            return currency + " " + numberString;
        }
    }

    public static String formaNumberToPriceString(float number, String currency) {
        try {
            if (currency.equalsIgnoreCase(IND_LOCAL_CURRENCY)) {
                currency = IND_CURRENCY;
            }

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance(currency));
            String result = "";
            if (currency.equalsIgnoreCase(IND_CURRENCY) || currency.equalsIgnoreCase(IND_LOCAL_CURRENCY)) {
                format.setMaximumFractionDigits(0);
                result = format.format(number).replace(",", ".").replace(IND_CURRENCY, IND_LOCAL_CURRENCY + " ");
            } else {
                result = format.format(number);
            }
            return result;
        } catch (Exception ex) {
            return currency + " " + number;
        }
    }

    public static boolean isUberMoto(String productDisplayName) {
        boolean isMoto = false;
        if (productDisplayName != null) {
            isMoto = productDisplayName.toLowerCase().contains("ubermo");
        }

        return isMoto;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static void addUberShortcutOnLauncher(Context context, String shortLabel, String lonLabel) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // only for gingerbread and newer versions

            ShortcutManager mShortcutManager = context.getSystemService(ShortcutManager.class);

            if (mShortcutManager.isRequestPinShortcutSupported()) {
                ShortcutInfo shortcut = new ShortcutInfo.Builder(context, "uber")
                        .setShortLabel(shortLabel)
                        .setLongLabel(lonLabel)
                        .setIcon(Icon.createWithResource(context, R.drawable.uber_shortcut))
                        .setIntent(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(Constants.Applinks.RIDE)))
                        .build();

                mShortcutManager.requestPinShortcut(shortcut, null);
                Toast.makeText(context, context.getString(R.string.msg_shortcut_created_successfully), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                Intent shortcutIntent = new Intent(context, RideHomeActivity.class);

                Intent addIntent = new Intent();
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortLabel);
                addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(context, R.drawable.uber_shortcut));
                addIntent.putExtra("duplicate", false);
                addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                context.sendBroadcast(addIntent);
                Toast.makeText(context, context.getString(R.string.msg_shortcut_created_successfully), Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context, context.getString(R.string.msg_shortcut_created_failed), Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String getShortAddress(ReverseGeoCodeAddress reverseGeoCodeAddres) {
        if (reverseGeoCodeAddres != null) {
            String completeAddress = reverseGeoCodeAddres.getFormattedAddress();
            if (completeAddress != null && completeAddress.contains(",")) {
                return completeAddress.split(",")[0];
            } else {
                return completeAddress;
            }
        }
        return "";
    }
}
