package ru.taximaxim.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import ru.taximaxim.demo.context.SessionContextHolder;

@Service
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private SessionContextHolder sessionContextHolder;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        // При выходе удаляем контекст из хранилище контекстов
        if (authentication instanceof CustomAuthenticationToken) {
            String context = ((CustomAuthenticationToken) authentication).getContext();
            sessionContextHolder.removeContext(context);
        }
        
        super.handle(request, response, authentication);
    }

}
