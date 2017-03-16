CREATE TABLE `Course` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '课程序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程名称',
`detail` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程描述',
`type` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程类型1必修课2选修课',
`state` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程状态0停用1可用',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Room` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '班级序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级名称(标签)',
`year` varchar(255) CHARACTER SET utf8 NULL COMMENT '入学年份',
`order` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级次序',
`slogan` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级口号',
`state` int NULL COMMENT '班级状态0停用1可用',
PRIMARY KEY (`id`) 
);

CREATE TABLE `CourseRoomTeacher` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '序号',
`course_id` int NOT NULL COMMENT '课程序号',
`room_id` int NOT NULL COMMENT '班级序号',
`teacher_id` int NOT NULL COMMENT '教师序号',
`semester_id` int NOT NULL COMMENT '学期序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Teacher` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
`userId` varchar(255) NULL COMMENT '微信号',
`login` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录名',
`pass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',
`sex` int NULL COMMENT '性别0未知1男2女',
`mobile` varchar(255) CHARACTER SET utf8 NULL COMMENT '手机号码',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT '电子邮箱',
`weixinId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信账号',
`qq` varchar(255) CHARACTER SET utf8 NULL COMMENT 'QQ号',
`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系地址',
`state` int NULL COMMENT '账号状态1关注2已冻结3取消关注4未关注',
`isManager` int NULL COMMENT '是否管理0否1是',
`picUrl` varchar(255) CHARACTER SET utf8 NULL COMMENT '头像地址',
`type` int NULL COMMENT '1在编2聘用3校外',
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
PRIMARY KEY (`id`) 
);

CREATE TABLE `Identity` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '身份序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '身份类型',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Relation` (
`parent_id` int NOT NULL COMMENT '家长序号',
`student_id` int NOT NULL COMMENT '学生序号',
`identity_id` int NOT NULL COMMENT '身份序号',
PRIMARY KEY (`parent_id`, `student_id`, `identity_id`) 
);

CREATE TABLE `Notice` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '消息序号',
`title` varchar(255) CHARACTER SET utf8 NULL COMMENT '通知标题',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '消息内容',
`time` datetime NULL COMMENT '发布时间',
`teacher_id` int NULL COMMENT '教师序号',
`semester_id` int NULL COMMENT '学期序号',
`state` int NULL COMMENT '消息状态0停用1可用',
PRIMARY KEY (`id`) 
);

CREATE TABLE `CourseMessage` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '作业序号',
`title` varchar(255) CHARACTER SET utf8 NULL COMMENT '标题',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '内容',
`time` datetime NULL COMMENT '发布时间',
`teacher_id` int NULL COMMENT '教师序号',
`course_id` int NULL COMMENT '课程序号',
`semester_id` int NULL COMMENT '学期序号',
`state` int NULL COMMENT '消息状态0停用1可用',
`reply` int NULL COMMENT '是否要求回复0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `CourseMessageRead` (
`id` int NOT NULL AUTO_INCREMENT,
`message_id` int NULL COMMENT '消息序号',
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
`identity_id` int NULL COMMENT '身份序号',
`time` datetime NULL COMMENT '阅读时间',
`state` int NULL COMMENT '是否阅读0未读1已读',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Leave` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '请假序号',
`type` int NULL COMMENT '请假类型1病假2事假3其他',
`flow` int NULL COMMENT '流转过程1申请中2已批准3未批准',
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

CREATE TABLE `Assessment` (
`id` int NOT NULL,
`title` varchar(255) CHARACTER SET utf8 NULL COMMENT '标题',
`content` varchar(255) CHARACTER SET utf8 NULL COMMENT '内容',
`time` datetime NULL COMMENT '时间',
`type` int NULL COMMENT '评定类型1表扬2批评',
`teacher_id` int NULL COMMENT '教师序号',
`course_id` int NULL COMMENT '课程序号',
`semester_id` int NULL COMMENT '学期序号',
`state` int NULL COMMENT '状态0停用1可用',
`send` int NULL COMMENT '发送范围0不发送1指定发送2全部发送',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Grade` (
`exam_id` int NOT NULL COMMENT '测试序号',
`student_id` int NOT NULL COMMENT '学生序号',
`semester_id` int NULL COMMENT '学期序号',
`time` datetime NULL COMMENT '时间',
`content` varchar(255) CHARACTER SET utf8 NULL COMMENT '评语',
`score` int NULL COMMENT '分数',
`state` int NULL COMMENT '是否评比0否1是',
PRIMARY KEY (`exam_id`, `student_id`) 
);

CREATE TABLE `Exam` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '测试序号',
`title` varchar(255) CHARACTER SET utf8 NULL COMMENT '测试标题',
`describe` varchar(255) CHARACTER SET utf8 NULL COMMENT '测试描述',
`time` datetime NULL COMMENT '测试时间',
`course_id` int NULL COMMENT '课程序号',
`semester_id` int NULL COMMENT '学期序号',
`type` int NULL COMMENT '测试类型1随堂测验2单元测验3期末测试',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '测试备注',
PRIMARY KEY (`id`) 
);

CREATE TABLE `AssessmentDetail` (
`assessment_id` int NOT NULL COMMENT '表扬序号',
`student_id` int NOT NULL COMMENT '学生序号',
`time` datetime NULL,
`content` varchar(255) CHARACTER SET utf8 NULL COMMENT '评语',
`score` int NULL COMMENT '分数',
`state` int NULL COMMENT '是否表扬0否1是',
PRIMARY KEY (`assessment_id`, `student_id`) 
);

CREATE TABLE `RoomStudent` (
`room_id` int NOT NULL COMMENT '社团序号',
`student_id` int NOT NULL COMMENT '学生序号',
PRIMARY KEY (`room_id`, `student_id`) 
);

CREATE TABLE `Schedule` (
`order_day` int NOT NULL COMMENT '星期*(1,2,3,4,5)',
`order_course` int NOT NULL COMMENT '第*堂课(1,2,3,4,5,6,7,8)',
`semester_id` int NOT NULL COMMENT '学期序号',
`time_start` datetime NULL COMMENT '开始时间',
`time_end` datetime NULL COMMENT '结束时间',
PRIMARY KEY (`order_day`, `order_course`, `semester_id`) 
);

CREATE TABLE `StudentCourse` (
`student_id` int NOT NULL COMMENT '学生序号',
`course_id` int NOT NULL COMMENT '课程序号',
`semester_id` int NOT NULL COMMENT '学期序号',
PRIMARY KEY (`student_id`, `course_id`, `semester_id`) 
);

CREATE TABLE `TeacherMessage` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '序号',
`title` varchar(255) CHARACTER SET utf8 NULL COMMENT '消息标题',
`content` varchar(255) CHARACTER SET utf8 NULL COMMENT '消息内容',
`teacher_id` int NULL COMMENT '教师序号',
`time` datetime NULL COMMENT '发送时间',
`state` int NULL COMMENT '消息状态0停用1可用',
`reply` int NULL COMMENT '是否要求回复0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `TeacherMessageRead` (
`id` int NOT NULL AUTO_INCREMENT,
`message_id` int NULL COMMENT '消息序号',
`teacher_id` int NULL COMMENT '教师序号',
`time` datetime NULL COMMENT '阅读时间',
`state` int NULL COMMENT '是否阅读0未读1已读',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Homework` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '作业序号',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '作业内容',
`time` datetime NULL COMMENT '发布时间',
`teacher_id` int NULL COMMENT '教师序号',
`course_id` int NULL COMMENT '课程序号',
`semester_id` int NULL COMMENT '学期序号',
`state` int NULL COMMENT '状态0停用1可用',
`reply` int NULL COMMENT '是否要求回复0否1是',
PRIMARY KEY (`id`) 
);

CREATE TABLE `HomeworkRead` (
`id` int NOT NULL AUTO_INCREMENT,
`homework_id` int NULL COMMENT '作业序号',
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
`identity_id` int NULL COMMENT '身份序号',
`time` datetime NULL COMMENT '阅读时间',
`state` int NULL COMMENT '是否阅读0未读1已读',
PRIMARY KEY (`id`) 
);

CREATE TABLE `HomeworkDetail` (
`homework_id` int NOT NULL COMMENT '作业序号',
`student_id` int NOT NULL COMMENT '学生序号',
`state` int NULL COMMENT '是否评比0否1是',
`time` datetime NULL COMMENT '评分时间',
`content` varchar(999) CHARACTER SET utf8 NULL COMMENT '评语',
`score` int NULL COMMENT '分数',
PRIMARY KEY (`homework_id`, `student_id`) 
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
PRIMARY KEY (`id`) 
);

CREATE TABLE `Parent` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
`userId` varchar(255) NULL COMMENT '微信号',
`login` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录名',
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
PRIMARY KEY (`id`) 
);


ALTER TABLE `CourseRoomTeacher` ADD CONSTRAINT `teacherPlan_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `CourseRoomTeacher` ADD CONSTRAINT `teacherPlan_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `CourseRoomTeacher` ADD CONSTRAINT `teacherPlan_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_parent` FOREIGN KEY (`parent_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_identity` FOREIGN KEY (`identity_id`) REFERENCES `Identity` (`id`);
ALTER TABLE `Notice` ADD CONSTRAINT `notice_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `CourseMessageRead` ADD CONSTRAINT `check_courseMessage` FOREIGN KEY (`message_id`) REFERENCES `CourseMessage` (`id`);
ALTER TABLE `CourseMessageRead` ADD CONSTRAINT `check_courseMessage_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `CourseMessage` ADD CONSTRAINT `courseMessage_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `CourseMessage` ADD CONSTRAINT `courseMessage_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `Assessment` ADD CONSTRAINT `assessment_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `Assessment` ADD CONSTRAINT `assessment_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `Exam` ADD CONSTRAINT `exam_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `Grade` ADD CONSTRAINT `grade_exam` FOREIGN KEY (`exam_id`) REFERENCES `Exam` (`id`);
ALTER TABLE `Grade` ADD CONSTRAINT `grade_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `CourseMessageRead` ADD CONSTRAINT `check_courseMessage_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `CourseMessageRead` ADD CONSTRAINT `check_courseMessage_identity` FOREIGN KEY (`identity_id`) REFERENCES `Identity` (`id`);
ALTER TABLE `AssessmentDetail` ADD CONSTRAINT `assessmentDetail_assessment` FOREIGN KEY (`assessment_id`) REFERENCES `Assessment` (`id`);
ALTER TABLE `AssessmentDetail` ADD CONSTRAINT `assessmentDetail_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `RoomStudent` ADD CONSTRAINT `studentRoom_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `RoomStudent` ADD CONSTRAINT `studentRoom_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `StudentCourse` ADD CONSTRAINT `studentCourse_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `StudentCourse` ADD CONSTRAINT `studentCourse_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `TeacherMessage` ADD CONSTRAINT `teacherMessage_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `TeacherMessageRead` ADD CONSTRAINT `teacherMessageRead_message` FOREIGN KEY (`message_id`) REFERENCES `TeacherMessage` (`id`);
ALTER TABLE `TeacherMessageRead` ADD CONSTRAINT `teacherMessageRead_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `Homework` ADD CONSTRAINT `homework_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `Homework` ADD CONSTRAINT `homework_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `HomeworkRead` ADD CONSTRAINT `homeworkRead_homework` FOREIGN KEY (`homework_id`) REFERENCES `Homework` (`id`);
ALTER TABLE `HomeworkRead` ADD CONSTRAINT `homeworkRead_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `HomeworkRead` ADD CONSTRAINT `homeworkRead_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `HomeworkRead` ADD CONSTRAINT `homeworkRead_identity` FOREIGN KEY (`identity_id`) REFERENCES `Identity` (`id`);
ALTER TABLE `HomeworkDetail` ADD CONSTRAINT `homeworkCheck_homework` FOREIGN KEY (`homework_id`) REFERENCES `Homework` (`id`);
ALTER TABLE `HomeworkDetail` ADD CONSTRAINT `homeworkCheck_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChoose_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChoose_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChoose_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `StudentCourse` ADD CONSTRAINT `studentCourse_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `CourseRoomTeacher` ADD CONSTRAINT `teacherPlan_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Schedule` ADD CONSTRAINT `schedule_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Grade` ADD CONSTRAINT `grade_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `CourseChoose` ADD CONSTRAINT `courseChoose_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Homework` ADD CONSTRAINT `homework_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Exam` ADD CONSTRAINT `exam_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Assessment` ADD CONSTRAINT `assessment_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Leave` ADD CONSTRAINT `leave_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `CourseMessage` ADD CONSTRAINT `courseMessage_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);
ALTER TABLE `Notice` ADD CONSTRAINT `notice_semester` FOREIGN KEY (`semester_id`) REFERENCES `Semester` (`id`);

