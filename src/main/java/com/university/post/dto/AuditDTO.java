package com.university.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonView;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditDTO<T> {
    protected T createBy;

    protected Date createAt;

    protected T updateBy;

    protected Date updateAt;
}
