package com.davidauz.bulk_mailing.config;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final Logger logger = (Logger) LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping("/error")
    public String handleError
    (   HttpServletRequest request
            , HttpServletResponse response
            , Model model
    ) {
        // get error details
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        // log error details
        logger.error("Error handling request: status={}, message={}, exception={}", status, message, exception);

        int statusCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            String url = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            model.addAttribute("url", url);
            return "404";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            String url = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            model.addAttribute("url", url);
            return "500";
        }

        // add error details to model
        model.addAttribute("status", status);
        model.addAttribute("message", message);
        model.addAttribute("exception", exception);

        // return custom error page
        return "error";
    }
}
