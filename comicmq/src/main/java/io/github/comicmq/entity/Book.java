package io.github.comicmq.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author pigeoncat
 * @Date 2023/09/05  12:52
 * @TODO description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer age;

}
