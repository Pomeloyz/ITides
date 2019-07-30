package oms.pomelo.itides.ui.daliy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import oms.pomelo.itides.R;

public class MainActivity extends AppCompatActivity implements DailyContract.DailyView {

    private ImageView ivDailyBg;
    private TextView tvDaily;
    private TextView tvTips;
    private Calendar mCalendar;
    private DailyPresenter mDailyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();
        initPresenter();
        initData();
    }

    private void initView() {
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
        Glide.with(MainActivity.this).load(dailyInfo.getOrigin_img_urls().get(0)).into(ivDailyBg);
        tvDaily.setText(dailyInfo.getTranslation());
    }

    @Override
    public void getDailyError(String result) {
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    }
}
