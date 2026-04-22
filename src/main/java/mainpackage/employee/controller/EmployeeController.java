package mainpackage.employee.controller;

import jakarta.validation.Valid;
import mainpackage.employee.dto.EmployeeDto;
import mainpackage.employee.dto.Filter;
import mainpackage.employee.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("dev")
    public String dev() {
        return "Hello World";
    }

    @GetMapping("")
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }

    @GetMapping("/byFilter")
    public ResponseEntity<List<EmployeeDto>> getEmployees(@RequestBody Filter filter) {
        log.info("get employees with filter {}", filter);
        List<EmployeeDto> employees = employeeService.getAllEmployeesByFilter(filter);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        log.info("get employee {}", id);
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping("")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        log.info("create employee {}", employeeDto);
        employeeDto = employeeService.addEmployee(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        log.info("update employee {}", employeeDto);
        employeeDto = employeeService.updateEmployee(id, employeeDto);
        return ResponseEntity.status(HttpStatus.OK).body(employeeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable Long id) {
        log.info("change employee's status {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().build();
    }

}
