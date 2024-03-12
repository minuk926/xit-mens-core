package kr.xit.core.support.utils;

import java.io.StringWriter;
import java.util.Map;

import kr.xit.core.spring.util.CoreSpringUtils;
import lombok.AccessLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import lombok.NoArgsConstructor;

/**
 * <pre>
 * description : 객제 변환 utility
 * packageName : kr.xit.core.support.utils
 * fileName    : ConvertHelper
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertHelper {
	private static final Logger log = LoggerFactory.getLogger(ConvertHelper.class);
	private static final ObjectMapper OBJECT_MAPPER = CoreSpringUtils.getObjectMapper();
	private static final JsonFactory JSON_FACTORY = new JsonFactory();

	/**
	 * Object -> Json String
	 * @param o
	 * @return
	 */
	public static String jsonToObject(Object o){
		JsonGenerator generator = null;
		StringWriter writer = new StringWriter();
		
		try{
			OBJECT_MAPPER.registerModule(new JodaModule());
			generator = JSON_FACTORY.createGenerator(writer);
			OBJECT_MAPPER.writeValue(generator, o);
			generator.flush();
			return writer.toString();
		}catch(Exception e){
			log.error("ConvertHelper::jsonToObject", e);
			return null;
		}finally{
			if(generator != null){
				try{
					writer.close();
					generator.close();
				}catch(Exception e){
					log.error("InternalServerError: {}", e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * json string -> Object
	 * @param json
	 * @param clazz
	 * @return T
	 */
	public static <T> T objectToJson(String json, Class<T> clazz){
		try{
			OBJECT_MAPPER.registerModule(new JodaModule());
			return OBJECT_MAPPER.readValue(json, clazz);
		}catch(Exception e){
			log.error("ConvertHelper::objectToJson", e);
			return null;
		}
	}

	/**
	 * Object -> MultiValueMap
	 * @param dto
	 * @return
	 */
	public static MultiValueMap<String, Object> toMultiValueMap(Object dto) {
		try {
			MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			Map<String, Object> map = OBJECT_MAPPER.convertValue(dto, new TypeReference<Map<String, Object>>() {});
			params.setAll(map);

			return params;
		} catch (Exception e) {
			log.error("Url Parameter 변환중 오류가 발생했습니다. requestDto={}", dto, e);
			throw new IllegalStateException("Url Parameter 변환중 오류가 발생했습니다.");
		}
	}
}
