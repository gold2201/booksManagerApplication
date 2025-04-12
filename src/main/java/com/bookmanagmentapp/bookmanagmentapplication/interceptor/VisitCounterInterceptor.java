package com.bookmanagmentapp.bookmanagmentapplication.interceptor;

import com.bookmanagmentapp.bookmanagmentapplication.service.VisitCounterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class VisitCounterInterceptor implements HandlerInterceptor {
    private final VisitCounterService visitCounterService;

    public VisitCounterInterceptor(@NonNull VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        String url = request.getRequestURI();
        if (url != null) {
            visitCounterService.increment(url);
        }
        return true;
    }
}


