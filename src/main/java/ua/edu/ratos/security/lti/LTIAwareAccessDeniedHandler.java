package ua.edu.ratos.security.lti;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class LTIAwareAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.debug("Detected a non-LTI user lacking authority trying to access protected resource, redirection to /access-denied endpoint");
		response.sendRedirect(request.getContextPath() + "/access-denied");
	}
}
