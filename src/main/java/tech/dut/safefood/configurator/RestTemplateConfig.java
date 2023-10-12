package tech.dut.safefood.configurator;

/**
 * The class for rest template configuration.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "restTemplate")
    public RestTemplate aiRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(180000);
        factory.setReadTimeout(180000);

        RestTemplate restTemplate = new RestTemplate(factory);

        return restTemplate;
    }
}