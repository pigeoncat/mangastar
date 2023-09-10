package io.github.pigeoncat.comicstar.core.common.constant;


/**
 * 通用常量
 */
public class CommonConsts {

    /**
     * 是
     */
    public static final Integer YES = 1;
    public static final String TRUE = "true";


    /**
     * 否
     */
    public static final Integer NO = 0;
    public static final String FALSE = "false";

    /**
     * 性别常量
     */
    public enum SexEnum {

        /**
         * 男
         */
        MALE(0, "男"),

        /**
         * 女
         */
        FEMALE(1, "女");

        SexEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }
}
