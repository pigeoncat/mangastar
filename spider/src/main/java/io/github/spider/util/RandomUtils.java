package io.github.spider.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author pigeoncat
 * @Date 2023/08/29  14:30
 * @TODO description
 */
public class RandomUtils {

    public static int getRandomNumberIn(int left,int right){
        return ThreadLocalRandom.current().nextInt(left, right + 1);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(getRandomNumberIn(1,30));
        }
    }

}
