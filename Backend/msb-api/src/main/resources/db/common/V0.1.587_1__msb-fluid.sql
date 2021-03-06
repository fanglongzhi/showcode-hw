CREATE TABLE `t_rate_limit_strategy_log` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `strategy_id` int(11) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `object_id` int(11) DEFAULT NULL,
  `object_path` varchar(100) DEFAULT NULL,
  `object_name` varchar(100) DEFAULT NULL,
  `method` varchar(10) DEFAULT NULL,
  `second_limit` int(11) DEFAULT NULL,
  `minute_limit` int(11) DEFAULT NULL,
  `hour_limit` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(50) DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  `operator_type` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB