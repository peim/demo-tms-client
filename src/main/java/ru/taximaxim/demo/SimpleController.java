package ru.taximaxim.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.taximaxim.demo.security.CustomAuthenticationToken;

@Controller
public class SimpleController {

    @RequestMapping("/debug")
    public String debug(HttpServletRequest request, HttpServletResponse response) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        boolean bool = principal instanceof CustomAuthenticationToken;
        if (bool) {
            CustomAuthenticationToken token = (CustomAuthenticationToken) principal;
            String context = token.getContext();
            System.err.println(context);
        }
        System.err.println(principal);
        return "debug";
    }
}
