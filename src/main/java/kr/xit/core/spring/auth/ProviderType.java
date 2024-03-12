package kr.xit.core.spring.auth;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProviderType {
    GOOGLE,
    FACEBOOK,
    NAVER,
    KAKAO,
    LOCAL;

    /**
     * Serialization을 위해 반드시 필요
     * 미정의시 미 매치값에 대해 IllegalArgumentException 에러 발생 - throw HttpMessageNotReadableException
     * @param s String
     * @return ProviderType
     */
    @JsonCreator
    public static ProviderType from(String s) {
        try{
            return ProviderType.valueOf(s);
        }catch (IllegalArgumentException iae){
            return null;
        }
    }
}
