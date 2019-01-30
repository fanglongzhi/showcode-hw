CREATE TABLE `t_breaker_strategy_log` (
  `enable_breaker` int(11) DEFAULT NULL,
  `request_volume` int(11) DEFAULT NULL,
  `sleep` int(11) DEFAULT NULL,
  `fail_rate` int(11) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `strategy_id` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `is_default` int(11) DEFAULT NULL,
  `strategy_name` varchar(100) DEFAULT NULL,
  `apis` varchar(1200) DEFAULT NULL,
  `org_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;