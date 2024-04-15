package ua.edu.ratos.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ratos.dao.entity.Group;
import ua.edu.ratos.dao.entity.Student;
import ua.edu.ratos.dao.entity.StudentGroup;
import ua.edu.ratos.dao.entity.StudentGroupId;
import ua.edu.ratos.dao.repository.GroupRepository;
import ua.edu.ratos.dao.repository.StudentGroupRepository;
import ua.edu.ratos.security.SecurityUtils;
import ua.edu.ratos.service.dto.in.GroupInDto;
import ua.edu.ratos.service.dto.out.GroupExtendedOutDto;
import ua.edu.ratos.service.dto.out.GroupOutDto;
import ua.edu.ratos.service.transformer.mapper.GroupExtMapper;
import ua.edu.ratos.service.transformer.mapper.GroupMapper;
import ua.edu.ratos.service.transformer.GroupTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Service
@AllArgsConstructor
public class GroupService {

    private static final String GROUP_NOT_FOUND = "The requested Group not found, groupId = ";

    @PersistenceContext
    private final EntityManager em;

    private final GroupRepository groupRepository;

    private final GroupTransformer groupTransformer;

    private final GroupExtMapper groupExtMapper;

    private final GroupMapper groupMapper;

    private final StudentGroupRepository studentGroupRepository;

    private final SecurityUtils securityUtils;

    //-------------------------------------------------------CRUD-------------------------------------------------------
    @Transactional
    public Long save(@NonNull final GroupInDto dto) {
        return groupRepository.save(groupTransformer.toEntity(dto)).getGroupId();
    }

    @Transactional
    public void updateName(@NonNull final Long groupId, @NonNull final String name) {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException(GROUP_NOT_FOUND + groupId))
                .setName(name);
    }

    //---------------------------------------------------Students operations--------------------------------------------
    @Transactional
    public void addStudent(@NonNull final Long groupId, @NonNull final Long studentId) {
        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setStudent(em.getReference(Student.class, studentId));
        studentGroup.setGroup(em.getReference(Group.class, groupId));
        studentGroupRepository.save(studentGroup);
    }

    @Transactional
    public void removeStudent(@NonNull final Long groupId, @NonNull final Long studentId) {
        studentGroupRepository.deleteById(new StudentGroupId(studentId, groupId));
    }

    @Transactional
    public void deleteById(@NonNull final Long groupId) {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException(GROUP_NOT_FOUND + groupId))
                .setDeleted(true);
    }

    //-------------------------------------------------------One (for edit)---------------------------------------------
    @Transactional(readOnly = true)
    public GroupOutDto findOneForEdit(@NonNull final Long groupId) {
        return groupMapper.toDto(groupRepository.findOneForEdit(groupId)
                .orElseThrow(() -> new EntityNotFoundException(GROUP_NOT_FOUND + groupId)));
    }

    //--------------------------------------------------------Staff table-----------------------------------------------
    @Transactional(readOnly = true)
    public Page<GroupExtendedOutDto> findAllByStaffId(@NonNull final Pageable pageable) {
        return groupRepository.findAllByStaffId(securityUtils.getAuthStaffId(), pageable).map(groupExtMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<GroupExtendedOutDto> findAllByStaffIdAndNameLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return groupRepository.findAllByStaffIdAndNameLettersContains(securityUtils.getAuthStaffId(), letters, pageable).map(groupExtMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<GroupExtendedOutDto> findAllByDepartmentId(@NonNull final Pageable pageable) {
        return groupRepository.findAllByDepartmentId(securityUtils.getAuthDepId(), pageable).map(groupExtMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<GroupExtendedOutDto> findAllByDepartmentIdAndNameLettersContains(@NonNull final String letters, @NonNull final Pageable pageable) {
        return groupRepository.findAllByDepartmentIdAndNameLettersContains(securityUtils.getAuthStaffId(), letters, pageable).map(groupExtMapper::toDto);
    }

    //--------------------------------------------------------ADMIN table-----------------------------------------------
    @Transactional(readOnly = true)
    public Page<GroupExtendedOutDto> findAllAdmin(@NonNull final Pageable pageable) {
        return groupRepository.findAllAdmin(pageable).map(groupExtMapper::toDto);
    }

}
