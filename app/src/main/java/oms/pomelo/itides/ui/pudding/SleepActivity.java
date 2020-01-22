package oms.pomelo.itides.ui.pudding;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import oms.pomelo.itides.R;

/**
 * NAME: Lay
 * DATE: 2019-07-31
 */
public final class SleepActivity extends BasePagerActivity {

    public static void start(Activity context, int requestCode) {
        Intent starter = new Intent(context, SleepActivity.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void initLayout() {
        View controlView = LayoutInflater.from(this).inflate(R.layout.layout_sleep_control, mFrameView, true);
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
