package com.wm.lock.http;

import android.content.Context;
import android.text.TextUtils;

import com.wm.lock.LockConfig;
import com.wm.lock.LockConstants;
import com.wm.lock.core.cache.CacheManager;
import com.wm.lock.entity.AttachmentUploadResult;
import com.wm.lock.entity.params.AttachmentUploadParam;
import com.wm.lock.http.template.RestTemplateProvider;
import com.wm.lock.http.template.RestTemplateProviderHttp;
import com.wm.lock.http.template.RestTemplateProviderSsl;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@EBean
public class Rest {

    @RestService
    RestClient mRestClient;

    @Bean
    RestErrorInjector mErrorHandler;

    @RootContext
    Context mCtx;

    @AfterInject
    void init() {
        resetClient();
    }

//    public String test() {
//        try {
//            final String url = "http://web-sso.demo.jinganiam.com:10008/ngiam-rst/v1/sdk/login/mobile";
//            resetClient(url);
//
//            Map<String, String> map = new HashMap<>();
//            map.put("device", Build.MANUFACTURER + Build.MODEL);
//            map.put("imei", HardwareUtils.getImeiNumber(mCtx));
//            map.put("liscense", HardwareUtils.getImeiNumber(mCtx));
//            map.put("os", "android");
//            map.put("osversion", Build.VERSION.RELEASE);
//
//            try {
//                mRestClient.setHeader("Client-Profile", com.wm.lock.core.security.SecurityManager.base64Encode(GsonUtils.toJson(map)));
//            }
//            catch (Exception e) {
//                throw new RemoteException(e);
//            }
//
//            final UserLoginParam param = new UserLoginParam();
//            param.setUserName("admin1");
//            param.setPassword("aA123.");
//            return mRestClient.test(param).getData().getUserApiKey();
//        }
//        finally {
//            resetClient();
//        }
//    }

    public AttachmentUploadResult uploadAttachment(AttachmentUploadParam param) {
        final MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.set("file", new FileSystemResource(param.getFile()));
        if (!TextUtils.isEmpty(param.getAliases())) {
            data.set("aliases", param.getAliases());
        }
        try {
            mRestClient.setHeader(LockConstants.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA);
            return mRestClient.uploadAttachment(data);
        }
        finally {
            mRestClient.setHeader(LockConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }
    }

    private void resetClient() {
        resetClient(getRootUrl());
    }

    private void resetClient(final String url) {
        mRestClient.setRootUrl(url);
        mRestClient.setRestTemplate(getRestTemplate(url));
        mRestClient.setHeader(LockConstants.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        mRestClient.setRestErrorHandler(mErrorHandler);
    }

    private RestTemplate getRestTemplate(final String url) {
        RestTemplateProvider provider = url.startsWith("https") ? new RestTemplateProviderSsl() : new RestTemplateProviderHttp();
        final RestTemplate template = provider.getRestTemplate(mCtx, url);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RestInterceptor());
        template.setInterceptors(interceptors);

        return template;
    }

    private String getRootUrl() {
        return CacheManager.getInstance().getString(LockConstants.HTTP_SERVER, LockConfig.HTTP_SERVER, CacheManager.CHANNEL_PREFERENCE);
    }

}
