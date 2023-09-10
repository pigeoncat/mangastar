package io.github.spider.util.tencentutil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Map;

/**
 * @Author pigeoncat
 * @Date 2023/08/29  13:31
 * @TODO description
 */
public class PagePool {

    private Integer startPage;
    private Integer endPage;
    private ArrayDeque<Integer> queue=new ArrayDeque<>();

    private void initPool(){
        for (int i = startPage; i <= endPage ; i++) {
            queue.offer(i);
        }
    }

    //获取页面编号
    public int getPageNum(){
        return queue.poll();
    }

    //判断页面池是否空了
    public boolean isEmpty(){
        return queue.isEmpty();
    }
    //重新填满页面池,页面池非空不要调用此方法
    public void refillPool(){
        initPool();
    }
    //数据入池
    public void addToPool(int pageNum){
        this.queue.offer(pageNum);
    }

    public PagePool(Integer startPage, Integer endPage) {
        this.startPage = startPage;
        this.endPage = endPage;
        initPool();
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }
}
