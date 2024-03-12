package kr.xit.biz.common.service;

import kr.xit.core.model.FileDTO.FileInfo;
import kr.xit.core.model.FileDTO.FileRequest;
import kr.xit.core.model.FileDTO.FileResponse;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.biz.common.service
 * fileName    : ICmmFileService
 * author      : limju
 * date        : 2023-12-01
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-12-01    limju       최초 생성
 *
 * </pre>
 */
public interface ICmmFileService {

    /**
     * <pre>
     * 파일 업로드 : 각 프로젝트에서 구현
     * @param fileRequest FileRequest
     * @return FileResponse
     * </pre>
     */
    FileResponse uploadFile(final FileRequest fileRequest);
    FileInfo findFileInfo(final FileInfo dto);
}
