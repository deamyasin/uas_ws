-- phpMyAdmin SQL Dump
-- version 4.9.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 15, 2020 at 06:50 AM
-- Server version: 5.6.41-84.1
-- PHP Version: 7.3.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `iclaunch_restaurant`
--

-- --------------------------------------------------------

--
-- Table structure for table `api_logs`
--

DROP TABLE IF EXISTS `api_logs`;
CREATE TABLE `api_logs` (
  `id` int(11) NOT NULL,
  `uri` varchar(255) NOT NULL,
  `method` varchar(6) NOT NULL,
  `params` text,
  `api_key` varchar(40) NOT NULL,
  `ip_address` varchar(45) NOT NULL,
  `device` varchar(255) NOT NULL,
  `version` varchar(10) NOT NULL,
  `language` varchar(30) NOT NULL,
  `time` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `rtime` float DEFAULT NULL,
  `authorized` varchar(1) NOT NULL,
  `response_code` smallint(3) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `app_pages`
--

DROP TABLE IF EXISTS `app_pages`;
CREATE TABLE `app_pages` (
  `page_id` int(11) NOT NULL,
  `page_title_en` varchar(255) CHARACTER SET utf8 NOT NULL,
  `page_content_en` longtext CHARACTER SET utf8 NOT NULL,
  `page_title_ar` varchar(255) CHARACTER SET utf8 NOT NULL,
  `page_content_ar` longtext CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `app_pages`
--

INSERT INTO `app_pages` (`page_id`, `page_title_en`, `page_content_en`, `page_title_ar`, `page_content_ar`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 'About Us', '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 0, 0, 0),
(3, 'Contact Us', '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 0, 0, 0),
(5, 'Terms and Condtions', '', '', '', '0000-00-00 00:00:00', '0000-00-00 00:00:00', 0, 0, 0),
(7, 'Privacy Policy', '<p>demo privacy policies.</p>\r\n', '', '', '0000-00-00 00:00:00', '2020-08-14 09:07:05', 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `banners`
--

DROP TABLE IF EXISTS `banners`;
CREATE TABLE `banners` (
  `banner_id` int(11) NOT NULL,
  `banner_title_en` varchar(200) NOT NULL,
  `banner_title_ar` varchar(200) NOT NULL,
  `banner_image` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL,
  `product_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS `branches`;
CREATE TABLE `branches` (
  `branch_id` int(11) NOT NULL,
  `branch_name_en` varchar(255) NOT NULL,
  `branch_name_ar` varchar(255) NOT NULL,
  `opening_time` time NOT NULL,
  `closing_time` time NOT NULL,
  `postal_code` varchar(30) NOT NULL,
  `address_en` varchar(255) NOT NULL,
  `area_en` varchar(255) NOT NULL,
  `address_ar` varchar(255) NOT NULL,
  `area_ar` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `delivery_area_in_km` decimal(10,0) NOT NULL,
  `is_active` tinyint(4) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `cart_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `qty` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `cart_option`
--

DROP TABLE IF EXISTS `cart_option`;
CREATE TABLE `cart_option` (
  `cart_option_id` int(11) NOT NULL,
  `cart_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `product_option_id` int(11) NOT NULL,
  `qty` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `category_id` int(11) NOT NULL,
  `cat_name_en` varchar(200) NOT NULL,
  `cat_name_ar` varchar(200) NOT NULL,
  `cat_image` varchar(255) NOT NULL,
  `cat_banner` varchar(255) NOT NULL,
  `cat_sort_order` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL,
  `status` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `ci_sessions`
--

DROP TABLE IF EXISTS `ci_sessions`;
CREATE TABLE `ci_sessions` (
  `id` varchar(128) NOT NULL,
  `ip_address` varchar(45) NOT NULL,
  `timestamp` int(10) UNSIGNED NOT NULL DEFAULT '0',
  `data` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `contact_request`
--

DROP TABLE IF EXISTS `contact_request`;
CREATE TABLE `contact_request` (
  `contact_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `fullname` varchar(255) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `message` longtext CHARACTER SET utf8 NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `coupons`
--

DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `coupon_id` int(11) NOT NULL,
  `coupon_code` varchar(50) NOT NULL,
  `users` varchar(255) NOT NULL,
  `coupon_type` varchar(30) NOT NULL,
  `multi_usage` tinyint(4) NOT NULL,
  `discount` double NOT NULL,
  `discount_type` varchar(30) NOT NULL,
  `min_order_amount` double NOT NULL,
  `max_discount_amount` double NOT NULL,
  `validity_start` date NOT NULL,
  `validity_end` date NOT NULL,
  `description_en` longtext NOT NULL,
  `description_ar` longtext NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `delivery_boy`
--

DROP TABLE IF EXISTS `delivery_boy`;
CREATE TABLE `delivery_boy` (
  `delivery_boy_id` int(11) NOT NULL,
  `boy_name` varchar(255) CHARACTER SET utf8 NOT NULL,
  `boy_phone` varchar(30) CHARACTER SET utf8 NOT NULL,
  `boy_password` varchar(255) CHARACTER SET utf8 NOT NULL,
  `boy_email` varchar(255) CHARACTER SET utf8 NOT NULL,
  `boy_id_proof` varchar(255) CHARACTER SET utf8 NOT NULL,
  `boy_licence` varchar(255) CHARACTER SET utf8 NOT NULL,
  `id_photo` varchar(255) CHARACTER SET utf8 NOT NULL,
  `licence_photo` varchar(255) CHARACTER SET utf8 NOT NULL,
  `vehicle_no` varchar(255) NOT NULL,
  `boy_photo` varchar(255) CHARACTER SET utf8 NOT NULL,
  `android_token` longtext CHARACTER SET utf8 NOT NULL,
  `ios_token` longtext CHARACTER SET utf8 NOT NULL,
  `status` tinyint(4) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `email_templates`
--

DROP TABLE IF EXISTS `email_templates`;
CREATE TABLE `email_templates` (
  `template_id` int(11) NOT NULL,
  `email_subject_en` longtext CHARACTER SET utf8 NOT NULL,
  `email_message_en` longtext CHARACTER SET utf8 NOT NULL,
  `email_subject_ar` longtext CHARACTER SET utf8 NOT NULL,
  `email_message_ar` longtext CHARACTER SET utf8 NOT NULL,
  `email_tags` longtext NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` int(11) NOT NULL,
  `lang` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `email_templates`
--

INSERT INTO `email_templates` (`template_id`, `email_subject_en`, `email_message_en`, `email_subject_ar`, `email_message_ar`, `email_tags`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`, `lang`) VALUES
(1, 'Thanks for new User', '<p>Hi&nbsp;##user_full_name##,</p>\r\n\r\n<p>Thanks for register with FOOD ORDER. You may use your phone number&nbsp;##user_phone## for access app.&nbsp;</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Thanks,</p>\r\n', 'Thanks for New User', '<p>Hi&nbsp;##user_full_name##,</p>\r\n\r\n<p>Thanks for register with FOOD ORDER. You may use your phone number&nbsp;##user_phone## for access app.&nbsp;</p>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Thanks,</p>\r\n', 'user_phone, user_full_name', '0000-00-00 00:00:00', '2020-07-25 10:40:58', 0, 1, 0, 'english'),
(2, 'Order Received Email', '<p>Hi,</p>\r\n\r\n<p>Your order placed successfully</p>\r\n\r\n<p>##order_details##</p>\r\n', 'Order Received Email', '<p>Hi,</p>\r\n\r\n<p>Your order placed successfully</p>\r\n\r\n<p>##order_details##</p>\r\n', 'order_details', '0000-00-00 00:00:00', '2020-07-25 10:25:58', 0, 1, 0, 'arabic'),
(3, 'Admin New User Received', '<p>Hi Food Order,</p>\r\n\r\n<p>New User registered with APP.</p>\r\n\r\n<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px\">\r\n	<tbody>\r\n		<tr>\r\n			<td>Full Name</td>\r\n			<td>##user_full_name##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Phone</td>\r\n			<td>##user_phone##</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n\r\n<p>&nbsp;</p>\r\n', 'Admin New User Received', '<p>Hi Food Order,</p>\r\n\r\n<p>New User registered with APP.</p>\r\n\r\n<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\r\n	<tbody>\r\n		<tr>\r\n			<td>Full Name</td>\r\n			<td>##user_full_name##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Phone</td>\r\n			<td>##user_phone##</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n', 'user_phone, user_full_name', '0000-00-00 00:00:00', '2020-07-25 10:10:58', 0, 1, 0, 'english'),
(4, 'Admin New Order Received', '<p>Hi Food Order,</p>\r\n\r\n<p>You received new Order.&nbsp;</p>\r\n\r\n<p>##order_details##</p>\r\n', 'Admin New Order Received', '<p>Hi Food Order,</p>\r\n\r\n<p>You received new Order.&nbsp;</p>\r\n\r\n<p>##order_details##</p>\r\n', 'order_details', '0000-00-00 00:00:00', '2020-07-25 10:53:57', 0, 1, 0, 'arabic'),
(5, 'Contact Request', '<p>Hi FOOD ORDER,</p>\r\n\r\n<p>You received new contact request. Please follow them.&nbsp;</p>\r\n\r\n<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px\">\r\n	<tbody>\r\n		<tr>\r\n			<td>Full Name</td>\r\n			<td>##full_name##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Phone</td>\r\n			<td>##phone##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Date</td>\r\n			<td>##date_time##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Message</td>\r\n			<td>##message##</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Thanks.</p>\r\n', 'Contact Request', '<p>Hi FOOD ORDER,</p>\r\n\r\n<p>You received new contact request. Please follow them.&nbsp;</p>\r\n\r\n<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\">\r\n	<tbody>\r\n		<tr>\r\n			<td>Full Name</td>\r\n			<td>##full_name##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Phone</td>\r\n			<td>##phone##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Date</td>\r\n			<td>##date_time##</td>\r\n		</tr>\r\n		<tr>\r\n			<td>Message</td>\r\n			<td>##message##</td>\r\n		</tr>\r\n	</tbody>\r\n</table>\r\n\r\n<p>&nbsp;</p>\r\n\r\n<p>Thanks.</p>\r\n', 'full_name,phone,message,date_time', '0000-00-00 00:00:00', '2020-07-25 10:37:57', 0, 1, 0, 'arabic'),
(6, 'Reply Contact Request', '<p>Hi&nbsp;##full_name##,</p>\r\n\r\n<p>Thanks for contact FOOD ORDER, we will contact you soon.&nbsp;</p>\r\n', 'Reply Contact Request', '<p>Hi&nbsp;##full_name##,</p>\r\n\r\n<p>Thanks for contact FOOD ORDER, we will contact you soon.&nbsp;</p>\r\n', 'full_name,phone,message,date_time', '0000-00-00 00:00:00', '2020-07-25 10:17:57', 0, 1, 0, 'arabic');

-- --------------------------------------------------------

--
-- Table structure for table `keys`
--

DROP TABLE IF EXISTS `keys`;
CREATE TABLE `keys` (
  `id` int(11) NOT NULL,
  `key` varchar(40) NOT NULL,
  `level` int(2) NOT NULL,
  `ignore_limits` tinyint(1) NOT NULL DEFAULT '0',
  `date_created` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------

--
-- Table structure for table `logs`
--

DROP TABLE IF EXISTS `logs`;
CREATE TABLE `logs` (
  `log_id` int(11) NOT NULL,
  `data_table` varchar(120) NOT NULL,
  `data_action` varchar(20) NOT NULL,
  `log` longtext NOT NULL,
  `user_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `ip` varchar(15) NOT NULL,
  `tablepkid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `noti_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `title_ar` varchar(255) CHARACTER SET utf8 NOT NULL,
  `message_ar` longtext CHARACTER SET utf8 NOT NULL,
  `title_en` varchar(255) CHARACTER SET utf8 NOT NULL,
  `message_en` longtext CHARACTER SET utf8 NOT NULL,
  `type` varchar(50) NOT NULL,
  `type_id` int(11) NOT NULL,
  `object_json` longtext NOT NULL,
  `attachment` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
CREATE TABLE `options` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `value` longtext NOT NULL,
  `type` varchar(30) NOT NULL,
  `autoload` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `options`
--

INSERT INTO `options` (`id`, `name`, `value`, `type`, `autoload`) VALUES
(1, 'name', 'Food Order', 'general_setting', 1),
(2, 'copyright', 'DS INFOWAY', 'general_setting', 1),
(3, 'website', 'http://dsinfoway.com/', 'general_setting', 1),
(4, 'currency', 'USD', 'general_setting', 1),
(5, 'currency_symbol', '$', 'general_setting', 1),
(6, 'gateway_charges', '0', 'general_setting', 1),
(7, 'app_contact', '0123456789', 'app_setting', 1),
(8, 'app_whatsapp', '0123456789', 'app_setting', 1),
(9, 'app_email', 'contact@dsinfoway.com', 'app_setting', 1),
(10, 'billing_name', 'DS INFOWAY', 'billing', 1),
(11, 'billing_address', 'demo address', 'billing', 1),
(12, 'tax_id', 'demo tax', 'billing', 1),
(13, 'billing_contact', '0123456789', 'billing', 1),
(14, 'billing_email', 'demo@demo.com', 'billing', 1),
(15, 'billing_note', 'this is privacy notes for demo', 'billing', 1),
(16, 'billing_signature', 'demo signature', 'billing', 1),
(17, 'mobile_prefix', '+91', 'general_setting', 1),
(18, 'username', 'SMSUSERNAME', 'sms_setting', 1),
(19, 'password', 'SMSPASSWORD', 'sms_setting', 1),
(20, 'sender', 'SENDERID', 'sms_setting', 1),
(21, 'delivery_charge', '5', 'general_setting', 1),
(22, 'sms_via', 'general', 'sms_setting', 1),
(23, 'twilio_sender_id', '', 'sms_setting', 1),
(24, 'twilio_account_sid', '', 'sms_setting', 1),
(25, 'twilio_auth_token', '', 'sms_setting', 1),
(26, 'nexmo_from', '', 'sms_setting', 1),
(27, 'nexmo_api_key', '', 'sms_setting', 1),
(28, 'nexmo_secret_key', '', 'sms_setting', 1),
(29, 'sms_link', 'https://www.yoursmsgateway.com?mobile=[mobile]&message=[message]&sender=XYZSENDR', 'sms_setting', 1),
(30, 'mail_via', 'smtp', 'email_settings', 1),
(31, 'smtp_protocol', 'smtp', 'email_settings', 1),
(32, 'smtp_host', '', 'email_settings', 1),
(33, 'smtp_user', '', 'email_settings', 1),
(34, 'smtp_pass', '', 'email_settings', 1),
(35, 'smtp_port', '465', 'email_settings', 1),
(36, 'smtp_crypto', 'ssl', 'email_settings', 1),
(37, 'sendgrid_sender', '', 'email_settings', 1),
(38, 'sendgrid_api_key', '', 'email_settings', 1),
(39, 'pay_via', 'payumoney', 'payment_settings', 1),
(40, 'enable_cod', 'on', 'payment_settings', 1),
(41, 'enable_payonline', 'on', 'payment_settings', 1),
(42, 'paypal_enviroment', 'sandbox', 'payment_settings', 1),
(43, 'paypal_client_id', '', 'payment_settings', 1),
(44, 'paypal_client_secret', '', 'payment_settings', 1),
(45, 'payu_merchant_key', '', 'payment_settings', 1),
(46, 'payu_salt', '', 'payment_settings', 1),
(47, 'email_sender', 'contact@dsinfoway.com', 'email_settings', 1),
(48, 'payu_enviroment', 'sandbox', 'payment_settings', 1),
(49, 'google_api_key', '', 'keys', 1),
(50, 'one_signal_id', '', 'keys', 1),
(51, 'one_signal_key', '', 'keys', 1),
(52, 'default_timezone', 'ASIA', 'general_setting', 1),
(53, 'date_default_timezone', 'Asia/Kolkata', 'general_setting', 1);

-- --------------------------------------------------------
INSERT INTO `options` ( `name`, `value`, `type`, `autoload`) VALUES ( 'env_token', '4f613328ff02e1b0ac5fce28400013d8eb9ce71d99e4c1c98e6dadbc340ef4e1659e0f684d3e3d80107f139f914015930era0615oUxduHtDXC3P6W0hMtcC03ioO8LN1WtJzRo9BXFCHDzyU2LvhiUQPhwaFUqKmauOZBd0Xx09wg9880Og--', 'token', '1');

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `branch_id` int(11) NOT NULL,
  `order_no` varchar(50) NOT NULL,
  `order_date` datetime NOT NULL,
  `order_type` enum('pickup','delivery') NOT NULL,
  `user_id` int(11) NOT NULL,
  `coupon_code` varchar(50) NOT NULL,
  `discount` double NOT NULL,
  `discount_type` varchar(20) NOT NULL,
  `discount_amount` double NOT NULL,
  `order_amount` double NOT NULL,
  `net_amount` double NOT NULL,
  `paid_amount` double NOT NULL,
  `paid_by` varchar(255) NOT NULL,
  `payment_ref` varchar(255) NOT NULL,
  `payment_log` longtext NOT NULL,
  `paid_date` datetime NOT NULL,
  `delivery_amount` double NOT NULL,
  `gateway_charges` decimal(10,2) NOT NULL,
  `user_address_id` int(11) NOT NULL,
  `order_note` longtext NOT NULL,
  `status` int(11) NOT NULL,
  `delivery_boy_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `order_delivery_address`
--

DROP TABLE IF EXISTS `order_delivery_address`;
CREATE TABLE `order_delivery_address` (
  `order_delivery_address_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `house_no` varchar(100) NOT NULL,
  `add_on_house_no` varchar(200) NOT NULL,
  `postal_code` varchar(30) NOT NULL,
  `street_name` varchar(200) NOT NULL,
  `area` varchar(255) NOT NULL,
  `city` varchar(200) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `order_qty` int(11) NOT NULL,
  `product_price` double NOT NULL,
  `discount_id` int(11) NOT NULL,
  `discount_amount` double NOT NULL,
  `discount` int(11) NOT NULL,
  `discount_type` varchar(20) NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `order_item_options`
--

DROP TABLE IF EXISTS `order_item_options`;
CREATE TABLE `order_item_options` (
  `order_item_option_id` int(11) NOT NULL,
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_option_id` int(11) NOT NULL,
  `order_qty` int(11) NOT NULL,
  `option_price` double NOT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `order_status`
--

DROP TABLE IF EXISTS `order_status`;
CREATE TABLE `order_status` (
  `status_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `created_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_at` datetime NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `postal_codes`
--

DROP TABLE IF EXISTS `postal_codes`;
CREATE TABLE `postal_codes` (
  `postal_code_id` int(11) NOT NULL,
  `postal_code` varchar(200) NOT NULL,
  `users_limits` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `product_name_en` varchar(200) NOT NULL,
  `product_name_ar` varchar(200) NOT NULL,
  `product_desc_en` text NOT NULL,
  `product_desc_ar` text NOT NULL,
  `level` int(11) NOT NULL,
  `is_veg` tinyint(4) NOT NULL,
  `product_image` varchar(255) NOT NULL,
  `is_promotional` tinyint(4) NOT NULL,
  `price` double NOT NULL,
  `price_note` varchar(255) NOT NULL,
  `calories` varchar(100) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `product_discounts`
--

DROP TABLE IF EXISTS `product_discounts`;
CREATE TABLE `product_discounts` (
  `product_discount_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `discount` double NOT NULL,
  `discount_type` varchar(30) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `product_options`
--

DROP TABLE IF EXISTS `product_options`;
CREATE TABLE `product_options` (
  `product_option_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `option_name_en` varchar(200) NOT NULL,
  `option_name_ar` varchar(200) NOT NULL,
  `option_desc_en` text NOT NULL,
  `option_desc_ar` text NOT NULL,
  `price` double NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL,
  `multiple` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `user_type_id` int(11) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_firstname` varchar(255) NOT NULL,
  `user_lastname` varchar(255) NOT NULL,
  `user_phone` varchar(50) NOT NULL,
  `user_image` varchar(255) NOT NULL,
  `user_company_name` varchar(255) NOT NULL,
  `user_company_id` varchar(255) NOT NULL,
  `android_token` longtext NOT NULL,
  `ios_token` longtext NOT NULL,
  `is_email_verified` tinyint(4) NOT NULL,
  `is_mobile_verified` tinyint(4) NOT NULL,
  `verify_token` varchar(255) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `req_queue` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_type_id`, `user_email`, `user_password`, `user_firstname`, `user_lastname`, `user_phone`, `user_image`, `user_company_name`, `user_company_id`, `android_token`, `ios_token`, `is_email_verified`, `is_mobile_verified`, `verify_token`, `status`, `req_queue`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 1, 'admin@gmail.com', 'e10adc3949ba59abbe56e057f20f883e', 'Admin', 'User', '1111111111', 'ed9d000b7f21720f449a69b6922dabbd_DS LOGO 2.jpg', 'wayweb', 'W0001', '', '', 0, 0, '4ab8f3960ea51263b6daf54555cbda44', 1, 0, '0000-00-00 00:00:00', '2020-07-19 12:54:00', 0, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `user_address_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `address_line1` varchar(255) NOT NULL,
  `postal_code` varchar(30) NOT NULL,
  `address_line2` varchar(255) NOT NULL,
  `city` varchar(200) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `is_active` tinyint(4) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `user_settings`
--

DROP TABLE IF EXISTS `user_settings`;
CREATE TABLE `user_settings` (
  `user_setting_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `general_notifications` tinyint(4) NOT NULL,
  `order_notifications` tinyint(4) NOT NULL,
  `general_emails` tinyint(4) NOT NULL,
  `order_emails` tinyint(4) NOT NULL,
  `created_at` datetime NOT NULL,
  `modified_at` datetime NOT NULL,
  `created_by` int(11) NOT NULL,
  `modified_by` int(11) NOT NULL,
  `draft` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_types`
--

DROP TABLE IF EXISTS `user_types`;
CREATE TABLE `user_types` (
  `user_type_id` int(11) NOT NULL,
  `type` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_types`
--

INSERT INTO `user_types` (`user_type_id`, `type`) VALUES
(1, 'Admin'),
(2, 'App User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `api_logs`
--
ALTER TABLE `api_logs`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `app_pages`
--
ALTER TABLE `app_pages`
  ADD PRIMARY KEY (`page_id`);

--
-- Indexes for table `banners`
--
ALTER TABLE `banners`
  ADD PRIMARY KEY (`banner_id`);

--
-- Indexes for table `branches`
--
ALTER TABLE `branches`
  ADD PRIMARY KEY (`branch_id`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`cart_id`);

--
-- Indexes for table `cart_option`
--
ALTER TABLE `cart_option`
  ADD PRIMARY KEY (`cart_option_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `ci_sessions`
--
ALTER TABLE `ci_sessions`
  ADD KEY `ci_sessions_timestamp` (`timestamp`);

--
-- Indexes for table `contact_request`
--
ALTER TABLE `contact_request`
  ADD PRIMARY KEY (`contact_id`);

--
-- Indexes for table `coupons`
--
ALTER TABLE `coupons`
  ADD PRIMARY KEY (`coupon_id`);

--
-- Indexes for table `delivery_boy`
--
ALTER TABLE `delivery_boy`
  ADD PRIMARY KEY (`delivery_boy_id`),
  ADD UNIQUE KEY `boy_phone` (`boy_phone`);

--
-- Indexes for table `email_templates`
--
ALTER TABLE `email_templates`
  ADD PRIMARY KEY (`template_id`);

--
-- Indexes for table `keys`
--
ALTER TABLE `keys`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`log_id`);

--
-- Indexes for table `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`noti_id`);

--
-- Indexes for table `options`
--
ALTER TABLE `options`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `order_delivery_address`
--
ALTER TABLE `order_delivery_address`
  ADD PRIMARY KEY (`order_delivery_address_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`order_item_id`);

--
-- Indexes for table `order_item_options`
--
ALTER TABLE `order_item_options`
  ADD PRIMARY KEY (`order_item_option_id`) USING BTREE;

--
-- Indexes for table `order_status`
--
ALTER TABLE `order_status`
  ADD PRIMARY KEY (`status_id`);

--
-- Indexes for table `postal_codes`
--
ALTER TABLE `postal_codes`
  ADD PRIMARY KEY (`postal_code_id`),
  ADD UNIQUE KEY `postal_code` (`postal_code`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `product_discounts`
--
ALTER TABLE `product_discounts`
  ADD PRIMARY KEY (`product_discount_id`);

--
-- Indexes for table `product_options`
--
ALTER TABLE `product_options`
  ADD PRIMARY KEY (`product_option_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_phone` (`user_phone`);

--
-- Indexes for table `user_address`
--
ALTER TABLE `user_address`
  ADD PRIMARY KEY (`user_address_id`);

--
-- Indexes for table `user_settings`
--
ALTER TABLE `user_settings`
  ADD PRIMARY KEY (`user_setting_id`);

--
-- Indexes for table `user_types`
--
ALTER TABLE `user_types`
  ADD UNIQUE KEY `user_role_id` (`user_type_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `api_logs`
--
ALTER TABLE `api_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `app_pages`
--
ALTER TABLE `app_pages`
  MODIFY `page_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `banners`
--
ALTER TABLE `banners`
  MODIFY `banner_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `branches`
--
ALTER TABLE `branches`
  MODIFY `branch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `cart_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `cart_option`
--
ALTER TABLE `cart_option`
  MODIFY `cart_option_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `contact_request`
--
ALTER TABLE `contact_request`
  MODIFY `contact_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `coupons`
--
ALTER TABLE `coupons`
  MODIFY `coupon_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `delivery_boy`
--
ALTER TABLE `delivery_boy`
  MODIFY `delivery_boy_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `email_templates`
--
ALTER TABLE `email_templates`
  MODIFY `template_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `keys`
--
ALTER TABLE `keys`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `logs`
--
ALTER TABLE `logs`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notifications`
--
ALTER TABLE `notifications`
  MODIFY `noti_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT for table `options`
--
ALTER TABLE `options`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;

--
-- AUTO_INCREMENT for table `order_delivery_address`
--
ALTER TABLE `order_delivery_address`
  MODIFY `order_delivery_address_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=111;

--
-- AUTO_INCREMENT for table `order_item_options`
--
ALTER TABLE `order_item_options`
  MODIFY `order_item_option_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=179;

--
-- AUTO_INCREMENT for table `order_status`
--
ALTER TABLE `order_status`
  MODIFY `status_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;

--
-- AUTO_INCREMENT for table `postal_codes`
--
ALTER TABLE `postal_codes`
  MODIFY `postal_code_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `product_discounts`
--
ALTER TABLE `product_discounts`
  MODIFY `product_discount_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `product_options`
--
ALTER TABLE `product_options`
  MODIFY `product_option_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `user_address`
--
ALTER TABLE `user_address`
  MODIFY `user_address_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `user_settings`
--
ALTER TABLE `user_settings`
  MODIFY `user_setting_id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
