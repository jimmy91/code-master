
-- 字典表 --
DROP TABLE IF EXISTS `system_dict_type`;
CREATE TABLE `system_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典类型',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `dict_type` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表';
INSERT INTO system_dict_type (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1, '用户性别', 'system_user_sex', 0, NULL, 'admin', '2021-01-05 17:03:48', '', '2022-02-01 16:30:31', b'0');
INSERT INTO system_dict_type (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2, '参数类型', 'infra_config_type', 0, NULL, 'admin', '2021-01-05 17:03:48', '', '2022-02-01 16:36:54', b'0');
INSERT INTO system_dict_type (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3, '通知类型', 'system_notice_type', 0, NULL, 'admin', '2021-01-05 17:03:48', '', '2022-02-01 16:35:26', b'0');
INSERT INTO system_dict_type (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (4, '操作类型', 'system_operate_type', 0, NULL, 'admin', '2021-01-05 17:03:48', '1', '2022-02-16 09:32:21', b'0');
INSERT INTO system_dict_type (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (5, '系统状态', 'common_status', 0, NULL, 'admin', '2021-01-05 17:03:48', '', '2022-02-01 16:21:28', b'0');

DROP TABLE IF EXISTS `system_dict_data`;
CREATE TABLE `system_dict_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `sort` int NOT NULL DEFAULT '0' COMMENT '字典排序',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典标签',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字典类型',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `color_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '颜色类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'css 样式',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='字典数据表';
INSERT INTO system_dict_data (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1, 1, '男', '1', 'system_user_sex', 0, 'default', '', '', 'admin', '2021-01-05 17:03:48', '1', '2022-08-23 17:52:15', b'0');
INSERT INTO system_dict_data (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2, 2, '女', '2', 'system_user_sex', 1, 'success', '', '性别女', 'admin', '2021-01-05 17:03:48', '1', '2022-02-16 01:30:51', b'0');
INSERT INTO system_dict_data (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (8, 1, '正常', '1', 'infra_job_status', 0, 'success', '', '正常状态', 'admin', '2021-01-05 17:03:48', '1', '2022-02-16 19:33:38', b'0');
INSERT INTO system_dict_data (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (9, 2, '暂停', '2', 'infra_job_status', 0, 'danger', '', '停用状态', 'admin', '2021-01-05 17:03:48', '1', '2022-02-16 19:33:45', b'0');

-- 字典表 --

-- 商品秒杀 --
DROP TABLE IF EXISTS `seckill`;
CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开启时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `version` int(11) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
insert  into `seckill`(`seckill_id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`version`) values (1000,'1000元秒杀iphone8',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0),(1001,'500元秒杀ipad2',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0),(1002,'300元秒杀小米4',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0),(1003,'200元秒杀红米note',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0);

DROP TABLE IF EXISTS `success_killed`;
CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品id',
  `user_id` bigint(20) NOT NULL COMMENT '用户Id',
  `state` tinyint(4) NOT NULL COMMENT '状态标示：-1指无效，0指成功，1指已付款',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
-- 商品秒杀 --


DROP TABLE IF EXISTS `table_test`;
CREATE TABLE `table_test`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测试字符串',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '测试日期值',
  `sal` decimal(10, 2) NULL DEFAULT NULL COMMENT '测试double 值',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '测试枚举值',
  `success` bit(1) NULL DEFAULT NULL COMMENT '测试bool 值',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件',
  `idcard` char(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `job` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职业',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '测试表' ROW_FORMAT = Compact;


-- 事务  --

DROP TABLE IF EXISTS `trx_dept`;
CREATE TABLE `trx_dept`  (
  `deptno` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门编号',
  `dname` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `loc` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '位置',
   PRIMARY KEY (`deptno`)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门信息表' ROW_FORMAT = Compact;


-- ----------------------------
-- Table structure for emp
-- ----------------------------
DROP TABLE IF EXISTS `trx_emp`;
CREATE TABLE `trx_emp` (
  `empno` int NOT NULL COMMENT '员工编号',
  `ename` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '员工姓名',
  `job` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '职业',
  `mgr` int DEFAULT NULL COMMENT '上级编号',
  `hiredate` datetime DEFAULT NULL COMMENT '雇佣日期',
  `sal` decimal(10,2) DEFAULT NULL COMMENT '薪水',
  `comm` decimal(10,2) DEFAULT NULL COMMENT '奖金',
  `deptno` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门编号',
  PRIMARY KEY (`empno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='员工信息表';

-- 事务  --

CREATE TABLE `ok_geo` (
  `id` varchar(255) DEFAULT NULL,
  `pid` varchar(255) DEFAULT NULL,
  `deep` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `ext_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `geo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `polygon` longblob
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 文件服务表
CREATE TABLE `file_detail`
(
    `id`                varchar(32)  NOT NULL COMMENT '文件id',
    `url`               varchar(512) NOT NULL COMMENT '文件访问地址',
    `size`              bigint(20)   DEFAULT NULL COMMENT '文件大小，单位字节',
    `filename`          varchar(256) DEFAULT NULL COMMENT '文件名称',
    `original_filename` varchar(256) DEFAULT NULL COMMENT '原始文件名',
    `base_path`         varchar(256) DEFAULT NULL COMMENT '基础存储路径',
    `path`              varchar(256) DEFAULT NULL COMMENT '存储路径',
    `ext`               varchar(32)  DEFAULT NULL COMMENT '文件扩展名',
    `content_type`      varchar(32)  DEFAULT NULL COMMENT 'MIME类型',
    `platform`          varchar(32)  DEFAULT NULL COMMENT '存储平台',
    `th_url`            varchar(512) DEFAULT NULL COMMENT '缩略图访问路径',
    `th_filename`       varchar(256) DEFAULT NULL COMMENT '缩略图名称',
    `th_size`           bigint(20)   DEFAULT NULL COMMENT '缩略图大小，单位字节',
    `th_content_type`   varchar(32)  DEFAULT NULL COMMENT '缩略图MIME类型',
    `object_id`         varchar(32)  DEFAULT NULL COMMENT '文件所属对象id',
    `object_type`       varchar(32)  DEFAULT NULL COMMENT '文件所属对象类型，例如用户头像，评价图片',
    `attr`              text COMMENT '附加属性',
    `create_time`       datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='文件记录表';



-- ------------- mysql所有数据类型示例数据  START ----------------
DROP TABLE IF EXISTS mysql_table ;
CREATE TABLE mysql_table (
    id INT PRIMARY KEY COMMENT '唯一标识符',
    tinyint_col TINYINT COMMENT 'TINYINT类型列',
    smallint_col SMALLINT COMMENT 'SMALLINT类型列',
    mediumint_col MEDIUMINT COMMENT 'MEDIUMINT类型列',
    int_col INT COMMENT 'INT类型列',
    bigint_col BIGINT COMMENT 'BIGINT类型列',
    float_col FLOAT COMMENT 'FLOAT类型列',
    double_col DOUBLE COMMENT 'DOUBLE类型列',
    decimal_col DECIMAL(10,2) COMMENT 'DECIMAL类型列',
    char_col CHAR(10) COMMENT 'CHAR类型列',
    varchar_col VARCHAR(50) COMMENT 'VARCHAR类型列',
    binary_col BINARY(10) COMMENT 'BINARY类型列',
    varbinary_col VARBINARY(50) COMMENT 'VARBINARY类型列',
    tinyblob_col TINYBLOB COMMENT 'TINYBLOB类型列',
    tinytext_col TINYTEXT COMMENT 'TINYTEXT类型列',
    blob_col BLOB COMMENT 'BLOB类型列',
    text_col TEXT COMMENT 'TEXT类型列',
    mediumblob_col MEDIUMBLOB COMMENT 'MEDIUMBLOB类型列',
    mediumtext_col MEDIUMTEXT COMMENT 'MEDIUMTEXT类型列',
    longblob_col LONGBLOB COMMENT 'LONGBLOB类型列',
    longtext_col LONGTEXT COMMENT 'LONGTEXT类型列',
    enum_col ENUM('value1', 'value2', 'value3') COMMENT 'ENUM类型列',
    set_col SET('value1', 'value2', 'value3') COMMENT 'SET类型列',
    date_col DATE COMMENT 'DATE类型列',
    time_col TIME COMMENT 'TIME类型列',
    datetime_col DATETIME COMMENT 'DATETIME类型列',
    timestamp_col TIMESTAMP COMMENT 'TIMESTAMP类型列',
    year_col YEAR COMMENT 'YEAR类型列',
    boolean_col BOOLEAN COMMENT 'BOOLEAN类型列',
    bit_col BIT(8) COMMENT 'BIT类型列',
    json_col JSON COMMENT 'JSON类型列',
    geometry_col GEOMETRY COMMENT 'GEOMETRY类型列',
    point_col POINT COMMENT 'POINT类型列',
    linestring_col LINESTRING COMMENT 'LINESTRING类型列',
    polygon_col POLYGON COMMENT 'POLYGON类型列',
    geometrycollection_col GEOMETRYCOLLECTION COMMENT 'GEOMETRYCOLLECTION类型列',
    multipoint_col MULTIPOINT COMMENT 'MULTIPOINT类型列',
    multilinestring_col MULTILINESTRING COMMENT 'MULTILINESTRING类型列',
    multipolygon_col MULTIPOLYGON COMMENT 'MULTIPOLYGON类型列'
) COMMENT '这是一个测试表，用于演示不同类型的列';

DELETE  FROM mysql_table ;
INSERT INTO mysql_table
(id, tinyint_col, smallint_col, mediumint_col, int_col, bigint_col, float_col, double_col, decimal_col, char_col, varchar_col,
 binary_col, varbinary_col, tinyblob_col, tinytext_col,

 blob_col, text_col,

 mediumblob_col, mediumtext_col,
 longblob_col, longtext_col,

 enum_col, set_col, date_col, time_col, datetime_col, timestamp_col, year_col,
 boolean_col, bit_col, json_col,

 geometry_col, point_col,
 linestring_col, polygon_col, geometrycollection_col, multipoint_col,
 multilinestring_col, multipolygon_col)
VALUES
(1, 1, -32768, -8388608, -2147483648, -9223372036854775808, -1.23, -4.56, -7890.12, 'B', 'Goodbye World',
 0x68656C6C6F, 0x776F726C640D0A, 0x68656C6C6F20776F726C64, 'This is a tiny text.',

 0x74696E79626C6F620D0A746869732069732061206D656469756D20626C6F62, 'This is a medium text',

 'It is a medium blob', 0x74696E79626C6F620D0A746869732069732061206C6F6E6720626C6F62,
 'It is a long blob.', 'This is a long text.',

 'value2', 'value1,value3', '2022-04-02', '01:23:45', '2022-04-02 01:23:45', '2022-04-02 01:23:45', 2022,
 false, b'01010101', '{"name": "Bob", "age": 40}',

 ST_GeomFromText('POINT(121.477727 31.229498)'), ST_GeomFromText('POINT(121.477727 31.229498)'),
 ST_GeomFromText('LINESTRING(121.477727 31.229498,121.477645 31.228925)'),
 ST_GeomFromText('POLYGON((121.477645 31.228925,121.477727 31.229498,121.477727 31.229498,121.477645 31.228925))'),
 ST_GeomFromText('GEOMETRYCOLLECTION(POINT(121.477727 31.229498),LINESTRING(121.477727 31.229498,121.477645 31.228925))'),
 ST_GeomFromText('MULTIPOINT((121.477727 31.229498),(121.477645 31.228925))'),
 ST_GeomFromText('MULTILINESTRING((121.477727 31.229498,121.477645 31.228925),(121.477445 31.228726,121.476819 31.228735))'),
 ST_GeomFromText('MULTIPOLYGON(((-73.964268 40.792881, -73.964513 40.792615, -73.96532 40.792824, -73.965059 40.793096, -73.964268 40.792881)),((-73.969769 40.781134, -73.971757 40.782094, -73.973188 40.781171, -73.971035 40.780154, -73.969769 40.781134)))')
 )
 ,

( 2, 1, 32767, 8388607, 2147483647, 9223372036854775807, 1.23, 4.56, 7890.12, 'B', 'Goodbye World',
  0x68656C6C6F, 0x776F726C640D0A, 0x68656C6C6F20776F726C64, 'This is a tiny text.',

  0x74696E79626C6F620D0A746869732069732061206D656469756D20626C6F62, 'This is a medium text',

 'It is a medium blob', 0x74696E79626C6F620D0A746869732069732061206C6F6E6720626C6F62,
 'It is a long blob.', 'This is a long text.',

 'value1', 'value1,value2', '2022-04-02', '01:23:45', '2022-04-02 01:23:45', '2022-04-02 01:23:45', 2022,
 TRUE, b'01010101', '{"name": "Jimmy", "age": 22}',

 ST_GeomFromText('POINT(121.477727 31.229498)'), ST_GeomFromText('POINT(121.477727 31.229498)'),
 ST_GeomFromText('LINESTRING(121.477727 31.229498,121.477645 31.228925)'),
 ST_GeomFromText('POLYGON((121.477645 31.228925,121.477727 31.229498,121.477727 31.229498,121.477645 31.228925))'),
 ST_GeomFromText('GEOMETRYCOLLECTION(POINT(121.477727 31.229498),LINESTRING(121.477727 31.229498,121.477645 31.228925))'),
 ST_GeomFromText('MULTIPOINT((121.477727 31.229498),(121.477645 31.228925))'),
 ST_GeomFromText('MULTILINESTRING((121.477727 31.229498,121.477645 31.228925),(121.477445 31.228726,121.476819 31.228735))'),
 ST_GeomFromText('MULTIPOLYGON(((-73.964268 40.792881, -73.964513 40.792615, -73.96532 40.792824, -73.965059 40.793096, -73.964268 40.792881)),((-73.969769 40.781134, -73.971757 40.782094, -73.973188 40.781171, -73.971035 40.780154, -73.969769 40.781134)))')

 );

-- ------------- mysql所有数据类型示例数据  END ----------------








