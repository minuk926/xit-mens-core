package kr.xit.core.spring.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang.StringUtils;

/**
 * <pre>
 * description : LocalDateTime > String 변환
 *               LocalDateTime(Object) > String(DB Type)
 *               - autoApply = true : 모든 LocalDateTime에 대해 String 적용
 *               - autoApply = true 미사용시 필드에 @Convert(converter = ToStringLocalDateTimeConverter.class)
 * packageName : kr.xit.core.spring.converter
 * fileName    : FromLocalDateTimeToStringConverter
 * author      : julim
 * date        : 2023-04-28
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-04-28    julim       최초 생성
 *
 * </pre>
 */
@Converter//(autoApply = true)
public class ToStringLocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {


    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {

        if(attribute == null) {
            return "";
        } else {
            return attribute.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }


    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {

        if(StringUtils.isEmpty(dbData)) {
            return null;
        } else {
            return LocalDate.parse(dbData, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        }
    }
}
