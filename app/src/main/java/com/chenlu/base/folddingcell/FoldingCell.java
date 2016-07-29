package com.chenlu.base.folddingcell;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chenlu.base.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Very first implementation of Folding Cell by Ramotion for Android platform
 * TODO: Update javadoc
 */
public class FoldingCell extends RelativeLayout
{

    private final String TAG = "folding-cell";

    /**
     * 当前Cell是不是展开的状态
     */
    private boolean mUnfolded;
    /**
     * 动画是否正在执行
     */
    private boolean mAnimationInProgress;

    /**
     * 默认情况下动画执行的时间
     */
    private final int DEF_ANIMATION_DURATION = 1000;
    /**
     * 默认情况下翻转的时候背面显示的背景颜色
     */
    private final int DEF_BACK_SIDE_COLOR = Color.GRAY;
    private final int DEF_ADDITIONAL_FLIPS = 0;

    /**
     * 动画执行的时间
     */
    private int mAnimationDuration = DEF_ANIMATION_DURATION;
    /**
     * 翻转的时候背面显示的颜色
     */
    private int mBackSideColor = DEF_BACK_SIDE_COLOR;
    private int mAdditionalFlipsCount = DEF_ADDITIONAL_FLIPS;

    public FoldingCell(Context context)
    {
        this(context,null);
    }

    public FoldingCell(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public FoldingCell(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initializeFromAttributes(context, attrs);
        //设置不裁剪Child
        this.setClipChildren(false);
        //设置根据Padding裁剪的时候不裁剪Child
        this.setClipToPadding(false);
    }

    /**
     * 根据View的属性的集合来初始化一些设置（动画执行的时间，翻转的时候背面显示的颜色）
     * @param context context           上下文对象
     * @param attrs   attributes        属性的集合
     */
    protected void initializeFromAttributes(Context context, AttributeSet attrs)
    {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FoldingCell, 0, 0);
        try
        {
            //获取动画的执行时间
            this.mAnimationDuration = array.getInt(R.styleable.FoldingCell_animationDuration, DEF_ANIMATION_DURATION);
            //翻转的时候背面显示的颜色
            this.mBackSideColor = array.getColor(R.styleable.FoldingCell_backSideColor, DEF_BACK_SIDE_COLOR);
            //
            this.mAdditionalFlipsCount = array.getInt(R.styleable.FoldingCell_additionalFlipsCount, DEF_ADDITIONAL_FLIPS);
        } finally
        {
            array.recycle();
        }
    }

    /**
     * 切换显示的Cell的状态，如果Cell的状态是折叠的话，就展开；如果是展开的话就折叠Cell
     * @param skipAnimation true：执行操作但是没有动画效果
     */
    public void toggle(boolean skipAnimation)
    {
        if (this.mUnfolded)
        {
            this.fold(skipAnimation);
        } else
        {
            this.unfold(skipAnimation);
        }
    }


    /**
     * 展开Cell
     * @param skipAnimation true：立刻展开cell，没有动画效果
     */
    public void unfold(boolean skipAnimation)
    {
        //如果当前的状态是展开的，或者动画还没有执行结束，那么不执行任何操作
        if (mUnfolded || mAnimationInProgress) return;
        //如果不显示动画效果，直接设置当前的状态为展开的状态
        if (skipAnimation)
        {
            setStateToUnfolded();
            return;
        }

        // 获取当前显示title和content的View，如果没有包含这些控件，表示当前的页面是一个不合法的页面，
        //不适合显示动画效果，所以不执行后续的操作
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;

        //创建一个用于执行动画效果的容器
        final LinearLayout foldingLayout = createAndPrepareFoldingContainer();
        this.addView(foldingLayout);

        //隐藏title和content显示的View
        titleView.setVisibility(GONE);
        contentView.setVisibility(GONE);

        // take bitmaps from title and content views
        Bitmap bitmapFromTitleView = getBitmapFromView(titleView, this.getMeasuredWidth());
        Bitmap bitmapFromContentView = getBitmapFromView(contentView, this.getMeasuredWidth());

        // calculate heights of animation parts
        ArrayList<Integer> heights = calculateHeightsForAnimationParts(titleView.getHeight(), contentView.getHeight(), mAdditionalFlipsCount);

        // create list with animation parts for animation
        ArrayList<FoldingCellView> foldingCellElements = prepareViewsForAnimation(heights, bitmapFromTitleView, bitmapFromContentView);

        // start fold animation with end listener
        int childCount = foldingCellElements.size();
        int part90degreeAnimationDuration = mAnimationDuration / (childCount * 2);
        startUnfoldAnimation(foldingCellElements, foldingLayout, part90degreeAnimationDuration, new AnimationEndListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                contentView.setVisibility(VISIBLE);
                foldingLayout.setVisibility(GONE);
                FoldingCell.this.removeView(foldingLayout);
                FoldingCell.this.mUnfolded = true;
                FoldingCell.this.mAnimationInProgress = false;
            }
        });

        startExpandHeightAnimation(heights, part90degreeAnimationDuration * 2);
        this.mAnimationInProgress = true;
    }

    /**
     * 设置当前的状态为展开的状态，没有动画效果
     */
    protected void setStateToUnfolded()
    {
        //如果当前的状态是展开的，或者动画正在执行，那么不执行任何操作
        if (this.mAnimationInProgress || this.mUnfolded) return;

        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;
        //设置显示内容，不现实标题
        contentView.setVisibility(VISIBLE);
        titleView.setVisibility(GONE);
        //设置当前的状态是展开的状态
        FoldingCell.this.mUnfolded = true;
        //设置当前的View显示的高度
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = contentView.getHeight();
        this.setLayoutParams(layoutParams);
        //请求重新计算布局
        this.requestLayout();
    }
    /**
     * 折叠Cell
     * @param skipAnimation true:折叠Cell但是没有动画
     */
    public void fold(boolean skipAnimation)
    {
        if (!mUnfolded || mAnimationInProgress) return;
        if (skipAnimation)
        {
            setStateToFolded();
            return;
        }

        // get basic views
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;

        // create empty layout for folding animation
        final LinearLayout foldingLayout = createAndPrepareFoldingContainer();
        // add that layout to structure
        this.addView(foldingLayout);

        // make bitmaps from title and content views
        Bitmap bitmapFromTitleView = getBitmapFromView(titleView, this.getMeasuredWidth());
        Bitmap bitmapFromContentView = getBitmapFromView(contentView, this.getMeasuredWidth());

        // hide title and content views
        titleView.setVisibility(GONE);
        contentView.setVisibility(GONE);

        // calculate heights of animation parts
        ArrayList<Integer> heights = calculateHeightsForAnimationParts(titleView.getHeight(), contentView.getHeight(), mAdditionalFlipsCount);

        // create list with animation parts for animation
        ArrayList<FoldingCellView> foldingCellElements = prepareViewsForAnimation(heights, bitmapFromTitleView, bitmapFromContentView);

        int childCount = foldingCellElements.size();
        int part90degreeAnimationDuration = mAnimationDuration / (childCount * 2);

        // start fold animation with end listener
        startFoldAnimation(foldingCellElements, foldingLayout, part90degreeAnimationDuration, new AnimationEndListener()
        {
            @Override
            public void onAnimationEnd(Animation animation)
            {
                contentView.setVisibility(GONE);
                titleView.setVisibility(VISIBLE);
                foldingLayout.setVisibility(GONE);
                FoldingCell.this.removeView(foldingLayout);
                FoldingCell.this.mAnimationInProgress = false;
                FoldingCell.this.mUnfolded = false;
            }
        });

        startCollapseHeightAnimation(heights, part90degreeAnimationDuration * 2);

        this.mAnimationInProgress = true;
    }
    /**
     * 创建一个容器，这个容器用于执行翻转的动画效果
     * @return Configured container for animation elements (LinearLayout)
     */
    protected LinearLayout createAndPrepareFoldingContainer()
    {
        LinearLayout foldingContainer = new LinearLayout(getContext());
        foldingContainer.setClipToPadding(false);
        foldingContainer.setClipChildren(false);
        foldingContainer.setOrientation(LinearLayout.VERTICAL);
        foldingContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return foldingContainer;
    }
    /**
     * Initializes folding cell programmatically with custom settings
     *
     * @param animationDuration    animation duration, default is 1000
     * @param backSideColor        color of back side, default is android.graphics.Color.GREY (0xFF888888)
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     */
    public void initialize(int animationDuration, int backSideColor, int additionalFlipsCount)
    {
        this.mAnimationDuration = animationDuration;
        this.mBackSideColor = backSideColor;
        this.mAdditionalFlipsCount = additionalFlipsCount;
    }




    /**
     * Create and prepare list of FoldingCellViews with different bitmap parts for fold animation
     *
     * @param titleViewBitmap   bitmap from title view
     * @param contentViewBitmap bitmap from content view
     * @return list of FoldingCellViews with bitmap parts
     */
    protected ArrayList<FoldingCellView> prepareViewsForAnimation(ArrayList<Integer> viewHeights, Bitmap titleViewBitmap, Bitmap contentViewBitmap)
    {
        if (viewHeights == null || viewHeights.isEmpty())
            throw new IllegalStateException("ViewHeights array must be not null and not empty");

        ArrayList<FoldingCellView> partsList = new ArrayList<>();

        int partWidth = titleViewBitmap.getWidth();
        int yOffset = 0;
        for (int i = 0; i < viewHeights.size(); i++)
        {
            int partHeight = viewHeights.get(i);
            Bitmap partBitmap = Bitmap.createBitmap(partWidth, partHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(partBitmap);
            Rect srcRect = new Rect(0, yOffset, partWidth, yOffset + partHeight);
            Rect destRect = new Rect(0, 0, partWidth, partHeight);
            canvas.drawBitmap(contentViewBitmap, srcRect, destRect, null);
            ImageView backView = createImageViewFromBitmap(partBitmap);
            ImageView frontView = null;
            if (i < viewHeights.size() - 1)
            {
                frontView = (i == 0) ? createImageViewFromBitmap(titleViewBitmap) : createBackSideView(viewHeights.get(i + 1));
            }
            partsList.add(new FoldingCellView(frontView, backView, getContext()));
            yOffset = yOffset + partHeight;
        }

        return partsList;
    }

    /**
     * Calculate heights for animation parts with some logic
     * TODO: Add detailed descriptions for logic
     *
     * @param titleViewHeight      height of title view
     * @param contentViewHeight    height of content view
     * @param additionalFlipsCount count of additional flips (after first one), set 0 for auto
     * @return list of calculated heights
     */
    protected ArrayList<Integer> calculateHeightsForAnimationParts(int titleViewHeight, int contentViewHeight, int additionalFlipsCount)
    {
        ArrayList<Integer> partHeights = new ArrayList<>();
        //内容页显示的高度必须大于标题页显示的高度
        int additionalPartsTotalHeight = contentViewHeight - titleViewHeight * 2;
        if (additionalPartsTotalHeight < 0)
            throw new IllegalStateException("Content View height is too small");
        // add two main parts - guarantee first flip
        partHeights.add(titleViewHeight);
        partHeights.add(titleViewHeight);

        // if no space left - return
        if (additionalPartsTotalHeight == 0) return partHeights;

        // if some space remained - use two different logic
        if (additionalFlipsCount != 0)
        {
            // 1 - additional parts count is specified and it is not 0 - divide remained space
            int additionalPartHeight = additionalPartsTotalHeight / additionalFlipsCount;
            int remainingHeight = additionalPartsTotalHeight % additionalFlipsCount;

            if (additionalPartHeight + remainingHeight > titleViewHeight)
                throw new IllegalStateException("Additional flips count is too small");
            for (int i = 0; i < additionalFlipsCount; i++)
                partHeights.add(additionalPartHeight + (i == 0 ? remainingHeight : 0));
        } else
        {
            // 2 - additional parts count isn't specified or 0 - divide remained space to parts with title view size
            int partsCount = additionalPartsTotalHeight / titleViewHeight;
            int restPartHeight = additionalPartsTotalHeight % titleViewHeight;
            for (int i = 0; i < partsCount; i++)
                partHeights.add(titleViewHeight);
            if (restPartHeight > 0) partHeights.add(restPartHeight);
        }

        return partHeights;
    }

    /**
     * Create image view for display back side of flip view
     *
     * @param height height for view
     * @return ImageView with selected height and default background color
     */
    protected ImageView createBackSideView(int height)
    {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(mBackSideColor);
        imageView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return imageView;
    }

    /**
     * Create image view for display selected bitmap
     *
     * @param bitmap bitmap to display in image view
     * @return ImageView with selected bitmap
     */
    protected ImageView createImageViewFromBitmap(Bitmap bitmap)
    {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
        return imageView;
    }

    /**
     * 从一个View中获取一个Bitmap。这个Bitmap就是这个View显示的内容的一个影像
     * @param view        source for bitmap
     * @param parentWidth result bitmap width       bitmap显示的宽度
     * @return bitmap from specified view
     */
    protected Bitmap getBitmapFromView(View view, int parentWidth)
    {
        //获取测量的宽度，这个宽度是精确的值
        int specW = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        //获取测量的高度，这个高度是不定的，需要测量计算
        int specH = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(specW, specH);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        return b;
    }



    /**
     * Prepare and start height expand animation for FoldingCellLayout
     *
     * @param partAnimationDuration one part animate duration
     * @param viewHeights           heights of animation parts
     */
    protected void startExpandHeightAnimation(ArrayList<Integer> viewHeights, int partAnimationDuration)
    {
        if (viewHeights == null || viewHeights.isEmpty())
            throw new IllegalArgumentException("ViewHeights array must have at least 2 elements");

        ArrayList<Animation> heightAnimations = new ArrayList<>();
        int fromHeight = viewHeights.get(0);
        for (int i = 1; i < viewHeights.size(); i++)
        {
            int toHeight = fromHeight + viewHeights.get(i);
            heightAnimations.add(new HeightAnimation(this, fromHeight, toHeight, partAnimationDuration).withInterpolator(new DecelerateInterpolator()));
            fromHeight = toHeight;
        }
        createAnimationChain(heightAnimations, this);
        this.startAnimation(heightAnimations.get(0));
    }

    /**
     * Prepare and start height collapse animation for FoldingCellLayout
     *
     * @param partAnimationDuration one part animate duration
     * @param viewHeights           heights of animation parts
     */
    protected void startCollapseHeightAnimation(ArrayList<Integer> viewHeights, int partAnimationDuration)
    {
        if (viewHeights == null || viewHeights.isEmpty())
            throw new IllegalArgumentException("ViewHeights array must have at least 2 elements");

        ArrayList<Animation> heightAnimations = new ArrayList<>();
        int fromHeight = viewHeights.get(0);
        for (int i = 1; i < viewHeights.size(); i++)
        {
            int toHeight = fromHeight + viewHeights.get(i);
            heightAnimations.add(new HeightAnimation(this, toHeight, fromHeight, partAnimationDuration).withInterpolator(new DecelerateInterpolator()));
            fromHeight = toHeight;
        }

        Collections.reverse(heightAnimations);
        createAnimationChain(heightAnimations, this);
        this.startAnimation(heightAnimations.get(0));
    }

    /**
     * Create "animation chain" for selected view from list of animations objects
     *
     * @param animationList   collection with animations
     * @param animationObject view for animations
     */
    protected void createAnimationChain(final List<Animation> animationList, final View animationObject)
    {
        for (int i = 0; i < animationList.size(); i++)
        {
            Animation animation = animationList.get(i);
            if (i + 1 < animationList.size())
            {
                final int finalI = i;
                animation.setAnimationListener(new AnimationEndListener()
                {
                    public void onAnimationEnd(Animation animation)
                    {
                        animationObject.startAnimation(animationList.get(finalI + 1));
                    }
                });
            }
        }
    }

    /**
     * Start fold animation
     *
     * @param foldingCellElements           ordered list with animation parts from top to bottom
     * @param foldingLayout                 prepared layout for animation parts
     * @param part90degreeAnimationDuration animation duration for 90 degree rotation
     * @param animationEndListener          animation end callback
     */
    protected void startFoldAnimation(ArrayList<FoldingCellView> foldingCellElements, ViewGroup foldingLayout, int part90degreeAnimationDuration, AnimationEndListener animationEndListener)
    {
        for (FoldingCellView foldingCellElement : foldingCellElements)
            foldingLayout.addView(foldingCellElement);

        Collections.reverse(foldingCellElements);

        int nextDelay = 0;
        for (int i = 0; i < foldingCellElements.size(); i++)
        {
            FoldingCellView cell = foldingCellElements.get(i);
            cell.setVisibility(VISIBLE);
            // not FIRST(BOTTOM) element - animate front view
            if (i != 0)
            {
                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_UP, part90degreeAnimationDuration).withStartOffset(nextDelay).withInterpolator(new DecelerateInterpolator());
                // if last(top) element - add end listener
                if (i == foldingCellElements.size() - 1)
                {
                    foldAnimation.setAnimationListener(animationEndListener);
                }
                cell.animateFrontView(foldAnimation);
                nextDelay = nextDelay + part90degreeAnimationDuration;
            }
            // if not last(top) element - animate whole view
            if (i != foldingCellElements.size() - 1)
            {
                cell.startAnimation(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_UP, part90degreeAnimationDuration).withStartOffset(nextDelay).withInterpolator(new DecelerateInterpolator()));
                nextDelay = nextDelay + part90degreeAnimationDuration;
            }
        }
    }

    /**
     * Start unfold animation
     *
     * @param foldingCellElements           ordered list with animation parts from top to bottom
     * @param foldingLayout                 prepared layout for animation parts
     * @param part90degreeAnimationDuration animation duration for 90 degree rotation
     * @param animationEndListener          animation end callback
     */
    protected void startUnfoldAnimation(ArrayList<FoldingCellView> foldingCellElements, ViewGroup foldingLayout, int part90degreeAnimationDuration, AnimationEndListener animationEndListener)
    {
        int nextDelay = 0;
        for (int i = 0; i < foldingCellElements.size(); i++)
        {
            FoldingCellView cell = foldingCellElements.get(i);
            cell.setVisibility(VISIBLE);
            foldingLayout.addView(cell);
            // if not first(top) element - animate whole view
            if (i != 0)
            {
                FoldAnimation foldAnimation = new FoldAnimation(FoldAnimation.FoldAnimationMode.UNFOLD_DOWN, part90degreeAnimationDuration).withStartOffset(nextDelay).withInterpolator(new DecelerateInterpolator());

                // if last(bottom) element - add end listener
                if (i == foldingCellElements.size() - 1)
                {
                    foldAnimation.setAnimationListener(animationEndListener);
                }

                nextDelay = nextDelay + part90degreeAnimationDuration;
                cell.startAnimation(foldAnimation);

            }
            // not last(bottom) element - animate front view
            if (i != foldingCellElements.size() - 1)
            {
                cell.animateFrontView(new FoldAnimation(FoldAnimation.FoldAnimationMode.FOLD_DOWN, part90degreeAnimationDuration).withStartOffset(nextDelay).withInterpolator(new DecelerateInterpolator()));
                nextDelay = nextDelay + part90degreeAnimationDuration;
            }
        }
    }



    /**
     * Instantly change current state of cell to Folded without any animations
     */
    protected void setStateToFolded()
    {
        if (this.mAnimationInProgress || !this.mUnfolded) return;
        // get basic views
        final View contentView = getChildAt(0);
        if (contentView == null) return;
        final View titleView = getChildAt(1);
        if (titleView == null) return;
        contentView.setVisibility(GONE);
        titleView.setVisibility(VISIBLE);
        FoldingCell.this.mUnfolded = false;
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = titleView.getHeight();
        this.setLayoutParams(layoutParams);
        this.requestLayout();
    }


}
