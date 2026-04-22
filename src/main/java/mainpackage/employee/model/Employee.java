package mainpackage.employee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mainpackage.hibernateRelationships.Coupon;
import mainpackage.hibernateRelationships.Order;
import mainpackage.hibernateRelationships.Profile;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "bith_date")
    private LocalDate birthDate;
//    @Column(name = "age")
    @Transient
    private Integer age;
    @Column(name = "salary")
    private Integer salary;
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    private List<Order> orders;

    @ManyToMany
    @JoinTable(name = "employee_coupon",
            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id", referencedColumnName = "id"))
    private List<Coupon> coupons;

    public Employee(Long id, String name, String email, LocalDate birthDate, Integer salary, EmployeeStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.age = Period.between(birthDate, LocalDate.now()).getYears();
        this.salary = salary;
        this.status = status;
    }


    public Integer getAge() {
        if (age == null)
            age = Period.between(birthDate, LocalDate.now()).getYears();
        return age;
    }

//    public void setAge() {
////        Period period = Period.between(birthDate, LocalDate.now());
//        this.age = 20;
//    }
}
