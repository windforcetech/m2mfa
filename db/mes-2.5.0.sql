CREATE VIEW v_mes_process_qty AS SELECT
	mmsp.schedule_id,
	mmsp.process_id,
	MIN( mmsp.actual_start_time ) start_time,
	MAX( mmsp.actual_end_time ) end_time,
	sum( IFNULL( mmsp.output_qty, 0 ) ) output_qty,
	if((
	SELECT
		count( * ) 
	FROM
		mes_mo_schedule mms,
		mes_part_route mpr 
	WHERE
		mms.part_id = mpr.part_id 
		AND mms.schedule_id = mmsp.schedule_id 
		AND mpr.output_process_id = mmsp.process_id 
	),1,0) flag
FROM
	mes_mo_schedule_process mmsp 
GROUP BY
	mmsp.schedule_id,
	mmsp.process_id;
	
	
	
	CREATE VIEW v_mes_process_fail_scrap AS SELECT
	schedule_id,
	target_process_id process_id,
	sum( IFNULL( fail_qty, 0 ) ) fail_qty,
	sum( IFNULL( scrap_qty, 0 ) )  scrap_qty
FROM
	mes_record_wip_fail
GROUP BY
	schedule_id,
	target_process_id;
	
	
	CREATE VIEW v_mes_process_info AS SELECT
	mmd.mo_id AS mo_id,
	mmc.schedule_id AS schedule_id,
	bp.part_id AS part_id,
	bp1.process_id AS process_id,
	vmpq.output_qty AS output_qty,
	vmpq.flag AS flag,
	vmpq.start_time,
	vmpq.end_time,
	vs.fail_qty AS fail_qty,
	vs.scrap_qty AS scrap_qty 
FROM
	mes_mo_desc mmd,
	base_parts bp,
	mes_mo_schedule mmc,
	base_process bp1,
	v_mes_process_qty vmpq
	LEFT JOIN v_mes_process_fail_scrap vs ON ( vmpq.schedule_id = vs.schedule_id AND vmpq.process_id = vs.process_id ) 
WHERE
	mmd.part_id = bp.part_id 
	AND mmd.mo_id = mmc.mo_id 
	AND mmc.schedule_id = vmpq.schedule_id 
	AND bp1.process_id = vmpq.process_id 
	AND mmd.close_flag <> 10 
	AND mmd.close_flag <> 11;
	
	
	
	CREATE VIEW v_mes_staff_qty AS SELECT
	mrw.schedule_id,
	mrw.process_id,
	mrs.staff_id,
	mrs.start_time,
	mrs.end_time,
	mrs.start_molds,
	mrs.end_molds,
	1 as flag
FROM
	mes_record_work mrw,
	mes_record_staff mrs,
	base_station bs 
WHERE
	mrw.station_id = bs.station_id 
	AND bs.`code` = 'KZ001' 
	AND mrw.rwid = mrs.rw_id 
	AND mrs.start_molds IS NOT NULL 
	AND mrs.end_molds IS NOT NULL UNION
SELECT
	mrw.schedule_id,
	mrw.process_id,
	mrs.staff_id,
	mrs.start_time,
	now() end_time,
	mrs.start_molds,
	(
	SELECT
		imou.output 
	FROM
		mes_mo_schedule mmsc,
		base_machine bma,
		iot_machine_output imou 
	WHERE
		mmsc.machine_id = bma.machine_id 
		AND bma.id = imou.org_id 
		AND mmsc.schedule_id = mrw.schedule_id 
	) end_molds,
	0 as flag
FROM
	mes_record_work mrw,
	mes_record_staff mrs,
	base_station bs 
WHERE
	mrw.station_id = bs.station_id 
	AND bs.`code` = 'KZ001' 
	AND mrw.rwid = mrs.rw_id 
	AND mrs.start_molds IS NOT NULL 
	AND mrs.end_molds IS NULL;
	
	
	CREATE VIEW v_mes_staff_fail_scrap as SELECT
	vq.*,
	IFNULL(mrwf.fail_qty,0) fail_qty,
	IFNULL(mrwf.scrap_qty,0) scrap_qty
FROM
	v_mes_staff_qty vq LEFT JOIN
	mes_record_wip_fail mrwf 
on( vq.schedule_id=mrwf.schedule_id
AND vq.process_id=mrwf.target_process_id
AND vq.staff_id=mrwf.staff_id
AND mrwf.create_on >=vq.start_time
AND mrwf.create_on<=vq.end_time);



CREATE VIEW v_mes_staff_process_fail_scrap as SELECT
	schedule_id,
	process_id,
	staff_id,
	start_time,
	end_time,
	start_molds,
	end_molds,
	flag,
	sum( fail_qty ) fail_qty,
	sum( scrap_qty ) scrap_qty 
FROM
	v_mes_staff_fail_scrap 
GROUP BY
	schedule_id,
	process_id,
	staff_id,
	start_time,
	end_time,
	start_molds,
	end_molds,
	flag;
	
	
	CREATE VIEW v_mes_staff_info AS SELECT
mms.machine_id,
vs.staff_id,
mms.mo_id,
vs.schedule_id,
vs.process_id,
mms.part_id,
vs.start_time,
vs.end_time,
vs.flag,
vs.end_molds - vs.start_molds output_qty,
vs.fail_qty,
vs.scrap_qty,
mmsp.mold_id,
CEILING( ( vs.end_molds - vs.start_molds ) / bm.cavity_available ) molds,
bm.cavity_available,
mprs.standard_hours 
FROM
	v_mes_staff_process_fail_scrap vs,
	mes_mo_schedule mms,
	mes_mo_desc mmd,
	mes_mo_schedule_process mmsp,
	base_mold bm,
	mes_part_route mpr,
	mes_part_route_station mprs,
	base_station bs 
WHERE
	vs.schedule_id = mms.schedule_id 
	AND mms.mo_id = mmd.mo_id 
	AND mmd.close_flag <> 10 
	AND mmd.close_flag <> 11 
	AND mms.schedule_id = mmsp.schedule_id 
	AND mmsp.mold_id IS NOT NULL 
	AND mmsp.mold_id = bm.mold_id 
	AND mms.part_id = mpr.part_id 
	AND mpr.part_route_id = mprs.part_route_id 
	AND mprs.process_id = vs.process_id 
	AND mprs.station_id = bs.station_id 
	AND bs.`code` = 'KZ001';
	

create table mes_process_info
(
   mes_process_info_id  VARCHAR(50) not null comment '主键',
   mo_id                VARCHAR(50) not null comment '工单ID',
   schedule_id          VARCHAR(50) not null comment '排产单id',
   part_id              VARCHAR(50) not null comment '料件ID',
   process_id           VARCHAR(50) not null comment '工序id',
   output_qty           INT comment '产量',
   flag                 INT comment '是否产出工序',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   fail_qty             INT comment '不良数量',
   scrap_qty            INT comment '报废数量',
   primary key (mes_process_info_id)
);

alter table mes_process_info comment '工单工序信息';

create table mes_info
(
   mes_info_id          VARCHAR(50) not null comment '主键',
   mo_id                VARCHAR(50) not null comment '工单ID',
   part_id              VARCHAR(50) not null comment '料件ID',
   output_qty           INT comment '产量',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   fail_qty             INT comment '不良数量',
   scrap_qty            INT comment '报废数量',
   primary key (mes_info_id)
);

alter table mes_info comment '工单信息';


CREATE TABLE `base_led_config` (
  `config_id` varchar(50) NOT NULL COMMENT 'id',
  `elemen` varchar(20) DEFAULT NULL COMMENT '看板ip',
  `workshop` varchar(50) DEFAULT NULL COMMENT '摆放车间',
  `position` varchar(100) DEFAULT NULL COMMENT '摆放位置',
  `rate` int(11) DEFAULT NULL COMMENT '切换画面频率',
  `row_page` int(11) DEFAULT NULL COMMENT '每页显示行数',
  `col_qty` int(11) DEFAULT NULL COMMENT '机台展示列数 ',
  `row_qty` int(11) DEFAULT NULL COMMENT '机台展示行数',
  `content_font_size` varchar(10) DEFAULT NULL COMMENT '内容字号',
  `title_font_size` varchar(10) DEFAULT NULL COMMENT '标题字号',
  `machine_list` longtext COMMENT '机台对应的列表id',
  `group_id` varchar(255) DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `create_on` datetime NOT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `modified_on` datetime NOT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




CREATE TABLE `base_machine_list` (
  `machine_list_id` varchar(50) NOT NULL,
  `config_id` varchar(50) DEFAULT NULL COMMENT '看版配置项Id',
  `machine_id` varchar(50) DEFAULT NULL COMMENT '机台id',
  `group_id` varchar(255) DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `create_on` datetime NOT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `modified_on` datetime NOT NULL,
  PRIMARY KEY (`machine_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;





CREATE TABLE `mes_staff_info` (
  `mes_staff_info_id` varchar(50) NOT NULL DEFAULT '',
  `machine_id` varchar(50) DEFAULT NULL COMMENT '机台',
  `staff_id` varchar(50) DEFAULT NULL COMMENT '人员',
  `mo_id` varchar(50) DEFAULT NULL COMMENT '工单',
  `schedule_id` varchar(50) DEFAULT NULL COMMENT '排单',
  `process_id` varchar(50) DEFAULT NULL COMMENT '工序',
  `part_id` varchar(50) DEFAULT NULL COMMENT '料件',
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	