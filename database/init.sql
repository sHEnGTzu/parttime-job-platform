-- ============================================================
-- 大学生兼职平台 - 数据库初始化脚本
-- 数据库类型: MariaDB / MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS part_time_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE part_time_db;

-- ============================================================
-- 1. 用户表 (user)
-- ============================================================
DROP TABLE IF EXISTS job_apply;
DROP TABLE IF EXISTS job;
DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id       INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  username VARCHAR(50)  NOT NULL COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT '密码',
  name     VARCHAR(50)  DEFAULT NULL COMMENT '姓名',
  phone    VARCHAR(20)  DEFAULT NULL COMMENT '联系方式',
  role     VARCHAR(20)  DEFAULT 'student' COMMENT '角色(admin=管理员, student=学生)',
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================================
-- 2. 兼职信息表 (job)
-- ============================================================
CREATE TABLE job (
  id          INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  title       VARCHAR(100) NOT NULL COMMENT '兼职名称',
  location    VARCHAR(100) DEFAULT NULL COMMENT '工作地点',
  salary      VARCHAR(50)  DEFAULT NULL COMMENT '薪资',
  work_time   VARCHAR(100) DEFAULT NULL COMMENT '工作时间',
  description TEXT         DEFAULT NULL COMMENT '工作内容',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='兼职信息表';

-- ============================================================
-- 3. 兼职申请表 (job_apply)
-- ============================================================
CREATE TABLE job_apply (
  id         INT          NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id    INT          NOT NULL COMMENT '用户ID',
  job_id     INT          NOT NULL COMMENT '兼职ID',
  apply_time DATETIME     NOT NULL COMMENT '申请时间',
  status     VARCHAR(20)  DEFAULT 'pending' COMMENT '申请状态(pending=待审核, approved=已通过, rejected=已拒绝)',
  PRIMARY KEY (id),
  KEY idx_user_id (user_id),
  KEY idx_job_id  (job_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='兼职申请表';

-- ============================================================
-- 4. 初始数据
-- ============================================================

-- 4.1 用户数据 (密码为明文, 与后端校验逻辑一致)
INSERT INTO user (id, username, password, name, phone, role) VALUES
(1, 'admin',    '123456',  '系统管理员', '13800000000', 'admin'),
(2, 'student1', '123456',  '张三',       '13800000001', 'student'),
(3, 'student2', '123456',  '李四',       '13800000002', 'student'),
(4, 'student3', '123456',  '王五',       '13800000003', 'student'),
(5, 'student4', '123456',  '赵六',       '13800000004', 'student'),
(6, 'student5', '123456',  '孙七',       '13800000005', 'student');

-- 4.2 兼职信息数据
INSERT INTO job (id, title, location, salary, work_time, description) VALUES
(1,  '校园图书馆管理员',     '校图书馆一楼',       '50元/小时',  '周一至周五 18:00-22:00', '负责图书整理、借还登记、维护阅览室秩序，要求工作认真负责，每周至少到岗3次。'),
(2,  '咖啡厅服务员',         '校内咖啡厅',         '45元/小时',  '轮班制 (早班 8:00-14:00 / 晚班 14:00-22:00)', '负责点单、出餐、清洁等工作，形象良好，有服务意识，有咖啡制作经验者优先。'),
(3,  '家教老师',             '学校周边小区',       '80元/小时',  '周末及晚间 (具体时间与家长协商)', '为中小学生提供一对一辅导，擅长数学、英语等科目者优先，需通过面试考核。'),
(4,  '快递驿站分拣员',       '校内快递服务中心',   '40元/小时',  '每天 12:00-14:00 + 17:00-19:00', '负责快递包裹的入库分拣、扫码上架、取件出库，工作细心、吃苦耐劳。'),
(5,  '新媒体运营助理',       '线上 (远程办公)',    '60元/小时',  '灵活排班 (每周累计不少于10小时)', '负责微信公众号推文撰写、排版发布、后台数据统计，有文案功底者优先。'),
(6,  '行政助理',             '行政楼A302',         '55元/小时',  '周一至周五 14:00-17:00', '协助老师处理日常行政事务，包括文件整理、数据录入、会议安排等，熟练使用Office软件。'),
(7,  '活动策划助理',         '学生活动中心',       '50元/小时',  '根据活动安排灵活排班', '参与校园活动的策划与执行，撰写活动方案，协助现场组织与协调，有大次活动经验者优先。'),
(8,  '实验室助理',           '实验楼C栋',          '45元/小时',  '周一至周五 18:00-21:00', '负责实验室日常管理，实验器材整理与维护，协助老师准备实验材料。'),
(9,  '超市收银员',           '校内超市',           '42元/小时',  '轮班制 8:00-12:00 / 12:00-16:00 / 16:00-22:00', '负责超市收银、商品陈列、库存盘点，要求细心认真，有收银经验者优先。'),
(10, '外卖配送员',           '校园内',             '35元/小时 + 每单1元提成', '午间 11:00-13:00 / 晚间 17:00-19:00', '负责校园内外卖订单的配送，自备电动车优先，熟悉校园路线，准时高效。'),
(11, '机房维护助理',         '计算中心',           '50元/小时',  '周一至周五 18:00-21:00', '负责公共机房的设备巡检、故障报修、软件维护，要求掌握基本的计算机硬件知识。');

-- 4.3 申请记录数据
INSERT INTO job_apply (id, user_id, job_id, apply_time, status) VALUES
(1,  2, 1,  '2026-06-01 10:30:00', 'approved'),
(2,  2, 3,  '2026-06-01 14:15:00', 'pending'),
(3,  3, 2,  '2026-06-02 09:00:00', 'approved'),
(4,  3, 4,  '2026-06-02 09:30:00', 'rejected'),
(5,  4, 1,  '2026-06-03 11:00:00', 'pending'),
(6,  4, 6,  '2026-06-03 15:20:00', 'approved'),
(7,  5, 5,  '2026-06-04 08:45:00', 'pending'),
(8,  5, 8,  '2026-06-04 16:00:00', 'pending'),
(9,  6, 7,  '2026-06-05 10:10:00', 'rejected'),
(10, 6, 10, '2026-06-05 13:30:00', 'approved');
