package io.github.spider.vo.tencentvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author pigeoncat
 * @Date 2023/08/29  09:47
 * @TODO description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    //章节地址
    private String url;
    //章节id
    private String cid;
    //章节序号，第几章节
    private String cSeq;
    //章节名称
    private String cTitle;

    public int getCSeqAsInt(){
        return Integer.parseInt(cSeq);
    }

    public void print(){
        System.err.println(".....当前章节信息....");
        System.out.println("章节地址: "+this.url);
        System.out.println("章节id: "+this.cid);
        System.out.println("第几章节: "+this.cSeq);
        System.out.println("章节名称: "+this.cTitle);
        System.err.println(".......................................");
    }

}
