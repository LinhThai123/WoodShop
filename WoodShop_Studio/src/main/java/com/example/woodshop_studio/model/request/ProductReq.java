package com.example.woodshop_studio.model.request;

import com.example.woodshop_studio.entity.Category;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReq {

    private String id ;

    @NotBlank(message = "Tên sản phẩm không được trống ")
    @Size(min = 1, max = 300, message = "Độ dài tên sản phẩm từ 1 - 300 ký tự")
    private String name;

    @NotBlank(message = "Mô tả trống")
    private String description;

    @Min(1)
    @Max(100)
    private Integer quantity;

    private Long price;

    private String image ;

    private int status ;

    @NotBlank(message = "Danh mục trống")
    private String category_id;

}
