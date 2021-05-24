package us.zoom.app.inmeetingfunction.customizedmeetingui.view.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.io.Serializable;

import us.zoom.app.R;

@SuppressLint("ViewConstructor")
public class OptionMenu extends ConstraintLayout implements Serializable {

    private OnClickListener onClickListener;
    private int drawableIco;
    private int stringId;
    private String text;

    public OptionMenu(Context context, int drawableIco, int stringId) {
        super(context);
        this.drawableIco = drawableIco;
        this.stringId = stringId;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_option_menu, this);
        init();
    }

    public OptionMenu(Context context, int drawableIco, String text) {
        super(context);
        this.drawableIco = drawableIco;
        this.text = text;
        LayoutInflater.from(getContext()).inflate(R.layout.layout_option_menu, this);
        init();
    }

    private void init() {
        @SuppressLint("InflateParams")
        ImageView menuIco = findViewById(R.id.layout_option_menu_ico);
        TextView menuText = findViewById(R.id.layout_option_menu_text);
        if (text != null) {
            menuText.setText(text);
        } else {
            menuText.setText(stringId);
        }
        menuIco.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableIco));
        setClickable(true);
        setFocusable(true);
    }

    public void hideBottomViewDivider() {
        View view = findViewById(R.id.view_divider);
        view.setVisibility(GONE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (onClickListener != null) onClickListener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setPressed(true);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (onClickListener != null) onClickListener.onClick(this);
            setPressed(false);
        } else {
            setPressed(false);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }
}
