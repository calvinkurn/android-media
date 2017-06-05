/*
 * Copyright 2015 Diogo Bernardino
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tokopedia.seller.lib.williamchart.tooltip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.williamchart.listener.OnTooltipEventListener;
import com.tokopedia.seller.lib.williamchart.model.TooltipModel;
import com.tokopedia.seller.lib.williamchart.renderer.StringFormatRenderer;

import java.text.DecimalFormat;


/**
 * Class representing chart's tooltips. It works basically as a wrapper.
 */
public class Tooltip extends RelativeLayout {

	private Alignment mVerticalAlignment = Alignment.CENTER;

	private Alignment mHorizontalAlignment = Alignment.CENTER;

	private TextView mTooltipValue;

	private OnTooltipEventListener mTooltipEventListener;

	private ObjectAnimator mEnterAnimator;

	private ObjectAnimator mExitAnimator;

	private int mWidth;

	private int mHeight;

	private int mLeftMargin;

	private int mTopMargin;

	private int mRightMargin;

	private int mBottomMargin;

	private boolean mOn;

	private DecimalFormat mValueFormat;
	private StringFormatRenderer stringFormatRenderer = new StringFormatRenderer() {
		@Override
		public String formatString(String rawString) {
			return rawString;
		}
	};

	public void setStringFormatRenderer(StringFormatRenderer stringFormatRenderer) {
		this.stringFormatRenderer = stringFormatRenderer;
	}

	public Tooltip(Context context) {

		super(context);
		init();
	}

	public Tooltip(Context context, int layoutId) {

		super(context);
		init();

		initView(context, layoutId);
	}


	public Tooltip(Context context, int layoutId, int valueId) {

		super(context);
		init();

		initView(context, layoutId);

		mTooltipValue = (TextView) findViewById(valueId);
	}


	public Tooltip(Context context, int layoutId, int valueId, StringFormatRenderer stringFormatRenderer) {

		super(context);
		init();

		initView(context, layoutId);

		mTooltipValue = (TextView) findViewById(valueId);
		this.stringFormatRenderer = stringFormatRenderer;
	}

	private void initView(Context context, int layoutId) {
		View layoutParent = inflate(getContext(), layoutId, null);
		layoutParent.setLayoutParams(
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(layoutParent);
	}

	private void init() {

		mWidth = -1;
		mHeight = -1;
		mLeftMargin = 0;
		mTopMargin = 0;
		mRightMargin = 0;
		mBottomMargin = 0;
		mOn = false;
		mValueFormat = new DecimalFormat();
	}

	/**
	 * Method called by ChartView before displaying the
	 * tooltip in order to set its layout parameters.
	 *
	 * @param rect {@link Rect} covering the are of the
	 * clicked {@link com.tokopedia.seller.lib.williamchart.model.ChartEntry}.
	 * @param value Value of the entry.
	 */
	public void prepare(Rect rect, TooltipModel value) {

		// If no previous dimensions defined, the size of the area of the entry will be used.
		int width = (mWidth == -1) ? rect.width() : mWidth;
		int height = (mHeight == -1) ? rect.height() : mHeight;

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);

		// Adjust left coordinate of the tooltip based on the Alignment defined
		if (mHorizontalAlignment == Alignment.RIGHT_LEFT)
			layoutParams.leftMargin = rect.left - width - mRightMargin;
		if (mHorizontalAlignment == Alignment.LEFT_LEFT)
			layoutParams.leftMargin = rect.left + mLeftMargin;
		if (mHorizontalAlignment == Alignment.CENTER)
			layoutParams.leftMargin = rect.centerX() - width / 2;
		if (mHorizontalAlignment == Alignment.RIGHT_RIGHT)
			layoutParams.leftMargin = rect.right - width - mRightMargin;
		if (mHorizontalAlignment == Alignment.LEFT_RIGHT)
			layoutParams.leftMargin = rect.right + mLeftMargin;

		// Adjust top coordinate of tooltip based on the Alignment defined
		if (mVerticalAlignment == Alignment.BOTTOM_TOP)
			layoutParams.topMargin = rect.top - height - mBottomMargin;
		else if (mVerticalAlignment == Alignment.TOP_TOP)
			layoutParams.topMargin = rect.top + mTopMargin;
		else if (mVerticalAlignment == Alignment.CENTER)
			layoutParams.topMargin = rect.centerY() - height / 2;
		else if (mVerticalAlignment == Alignment.BOTTOM_BOTTOM)
			layoutParams.topMargin = rect.bottom - height - mBottomMargin;
		else if (mVerticalAlignment == Alignment.TOP_BOTTOM)
			layoutParams.topMargin = rect.bottom + mTopMargin;

		setLayoutParams(layoutParams);


		if (mTooltipValue != null) mTooltipValue.setText(stringFormatRenderer.formatString(String.valueOf(value.getValue())));
	}


	/**
	 * Corrects the position of a tooltip and forces it to
	 * be within {@link com.tokopedia.seller.lib.williamchart.view.ChartView}.
	 *
	 * @param left left coordinate of {@link com.tokopedia.seller.lib.williamchart.view.ChartView}
	 * @param top top coordinate of {@link com.tokopedia.seller.lib.williamchart.view.ChartView}
	 * @param right right coordinate of {@link com.tokopedia.seller.lib.williamchart.view.ChartView}
	 * @param bottom bottom coordinate of {@link com.tokopedia.seller.lib.williamchart.view.ChartView}
	 */
	public void correctPosition(int left, int top, int right, int bottom) {

		final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

		if (layoutParams.leftMargin < left) layoutParams.leftMargin = left;
		if (layoutParams.topMargin < top) layoutParams.topMargin = top;
		if (layoutParams.leftMargin + layoutParams.width > right)
			layoutParams.leftMargin = right - layoutParams.width;
		if (layoutParams.topMargin + layoutParams.height > bottom)
			layoutParams.topMargin = bottom - layoutParams.height;
		setLayoutParams(layoutParams);
	}


	/**
	 * Starts enter animation.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void animateEnter() {

		mEnterAnimator.start();
	}


	/**
	 * Start exit animation.
	 *
	 * @param endAction Action to be executed at the end of the animation.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void animateExit(final Runnable endAction) {

		mExitAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {}


			@Override
			public void onAnimationEnd(Animator animation) {

				endAction.run();
			}


			@Override
			public void onAnimationCancel(Animator animation) {}


			@Override
			public void onAnimationRepeat(Animator animation) {}
		});
		mExitAnimator.start();
	}


	/**
	 * Check if enter animation of type {@link ObjectAnimator} exists.
	 *
	 * @return true if {@link Tooltip} has enter animation defined.
	 */
	public boolean hasEnterAnimation() {

		return mEnterAnimator != null;
	}


	/**
	 * Check if exit animation of type {@link ObjectAnimator} exists.
	 *
	 * @return true if {@link Tooltip} has exit animation define.
	 */
	public boolean hasExitAnimation() {

		return mExitAnimator != null;
	}


	/**
	 * Maintain information about whether the tooltip is being displayed or not.
	 *
	 * @return true if {@link Tooltip} is currently displayed.
	 */
	public boolean on() {

		return mOn;
	}


	/**
	 * Define the horizontal alignment of tooltip wrt entry's position.
	 * Ex. Alignment.LEFT_RIGHT means that the left side of the tooltip
	 * will be aligned with the right side of the entry.
	 *
	 * @param alignment horizontal alignment wrt entry's position.
	 *
	 * @return {@link Tooltip} self-reference.
	 */
	public Tooltip setHorizontalAlignment(Alignment alignment) {

		mHorizontalAlignment = alignment;
		return this;
	}


	/**
	 * Define the vertical alignment of tooltip wrt entry's position.
	 * Ex. Alignment.TOP_TOP means that the top side of the tooltip
	 * will be aligned with the top side of the entry.
	 *
	 * @param alignment vertical alignment wrt entry's position.
	 *
	 * @return {@link Tooltip} self-reference.
	 */
	public Tooltip setVerticalAlignment(Alignment alignment) {

		mVerticalAlignment = alignment;
		return this;
	}


	/**
	 * Set the dimensions of the tooltip.
	 *
	 * @param width width dimension
	 * @param height height dimension
	 *
	 * @return {@link Tooltip} self-reference.
	 */
	public Tooltip setDimensions(int width, int height) {

		mWidth = width;
		mHeight = height;
		return this;
	}

	/**
	 * Set the margins of the tooltip wrt entry.
	 *
	 * @param left left margin dimension.
	 * @param top top margin dimension.
	 * @param right right margin dimension.
	 * @param bottom bottom margin dimension.
	 *
	 * @return {@link Tooltip} self-reference.
	 */
	public Tooltip setMargins(int left, int top, int right, int bottom) {

		mLeftMargin = left;
		mTopMargin = top;
		mRightMargin = right;
		mBottomMargin = bottom;
		return this;
	}


	/**
	 * If the tooltip is being displayed.
	 *
	 * @param on True if displayed, False if not.
	 */
	public void setOn(boolean on) {

		mOn = on;
	}


	/**
	 * Set the format to be applied to tooltip's value.
	 *
	 * @param format value format to be used once the tooltip is displayed.
	 *
	 * @return {@link Tooltip} self-reference.
	 */
	public Tooltip setValueFormat(DecimalFormat format) {

		mValueFormat = format;
		return this;
	}


	/**
	 * @param values
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ObjectAnimator setEnterAnimation(PropertyValuesHolder... values) {

		for (PropertyValuesHolder value : values) {

			if (value.getPropertyName().equals("alpha")) setAlpha(0);
			if (value.getPropertyName().equals("rotation")) setRotation(0);
			if (value.getPropertyName().equals("rotationX")) setRotationX(0);
			if (value.getPropertyName().equals("rotationY")) setRotationY(0);
			if (value.getPropertyName().equals("translationX")) setTranslationX(0);
			if (value.getPropertyName().equals("translationY")) setTranslationY(0);
			if (value.getPropertyName().equals("scaleX")) setScaleX(0);
			if (value.getPropertyName().equals("scaleY")) setScaleY(0);
		}
		return mEnterAnimator = ObjectAnimator.ofPropertyValuesHolder(this, values);
	}


	/**
	 * @param values
	 *
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ObjectAnimator setExitAnimation(PropertyValuesHolder... values) {

		return mExitAnimator = ObjectAnimator.ofPropertyValuesHolder(this, values);
	}


	public enum Alignment {
		BOTTOM_TOP,
		TOP_BOTTOM,
		TOP_TOP,
		CENTER,
		BOTTOM_BOTTOM,
		LEFT_LEFT,
		RIGHT_LEFT,
		RIGHT_RIGHT,
		LEFT_RIGHT
	}
}
