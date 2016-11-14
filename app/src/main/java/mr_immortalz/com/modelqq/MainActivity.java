package mr_immortalz.com.modelqq;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import mr_immortalz.com.modelqq.been.Info;
import mr_immortalz.com.modelqq.custom.CircleView;
import mr_immortalz.com.modelqq.custom.CustomViewPager;
import mr_immortalz.com.modelqq.custom.RadarViewGroup;
import mr_immortalz.com.modelqq.utils.FixedSpeedScroller;
import mr_immortalz.com.modelqq.utils.LogUtil;
import mr_immortalz.com.modelqq.utils.ZoomOutPageTransformer;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    ViewpagerAdapter mAdapter;
    private int[] mImgs = {R.drawable.len, R.drawable.leo, R.drawable.lep,
            R.drawable.leq, R.drawable.ler, R.drawable.les, R.drawable.mln, R.drawable.mmz, R.drawable.mna,
            R.drawable.mnj, R.drawable.leo, R.drawable.leq, R.drawable.les, R.drawable.lep};
    private String[] mNames = {"15", "25", "45", "25", "59", "60", "87", "99", "10", "23", "124", "100", "20", "85"};
    private double[] mDistance = {15, 25, 45, 25, 59, 60, 87, 99, 10, 23, 124, 100, 20, 85};
    private int mPosition;
    private FixedSpeedScroller scroller;
    private SparseArray<Info> mDatas = new SparseArray<>();
    private SparseArray<Info> tempmDatas = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar radar = (SeekBar) findViewById(R.id.pradarProgress);
        initView();
        initData(0);
        ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });

        viewPager.setOffscreenPageLimit(mImgs.length);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(this);
        radar.setProgress(150);
        setViewPagerSpeed(250);
        for (int i = 0; i < mDatas.size(); i++) {
            tempmDatas.put(i, mDatas.get(i));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                radarViewGroup.setDatas(mDatas);
                mAdapter = new ViewpagerAdapter();
                viewPager.setAdapter(mAdapter);
            }
        }, 1500);

        radarViewGroup.setiRadarClickListener(this);

        radar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                // here is distance progress
                //Toast toast=Toast.makeText(getApplicationContext(),"Hello Javatpoint"+progress,Toast.LENGTH_SHORT);
                //toast.setMargin(50,50);
                //toast.show();
                ///  initView();




                int childCount = radarViewGroup.getChildCount();
                //首先放置雷达扫描图
                View view = findViewById(R.id.id_scan_circle);
                if (view != null) {
                    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                }
                //放置雷达图中需要展示的item圆点
                for (int i = 0; i < childCount; i++) {
                    final int j = i;
                    final View child = radarViewGroup.getChildAt(i);
                    if (child.getId() == R.id.id_scan_circle) {
                        continue;
                    }

                    CircleView circleView = ((CircleView) child);
                    float distance = circleView.getDistance();
                    if ((distance * 100) > progress) {
                        circleView.setVisibility(View.GONE);
                    } else {
                        circleView.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tempmDatas = new SparseArray<Info>();
                int k = 0;
                for (int i = 0; i < mDatas.size(); i++) {

                    float progress=((float)seekBar.getProgress());
                    float distance=mDatas.get(i).getDistance()*100;


                    if ( distance<= progress) {
                        tempmDatas.put(k, mDatas.get(i));
                        k++;
                    }
                }

                viewPager = (CustomViewPager) findViewById(R.id.vp);
                mAdapter = new ViewpagerAdapter();
                viewPager.setAdapter(mAdapter);

            }
        });
    }

    private void initData(int progress) {
        //get data from server
        for (int i = 0; i < mImgs.length; i++) {
            Info info = new Info();
            info.setPortraitId(mImgs[i]);
            info.setAge(((int) Math.random() * 25 + 16) + "");
            info.setName(mNames[i]);
            info.setSex(i % 3 == 0 ? false : true);
            info.setDistance((float) (mDistance[i] / 100));
            mDatas.put(i, info);
        }
    }

    private void initView() {

        if (viewPager != null) {
            viewPager = null;
        }
        if (radarViewGroup != null) {
            radarViewGroup = null;
        }
        if (ryContainer != null) {
            ryContainer = null;
        }

        viewPager = (CustomViewPager) findViewById(R.id.vp);
        radarViewGroup = (RadarViewGroup) findViewById(R.id.radar);
        ryContainer = (RelativeLayout) findViewById(R.id.ry_container);
    }

    /**
     * 设置ViewPager切换速度
     *
     * @param duration
     */
    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(MainActivity.this, new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        radarViewGroup.setCurrentShowItem(position);
        LogUtil.m("当前位置 " + mPosition);
        LogUtil.m("速度 " + viewPager.getSpeed());
        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
        if (viewPager.getSpeed() < -1800) {
            viewPager.setCurrentItem(mPosition + 2);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        } else if (viewPager.getSpeed() > 1800 && mPosition > 0) {
            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
            viewPager.setCurrentItem(mPosition - 1);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }


    class ViewpagerAdapter extends PagerAdapter {

        float progress;


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {


            final Info info = tempmDatas.get(position);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.viewpager_layout, null);


            //设置一大堆演示用的数据，麻里麻烦~~

            ImageView ivPortrait = (ImageView) view.findViewById(R.id.iv);
            ImageView ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
            tvName.setText(info.getName());
            tvDistance.setText(info.getDistance() + "km");
            ivPortrait.setImageResource(info.getPortraitId());
            if (info.getSex()) {
                ivSex.setImageResource(R.drawable.girl);
            } else {
                ivSex.setImageResource(R.drawable.boy);
            }
            ivPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "这是 " + info.getName() + " >.<", Toast.LENGTH_SHORT).show();
                }
            });


            container.addView(view);


            return view;
        }

        @Override
        public int getCount() {
            return tempmDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}
