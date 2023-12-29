package com.university.post.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn(
        name="resource_type",
        discriminatorType= DiscriminatorType.STRING
)
@Data
public abstract class ResourcePoint extends Audit<String>{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(250)")
    protected String id;

    protected Long resourceId = 10000000L;

    protected String name;

    protected String phoneNo;

    protected String address;

    protected String typeResource;
}
