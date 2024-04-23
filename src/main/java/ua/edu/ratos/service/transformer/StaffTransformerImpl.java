package ua.edu.ratos.service.transformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.ratos.dao.entity.Department;
import ua.edu.ratos.dao.entity.Position;
import ua.edu.ratos.dao.entity.Staff;
import ua.edu.ratos.dao.entity.User;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.StaffInDto;
import ua.edu.ratos.service.dto.in.StaffUpdInDto;
import ua.edu.ratos.service.transformer.mapper.UserMapper;
import ua.edu.ratos.service.transformer.mapper.UserUpdMapper;

import java.util.Set;

@Component
@AllArgsConstructor
public class StaffTransformerImpl implements StaffTransformer {

    @PersistenceContext
    private final EntityManager em;

    private final UserMapper userMapper;

    private final UserUpdMapper userUpdMapper;

    private final SecurityUtils securityUtils;


    @Override
    public Staff toEntity(@NonNull final StaffInDto dto) {
        Staff staff = new Staff();
        staff.setStaffId(dto.getStaffId());
        User user = userMapper.toEntity(dto.getUser());
        user.setRoles(Set.of(dto.getRole()));
        user.setActive(dto.isActive());
        staff.setUser(user);
        staff.setPosition(em.getReference(Position.class, dto.getPositionId()));
        // If depId is included then transform, if not - current admin depId
        staff.setDepartment(em.getReference(Department.class, dto.getDepId()
                .orElse(securityUtils.getAuthDepId())));
        return staff;
    }

    @Override
    public Staff toEntity(@NonNull final Staff staff, @NonNull final StaffUpdInDto dto) {
        staff.setStaffId(dto.getStaffId());
        User user = userUpdMapper.toEntity(staff.getUser(), dto.getUser());
        user.setRoles(Set.of(dto.getRole()));
        user.setActive(dto.isActive());
        staff.setPosition(em.getReference(Position.class, dto.getPositionId()));
        // If depId is included then transform, if not - current admin depId
        staff.setDepartment(em.getReference(Department.class, dto.getDepId()
                .orElse(securityUtils.getAuthDepId())));
        return staff;
    }
}
