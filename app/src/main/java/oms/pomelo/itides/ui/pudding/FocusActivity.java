package oms.pomelo.itides.ui.pudding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

import oms.pomelo.itides.R;
import oms.pomelo.itides.ui.adapters.BreathPagerAdapter;
import oms.pomelo.itides.ui.daliy.DailyContract;
import oms.pomelo.itides.ui.daliy.DailyInfo;
import oms.pomelo.itides.ui.daliy.DailyPresenter;
import oms.pomelo.itides.ui.focus.FocusChildView;

/**
 * NAME: Lay
 * DATE: 2019-07-31
 */
public final class FocusActivity extends BasePagerActivity implements DailyContract.DailyView {

    private DailyPresenter mDailyPresenter;

    private static final int childViewCount = 4;
    private static final String[] childViewText = new String[]{"雨天", "海洋", "冥想", "雷雨"};
    private static final int[] childViewBackground = new int[]{
            Color.parseColor("#5566EEFF"), Color.parseColor("#557C8488"),
            Color.parseColor("#55F7D8A8"), Color.parseColor("#5558C5FF")};

    public static void start(Activity context, int requestCode) {
        Intent starter = new Intent(context, FocusActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void initLayout() {
        if (mRootViewPager != null) {
            List<FocusChildView> list = new ArrayList<>();

            for (int i = 0; i < childViewCount; i++) {
                FocusChildView view = new FocusChildView(this).setText(childViewText[i]);
                view.setBackgroundColor(childViewBackground[i]);

                list.add(view);
            }

            mRootViewPager.setAdapter(new BreathPagerAdapter<>(this, list));
        }

        View controlView = LayoutInflater.from(this).inflate(R.layout.layout_focus_control, mFrameView, true);

        initPresenter();
    }

    private void initPresenter() {
        mDailyPresenter = new DailyPresenter(this);
        mDailyPresenter.attachView(this);
        mDailyPresenter.getDaily();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDailyPresenter.detachView();
    }

    @NonNull
    @Override
    protected View.OnClickListener setSettingButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing
            }
        };
    }

    @Override
    public void getDailySuccess(DailyInfo dailyInfo) {
        Glide.with(FocusActivity.this).load(dailyInfo.getPic_url()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mRootViewPager.setBackground(resource);
            }
        });
    }

    @Override
    public void getDailyError(String result) {
        Toast.makeText(FocusActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
