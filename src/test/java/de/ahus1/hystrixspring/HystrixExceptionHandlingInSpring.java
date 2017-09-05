package de.ahus1.hystrixspring;

import com.netflix.hystrix.Hystrix;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * @author Alexander Schwartz 2016
 */
@SpringBootTest(classes = {
        SpringApplication.class
}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class})
@TestPropertySource(properties = {"hystrix.command.default.circuitBreaker.requestVolumeThreshold=1"})
public class HystrixExceptionHandlingInSpring {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private ExternalInterfaceWrappedWithHystrix externalInterface;

    @Before
    public void before() {
        Hystrix.reset();
    }

    @Test
    public void should_receive_primary_exception_on_first_call() {
        // when the primary function is called, an exception is thrown
        Assertions.assertThatExceptionOfType(PrimaryException.class).isThrownBy(() -> externalInterface.primary());
    }

    @Test
    public void should_receive_java_runtime_exception_on_circuit_breaker_open() throws InterruptedException {
        // given ...

        // ... there was already one unsuccessful call
        Assertions.assertThatExceptionOfType(PrimaryException.class).isThrownBy(() -> externalInterface.primary());

        // ... and the metrics have been calculated
        // wait at least hystrix.command.default.metrics.healthSnapshot.intervalInMilliseconds to calculate metrics
        // the default is 500 ms
        Thread.sleep(1000);

        // then ...
        // ... a runtime exception with circuit breaker open is raised
        Assertions.assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> externalInterface.primary())
                .withMessage("Hystrix circuit short-circuited and is OPEN");

    }
}

