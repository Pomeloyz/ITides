package oms.pomelo.itides.ui.pudding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import oms.pomelo.itides.R;
import oms.pomelo.itides.ui.daliy.DailyContract;
import oms.pomelo.itides.ui.daliy.DailyInfo;
import oms.pomelo.itides.ui.daliy.DailyPresenter;
import oms.pomelo.itides.utils.glide.GrayscaleTransformation;

/**
 * NAME: Lay
 * DATE: 2019-07-31
 */
public final class BreathActivity extends BasePagerActivity implements DailyContract.DailyView {

    private DailyPresenter mDailyPresenter;

    public static void start(Activity context, int requestCode) {
        Intent starter = new Intent(context, BreathActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void initLayout() {
        View controlView = LayoutInflater.from(this).inflate(R.layout.layout_breath_control, mFrameView, true);

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
        Glide.with(BreathActivity.this)
                .load(dailyInfo.getPic_url())
                .transform(new GrayscaleTransformation())
                .into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                mRootViewPager.setBackground(resource);
            }
        });
    }

    @Override
    public void getDailyError(String result) {
        Toast.makeText(BreathActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
