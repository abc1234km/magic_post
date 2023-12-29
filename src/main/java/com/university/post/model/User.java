package com.university.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.university.post.enums.Type;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data // lombok
public class User extends Audit<String> {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(250)")
    private String id;

    private Integer userId = Math.multiplyExact(LocalDate.now().getYear(), Type.FORMAT_ID);

    @JsonIgnore
    private String password;

    private String fullName;

    private LocalDate dateOfBirth;

    private String phoneNo;

    private String address;

    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;

    @ManyToOne
//    @JoinColumn(name="delivery_point_id")
    @JsonIgnore
    private DeliveryPoint workAt;
}
