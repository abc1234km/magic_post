package com.university.post.model;

import javax.persistence.Embeddable;

@Embeddable
public class ContextOrder {
    public String context;
    public Integer quantity;
    public Integer value;
    public Integer documentNo;
}
