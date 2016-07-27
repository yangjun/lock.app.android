package com.wm.lock.http.template;

import android.content.Context;
import android.text.TextUtils;

import org.springframework.web.client.RestTemplate;

public class RestTemplateProviderSsl implements RestTemplateProvider {
    
    @Override
    public RestTemplate getRestTemplate(Context ctx, String rootUrl) {
        String[] strs = rootUrl.split(":");
        int port = 443;
        if (strs.length == 3 && !TextUtils.isEmpty(strs[2])) {
            String portStr = strs[2];
            int pos = portStr.indexOf("/");
            if (pos >= 0) {
                portStr = portStr.substring(0, pos);
            }
            port = Integer.parseInt(portStr);
        }
        return RestTemplateClientSsl.getRestTemplate(ctx, port);
    }

}
