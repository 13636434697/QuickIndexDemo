package com.xu.quickindexdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/*
* 右侧字母的view
*
* 自定义view，没有子view，继承view就可以了
* */
public class QuickIndexBar extends View{
	private String[] indexArr = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };

	private Paint paint;
	private int width;
	private float cellHeight;//初始化单元格的高度
	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QuickIndexBar(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		//初始化画笔
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
		paint.setColor(Color.WHITE);
		paint.setTextSize(16);
		//绘制在文字的起始点左下角度所以不能0
		paint.setTextAlign(Align.CENTER);//设置文本的起点是文字边框底边的中心
	}

	//初始化view的宽度
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//自身宽度的一半
		width = getMeasuredWidth();
		//得到一个格子的高度，保留精确度*1f
		cellHeight = getMeasuredHeight()*1f/indexArr.length;
	}

	//在这里绘制，canvas可以绘制很多东西
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//循环数组
		for (int i = 0; i < indexArr.length; i++) {
			//自身宽度的一半
			float x = width/2;
			//应该往下偏移，等分的分布在view上面的垂直空间（格子高度的一半加上文本高度的一半在加上位置乘以高度）
			float y = cellHeight/2 + getTextHeight(indexArr[i])/2 + i*cellHeight;
			//初始化画笔，当前绘制的字母是不是和选中的一个，是的话，就变成黑色，不是的话，白色，这里改完之后，下面在绘制一次
			paint.setColor(lastIndex==i?Color.BLACK:Color.WHITE);
			//绘制文本（数组对应的字母，x坐标的起点，y坐标的起点，画笔）绘制在文字的起始点左下角度所以不能0
			canvas.drawText(indexArr[i], x, y, paint);
		}
	}


	//触摸的时候能够知道当前触摸的字母
	private int lastIndex = -1;//记录上次的触摸字母的索引，默认值0是有效的，所以给-1
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//三个常量一般同时存在
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			float y = event.getY();
			int index = (int) (y/cellHeight);//得到字母对应的索引
			if(lastIndex!=index){
				//说明当前触摸字母和上一个不是同一个字母
//				Log.e("tag", indexArr[index]);
				//对index做安全性的检查
				if(index>=0 && index<indexArr.length){
					//回调不等于空才调用
					if(listener!=null){
						listener.onTouchLetter(indexArr[index]);
					}
				}
			}
			lastIndex = index;
			break;
		case MotionEvent.ACTION_UP:
			//抬起的时候必须重置lastIndex
			lastIndex = -1;
			break;
		}

		//引起重绘，因为想显示点击字母变色。触摸的时候就会回调onDraw的方法
		invalidate();
		//自己要处理要返回true，自己消费事件
		return true;
	}

	/**
	 * 获取文本的高度
	 * @param text
	 * @return
	 */
	private int getTextHeight(String text) {
		//矩形对象
		Rect bounds = new Rect();
		//借助画笔的方法：（获取字符串，从什么字符，到什么字符，矩形对象）
		paint.getTextBounds(text,0,text.length(), bounds);
		//返回文本的botton-top=height
		return bounds.height();
	}

	//给回调接口设置监听的方法
	private OnTouchLetterListener listener;
	public void setOnTouchLetterListener(OnTouchLetterListener listener){
		this.listener = listener;
	}
	/**
	 * 触摸字母的监听器
	 *
	 *暴漏方法给别人用，传数据通过接口
	 */
	public interface OnTouchLetterListener{
		void onTouchLetter(String letter);
	}

}
