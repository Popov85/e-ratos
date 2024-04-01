package ua.edu.ratos.web.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class SecurityController {

    @Value("${spring.web.resources.cache.cachecontrol.max-age}")
    private String maxAge;

    // Custom login form
    @GetMapping(value = "/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name= "logout", required = false) String logout,
                        final HttpServletResponse response) {
        response.addHeader("Cache-Control", "max-age="+maxAge+", must-revalidate, no-transform");
        return "login";
    }

   // Custom access denied form
    @GetMapping("/access-denied")
    public String denied(final HttpServletResponse response) {
        response.addHeader("Cache-Control", "max-age="+maxAge+", must-revalidate, no-transform");
        return "access-denied";
    }
}
