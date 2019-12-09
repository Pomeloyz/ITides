package oms.pomelo.itides.ui.pudding;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import oms.pomelo.itides.ui.adapters.BreathPagerAdapter;
import oms.pomelo.itides.ui.focus.FocusChildView;

/**
 * NAME: Lay
 * DATE: 2019-07-31
 */
public final class FocusActivity extends BasePagerActivity {

    public static void start(Activity context, int requestCode) {
        Intent starter = new Intent(context, FocusActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void initLayout() {
        if (mRootViewPager != null) {
            List<FocusChildView> list = new ArrayList<>();
            list.add(new FocusChildView(this).setText("雨天"));
            list.add(new FocusChildView(this).setText("海洋"));
            list.add(new FocusChildView(this).setText("冥想"));
            list.add(new FocusChildView(this).setText("雷雨"));

            mRootViewPager.setAdapter(new BreathPagerAdapter<>(this, list));
        }
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
}
