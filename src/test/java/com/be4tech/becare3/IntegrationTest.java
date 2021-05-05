package com.be4tech.becare3;

import com.be4tech.becare3.Becare3App;
import com.be4tech.becare3.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Becare3App.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
