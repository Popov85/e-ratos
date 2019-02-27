package ua.edu.ratos.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.edu.ratos.dao.entity.Help;

/**
 * @link https://stackoverflow.com/totalByType/21549480/spring-data-fetch-join-with-paging-is-not-working
 * @link https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/Query.html
 */
public interface HelpRepository extends JpaRepository<Help, Long> {

    //-------------------------------------------------------ONE for update---------------------------------------------

    @Query(value = "SELECT h FROM Help h join fetch h.staff s left join fetch h.resources where h.helpId=?1")
    Help findOneForUpdate(Long helpId);

    //------------------------------------------------INSTRUCTOR table & dropdown---------------------------------------

    @Query(value = "SELECT h FROM Help h join fetch h.staff s left join fetch h.resources where s.staffId=?1",
            countQuery = "SELECT count(h) FROM Help h join h.staff s where s.staffId=?1")
    Page<Help> findAllByStaffId(Long staffId, Pageable pageable);

    @Query(value = "SELECT h FROM Help h join fetch h.staff s join s.department d left join fetch h.resources where d.depId=?1",
            countQuery = "SELECT count(h) FROM Help h join h.staff s join s.department d where d.depId=?1")
    Page<Help> findAllByDepartmentId(Long depId, Pageable pageable);

    //------------------------------------------------------Search in table---------------------------------------------

    @Query(value = "SELECT h FROM Help h join fetch h.staff s left join fetch h.resources where s.staffId=?1 and h.name like ?2%",
            countQuery = "SELECT count(h) FROM Help h join h.staff s where s.staffId=?1 and h.name like ?2%")
    Page<Help> findAllByStaffIdAndNameStarts(Long staffId, String starts, Pageable pageable);

    @Query(value = "SELECT h FROM Help h join fetch h.staff s left join fetch h.resources where s.staffId=?1 and h.name like %?2%",
            countQuery = "SELECT count(h) FROM Help h join h.staff s where s.staffId=?1 and h.name like %?2%")
    Page<Help> findAllByStaffIdAndNameLettersContains(Long staffId, String contains, Pageable pageable);

    @Query(value = "SELECT h FROM Help h join fetch h.staff s join s.department d left join fetch h.resources where d.depId=?1 and h.name like ?2%",
            countQuery = "SELECT count(h) FROM Help h join h.staff s join s.department d where d.depId=?1 and h.name like ?2%")
    Page<Help> findAllByDepartmentIdAndNameStarts(Long depId, String starts, Pageable pageable);

    @Query(value = "SELECT h FROM Help h join fetch h.staff s join s.department d left join fetch h.resources where d.depId=?1 and h.name like %?2%",
            countQuery = "SELECT count(h) FROM Help h join h.staff s join s.department d where d.depId=?1 and h.name like %?2%")
    Page<Help> findAllByDepartmentIdAndNameLettersContains(Long depId, String contains, Pageable pageable);

    //----------------------------------------------------------ADMIN---------------------------------------------------

    @Query(value="SELECT h FROM Help h join fetch h.staff s left join fetch h.resources", countQuery = "SELECT h FROM Help h")
    Page<Help> findAll(Pageable pageable);
}
