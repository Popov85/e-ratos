package ua.edu.ratos.web.page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Also see for /access-denied mapping
 * @see SecurityController
 */
@Slf4j
@Controller
public class PagesController {

    @Value("${spring.web.resources.cache.cachecontrol.max-age}")
    private String maxAge;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @GetMapping({"/", "/index"})
    public String getIndexPage(final HttpServletResponse response) {
        if (activeProfile!="dev") {
            response.addHeader("Cache-Control", "max-age="+maxAge+", must-revalidate, no-transform");
        }
        return "index";
    }

    @GetMapping("/department/**")
    public String getStaffPage(final HttpServletResponse response) {
        if (activeProfile!="dev") {
            response.addHeader("Cache-Control", "max-age="+maxAge+", must-revalidate, no-transform");
        }
        return "staff";
    }

    @GetMapping("/student/**")
    public String getStudentPage(final HttpServletResponse response) {
        if (activeProfile!="dev") {
            response.addHeader("Cache-Control", "max-age="+maxAge+", must-revalidate, no-transform");
        }
        return "student";
    }

    @GetMapping("/session/start")
    public String getSessionPage(@RequestParam Long schemeId, final HttpServletResponse response) {
        if (activeProfile!="dev") {
            response.addHeader("Cache-Control", "max-age="+maxAge+", must-revalidate, no-transform");
        }
        return "session";
    }
}
