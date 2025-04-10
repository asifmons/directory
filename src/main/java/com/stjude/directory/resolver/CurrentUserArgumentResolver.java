package com.stjude.directory.resolver;

import com.stjude.directory.annotation.CurrentUser;
import com.stjude.directory.model.UserContext;
import com.stjude.directory.model.UserContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import org.springframework.stereotype.Component;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // Get the UserContext from UserContextHolder
        UserContext userContext = UserContextHolder.getUserContext();
        if (userContext == null) {
            throw new IllegalStateException("User context is missing. User is not authenticated.");
        }

        return userContext.getUserId();
    }


}
