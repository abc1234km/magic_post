package com.university.post.payload.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paginate {
    private Integer totalPages;

    private Integer totalItems;

    private Integer perPage;

    private Integer currentPage;
}
