package mr_immortalz.com.modelqq;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import mr_immortalz.com.modelqq.been.Info;
import mr_immortalz.com.modelqq.custom.CircleView;
import mr_immortalz.com.modelqq.custom.CustomViewPager;
import mr_immortalz.com.modelqq.custom.RadarViewGroup;
import mr_immortalz.com.modelqq.utils.DateUtils;
import mr_immortalz.com.modelqq.utils.FixedSpeedScroller;
import mr_immortalz.com.modelqq.utils.LogUtil;
import mr_immortalz.com.modelqq.utils.RequestResponse;
import mr_immortalz.com.modelqq.utils.ResponseUtils;
import mr_immortalz.com.modelqq.utils.Result;
import mr_immortalz.com.modelqq.utils.SyncHttpRestClient;
import mr_immortalz.com.modelqq.utils.ZoomOutPageTransformer;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    ViewpagerAdapter mAdapter;
    //    private int[] mImgs = {R.drawable.len, R.drawable.leo, R.drawable.lep,
//            R.drawable.leq, R.drawable.ler, R.drawable.les, R.drawable.mln, R.drawable.mmz, R.drawable.mna,
//            R.drawable.mnj, R.drawable.leo, R.drawable.leq, R.drawable.les, R.drawable.lep};
//    private String[] mNames = {"90","25","14","55","14","99","44","22","66","29","34","88","104","22"};
//    private double[] mDistance = {90,25,14,55,14,99,44,22,66,29,34,88,104,22};
    private int mPosition;
    private FixedSpeedScroller scroller;
    private SparseArray<Info> mDatas = new SparseArray<>();
    private SparseArray<Info> tempmDatas = new SparseArray<>();
    ArrayList<MeetupUser> UserList;
    ArrayList<MeetupRowItem> mretupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar radar = (SeekBar) findViewById(R.id.pradarProgress);
        initView();


        AsyncTask myTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                UserList=new ArrayList<MeetupUser>();
                mretupList=new ArrayList<MeetupRowItem>();
                GlobalSearchResult(0, 150, UserList, mretupList, "");
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                radarview();
                int n = mDatas.size();
                Info swap = new Info();
                for (int c = 0; c < (n - 1); c++) {
                    for (int d = 0; d < n - c - 1; d++) {
                        if (mDatas.get(d).getDistance() > mDatas.get(d + 1).getDistance()) /* For descending order use < */ {

                            swap = mDatas.get(d);
                            mDatas.setValueAt(d, mDatas.get(d + 1));
                            mDatas.setValueAt(d + 1, swap);
                        }
                    }
                }
                for (int i = 0; i < mDatas.size(); i++) {
                    tempmDatas.put(i, mDatas.get(i));
                }
                viewPager.setOffscreenPageLimit(mDatas.size());
                viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
                viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        radarViewGroup.setDatas(mDatas);

                        mAdapter = new ViewpagerAdapter();
                        viewPager.setAdapter(mAdapter);
                    }
                }, 1500);
            }


        };

        myTask.execute();


        ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });


        viewPager.addOnPageChangeListener(this);
        radar.setProgress(150);
        setViewPagerSpeed(250);


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
                    double distance = circleView.getDistance();
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

                    float progress = ((float) seekBar.getProgress());
                    double distance = mDatas.get(i).getDistance() * 100;


                    if (distance <= progress) {
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
            if (info.isMeetup0_user1()) {
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

    void GlobalSearchResult(int skip, int take, ArrayList<MeetupUser> _UserList, final ArrayList<MeetupRowItem> _mretupList, String Searchkey) {

        String url = "http://urbanx-urbanxdevelopment.azurewebsites.net/api/v1.0/";
        SyncHttpClient client = null;
        String Latitude, Longitude, Distance, Gender, SportList = "", FromAge, ToAge, DayDifference;

        if (this != null) {


            //remember screen

            UserList = _UserList;

            client = new SyncHttpClient();
            RequestParams params = new RequestParams();
            //url to hit
            url = url + "Radar/GetAllRadarMeetupsAndUsers";
            params.put("take", take);


            Latitude = "18.511801999999996";
            params.put("Latitude", Latitude);


            Longitude = "73.923231";
            params.put("Longitude", Longitude);


            Distance = "150";

            params.put("Distance", Distance + "000");


            Gender = "3";

            params.put("Gender", Gender);


            DayDifference = "90";

            params.put("DayDifference", DayDifference);


            SportList = "c69e52fe-4650-43d1-b695-8d3acbc27028,b997853e-73b1-4218-b41b-4b7e08a04607,d52ba0f4-5982-44d6-a7fa-84685d0f9d17,49e0c859-dcfa-471d-95f0-c1335b19f6ce,f7396fac-5036-49e1-a070-2ac4fb7b488d,d46fd579-f033-4dc6-bc5b-08648b0168a4,9be255c8-3312-41c3-be10-eee8e1e99a01";

            params.put("SportList", SportList);


            FromAge = "18";

            params.put("FromAge", FromAge);


            ToAge = "100";

            params.put("ToAge", ToAge);


            SyncHttpRestClient.get(url, true, "ezt0qa3gSTYc8WkNA3y_a4glmzlgvHxtJhnKQ27d5E-RBdgB6VKVK2_TKRdnv52P-M5iJyn8DU2R_qEjp4M_fHxaWm1qh0gOdz1wT9YTXL_x4YlJ0cQEJT_c-KfwHvV9dDAT3Zp7jX8hRQOqd0f6OWjaHzMnjl0WX6Ji9lBgeSTmUtlhN0_hlxQ4P3MBIAf5Kj0EEYSLrMPzcHnI4l39GH_U26j6EeWctd1HvEV9zI-kugk4Z9f6BCHqGOJfSpwELtbnjSr6HcBCYW5sei256uwLxZe8yQ3yUxTGrVwP36N7b7m1YmcW08IaDsoWXqKLEPBcD1kyAVvh0r4tMJWmm2na6GNiGrQxlo7_B9Q7RPtCCEhLLXGdflw03lsC6ptTp2vZ9XooXki5gITXy8uwn1VmZx-LhW79lNee08XmC2qbpqNPMyQhza0GOEEenMQWpG2PCavoraLkNdPk93ctNK2yBqsJzkEqMgRKU6mNNisbRoW3qVoLXO9a57D-4BFOFMZEKJ68WXV9C8KzhBl79Q", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Result result = ResponseUtils.parseJsonArrayResponseCheck(response);
                        RequestResponse reqres = result.getReqres();
                        if (reqres.getStatus() == 200) {
                            JSONObject ResponseObj = result.getObj();
                            //userlist
                            JSONArray Usersresult_array_AllCurrentLocationUsers = ResponseObj.getJSONArray("AllCurrentLocationUsers");
                            if (Usersresult_array_AllCurrentLocationUsers != null) {
                                MeetupUser meetupUser;
                                for (int i = 0; i < Usersresult_array_AllCurrentLocationUsers.length(); i++) {
                                    meetupUser = new MeetupUser();
                                    JSONObject joObject = Usersresult_array_AllCurrentLocationUsers.getJSONObject(i);
                                    if (joObject != null) {
                                        meetupUser.UserId = joObject.get("Id").toString().replace("null", "");
                                        meetupUser.ClientUserName = joObject.get("ClientUserName").toString().replace("null", "");
                                        meetupUser.Name = joObject.get("Name").toString().replace("null", "");
                                        String ThumbImgPath = joObject.get("ThumbImgPath").toString().replace("null", "");
                                        if (!ThumbImgPath.isEmpty()) {
                                            String slash = ThumbImgPath.substring(0, 1);
                                            if (slash.equals("/")) {
                                                ThumbImgPath = ThumbImgPath.substring(1);
                                            }
                                            meetupUser.ThumbImgPath = ThumbImgPath;
                                        }
                                        String ProfilePicURL = joObject.get("ProfilePicURL").toString().replace("null", "");
                                        if (!ProfilePicURL.isEmpty()) {
                                            String slash = ProfilePicURL.substring(0, 1);
                                            if (slash.equals("/")) {
                                                ProfilePicURL = ProfilePicURL.substring(1);
                                            }
                                            meetupUser.ProfilePicURL = ProfilePicURL;
                                        }
                                        String GallaryImage = joObject.get("GallaryImage").toString().replace("null", "");
                                        if (!GallaryImage.isEmpty()) {
                                            String slash = GallaryImage.substring(0, 1);
                                            if (slash.equals("/")) {
                                                GallaryImage = GallaryImage.substring(1);
                                            }
                                            meetupUser.GallaryImage = GallaryImage;
                                        }
                                        meetupUser.BirthDate = joObject.get("BirthDate").toString().replace("null", "");
                                        if (!meetupUser.BirthDate.isEmpty()) {
                                            String BirthDate = meetupUser.BirthDate;
                                            String currentdate = new SimpleDateFormat("yyyy").format(new Date());
                                            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                                            BirthDate = BirthDate.substring(0, 10);
                                            String strMonth = BirthDate.substring(5, 7);
                                            String strYear = BirthDate.substring(0, 4);
                                            String strDay = BirthDate.substring(8, 10);
                                            int year = Integer.parseInt(currentdate) - Integer.parseInt(strYear);
                                            meetupUser.setAge(year);
                                        } else {
                                            meetupUser.setAge(1);
                                        }
                                        meetupUser.FollowStatus = joObject.get("IsFollowing").toString().replace("null", "");
                                        meetupUser.MutualSportCount = joObject.getLong("MutualSportCount");
                                        meetupUser.Latitude = joObject.get("Latitude").toString().replace("null", "");
                                        meetupUser.Longitude = joObject.get("Longitude").toString().replace("null", "");
                                        meetupUser.LocationName = joObject.get("LocationName").toString().replace("null", "");
                                        double Distance = joObject.getDouble("Distance");
                                        meetupUser.Distance = Distance;
                                        try {
                                            double dist = Double.parseDouble(joObject.get("Distance").toString());
                                            dist = dist / 1000;
                                            dist = new BigDecimal(dist)
                                                    .setScale(1, BigDecimal.ROUND_HALF_UP)
                                                    .doubleValue();
                                            meetupUser.Distance = dist;
                                        } catch (Exception eee) {
                                            meetupUser.Distance = 0;
                                            //Log.d("log", eee.getMessage());
                                        }
                                        UserList.add(meetupUser);
                                    }
                                }
                            }
                            mretupList = (ArrayList<MeetupRowItem>) _mretupList;
                            // meetuplist
                            JSONArray meetupsresult_array_ = ResponseObj.getJSONArray("AllMeetups");
                            MeetupRowItem meetupItem;
                            if (meetupsresult_array_ != null) {
                                for (int i = 0; i < meetupsresult_array_.length(); i++) {
                                    meetupItem = new MeetupRowItem();
                                    JSONObject joObject = meetupsresult_array_.getJSONObject(i);
                                    if (joObject != null) {
                                        meetupItem.ID = joObject.get("Id").toString().replace("null", "");
                                        meetupItem.UserID = joObject.get("UserID").toString().replace("null", "");
                                        meetupItem.ActivityID = joObject.get("ActivityID").toString().replace("null", "");
                                        meetupItem.SportID = joObject.get("SportID").toString().replace("null", "");
                                        meetupItem.ParticipantsStatus = joObject.getInt("ParticipantsStatus");
                                        meetupItem.MeetupFrequencyID = joObject.get("MeetupFrequencyID").toString().replace("null", "");
                                        meetupItem.MeetupLevelID = joObject.get("MeetupLevelID").toString().replace("null", "");
                                        meetupItem.MeetupTitle = joObject.get("MeetupTitle").toString().replace("null", "");
                                        meetupItem.SmallPic = joObject.get("SmallPic").toString().replace("null", "");

                                        String SmallPic = joObject.get("SmallPic").toString().replace("null", "");
                                        if (!SmallPic.isEmpty()) {
                                            String slash = SmallPic.substring(0, 1);
                                            if (slash.equals("/")) {
                                                SmallPic = SmallPic.substring(1);
                                            }
                                            meetupItem.SmallPic = SmallPic;
                                        }

                                        String MeduimPic = joObject.get("MeduimPic").toString().replace("null", "");
                                        if (!MeduimPic.isEmpty()) {
                                            String slash = MeduimPic.substring(0, 1);
                                            if (slash.equals("/")) {
                                                MeduimPic = MeduimPic.substring(1);
                                            }
                                            meetupItem.MeduimPic = MeduimPic;
                                        }
                                        meetupItem.SavedMeetupId = joObject.get("SavedMeetupId").toString().replace("null", "");
                                        meetupItem.IsSaved = joObject.getBoolean("IsSaved");
                                        String OriginalPic = joObject.get("OriginalPic").toString().replace("null", "");
                                        if (!OriginalPic.isEmpty()) {
                                            String slash = OriginalPic.substring(0, 1);
                                            if (slash.equals("/")) {
                                                OriginalPic = OriginalPic.substring(1);
                                            }
                                            meetupItem.OriginalPic = OriginalPic;
                                        }
                                        meetupItem.Description = joObject.get("Description").toString().replace("null", "");
                                        meetupItem.Location = joObject.get("Location").toString().replace("null", "");
                                        meetupItem.Longitude = joObject.getDouble("Longitude");
                                        meetupItem.Latitude = joObject.getDouble("Latitude");
                                        meetupItem.MeetupFromDateTime = DateUtils.UTCDateToLocalTime(joObject.get("MeetupFromDateTime").toString().replace("null", ""));
                                        meetupItem.MeetupToDateTime = DateUtils.UTCDateToLocalTime(joObject.get("MeetupToDateTime").toString().replace("null", ""));
                                        meetupItem.PrivacyStatus = joObject.getBoolean("PrivacyStatus");
                                        meetupItem.MaxParticipants = joObject.get("MaxParticipants").toString().replace("null", "");
                                        meetupItem.SportActiveIcon = joObject.get("SportActiveIcon").toString().replace("null", "");
                                        meetupItem.EditStatus = joObject.getBoolean("EditStatus");
                                        meetupItem.LikeCount = joObject.getLong("LikeCount");
                                        meetupItem.LikeStatus = joObject.get("LikeStatus").toString().replace("null", "");
                                        String Distance = joObject.get("Distance").toString().replace("null", "");
                                        if (!Distance.isEmpty()) {
                                            try {
                                                double dist = Double.parseDouble(joObject.get("Distance").toString());
                                                dist = dist / 1000;
                                                dist = new BigDecimal(dist).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                                                meetupItem.Distance = dist;
                                            } catch (Exception eee) {
                                                meetupItem.Distance = 0;
                                                ///   Log.d("log", eee.getMessage());
                                            }
                                        }
                                        mretupList.add(meetupItem);
                                    }


                                }
                            }

                        }

                    } catch (Exception ex) {
                        ////     Log.d("log", ex.getMessage());
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray Result) {
                    Log.i("onSuccess", Result.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable Error, JSONObject Result) {


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                }
            });


        }

    }

    private void radarview() {
        mDatas = new SparseArray<Info>();


        String ImageUrl = "https://urbanx.blob.core.windows.net/";
        //get meetup list
        for (int i = 0; i < mretupList.size(); i++) {
            Info info = new Info();
            info.setItemid(1);
            info.setPortraitId(R.drawable.placeholder);
            info.setProfilePic(ImageUrl + mretupList.get(i).SportActiveIcon);
            info.setAge(((int) Math.random() * 25 + 16));
            info.setName(mretupList.get(i).MeetupTitle);
            String imgurl = mretupList.get(i).getSmallPic();
            if (!imgurl.replace("null", "").isEmpty()) {
                info.setGallaryImage(ImageUrl + mretupList.get(i).getSmallPic());
            }
            info.setMutualsport("0");
            info.setFollowStatus("0");
            info.setDistance(mretupList.get(i).Distance);
            info.setLikeCount(mretupList.get(i).LikeCount);
            info.setMeetuptime(mretupList.get(i).MeetupFromDateTime);
            info.setAge(0);
            info.setLocation("0");
            info.setUserId(mretupList.get(i).UserID);
            info.setMeetupId(mretupList.get(i).ID);
            info.setMeetup0_user1(false);
            mDatas.put(i, info);
        }
        int j = mDatas.size();
        int k = 0;
        //get users list
        for (int i = j; i < (UserList.size() + j); i++) {
            Info info = new Info();
            try {
                info.setMeetup0_user1(true);
                info.setItemid(1);
                info.setDistance(UserList.get(k).Distance);
                info.setUserId(UserList.get(k).getUserId());
                info.setPortraitId(R.drawable.placeholder);
                info.setMutualsport(UserList.get(k).getMutualSportCount() + "");
                info.setFollowStatus(UserList.get(k).getFollowStatus());
                info.setLocation(UserList.get(k).LocationName);
                info.setClientUsername(UserList.get(k).getClientUserName());
                info.setAge(UserList.get(k).getAge());
                info.setUserId(UserList.get(k).getUserId());
                String imgurl = UserList.get(k).getThumbImgPath();
                if (!imgurl.replace("null", "").isEmpty()) {
                    info.setProfilePic(ImageUrl + UserList.get(k).getThumbImgPath());
                }
                info.setGallaryImage(ImageUrl + UserList.get(k).getGallaryImage());
                info.setName(UserList.get(k).getClientUserName());
            } catch (Exception ex) {
                /// Log.d("log", ex.getMessage());
            }
            mDatas.put(i, info);
            k++;
        }


        int n = mDatas.size();
        Info swap = new Info();
        for (int c = 0; c < (n - 1); c++) {
            for (int d = 0; d < n - c - 1; d++) {
                if (mDatas.get(d).getDistance() > mDatas.get(d + 1).getDistance()) /* For descending order use < */ {

                    swap = mDatas.get(d);
                    mDatas.setValueAt(d, mDatas.get(d + 1));
                    mDatas.setValueAt(d + 1, swap);
                }
            }
        }

        //assign the data

    }
}
