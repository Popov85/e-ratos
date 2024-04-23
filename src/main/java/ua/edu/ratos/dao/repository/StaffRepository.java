package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ratos.dao.entity.Staff;

import java.util.Optional;
import java.util.Set;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    //------------------------------------------------AUTHENTICATION (security)-----------------------------------------
    @Query(value = "SELECT s FROM Staff s join fetch s.user u join fetch s.department d join fetch d.faculty f join fetch f.organisation o where u.email = ?1")
    Optional<Staff> findByIdForAuthentication(String email);

    //-----------------------------------------------------ONE for edit-------------------------------------------------
    @Query(value="select s from Staff s join fetch s.user join fetch s.position where s.staffId = ?1")
    Optional<Staff> findOneForEdit(Long staffId);

    //------------------------------------------------------ADMIN table-------------------------------------------------
    @Query(value="select s from Staff s join fetch s.user u join fetch s.position join fetch s.department d join fetch d.faculty f join fetch f.organisation where d.depId = ?1")
    Set<Staff> findAllByDepartmentId(Long depId);

    @Query(value="select s from Staff s join fetch s.user u join fetch s.position join fetch s.department d join fetch d.faculty f join fetch f.organisation where f.facId = ?1")
    Set<Staff> findAllByFacultyId(Long facId);

    @Query(value="select s from Staff s join fetch s.user u join fetch s.position join fetch s.department d join fetch d.faculty f join fetch f.organisation o where o.orgId = ?1")
    Set<Staff> findAllByOrganisationId(Long orgId);

    @Query(value="select s from Staff s join fetch s.user u join fetch s.position join fetch s.department d join fetch d.faculty f join fetch f.organisation")
    Set<Staff> findAllByRatos();

    //-------------------------------------------------SOLELY for future refs.------------------------------------------

    @Query(value="select s from Staff s join fetch s.user u join s.department d where d.depId = ?1",
            countQuery = "select count(s) from Staff s join s.user u join s.department d where d.depId=?1")
    Page<Staff> findAllByDepartmentId(Long depId, Pageable pageable);

    @Query(value="select s from Staff s join fetch s.user u join s.department d where d.depId = ?1 and (u.name like %?2% or u.surname like %?2%)",
            countQuery = "select count(s) from Staff s join s.user u join s.department d where d.depId=?1 and (u.name like %?2% or u.surname like %?2%)")
    Page<Staff> findAllByDepartmentIdAndNameLettersContains(Long depId, String letters, Pageable pageable);

    //----------------------------------------------------------ADMIN---------------------------------------------------
    @Query(value="select s from Staff s join fetch s.user u join s.department", countQuery = "select count(s) from Staff s")
    Page<Staff> findAllAdmin(Pageable pageable);
}
