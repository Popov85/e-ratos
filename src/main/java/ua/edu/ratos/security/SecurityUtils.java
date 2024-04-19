package ua.edu.ratos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth.common.signature.SignatureSecret;
import org.springframework.stereotype.Component;
import ua.edu.ratos.security.lti.LTIToolConsumerCredentials;
import ua.edu.ratos.security.lti.LTIUserConsumerCredentials;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Convenience security methods for usage throughout the app
 *
 * @see "https://stackoverflow.com/questions/25713315/spring-security-get-login-user-within-controllers-good-manners"
 */
@Component
public class SecurityUtils {

    @Value("${spring.profiles.active}")
    private String profile;

    //----------------------------------------------------Staff---------------------------------------------------------

    public AuthenticatedStaff getAuthStaff() {
        Authentication auth = getStaffAuthentication();
        return (AuthenticatedStaff) auth.getPrincipal();
    }

    /**
     * Obtain ID of the current authenticated staff
     * For Unit-testing: fallback to default values
     *
     * @return staffId
     */
    public Long getAuthStaffId() {
        if ("test".equals(profile)) return 1L;
        Authentication auth = getStaffAuthentication();
        return ((AuthenticatedStaff) auth.getPrincipal()).getUserId();
    }

    /**
     * Obtain ID of the department to which the current authenticated staff belongs
     * For Unit-testing: fallback to default values
     *
     * @return depId
     */
    public Long getAuthDepId() {
        if ("test".equals(profile)) return 1L;
        Authentication auth = getStaffAuthentication();
        return ((AuthenticatedStaff) auth.getPrincipal()).getDepId();
    }

    /**
     * Obtain ID of the faculty to which the current authenticated staff belongs
     * For Unit-testing: fallback to default values
     *
     * @return facId
     */
    public Long getAuthFacId() {
        if ("test".equals(profile)) return 1L;
        Authentication auth = getStaffAuthentication();
        return ((AuthenticatedStaff) auth.getPrincipal()).getFacId();
    }

    /**
     * Obtain ID of the organization to which the current authenticated staff belongs
     * For Unit-testing: fallback to default values
     *
     * @return orgId
     */
    public Long getAuthOrgId() {
        if ("test".equals(profile)) return 1L;
        Authentication auth = getStaffAuthentication();
        return ((AuthenticatedStaff) auth.getPrincipal()).getOrgId();
    }

    private Authentication getStaffAuthentication() {
        Authentication auth = getAuthentication();
        if (auth.getPrincipal().getClass() != AuthenticatedStaff.class)
            throw new SecurityException("Lack of authority");
        return auth;
    }


    //----------------------------------------------------User----------------------------------------------------------

    public AuthenticatedUser getAuthUser() {
        Authentication auth = getAuthentication();
        if (!(auth.getPrincipal() instanceof AuthenticatedUser))
            throw new SecurityException("Lack of authority");
        return (AuthenticatedUser) auth.getPrincipal();
    }

    /**
     * Obtain ID of any currently authenticated  user (whether staff or student)
     * For Unit-testing: fallback to default values, 2L.
     * Do not use this method for job withing LMS!
     * Only for usage outside LMS authentication: cabinets, statistics
     *
     * @return userId
     */
    public Long getAuthUserId() {
        if ("test".equals(profile)) return 2L;
        Authentication auth = getAuthentication();
        if (!(auth.getPrincipal() instanceof AuthenticatedUser))
            throw new SecurityException("Lack of authority");
        return ((AuthenticatedUser) auth.getPrincipal()).getUserId();
    }

    public String getAuthUsername() {
        if ("test".equals(profile)) return "Test";
        Authentication auth = getAuthentication();
        if (!(auth.getPrincipal() instanceof AuthenticatedUser))
            throw new SecurityException("Lack of authority");
        return ((AuthenticatedUser) auth.getPrincipal()).getUsername();
    }

    /**
     * In e-RATOS a user can have ONLY a single role!
     * (Technically DB allows a set or roles for future improvements!)
     * Each higher role has all the advantages as all lower roles.
     *
     * @return role name
     */
    public String getAuthRole() {
        Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toList()).get(0);
    }

    public boolean isCurrentUserStudent() {
        String role = "ROLE_STUDENT";
        return getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(role));
    }

    //------------------------------------------------------LMS---------------------------------------------------------

    /**
     * Checks if the current user comes from within some known LMS and fully authenticated.
     * For Unit-testing: fallback to false (non-LMS user)
     * This method throws RuntimeException if user is not authenticated
     *
     * @return verdict
     */
    public boolean isLmsUser() {
        if ("test".equals(profile)) return false;
        Authentication auth = getAuthentication();
        if (auth.getPrincipal().getClass() != LTIUserConsumerCredentials.class) return false;
        LTIUserConsumerCredentials credentials = (LTIUserConsumerCredentials) auth.getPrincipal();
        if (credentials.getLmsId() == null || credentials.getUserId() == null || credentials.getLmsId() <= 0 || credentials.getUserId() <= 0)
            return false;
        return true;
    }

    /**
     * Use this check if an exception thrown in case of unauthenticated user is not desirable
     *
     * @return verdict
     */
    public boolean isLtiUser() {
        if ("test".equals(profile)) return false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        if (!(auth.getPrincipal() instanceof LTIToolConsumerCredentials)) return false;
        LTIToolConsumerCredentials credentials = (LTIToolConsumerCredentials) auth.getPrincipal();
        if (credentials.getLmsId() == null || credentials.getLmsId() <= 0) return false;
        return true;
    }

    /**
     * Obtain ID of LMS, with which a user is currently authenticated
     *
     * @return lmsId
     */
    public Long getLmsId() {
        if ("test".equals(profile)) return 1L;
        LTIToolConsumerCredentials auth = getLmsToolAuthentication();
        Long lmsId = auth.getLmsId();
        if (lmsId == null || lmsId <= 0)
            throw new SecurityException("Nullable lmsId");
        return lmsId;
    }

    /**
     * Obtain ID of User, authenticated within LMS
     *
     * @return lmsId
     */
    public Long getLmsUserId() {
        if ("test".equals(profile)) return 2L;
        LTIUserConsumerCredentials auth = getLmsUserAuthentication();
        Long userId = auth.getUserId();
        if (userId == null || userId <= 0)
            throw new SecurityException("Nullable userId");
        return userId;
    }

    /**
     * Obtain LTI tool consumer credentials
     *
     * @return LTIToolConsumerCredentials
     */
    public LTIToolConsumerCredentials getLmsToolAuthentication() {
        Authentication auth = getAuthentication();
        if (!(auth.getPrincipal() instanceof LTIToolConsumerCredentials))
            throw new SecurityException("Lack of authority: non-lms user");
        return (LTIToolConsumerCredentials) auth.getPrincipal();
    }

    /**
     * Obtain LTI tool + userId credentials
     *
     * @return LTIUserConsumerCredentials
     */
    public LTIUserConsumerCredentials getLmsUserAuthentication() {
        Authentication auth = getAuthentication();
        if (auth.getPrincipal().getClass() != LTIUserConsumerCredentials.class)
            throw new SecurityException("Lack of authority: non-authenticated lms user");
        return (LTIUserConsumerCredentials) auth.getPrincipal();
    }

    /**
     * Obtain SignatureSecret for posting score to LMS
     *
     * @return OAuth 1.0 SignatureSecret
     */
    public SignatureSecret getOauthSignatureSecret() {
        Object credentials = getAuthentication().getCredentials();
        if (!(credentials instanceof SignatureSecret))
            throw new SecurityException("No SignatureSecret was found!");
        return (SignatureSecret) credentials;
    }

    /**
     * Check if first role is "greater" than the second, that is has more privileges than another;
     * Only to compare admin roles!
     *
     * @param role1 first role
     * @param role2 second role
     * @return true if so, false - otherwise
     */
    public boolean isRoleGreater(String role1, String role2) {
        List<String> allowedRoles;
        if ("ROLE_DEP-ADMIN".equals(role1)) {
            allowedRoles = Arrays.asList("ROLE_INSTRUCTOR", "ROLE_LAB-ASSISTANT");
        } else if ("ROLE_FAC-ADMIN".equals(role1)) {
            allowedRoles = Arrays.asList("ROLE_DEP-ADMIN", "ROLE_INSTRUCTOR", "ROLE_LAB-ASSISTANT");
        } else if ("ROLE_ORG-ADMIN".equals(role1)) {
            allowedRoles = Arrays.asList("ROLE_FAC-ADMIN", "ROLE_DEP-ADMIN", "ROLE_INSTRUCTOR", "ROLE_LAB-ASSISTANT");
        } else if ("ROLE_GLOBAL-ADMIN".equals(role1)) {
            allowedRoles = Arrays.asList("ROLE_ORG-ADMIN", "ROLE_FAC-ADMIN", "ROLE_DEP-ADMIN", "ROLE_INSTRUCTOR", "ROLE_LAB-ASSISTANT");
        } else {
            throw new SecurityException("Unknown role");
        }
        return allowedRoles.contains(role2);
    }

    //--------------------------------------------------------Private---------------------------------------------------

    private Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new SecurityException("Unauthorized");
        return auth;
    }

}
