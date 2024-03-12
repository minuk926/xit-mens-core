package kr.xit.biz.ens.model.epost;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import kr.xit.biz.ens.model.cmm.CmmEnsRequestDTO;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.model.IApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;

/**
 * <pre>
 * description : EPost DTO
 *               - 우편물 종적 추적
 *                 Request : {@link EpostTraceRequest}
 *                 Response : {@link EpostTraceResponse}
 * packageName : kr.xit.biz.ens.model.epost
 * fileName    : EPostDTO
 * author      : limju
 * date        : 2023-10-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-10-04    limju       최초 생성
 *
 * </pre>
 */
public class EPostDTO {
    /**
     * <pre>
     * EPost 우편물 종적 추적 DTO
     * Request: EpostTraceRequest
     * Response: EpostTraceResponse
     * </pre>
     */
    @Schema(name = "EpostTraceRequest", description = "EPost 종적추적 요청 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @EqualsAndHashCode(callSuper = false)
    public static class EpostTraceRequest extends CmmEnsRequestDTO {

        /**
         * <pre>
         * 공인데이터포털에서 발급받은 인증키(URL Encode)
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "인증키", example = " ")
        @Size(min = 30, max = 100, message = "apiKey는 필수 입니다.")
        private String serviceKey;

        /**
         * <pre>
         * 등기번호 - 13자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "등기번호", example = "1900472677718|1900472677695")
        @Size(min = 13, max = 13, message = "등기번호는 필수 입니다(13자리)")
        private String rgist;
    }

    /**
     * <pre>
     * EPost 우편물 종적 추적 결과 DTO
     * </pre>
     */
    @Schema(name = "EpostTraceResponse", description = "EPost 종적추적 요청 결과 DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "response")
    @Builder
    public static class EpostTraceResponse implements IApiResponse {

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @XmlElement(name = "header")
        private EpostTraceResHeader header;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        @XmlElement(name = "trackInfo")
        private EpostTraceResTrackInfo trackInfo;

        public EpostTraceResTrackInfo result(){
            if("N".equals(this.header.successYN)){
                throw BizRuntimeException.create(this.header.errorMessage);
            }

            // 배달 완료시 배달시간 set
            if(ObjectUtils.isNotEmpty(this.trackInfo.receiveDate)){
                String time = trackInfo.getDetaileTrackList().get(trackInfo.getDetaileTrackList().size()-1)
                    .getTime();
                this.trackInfo.setReceiveDate(this.trackInfo.receiveDate + time.replace(":", ""));
            }
            return this.trackInfo;
        }
    }

    /**
     * <pre>
     * EPost 우편물 종적 추적 결과 header DTO
     * </pre>
     */
    @Schema(name = "EpostTraceResHeader", description = "EPost 종적추적 요청 결과 header DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "header")
    public static class EpostTraceResHeader {

        /**
         * <pre>
         * API 요청 등기 번호 : 필수 - 13자리
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "등기번호")
        private String requestRegiNo;

        /**
         * <pre>
         * 응답시간 : 필수 - 20 자리
         * yyyy-mm-dd HH:mi:ss
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "응답시간")
        private String responseTime;

        /**
         * <pre>
         * 성공여부 : 필수 - Y|N
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "성공여부")
        private String successYN;

        /**
         * <pre>
         * 에러메세지 : max 100
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "에러메세지")
        private String errorMessage;
    }

    /**
     * <pre>
     * EPost 우편물 종적 추적 결과 trackInfo DTO
     * </pre>
     */
    @Schema(name = "EpostTraceResTrackInfo", description = "EPost 종적추적 요청 결과 trackInfo DTO")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "trackInfo")
    @Builder
    public static class EpostTraceResTrackInfo {

        /**
         * <pre>
         * 등기번호 : 필수 - 13자리
         * 조회된 등기번호
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "등기번호")
        private String regiNo;

        /**
         * <pre>
         * 보낸 사람 : 필수 - max 50
         * 발신인 명
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "보낸 사람")
        private String senderName;

        /**
         * <pre>
         * 보낸 날자 : 필수 - 10자리
         * 발신일자 yyyy-mm-dd
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "보낸 날자")
        private String senderData;

        /**
         * <pre>
         * 받는 사람 : 필수 - max 50
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "받는 사람")
        private String receiveName;

        /**
         * <pre>
         * 받은 날자 : 필수 - 10자리
         * 수신일자 yyyy-mm-dd
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "받은 날자")
        private String receiveDate;

        /**
         * <pre>
         * 배달상태: 필수 - max 10
         * 배달완로|미배달
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "배달 상태")
        private String trackState;

        /**
         * <pre>
         * 서비스 종류: 필수 - max 10
         * 국제우편인 경우에 해당 서비스 종류가 표기 : EMS
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "서비스 종류")
        private String expressType;

        @Schema(requiredMode = RequiredMode.REQUIRED)
        private List<EpostTraceResDtlTrack> detaileTrackList;

        /**
         * <pre>
         * EPost 우편물 종적 추적 결과 detaileTrackList DTO
         * </pre>
         */
        @Schema(name = "EpostTraceResDtlTrack", description = "EPost 종적추적 요청 결과 detaileTrackList DTO")
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlRootElement(name = "detaileTrackList")
        private static class EpostTraceResDtlTrack {
            /**
             * <pre>
             * 정렬순서 : 필수 - 1자리
             * 배송 진행 사항에 대한 정렬 순서
             * </pre>
             */
            @Schema(requiredMode = RequiredMode.REQUIRED, title = "정렬 순서")
            private String sortNo;

            /**
             * <pre>
             * 처리일 : 필수 - 10자리
             * 배송진행 처리일 yyyy-mm-dd
             * </pre>
             */
            @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리일")
            private String date;

            /**
             * <pre>
             * 처리시간 : 필수 - 5자리
             * 배송진행 처리시간 mi:ss
             * </pre>
             */
            @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리 시간")
            private String time;

            /**
             * <pre>
             * 처리상태 : 필수 - max 10
             * 배송진행 처리상태 접수
             * </pre>
             */
            @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리 상태")
            private String status;

            /**
             * <pre>
             * 처리장소 : 필수
             * 배송진행 처리장소 접수
             * </pre>
             */
            @Schema(requiredMode = RequiredMode.REQUIRED, title = "처리 장소")
            private String location;

            /**
             * <pre>
             * 상세설명 : 필수 - max 500
             * 배송진행  상세설명 - 국제우편인 경우 상세한 설명 표기
             * </pre>
             */
            @Schema(requiredMode = RequiredMode.REQUIRED, title = "상세 설명")
            private String remark;

        }
    }
}
