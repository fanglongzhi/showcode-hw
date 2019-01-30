ALTER TABLE `t_app_order_api_audit`
ADD COLUMN `is_annoymous_access` tinyint(1)  NULL DEFAULT '0';
ALTER TABLE `t_service_api_audit`
ADD COLUMN `is_annoymous_access` TINYINT(1) NULL DEFAULT '0';

