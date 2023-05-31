package com.rdalabs.automation.testing.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Documented
public @interface Priority {

  enum PriorityValue {
    CRITICAL, HIGH, MEDIMUM, LOW

  }

  PriorityValue value() default PriorityValue.LOW;

}
