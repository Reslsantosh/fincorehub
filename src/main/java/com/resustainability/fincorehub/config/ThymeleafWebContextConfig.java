package com.resustainability.fincorehub.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ThymeleafWebContextConfig {

    @ModelAttribute
    public void addWebContextObjects(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session,
            Model model) {
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("session", session);
        model.addAttribute("servletContext", request.getServletContext());
        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("currentUri", request.getRequestURI());
    }
}