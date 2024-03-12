
## 전자정부 프레임워크 호환성
[호환성 가이드 문서](./egov_compatibility_guide.md)

### log 설정 : application-${spring.profiles.active}.yml

```yml
# application-local.yml 파일 기준
app:
  # parameter 로그 출력
  param.log.enabled: true
  # parameter custom 로그 출력(LoggerAspect)
  param.log.custom.enabled: false
  # MDC logging trace 활성
  mdc.log.trace.enabled: true
  
  # SQL 로그 : p6spy 
  sql.logging.enabled: true



# 로그 파일 위치
logging:
  level:
    root: debug
  file:
    path: D:/data/mens/logs
```
### SQL 로그 : p6spy 또는 log4jdbc
```yml
# SQL 로그를 log4jdbc로 교체할 경우
# pom.xml 변경 : 아래 comment 제거
      <!-- p6spy 사용으로 comment 처리
      <dependency>
      <groupId>org.bgee.log4jdbc-log4j2</groupId>
      <artifactId>log4jdbc-log4j2-jdbc4.1</artifactId>
      <version>1.16</version>
      </dependency>
      --> 
#  application-local.yml 파일 spring.datasouce의 driver 와 url 변경  
    # ================ log4jdbc ===========================
    #driver-class-name: net.sf.log4jdbc.sql.jdbcapi.DriverSpy
    #url: jdbc:log4jdbc:mariadb://211.119.124.122:3306/xplatform?useUnicode=true&characterEncoding=utf-8
    # =====================================================
    # =============== p6spy ===============================
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://211.119.124.122:3306/xplatform?useUnicode=true&characterEncoding=utf-8
    # =====================================================

# sql.logging.enabled: true -> false로 변경 : p6spy log disalbled
# /resources/logback-spring-log4jdbc.xml -> logback-spring.xml 으로 변경
```

### JPA 활성 : application-jpa.yml
```yml
app:
  # jpa 활성 여부
  jpa:
    enabled: false  -> true
```


## 암호화(license) 적용
### 모듈 import : xit-init.jar
  -> 복호화 메소드만 제공
```text
1. /resources/META-INF/spring.factories 파일 생성

# META-INF/spring.factories : xit-init
org.springframework.context.ApplicationContextInitializer=xit.core.init.custom.CustomContextInitalizer
```
### 정보(license) 암호화
```text
# 암호화 필요 정보 encoding : 전자서명 알고리즘- RSA, 블록암호화알고리즘 - SEEDEngine
kr.xit.core.spring.config.custom.bouncy.BouncyUtils 사용
-> packaging시 제외되는 클래스로 로컬에서만 사용
ex) app:
      license:
        path: ${app.data.root.path}/ens/.pem/
        key: 'mxLAM1fAEDPWkFz8'
        data1: 'jz5LT6TlZtewv1GRVN3cI6CgoPgS89Sfh7qSKkCVjjMPOyBKkT386tlnMnjXluTSr8OIvI1pHd96fHxRZHNUBuLeQOkUeWuzkfxlP8C7nDyIrG36T2aontjroAxoNk3oYdIYRVWNs1Iqw39v9xF8NYFFLGqtfv/wnCGxlwTsDwCf9bjAtyd9cTiS27dVrIbrAVKnchgxIF/DUQQc901l4DZ5gsT6aLx6TmzhvAewPK1HfiG9WrMWxpw0TMxt++0Vedh+oZEs48ACMpuHFFh406LynyxxE7boRVbKmh7Tn87gKa6+zzdzIN1kS8sk58Ms1HPvBfvwwnD8qiJItXO6DQ=='
        data2: 'Te3bfmvdiMZdpfRwC3OO9UjwbNkvbf16kqEqn9VqwbVztizA9rvMFshlI0vuqai9Hml31IsNINKg+OYkhmkH6ic1I10r6MNIVl3WL5YxfeK7YBmjvNuGZtKwchlWzhMODsgNAq0aIQVi4kLk5filDaZESY10xlNdbf9c/SKGfJeLZxY7DCchkAgj/ZnmZNqOE2kDAoC+O1ksDNTS0+cr+WsKsoFON0EpNI5B2ElBtnT1LmQQ3R+FNCtp7YJaRZA3RPsata05kKH7sL1J0M6A6HIVxisOU3bjH0hB+60BHJfdlEiXo6RJvsPyXotwe8MYVrHJbIQgsepxSDpMFZe8HA=='
        data3: 't9qYJTU49dbaeezJkzpM2uY3iLIcy/V/VnyVsWIcd0f4QMLJ3cmLZ0QcMyKoR7CL2CuHMnPJz8j7KFOTQRPpeN/Dl4bCpOu+BM3foYpn4wb5HcLdHJxp5CuFmhTfqRGuUxurv6jcqkwmRzPW35UjQLjeKSdv6m+2b84PN4sZSNeMjQDH0QC85yKphHKV8m6bzqUbHLiZwDXndgpq2/YGKdWjPinlH7PZ+L2xfrfhdWXoY9QrHYVOSPogd81EizzyQseif8GAkeUG1OKAOomhyEuTOxtdGbUew59YuuBlUpORgj/Koclyd2shHyne9CJdqnQqAA2mh61V1ZzBUvSIWQ=='


# 암호화된 정보 등록 : ens-api 모듈에 적용
/resources/config/application-${profiles}.yml
- app.license 속성

# 필요시 xit-init 모듈 AppInitHelper.init() method 재정의
```
@ Class Property 대소문자
Swagger API 사용시 대문자 필드 사용 불가   
대문자 필드 사용시 @Schema 속성 및 데이타 전달 불가   
@JsonProperty 또는 전체 클래스 필드인 경우 @JsonNaming 사용  
json <-> java class 변환 적용
```text
    @JsonNaming(value = PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
    public static class RequestDataHeader {

        /**
         * <pre>
         * TRAN_ID : 요청한값 그대로 return
         * 고유번호 : 최대 24
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "TRAN_ID", example = "20230906120000")
        @Size(min = 0, max = 24, message = "TRAN_ID는 24자를 넘을 수 없습니다.")
        //@JsonProperty("TRAN_ID")
        private String tran_id;

        /**
         * CNTY_ID : 요청한값 그대로 return
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "CNTY_ID", example = "kr")
        @Size(min = 0, max = 2, message = "CNTY_ID는 2자를 넘을 수 없습니다.")
        //@JsonProperty("CNTY_ID")
        private String cnty_id;
    }
   /* 
   {
   "TRAN_ID": "20230906120000",
    "CNTY_ID": "kr" 
   }
   -> getTranId, getCntyId
   */
   
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PublickeyReqDataBody {
        /**
         * 공개키 요청일시 (YYYYMMDDHH24MISS)
         */
        @Schema(requiredMode = RequiredMode.REQUIRED, title = "공개키 요청일시", example = "2023090612122259")
        @Size(min = 16, max = 16, message = "요청일시(req_dtim)는 필수 입니다(16자리)")
        private String reqDtim;
    }
    /*
   {
     "req_dtim": "20230906120000",
   }
   -> getReqDtim
   */
```
## Json 데이타 null 필드 제외
@JsonInclude(JsonInclude.Include.NON_NULL)
```text
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
    public static class RequestDataHeader {

        /**
         * <pre>
         * TRAN_ID : 요청한값 그대로 return
         * 고유번호 : 최대 24
         * </pre>
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "TRAN_ID", example = "20230906120000")
        @Size(min = 0, max = 24, message = "TRAN_ID는 24자를 넘을 수 없습니다.")
        private String tranId;

        /**
         * CNTY_ID : 요청한값 그대로 return
         */
        @Schema(requiredMode = RequiredMode.AUTO, title = "CNTY_ID", example = "kr")
        @Size(min = 0, max = 2, message = "CNTY_ID는 2자를 넘을 수 없습니다.")
        private String cntyId;
    }
    
    /*
    {TRAN_ID : null or ""}  --> tranId 필드 제외
    */
```
