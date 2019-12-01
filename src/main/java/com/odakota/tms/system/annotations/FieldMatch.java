package com.odakota.tms.system.annotations;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation annotation to validate that 2 fields have the same value. An array of fields and their matching
 * confirmation fields can be supplied.
 * <p>
 * Example:
 * <pre>
 * <code>
 *   @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
 *   @FieldMatch.List({
 *      @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
 *      @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
 *   })
 *  </code>
 *  </pre>
 *
 * @author haidv
 * @version 1.0
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatch.FieldMatchValidator.class)
public @interface FieldMatch {

    String message() default "The fields must match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String first();

    /**
     * @return The second field
     */
    String second();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {

        FieldMatch[] value();
    }

    class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

        private String firstFieldName;

        private String secondFieldName;

        private String message;

        @Override
        public void initialize(final FieldMatch constraintAnnotation) {
            firstFieldName = constraintAnnotation.first();
            secondFieldName = constraintAnnotation.second();
            message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            boolean valid;
            try {
                final Object firstObj = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
                final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);

                valid = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
            } catch (final Exception ignore) {
                valid = false;
            }

            if (!valid) {
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(secondFieldName)
                       .addConstraintViolation().disableDefaultConstraintViolation();
            }

            return valid;
        }
    }
}
