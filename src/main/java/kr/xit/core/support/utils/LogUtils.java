package kr.xit.core.support.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogUtils {

	/**
	 * Json 데이터 보기 좋게 변환.
	 * @param obj Object	obj
	 * @return	String
	 */
	public static Object toString(Object obj){
		return JsonUtils.jsonEnterConvert(obj);
	}

	public static String getClassNm(Throwable throwable) {
		StackTraceElement[] stacks	= throwable.getStackTrace();
		return stacks[0].getClassName();
	}

	public static String getMethodNm(Throwable throwable) {
		StackTraceElement[] stacks	= throwable.getStackTrace();
		return stacks[ 0 ].getMethodName();
	}

	public static String getMethodInfo(Throwable throwable) {
		StackTraceElement[] stacks	= throwable.getStackTrace();
		return stacks[0].getClassName() + "." + stacks[0].getMethodName();
	}
}
