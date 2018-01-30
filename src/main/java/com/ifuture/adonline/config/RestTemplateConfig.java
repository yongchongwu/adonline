package com.ifuture.adonline.config;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonConverter.setSupportedMediaTypes(ImmutableList
                    .of(new MediaType(MediaType.APPLICATION_JSON,
                            MappingJackson2HttpMessageConverter.DEFAULT_CHARSET),
                        new MediaType("application", "*+json",
                            MappingJackson2HttpMessageConverter.DEFAULT_CHARSET),
                        new MediaType(MediaType.TEXT_PLAIN,
                            MappingJackson2HttpMessageConverter.DEFAULT_CHARSET),
                        new MediaType(MediaType.TEXT_HTML,
                            MappingJackson2HttpMessageConverter.DEFAULT_CHARSET)));
            }
        }
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//ms
        factory.setConnectTimeout(5000);//ms
        return factory;
    }
}
