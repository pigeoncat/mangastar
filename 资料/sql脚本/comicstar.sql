/*
 Navicat Premium Data Transfer

 Source Server         : 华为云
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 
 Source Schema         : comicstar

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 10/09/2023 13:46:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for author_code
-- ----------------------------
DROP TABLE IF EXISTS `author_code`;
CREATE TABLE `author_code`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `invite_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请码',
  `validity_time` datetime NOT NULL COMMENT '有效时间',
  `is_used` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否使用过;0-未使用 1-使用过',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`invite_code` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作家邀请码' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for author_income
-- ----------------------------
DROP TABLE IF EXISTS `author_income`;
CREATE TABLE `author_income`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `author_id` bigint UNSIGNED NOT NULL COMMENT '作家ID',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '漫画ID',
  `income_month` date NOT NULL COMMENT '收入月份',
  `pre_tax_income` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '税前收入;单位：分',
  `after_tax_income` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '税后收入;单位：分',
  `pay_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '支付状态;0-待支付 1-已支付',
  `confirm_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '稿费确认状态;0-待确认 1-已确认',
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '稿费收入统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for author_income_detail
-- ----------------------------
DROP TABLE IF EXISTS `author_income_detail`;
CREATE TABLE `author_income_detail`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `author_id` bigint UNSIGNED NOT NULL COMMENT '作家ID',
  `comic_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '动漫ID;0表示全部作品',
  `income_date` date NOT NULL COMMENT '收入日期',
  `income_account` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '订阅总额',
  `income_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '订阅次数',
  `income_number` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '订阅人数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '稿费收入明细统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for author_info
-- ----------------------------
DROP TABLE IF EXISTS `author_info`;
CREATE TABLE `author_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `invite_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请码',
  `pen_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '笔名',
  `tel_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `wechat_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信账号',
  `qq_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'QQ账号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `work_direction` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '作品方向;0-男频 1-女频',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '0：正常;1-封禁',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '作者信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comic_category
-- ----------------------------
DROP TABLE IF EXISTS `comic_category`;
CREATE TABLE `comic_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `work_direction` tinyint UNSIGNED NOT NULL COMMENT '作品方向;0-男频 1-女频',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名',
  `sort` tinyint UNSIGNED NOT NULL DEFAULT 10 COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动漫类别' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comic_chapter
-- ----------------------------
DROP TABLE IF EXISTS `comic_chapter`;
CREATE TABLE `comic_chapter`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '动漫ID',
  `chapter_num` int UNSIGNED NOT NULL COMMENT '章节号',
  `chapter_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '章节名',
  `chapter_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '章节描述',
  `page_count` int UNSIGNED NOT NULL COMMENT '动漫页数',
  `is_vip` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否收费;1-收费 0-免费',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_comicId_chapterNum`(`comic_id` ASC, `chapter_num` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `idx_comicId`(`comic_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 267433224488226817 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动漫章节' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comic_chapter_picture
-- ----------------------------
DROP TABLE IF EXISTS `comic_chapter_picture`;
CREATE TABLE `comic_chapter_picture`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '漫画ID',
  `chapter_num` int UNSIGNED NOT NULL COMMENT '章节号',
  `sort` smallint UNSIGNED NOT NULL COMMENT '漫画章节图片序号',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '漫画图片地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `idx_comicId_chapterNum_pageSort`(`comic_id` ASC, `chapter_num` ASC, `sort` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 267433224488226973 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '漫画章节图片地址' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comic_comment
-- ----------------------------
DROP TABLE IF EXISTS `comic_comment`;
CREATE TABLE `comic_comment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '被评动漫ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '评论用户ID',
  `comment_content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评价内容',
  `reply_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复数量',
  `audit_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核状态;0-待审核 1-审核通过 2-审核不通过',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_comicId_userId`(`comic_id` ASC, `user_id` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动漫评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comic_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `comic_comment_reply`;
CREATE TABLE `comic_comment_reply`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键',
  `comment_id` bigint UNSIGNED NOT NULL COMMENT '动漫评论ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '回复用户ID',
  `reply_content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容',
  `audit_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核状态;0-待审核 1-审核通过 2-审核不通过',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动漫评论回复' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for comic_info
-- ----------------------------
DROP TABLE IF EXISTS `comic_info`;
CREATE TABLE `comic_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_direction` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '作品方向;0-男频 1-女频',
  `category_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '类别ID',
  `category_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类别名',
  `pic_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '动漫封面地址',
  `comic_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '动漫名',
  `author_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '作家id，0表示无效',
  `author_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作家名',
  `comic_desc` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '动漫描述',
  `tags` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '漫画标签',
  `score` tinyint UNSIGNED NOT NULL COMMENT '评分;总分:10 ，真实评分 = score/10',
  `comic_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '动漫状态;0-连载中 1-已完结',
  `visit_count` bigint UNSIGNED NOT NULL DEFAULT 103 COMMENT '点击量',
  `chapter_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '总章节数',
  `comment_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
  `last_chapter_num` int UNSIGNED NULL DEFAULT NULL COMMENT '最新章节号',
  `last_chapter_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最新章节名',
  `last_chapter_update_time` datetime NULL DEFAULT NULL COMMENT '最新章节更新时间',
  `is_vip` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否收费;1-收费 0-免费',
  `origin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '漫画来源，爬虫从哪个站点爬取的，没有来源表示本站所有',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_comicName_authorName`(`comic_name` ASC, `author_name` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `idx_createTime`(`create_time` ASC) USING BTREE,
  INDEX `idx_lastChapterUpdateTime`(`last_chapter_update_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 267433073417785345 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动漫信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for home_comic
-- ----------------------------
DROP TABLE IF EXISTS `home_comic`;
CREATE TABLE `home_comic`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `type` tinyint UNSIGNED NOT NULL COMMENT '推荐类型;0-轮播图 1-顶部栏 2-本周强推 3-热门推荐 4-精品推荐',
  `sort` tinyint UNSIGNED NOT NULL COMMENT '推荐排序',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '推荐动漫ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 414 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '动漫推荐' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for home_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `home_friend_link`;
CREATE TABLE `home_friend_link`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `link_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接名',
  `link_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '链接url',
  `sort` tinyint UNSIGNED NOT NULL DEFAULT 11 COMMENT '排序号',
  `is_open` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否开启;0-不开启 1-开启',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '友情链接' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pay_alipay
-- ----------------------------
DROP TABLE IF EXISTS `pay_alipay`;
CREATE TABLE `pay_alipay`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户订单号',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付宝交易号',
  `buyer_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '买家支付宝账号 ID',
  `trade_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易状态;TRADE_SUCCESS-交易成功',
  `total_amount` int UNSIGNED NOT NULL COMMENT '订单金额;单位：分',
  `receipt_amount` int UNSIGNED NULL DEFAULT NULL COMMENT '实收金额;单位：分',
  `invoice_amount` int UNSIGNED NULL DEFAULT NULL COMMENT '开票金额',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '交易创建时间',
  `gmt_payment` datetime NULL DEFAULT NULL COMMENT '交易付款时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `uk_outTradeNo`(`out_trade_no` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付宝支付' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for pay_wechat
-- ----------------------------
DROP TABLE IF EXISTS `pay_wechat`;
CREATE TABLE `pay_wechat`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户订单号',
  `transaction_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信支付订单号',
  `trade_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易类型;JSAPI-公众号支付 NATIVE-扫码支付 APP-APP支付 MICROPAY-付款码支付 MWEB-H5支付 FACEPAY-刷脸支付',
  `trade_state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易状态;SUCCESS-支付成功 REFUND-转入退款 NOTPAY-未支付 CLOSED-已关闭 REVOKED-已撤销（付款码支付） USERPAYING-用户支付中（付款码支付） PAYERROR-支付失败(其他原因，如银行返回失败)',
  `trade_state_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易状态描述',
  `amount` int UNSIGNED NOT NULL COMMENT '订单总金额;单位：分',
  `payer_total` int UNSIGNED NULL DEFAULT NULL COMMENT '用户支付金额;单位：分',
  `success_time` datetime NULL DEFAULT NULL COMMENT '支付完成时间',
  `payer_openid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付者用户标识;用户在直连商户appid下的唯一标识',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `uk_outTradeNo`(`out_trade_no` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '微信支付' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户操作',
  `time` int UNSIGNED NULL DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '父菜单ID;一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `type` tinyint UNSIGNED NOT NULL COMMENT '类型;0-目录   1-菜单',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_sign` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` bigint UNSIGNED NOT NULL COMMENT '角色ID',
  `menu_id` bigint UNSIGNED NOT NULL COMMENT '菜单ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `sex` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '性别;0-男 1-女',
  `birth` datetime NULL DEFAULT NULL COMMENT '出身日期',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态;0-禁用 1-正常',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` bigint UNSIGNED NOT NULL COMMENT '角色ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_bookshelf
-- ----------------------------
DROP TABLE IF EXISTS `user_bookshelf`;
CREATE TABLE `user_bookshelf`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '动漫ID',
  `pre_chapter_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '上一次阅读的章节ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间;',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间;',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_userId_comicId`(`user_id` ASC, `comic_id` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户书架' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_comment
-- ----------------------------
DROP TABLE IF EXISTS `user_comment`;
CREATE TABLE `user_comment`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '评论用户ID',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '被评动漫ID',
  `comment_content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评价内容',
  `reply_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复数量',
  `audit_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核状态;0-待审核 1-审核通过 2-审核不通过',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_comicId_userId`(`comic_id` ASC, `user_id` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户评论' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `user_comment_reply`;
CREATE TABLE `user_comment_reply`  (
  `id` bigint UNSIGNED NOT NULL COMMENT '主键',
  `comment_id` bigint UNSIGNED NOT NULL COMMENT '评论ID',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '回复用户ID',
  `reply_content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回复内容',
  `audit_status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '审核状态;0-待审核 1-审核通过 2-审核不通过',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户评论回复' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_consume_log
-- ----------------------------
DROP TABLE IF EXISTS `user_consume_log`;
CREATE TABLE `user_consume_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '消费用户ID',
  `amount` int UNSIGNED NOT NULL COMMENT '消费使用的金额;单位：漫币',
  `product_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '消费商品类型;0-动漫VIP章节',
  `product_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '消费的的商品ID;例如：章节ID',
  `produc_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消费的的商品名;例如：章节名',
  `produc_value` int UNSIGNED NULL DEFAULT NULL COMMENT '消费的的商品值;例如：1',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户消费记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_feedback
-- ----------------------------
DROP TABLE IF EXISTS `user_feedback`;
CREATE TABLE `user_feedback`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint UNSIGNED NOT NULL COMMENT '反馈用户id',
  `content` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户反馈' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录密码-加密',
  `salt` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密盐值',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `user_photo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_sex` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '用户性别;0-男 1-女',
  `account_balance` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '账户余额',
  `status` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户状态;0-正常',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_pay_log
-- ----------------------------
DROP TABLE IF EXISTS `user_pay_log`;
CREATE TABLE `user_pay_log`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint UNSIGNED NOT NULL COMMENT '充值用户ID',
  `pay_channel` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '充值方式;0-支付宝 1-微信',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户订单号',
  `amount` int UNSIGNED NOT NULL COMMENT '充值金额;单位：分',
  `product_type` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '充值商品类型;0-漫币 1-包年VIP',
  `product_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '充值商品ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '充值商品名;示例值：漫币',
  `product_value` int UNSIGNED NULL DEFAULT NULL COMMENT '充值商品值;示例值：255',
  `pay_time` datetime NOT NULL COMMENT '充值时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户充值记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_read_history
-- ----------------------------
DROP TABLE IF EXISTS `user_read_history`;
CREATE TABLE `user_read_history`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `comic_id` bigint UNSIGNED NOT NULL COMMENT '动漫ID',
  `pre_chapter_id` bigint UNSIGNED NOT NULL COMMENT '上一次阅读的章节ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间;',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间;',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_userId_comicId`(`user_id` ASC, `comic_id` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户阅读历史' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
