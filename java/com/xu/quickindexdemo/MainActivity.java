package com.xu.quickindexdemo;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;


public class MainActivity extends Activity {
    private QuickIndexBar quickIndexBar;
    private ListView listview;
    private TextView currentWord;

    private ArrayList<Friend> friends = new ArrayList<Friend>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
        listview = (ListView) findViewById(R.id.listview);
        currentWord = (TextView) findViewById(R.id.currentWord);

        //1.准备数据
        fillList();
        //对集合的数据进行排序，排序是根据java里面某个字段排序，这里根据拼音来排序
        //2.对数据进行排序
        Collections.sort(friends);
        //3.设置Adapter
        listview.setAdapter(new MyAdapter(this,friends));

        //让view和listView之间有关系
        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                //根据当前触摸的字母，去集合中找那个item的首字母和letter一样，然后将对应的item放到屏幕顶端
                //遍历集合
                for (int i = 0; i < friends.size(); i++) {
                    //先获取当前的首字母
                    String firstWord = friends.get(i).getPinyin().charAt(0)+"";
                    //如果当前触摸的字母和当前的首字母一样
                    if(letter.equals(firstWord)){
                        //说明找到了，那么应该讲当前的item放到屏幕顶端
                        listview.setSelection(i);
                        break;//只需要找到第一个就行
                    }
                }

                //滑动过程中一直要显示中间的字母
                //显示当前触摸的字母
                showCurrentWord(letter);
            }
        });


        //显示的时候加动画，不用隐藏和显示了gone和visbily
        //通过缩小currentWord来隐藏。参数是缩小什么，缩小到0
        ViewHelper.setScaleX(currentWord, 0);
        ViewHelper.setScaleY(currentWord, 0);

//		Log.e("tag", PinYinUtil.getPinyin("黑    马"));//HEIMA
//		Log.e("tag", PinYinUtil.getPinyin("#黑**马"));//#HEI**MA
//		Log.e("tag", PinYinUtil.getPinyin("O(∩_∩)O~黑。，马"));//HEIMA
    }

    //是否开启过动画，默认是没有缩放过
    private boolean isScale = false;
    //延时一段时间消失
    private Handler handler = new Handler();
    protected void showCurrentWord(String letter) {
        //设置个textView就可以了，
        currentWord.setText(letter);
        if(!isScale){
            //只要执行过，就是true
            isScale = true;
            //动画显示，对什么执行动画，执行缩放动画，缩小1倍，设置一个弹性的插值器在500毫秒，开始动画
            ViewPropertyAnimator.animate(currentWord).scaleX(1f).setInterpolator(new OvershootInterpolator()).setDuration(450).start();
            ViewPropertyAnimator.animate(currentWord).scaleY(1f).setInterpolator(new OvershootInterpolator()).setDuration(450).start();
        }

        //滑动过程中闪烁。1.5秒到了，因为滑动过程会一直调用showCurrentWord方法，每次调用就post。
        //所以每次调用post之前，先移除之前的任务
        handler.removeCallbacksAndMessages(null);

        //延时隐藏currentWord
        //延时一段时间消失，这个在主线程里面执行的（handler在主线程的话，post所有任务就在主线程，反之）
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//				currentWord.setVisibility(View.INVISIBLE);//隐藏
                ViewPropertyAnimator.animate(currentWord).scaleX(0f).setDuration(450).start();
                ViewPropertyAnimator.animate(currentWord).scaleY(0f).setDuration(450).start();
                //消失的时候要执行false
                isScale = false;
            }
        }, 1500);
    }

    private void fillList() {
        // 虚拟数据
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }

}
