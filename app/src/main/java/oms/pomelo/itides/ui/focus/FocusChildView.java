package oms.pomelo.itides.ui.focus;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import oms.pomelo.itides.R;

/**
 * NAME: Lay
 * DATE: 2019-12-07
 */
public class FocusChildView extends LinearLayout {

    private View rootView;

    public FocusChildView(Context context) {
        this(context, null);
    }

    public FocusChildView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusChildView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(@NonNull Context context) {
        rootView = LayoutInflater.from(context).inflate(R.layout.weiget_focus_child_view, this, false);

        addView(rootView);
    }

    public FocusChildView setText(@NonNull CharSequence text) {
        TextView textView = rootView.findViewById(R.id.tv_focus_item_title);
        textView.setText(text);

        return this;
    }

    public void setBackgroundColor(@ColorInt int color) {
        View view = rootView.findViewById(R.id.cl_focus_item_root_view);
        view.setBackgroundColor(color);
    }
}
