package mainpackage.employee.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private Integer pageSize;
    private Integer pageNumber;

}
