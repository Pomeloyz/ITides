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
import oms.pomelo.itides.model.DailyInfo;

public class MainActivity extends AppCompatActivity {

    private DailyInfoPresenter mDailyInfoPresenter;
    private ImageView ivDailyBg;
    private TextView tvDaily;
    private TextView tvTips;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        ivDailyBg = findViewById(R.id.ivDailyBg);
        tvDaily = findViewById(R.id.tvDaily);
        tvTips = findViewById(R.id.tvTips);

        mDailyInfoPresenter = new DailyInfoPresenter(this);
        mDailyInfoPresenter.bindPresenterView(mDailyInfoContract);
    }

    private void initListener() {
        mDailyInfoPresenter.getDailyInfo();
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

    private DailyInfoContract mDailyInfoContract = new DailyInfoContract() {

        @Override
        public void onSuccess(DailyInfo dailyInfo) {
            Glide.with(MainActivity.this).load(dailyInfo.getOrigin_img_urls().get(0)).into(ivDailyBg);
            tvDaily.setText(dailyInfo.getContent());
        }

        @Override
        public void onError(String result) {
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDailyInfoPresenter.release();
    }
}
