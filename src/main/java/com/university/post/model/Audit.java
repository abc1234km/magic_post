package com.university.post.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class Audit<T> {

    @CreatedBy
    @JsonIgnore
    protected T createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonIgnore
    protected Date createAt;

    @LastModifiedBy
    @JsonIgnore
    protected T updateBy;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonIgnore
    protected Date updateAt;
}
