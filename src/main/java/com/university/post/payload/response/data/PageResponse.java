package com.university.post.payload.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageResponse<T> {
    private List<T> content;

    private Paginate paginate;

    public PageResponse(Page page) {
        this.content = page.getContent();
        this.paginate = new Paginate(page.getTotalPages(),
                                        Math.toIntExact(page.getTotalElements()),
                                        page.getSize(),
                              page.getNumber() + 1);
    }
}
