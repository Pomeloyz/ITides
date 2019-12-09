package oms.pomelo.itides.ui.pudding;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

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
