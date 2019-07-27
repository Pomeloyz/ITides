package oms.pomelo.itides.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import oms.pomelo.itides.R;
import oms.pomelo.itides.daliy.DailyInfo;
import oms.pomelo.itides.daliy.DailyInfoContract;
import oms.pomelo.itides.daliy.DailyInfoPresenter;

public class MainActivity extends AppCompatActivity {

    private DailyInfoPresenter mDailyInfoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView() {
        mDailyInfoPresenter = new DailyInfoPresenter(this);
        mDailyInfoPresenter.init();
        mDailyInfoPresenter.BindPresenterView(mDailyInfoContract);
    }

    private void initListener() {
        mDailyInfoPresenter.getDailyInfo();
    }

    private DailyInfoContract mDailyInfoContract = new DailyInfoContract() {

        @Override
        public void onSuccess(DailyInfo dailyInfo) {

        }

        @Override
        public void onError(String result) {
            Log.e("youzi", "error===" + result);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDailyInfoPresenter.release();
    }
}
