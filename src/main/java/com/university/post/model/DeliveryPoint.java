package com.university.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="delivery_points")
@Data
@DiscriminatorValue("delivery-point")
public class DeliveryPoint extends ResourcePoint{
//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", columnDefinition = "VARCHAR(250)")
//    private String id;
//
//    private Long pointId = 10000000L;
//
//    private String name;
//
//    private String phoneNo;
//
//    private String address;
//
//    @JsonIgnore
//    private String typePoint;

    @OneToMany(mappedBy = "workAt",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER)
    @JsonIgnore
    private List<User> staffs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private DeliveryPoint gatheringPoint;
}
