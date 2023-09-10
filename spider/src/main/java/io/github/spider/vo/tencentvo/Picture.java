package io.github.spider.vo.tencentvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  21:23
 * @TODO description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
    //图片id
    private Long pid;
    //图片宽度
    private Integer width;
    //图片高度
    private Integer height;
    //图片地址
    private String url;

    public void print(){
        System.err.println(".....漫画图片信息....");
        System.out.println("图片id: "+this.pid);
        System.out.println("图片宽度: "+this.width);
        System.out.println("图片高度: "+this.height);
        System.out.println("图片地址: "+this.url);
        System.err.println(".......................................");
    }
}
