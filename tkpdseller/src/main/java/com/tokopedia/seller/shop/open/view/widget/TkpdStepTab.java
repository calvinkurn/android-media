package com.tokopedia.seller.shop.open.view.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.stepstone.stepper.internal.widget.StepTab;
import com.tokopedia.seller.R;

/**
 * Created by Nathaniel on 3/22/2017.
 */

public class TkpdStepTab extends StepTab {

    private static final float ALPHA_TRANSPARENT = 0.0f;

    private static final float ALPHA_INACTIVE_STEP_TITLE = 0.54f;

    private static final float ALPHA_OPAQUE = 1.0f;

    private static final float HALF_SIZE_SCALE = 0.5f;

    private static final float FULL_SIZE_SCALE = 1.0f;

    @ColorInt
    private int mUnselectedColor;

    @ColorInt
    private int mSelectedColor;

    @ColorInt
    private int mErrorColor;

    @ColorInt
    private int mTitleColor;

    private TextView mStepNumber;

    private View mStepDivider;

    private View mLeftStepDivider;

    private TextView mStepTitle;

    private ImageView mStepDoneIndicator;

    private ImageView mStepIconBackground;

    private AbstractState mCurrentState = new InactiveNumberState();

    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    public TkpdStepTab(Context context) {
        super(context);
        initializeView(context);
    }

    public TkpdStepTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public TkpdStepTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context);
    }

    private void initializeView(Context context) {
        mSelectedColor = ContextCompat.getColor(context, R.color.ms_selectedColor);
        mUnselectedColor = ContextCompat.getColor(context, R.color.ms_unselectedColor);
        mErrorColor = ContextCompat.getColor(context, R.color.ms_errorColor);

        mStepNumber = (TextView) findViewById(R.id.ms_stepNumber);
        mStepDoneIndicator = (ImageView) findViewById(R.id.ms_stepDoneIndicator);
        mStepIconBackground = (ImageView) findViewById(R.id.ms_stepIconBackground);
        mStepDivider = findViewById(R.id.ms_stepDivider);
        mStepTitle = ((TextView) findViewById(R.id.ms_stepTitle));

        mTitleColor = mStepTitle.getCurrentTextColor();

        Typeface typeface = mStepTitle.getTypeface();
        Drawable avd = createCircleToWarningDrawable();
        mStepIconBackground.setImageDrawable(avd);

        mStepDivider = findViewById(R.id.ms_stepDivider);
        mLeftStepDivider = findViewById(R.id.ms_left_stepDivider);
    }

    @Override
    public void toggleDividerVisibility(boolean show) {
        mStepDivider.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    public void toggleLeftDividerVisibility(boolean show) {
        mLeftStepDivider.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    @Override
    public void updateState(final boolean error, final boolean done, final boolean current) {
        if (error) {
            mCurrentState.changeToWarning();
        } else if (done) {
            mCurrentState.changeToDone();
        } else if (current) {
            mCurrentState.changeToActiveNumber();
        } else {
            mCurrentState.changeToInactiveNumber();
        }
    }

    @Override
    public void setUnselectedColor(int unselectedColor) {
        mUnselectedColor = unselectedColor;
    }

    @Override
    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    @Override
    public void setErrorColor(int errorColor) {
        mErrorColor = errorColor;
    }

    private Drawable createCircleToWarningDrawable() {
        return createAnimatedVectorDrawable(R.drawable.ms_animated_vector_circle_to_warning_24dp);
    }

    private Drawable createWarningToCircleDrawable() {
        return createAnimatedVectorDrawable(R.drawable.ms_animated_vector_warning_to_circle_24dp);
    }

    @Override
    public Drawable createAnimatedVectorDrawable(@DrawableRes int animatedVectorDrawableResId) {
        Context context = getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = context.getDrawable(animatedVectorDrawableResId);
            return drawable.getConstantState().newDrawable(context.getResources());
        } else {
            return AnimatedVectorDrawableCompat.create(context, animatedVectorDrawableResId);
        }
    }

    private abstract class AbstractState {

        @CallSuper
        protected void changeToInactiveNumber() {
            mCurrentState = new InactiveNumberState();
        }

        @CallSuper
        protected void changeToActiveNumber() {
            mCurrentState = new ActiveNumberState();
        }

        @CallSuper
        protected void changeToDone() {
            mCurrentState = new DoneState();
        }

        @CallSuper
        protected void changeToWarning() {
            mStepDoneIndicator.setVisibility(View.GONE);
            mStepNumber.setVisibility(View.GONE);
            mStepIconBackground.setColorFilter(mErrorColor);
            mStepTitle.setTextColor(mErrorColor);
            mCurrentState = new WarningState();
        }
    }

    private abstract class AbstractNumberState extends AbstractState {

        @Override
        @CallSuper
        protected void changeToWarning() {
            Drawable avd = createCircleToWarningDrawable();
            mStepIconBackground.setImageDrawable(avd);
            ((Animatable) avd).start();
            super.changeToWarning();
        }

        @Override
        @CallSuper
        protected void changeToDone() {
            mStepDoneIndicator.setVisibility(VISIBLE);
            mStepNumber.setVisibility(GONE);
            super.changeToDone();
        }

    }

    private class InactiveNumberState extends AbstractNumberState {

        @Override
        protected void changeToInactiveNumber() {
            mStepIconBackground.setColorFilter(mUnselectedColor);
            mStepTitle.setTextColor(mTitleColor);
            mStepTitle.setAlpha(ALPHA_INACTIVE_STEP_TITLE);
            super.changeToInactiveNumber();
        }

        @Override
        protected void changeToActiveNumber() {
            mStepIconBackground.setColorFilter(mSelectedColor);
            mStepTitle.setAlpha(ALPHA_OPAQUE);
            super.changeToActiveNumber();
        }

        @Override
        protected void changeToDone() {
            mStepIconBackground.setColorFilter(mSelectedColor);
            mStepTitle.setAlpha(ALPHA_OPAQUE);
            super.changeToDone();
        }
    }

    private class ActiveNumberState extends AbstractNumberState {

        @Override
        protected void changeToInactiveNumber() {
            mStepIconBackground.setColorFilter(mUnselectedColor);
            mStepTitle.setAlpha(ALPHA_INACTIVE_STEP_TITLE);
            super.changeToInactiveNumber();
        }
    }

    private class DoneState extends AbstractState {

        @Override
        protected void changeToInactiveNumber() {
            mStepDoneIndicator.setVisibility(GONE);
            mStepNumber.setVisibility(VISIBLE);
            mStepIconBackground.setColorFilter(mUnselectedColor);
            mStepTitle.setAlpha(ALPHA_INACTIVE_STEP_TITLE);
            super.changeToInactiveNumber();
        }

        @Override
        protected void changeToActiveNumber() {
            mStepDoneIndicator.setVisibility(GONE);
            mStepNumber.setVisibility(VISIBLE);
            super.changeToActiveNumber();
        }

        @Override
        protected void changeToWarning() {
            Drawable avd = createCircleToWarningDrawable();
            mStepIconBackground.setImageDrawable(avd);
            ((Animatable) avd).start();
            super.changeToWarning();
        }
    }

    private class WarningState extends AbstractState {

        @Override
        protected void changeToDone() {
            animateViewIn(mStepDoneIndicator);

            mStepIconBackground.setColorFilter(mSelectedColor);
            mStepTitle.setTextColor(mTitleColor);
            super.changeToDone();
        }

        @Override
        protected void changeToInactiveNumber() {
            animateViewIn(mStepNumber);

            mStepIconBackground.setColorFilter(mUnselectedColor);
            mStepTitle.setTextColor(mTitleColor);
            mStepTitle.setAlpha(ALPHA_INACTIVE_STEP_TITLE);

            super.changeToInactiveNumber();
        }

        @Override
        protected void changeToActiveNumber() {
            animateViewIn(mStepNumber);

            mStepIconBackground.setColorFilter(mSelectedColor);
            mStepTitle.setTextColor(mTitleColor);
            super.changeToActiveNumber();
        }

        private void animateViewIn(final View view) {
            Drawable avd = createWarningToCircleDrawable();
            mStepIconBackground.setImageDrawable(avd);
            ((Animatable) avd).start();

            view.setVisibility(View.VISIBLE);
            view.setAlpha(ALPHA_TRANSPARENT);
            view.setScaleX(HALF_SIZE_SCALE);
            view.setScaleY(HALF_SIZE_SCALE);
            view.animate()
                    .setInterpolator(accelerateInterpolator)
                    .alpha(ALPHA_OPAQUE)
                    .scaleX(FULL_SIZE_SCALE)
                    .scaleY(FULL_SIZE_SCALE);

        }
    }
}
