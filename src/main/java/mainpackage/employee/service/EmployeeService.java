package mainpackage.employee.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mainpackage.employee.repo.EmployeeRepo;
import mainpackage.employee.dto.EmployeeDto;
import mainpackage.employee.dto.Filter;
import mainpackage.employee.model.Employee;
import mainpackage.employee.model.EmployeeStatus;
import mainpackage.security.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;




    public List<EmployeeDto> getAllEmployees() {
         List<Employee> employees = employeeRepo.findAll();
         List<EmployeeDto> employeeDtos = employees.stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
         log.info("getAllEmployees: employees: {}", employeeDtos);
         return employeeDtos;
    }

    public List<EmployeeDto> getAllEmployeesByFilter(Filter filter) {
        Integer pageSize = filter.getPageSize() != null ? filter.getPageSize() : 10;
        Integer pageNumber = filter.getPageNumber() != null ? filter.getPageNumber() : 0;
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
        List<Employee> employees = employeeRepo.findAllByFilter(pageable);
        List<EmployeeDto> employeeDtos = employees.stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
        log.info("getAllEmployees: employees: {}", employeeDtos);
        return employeeDtos;
    }

    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found with id %s ".formatted(id)));
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        log.info("getEmployeeById: employee: {}", employeeDto);
        return employeeDto;
    }

    public EmployeeDto addEmployee(EmployeeDto employeeDtoToSave) {
//        if (employeeDtoToSave.getStatus() != null || employeeDtoToSave.getId() != null) throw new IllegalArgumentException("Employee status & id should be empty");
        if (employeeRepo.findByEmail(employeeDtoToSave.getEmail()).isPresent())
            throw new EntityExistsException("Employee with email %s already exists".formatted(employeeDtoToSave.getEmail()));

        Employee employee = modelMapper.map(employeeDtoToSave, Employee.class);
        employee.setStatus(EmployeeStatus.ACTIVE);
//        employee.setPassword(passwordEncoder.encode(employeeDtoToSave.getPassword()));
//        employee.setPassword(employeeDtoToSave.getpas);
        employee = employeeRepo.save(employee);
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        log.info("addEmployee: employee: {}", employeeDto);
        return employeeDto;
    }

    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDtoToUpdate) {
//        if (employeeDtoToUpdate.getStatus() != null || employeeDtoToUpdate.getId() != null) throw new IllegalArgumentException("Employee status & id should be empty");
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found with id %s ".formatted(id)));
        if (employee.getStatus().equals(EmployeeStatus.DELETED)) throw new IllegalStateException("Employee status is deleted");
        if (employeeRepo.findByEmail(employeeDtoToUpdate.getEmail()).isPresent())
            throw new EntityExistsException("Employee with email %s already exists".formatted(employeeDtoToUpdate.getEmail()));
        employeeDtoToUpdate.setStatus(employee.getStatus());
        employee = modelMapper.map(employeeDtoToUpdate, Employee.class);
        employee.setId(id);
//        employee = employeeRepo.save(employee);// отслеживается благодаря @Transactional, нет необходимости в save
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        log.info("updateEmployee: employee: {}", employeeDto);
        return employeeDto;
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found with id %s ".formatted(id)));
//        employeeRepo.deleteById(employee.getId());
        employeeRepo.updateStatus(id, EmployeeStatus.DELETED);
        log.info("Employee with id {} was chanched", id);
    }



    //security
    public JwtAuthenticationDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Employee employee = findByCredentials(userCredentialsDto);
//        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        return jwtService.generateAuthToken(employee.getEmail());
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
//        JwtAuthenticationDto jwtAuthenticationDto = new JwtAuthenticationDto();
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            Employee employee = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(employee.getEmail(), refreshToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    private Employee findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<Employee> optionalEmployee = employeeRepo.findByEmail(userCredentialsDto.getEmail());
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
//            if (passwordEncoder.matches(userCredentialsDto.getPassword(), employee.getPassword()))
                return employee;
        }
        throw new AuthenticationException("email or password is incorrect");
    }

    private Employee findByEmail(String email) throws Exception {
        return employeeRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Not found with email %s ".formatted(email)));
    }


    public EmployeeDto addEmployeeRegistration(RegistrationEmployeeDto registrationEmployeeDto) {
        if (employeeRepo.findByEmail(registrationEmployeeDto.getEmail()).isPresent())
            throw new EntityExistsException("Employee with email %s already exists".formatted(registrationEmployeeDto.getEmail()));

        Employee employee = modelMapper.map(registrationEmployeeDto, Employee.class);
        employee.setStatus(EmployeeStatus.ACTIVE);
//        employee.setPassword(passwordEncoder.encode(employeeDtoToSave.getPassword()));
        employee.setPassword(registrationEmployeeDto.getPassword());
        employee = employeeRepo.save(employee);
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        log.info("addEmployee: employee: {}", employeeDto);
        return employeeDto;
    }
}
