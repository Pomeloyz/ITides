package oms.pomelo.module.slidingpanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SlidingUpPanelLayout extends ViewGroup {

    private static final String TAG = SlidingUpPanelLayout.class.getSimpleName();

    /**
     * 默认的滑动面板初始状态露出的尺寸
     */
    private static final int DEFAULT_PANEL_HEIGHT = 70; // dp;
    /**
     * 认定为速滑的最小速度
     */
    private static final int DEFAULT_MIN_FLING_VELOCITY = 400; // dips per second
    /**
     * 默认可设置的attrs
     */
    private static final int[] DEFAULT_ATTRS = new int[] {
            android.R.attr.gravity
    };

    /**
     * 认定为速滑的最小速度
     */
    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;

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
     * 滑动面板内部需要处理滚动时，或者有其他阻止拖动滑动面板的事件发生时为 True
     */
    private boolean mIsUnableToDrag;

    /**
     * 启用/禁用面板滑动功能的标志
     */
    private boolean mIsTouchEnabled;

    private float mPrevMotionX;
    private float mPrevMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsScrollableViewHandlingTouch = false;

    private final List<PanelSlideListener> mPanelSlideListeners = new CopyOnWriteArrayList<>();
    private View.OnClickListener mFadeOnClickListener;

    private final ViewDragHelper mDragHelper;

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

    /**
     * 滑动面板从非滑动状态变为正在滑动状态时，存储那个非滑动状态
     */
    private PanelState mLastNotDraggingSlideState = DEFAULT_SLIDE_STATE;

    /**
     * 用于监听滑动面板滑动事件的监听器
     */
    public interface PanelSlideListener {
        /**
         * 面板滑动时回调
         *
         * @param panel       滑动面板View
         * @param slideOffset 滑动面板的新 slideOffset 值，[0 ~ 1]
         */
        public void onPanelSlide(View panel, float slideOffset);

        /**
         * 滑动面板状态更改时调用
         */
        public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState);
    }

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

        mDragHelper = ViewDragHelper.create(this, 0.5f, new DragHelperCallback());
        mDragHelper.setMinVelocity(mMinFlingVelocity * density);

        mIsTouchEnabled = true;
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

    /**
     * 设置滑动启用标志
     */
    public void setTouchEnabled(boolean enabled) {
        mIsTouchEnabled = enabled;
    }

    /**
     * 设置滑动面板初始状态露出的尺寸(pixels)
     */
    public void setPanelHeight(int pixels) {
        if (getPanelHeight() == pixels) return;

        mPanelHeight = pixels;
        if (!mFirstLayout) {
            requestLayout();
        }

        if (getPanelState() == PanelState.COLLAPSED) {
            smoothSlideTo(0, 0); //to Bottom
            invalidate();
        }
    }

    public int getPanelHeight() {
        return mPanelHeight;
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
                    if (!isEnabled() || !isTouchEnabled()) {
                        return;
                    }
                    if (mSlideState != PanelState.EXPANDED && mSlideState != PanelState.ANCHORED) {
                        if (mAnchorPoint < 1.0f) {
                            setPanelState(PanelState.ANCHORED);
                        } else {
                            setPanelState(PanelState.EXPANDED);
                        }
                    } else {
                        setPanelState(PanelState.COLLAPSED);
                    }
                }
            });
        }
    }

    /**
     * Adds a panel slide listener
     *
     * @param listener
     */
    public void addPanelSlideListener(PanelSlideListener listener) {
        synchronized (mPanelSlideListeners) {
            mPanelSlideListeners.add(listener);
        }
    }

    /**
     * Removes a panel slide listener
     *
     * @param listener
     */
    public void removePanelSlideListener(PanelSlideListener listener) {
        synchronized (mPanelSlideListeners) {
            mPanelSlideListeners.remove(listener);
        }
    }

    void setAllChildrenVisible() {
        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == INVISIBLE) {
                child.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
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

        mFirstLayout = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果滑动面板内部需要处理滑动，则不拦截
        if (mIsScrollableViewHandlingTouch || !isTouchEnabled()) {
            return false;
        }

        int action = ev.getAction();
        float x = ev.getX();
        float y = ev.getY();
        float adx = Math.abs(x - mInitialMotionX);
        float ady = Math.abs(y - mInitialMotionY);
        int dragSlop = mDragHelper.getTouchSlop();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsUnableToDrag = false;
                mInitialMotionX = x;
                mInitialMotionY = y;
                if (!isViewUnder(mDragView, (int) x, (int) y)) {
                    mDragHelper.cancel();
                    mIsUnableToDrag = true;
                    return false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // 不拦截左右滑动
                if (ady > dragSlop && adx > ady) {  // TODO 对于斜率小于45°和大于135°的手势，应视为左右滑动
                    mDragHelper.cancel();
                    mIsUnableToDrag = true;
                    return false;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //当滑动面板正在被手指拖动时
                if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING) {
                    mDragHelper.processTouchEvent(ev);
                    return true;
                }
                // 在滑动面板收起的情况下，若mFadeOnClickListener不为空，在这里调用点击"主页面"事件
                if (ady <= dragSlop && adx <= dragSlop
                        && mSlideOffset > 0
                        && !isViewUnder(mSlideableView, (int) mInitialMotionX, (int) mInitialMotionY)
                        && mFadeOnClickListener != null) {
                    playSoundEffect(SoundEffectConstants.CLICK);
                    mFadeOnClickListener.onClick(this);
                }
                break;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || !isTouchEnabled()) {
            return super.onTouchEvent(event);
        }
        // FIXME 这里会抛异常吗?
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        if (!isEnabled() || !isTouchEnabled() || (mIsUnableToDrag && action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.abort();
            return super.dispatchTouchEvent(ev);
        }

        final float x = ev.getX();
        final float y = ev.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            mIsScrollableViewHandlingTouch = false;
            mPrevMotionX = x;
            mPrevMotionY = y;

        } else if (action == MotionEvent.ACTION_MOVE) {
            float dx = x - mPrevMotionX;
            float dy = y - mPrevMotionY;
            mPrevMotionX = x;
            mPrevMotionY = y;

            if (Math.abs(dx) > Math.abs(y)) {
                //忽略水平滑动
                return super.dispatchTouchEvent(ev);
            }
            // TODO 重点：当触摸位置不在mScrollableView内时，将事件传递到dragView (即不拦截)
            //if (!isViewUnder(mScrollableView))

            if (dy * (mIsSlidingUp ? 1 : -1) > 0) { //收起
                // TODO 先处理 mScrollableView 的情况

                // 判断滑动面板内是否之前接收到并正在处理事件
                if (mIsScrollableViewHandlingTouch) {
                    // 发送Up事件到子View
                    MotionEvent up = MotionEvent.obtain(ev);
                    up.setAction(MotionEvent.ACTION_CANCEL);
                    super.dispatchTouchEvent(up);
                    up.recycle();

                    // 发送Down事件到dragPanel
                    ev.setAction(MotionEvent.ACTION_DOWN);
                }
                mIsScrollableViewHandlingTouch = false;
                return this.onTouchEvent(ev);

            } else if (dy * (mIsSlidingUp ? 1 : -1) < 0) { //展开
                // 如果滑动面板不处于完全展开状态，在这里传递事件到dragPanel处理
                if (mSlideOffset < 1.0f) {
                    mIsScrollableViewHandlingTouch = false;
                    return this.onTouchEvent(ev);
                }
                // 判断滑动面板内是否之前接收到并正在处理事件
                if (!mIsScrollableViewHandlingTouch && mDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING) {
                    mDragHelper.cancel();
                    // 发送Down事件到dragPanel
                    ev.setAction(MotionEvent.ACTION_DOWN);
                }

                mIsScrollableViewHandlingTouch = true;
                return super.dispatchTouchEvent(ev);
            }

        } else if (action == MotionEvent.ACTION_UP) {
            if (mIsScrollableViewHandlingTouch) {
                // TODO mDragHelper.setDragState(ViewDragHelper.STATE_IDLE);
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean isTouchEnabled() {
        return mIsTouchEnabled && mSlideableView != null && mSlideState != PanelState.HIDDEN;
    }

    /**
     * 判断当前触摸的点是否在view内
     */
    private boolean isViewUnder(@Nullable View view, int x, int y) {
        if (view == null)
            return false;

        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);

        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);

        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0]
                && screenX < viewLocation[0] + view.getWidth()
                && screenY >= viewLocation[1]
                && screenY < viewLocation[1] + view.getHeight();
    }

    public PanelState getPanelState() {
        return mSlideState;
    }

    public void setPanelState(PanelState state) {
        if(mDragHelper.getViewDragState() == ViewDragHelper.STATE_SETTLING){
            Log.d(TAG, "View is settling. Aborting animation.");
            mDragHelper.abort();
        }

        if (state == null || state == PanelState.DRAGGING) {
            throw new IllegalArgumentException("Panel state cannot be null or DRAGGING.");
        }
        if (!isEnabled()
                || (!mFirstLayout && mSlideableView == null)
                || state == mSlideState
                || mSlideState == PanelState.DRAGGING) return;

        if (mFirstLayout) {
            setPanelStateInternal(state);
        } else {
            if (mSlideState == PanelState.HIDDEN) {
                mSlideableView.setVisibility(View.VISIBLE);
                requestLayout();
            }
            switch (state) {
                case ANCHORED:
                    smoothSlideTo(mAnchorPoint, 0);
                    break;
                case COLLAPSED:
                    smoothSlideTo(0, 0);
                    break;
                case EXPANDED:
                    smoothSlideTo(1.0f, 0);
                    break;
                case HIDDEN:
                    int newTop = computePanelTopPosition(0.0f) + (mIsSlidingUp ? +mPanelHeight : -mPanelHeight);
                    smoothSlideTo(computeSlideOffset(newTop), 0);
                    break;
            }
        }
    }

    private void setPanelStateInternal(PanelState state) {
        if (mSlideState == state) return;
        PanelState oldState = mSlideState;
        mSlideState = state;
        dispatchOnPanelStateChanged(this, oldState, state);
    }

    void dispatchOnPanelSlide(View panel) {
        synchronized (mPanelSlideListeners) {
            for (PanelSlideListener l : mPanelSlideListeners) {
                l.onPanelSlide(panel, mSlideOffset);
            }
        }
    }

    private void dispatchOnPanelStateChanged(View panel, PanelState perviousState, PanelState newState) {
        synchronized (mPanelSlideListeners) {
            for (PanelSlideListener l : mPanelSlideListeners) {
                l.onPanelStateChanged(panel, perviousState, newState);
            }
        }
        sendAccessibilityEvent(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
    }

    boolean smoothSlideTo(float slideOffset, int velocity) {
        if (!isEnabled() || mSlideableView == null) {
            // Nothing to do.
            return false;
        }

        int panelTop = computePanelTopPosition(slideOffset);

        if (mDragHelper.smoothSlideViewTo(mSlideableView, mSlideableView.getLeft(), panelTop)) {
            setAllChildrenVisible();
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            if (!isEnabled()) {
                mDragHelper.abort();
                return;
            }

            ViewCompat.postInvalidateOnAnimation(this);
        }
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

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return !mIsUnableToDrag && child == mSlideableView;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            if (mDragHelper != null && mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                mSlideOffset = computeSlideOffset(mSlideableView.getTop());

                if (mSlideOffset == 1) {
                    setPanelStateInternal(PanelState.EXPANDED);
                } else if (mSlideOffset == 0) {
                    setPanelStateInternal(PanelState.COLLAPSED);
                } else if (mSlideOffset < 0) {
                    setPanelStateInternal(PanelState.HIDDEN);
                    mSlideableView.setVisibility(INVISIBLE);
                } else {
                    setPanelStateInternal(PanelState.ANCHORED);
                }
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int collapsedTop = computePanelTopPosition(0f);
            int expandedTop = computePanelTopPosition(1.0f);
            if (mIsSlidingUp) {
                return Math.min(collapsedTop, Math.max(top, expandedTop));
            } else {
                return Math.min(expandedTop, Math.max(top, collapsedTop));
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            onPanelDragged(top);
            invalidate();
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            setAllChildrenVisible();
        }

        /**
         * 手指释放时回调
         */
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            int target = 0;

            // 根据初始状态的不同，将趋向目标方向的y值转为正数
            float direction = mIsSlidingUp ? -yvel : yvel;

            if (direction > 0 && mSlideOffset <= mAnchorPoint) {
                // 上拉 -> 展开并停在锚点
                target = computePanelTopPosition(mAnchorPoint);
            } else if (direction > 0 && mSlideOffset > mAnchorPoint) {
                // 上拉超过锚点 -> 完全展开
                target = computePanelTopPosition(1.0f);
            } else if (direction < 0 && mSlideOffset >= mAnchorPoint) {
                // 下拉 -> 收起并停在锚点
                target = computePanelTopPosition(mAnchorPoint);
            } else if (direction < 0 && mSlideOffset < mAnchorPoint) {
                // 下拉超过锚点 -> 收起
                target = computePanelTopPosition(0.0f);
            } else if (mSlideOffset >= (1.f + mAnchorPoint) / 2) {
                // 速度为0，且离锚点足够远 => 展开
                target = computePanelTopPosition(1.0f);
            } else if (mSlideOffset >= mAnchorPoint / 2) {
                // 速度为0，且离锚点很近 => 停在锚点
                target = computePanelTopPosition(mAnchorPoint);
            } else {
                // 停在底部
                target = computePanelTopPosition(0.0f);
            }

            if (mDragHelper != null) {
                mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), target);
            }
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return mSlideRange;
        }
    }

    private void onPanelDragged(int newTop) {
        if (mSlideState != PanelState.DRAGGING) {
            mLastNotDraggingSlideState = mSlideState;
        }
        setPanelStateInternal(PanelState.DRAGGING);
        mSlideOffset = computeSlideOffset(newTop);

        dispatchOnPanelSlide(mSlideableView);
        LayoutParams lp = (LayoutParams) mMainView.getLayoutParams();
        int defaultHeight = getHeight() - getPaddingTop() - getPaddingBottom() - mPanelHeight;

        if (mSlideOffset <= 0 && !mOverlayContent) {
            lp.height = mIsSlidingUp ? (newTop - getPaddingBottom()) : (getHeight() - getPaddingBottom() - mSlideableView.getMeasuredHeight() - newTop);
            if (lp.height == defaultHeight) {
                lp.height = LayoutParams.MATCH_PARENT;
            }
            mMainView.requestLayout();
        } else if (lp.height != LayoutParams.MATCH_PARENT && !mOverlayContent) {
            lp.height = LayoutParams.MATCH_PARENT;
            mMainView.requestLayout();
        }
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
