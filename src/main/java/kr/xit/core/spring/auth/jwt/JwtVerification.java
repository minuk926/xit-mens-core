package kr.xit.core.spring.auth.jwt;

import egovframework.com.cmm.model.LoginVO;
import egovframework.com.cmm.util.EgovStringUtil;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import javax.servlet.http.HttpServletRequest;
import kr.xit.core.consts.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtVerification {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final Logger log = LoggerFactory.getLogger(JwtVerification.class);
	
	public boolean isVerification(HttpServletRequest request) {
		
		// step 1. request header에서 토큰을 가져온다.
		String jwtToken = EgovStringUtil.isNullToString(request.getHeader(Constants.JwtToken.HEADER_NAME.getCode()));

		return isVerification(jwtToken);
	}

	public boolean isVerification(String jwtToken) {
		// step 1. request header에서 토큰을 가져온다.

		// step 2.비교를 위해 loginVO를 가져옴
		LoginVO loginVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();

		// step 3. 토큰에 내용이 있는지 확인 & 토큰 기간이 자났는지를 확인해서 username값을 가져옴
		// Exception 핸들링 추가처리
		String username = null;

		try {
			username = jwtTokenProvider.getUsernameFromToken(jwtToken);
		} catch (IllegalArgumentException e) {
			log.debug("Unable to get JWT Token");
		} catch (ExpiredJwtException e) {
			log.debug("JWT Token has expired");
		} catch (MalformedJwtException e) {
			log.debug("JWT strings must contain exactly 2 period characters");
		} catch (UnsupportedJwtException e) {
			log.debug("not support JWT token.");
		}

		// step 4. 가져온 username이랑 2에서 가져온 loginVO랑 비교해서 같은지 체크 & 이 과정에서 한번 더 기간 체크를 한다.
		if (username == null || !(jwtTokenProvider.validateToken(jwtToken, loginVO))) {
			log.debug("jwtToken not validate");
			return false;
		}
		return true;
	}
}
