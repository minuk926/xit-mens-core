package kr.xit.core.spring.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

/**
 * <pre>
 * description : LocalDateTime > Date 변환
 *               LocalDateTime(Object) > Date(DB Type)
 *               - autoApply = true : 모든 LocalDateTime에 대해 String 적용
 *               - autoApply = true 미사용시 필드에 @Convert(converter = LocalDateConverter.class)
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
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toLocalDate)
                .orElse(null);
    }

}
