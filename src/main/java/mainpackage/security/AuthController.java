package mainpackage.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mainpackage.employee.dto.EmployeeDto;
import mainpackage.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmployeeService employeeService;

    @PostMapping("/registration")
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody RegistrationEmployeeDto registrationEmployeeDto) {
        log.info("create employee {}", registrationEmployeeDto);
        EmployeeDto employeeDto = employeeService.addEmployeeRegistration(registrationEmployeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationDto> refistration(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = employeeService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed %s".formatted(e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return employeeService.refreshToken(refreshTokenDto);
    }


}
