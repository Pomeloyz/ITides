package oms.pomelo.itides.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import oms.pomelo.itides.R;

/**
 * NAME: Lay
 * DATE: 2019-12-08
 */
public class BasePagerContainerView extends LinearLayout {

    private int startX;
    private int startY;

    public BasePagerContainerView(Context context) {
        this(context, null);
    }

    public BasePagerContainerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePagerContainerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_base_pager_container, this, false);
        addView(view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();

            case MotionEvent.ACTION_MOVE:
                int dX = (int) (event.getRawX() - startX);
                int dY = (int) (event.getRawY() - startY);
                if (Math.abs(dX) < Math.abs(dY)) { //上下滑动
                    return true;
                } else { //左右滑动
                    return false;
                }

            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }
}
