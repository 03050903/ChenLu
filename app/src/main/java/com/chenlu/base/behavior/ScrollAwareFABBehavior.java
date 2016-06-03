package com.chenlu.base.behavior;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import com.chenlu.base.R;

import static android.R.attr.button;

/**
 * 定义了FloatingActionButton根据滑动显示隐藏的操作
 * Created by C&C on 2016/6/3.
 */

public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior
{
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;
    public ScrollAwareFABBehavior(Context context, AttributeSet attributeSet)
    {
        super();
    }
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes)
    {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)
    {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE)
        {
            animateOut(child);
        } else if(dyConsumed < 0 && child.getVisibility()!=View.VISIBLE)
        {
            animatingIn(child);
        }
    }

    private void animateOut(final FloatingActionButton child)
    {
        if (Build.VERSION.SDK_INT >= 14)
        {
            ViewCompat.animate(child).scaleX(0.0f).scaleY(0.0f).alpha(0.0f).
                    setInterpolator(INTERPOLATOR).withLayer().setListener(new ViewPropertyAnimatorListener()
            {
                @Override
                public void onAnimationStart(View view)
                {
                    ScrollAwareFABBehavior.this.mIsAnimatingOut = true;
                }

                @Override
                public void onAnimationEnd(View view)
                {
                    ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(View view)
                {
                    ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                }
            }).start();
        } else
        {
            Animation anim = AnimationUtils.loadAnimation(child.getContext(), R.anim.fab_out);
            anim.setInterpolator(INTERPOLATOR);
            anim.setDuration(200L);
            anim.setAnimationListener(new Animation.AnimationListener()
            {
                public void onAnimationStart(Animation animation)
                {
                    ScrollAwareFABBehavior.this.mIsAnimatingOut = true;
                }

                public void onAnimationEnd(Animation animation)
                {
                    ScrollAwareFABBehavior.this.mIsAnimatingOut = false;
                    child.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(final Animation animation)
                {

                }
            });
            child.startAnimation(anim);
        }

    }

    private void animatingIn(final FloatingActionButton child)
    {
        child.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 14)
        {
            ViewCompat.animate(child).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                    .start();
        } else
        {
            Animation anim = AnimationUtils.loadAnimation(child.getContext(), R.anim.fab_in);
            anim.setDuration(200L);
            anim.setInterpolator(INTERPOLATOR);
            child.startAnimation(anim);
        }
    }
}
