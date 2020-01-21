package oms.pomelo.itides.ui.daliy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import oms.pomelo.itides.R;
import oms.pomelo.itides.ui.pudding.BreathActivity;
import oms.pomelo.itides.ui.pudding.FocusActivity;
import oms.pomelo.itides.ui.pudding.RelaxActivity;
import oms.pomelo.itides.ui.pudding.SleepActivity;
import oms.pomelo.itides.utils.StatusBarUtil;

public class MainActivity extends AppCompatActivity implements DailyContract.DailyView {

    private ImageView ivDailyBg;
    private TextView tvDaily;
    private TextView tvTips;
    private Calendar mCalendar;
    private DailyPresenter mDailyPresenter;
    private ConstraintLayout mContentView;

    private static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();
        initPresenter();
        initData();

        StatusBarUtil.setTransparentForImageView(this, mContentView);
    }

    private void initView() {
        mContentView = findViewById(R.id.cl_content_view);
        ivDailyBg = findViewById(R.id.ivDailyBg);
        tvDaily = findViewById(R.id.tvDaily);
        tvTips = findViewById(R.id.tvTips);
    }

    private void initPresenter() {
        mDailyPresenter = new DailyPresenter(this);
        mDailyPresenter.attachView(this);
        mDailyPresenter.getDaily();
    }

    private void initData() {
        mCalendar = Calendar.getInstance();

        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 6 && hour < 11) {
            tvTips.setText("早上好");
        } else if (hour >= 11 && hour < 13) {
            tvTips.setText("中午好");
        } else if (hour >= 13 && hour < 18) {
            tvTips.setText("下午好");
        } else if (hour >= 18 && hour < 21) {
            tvTips.setText("傍晚了");
        } else {
            tvTips.setText("夜深了");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDailyPresenter.detachView();
    }

    @Override
    public void getDailySuccess(DailyInfo dailyInfo) {
        Glide.with(MainActivity.this).load(dailyInfo.getPic_url()).into(ivDailyBg);
        DailyInfo.ContentBean content = dailyInfo.getContent();
        tvDaily.setText(content.getZhHans().getQuote().getText());
    }

    @Override
    public void getDailyError(String result) {
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    public void toFocus(View view) {
        hideUI();
        FocusActivity.start(MainActivity.this, REQUEST_CODE);
    }

    public void toSleep(View view) {
        hideUI();
        SleepActivity.start(MainActivity.this, REQUEST_CODE);
    }

    public void toRelax(View view) {
        hideUI();
        RelaxActivity.start(MainActivity.this, REQUEST_CODE);
    }

    public void toBreath(View view) {
        hideUI();
        BreathActivity.start(MainActivity.this, REQUEST_CODE);
    }

    private void hideUI() {
        // TODO use animation
        findViewById(R.id.cl_content_view).setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            // TODO use animation
            findViewById(R.id.cl_content_view).setVisibility(View.VISIBLE);
        }
    }
}
