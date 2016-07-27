package com.wm.lock.http.template;

import android.content.Context;

import org.springframework.web.client.RestTemplate;

public class RestTemplateProviderHttp implements RestTemplateProvider {

    @Override
    public RestTemplate getRestTemplate(Context ctx, String rootUrl) {
        return RestTemplateClientHttp.getRestTemplate();
    }
}
