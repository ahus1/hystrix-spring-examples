package de.ahus1.hystrixspring;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Schwartz 2017
 */
@Component
public class ExternalInterfaceWrappedWithHystrix {

    @HystrixCommand(commandKey = "external", fallbackMethod = "fallback")
    public String primary() {
        throw new PrimaryException();
    }

    public String fallback() {
        throw new FallbackException();
    }

}
