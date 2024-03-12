package kr.xit.core.support.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import kr.xit.core.exception.BizRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 * description : SFTP Util class
 *
 * packageName : kr.xit.core.support.utils
 * fileName    : SFTPUtils
 * author      : limju
 * date        : 2023-07-07
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-07    limju       최초 생성
 *
 * </pre>
 */
@Slf4j
public class SFTPUtils {
	
	public Session session = null;
	public Channel channel = null;
	public ChannelSftp channelSftp = null;
	private static final int TIME_OUT = 60000;

	/**
	 * SFTP 접속
	 * 
	 * @param host String
	 * @param port int
	 * @param id String
	 * @param passwd String
	 * @param sshKey String
	 */
	public void init(String host, int port, String id, String passwd, String sshKey) {
		
		JSch jsch = new JSch();

		try {
            //세션객체 생성 
            session = jsch.getSession(id, host, port);

			// 인증
			if(StringUtils.isNotEmpty(sshKey)) 	jsch.addIdentity(sshKey);
			else								session.setPassword(passwd);
            
            //세션관련 설정정보 설정
            java.util.Properties config = new java.util.Properties();
			//호스트 정보 검사 skip.
			config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(TIME_OUT); //타임아웃 설정

            session.connect();

			//sftp 채널 접속
            channel = session.openChannel("sftp");
            channel.connect();
            
		} catch (JSchException e) {
            throw BizRuntimeException.create(e.getMessage());
		}
		channelSftp = (ChannelSftp) channel;
	}
	
	/**
	 * SFTP 서버 파일 업로드
	 * @param uploadPath String 서버업로드 절대경로
	 * @param localPathOnly String 로컬 업로드 파일 경로(파일 미포함)
	 * @param uploadFileNm String 업로드할 파일명
	 */
	public void fileUpload(String uploadPath, String localPathOnly, String uploadFileNm) {

		try(FileInputStream in = new FileInputStream(String.format("%s/%s", localPathOnly, uploadFileNm));) {
			channelSftp.cd(uploadPath);
			channelSftp.put(in, uploadFileNm);

		}catch(IOException | SftpException fe){
			throw BizRuntimeException.create(fe.getMessage());
		}
	}
	
	/**
	 * SFTP 서버 여러 파일 업로드
	 * @param uploadPath String 업로드할 서버 절대경로
	 * @param localPath String 업로드할 로컬 파일 경로
	 * @param uploadFiles ArrayList<String> 업로드할 로컬파일명 목록
	 */
	public void fileUploads(String uploadPath, String localPath, ArrayList<String> uploadFiles) {
		try {
			channelSftp.cd(uploadPath);
			for(String uploadFile : uploadFiles) {
				try (FileInputStream in = new FileInputStream(localPath + String.valueOf(uploadFile))) {
					channelSftp.put(in, String.valueOf(uploadFile));

				} catch (IOException | SftpException fe) {
					throw BizRuntimeException.create(fe.getMessage());
				}
			}
		} catch (SftpException e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}
	
	/**
	 * SFTP 서버 파일 다운로드 
	 * @param downloadPath String 서버 절대 경로
	 * @param localFilePath String 다운로드 로컬경로
	 */
	public void fileDownload(String downloadPath, String localFilePath) {
		byte[] buffer = new byte[1024];
		OutputStream os = null;
        
        try {
            String cdDir = downloadPath.substring(0, downloadPath.lastIndexOf("/") + 1);
            String fileName = downloadPath.substring(downloadPath.lastIndexOf("/") + 1);
            channelSftp.cd(cdDir);

            File newFile = new File(String.format("%s/%s", localFilePath, fileName));
			os = Files.newOutputStream(newFile.toPath());

            //파일 다운로드 SFTP 서버 -> 다운로드 서버
            try(BufferedInputStream bis = new BufferedInputStream(channelSftp.get(fileName));
			    BufferedOutputStream bos = new BufferedOutputStream(os);) {
				int readCount;
				while ((readCount = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, readCount);
				}
			}

        } catch (Exception e) {
			throw BizRuntimeException.create(e.getMessage());

		} finally {
        	try {
	            if(os != null)	os.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
	}
	
	/**
	 * SFTP 서버 파일 read(파일명) - 파일명(확장자 제외) 으로 search
	 * @param fullPath String
	 * @return String
	 */
	public String findFileName(String fullPath) {
        try {
            String cdDir = fullPath.substring(0, fullPath.lastIndexOf("/") + 1);
            String fileName = fullPath.substring(fullPath.lastIndexOf("/") + 1);

            channelSftp.cd(cdDir);
            Vector<LsEntry> fileList = channelSftp.ls(cdDir);

			for (LsEntry files : fileList) {
				if ((fileName + (".*")).matches(files.getFilename())) {
					return files.getFilename();
				}
			}

        } catch (Exception e) {
			throw BizRuntimeException.create(e.getMessage());
		}
		return null;
	}

	/**
	 * SFTP 서버 경로의 파일 목록 조회
	 * @param path String 서버 절대 경로
	 * @return ArrayList<String>
	 */
	public ArrayList<String> findFileNameList(String path) {
		ArrayList<String> fileNameList = new ArrayList<>();

		try {
			Vector<LsEntry> fileList = channelSftp.ls(path);
			for (LsEntry le : fileList){
				if(".".equals(le.getFilename()) || "..".equals(le.getFilename())) {
					continue;
				}
				fileNameList.add(le.getFilename());
			}
			return fileNameList;

		} catch (Exception e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}

	/**
	 * SFTP 파일 move
	 * @param src String source file full path(파일명 포함)
	 * @param dest String target full path(파일명 포함)
	 */
	public void mv(String src, String dest) {
		try {
			String cdDir = dest.substring(0, dest.lastIndexOf("/"));
			SftpATTRS attrs = null;
			try {
			 	attrs = channelSftp.stat(cdDir);
			} catch (SftpException e) {
				log.info("파일 move 대상 path 미존재 :: {}", cdDir);
			}
			if(attrs == null)	channelSftp.mkdir(cdDir);

			channelSftp.rename(src, dest);
		} catch (SftpException e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}

	/**
	 * SFTP 파일 삭제
	 * @param fileName 파일명
	 */
	public void rm(String fileName) {
		try {
			channelSftp.rm(fileName);
		} catch (SftpException e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}

	public String command(String command) {
		ArrayList<String> fileNameList = new ArrayList<>();

		try {
			ChannelExec exec = (ChannelExec) session.openChannel("exec");
			exec.setCommand(command);

			try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream()) {
				exec.setOutputStream(responseStream);
				exec.connect();

				while (exec.isConnected()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// thread pool에 에러 상태 전송
						Thread.currentThread().interrupt();
						throw BizRuntimeException.create(e.getMessage());
					}
				}

				return responseStream.toString();
			}

		} catch (JSchException | IOException e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}

	/**
	 * SFTP 서버 접속 종료
	 */
	public void disconnect() {
		if(channelSftp != null) {
			channelSftp.quit();
		}
		if(channel != null) {
			channel.disconnect();
		}
		if(session != null) {
			session.disconnect();
		}
	}

	public static void main(String[] args) {
		/*
		SFTPUtils sftpUtil = new SFTPUtils();
		final String downloadFile = "/data/ens/sg-pni-cctv/rcv/20230622111301_49고3736_174번카메라-UDP_101.jpg";
		final String uploadFile = "D:/doc/ens/04. 서광 CCTV/사전알리미 샘플자료/20230622111301_49고3736_174번카메라-UDP_101.jpg";
		try {
			//SFTP 서버 접속
			//접속할 SFTP 서버 IP, SFTP 포트, 계정 ID, 계정비밀번호, Pem Key
			sftpUtil.init("211.119.124.9", 22, "xituser", "xituser!@", null);

			ArrayList<String> fileNameList = sftpUtil.findFileNameList("/data/ens/sg-pni-cctv/rcv/");
			System.out.println(fileNameList);

			sftpUtil.mv(
					"/data/mens/sg-pni-cctv/err/20230622111301_49고3736_174번카메라-UDP_101.jpg",
					"/data/mens/sg-pni-cctv/rcv/20230622111301_49고3736_174번카메라-UDP_101.jpg"
					);

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			sftpUtil.disconnect();
		}

		 */
	}
}
