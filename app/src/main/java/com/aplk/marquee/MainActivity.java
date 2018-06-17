package com.aplk.marquee;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MarqueeView mMarquee;
    private MarqueeView mMarquee2;
    private int num = 0;
    private List<String> mInfo;
    private List<String> mInfo2;
    private List<String> mReceiveInfo = new ArrayList<>();
    private List<String> mReceiveInfo2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {

        mMarquee = findViewById(R.id.marqueeView);
        mMarquee2 = findViewById(R.id.marqueeView2);

        mInfo = new ArrayList<>();
        mInfo.add("准备开始推送公告了,请注意!");
        mInfo.add("准备开始推送公告了,请注意!!!");
        mInfo2 = new ArrayList<>();
        mInfo2.add("准备开始推送公告了,请注意!");
        mInfo2.add("准备开始推送公告了,请注意!!!");
        mMarquee.setType(0);
        mMarquee2.setType(1);
        mMarquee.startWithList(mInfo, R.anim.anim_bottom_in, R.anim.anim_top_out);
        mMarquee2.startWithList(mInfo2, R.anim.anim_bottom_in, R.anim.anim_top_out);
        mMarquee.setAnimationEndListener(new MarqueeView.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                if (mReceiveInfo != null && mReceiveInfo.size() > 0) {
                    mMarquee.createNextTextView(mReceiveInfo.get(0));
                    mReceiveInfo.remove(0);
                } else {
                    mMarquee.createNextTextView("");
                }
            }
        });
        mMarquee.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(CharSequence text, TextView textView) {
                Toast.makeText(MainActivity.this, "点击了公告:" + text, Toast.LENGTH_SHORT).show();
            }
        });
        mMarquee2.setAnimationEndListener(new MarqueeView.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                if (mReceiveInfo2 != null && mReceiveInfo2.size() > 0) {
                    mMarquee2.createNextView(mReceiveInfo2.get(0), mReceiveInfo2.get(0));
                    mReceiveInfo2.remove(0);
                } else {
                    mMarquee2.createNextView("", "");
                }
            }
        });
    }

    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 100) {
                    if (num < 100) {
                        num++;
                    } else {
                        num = 0;
                    }
                    StringBuffer notice=new StringBuffer("现在开始播送广告:");
                    notice.append(num);
                    mReceiveInfo.add(notice.toString());
                    mReceiveInfo2.add(notice.toString());
                    if (mInfo != null) {
                        if (mInfo.size() < 10) {
                            mInfo.add((notice.toString()));
                        } else {
                            mInfo.remove(0);
                        }
                    }
                    if (mInfo2 != null) {
                        if (mInfo2.size() < 10) {
                            mInfo2.add((notice.toString()));
                        } else {
                            mInfo2.remove(0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable mRunnable = new Runnable() {
        public void run() {
            mhandler.sendEmptyMessage(100);
            mhandler.postDelayed(mRunnable, 3000);  //给自己发送消息，自运行
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mhandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mhandler.post(mRunnable);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMarquee.startFlipping();
        mMarquee2.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMarquee.stopFlipping();
        mMarquee2.stopFlipping();
    }


    @Override
    public void onDestroy() {
        mhandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
