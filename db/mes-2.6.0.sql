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

create table mes_record_machine
(
   id                   VARCHAR(50) not null comment '主键',
   machine_id           VARCHAR(50) comment '机台',
   schedule_id          VARCHAR(50) comment '排产单id',
   part_id              VARCHAR(50) comment '料件ID',
   mold_id              VARCHAR(50) comment '模具id',
   cavity_qty           INT comment '模具穴数',
   cavity_available     INT comment '生产穴数',
   open_qty             INT comment '开模次数',
   power                decimal(18,2) comment '电量',
   shift_id             VARCHAR(50) comment '班别',
   staff_id             VARCHAR(50) comment '操作员id',
   create_on            datetime comment '创建时间',
   primary key (id)
);

alter table mes_record_machine comment '机台抄读历史记录';