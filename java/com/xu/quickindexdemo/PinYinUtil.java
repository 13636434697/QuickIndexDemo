package com.xu.quickindexdemo;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.text.TextUtils;

public class PinYinUtil {
	/**
	 * 获取汉字的拼音，会销毁一定的资源，所以不应该被频繁调用
	 * @param chinese
	 * @return
	 */
	public static String getPinyin(String chinese){
		//如果汉字为空就直接返回
		if(TextUtils.isEmpty(chinese)) return null;
		
		//用来设置转化的拼音的大小写，或者声调
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置转化的拼音是大写字母
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置转化的拼音不带声调
		
		//1.由于只能对单个汉字转化，所以需要将字符串转化为字符数组，然后对每个字符转化，最后拼接起来
		char[] charArray = chinese.toCharArray();
		String pinyin = "";
		//遍历，对每个字符进行转化
		for (int i = 0; i < charArray.length; i++) {
			//2.过滤空格   黑   马->HEIMA
			//判断当前字符是否是空格，就不处理。
			if(Character.isWhitespace(charArray[i]))continue;
			
			//3.需要判断是否是汉字   a@#黑*&*马（像这种字符，键盘上输出来的字符都有uncode，对应计算机都有字符，也可以排序）
			//判断是不是汉字，汉字占2个字节，一个字节范围是-128~127，那么汉字肯定大于127
			if(charArray[i]>127){
				//可能是汉字（粗略的判断）
				try {
					//由于多音字的存在，比如单  dan shan,所以返回的是字符串数组
					//提供的类进行转换，带格式化的，参数1，当前的字符，参数2，用来设置转化的拼音的大小写，或者声调
					String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(charArray[i],format);
					if(pinyinArr!=null){
						//取拼音，并且拼接到字符串上，只能取第一个数组
						pinyin += pinyinArr[0];//此处即使有多音字，那么也只能取第一个拼音
					}else {
						//说明没有找到对应的拼音，汉字有问题，或者可能不是汉字，则忽略
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					//说明转化失败，不是汉字，比如O(∩_∩)O~，那么则忽略
				}
			}else {
				//肯定不是汉字，应该是键盘上能够直接输入的字符，这些字符能够排序，但不能获取拼音
				//所以可以直接拼接  a黑马->aheima，在上面直接初始化一个字符String pinyin = "";
				pinyin += charArray[i];
			}
		}
		//最终返回拼音的字符串
		return pinyin;
	}
}
