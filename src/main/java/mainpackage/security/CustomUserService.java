package mainpackage.security;

import lombok.RequiredArgsConstructor;
import mainpackage.employee.model.Employee;
import mainpackage.employee.repo.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final EmployeeRepo employeeRepo;
    private final ModelMapper modelMapper;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        CustomUserDetails userDetails = new CustomUserDetails(employee);// modelMapper.map(employee, CustomUserDetails.class);
        return userDetails;
    }

}
