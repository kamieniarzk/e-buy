package com.example.ebuy.exception;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ExceptionController {
    private final Log logger = LogFactory.getLog(ExceptionHandler.class);

    @ExceptionHandler(value = AccessDeniedException.class)
    public String handleAccessException() {
        return "redirect:/users/login";
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException(RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest, Exception exception) {
        redirectAttributes.addFlashAttribute("message", "Sorry! Something went wrong.");
        logger.error("Request: "+ httpServletRequest.getRequestURL() + " threw an " + exception);
        return "redirect:/products/home";
    }
}
