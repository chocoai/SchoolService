CREATE TABLE `Course` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '课程序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程名称',
`detail` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程描述',
`amount` int NULL COMMENT '选课人数，0为不限制',
`type` int NULL COMMENT '课程类型1必修课2选修课',
`state` int NULL COMMENT '课程状态0停用1可用',
`del` int NULL COMMENT '删除标识0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Room` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '班级序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级名称(标签)',
`year` int NULL COMMENT '入学年份',
`order` int NULL COMMENT '班级次序',
`state` int NULL COMMENT '班级状态0停用1可用',
`del` int NULL COMMENT '删除标识0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `CourseRoomTeacherSemester` (
`course_id` int NOT NULL COMMENT '课程序号',
`room_id` int NOT NULL COMMENT '班级序号',
`teacher_id` int NOT NULL COMMENT '教师序号',
`semester_id` int NOT NULL COMMENT '学期序号',
PRIMARY KEY (`course_id`, `room_id`, `teacher_id`, `semester_id`) 
);

CREATE TABLE `Teacher` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
`userId` varchar(255) NULL COMMENT '微信号',
`pass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',
`sex` int NULL COMMENT '性别0未知1男2女',
`mobile` varchar(255) CHARACTER SET utf8 NULL COMMENT '手机号码',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT '电子邮箱',
`weixinId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信账号',
`qq` varchar(255) CHARACTER SET utf8 NULL COMMENT 'QQ号',
`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系地址',
`state` int NULL COMMENT '账号状态1关注2已冻结3取消关注4未关注',
`picUrl` varchar(255) CHARACTER SET utf8 NULL COMMENT '头像地址',
`type` int NULL COMMENT '1在编2聘用3校外',
`del` int NULL COMMENT '删除标识0否1是',
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
`state` int NULL COMMENT '学生状态0停用1可用',
`del` int NULL COMMENT '删除标识0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Identity` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '身份序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '身份类型',
PRIMARY KEY (`id`) 
);

CREATE TABLE `StudentParentIdentity` (
`parent_id` int NOT NULL COMMENT '家长序号',
`student_id` int NOT NULL COMMENT '学生序号',
`identity_id` int NOT NULL COMMENT '身份序号',
PRIMARY KEY (`parent_id`, `student_id`, `identity_id`) 
);

CREATE TABLE `Leave` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '请假序号',
`type` int NULL COMMENT '请假类型1病假2事假3其他',
`state` int NULL COMMENT '状态0未回应1已批准2已拒绝',
`reason` varchar(999) CHARACTER SET utf8 NULL COMMENT '请假原因',
`reply` varchar(999) CHARACTER SET utf8 NULL COMMENT '教师回复',
`student_id` int NULL COMMENT '请假学生',
`parent_id` int NULL COMMENT '请假家长',
`teacher_id` int NULL COMMENT '批准教师',
`semester_id` int NULL COMMENT '学期序号',
`time_apply` datetime NULL COMMENT '申请时间',
`time_ratify` datetime NULL COMMENT '批准时间',
`time_start` datetime NULL COMMENT '开始时间',
`time_end` datetime NULL COMMENT '终止时间',
PRIMARY KEY (`id`) 
);

CREATE TABLE `RoomStudent` (
`room_id` int NOT NULL COMMENT '社团序号',
`student_id` int NOT NULL COMMENT '学生序号',
PRIMARY KEY (`room_id`, `student_id`) 
);

CREATE TABLE `StudentCourseSemester` (
`student_id` int NOT NULL COMMENT '学生序号',
`course_id` int NOT NULL COMMENT '课程序号',
`semester_id` int NOT NULL COMMENT '学期序号',
PRIMARY KEY (`student_id`, `course_id`, `semester_id`) 
);

CREATE TABLE `CourseChoose` (
`id` int NOT NULL AUTO_INCREMENT,
`student_id` int NULL COMMENT '学生序号',
`course_id` int NULL COMMENT '课程序号',
`parent_id` int NULL COMMENT '家长序号',
`semester_id` int NULL COMMENT '学期序号',
`time` datetime NULL COMMENT '选课时间',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Semester` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '学期名称',
`time_start` datetime NULL COMMENT '开始时间',
`time_end` datetime NULL COMMENT '终止时间',
`state` int NULL COMMENT '状态0非当前学期1当前学期',
`del` int NULL COMMENT '删除标识0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Parent` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
`userId` varchar(255) NULL COMMENT '微信号',
`pass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',
`sex` int NULL COMMENT '性别0未知1男2女',
`mobile` varchar(255) CHARACTER SET utf8 NULL COMMENT '手机号码',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT '电子邮箱',
`weixinId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信账号',
`qq` varchar(255) CHARACTER SET utf8 NULL COMMENT 'QQ号',
`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系地址',
`state` int NULL COMMENT '账号状态1关注2已冻结3取消关注4未关注',
`picUrl` varchar(255) CHARACTER SET utf8 NULL COMMENT '头像地址',
`del` int NULL COMMENT '删除标识0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Permission` (
`id` int NOT NULL COMMENT '权限序号',
`url` varchar(255) CHARACTER SET utf8 NULL COMMENT '访问路径',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '权限名称',
PRIMARY KEY (`id`) 
);

CREATE TABLE `TeacherPermission` (
`teacher_id` int NOT NULL COMMENT '教师序号',
`permission_id` int NOT NULL COMMENT '权限序号',
`state` int NULL COMMENT '权限状态0无1有',
PRIMARY KEY (`teacher_id`, `permission_id`) 
);


ALTER TABLE `CourseRoomTeacherSemester` ADD CONSTRAINT `teacherPlan_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `CourseRoomTeacherSemester` ADD CONSTRAINT `teacherPlan_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `CourseRoomTeacherSemester` ADD CONSTRAINT `teacherPlan_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `StudentParentIdentity` ADD CONSTRAINT `relation_parent` FOREIGN KEY (`parent_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `StudentParentIdentity` ADD CONSTRAINT `relation_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `StudentParentIdentity` ADD CONSTRAINT `relation_identity` FOREIGN KEY (`identity_id`) REFERENCES `Identity` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `RoomStudent` ADD CONSTRAINT `studentRoom_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `RoomStudent` ADD CONSTRAINT `studentRoom_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `StudentCourseSemester` ADD CONSTRAINT `studentCourse_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `StudentCourseSemester` ADD CONSTRAINT `studentCourse_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChooseDetail_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChooseDetail_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChooseDetail_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `StudentCourseSemester` ADD CONSTRAINT `studentCourse_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `CourseRoomTeacherSemester` ADD CONSTRAINT `teacherPlan_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChooseDetail_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `TeacherPermission` ADD CONSTRAINT `teacherPermission_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `TeacherPermission` ADD CONSTRAINT `teacherPermission_permission` FOREIGN KEY (`permission_id`) REFERENCES `Permission` (`id`);

