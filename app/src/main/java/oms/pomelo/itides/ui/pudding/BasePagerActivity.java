package oms.pomelo.itides.ui.pudding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import oms.pomelo.itides.R;
import oms.pomelo.itides.ui.view.BasePagerContainerView;

/**
 * NAME: Lay
 * DATE: 2019-07-31
 */
public abstract class BasePagerActivity extends AppCompatActivity implements View.OnClickListener {

    protected FrameLayout mRootView;
    protected ViewPager mRootViewPager;

    private float downY;
    private float startY;
    private boolean isClosed = false;
    private boolean getDownY = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_pager);

        //StatusBarUtil.setTransparentForImageView(this, findViewById(R.id.cl_base_pager_toolbar_layout));

        mRootView = findViewById(R.id.fl_base_pager_root_view);

        BasePagerContainerView containerView = findViewById(R.id.container_view);

        View settingButton = containerView.findViewById(R.id.ib_base_pager_toolbar_setting);
        settingButton.setOnClickListener(setSettingButtonClickListener());

        containerView.findViewById(R.id.ib_base_pager_toolbar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRootViewPager = containerView.findViewById(R.id.vp_root_view_pager);

        containerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:

                        if (event.getRawY() - downY > 10 || isClosed) {
                            if (!getDownY) {
                                getDownY = true;
                                downY = event.getRawY();
                                startY = v.getTranslationY();
                            }
                            float offsetY = event.getRawY() - downY;
                            if (v.getHeight() - offsetY - startY > 300) {
                                v.setTranslationY(offsetY + startY);
                            }
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        getDownY = false;
                        downY = 0;
                        if ((v.getHeight() >> 1) < v.getTranslationY()) {
                            v.setTranslationY(v.getHeight() - 300);
                            isClosed = true;
                        } else {
                            v.setTranslationY(0);
                            isClosed = false;
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + event.getAction());
                }
                return false;
            }

        });


        initLayout();
    }

    @Override
    public void onClick(View v) {

    }

    protected abstract void initLayout();

    @NonNull
    protected abstract View.OnClickListener setSettingButtonClickListener();
}
