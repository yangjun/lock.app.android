package com.wm.lock.http.template;

import com.wm.lock.LockConfig;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RestTemplateClientHttp {
    
    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getRequestFactory());
        restTemplate.setMessageConverters(getMessageConvertors());
        return restTemplate;
    }

    private static ClientHttpRequestFactory getRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setReadTimeout(LockConfig.HTTP_READ_TIMEOUT);
        clientHttpRequestFactory.setConnectTimeout(LockConfig.HTTP_CONN_TIMEOUT);
        return clientHttpRequestFactory;
    }

    private static List<HttpMessageConverter<?>> getMessageConvertors() {
        List<HttpMessageConverter<?>> result = new ArrayList<>();
        result.add(new MappingJacksonHttpMessageConverter());
        result.add(new FormHttpMessageConverter());
        result.add(new ResourceHttpMessageConverter());
        result.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return result;
    }

}
