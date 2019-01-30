ALTER TABLE `t_app_audit`
ADD COLUMN `app_create_time` DATETIME NULL AFTER `create_date`;
ALTER TABLE `t_app_order_api_audit`
ADD COLUMN `app_name` VARCHAR(100) NULL AFTER `org_id`,
ADD COLUMN `description` VARCHAR(1000) NULL AFTER `app_name`,
ADD COLUMN `link_email` VARCHAR(100) NULL AFTER `description`,
ADD COLUMN `link_tel` VARCHAR(20) NULL AFTER `link_email`,
ADD COLUMN `link_man` VARCHAR(50) NULL AFTER `link_tel`,
ADD COLUMN `company` VARCHAR(100) NULL AFTER `link_man`,
ADD COLUMN `app_create_time` DATETIME NULL AFTER `company`,
ADD COLUMN `service_id` VARCHAR(100) NULL AFTER `app_create_time`,
ADD COLUMN `api_name` VARCHAR(100) NULL AFTER `service_id`,
ADD COLUMN `method` VARCHAR(10) NULL AFTER `api_name`,
ADD COLUMN `path` VARCHAR(100) NULL AFTER `method`;