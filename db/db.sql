CREATE VIEW v_mes_mo_desc AS SELECT
	md.*, bp.`name` part_name,
	bp.spec part_spec,
	brd.route_name route_name,
	bpro.process_name input_process_name,
	bpr.process_name output_process_name,
	bc.`name` customer_name
FROM
	mes_mo_desc md,
	base_parts bp,
	base_route_desc brd,
	base_process bpro,
	base_process bpr,
	base_customer bc
WHERE
	md.part_id = bp.part_id
AND brd.route_id = md.route_id
AND bpro.process_id = md.input_process_id
AND bpr.process_id = md.output_process_id
AND bc.customer_id = md.customer_id;