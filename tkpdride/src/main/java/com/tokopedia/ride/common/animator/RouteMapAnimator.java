package com.tokopedia.ride.common.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by alvarisi on 4/17/17.
 */

public class RouteMapAnimator {
    public static final float POLYLINE_WIDTH_IN_DP = 2.0f;

    private static RouteMapAnimator mapAnimator;

    private Polyline backgroundPolyline;

    private Polyline foregroundPolyline;

    private PolylineOptions optionsForeground;

    private AnimatorSet firstRunAnimSet;

    private AnimatorSet secondLoopRunAnimSet;

    static final int GREY = Color.parseColor("#FFA7A6A6");
    static final int BLACK = Color.parseColor("#FF000000");

    private float pathWidth;


    private RouteMapAnimator() {
        pathWidth = convertDpToPixel(POLYLINE_WIDTH_IN_DP);
    }

    public static RouteMapAnimator getInstance() {
        if (mapAnimator == null) mapAnimator = new RouteMapAnimator();
        return mapAnimator;
    }

    public void renderRouteWithoutAnimation(GoogleMap googleMap, List<LatLng> latLngList) {
        if (googleMap == null) {
            return;
        }

        if (pathWidth == 0) {
            pathWidth = convertDpToPixel(POLYLINE_WIDTH_IN_DP);
        }

        googleMap.addPolyline(new PolylineOptions()
                .addAll(latLngList)
                .width(pathWidth)
                .color(BLACK)
                .geodesic(true));
    }


    public void animateRoute(GoogleMap googleMap, List<LatLng> latLngList) {
        if (pathWidth == 0) {
            pathWidth = convertDpToPixel(POLYLINE_WIDTH_IN_DP);
        }

        if (firstRunAnimSet == null) {
            firstRunAnimSet = new AnimatorSet();
        } else {
            firstRunAnimSet.removeAllListeners();
            firstRunAnimSet.end();
            firstRunAnimSet.cancel();

            firstRunAnimSet = new AnimatorSet();
        }
        if (secondLoopRunAnimSet == null) {
            secondLoopRunAnimSet = new AnimatorSet();
        } else {
            secondLoopRunAnimSet.removeAllListeners();
            secondLoopRunAnimSet.end();
            secondLoopRunAnimSet.cancel();

            secondLoopRunAnimSet = new AnimatorSet();
        }
        //Reset the polylines
        if (foregroundPolyline != null) foregroundPolyline.remove();
        if (backgroundPolyline != null) backgroundPolyline.remove();


        PolylineOptions
                optionsBackground = new PolylineOptions().add(latLngList.get(0)).color(GREY).width(pathWidth).geodesic(true);
        backgroundPolyline = googleMap.addPolyline(optionsBackground);

        optionsForeground = new PolylineOptions().add(latLngList.get(0)).color(BLACK).width(pathWidth).geodesic(true);
        foregroundPolyline = googleMap.addPolyline(optionsForeground);

        final ValueAnimator percentageCompletion = ValueAnimator.ofInt(0, 100);
        percentageCompletion.setDuration(2000);
        percentageCompletion.setInterpolator(new DecelerateInterpolator());
        percentageCompletion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                List<LatLng> foregroundPoints = backgroundPolyline.getPoints();

                int percentageValue = (int) animation.getAnimatedValue();
                int pointcount = foregroundPoints.size();
                int countTobeRemoved = (int) (pointcount * (percentageValue / 100.0f));
                List<LatLng> subListTobeRemoved = foregroundPoints.subList(0, countTobeRemoved);
                subListTobeRemoved.clear();

                foregroundPolyline.setPoints(foregroundPoints);
            }
        });
        percentageCompletion.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                foregroundPolyline.setColor(BLACK);
                foregroundPolyline.setPoints(backgroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), GREY, BLACK);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        colorAnimation.setDuration(1200); // milliseconds

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                foregroundPolyline.setColor((int) animator.getAnimatedValue());
            }

        });

        ObjectAnimator foregroundRouteAnimator = ObjectAnimator.ofObject(this, "routeIncreaseForward", new RouteEvaluator(), latLngList.toArray());
        foregroundRouteAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        foregroundRouteAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                backgroundPolyline.setPoints(foregroundPolyline.getPoints());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        foregroundRouteAnimator.setDuration(1600);

        firstRunAnimSet.playSequentially(foregroundRouteAnimator,
                percentageCompletion);
        firstRunAnimSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        firstRunAnimSet.start();
    }

    /**
     * This will be invoked by the ObjectAnimator multiple times. Mostly every 16ms.
     **/
    public void setRouteIncreaseForward(LatLng endLatLng) {
        List<LatLng> foregroundPoints = foregroundPolyline.getPoints();
        foregroundPoints.add(endLatLng);
        foregroundPolyline.setPoints(foregroundPoints);
    }

    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
