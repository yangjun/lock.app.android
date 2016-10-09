package com.wm.lock.http;

import com.wm.lock.entity.AttachmentUploadResult;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.androidannotations.api.rest.RestClientHeaders;
import org.androidannotations.api.rest.RestClientRootUrl;
import org.androidannotations.api.rest.RestClientSupport;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.MultiValueMap;

/**
 * Created by wangmin on 16/7/27.
 */
@Rest(converters = {MappingJacksonHttpMessageConverter.class})
public interface RestClient extends RestClientErrorHandling,
        RestClientHeaders, RestClientRootUrl, RestClientSupport {

//    @Post("")
//    @Accept(MediaType.APPLICATION_JSON)
//    @RequiresHeader({LockConstants.CONTENT_TYPE, "Client-Profile"})
//    RestResult<UserInfo> test(UserLoginParam param);

    @Post("/docs/upload")
    @Accept(MediaType.APPLICATION_JSON)
    AttachmentUploadResult uploadAttachment(MultiValueMap<String, Object> param);

}
