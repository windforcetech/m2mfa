DROP TABLE IF EXISTS mes_staff_info;

CREATE TABLE `mes_staff_info` (
  `mes_staff_info_id` varchar(20) NOT NULL DEFAULT '',
  `machine_id` varchar(20) DEFAULT NULL COMMENT '机台',
  `staff_id` varchar(20) DEFAULT NULL COMMENT '人员',
  `mo_id` varchar(20) DEFAULT NULL COMMENT '工单',
  `schedule_id` varchar(20) DEFAULT NULL COMMENT '排单',
  `process_id` varchar(20) DEFAULT NULL COMMENT '工序',
  `part_id` varchar(20) DEFAULT NULL COMMENT '料件',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `flag` int(11) DEFAULT NULL COMMENT '状态',
  `output_qty` decimal(20,0) DEFAULT NULL COMMENT '产量',
  `fail_qty` decimal(10,0) DEFAULT NULL,
  `scrap_qty` decimal(10,0) DEFAULT NULL COMMENT '报废',
  `mold_id` varchar(20) DEFAULT NULL COMMENT '模具',
  `molds` decimal(10,0) DEFAULT NULL COMMENT '不良',
  `cavity_available` int(11) DEFAULT NULL COMMENT '穴数',
  `standard_hours` decimal(10,0) DEFAULT NULL COMMENT '标准生产率',
  PRIMARY KEY (`mes_staff_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table mes_staff_info comment '工单职员信息';

DROP TABLE IF EXISTS mes_record_machine;


ALTER TABLE mes_record_wip_log MODIFY in_time date COMMENT '进工序时间';
ALTER TABLE mes_record_wip_log MODIFY out_time date COMMENT '出工序时间';
ALTER TABLE mes_record_wip_log MODIFY inline_time date COMMENT '进线时间';
ALTER TABLE mes_record_wip_log MODIFY outline_time date COMMENT '出线时间';