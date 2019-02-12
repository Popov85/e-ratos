package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ratos.dao.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    //----------------------------------------------ONE for authentication--------------------------------------------

    @Query(value = "SELECT s FROM Student s join fetch s.user u left join fetch u.roles where u.email = ?1")
    Student findByIdForAuthentication(String email);

    //---------------------------------------------------ONE for edit-------------------------------------------------

    @Query(value="select s from Student s join fetch s.user u join fetch s.studentClass c join fetch c.faculty f join fetch f.organisation o where s.studId = ?1")
    Student findOneForEdit(Long studId);

    //---------------------------------------------INSTRUCTOR for group management-------------------------------------

    @Query(value="select s from Student s join fetch s.user u join fetch s.studentClass c join fetch c.faculty f join fetch f.organisation o where o.orgId = ?1 order by u.surname asc",
            countQuery = "select count(s) from Student s join s.studentClass c join c.faculty f join f.organisation o where o.orgId = ?1")
    Page<Student> findAllByOrgId(Long orgId, Pageable pageable);

    @Query(value="select s from Student s join fetch s.user u join fetch s.studentClass c join fetch c.faculty f join fetch f.organisation o where o.orgId = ?1 and (u.name like %?2% or u.surname like %?2% or u.email like %?2%) order by u.surname asc")
    Slice<Student> findAllByOrgIdAndNameLettersContains(Long orgId, String letters, Pageable pageable);

    //-------------------------------------------------------ADMIN-----------------------------------------------------

    @Query(value="select s from Student s join fetch s.user u join fetch s.studentClass c join fetch c.faculty f join fetch f.organisation o order by u.surname asc", countQuery = "select count(s) from Student s")
    Page<Student> findAllAdmin(Pageable pageable);
}
