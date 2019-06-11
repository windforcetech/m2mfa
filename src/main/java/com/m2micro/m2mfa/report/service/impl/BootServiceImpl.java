package com.m2micro.m2mfa.report.service.impl;

import com.m2micro.m2mfa.report.query.BootQuery;
import com.m2micro.m2mfa.report.service.BootService;
import com.m2micro.m2mfa.report.vo.Distributed;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BootServiceImpl  implements BootService {


  @Override
  public List<Distributed> BootShow(BootQuery bootQuery) {

    String  sql ="SELECT\n" +
        "	vmsi.start_time,\n" +
        "	bs.`name` shift_name,\n" +
        "	bm.`name` machine_name,\n" +
        "	bst.`code` staff_code,\n" +
        "	bst.staff_name,\n" +
        "	(\n" +
        "		vmsi.end_time - vmsi.start_time\n" +
        "	) use_time,\n" +
        "	bp.`name` part_name,\n" +
        "	bmd.`code` mold_code,\n" +
        "	bmd.cavity_qty,\n" +
        "	vmsi.molds,\n" +
        "	bmd.cavity_available,\n" +
        "	mms.schedule_qty,\n" +
        "	CONVERT (\n" +
        "		(\n" +
        "			CASE\n" +
        "			WHEN vmsi.end_time IS NULL THEN\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(vmsi.end_time) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			ELSE\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			END\n" +
        "		),\n" +
        "		DECIMAL (10, 2)\n" +
        "	) reach,\n" +
        "	vmsi.output_qty,\n" +
        "	CONVERT (\n" +
        "		vmsi.output_qty / (\n" +
        "			CASE\n" +
        "			WHEN vmsi.end_time IS NULL THEN\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(vmsi.end_time) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			ELSE\n" +
        "				(\n" +
        "					UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(vmsi.start_time)\n" +
        "				) / vmsi.standard_hours\n" +
        "			END\n" +
        "		),\n" +
        "		DECIMAL (10, 2)\n" +
        "	) achieving_rate,\n" +
        "	vmsi.scrap_qty,\n" +
        "	vmsi.fail_qty\n" +
        "FROM\n" +
        "	v_mes_staff_info vmsi\n" +
        "LEFT JOIN mes_mo_schedule mms ON mms.schedule_id = vmsi.schedule_id\n" +
        "LEFT JOIN base_mold bmd ON bmd.mold_id = vmsi.mold_id\n" +
        "LEFT JOIN base_parts bp ON bp.part_id = vmsi.part_id\n" +
        "LEFT JOIN base_machine bm ON bm.machine_id = vmsi.machine_id\n" +
        "LEFT JOIN base_staff bst ON bst.staff_id = vmsi.staff_id\n" +
        "LEFT JOIN base_shift bs ON bs.shift_id = (\n" +
        "	SELECT\n" +
        "		shift_id\n" +
        "	FROM\n" +
        "		mes_mo_schedule_staff\n" +
        "	WHERE\n" +
        "		schedule_id = vmsi.schedule_id\n" +
        "	AND staff_id = vmsi.staff_id\n" +
        "	LIMIT 0,\n" +
        "	1\n" +
        ") #GROUP BY bs.shift_id";


    return null;
  }
}
