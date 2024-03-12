package kr.xit.core.support.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import kr.xit.core.exception.BizRuntimeException;
import kr.xit.core.spring.util.CoreSpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * <pre>
 * description : JSON Utility
 *
 * packageName : kr.xit.core.support.utils
 * fileName    : JsonUtils
 * author      : limju
 * date        : 2023-09-04
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-09-04    limju       최초 생성
 *
 * </pre>
 * @see kr.xit.core.spring.config.support.CustomJacksonConfig
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

	private static final ObjectMapper OM = CoreSpringUtils.getObjectMapper();

	/**
	 * json string 인지 판별
	 * @param str String
	 * @return boolean
	 */
	public static boolean isJson(final String str) {
		try {
			OM.readTree(str);
			return true;
		} catch (JsonProcessingException e) {
			return false;
		}
	}

	/**
	 * Object -> json string
	 * @return String
	 * @param obj Object
	 */
	public static String toJson(final Object obj) {
		try {
			return ObjectUtils.isNotEmpty(obj)? OM.writeValueAsString(obj) : null;
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * Json string -> class로 변환
	 * @return T
	 * @param str String
	 * @param cls Class
	 */
	public static <T> T toObject(final String str, final Class<T> cls) {
		try {
			return ObjectUtils.isNotEmpty(str)? OM.readValue(str, cls) : null;
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * Object -> class로 변환
	 * @param obj Object
	 * @param cls Class
	 * @return T
	 */
	public static <T> T toObjByObj(final Object obj, final Class<T> cls) {
		try {
			return ObjectUtils.isNotEmpty(obj)? OM.convertValue(obj, cls) : null;
		} catch (IllegalArgumentException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * xml String -> cls
	 * @param xml String
	 * @param cls Class
	 * @return cls Class
	 */
	public static <T> T toObjByXml(String xml, final Class<T> cls){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			return toObject(OM.writeValueAsString(document), cls);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw BizRuntimeException.create(e.getMessage());
		}
	}


	/**
	 * Json string -> class list로 변환
	 * @return		T
	 * @param		str
	 * @param		cls
	 * @throws IOException
	 */
	public static <T> List<T> toObjectList(final String str, final Class<T> cls) {
		if(ObjectUtils.isNotEmpty(str)){
			try {
				return OM.readValue(str, OM.getTypeFactory().constructCollectionType(List.class, cls));
			} catch (JsonProcessingException e) {
				throw BizRuntimeException.create(e.getLocalizedMessage());
			}
		}else {
			return null;
		}
	}

	/**
	 * JSON 문자열 -> Map 구조체로 변환
	 * @param str String	str
	 * @return Map
	 */
	public static Map<String, Object> toMap(final String str) {
		try {
			return ObjectUtils.isNotEmpty(str)? OM.readValue(str, new TypeReference<Map<String, Object>>(){}) : null;
		} catch (JsonProcessingException e) {
			throw BizRuntimeException.create(e.getLocalizedMessage());
		}
	}

	/**
	 * Object
	 * -> MultiValueMap<String, String> return
	 * @param obj Object
	 * @return MultiValueMap<String, String>
	 */
	public static MultiValueMap<String, String> toMultiValue(final Object obj){
		if(ObjectUtils.isEmpty(obj))	return null;
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		JSONObject jsonObj = toObjByObj(obj, JSONObject.class);
		for (Object key : jsonObj.keySet()) {
			formData.add((String) key, (String) jsonObj.get(key));
		}
		return formData;
	}

	/**
	 * Object의 key와 value를 추출
	 * -> key배열, value배열의 JSONObject return
	 * @param obj key, value를 추출할 Object
	 * @param keyName key배열의 JSON key name
	 * @param valueName value배열의 JSON key name
	 * @return JSONObject key배열, value배열의 JSONObject
	 */
	public static JSONObject extractObjKeyValue(final Object obj, final String keyName, final String valueName){
		return extractJsonKeyValue(toObjByObj(obj, JSONObject.class), keyName, valueName);
	}

	/**
	 * JSONObject의 key와 value를 추출
	 * -> key배열, value배열의 JSONObject return
	 * @param json
	 * @param keyName key배열의 JSON key name
	 * @param valueName value배열의 JSON key name
	 * @return JSONObject key배열, value배열의 JSONObject
	 */
	public static JSONObject extractJsonKeyValue(final JSONObject json, final String keyName, final String valueName){
		final JSONObject rtnJson = new JSONObject();
		final JSONArray keys = new JSONArray();
		final JSONArray values = new JSONArray();
		for (Object key : json.keySet()) {
			Object value = json.get(key);

			if (value instanceof JSONObject jo)
				extractJsonKeyValue(jo, keyName, valueName);
			else if (value instanceof JSONArray ja)
				ja.forEach(obj -> extractJsonKeyValue((JSONObject)obj, keyName, valueName));
			else{
				//System.out.println(key + ", " + value);
				keys.add(String.valueOf(key));
				values.add(value);
			}
		}
		rtnJson.put(keyName, keys);
		rtnJson.put(valueName, values);
		return rtnJson;
	}

	/**
	 * JSONArray의 key와 value를 추출
	 * -> key배열, value배열의 JSONObject return
	 * @param jsons JSONArray
	 * @param keyName key배열의 JSON key name
	 * @param valueName value배열의 JSON key name
	 * @return JSONObject key배열, value배열의 JSONObject
	 */
	public static JSONObject extractJsonArrayKeyValue(final net.sf.json.JSONArray jsons, final String keyName, final String valueName){
		final JSONObject rtnJson = new JSONObject();
		final JSONArray keys = new JSONArray();
		final JSONArray values = new JSONArray();
		for(int i = 0; i<jsons.size(); i++){
			JSONObject json = extract(jsons.getJSONObject(i), keyName, valueName);
			if(i == 0)	rtnJson.put(keyName, json.get(keyName));
			values.add(json.get(valueName));
		}
		rtnJson.put(valueName, values);
		return rtnJson;
	}

	private static JSONObject extract(net.sf.json.JSONObject json, String keyName, String valueName) {
		final JSONObject rtnJson = new JSONObject();
		final JSONArray keys = new JSONArray();
		final JSONArray values = new JSONArray();
		for (Object key : json.keySet()) {
			Object value = json.get(key);

			if (value instanceof JSONObject jo)
				extractJsonKeyValue(jo, keyName, valueName);
			else if (value instanceof JSONArray ja)
				ja.forEach(obj -> extractJsonKeyValue((JSONObject)obj, keyName, valueName));
			else{
				//System.out.println(key + ", " + value);
				keys.add(String.valueOf(key));
				values.add(value);
			}
		}
		rtnJson.put(keyName, keys);
		rtnJson.put(valueName, values);
		return rtnJson;
	}

	/**
	 * Json 데이터 보기 좋게 변환.
	 * @param obj Object	json
	 * @return String
	 */
	public static String jsonEnterConvert(final Object obj) {

		try {
			return jsonEnterConvert((JsonUtils.toJson(obj)));
		} catch(Exception e) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * Json 데이터 보기 좋게 변환.
	 * @param json String	json
	 * @return	String
	 */
	private static String jsonEnterConvert(String json) {

		if( json == null || json.length() < 2 )
			return json;

		final int len = json.length();
		final StringBuilder sb = new StringBuilder();
		char c;
		String tab = "";
		boolean beginEnd = true;
		for( int i=0 ; i<len ; i++ ) {
			c = json.charAt(i);
			switch( c ) {
				case '{': case '[':{
					sb.append( c );
					if( beginEnd ){
						tab += "\t";
						sb.append("\n");
						sb.append( tab );
					}
					break;
				}
				case '}': case ']':{
					if( beginEnd ){
						tab = tab.substring(0, tab.length()-1);
						sb.append("\n");
						sb.append( tab );
					}
					sb.append( c );
					break;
				}
				case '"':{
					if( json.charAt(i-1)!='\\' )
						beginEnd = ! beginEnd;
					sb.append( c );
					break;
				}
				case ',':{
					sb.append( c );
					if( beginEnd ){
						sb.append("\n");
						sb.append( tab );
					}
					break;
				}
				default :{
					sb.append( c );
				}
			} // switch end

		}
		if( sb.length() > 0 )
			sb.insert(0, '\n');
		return sb.toString();
	}
}

