/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : school_service

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2017-01-20 12:49:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程序号',
  `name` varchar(255) DEFAULT NULL COMMENT '课程名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', '班主任');
INSERT INTO `course` VALUES ('2', '语文');
INSERT INTO `course` VALUES ('3', '数学');
INSERT INTO `course` VALUES ('4', '英语');

-- ----------------------------
-- Table structure for courseplan
-- ----------------------------
DROP TABLE IF EXISTS `courseplan`;
CREATE TABLE `courseplan` (
  `id` int(11) NOT NULL,
  `course_id` int(11) DEFAULT NULL COMMENT '课程序号',
  `teacher_id` int(11) DEFAULT NULL COMMENT '教师序号',
  `room_id` int(11) DEFAULT NULL COMMENT '班级序号',
  PRIMARY KEY (`id`),
  KEY `coursePlan_course` (`course_id`),
  KEY `coursePlan_teacher` (`teacher_id`),
  KEY `coursePlan_room` (`room_id`),
  CONSTRAINT `coursePlan_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `coursePlan_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `coursePlan_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of courseplan
-- ----------------------------

-- ----------------------------
-- Table structure for enterprise
-- ----------------------------
DROP TABLE IF EXISTS `enterprise`;
CREATE TABLE `enterprise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
  `userId` varchar(255) DEFAULT NULL COMMENT '微信号',
  `pass` varchar(255) DEFAULT NULL COMMENT '登录密码',
  `openId` varchar(255) DEFAULT NULL COMMENT '微信号',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `work` varchar(255) DEFAULT NULL COMMENT '工作单位',
  `sex` int(11) DEFAULT NULL COMMENT '性别0未知1男2女',
  `phone` varchar(255) DEFAULT NULL COMMENT '备用电话',
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `weixinId` varchar(255) DEFAULT NULL COMMENT '微信账号',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `picUrl` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `state` int(11) DEFAULT NULL COMMENT '账号状态1关注2未关注3冻结关注4取消关注',
  `isTeacher` int(11) DEFAULT NULL COMMENT '是否教师0否1是',
  `isParent` int(11) DEFAULT NULL COMMENT '是否家长0否1是',
  `isManager` int(11) DEFAULT NULL COMMENT '是否管理0否1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of enterprise
-- ----------------------------
INSERT INTO `enterprise` VALUES ('1', 'WangTianShuo', 'wts', null, '王天硕', null, null, '1', null, '18653145531', null, null, null, 'http://shp.qpic.cn/bizmp/ZVx4icpOeichXg9lLYicEWAxwZgLIMOcKaruicXHCsdZFKB6t38U8EW1XQ/', '1', '1', '0', '1');
INSERT INTO `enterprise` VALUES ('2', 'XuHanQi', 'wts', null, '徐汉琪', null, null, '0', null, '13864100771', null, null, null, 'http://shp.qpic.cn/bizmp/ZVx4icpOeichXg9lLYicEWAxwZgLIMOcKar4pX7uQLQUQBo6E5fVJqLMQ/', '1', '1', '0', '0');
INSERT INTO `enterprise` VALUES ('3', 'WangMingLiang', 'wts', null, '王明亮', null, null, '1', null, '13969190978', null, null, null, 'http://shp.qpic.cn/bizmp/ZVx4icpOeichXg9lLYicEWAxwZgLIMOcKarPx9G3VVkWgtbaOicREtnEkg/', '1', '1', '0', '1');
INSERT INTO `enterprise` VALUES ('4', 'WangQun', 'wts', null, '王群', null, null, '2', null, '15949700280', null, null, null, 'http://shp.qpic.cn/bizmp/ZVx4icpOeichVPTvhDrfQHA4ZZNG4UC6UDe9ibekzyomiam7r9Bk9HEhbw/', '1', '1', '0', '0');

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '测试序号',
  `name` varchar(255) DEFAULT NULL COMMENT '测试名称',
  `time` datetime DEFAULT NULL COMMENT '测试时间',
  `course_id` int(11) DEFAULT NULL COMMENT '课程序号',
  `type` int(11) DEFAULT NULL COMMENT '测试类型1随堂测验2单元测验3期末测试',
  `room_id` int(11) DEFAULT NULL COMMENT '班级序号',
  `remark` varchar(255) DEFAULT NULL COMMENT '测试备注',
  PRIMARY KEY (`id`),
  KEY `exam_course` (`course_id`),
  KEY `exam_room` (`room_id`),
  CONSTRAINT `exam_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `exam_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam
-- ----------------------------

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '成绩序号',
  `exam_id` int(11) DEFAULT NULL COMMENT '测试序号',
  `student_id` int(11) DEFAULT NULL COMMENT '学生序号',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`),
  KEY `grade_exam` (`exam_id`),
  KEY `grade_student` (`student_id`),
  CONSTRAINT `grade_exam` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`id`),
  CONSTRAINT `grade_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of grade
-- ----------------------------

-- ----------------------------
-- Table structure for homework
-- ----------------------------
DROP TABLE IF EXISTS `homework`;
CREATE TABLE `homework` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业序号',
  `content` varchar(999) DEFAULT NULL COMMENT '作业内容',
  `time` datetime DEFAULT NULL COMMENT '发布时间',
  `teacher_id` int(11) DEFAULT NULL COMMENT '教师序号',
  `room_id` int(11) DEFAULT NULL COMMENT '班级序号',
  `course_id` int(11) DEFAULT NULL COMMENT '课程序号',
  PRIMARY KEY (`id`),
  KEY `homework_teacher` (`teacher_id`),
  KEY `homework_room` (`room_id`),
  KEY `homework_course` (`course_id`),
  CONSTRAINT `homework_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `homework_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `homework_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of homework
-- ----------------------------

-- ----------------------------
-- Table structure for homeworkread
-- ----------------------------
DROP TABLE IF EXISTS `homeworkread`;
CREATE TABLE `homeworkread` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homework_id` int(11) DEFAULT NULL COMMENT '作业序号',
  `parent_id` int(11) DEFAULT NULL COMMENT '家长序号',
  `state` int(11) DEFAULT NULL COMMENT '是否阅读0否1是',
  `time` datetime DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`),
  KEY `check_homework` (`homework_id`),
  KEY `check_homework_parent` (`parent_id`),
  CONSTRAINT `check_homework` FOREIGN KEY (`homework_id`) REFERENCES `homework` (`id`),
  CONSTRAINT `check_homework_parent` FOREIGN KEY (`parent_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of homeworkread
-- ----------------------------

-- ----------------------------
-- Table structure for identity
-- ----------------------------
DROP TABLE IF EXISTS `identity`;
CREATE TABLE `identity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '身份序号',
  `name` varchar(255) DEFAULT NULL COMMENT '身份类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of identity
-- ----------------------------
INSERT INTO `identity` VALUES ('1', '爸爸');
INSERT INTO `identity` VALUES ('2', '妈妈');
INSERT INTO `identity` VALUES ('3', '爷爷');
INSERT INTO `identity` VALUES ('4', '奶奶');
INSERT INTO `identity` VALUES ('5', '姥姥');
INSERT INTO `identity` VALUES ('6', '姥爷');

-- ----------------------------
-- Table structure for leave
-- ----------------------------
DROP TABLE IF EXISTS `leave`;
CREATE TABLE `leave` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '请假序号',
  `type` int(11) DEFAULT NULL COMMENT '请假类型1病假2事假',
  `flow` int(11) DEFAULT NULL COMMENT '流转过程1申请2批准3未批准',
  `parentContent` varchar(999) DEFAULT NULL COMMENT '请假说明',
  `teacherContent` varchar(999) DEFAULT NULL COMMENT '教师回复',
  `student_id` int(11) DEFAULT NULL COMMENT '请假学生',
  `parent_id` int(11) DEFAULT NULL COMMENT '请假家长',
  `teacher_id` int(11) DEFAULT NULL COMMENT '批准教师',
  `time_apply` datetime DEFAULT NULL COMMENT '申请时间',
  `time_ratify` datetime DEFAULT NULL COMMENT '批准时间',
  `time_start` datetime DEFAULT NULL COMMENT '开始时间',
  `time_end` datetime DEFAULT NULL COMMENT '终止时间',
  PRIMARY KEY (`id`),
  KEY `leave_student` (`student_id`),
  KEY `leave_parent` (`parent_id`),
  KEY `leave_teacher` (`teacher_id`),
  CONSTRAINT `leave_parent` FOREIGN KEY (`parent_id`) REFERENCES `enterprise` (`id`),
  CONSTRAINT `leave_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `leave_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of leave
-- ----------------------------

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息序号',
  `content` varchar(999) DEFAULT NULL COMMENT '消息内容',
  `time` datetime DEFAULT NULL COMMENT '发布时间',
  `teacher_id` int(11) DEFAULT NULL COMMENT '教师序号',
  PRIMARY KEY (`id`),
  KEY `notice_teacher` (`teacher_id`),
  CONSTRAINT `notice_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------

-- ----------------------------
-- Table structure for praise
-- ----------------------------
DROP TABLE IF EXISTS `praise`;
CREATE TABLE `praise` (
  `id` int(11) NOT NULL,
  `content` varchar(999) DEFAULT NULL COMMENT '表扬原因',
  `time` datetime DEFAULT NULL COMMENT '表扬时间',
  `student_id` int(11) DEFAULT NULL COMMENT '学生序号',
  `teacher_id` int(11) DEFAULT NULL COMMENT '教师序号',
  `course_id` int(11) DEFAULT NULL COMMENT '课程序号',
  `room_id` int(11) DEFAULT NULL COMMENT '班级序号',
  PRIMARY KEY (`id`),
  KEY `praise_student` (`student_id`),
  KEY `praise_teacher` (`teacher_id`),
  KEY `praise_course` (`course_id`),
  KEY `praise_room` (`room_id`),
  CONSTRAINT `praise_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `praise_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `praise_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `praise_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `enterprise` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of praise
-- ----------------------------

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL COMMENT '家长序号',
  `student_id` int(11) DEFAULT NULL COMMENT '学生序号',
  `identity_id` int(11) DEFAULT NULL COMMENT '身份序号',
  PRIMARY KEY (`id`),
  KEY `relation_parent` (`parent_id`),
  KEY `relation_student` (`student_id`),
  KEY `relation_identity` (`identity_id`),
  CONSTRAINT `relation_identity` FOREIGN KEY (`identity_id`) REFERENCES `identity` (`id`),
  CONSTRAINT `relation_parent` FOREIGN KEY (`parent_id`) REFERENCES `enterprise` (`id`),
  CONSTRAINT `relation_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relation
-- ----------------------------

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '班级序号',
  `name` varchar(255) DEFAULT NULL COMMENT '班级名称',
  `state` int(11) DEFAULT NULL COMMENT '班级状态1激活2注销3删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of room
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '学生序号',
  `name` varchar(255) DEFAULT NULL COMMENT '学生姓名',
  `number` varchar(255) DEFAULT NULL COMMENT '学生身份证号码',
  `code` varchar(255) DEFAULT NULL COMMENT '学籍号',
  `sex` int(11) DEFAULT NULL COMMENT '学生性别0未知1男2女',
  `birth` datetime DEFAULT NULL COMMENT '出生日期',
  `room_id` int(11) DEFAULT NULL COMMENT '班级序号',
  `remark` varchar(255) DEFAULT NULL COMMENT '学生备注',
  `state` int(11) DEFAULT NULL COMMENT '学生状态1激活2注销3删除',
  `phone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `adress` varchar(255) DEFAULT NULL COMMENT '联系地址',
  PRIMARY KEY (`id`),
  KEY `student_room` (`room_id`),
  CONSTRAINT `student_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
