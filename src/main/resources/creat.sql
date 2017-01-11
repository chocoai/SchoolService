CREATE TABLE `Teacher` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '教师序号',
`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '教师身份证号码',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '教师姓名',
`sex` int NULL COMMENT '教师性别',
`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信绑定的手机号',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信绑定的电子邮箱',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '教师备注',
`loginAccount` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录账号',
`loginPass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',
`school_id` int NULL COMMENT '学校序号',
`state` int NULL COMMENT '教师状态',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Tag` (
`id` int NOT NULL AUTO_INCREMENT COMMENT ' 标签序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '标签名称',
`type` int NULL COMMENT '标签类型（1教师、2家长、3学生）',
`state` int NULL COMMENT '标签状态',
`school_id` int NULL COMMENT '学校序号',
`category` int NULL COMMENT '标签类别（1企业，2订阅，3服务）',
`qy_id` int NULL COMMENT '企业号',
`fw_id` int NULL COMMENT '服务号',
`dy_id` int NULL COMMENT '订阅号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `TeacherTag` (
`teacher_id` int NULL COMMENT '教师序号',
`tag_id` int NULL COMMENT '标签序号'
);

CREATE TABLE `Course` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '课程序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '课程名称',
`state` int NULL COMMENT '课程状态',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Room` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '班级序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级名称',
`code` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级编号',
`state` int NULL COMMENT '班级状态',
`school_id` int NULL COMMENT '学校序号',
`enter_year` datetime NULL COMMENT '入学时间',
`blog` varchar(255) CHARACTER SET utf8 NULL COMMENT '班级博客',
PRIMARY KEY (`id`) 
);

CREATE TABLE `School` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '学校序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '学校名称',
`code` varchar(255) CHARACTER SET utf8 NULL COMMENT '学校编号',
`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '学校地址',
`state` int NULL COMMENT '学校状态',
`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',
PRIMARY KEY (`id`) 
);

CREATE TABLE `CoursePlan` (
`course_id` int NULL COMMENT '课程序号',
`teacher_id` int NULL COMMENT '教师序号',
`room_id` int NULL COMMENT '班级序号',
`sequence` int NULL COMMENT '教师序号'
);

CREATE TABLE `User_qy` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号用户序号',
`userId` varchar(255) NULL COMMENT '微信号',
`openId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信号',
`account` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信账户',
`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信电话',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT ' 微信电子邮箱',
`sex` int NULL COMMENT '性别',
`state` int NULL COMMENT '账号状态',
`teacher_id` int NULL COMMENT '教师序号',
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
`school_id` int NULL COMMENT '所属学校',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Student` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '学生序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '学生姓名',
`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '学生身份证号码',
`code` varchar(255) CHARACTER SET utf8 NULL COMMENT '学籍号',
`sex` int NULL COMMENT '学生性别',
`birth` datetime NULL COMMENT '出生日期',
`room_id` int NULL COMMENT '班级序号',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '学生备注',
`state` int NULL COMMENT '学生状态',
`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',
`adress` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系地址',
PRIMARY KEY (`id`) 
);

CREATE TABLE `User_dy` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '订阅号用户序号',
`openId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信公共号内的ID',
`unionId` int NULL,
`nickname` varchar(255) CHARACTER SET utf8 NULL COMMENT '昵称',
`sex` varchar(255) CHARACTER SET utf8 NULL COMMENT '性别',
`city` varchar(255) CHARACTER SET utf8 NULL COMMENT '城市',
`province` varchar(255) CHARACTER SET utf8 NULL COMMENT '省份',
`country` varchar(255) CHARACTER SET utf8 NULL COMMENT '国家',
`language` varchar(255) CHARACTER SET utf8 NULL COMMENT '语言',
`subscribe_time` datetime NULL COMMENT '关注时间',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '备注',
`groupid` varchar(255) CHARACTER SET utf8 NULL COMMENT '用户所在的分组ID',
`headimgurl` varchar(255) CHARACTER SET utf8 NULL COMMENT '头像地址',
`tagid_list` varchar(255) CHARACTER SET utf8 NULL COMMENT '用户被打上的标签ID列表',
`school_id` int NULL COMMENT '学校序号',
`teacher_id` int NULL COMMENT '教师序号',
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Parent` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '家长序号',
`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '家长证件号码',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '家长姓名',
`sex` int NULL COMMENT '家长性别',
`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',
`email` varchar(255) CHARACTER SET utf8 NULL COMMENT '电子邮箱',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '家长备注',
`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '家庭住址',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Identity` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '身份序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '身份类型',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Relation` (
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
`identity_id` int NULL COMMENT '身份序号',
`state` int NULL COMMENT '状态'
);

CREATE TABLE `User_fw` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '服务号用户序号',
`openId` varchar(255) CHARACTER SET utf8 NULL COMMENT '微信公共号内的ID',
`unionId` int NULL,
`nickname` varchar(255) CHARACTER SET utf8 NULL COMMENT '昵称',
`sex` varchar(255) CHARACTER SET utf8 NULL COMMENT '性别',
`city` varchar(255) CHARACTER SET utf8 NULL COMMENT '城市',
`province` varchar(255) CHARACTER SET utf8 NULL COMMENT '省份',
`country` varchar(255) CHARACTER SET utf8 NULL COMMENT '国家',
`language` varchar(255) CHARACTER SET utf8 NULL COMMENT '语言',
`subscribe_time` datetime NULL COMMENT '关注时间',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '备注',
`groupid` varchar(255) CHARACTER SET utf8 NULL COMMENT '用户所在的分组ID',
`headimgurl` varchar(255) CHARACTER SET utf8 NULL COMMENT '头像地址',
`tagid_list` varchar(255) CHARACTER SET utf8 NULL COMMENT '用户被打上的标签ID列表',
`school_id` int NULL COMMENT '学校序号',
`teacher_id` int NULL COMMENT '教师序号',
`parent_id` int NULL COMMENT '家长序号',
`student_id` int NULL COMMENT '学生序号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `ParentTag` (
`parent_id` int NULL COMMENT '家长序号',
`tag_id` int NULL COMMENT '标签序号'
);

CREATE TABLE `StudentTag` (
`student_id` int NULL COMMENT '学生序号',
`tag_id` int NULL COMMENT '标签序号'
);

CREATE TABLE `Menu` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '按钮序号',
`app_id` int NULL COMMENT '应用ID',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '按钮名称',
`type` varchar(255) CHARACTER SET utf8 NULL COMMENT '按钮类型',
`level` int NULL COMMENT '按钮级别（1,2）',
`father_id` int NULL COMMENT '父按钮',
`url` varchar(255) CHARACTER SET utf8 NULL COMMENT '链接地址',
`category` int NULL COMMENT '1订阅号2服务号3企业号',
`qy_id` int NULL COMMENT '企业号',
`fw_id` int NULL COMMENT '服务号',
`dy_id` int NULL COMMENT '订阅号',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Application` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '应用序号',
`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '应用名称',
`agentId` int NULL COMMENT '企业应用ID',
`school_id` int NULL COMMENT '所属学校',
`qy_id` int NULL COMMENT '所属企业号',
`description` varchar(255) CHARACTER SET utf8 NULL COMMENT '企业应用详情',
`redirect_domain` varchar(255) CHARACTER SET utf8 NULL COMMENT '企业应用可信域',
`report_location_flag` int NULL COMMENT '企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；2：持续上报',
`chat_extension_url` varchar(255) CHARACTER SET utf8 NULL COMMENT '关联会话url',
`isreportuser` int NULL COMMENT '是否接收用户变更通知。0：不接收；1：接收',
`isreportenter` int NULL COMMENT '是否上报用户进入应用事件。0：不接收；1：接收',
`type` int NULL COMMENT '应用类型。1：消息型；2：主页型',
`home_url` varchar(255) CHARACTER SET utf8 NULL COMMENT '主页型应用url。url必须以http或者https开头。消息型应用无需该参数',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Weixin_qy` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '企业号序号',
`state` int NULL COMMENT '企业号状态',
`corpId` varchar(255) CHARACTER SET utf8 NULL COMMENT '企业号',
`corpSecret` varchar(255) CHARACTER SET utf8 NULL COMMENT '管理组密钥',
`school_id` int NULL COMMENT '学校序号',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '备注',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Weixin_fw` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '服务号序号',
`state` int NULL COMMENT '状态',
`appId` varchar(255) CHARACTER SET utf8 NULL COMMENT '服务号',
`appSecret` varchar(255) CHARACTER SET utf8 NULL COMMENT '密钥',
`school_id` int NULL COMMENT '学校序号',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '备注',
PRIMARY KEY (`id`) 
);

CREATE TABLE `Weixin_dy` (
`id` int NOT NULL AUTO_INCREMENT COMMENT '订阅号序号',
`state` int NULL COMMENT '状态',
`appId` varchar(255) CHARACTER SET utf8 NULL COMMENT '服务号',
`appSecret` varchar(255) CHARACTER SET utf8 NULL COMMENT '密钥',
`school_id` int NULL COMMENT '学校序号',
`remark` varchar(255) CHARACTER SET utf8 NULL COMMENT '备注',
PRIMARY KEY (`id`) 
);


ALTER TABLE `TeacherTag` ADD CONSTRAINT `teacher_tag` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `TeacherTag` ADD CONSTRAINT `tag_teacher` FOREIGN KEY (`tag_id`) REFERENCES `Tag` (`id`);
ALTER TABLE `Teacher` ADD CONSTRAINT `teacher_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `CoursePlan` ADD CONSTRAINT `coursePlan_course` FOREIGN KEY (`course_id`) REFERENCES `Course` (`id`);
ALTER TABLE `CoursePlan` ADD CONSTRAINT `coursePlan_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `CoursePlan` ADD CONSTRAINT `coursePlan_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `User_qy` ADD CONSTRAINT `user_qy_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `User_qy` ADD CONSTRAINT `user_qy_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `Student` ADD CONSTRAINT `student_room` FOREIGN KEY (`room_id`) REFERENCES `Room` (`id`);
ALTER TABLE `User_qy` ADD CONSTRAINT `user_qy_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `User_qy` ADD CONSTRAINT `user_qy_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `Relation` ADD CONSTRAINT `relation_identity` FOREIGN KEY (`identity_id`) REFERENCES `Identity` (`id`);
ALTER TABLE `User_dy` ADD CONSTRAINT `user_dy_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `User_fw` ADD CONSTRAINT `user_fw_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `User_dy` ADD CONSTRAINT `user_dy_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `User_dy` ADD CONSTRAINT `user_dy_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `User_dy` ADD CONSTRAINT `user_dy_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `User_fw` ADD CONSTRAINT `user_fw_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `Teacher` (`id`);
ALTER TABLE `User_fw` ADD CONSTRAINT `user_fw_student` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `User_fw` ADD CONSTRAINT `user_fw_parent` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `ParentTag` ADD CONSTRAINT `parent_tag` FOREIGN KEY (`parent_id`) REFERENCES `Parent` (`id`);
ALTER TABLE `ParentTag` ADD CONSTRAINT `tag_parent` FOREIGN KEY (`tag_id`) REFERENCES `Tag` (`id`);
ALTER TABLE `StudentTag` ADD CONSTRAINT `student_tag` FOREIGN KEY (`student_id`) REFERENCES `Student` (`id`);
ALTER TABLE `StudentTag` ADD CONSTRAINT `tag_student` FOREIGN KEY (`tag_id`) REFERENCES `Tag` (`id`);
ALTER TABLE `Tag` ADD CONSTRAINT `tag_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `Application` ADD CONSTRAINT `app_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `Application` ADD CONSTRAINT `app_qy` FOREIGN KEY (`qy_id`) REFERENCES `User_qy` (`id`);
ALTER TABLE `Menu` ADD CONSTRAINT `menu_app` FOREIGN KEY (`app_id`) REFERENCES `Application` (`id`);
ALTER TABLE `Menu` ADD CONSTRAINT `menu_father` FOREIGN KEY (`father_id`) REFERENCES `Menu` (`id`);
ALTER TABLE `Weixin_qy` ADD CONSTRAINT `qy_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `Weixin_fw` ADD CONSTRAINT `fw_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `Weixin_dy` ADD CONSTRAINT `dy_school` FOREIGN KEY (`school_id`) REFERENCES `School` (`id`);
ALTER TABLE `Menu` ADD CONSTRAINT `menu_qy` FOREIGN KEY (`qy_id`) REFERENCES `Weixin_qy` (`id`);
ALTER TABLE `Menu` ADD CONSTRAINT `menu_fw` FOREIGN KEY (`fw_id`) REFERENCES `Weixin_fw` (`id`);
ALTER TABLE `Menu` ADD CONSTRAINT `menu_dy` FOREIGN KEY (`dy_id`) REFERENCES `Weixin_dy` (`id`);
ALTER TABLE `Tag` ADD CONSTRAINT `tag_qy` FOREIGN KEY (`qy_id`) REFERENCES `Weixin_qy` (`id`);
ALTER TABLE `Tag` ADD CONSTRAINT `tag_fw` FOREIGN KEY (`fw_id`) REFERENCES `Weixin_fw` (`id`);
ALTER TABLE `Tag` ADD CONSTRAINT `tag_dy` FOREIGN KEY (`dy_id`) REFERENCES `Weixin_dy` (`id`);

