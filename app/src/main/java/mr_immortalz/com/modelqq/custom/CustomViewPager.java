package mr_immortalz.com.modelqq.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by DELL2 on 7/27/2016.
 */
public class CustomViewPager extends ViewPager {
    private long downTime;
    private float LastX;
    private float mSpeed;
    boolean enabled=true;
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);this.enabled = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                LastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                x = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                //计算得到手指从按下到离开的滑动速度
                mSpeed = (x - LastX) * 1000 / (System.currentTimeMillis() - downTime);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

