package mainpackage.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mainpackage.employee.model.EmployeeStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @Null
    private Long id;

    @NotNull
    private String name;

    @NotNull
//    @UniqueElements
    private String email;


    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;


    @Null
    private Integer age;

    @Min(value = 5001, message = "should be greather than 5000")
    private Integer salary;

    @Null
    private EmployeeStatus status;

}
