package com.xu.quickindexdemo;

/*
* 这个对象需要实现Comparable接口，传自己的类型，因为是自己给自己补
* */
public class Friend implements Comparable<Friend>{
	private String name;
	private String pinyin;

	//使用成员变量生成构造方法：alt+shift+s->o
	public Friend(String name) {
		super();
		this.name = name;

		//在排序的时候要调用N多次，跟每个人比一次，因为拼音都固定的，所以不用每次都转
		//这里优化一下，一开始就转化好拼音，
		setPinyin(PinYinUtil.getPinyin(name));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//实现这个方法，这个方法和其他数进行比较用的
	//在排序的时候要调用N多次，跟每个人比一次，因为拼音都固定的，所以不用每次都转
	@Override
	public int compareTo(Friend another) {
		//获取当前的拼音，然后another是另外的拼音
		return getPinyin().compareTo(another.getPinyin());
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	
}
