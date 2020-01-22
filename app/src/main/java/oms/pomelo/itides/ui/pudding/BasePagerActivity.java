package oms.pomelo.itides.ui.pudding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import oms.pomelo.itides.R;
import oms.pomelo.itides.ui.view.BasePagerContainerView;
import oms.pomelo.itides.utils.StatusBarUtil;

/**
 * NAME: Lay
 * DATE: 2019-07-31
 */
public abstract class BasePagerActivity extends AppCompatActivity implements View.OnClickListener {

    protected FrameLayout mFrameView;
    protected ViewPager mRootViewPager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base_pager);

        StatusBarUtil.setTransparentForImageView(this, findViewById(R.id.cl_base_pager_toolbar_layout));

        BasePagerContainerView containerView = findViewById(R.id.base_container_view);

        View settingButton = containerView.findViewById(R.id.ib_base_pager_toolbar_setting);
        settingButton.setOnClickListener(setSettingButtonClickListener());

        containerView.findViewById(R.id.ib_base_pager_toolbar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFrameView = containerView.findViewById(R.id.fl_base_pager_frame);
        mRootViewPager = containerView.findViewById(R.id.vp_root_view_pager);

        initLayout();
    }

    @Override
    public void onClick(View v) {

    }

    protected abstract void initLayout();

    @NonNull
    protected abstract View.OnClickListener setSettingButtonClickListener();
}
