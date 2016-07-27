package com.wm.lock.http.template;

import android.content.Context;

import org.springframework.web.client.RestTemplate;

public interface RestTemplateProvider {
    
    RestTemplate getRestTemplate(Context ctx, String rootUrl);
}
