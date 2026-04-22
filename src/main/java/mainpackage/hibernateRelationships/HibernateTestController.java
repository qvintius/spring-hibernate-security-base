package mainpackage.hibernateRelationships;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainpackage.employee.model.Employee;
import mainpackage.employee.repo.EmployeeRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class HibernateTestController {
    private final EmployeeRepo employeeRepo;

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepo.findById(id).orElseThrow();
    }

    @GetMapping("")
    public List<Employee> getAllEmployees() {
        return employeeRepo.findAll();
    }

}
