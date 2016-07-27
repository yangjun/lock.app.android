package com.wm.lock.http;

import com.wm.lock.exception.RemoteException;

import org.androidannotations.annotations.EBean;
import org.androidannotations.api.rest.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.web.client.HttpClientErrorException;

@EBean
public class RestErrorInjector implements RestErrorHandler {

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        RemoteException re = new RemoteException(e);
        if (e instanceof HttpClientErrorException) {
            String body = ((HttpClientErrorException) e).getResponseBodyAsString();
//            try {
//                RestResult<RestErrorData> errorData = JacksonUtils.readValue(body, new TypeReference<RestResult<RestErrorData>>() { });
//                re.setResponse(errorData);
//            } catch (Exception ex) {
                re.setResponse(body);
//            }
        }
//        else if (e instanceof HttpServerErrorException) {
//            String body = ((HttpServerErrorException) e).getResponseBodyAsString();
//            re.setResponse(body);
//        }
        throw re;
    }

}
