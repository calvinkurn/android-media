package com.tokopedia.events.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.tokopedia.events.R;
import com.tokopedia.events.domain.model.EventsCategoryDomain;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;
import com.tokopedia.events.view.viewmodel.SearchViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by pranaymohapatra on 10/01/18.
 */

public class Utils {
    private static Utils singleInstance;

    synchronized public static Utils getSingletonInstance() {
        if (singleInstance == null)
            singleInstance = new Utils();
        return singleInstance;
    }

    private Utils() {
        Log.d("UTILS", "Utils Instance created");
    }

    public List<CategoryViewModel> convertIntoCategoryListVeiwModel(List<EventsCategoryDomain> categoryList) {
        List<CategoryViewModel> categoryViewModels = new ArrayList<>();
        if (categoryList != null) {
            for (EventsCategoryDomain eventsCategoryDomain : categoryList
                    ) {
                if ("top".equalsIgnoreCase(eventsCategoryDomain.getName())) {
                    categoryViewModels.add(0, new CategoryViewModel(eventsCategoryDomain.getTitle(),
                            eventsCategoryDomain.getName(),
                            convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

                } else {
                    categoryViewModels.add(new CategoryViewModel(eventsCategoryDomain.getTitle(),
                            eventsCategoryDomain.getName(),
                            convertIntoCategoryListItemsVeiwModel(eventsCategoryDomain.getItems())));

                }

            }
        }
        return categoryViewModels;
    }

    public List<CategoryItemsViewModel> convertIntoCategoryListItemsVeiwModel(List<EventsItemDomain> categoryResponseItemsList) {
        List<CategoryItemsViewModel> categoryItemsViewModelList = new ArrayList<>();
        if (categoryResponseItemsList != null) {
            CategoryItemsViewModel CategoryItemsViewModel;
            for (EventsItemDomain categoryEntity : categoryResponseItemsList) {
                CategoryItemsViewModel = new CategoryItemsViewModel();
                CategoryItemsViewModel.setId(categoryEntity.getId());
                CategoryItemsViewModel.setCategoryId(categoryEntity.getCategoryId());
                CategoryItemsViewModel.setDisplayName(categoryEntity.getDisplayName());
                CategoryItemsViewModel.setTitle(categoryEntity.getTitle());
                CategoryItemsViewModel.setImageApp(categoryEntity.getImageApp());
                CategoryItemsViewModel.setThumbnailApp(categoryEntity.getThumbnailApp());
                CategoryItemsViewModel.setSalesPrice(categoryEntity.getSalesPrice());
                CategoryItemsViewModel.setMinStartTime(categoryEntity.getMinStartTime());
                CategoryItemsViewModel.setCityName(categoryEntity.getCityName());
                CategoryItemsViewModel.setMinStartDate(categoryEntity.getMinStartDate());
                CategoryItemsViewModel.setMaxEndDate(categoryEntity.getMaxEndDate());
                CategoryItemsViewModel.setLongRichDesc(categoryEntity.getLongRichDesc());
                CategoryItemsViewModel.setDisplayTags(categoryEntity.getDisplayTags());
                CategoryItemsViewModel.setTnc(categoryEntity.getTnc());
                CategoryItemsViewModel.setIsTop(categoryEntity.getIsTop());
                CategoryItemsViewModel.setHasSeatLayout(categoryEntity.getHasSeatLayout());
                CategoryItemsViewModel.setUrl(categoryEntity.getUrl());
                categoryItemsViewModelList.add(CategoryItemsViewModel);
            }
        }
        return categoryItemsViewModelList;
    }

    public ArrayList<SearchViewModel> convertIntoSearchViewModel(List<CategoryViewModel> source) {
        ArrayList<SearchViewModel> searchViewModels = new ArrayList<>();
        if (source != null) {
            SearchViewModel searchModelItem;
            for (CategoryViewModel item : source) {
                List<CategoryItemsViewModel> sourceModels = item.getItems();
                for (CategoryItemsViewModel sourceItem : sourceModels) {
                    if (sourceItem.getIsTop() == 1) {
                        searchModelItem = new SearchViewModel();
                        searchModelItem.setCityName(sourceItem.getCityName());
                        searchModelItem.setDisplayName(sourceItem.getDisplayName());
                        searchModelItem.setImageApp(sourceItem.getImageApp());
                        searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                        searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                        searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                        searchModelItem.setTitle(sourceItem.getTitle());
                        searchModelItem.setUrl(sourceItem.getUrl());
                        searchViewModels.add(searchModelItem);
                    }
                }

            }
        }
        return searchViewModels;
    }

    public List<SearchViewModel> convertSearchResultsToModel(List<CategoryItemsViewModel> categoryItemsViewModels) {
        List<SearchViewModel> searchResults = null;
        if (categoryItemsViewModels != null && !categoryItemsViewModels.isEmpty()) {
            SearchViewModel searchModelItem;
            searchResults = new ArrayList<>();
            for (CategoryItemsViewModel sourceItem : categoryItemsViewModels) {
                searchModelItem = new SearchViewModel();
                searchModelItem.setCityName(sourceItem.getCityName());
                searchModelItem.setDisplayName(sourceItem.getDisplayName());
                searchModelItem.setImageApp(sourceItem.getImageApp());
                searchModelItem.setMaxEndDate(sourceItem.getMaxEndDate());
                searchModelItem.setMinStartDate(sourceItem.getMinStartDate());
                searchModelItem.setSalesPrice(sourceItem.getSalesPrice());
                searchModelItem.setTitle(sourceItem.getTitle());
                searchModelItem.setUrl(sourceItem.getUrl());
                searchResults.add(searchModelItem);
            }
        }
        return searchResults;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }


    public static List<String> getGenres(String genre) {
        List<String> genreList = new ArrayList<>();

        String[] temp = genre.split("\\|");
        for (int i = 0; i < temp.length; i++) {
            genreList.add(temp[i]);
        }

        return genreList;
    }

    public static List<String> getDisplayTags(String displayTag) {
        List<String> displayTagsList = new ArrayList<>();

        String[] temp = displayTag.split("\\|");
        for (int i = 0; i < temp.length; i++) {
            displayTagsList.add(temp[i]);
        }

        return displayTagsList;
    }

    public static String convertTime(String time) {

        int totalMinutesInt = Integer.valueOf(time.toString());

        int hours = totalMinutesInt / 60;
        int hoursToDisplay = hours;

        if (hours > 12) {
            hoursToDisplay = hoursToDisplay - 12;
        }

        int minutesToDisplay = totalMinutesInt - (hours * 60);

        String minToDisplay = null;
        if (minutesToDisplay == 0) minToDisplay = "00";
        else if (minutesToDisplay < 10) minToDisplay = "0" + minutesToDisplay;
        else minToDisplay = "" + minutesToDisplay;

        String displayValue = hoursToDisplay + " " + "Jam" + " " + minToDisplay + " " + "Menit";

        return displayValue;

    }


    public static String convertEpochToString(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String[] getDateArray(String dateRange) {
        String[] date = new String[3];
        date[0] = dateRange.substring(0, 3);//day
        //Sat, 14 Apr 2018 - Sat, 14 Apr 2018
        date[1] = dateRange.substring(5, 7).trim();//date
        date[2] = dateRange.substring(7, 11).trim();//month
        return date;
    }

    public static String[] scheduleDateArray(int time, int pos, int size) {
        String[] scheduleDates = new String[size];
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        scheduleDates[pos] = dateString;
        return scheduleDates;
    }


    public static String convertShowTiming(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Long epochTime = time * 1000L;
        Date date = new Date(epochTime);
        String dateString = sdf.format(date);
        return dateString;
    }

    public static String currentTime() {
        SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("HH:mm");
        //Setting the time zone
        dateTimeInGMT.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        Log.d("Naveen", "Current Time in Jakarta" + dateTimeInGMT.format(new Date()));
        return dateTimeInGMT.format(new Date());
    }


    public static String movieDimension(String movieName) {
        if (movieName.contains("5D")) {
            return "5D";
        } else if (movieName.contains("4D")) {
            return "4D";
        } else if (movieName.contains("3D")) {
            return "3D";
        } else {
            return "2D";
        }
    }

    public static String getEmptyStringIfNull(String input) {
        return input == null ? "" : input;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static Bitmap getBitmap(Context context, LinearLayout v) {
        v.setDrawingCacheEnabled(true);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Bitmap bmp = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        canvas.drawColor(ContextCompat.getColor(context, R.color.preview_bg));
        v.draw(canvas);
        return bmp;
    }

    public static void saveImage(Context context, Bitmap finalBitmap) {


        String extStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(extStorageDirectory, "store_image");
        if (!folder.exists())
            folder.mkdir();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image" + n + ".png";

        File pdfFile = new File(folder, fname);

        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(pdfFile);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
