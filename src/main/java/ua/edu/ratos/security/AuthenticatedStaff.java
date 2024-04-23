package ua.edu.ratos.security;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import ua.edu.ratos.dao.entity.Staff;
import java.util.Collection;

/**
 * Inject Principal to web method, cast it to this class and get all the perks such as staffId, depId, etc..
 */
@Getter
@ToString(callSuper = true)
public class AuthenticatedStaff extends AuthenticatedUser {

    private final Long posId;
    private final Long depId;
    private final Long facId;
    private final Long orgId;
    private final String pos;
    private final String dep;
    private final String fac;
    private final String org;

    private AuthenticatedStaff(Long staffId,
                               String name,
                               String surname,
                               Long posId,
                               Long depId,
                               Long facId,
                               Long orgId,
                               String pos,
                               String dep,
                               String fac,
                               String org,
                               String username,
                               String password,
                               Collection<? extends GrantedAuthority> authorities) {
        super(staffId, name, surname, username, password, authorities);
        this.posId = posId;
        this.depId = depId;
        this.facId = facId;
        this.orgId = orgId;
        this.pos = pos;
        this.dep = dep;
        this.fac = fac;
        this.org = org;
    }

    public static AuthenticatedStaff create(@NonNull final Staff staff) {
        return new AuthenticatedStaff(
                staff.getStaffId(),
                staff.getUser().getName(),
                staff.getUser().getSurname(),
                staff.getPosition().getPosId(),
                staff.getDepartment().getDepId(),
                staff.getDepartment().getFaculty().getFacId(),
                staff.getDepartment().getFaculty().getOrganisation().getOrgId(),
                staff.getPosition().getName(),
                staff.getDepartment().getName(),
                staff.getDepartment().getFaculty().getName(),
                staff.getDepartment().getFaculty().getOrganisation().getName(),
                staff.getUser().getEmail(),
                new String(staff.getUser().getPassword()),
                getAuthorities(staff.getUser()));
    }
}
