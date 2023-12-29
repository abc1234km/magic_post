package com.university.post.dataset;

import com.university.post.utils.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Paging {
    protected Integer pageNumber = Constant.DEFAULT_PAGE_NUMBER;

    protected Integer pageSize = Constant.DEFAULT_PAGE_SIZE;
}
