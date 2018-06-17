# MarqueeView
公告消息跑马灯效果,参考:https://github.com/sfsheng0322/MarqueeView  开源项目,根据自己业务需求修改成了定时3秒推送一条新的公告消息到滚动栏上显示
## 使用方式:
### 1.在布局文件中引入
<com.aplk.marquee.MarqueeView</br>
        android:id="@+id/marqueeView"</br>
        android:layout_width="match_parent"</br>
        android:layout_height="30dp"</br>
        android:layout_centerVertical="true"</br>
        android:background="#88303F9F"</br>
        android:layout_marginLeft="40dp"</br>
        android:layout_marginRight="40dp"</br>
        android:paddingLeft="5dp"</br>
        android:paddingRight="5dp"</br>
        app:mvAnimDuration="800"</br>
        app:mvDirection="bottom_to_top"</br>
        app:mvGravity="left"</br>
        app:mvInterval="2500"</br>
        app:mvSingleLine="true"</br>
        app:mvTextColor="#ffffff"</br>
        app:mvTextSize="14sp"/></br>
  </br>  
### 2.在Activity中启动一个handle,模拟每隔3秒自动发送一条公告消息
private Handler mhandler = new Handler() {</br>
        public void handleMessage(Message msg) {</br>
            try {</br>
                if (msg.what == 100) {</br>
                    if (num < 100) {</br>
                        num++;</br>
                    }else{</br>
                        num=0;</br>
                    }</br>
                    String notice = "现在开始播送广告:" + num;</br>
                    mReceiveInfo.add(notice);</br>
                    if (mInfo != null) {</br>
                        if (mInfo.size() < 10) {</br>
                            mInfo.add(notice);</br>
                        } else {</br>
                            mInfo.remove(0);</br>
                        }</br>
                    }</br>
                }</br>
            } catch (Exception e) {</br>
                e.printStackTrace();</br>
            }</br>
        }</br>
    };</br>
    private Runnable mRunnable = new Runnable() {</br>
        public void run() {</br>
            mhandler.sendEmptyMessage(100);</br>
            mhandler.postDelayed(mRunnable, 3000);  //给自己发送消息，自运行</br>
        }</br>
    };</br>
</br>
    @Override</br>
    public void onPause() {</br>
        super.onPause();</br>
        mhandler.removeCallbacks(mRunnable);</br>
    }</br>
</br>
    @Override</br>
    public void onResume() {</br>
        super.onResume();</br>
        mhandler.post(mRunnable);</br>
    }</br>
      @Override</br>
    public void onDestroy() {</br>
        mhandler.removeCallbacks(mRunnable);</br>
        super.onDestroy();</br>
    }</br>
  </br>      
 ### 3.设置轮播的初始化数据,并监听每个view动画结束时的事件和点击事件
 mInfo = new ArrayList<>();</br>
        mInfo.add("准备开始推送公告了,请注意!");</br>
        mMarquee.startWithList(mInfo, R.anim.anim_bottom_in, R.anim.anim_top_out);</br>
        mMarquee.setAnimationEndListener(new MarqueeView.AnimationEndListener() {</br>
            @Override</br>
            public void onAnimationEnd() {</br>
                if (mReceiveInfo != null && mReceiveInfo.size() > 0) {</br>
                    mMarquee.createNextTextView(mReceiveInfo.get(0));</br>
                    mReceiveInfo.remove(0);</br>
                }else{</br>
                    mMarquee.createNextTextView("");</br>
                }</br>
            }</br>
        });</br>
        mMarquee.setOnItemClickListener(new MarqueeView.OnItemClickListener() {</br>
            @Override</br>
            public void onItemClick(CharSequence text, TextView textView) {</br>
                Toast.makeText(MainActivity.this,"点击了公告:"+text,Toast.LENGTH_SHORT).show();</br>
            }</br>
        });</br>
        </br>
 ### 4.去除重影的问题
  @Override</br>
    public void onStart() {</br>
        super.onStart();</br>
        mMarquee.startFlipping();</br>
    }</br>
</br>
    @Override</br>
    public void onStop() {</br>
        super.onStop();</br>
        mMarquee.stopFlipping();</br>
    }</br>
