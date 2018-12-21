CREATE VIEW v_mes_mo_desc AS SELECT
	md.*,
	bi.item_name categoryName,
	bp.`name` partName,
	bp.spec partSpec,
	brd.route_name routeName,
	bpro.process_name inputProcessName,
	bpr.process_name outputProcessName,
	bc.`name` customerName
FROM
	mes_mo_desc md,
	base_items_target bi,
	base_parts bp,
	base_route_desc brd,
	base_process bpro,
	base_process bpr,
	base_customer bc
WHERE
	md.part_id = bp.part_id
	and bi.id=md.category
AND brd.route_id = md.route_id
AND bpro.process_id = md.input_process_id
AND bpr.process_id = md.output_process_id
AND bc.customer_id = md.customer_id;