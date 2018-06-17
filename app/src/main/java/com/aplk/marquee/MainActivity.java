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
    private int num = 0;
    private List<String> mInfo;
    private List<String> mReceiveInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    private void initData() {
        mMarquee = findViewById(R.id.marqueeView);

        mInfo = new ArrayList<>();
        mInfo.add("准备开始推送公告了,请注意!");
        mMarquee.startWithList(mInfo, R.anim.anim_bottom_in, R.anim.anim_top_out);
        mMarquee.setAnimationEndListener(new MarqueeView.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                if (mReceiveInfo != null && mReceiveInfo.size() > 0) {
                    mMarquee.createNextTextView(mReceiveInfo.get(0));
                    mReceiveInfo.remove(0);
                }else{
                    mMarquee.createNextTextView("");
                }
            }
        });
        mMarquee.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(CharSequence text, TextView textView) {
                Toast.makeText(MainActivity.this,"点击了公告:"+text,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 100) {
                    if (num < 100) {
                        num++;
                    }else{
                        num=0;
                    }
                    String notice = "现在开始播送广告:" + num;
                    mReceiveInfo.add(notice);
                    if (mInfo != null) {
                        if (mInfo.size() < 10) {
                            mInfo.add(notice);
                        } else {
                            mInfo.remove(0);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        mMarquee.stopFlipping();
    }


    @Override
    public void onDestroy() {
        mhandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }
}
