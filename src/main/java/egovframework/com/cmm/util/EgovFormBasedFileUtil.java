package egovframework.com.cmm.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import kr.xit.core.exception.BizRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.model.EgovFormBasedUUID;

/**
 * @Class Name  : EgovFormBasedFileUtil.java
 * @Description : Form-based File Upload 유틸리티
 * @Modification Information
 *
 *   수정일                수정자              수정내용
 *   ----------   --------     ---------------------------
 *   2009.08.26   한성곤               최초 생성
 *   2017.03.03     조성원 	            시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
 *   2019.12.09   신용호               KISA 보안약점 조치 (위험한 형식 파일 업로드) : uploadFiles 삭제  => EgovFileUploadUtil.uploadFilesExt(확장자 기록) 대체
 *
 * @author 공통컴포넌트 개발팀 한성곤
 * @since 2009.08.26
 * @version 1.0
 * @see
 */
public class EgovFormBasedFileUtil {
	/** Buffer size */
	public static final int BUFFER_SIZE = 8192;

	public static final String SEPERATOR = File.separator;

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovFormBasedFileUtil.class);

	/**
	 * 오늘 날짜 문자열 취득.
	 * ex) 20090101
	 * @return String
	 */
	public static String getTodayString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

		return format.format(new Date());
	}

	/**
	 * 물리적 파일명 생성.
	 * @return String
	 */
	public static String getPhysicalFileName() {
		return EgovFormBasedUUID.randomUUID().toString().replaceAll("/-/g", "").toUpperCase();
	}

	/**
	 * 파일명 변환.
	 * @param filename String
	 * @return String
	 * @throws Exception
	 */
	protected static String convert(String filename) {
		try {
			return java.net.URLEncoder.encode(filename, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}

	/**
	 * Stream으로부터 파일을 저장함.
	 * @param is InputStream
	 * @param file File
	 * @throws IOException
	 */
	public static long saveFile(InputStream is, File file) throws IOException {
		//KISA 보안약점 조치 (2018-10-29, 윤창원)
		if (file.getParentFile() == null) {
			LOGGER.debug("file.getParentFile() is null");
			throw new RuntimeException("file.getParentFile() is null");
		}

		// 디렉토리 생성
		if (!file.getParentFile().exists()) {
			//2017.03.03 	조성원 	시큐어코딩(ES)-부적절한 예외 처리[CWE-253, CWE-440, CWE-754]
			if(file.getParentFile().mkdirs()){
				LOGGER.debug("[file.mkdirs] file : Directory Creation Success");
			}else{
				LOGGER.error("[file.mkdirs] file : Directory Creation Fail");
			}
		}

		long size = 0L;
		try(OutputStream os = new FileOutputStream(file);){
			int bytesRead;
			byte[] buffer = new byte[BUFFER_SIZE];

			while ((bytesRead = is.read(buffer, 0, BUFFER_SIZE)) != -1) {
				size += bytesRead;
				os.write(buffer, 0, bytesRead);
			}
		}
		return size;
	}

	/**
	 * 파일을 Download 처리한다.
	 *
	 * @param response
	 * @param where
	 * @param serverSubPath
	 * @param physicalName
	 * @param original
	 * @throws Exception
	 */
	public static void downloadFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String original) throws Exception {
		String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;

		File file = new File(EgovWebUtil.filePathBlackList(downFileName));

		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		byte[] b = new byte[BUFFER_SIZE];

		original = original.replaceAll("(\r\n|\r|\n|\n\r)", "");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + convert(original) + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");

		try(BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
		){
			int read;
			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		}
	}

	/**
	 * 이미지에 대한 미리보기 기능을 제공한다.
	 *
	 * mimeType의 경우는 JSP 상에서 다음과 같이 얻을 수 있다.
	 * getServletConfig().getServletContext().getMimeType(name);
	 *
	 * @param response
	 * @param where
	 * @param serverSubPath
	 * @param physicalName
	 * @param mimeTypeParam
	 * @throws Exception
	 */
	public static void viewFile(HttpServletResponse response, String where, String serverSubPath, String physicalName, String mimeTypeParam) throws Exception {
		String mimeType = mimeTypeParam;
		String downFileName = where + SEPERATOR + serverSubPath + SEPERATOR + physicalName;

		File file = new File(EgovWebUtil.filePathBlackList(downFileName));

		if (!file.exists()) {
			throw new FileNotFoundException(downFileName);
		}

		if (!file.isFile()) {
			throw new FileNotFoundException(downFileName);
		}

		byte[] b = new byte[BUFFER_SIZE];

		if (mimeType == null) {
			mimeType = "application/octet-stream;";
		}

		response.setContentType(EgovWebUtil.removeCRLF(mimeType));
		response.setHeader("Content-Disposition", "filename=image;");

		try(
			BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
		){
			int read;
			while ((read = fin.read(b)) != -1) {
				outs.write(b, 0, read);
			}
		}
	}
}
