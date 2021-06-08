/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.2-MyAQL
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : goodgoods02

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 08/06/2021 20:48:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '433', '管理部门');
INSERT INTO `department` VALUES (2, '433', '生产部门');

-- ----------------------------
-- Table structure for distribution
-- ----------------------------
DROP TABLE IF EXISTS `distribution`;
CREATE TABLE `distribution`  (
                                 `id` int NOT NULL AUTO_INCREMENT,
                                 `employee_id` bigint NOT NULL,
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `FK4mlpwb7djh8bm1dp33pbpoxpf`(`employee_id`) USING BTREE,
                                 CONSTRAINT `FK4mlpwb7djh8bm1dp33pbpoxpf` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of distribution
-- ----------------------------
INSERT INTO `distribution` VALUES (10, 2);
INSERT INTO `distribution` VALUES (12, 2);
INSERT INTO `distribution` VALUES (16, 2);
INSERT INTO `distribution` VALUES (17, 2);
INSERT INTO `distribution` VALUES (18, 2);
INSERT INTO `distribution` VALUES (19, 2);
INSERT INTO `distribution` VALUES (20, 2);
INSERT INTO `distribution` VALUES (24, 17);
INSERT INTO `distribution` VALUES (25, 21);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `createtime` datetime(6) NOT NULL,
                             `iscredentials` bit(1) NOT NULL,
                             `isenable` bit(1) NOT NULL,
                             `isexpired` bit(1) NOT NULL,
                             `islock` bit(1) NOT NULL,
                             `logintime` datetime(6) NOT NULL,
                             `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `UK_im8flsuftl52etbhgnr62d6wh`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '2021-05-27 15:17:57.420000', b'1', b'1', b'1', b'1', '2021-06-08 12:38:54.010000', '$2a$10$M./ZaYXMkj4Nv59o6ppK8uNmUibcBFAqWFo521CF3QPeH5FkIuuKO', 'admin');
INSERT INTO `employee` VALUES (2, '2021-05-27 15:17:57.547000', b'1', b'1', b'1', b'1', '2021-06-08 12:36:29.599000', '$2a$10$Z.zZqIC.D6HFultwq7.WxOjQZ8Ifs0OTb99q18x39qSRB08NOhzNO', 'zhangsan');
INSERT INTO `employee` VALUES (16, '2021-06-06 12:27:45.916000', b'1', b'1', b'1', b'1', '2021-06-06 12:27:45.916000', '$2a$10$gZbX27wvnOR0WcjnCHGTVuAaeT26dthhLV9FAmqx8yTt.nwDlIXeq', 'dicong');
INSERT INTO `employee` VALUES (17, '2021-06-06 12:41:06.991000', b'1', b'1', b'1', b'1', '2021-06-06 12:41:06.991000', '$2a$10$UWjr4S9TDfrKfill/5QDDeappAPW3IoUQp0KVWjtp9kCIRwFmLmSa', '李晋');
INSERT INTO `employee` VALUES (18, '2021-06-06 12:41:47.716000', b'1', b'1', b'1', b'1', '2021-06-06 12:41:47.716000', '$2a$10$2Y37DMGVALTtlg41G6HzneGxDEM1DpjVScpTccHFjhu5nU0cys/Iq', '张凯旋');
INSERT INTO `employee` VALUES (19, '2021-06-06 13:00:47.110000', b'1', b'1', b'1', b'1', '2021-06-06 13:00:47.110000', '$2a$10$5GEe3O6HTINuJrmCOtkxjOtzUdM/RToZynd.sIh9hrEYGQ0EIxmMu', 'lisa');
INSERT INTO `employee` VALUES (20, '2021-06-06 13:46:56.397000', b'1', b'1', b'1', b'1', '2021-06-06 13:46:56.397000', '$2a$10$3Kbir3eKurGwzxi62eZ8DO6N8iLqvJ2SdJstSie2u5bvdGRA.to0S', 'zks');
INSERT INTO `employee` VALUES (21, '2021-06-07 02:02:02.207000', b'1', b'1', b'1', b'1', '2021-06-07 02:02:02.207000', '$2a$10$nKqdqM3gLScjDhFDh2pk5erzxdHS7Es4.ivaeIQmdz2WETXOUzOl2', 'big');

-- ----------------------------
-- Table structure for employee_information
-- ----------------------------
DROP TABLE IF EXISTS `employee_information`;
CREATE TABLE `employee_information`  (
                                         `id` int NOT NULL AUTO_INCREMENT,
                                         `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                         `sex` int NULL DEFAULT NULL,
                                         `employee_id` bigint NOT NULL,
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `UK_g06gmlo4ahnhqm1ng4cp3lxps`(`employee_id`) USING BTREE,
                                         CONSTRAINT `FK4p2jby4hdpnls7gv872gdlge4` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee_information
-- ----------------------------
INSERT INTO `employee_information` VALUES (1, '江南大学433', 'memories@163.com', '管理员', '18206182935', 0, 1);
INSERT INTO `employee_information` VALUES (2, '江南大学433', '2159995470@qq.com', '张三', '15206182935', 0, 2);
INSERT INTO `employee_information` VALUES (13, '中国', NULL, '丁聪聪', '1231231234', 1, 16);
INSERT INTO `employee_information` VALUES (14, '中国', NULL, '李晋', '1231231234', 0, 17);
INSERT INTO `employee_information` VALUES (15, '中国', NULL, '张凯旋', '1231231234', 0, 18);
INSERT INTO `employee_information` VALUES (16, '中国', NULL, '李洒', '1231231234', 1, 19);
INSERT INTO `employee_information` VALUES (17, '中国', NULL, '张文文', '1231231234', 0, 20);
INSERT INTO `employee_information` VALUES (18, '江南大学', NULL, '大聪明', '1231231234', 0, 21);

-- ----------------------------
-- Table structure for employee_position
-- ----------------------------
DROP TABLE IF EXISTS `employee_position`;
CREATE TABLE `employee_position`  (
                                      `employee_id` bigint NOT NULL,
                                      `position_id` bigint NOT NULL,
                                      PRIMARY KEY (`employee_id`, `position_id`) USING BTREE,
                                      INDEX `FKl7ys86ssetdr0mkqikbyoram6`(`position_id`) USING BTREE,
                                      CONSTRAINT `FKccp61k7h2okqdhwerjfqa7u47` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                      CONSTRAINT `FKl7ys86ssetdr0mkqikbyoram6` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee_position
-- ----------------------------
INSERT INTO `employee_position` VALUES (1, 1);
INSERT INTO `employee_position` VALUES (2, 2);
INSERT INTO `employee_position` VALUES (19, 2);
INSERT INTO `employee_position` VALUES (16, 3);
INSERT INTO `employee_position` VALUES (17, 4);
INSERT INTO `employee_position` VALUES (18, 4);
INSERT INTO `employee_position` VALUES (20, 4);
INSERT INTO `employee_position` VALUES (21, 4);

-- ----------------------------
-- Table structure for ingredient
-- ----------------------------
DROP TABLE IF EXISTS `ingredient`;
CREATE TABLE `ingredient`  (
                               `id` int NOT NULL AUTO_INCREMENT,
                               `dosage` double NOT NULL,
                               `distribution_id` int NOT NULL,
                               `material_id` int NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `FKc8qjf3nkistuw0jlliam1gtp`(`distribution_id`) USING BTREE,
                               INDEX `FK7aigghe8uw5po7eg55naxmf2r`(`material_id`) USING BTREE,
                               CONSTRAINT `FK7aigghe8uw5po7eg55naxmf2r` FOREIGN KEY (`material_id`) REFERENCES `material` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                               CONSTRAINT `FKc8qjf3nkistuw0jlliam1gtp` FOREIGN KEY (`distribution_id`) REFERENCES `distribution` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ingredient
-- ----------------------------
INSERT INTO `ingredient` VALUES (28, 3, 10, 4);
INSERT INTO `ingredient` VALUES (29, 3, 10, 5);
INSERT INTO `ingredient` VALUES (30, 4, 10, 6);
INSERT INTO `ingredient` VALUES (34, 2, 12, 4);
INSERT INTO `ingredient` VALUES (35, 2, 12, 5);
INSERT INTO `ingredient` VALUES (36, 1, 12, 6);
INSERT INTO `ingredient` VALUES (46, 1, 16, 4);
INSERT INTO `ingredient` VALUES (47, 1, 16, 5);
INSERT INTO `ingredient` VALUES (48, 16, 16, 6);
INSERT INTO `ingredient` VALUES (49, 3, 17, 6);
INSERT INTO `ingredient` VALUES (50, 3, 17, 5);
INSERT INTO `ingredient` VALUES (51, 17, 17, 4);
INSERT INTO `ingredient` VALUES (52, 3, 18, 4);
INSERT INTO `ingredient` VALUES (53, 3, 18, 6);
INSERT INTO `ingredient` VALUES (54, 3, 18, 5);
INSERT INTO `ingredient` VALUES (55, 2, 19, 6);
INSERT INTO `ingredient` VALUES (56, 2, 19, 5);
INSERT INTO `ingredient` VALUES (57, 2, 19, 4);
INSERT INTO `ingredient` VALUES (58, 2, 20, 6);
INSERT INTO `ingredient` VALUES (59, 2, 20, 4);
INSERT INTO `ingredient` VALUES (60, 2, 20, 5);
INSERT INTO `ingredient` VALUES (70, 2, 24, 4);
INSERT INTO `ingredient` VALUES (71, 3, 24, 5);
INSERT INTO `ingredient` VALUES (72, 3, 24, 6);
INSERT INTO `ingredient` VALUES (73, 21, 25, 5);
INSERT INTO `ingredient` VALUES (74, 21, 25, 5);
INSERT INTO `ingredient` VALUES (75, 21, 25, 6);

-- ----------------------------
-- Table structure for mainingredient
-- ----------------------------
DROP TABLE IF EXISTS `mainingredient`;
CREATE TABLE `mainingredient`  (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `dosage` double NOT NULL,
                                   `distribution_id` int NOT NULL,
                                   `material_id` int NOT NULL,
                                   PRIMARY KEY (`id`) USING BTREE,
                                   INDEX `FK9fm9j70reo00muexd69a6dorc`(`distribution_id`) USING BTREE,
                                   INDEX `FKrnsixbdgq1jgbwsdtqn8shk4p`(`material_id`) USING BTREE,
                                   CONSTRAINT `FK9fm9j70reo00muexd69a6dorc` FOREIGN KEY (`distribution_id`) REFERENCES `distribution` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                   CONSTRAINT `FKrnsixbdgq1jgbwsdtqn8shk4p` FOREIGN KEY (`material_id`) REFERENCES `material` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mainingredient
-- ----------------------------
INSERT INTO `mainingredient` VALUES (28, 2, 10, 7);
INSERT INTO `mainingredient` VALUES (29, 3, 10, 2);
INSERT INTO `mainingredient` VALUES (30, 3, 10, 3);
INSERT INTO `mainingredient` VALUES (34, 1, 12, 2);
INSERT INTO `mainingredient` VALUES (35, 2, 12, 3);
INSERT INTO `mainingredient` VALUES (36, 1, 12, 7);
INSERT INTO `mainingredient` VALUES (46, 1, 16, 2);
INSERT INTO `mainingredient` VALUES (47, 1, 16, 3);
INSERT INTO `mainingredient` VALUES (48, 1, 16, 7);
INSERT INTO `mainingredient` VALUES (49, 3, 17, 2);
INSERT INTO `mainingredient` VALUES (50, 3, 17, 7);
INSERT INTO `mainingredient` VALUES (51, 3, 17, 3);
INSERT INTO `mainingredient` VALUES (52, 3, 18, 2);
INSERT INTO `mainingredient` VALUES (53, 3, 18, 3);
INSERT INTO `mainingredient` VALUES (54, 3, 18, 7);
INSERT INTO `mainingredient` VALUES (55, 1, 19, 2);
INSERT INTO `mainingredient` VALUES (56, 2, 19, 7);
INSERT INTO `mainingredient` VALUES (57, 2, 19, 3);
INSERT INTO `mainingredient` VALUES (58, 1, 20, 2);
INSERT INTO `mainingredient` VALUES (59, 2, 20, 3);
INSERT INTO `mainingredient` VALUES (60, 2, 20, 7);
INSERT INTO `mainingredient` VALUES (70, 1, 24, 2);
INSERT INTO `mainingredient` VALUES (71, 21, 24, 3);
INSERT INTO `mainingredient` VALUES (72, 21, 24, 7);
INSERT INTO `mainingredient` VALUES (73, 13, 25, 2);
INSERT INTO `mainingredient` VALUES (74, 31, 25, 3);
INSERT INTO `mainingredient` VALUES (75, 1, 25, 2);

-- ----------------------------
-- Table structure for material
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material`  (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `allowance` double NULL DEFAULT NULL,
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `state` int NULL DEFAULT NULL,
                             `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of material
-- ----------------------------
INSERT INTO `material` VALUES (2, 100000, '主料2', 1, 'Kg');
INSERT INTO `material` VALUES (3, 100000, '主料3', 1, 'Kg');
INSERT INTO `material` VALUES (4, 100000, '辅料1', 0, 'Kg');
INSERT INTO `material` VALUES (5, 100000, '辅料2', 0, 'Kg');
INSERT INTO `material` VALUES (6, 100000, '辅料3', 0, 'Kg');
INSERT INTO `material` VALUES (7, 10000, '主料1', 1, 'Kg');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 'ROLE_ADMIN');
INSERT INTO `permission` VALUES (2, 'ROLE_APPLY');

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                             `suggest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `position` VALUES (1, '管理员', '管理整个系统');
INSERT INTO `position` VALUES (2, ' 生产员', '负责原料的申请，产品的生产');
INSERT INTO `position` VALUES (3, '审批员', '负责申请单的审批');
INSERT INTO `position` VALUES (4, '配送员', '负责原料的配送');

-- ----------------------------
-- Table structure for position_permission
-- ----------------------------
DROP TABLE IF EXISTS `position_permission`;
CREATE TABLE `position_permission`  (
                                        `position_id` bigint NOT NULL,
                                        `permission_id` bigint NOT NULL,
                                        PRIMARY KEY (`position_id`, `permission_id`) USING BTREE,
                                        INDEX `FKqsdk2yyb06wblluljxga8snq0`(`permission_id`) USING BTREE,
                                        CONSTRAINT `FKkhnkdtlsfd9u6wkim2pacu23g` FOREIGN KEY (`position_id`) REFERENCES `position` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                        CONSTRAINT `FKqsdk2yyb06wblluljxga8snq0` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of position_permission
-- ----------------------------
INSERT INTO `position_permission` VALUES (1, 1);
INSERT INTO `position_permission` VALUES (2, 2);

-- ----------------------------
-- Table structure for requisition
-- ----------------------------
DROP TABLE IF EXISTS `requisition`;
CREATE TABLE `requisition`  (
                                `id` int NOT NULL AUTO_INCREMENT,
                                `dateline` datetime(6) NULL DEFAULT NULL,
                                `date` datetime(6) NULL DEFAULT NULL,
                                `state` int NULL DEFAULT NULL,
                                `distribution_id` int NOT NULL,
                                `employee_id` bigint NOT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE INDEX `UK_9nq3qy782my7e0r15qiflg13g`(`distribution_id`) USING BTREE,
                                INDEX `FK9gvy62ovgyxm1qf1e1kur3pg`(`employee_id`) USING BTREE,
                                CONSTRAINT `FK4y3nlfdqmpw0up6i0kn42kl7p` FOREIGN KEY (`distribution_id`) REFERENCES `distribution` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                CONSTRAINT `FK9gvy62ovgyxm1qf1e1kur3pg` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of requisition
-- ----------------------------
INSERT INTO `requisition` VALUES (10, '2021-06-06 16:00:00.000000', '2021-05-27 16:54:52.576000', 1, 10, 2);
INSERT INTO `requisition` VALUES (12, '2021-05-28 16:00:00.000000', '2021-05-27 17:01:21.040000', 1, 12, 2);
INSERT INTO `requisition` VALUES (16, '2021-06-06 16:00:00.000000', '2021-05-27 17:11:21.317000', 1, 16, 2);
INSERT INTO `requisition` VALUES (17, '2021-06-06 16:00:00.000000', '2021-05-27 17:13:53.303000', 1, 17, 2);
INSERT INTO `requisition` VALUES (18, '2021-05-25 16:00:00.000000', '2021-05-27 17:14:26.424000', 0, 18, 2);
INSERT INTO `requisition` VALUES (19, '2021-05-03 16:00:00.000000', '2021-05-27 17:15:34.214000', 0, 19, 2);
INSERT INTO `requisition` VALUES (20, '2021-05-18 16:00:00.000000', '2021-05-27 17:17:40.936000', 0, 20, 2);
INSERT INTO `requisition` VALUES (24, '2021-06-22 16:00:00.000000', '2021-06-06 13:39:48.619000', 1, 24, 1);
INSERT INTO `requisition` VALUES (25, '2021-06-22 16:00:00.000000', '2021-06-08 12:25:23.618000', 1, 25, 1);

-- ----------------------------
-- Table structure for sensor
-- ----------------------------
DROP TABLE IF EXISTS `sensor`;
CREATE TABLE `sensor`  (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sensor
-- ----------------------------

-- ----------------------------
-- Table structure for sensordata
-- ----------------------------
DROP TABLE IF EXISTS `sensordata`;
CREATE TABLE `sensordata`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `time` datetime(6) NOT NULL,
                               `value` double NOT NULL,
                               `sensor_id` int NOT NULL,
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `FKa106ssx97vr4humqfbgjp4g98`(`sensor_id`) USING BTREE,
                               CONSTRAINT `FKa106ssx97vr4humqfbgjp4g98` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sensordata
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
