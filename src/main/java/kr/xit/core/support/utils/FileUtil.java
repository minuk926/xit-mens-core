package kr.xit.core.support.utils;

import static java.nio.file.Files.probeContentType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.BadRequestException;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.model.FileDTO.FileInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * <pre>
 * description : File Utils
 *
 * packageName : kr.xit.core.support.utils
 * fileName    : FileUtil
 * author      : limju
 * date        : 2023-07-20
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-07-20    limju       최초 생성
 *
 * </pre>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    /**
     * <pre>
     * 서버로 파일을 전송(저장) 한다
     * @param filePath 저장 경로
     * @param files MultipartFile[]
     * @return
     * </pre>
     */
    public static List<FileInfo> fileTransferServer(final String filePath, final MultipartFile[] files) {
        final String fileGenerateName = UUIDUtils.generateLengthUuid(20);
        final List<FileInfo> fileList = new ArrayList<>();
        int idx = 1;

        for(MultipartFile mf : files) {
            String fileName = StringUtils.cleanPath(
                Objects.requireNonNull(mf.getOriginalFilename()));
            long size = mf.getSize();
            String fileExt = FilenameUtils.getExtension(fileName).toUpperCase();

            Path uploadPath = Paths.get(filePath);

            if(!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException ioe) {
                    throw BizRuntimeException.create("Create directory error");
                }
            }

            try (InputStream inputStream = mf.getInputStream()) {
                Path path = uploadPath.resolve(fileGenerateName);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                throw BizRuntimeException.create("Could not save file: " + fileName);
            }
            fileList.add(FileInfo.builder()
                .atchFileId(fileGenerateName)
                .fileSn("00"+idx++)
                .fileStreCours(filePath)
                .orignlFileNm(fileName)
                .streFileNm("")
                .fileExtsn(fileExt)
                .fileSize(size)
                .build());
        }
        return fileList;
    }

    /**
     *
     * @param fileGenerateName 파일이름
     * @return Resource 파일객체 리소스
     */
    public static Resource getFileAsResource(final String filePath, final String fileGenerateName) {
        final String found = Stream.of(Objects.requireNonNull(new File(filePath).list()))
            .filter(file -> file.startsWith(fileGenerateName))
            .findFirst()
            .orElse(null);

        if (Objects.isNull(found)) {
            throw new BadRequestException("File not found. file = [" + fileGenerateName + "]");
        }

        Path foundFile = Paths.get(filePath).resolve(Paths.get(Objects.requireNonNull(found)));

        try {
            return new UrlResource(foundFile.toUri());
        } catch (MalformedURLException e) {
            throw BizRuntimeException.create("Internal Server Error.");
        }
    }

    public static byte[] getFileBytesFrom(String path) {

        // 파일 객체 생성
        File file = new File(path);
        byte[] fileBytes = new byte[(int)file.length()];

        try(FileInputStream fis = new FileInputStream(file);){
            fis.read(fileBytes);

        } catch (IOException e) {
            throw BizRuntimeException.create(e.getMessage());
        }
        return fileBytes;
    }

    public static void saveFile(String path, String fileName, byte[] fileData) {
        Path p = Paths.get(path);

        if(!Files.isDirectory(p)){
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                throw BizRuntimeException.create(e.getMessage());
            }
        }
        try(FileOutputStream fos = new FileOutputStream(path + "/" + fileName);){
            fos.write(fileData);
        } catch (IOException e) {
            throw BizRuntimeException.create(e.getMessage());
        }
    }

    /**
     * 파일명 + 파일경로를 받아 MultipartFile 생성 return
     * @param fileNm
     * @param filePath
     * @return MultipartFile
     */
    @SuppressWarnings("DuplicatedCode")
    public static MultipartFile createMutipartFile(final String fileNm, final String filePath){
        File file = new File(filePath+"/"+fileNm);
        try {
            FileItem fileItem = new DiskFileItem(fileNm, probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

            try(InputStream input = Files.newInputStream(file.toPath());
                OutputStream os = fileItem.getOutputStream()) {
                IOUtils.copy(input, os);
            }catch(IOException e){
                throw BizRuntimeException.create(e.getMessage());
            }
            return new CommonsMultipartFile(fileItem);

        } catch (IOException ex) {
            throw BizRuntimeException.create(ex.getMessage());
        }
    }

    /**
     * binary(byte) 파일객체 에서 MultipartFile 객체 GET
     * @param fileNm 원본파일 명
     * @param filePath 업로드 경로 - MultipartFile 객체 생성을 위한 임시 경로
     * @param data byte 파일 객체
     * @param isFileDelete 임시 파일 삭제 여부
     * @return MultipartFile 생성된 MultipartFile
     */
    @SuppressWarnings("DuplicatedCode")
    public static MultipartFile createMutipartFileFromBytes(final String fileNm, final String filePath, final byte[] data, boolean isFileDelete){
        File file = new File(filePath+"/"+fileNm);
        try {
            FileUtils.writeByteArrayToFile(file, data);

            FileItem fileItem = new DiskFileItem(fileNm, probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

            try(InputStream input = Files.newInputStream(file.toPath());
                OutputStream os = fileItem.getOutputStream()) {
                IOUtils.copy(input, os);
            }catch(IOException e){
                throw BizRuntimeException.create(e.getMessage());
            }
            if(isFileDelete && file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            return new CommonsMultipartFile(fileItem);
        } catch (IOException ex) {
            throw BizRuntimeException.create(ex.getMessage());
        }
    }

    /**
     * zip 파일 생성
     * @param filePath source file path
     * @param fileNameList source file name list
     * @param zipFilePath zip file folder
     * @param zipFileName zip file name
     */
    public static String compressZip(final String filePath, final List<String> fileNameList, final String zipFilePath, final String zipFileName) {
        List<File> fileList = new ArrayList<>();

        fileNameList.forEach(s -> fileList.add(new File(filePath, s)));
        return compressZip(fileList, zipFilePath, zipFileName);
    }

    /**
     * Zip 파일 생성
     * @param fileList zip file name list
     * @param zipFilePath zip file folder
     * @param zipFileName zip file name
     */
    public static String compressZip(final List<File> fileList, final String zipFilePath, final String zipFileName) {
        File zipFile = new File(zipFilePath, zipFileName);

        byte[] buff = new byte[4096];
        // 압축파일 생성
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))){

            // 파일 압축
            fileList.forEach(f -> {
                try(FileInputStream fis = new FileInputStream(f)){
                    // 압축파일 지정
                    ZipEntry ze = new ZipEntry(f.getName());
                    zos.putNextEntry(ze);

                    // 압축 파일에 추가
                    int len;
                    while ((len = fis.read(buff)) > 0)  zos.write(buff, 0, len);

                    zos.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            return zipFilePath + "/" + zipFileName;

        } catch (FileNotFoundException e) {
            log.error("zip파일 생성 오류::{}", e.getLocalizedMessage());
            throw BizRuntimeException.create("zip 파일생성 오류[대상파일 미존재]");
        } catch (IOException e) {
            log.error("zip파일 생성 오류::{}", e.getLocalizedMessage());
            throw BizRuntimeException.create("zip 파일생성 오류[대상파일 미존재]");
        }
    }

    /**
     * <pre>
     * path 경로의 모든 파일 삭제(directory 포함)
     * @param path String 삭제할 경로
     * </pre>
     */
    public static void removeDirectyAndFiles(String path){
        try {
            // 작업 파일 삭제
            Path dir = Paths.get(path);
            Files.walk(dir) // Traverse the file tree in depth-first order
                .sorted(Comparator.reverseOrder())
                .forEach(f -> {
                    try {
                        Files.delete(f);  //delete each file or directory
                    } catch (IOException e) {
                        log.error(e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                });
        }catch (IOException ie){
            log.error(ie.getLocalizedMessage());
            ie.printStackTrace();
        }
    }
}
