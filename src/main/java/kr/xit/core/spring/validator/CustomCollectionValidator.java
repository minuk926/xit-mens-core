package kr.xit.core.spring.validator;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * description :
 *
 * packageName : kr.xit.core.spring.validator
 * fileName    : CustomCollectionValidator
 * author      : limju
 * date        : 2023-05-12
 * ======================================================================
 * 변경일         변경자        변경 내용
 * ----------------------------------------------------------------------
 * 2023-05-12    limju       최초 생성
 *
 * </pre>
 */
@RequiredArgsConstructor
@Component
public class CustomCollectionValidator implements Validator {

    private final SpringValidatorAdapter validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return List.class.equals(clazz);
        //return Collection.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        for (Object object : (Collection)target) {
            validator.validate(object, errors);
        }
    }
}
