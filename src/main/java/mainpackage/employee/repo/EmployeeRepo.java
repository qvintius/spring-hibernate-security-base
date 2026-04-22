package mainpackage.employee.repo;

import mainpackage.employee.model.Employee;
import mainpackage.employee.model.EmployeeStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

//    @Transactional
//    @Modifying
//    @Query("""
//            update Employee e
//            set e.name = :name, e.email = :email, e.birthDate = :birthDate, e.age = :age, e.salary = :salary
//            where e.id = :id
//            """)
//    void updateAllFields(@Param("id") Long id, @Param("name") String name, @Param("email") String email,
//                         @Param("birthDate") LocalDate birthDate, @Param("age") Integer age, @Param("salary") Integer salary);

    @EntityGraph(attributePaths = {"orders"})//fetch
    List<Employee> findAll();

    @Transactional
    @Modifying
    @Query("""
            update Employee e
            set e.status = :status
            where e.id = :id
            """)
    void updateStatus(@Param("id") Long id, @Param("status") EmployeeStatus status);

   @Query("select e from Employee e")
   List<Employee> findAllByFilter(Pageable pageable);

   Optional<Employee> findByEmail(String email);

//    Employee findEmployeeByEmail(String email);
}
