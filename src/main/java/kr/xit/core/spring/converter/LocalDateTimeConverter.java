package kr.xit.core.spring.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <pre>
 * description : LocalDateTime > Timestamp 변환
 *               LocalDateTime(Object) > Timestamp(DB Type)
 *               - autoApply = true : 모든 LocalDateTime에 대해 String 적용
 *               - autoApply = true 미사용시 필드에 @Convert(converter = LocalDateTimeConverter.class)
 * packageName : kr.xit.core.spring.converter
 * fileName    : LocalDateTimeConverter
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
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return Optional.ofNullable(localDateTime)
                .map(Timestamp::valueOf)
                .orElse(null);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return Optional.ofNullable(timestamp)
                .map(Timestamp::toLocalDateTime)
                .orElse(null);
    }

}
