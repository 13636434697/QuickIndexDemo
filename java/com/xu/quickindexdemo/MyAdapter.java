package com.xu.quickindexdemo;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context context;
	//需要用一个构造方法接收一个集合
	private ArrayList<Friend> list;
	public MyAdapter(Context context,ArrayList<Friend> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	//封装了holder
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = View.inflate(context, R.layout.adapter_friend,null);
		}
		//封装的方法
		ViewHolder holder = ViewHolder.getHolder(convertView);

		//设置数据
		Friend friend = list.get(position);
		holder.name.setText(friend.getName());
		//显示首字母，返回的是charAt，但是setText不能接收charAt的方法，转成int了。所以不能找到，加个空格字符就可以了
		String currentWord = friend.getPinyin().charAt(0)+"";
		//判断一下
		if(position>0){
			//同一个字母需要合并item，获取上一个item的首字母
			String lastWord = list.get(position-1).getPinyin().charAt(0)+"";
			//拿当前的首字母和上一个首字母比较
			if(currentWord.equals(lastWord)){
				//说明首字母相同，需要隐藏当前item的first_word
				holder.first_word.setVisibility(View.GONE);
			}else {
				//不一样，需要显示当前的首字母
				//由于布局是复用的，所以在需要显示的时候，再次将first_word设置为可见
				holder.first_word.setVisibility(View.VISIBLE);
				//让引导字母显示拼音了
				holder.first_word.setText(currentWord);
			}
		}else {
			//说明是第一个，不用判断了。直接显示了
			holder.first_word.setVisibility(View.VISIBLE);
			holder.first_word.setText(currentWord);
		}
		
		return convertView;
	}

	//封装
	static class ViewHolder{
		TextView name,first_word;
		//构造方法
		public ViewHolder(View convertView){
			name = (TextView) convertView.findViewById(R.id.name);
			first_word = (TextView) convertView.findViewById(R.id.first_word);
		}
		public static ViewHolder getHolder(View convertView){
			//先获取tag了
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				//为空就new在设置tag
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}
