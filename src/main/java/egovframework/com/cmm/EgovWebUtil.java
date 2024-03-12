package egovframework.com.cmm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Pattern;

/**
 * 교차접속 스크립트 공격 취약성 방지(파라미터 문자열 교체)
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자           수정내용
 *  -----------  --------  ---------------------------
 *   2011.10.10  한성곤           최초 생성
 *	 2017-02-07   이정은           시큐어코딩(ES) - 시큐어코딩 경로 조작 및 자원 삽입[CWE-22, CWE-23, CWE-95, CWE-99]
 *   2018.08.17  신용호           filePathBlackList 수정
 *   2018.10.10  신용호           . => \\.으로 수정
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EgovWebUtil {
	public static String clearXSSMinimum(String value) {
		if (value == null || value.trim().equals("")) {
			return "";
		}
		return StringEscapeUtils.escapeHtml4(value);
	}

	public static String filePathBlackList(String value) {
		String returnValue = value;
		if (returnValue == null || returnValue.trim().equals("")) {
			return "";
		}

		returnValue = returnValue.replaceAll("/\\.\\./g", "");

		return returnValue;
	}

	public static String removeCRLF(String parameter) {
		return parameter.replaceAll("(\r\n|\r|\n|\n\r)", "");
	}
}
