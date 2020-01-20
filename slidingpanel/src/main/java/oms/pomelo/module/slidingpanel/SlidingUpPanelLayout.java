package oms.pomelo.module.slidingpanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public class SlidingUpPanelLayout extends ViewGroup {

    private static final String TAG = SlidingUpPanelLayout.class.getSimpleName();

    /**
     * 默认的滑动面板初始状态露出的尺寸
     */
    private static final int DEFAULT_PANEL_HEIGHT = 70; // dp;
    /**
     * 默认可设置的attrs
     */
    private static final int[] DEFAULT_ATTRS = new int[] {
            android.R.attr.gravity
    };
    /**
     * 滑动面板默认<b>不</b>覆盖 Window
     */
    private static final boolean DEFAULT_OVERLAY_FLAG = false;

    /**
     * 滑动面板初始状态露出的尺寸(pixels)
     */
    private int mPanelHeight = -1;

    /**
     * 滑动面板收起的时候则为 True
     */
    private boolean mIsSlidingUp;

    /**
     * 滑动面板是否覆盖 Window FIXME
     */
    private boolean mOverlayContent = DEFAULT_OVERLAY_FLAG;

    /**
     * Layout的主布局
     */
    private View mMainView;

    /**
     * Layout的滑动面板
     */
    private View mSlideableView;

    /**
     * 拖拽区域：默认为整个 mSlideableView
     */
    private View mDragView;

    /**
     * 滑动面板可滑动的尺寸(pixels)
     */
    private int mSlideRange;

    /**
     * 滑动面板偏离其展开位置的距离
     * range [0, 1] where 0 = collapsed, 1 = expanded.
     */
    private float mSlideOffset;

    /**
     * 滑动面板在滑动过程中可以停下来的锚点
     */
    private float mAnchorPoint = 1.f;

    /**
     * Stores whether or not the pane was expanded the last time it was slideable.
     * If expand/collapse operations are invoked this state is modified. Used by
     * instance state save/restore.
     */
    private boolean mFirstLayout = true;

    public enum PanelState {
        EXPANDED,  //展开
        COLLAPSED, //收起
        ANCHORED,
        HIDDEN,    //隐藏
        DRAGGING   //正在拖动
    }

    /**
     * Default initial state for the component
     */
    private static PanelState DEFAULT_SLIDE_STATE = PanelState.COLLAPSED;

    private PanelState mSlideState = DEFAULT_SLIDE_STATE;


    public SlidingUpPanelLayout(Context context) {
        this(context, null);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingUpPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray defAttrs = context.obtainStyledAttributes(attrs, DEFAULT_ATTRS);

            if (defAttrs != null) {
                int gravity = defAttrs.getInt(0, Gravity.NO_GRAVITY);
                setGravity(gravity);
                defAttrs.recycle();
            }

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlidingUpPanelLayout);

            if (typedArray != null) {
                mPanelHeight = typedArray.getDimensionPixelSize(R.styleable.SlidingUpPanelLayout_panelHeight, -1);
                mOverlayContent = typedArray.getBoolean(R.styleable.SlidingUpPanelLayout_overlay, DEFAULT_OVERLAY_FLAG);
                mSlideState = PanelState.values()[typedArray.getInt(R.styleable.SlidingUpPanelLayout_initialState, DEFAULT_SLIDE_STATE.ordinal())];

                typedArray.recycle();
            }
        }

        final float density = context.getResources().getDisplayMetrics().density;
        if (mPanelHeight == -1) {
            mPanelHeight = (int) (DEFAULT_PANEL_HEIGHT * density + 0.5f);
        }

        //FIXME ViewGroup默认不执行onDraw()方法，不需要重写onDraw，为何设置false
        setWillNotDraw(false);
    }

    public void setGravity(int gravity) {
        if (gravity != Gravity.TOP && gravity != Gravity.BOTTOM) {
            throw new IllegalArgumentException("gravity must be set to either top or bottom");
        }
        mIsSlidingUp = gravity == Gravity.BOTTOM;
        if (!mFirstLayout) {
            requestLayout();
        }
    }

    public void setDragView(View dragView) {
        if (mDragView != null) {
            mDragView.setOnClickListener(null);
        }
        mDragView = dragView;
        if (mDragView != null) {
            mDragView.setClickable(true);
            mDragView.setFocusable(false);
            mDragView.setFocusableInTouchMode(false);
            mDragView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY && widthMode != MeasureSpec.AT_MOST) {
            throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
        }
        if (heightMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.AT_MOST) {
            throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
        }

        int childCount = getChildCount();

        if (childCount != 2) {
            throw new IllegalStateException("Sliding up panel layout must have exactly 2 children!");
        }

        mMainView = getChildAt(0);
        mSlideableView = getChildAt(1);
        if (mDragView == null) {
            setDragView(mSlideableView);
        }

        // TODO If the sliding panel is not visible, then put the whole view in the hidden state
        if (mSlideableView.getVisibility() != VISIBLE) {
            mSlideState = PanelState.HIDDEN;
        }

        int layoutWidth  = widthSize - getPaddingLeft() - getPaddingRight();
        int layoutHeight = heightSize - getPaddingTop() - getPaddingBottom();

        //测量子View
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE && i == 0) {
                continue;
            }

            int width = layoutWidth;
            int height = layoutHeight;
            if (child == mMainView) {
                width -= layoutParams.leftMargin + layoutParams.rightMargin;
            } else if (child == mSlideableView) {
                height -= layoutParams.topMargin;
            }

            int childWidthSpec;
            if (layoutParams.width == LayoutParams.WRAP_CONTENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
            } else if (layoutParams.width == LayoutParams.MATCH_PARENT) {
                childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            } else {
                childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            }

            int childHeightSpec;
            if (layoutParams.height == LayoutParams.WRAP_CONTENT) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
            } else {
                if (layoutParams.weight > 0 && layoutParams.weight < 1) {
                    height = (int) (height * layoutParams.weight);
                } else if (layoutParams.height != LayoutParams.MATCH_PARENT) {
                    height = layoutParams.height;
                }
                childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthSpec, childHeightSpec);

            if (child == mSlideableView) {
                //计算滑动面板可滑动尺寸
                mSlideRange = mSlideableView.getMeasuredHeight() - mPanelHeight;
            }
        }

        //存储测量得到的宽度和高度
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int childCount = getChildCount();

        if (mFirstLayout) {
            switch (mSlideState) {
                case EXPANDED:
                    mSlideOffset = 1.0f;
                    break;
                case ANCHORED:
                    mSlideOffset = mAnchorPoint;
                    break;
                case HIDDEN:
                    int newTop = computePanelTopPosition(0f) + (mIsSlidingUp ? +mPanelHeight : -mPanelHeight);
                    mSlideOffset = computeSlideOffset(newTop);
                    break;
                default:
                    mSlideOffset = 0f;
            }
        }

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            // FIXME Always layout the sliding view on the first layout
            if (child.getVisibility() == GONE && (i == 0 || mFirstLayout)) {
                continue;
            }

            int childHeight = child.getMeasuredHeight();
            int childTop = paddingTop;

            if (child == mSlideableView) {
                childTop = computePanelTopPosition(mSlideOffset);
            }

            if (!mIsSlidingUp) {
                if (child == mMainView && !mOverlayContent) {
                    childTop = computePanelTopPosition(mSlideOffset) + mSlideableView.getMeasuredHeight();
                }
            }
            int childBottom = childTop + childHeight;
            int childLeft = paddingLeft + lp.leftMargin;
            int childRight = childLeft + child.getMeasuredWidth();

            child.layout(childLeft, childTop, childRight, childBottom);
        }

        if (mFirstLayout) {
            // TODO updateObscuredViewVisibility();
        }
        // TODO applyParallaxForCurrentSlideOffset();

        mFirstLayout = false;
    }

    /**
     * 根据 slideOffset 计算滑动面板的顶部位置
     */
    private int computePanelTopPosition(float slideOffset) {
        int slidingViewHeight = mSlideableView != null ? mSlideableView.getMeasuredHeight() : 0;
        int slidePixelOffset = (int) (slideOffset * mSlideRange);
        return mIsSlidingUp
                ? getMeasuredHeight() - getPaddingBottom() - mPanelHeight - slidePixelOffset
                : getPaddingTop() + mPanelHeight + slidePixelOffset - slidingViewHeight;
    }

    /**
     * 根据滑动面板顶部位置计算 slideOffset
     */
    private float computeSlideOffset(int topPosition) {
        //计算面板收起的时候顶部位置(offset = 0)
        int topBoundCollapsed = computePanelTopPosition(0f);

        //根据收起时面板的顶部位置和所需的新顶部位置确定面板偏移比例
        return mIsSlidingUp
                ? (float) (topBoundCollapsed - topPosition) / mSlideRange
                : (float) (topPosition - topBoundCollapsed) / mSlideRange;
    }

    //============================自定义ViewGroup LayoutParams============================
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MarginLayoutParams ?
                new LayoutParams((MarginLayoutParams) p) : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        private static final int[] ATTRS = new int[]{
                android.R.attr.layout_weight
        };

        public float weight = 0;

        public LayoutParams() {
            super(MATCH_PARENT, MATCH_PARENT);
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray typedArray = c.obtainStyledAttributes(attrs, ATTRS);
            if (typedArray != null) {
                this.weight = typedArray.getFloat(0, 0);
                typedArray.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.weight = weight;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }
    }
}
