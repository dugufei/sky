package jc.sky.modules.systemuihider;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author sky
 * @version 版本
 */
public abstract class SKYSystemUiHider {
    public static final int FLAG_LAYOUT_IN_SCREEN_OLDER_DEVICES = 0x1;


    public static final int FLAG_FULLSCREEN = 0x2;


    public static final int FLAG_HIDE_NAVIGATION = FLAG_FULLSCREEN | 0x4;


    protected AppCompatActivity mActivity;

    protected View mAnchorView;

    protected int mFlags;

    protected OnVisibilityChangeListener mOnVisibilityChangeListener = sDummyListener;


    public static SKYSystemUiHider getInstance(AppCompatActivity activity, View anchorView,
                                               int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new SKYSystemUiHiderHoneycomb(activity, anchorView, flags);
        } else {
            return new SKYSystemUiHiderBase(activity, anchorView, flags);
        }
    }

    protected SKYSystemUiHider(AppCompatActivity activity, View anchorView, int flags) {
        mActivity = activity;
        mAnchorView = anchorView;
        mFlags = flags;
    }


    public abstract void setup();


    public abstract boolean isVisible();

    public abstract void hide();

    public abstract void show();


    public void toggle() {
        if (isVisible()) {
            hide();
        } else {
            show();
        }
    }

    public void setOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        if (listener == null) {
            listener = sDummyListener;
        }

        mOnVisibilityChangeListener = listener;
    }

    private static OnVisibilityChangeListener sDummyListener = new OnVisibilityChangeListener() {
        @Override
        public void onVisibilityChange(boolean visible) {
        }
    };


    public interface OnVisibilityChangeListener {

        void onVisibilityChange(boolean visible);
    }
}