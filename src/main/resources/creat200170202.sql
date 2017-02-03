CREATE TABLE `Course` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '课程序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程名称',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Room` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '班级序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级名称',
`state` int NULL COMMENT '班级状态1激活2注销3删除',
PRIMARY KEY (`id`) 
);

CREATE TABLE `RoomPlan` (
`id` int NOT NULL AUTO_INCREMENT,
`course_id` int NULL COMMENT '课程序号',
`teacher_id` int NULL COMMENT '教师序号',
`room_id` int NULL COMMENT '班级序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Enterprise` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
`userId` varchar(255) NULL COMMENT '微信号',
`pass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',
`openId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',
`sex` int NULL COMMENT '性别0未知1男2女',
`mobile` varchar(255) CHARACTER SET utf8 NULL COMMENT '手机号码',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT '电子邮箱',
`weixinId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信账号',
`state` int NULL COMMENT '账号状态1关注2已冻结3取消关注4未关注',
`isTeacher` int NULL COMMENT '是否教师0否1是',
`isParent` int NULL COMMENT '是否家长0否1是',
`isManager` int NULL COMMENT '是否管理0否1是',
`picUrl` varchar(255) CHARACTER SET utf8 NULL COMMENT '头像地址',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Student` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '学生序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '学生姓名',
`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '学生身份证号码',
`code` varchar(255) CHARACTER SET utf8 NULL COMMENT '学籍号',
`sex` int NULL COMMENT '学生性别0未知1男2女',
`birth` datetime NULL COMMENT '出生日期',
`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '家庭地址',
`room_id` int NULL COMMENT '班级序号',
`team_id` int NULL COMMENT '社团序号',
`state` int NULL COMMENT '学生状态1激活2注销3删除',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Identity` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '身份序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '身份类型',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Relation` (
`id` int NOT NULL,
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
`identity_id` int NULL COMMENT '身份序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Notice` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '消息序号',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '消息内容',
`time` datetime NULL COMMENT '发布时间',
`teacher_id` int NULL COMMENT '教师序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Roomwork` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '作业序号',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '作业内容',
`time` datetime NULL COMMENT '发布时间',
`teacher_id` int NULL COMMENT '教师序号',
`room_id` int NULL COMMENT '班级序号',
`course_id` int NULL COMMENT '课程序号',
`state` int NULL COMMENT '消息状态1可用2注销',
PRIMARY KEY (`id`) 
);

CREATE TABLE `RoomworkRead` (
`id` int NOT NULL AUTO_INCREMENT,
`roomwork_id` int NULL COMMENT '作业序号',
`parent_id` int NULL COMMENT '家长序号',
`state` int NULL COMMENT '是否阅读0否1是',
`time` datetime NULL COMMENT '阅读时间',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Leave` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '请假序号',
`type` int NULL COMMENT '请假类型1病假2事假',
`flow` int NULL COMMENT '流转过程1申请2批准3未批准',
`parentContent` varchar(999) CHARACTER SET utf8 NULL COMMENT '请假说明',
`teacherContent` varchar(999) CHARACTER SET utf8 NULL COMMENT '教师回复',
`student_id` int NULL COMMENT '请假学生',
`parent_id` int NULL COMMENT '请假家长',
`teacher_id` int NULL COMMENT '批准教师',
`time_apply` datetime NULL COMMENT '申请时间',
`time_ratify` datetime NULL COMMENT '批准时间',
`time_start` datetime NULL COMMENT '开始时间',
`time_end` datetime NULL COMMENT '终止时间',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Praise` (
`id` int NOT NULL,
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '表扬原因',
`time` datetime NULL COMMENT '表扬时间',
`student_id` int NULL COMMENT '学生序号',
`teacher_id` int NULL COMMENT '教师序号',
`course_id` int NULL COMMENT '课程序号',
`room_id` int NULL COMMENT '班级序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Grade` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '成绩序号',
`exam_id` int NULL COMMENT '测试序号',
`student_id` int NULL COMMENT '学生序号',
`score` int NULL COMMENT '分数',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Exam` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '测试序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '测试名称',
`time` datetime NULL COMMENT '测试时间',
`course_id` int NULL COMMENT '课程序号',
`type` int NULL COMMENT '测试类型1随堂测验2单元测验3期末测试',
`room_id` int NULL COMMENT '班级序号',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '测试备注',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Team` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '社团序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '社团名称',
`state` int NULL COMMENT '1激活2注销',
PRIMARY KEY (`id`) 
);

CREATE TABLE `TeamPlan` (
`id` int NOT NULL AUTO_INCREMENT,
`team_id` int NULL COMMENT '社团序号',
`teacher_id` int NULL COMMENT '教师序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Teamwork` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '作业序号',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '作业内容',
`time` datetime NULL COMMENT '发布时间',
`teacher_id` int NULL COMMENT '教师序号',
`team_id` int NULL COMMENT '班级序号',
`state` int NULL COMMENT '消息状态1可用2注销',
PRIMARY KEY (`id`) 
);

CREATE TABLE `TeamworkRead` (
`id` int NOT NULL AUTO_INCREMENT,
`teamwork_id` int NULL COMMENT '作业序号',
`parent_id` int NULL COMMENT '家长序号',
`state` int NULL COMMENT '是否阅读0否1是',
`time` datetime NULL COMMENT '阅读时间',
PRIMARY KEY (`id`) 
);


ALTER TABLE `RoomPlan` ADD CONSTRAINT `coursePlan_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `RoomPlan` ADD CONSTRAINT `coursePlan_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `RoomPlan` ADD CONSTRAINT `coursePlan_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Student` ADD CONSTRAINT `student_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_parent` FOREIGN KEY (`parent_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_identity` FOREIGN KEY (`identity_id`) REFERENCES `Identity` (`id`);
ALTER TABLE `Notice` ADD CONSTRAINT `notice_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `RoomworkRead` ADD CONSTRAINT `check_roomwork` FOREIGN KEY (`roomwork_id`) REFERENCES `Roomwork` (`id`);
ALTER TABLE `RoomworkRead` ADD CONSTRAINT `check_roomwork_parent` FOREIGN KEY (`parent_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Roomwork` ADD CONSTRAINT `homework_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Roomwork` ADD CONSTRAINT `homework_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Roomwork` ADD CONSTRAINT `homework_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_parent` FOREIGN KEY (`parent_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Praise` ADD CONSTRAINT `praise_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Praise` ADD CONSTRAINT `praise_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Praise` ADD CONSTRAINT `praise_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `Praise` ADD CONSTRAINT `praise_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Exam` ADD CONSTRAINT `exam_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `Grade` ADD CONSTRAINT `grade_exam` FOREIGN KEY (`exam_id`) REFERENCES `Exam` (`id`);
ALTER TABLE `Grade` ADD CONSTRAINT `grade_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Exam` ADD CONSTRAINT `exam_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `TeamPlan` ADD CONSTRAINT `teamPlan_team` FOREIGN KEY (`team_id`) REFERENCES `Team` (`id`);
ALTER TABLE `TeamPlan` ADD CONSTRAINT `teamPlan_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Student` ADD CONSTRAINT `student_team` FOREIGN KEY (`team_id`) REFERENCES `Team` (`id`);
ALTER TABLE `Teamwork` ADD CONSTRAINT `teamwork_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Enterprise` (`id`);
ALTER TABLE `Teamwork` ADD CONSTRAINT `teamwork_team` FOREIGN KEY (`team_id`) REFERENCES `Team` (`id`);
ALTER TABLE `TeamworkRead` ADD CONSTRAINT `check_teamwork` FOREIGN KEY (`teamwork_id`) REFERENCES `Teamwork` (`id`);
ALTER TABLE `TeamworkRead` ADD CONSTRAINT `check_teamwork_parent` FOREIGN KEY (`parent_id`) REFERENCES `Enterprise` (`id`);

