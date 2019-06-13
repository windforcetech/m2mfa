DROP TABLE IF EXISTS mes_staff_info;
create table mes_staff_info
(
   mes_staff_info_id    VARCHAR(50) not null comment '主键',
   machine_id           VARCHAR(50) comment '机台',
   staff_id             VARCHAR(50) comment '人员',
   mo_id                VARCHAR(50) not null comment '工单ID',
   schedule_id          VARCHAR(50) comment '排单',
   process_id           VARCHAR(50) comment '工序',
   part_id              VARCHAR(50) not null comment '料件ID',
   start_time           datetime comment '开始时间',
   end_time             datetime comment '结束时间',
   flag                 INT comment '是否结束',
   output_qty           decimal(18,2) comment '产量',
   fail_qty             INT comment '不良数量',
   scrap_qty            INT comment '报废数量',
   mold_id              VARCHAR(50) comment '模具',
   molds                decimal(18,2) comment '模数',
   cavity_available     INT comment '穴数',
   standard_hours       decimal(18,2) comment '标准生产率',
   primary key (mes_staff_info_id)
);
alter table mes_staff_info comment '工单职员信息';