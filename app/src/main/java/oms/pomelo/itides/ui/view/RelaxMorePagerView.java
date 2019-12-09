package oms.pomelo.itides.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import oms.pomelo.itides.R;

/**
 * NAME: Lay
 * DATE: 2019-12-08
 */
public class RelaxMorePagerView extends LinearLayout {

    public RelaxMorePagerView(Context context) {
        this(context, null);
    }

    public RelaxMorePagerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelaxMorePagerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(@NonNull Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_relax_more_pager, this, false);
        addView(view);
    }
}
