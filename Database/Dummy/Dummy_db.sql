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
-- Dumping data for table `banners`
--

INSERT INTO `banners` (`banner_id`, `banner_title_en`, `banner_title_ar`, `banner_image`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`, `product_id`) VALUES
(1, 'home', 'home', '52bd5c2bb144f75c1cda2a0b6e417387.jpg', '2020-04-21 09:23:16', '2020-08-15 04:45:50', 1, 1, 0, 10),
(2, 'Noodles', 'Noodles', 'f49da48a2800dc8de497cd466500612d.jpg', '2020-08-15 04:07:51', '0000-00-00 00:00:00', 1, 0, 0, 11),
(3, 'Burger', 'Burger', 'ef8cf9629b6d6a3becc33f2fb61897ef.jpg', '2020-08-15 04:57:54', '2020-08-15 04:14:55', 1, 1, 0, 9);

-- --------------------------------------------------------

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
-- Dumping data for table `branches`
--

INSERT INTO `branches` (`branch_id`, `branch_name_en`, `branch_name_ar`, `opening_time`, `closing_time`, `postal_code`, `address_en`, `area_en`, `address_ar`, `area_ar`, `phone`, `latitude`, `longitude`, `delivery_area_in_km`, `is_active`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 'Branchen', 'branchar', '10:30:00', '20:30:00', '123456', 'B 69 70, 8-A National Highway', 'University Road', 'B 69 70, 8-A National Highway', 'University Road', 'morbi', 123456, 654321, 0, 1, '2020-04-24 12:44:19', '2020-05-06 12:29:46', 1, 1, 0),
(2, 'JecobJo', 'JecobJo', '08:30:00', '20:30:00', '63001', 'Nr JK Chowk, Pushkardham Main Road', 'Pushkar Dham', 'Nr JK Chowk, Pushkardham Main Road', 'Pushkar Dham', '+96698765432', 5467854, 563456, 0, 1, '2020-04-26 09:14:47', '2020-05-06 14:34:17', 1, 1, 0);

-- --------------------------------------------------------

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
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`category_id`, `cat_name_en`, `cat_name_ar`, `cat_image`, `cat_banner`, `cat_sort_order`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`, `status`) VALUES
(1, 'indian food', 'indian food ar', '4bba200bf95311adf60bb71ef2d9c81e.jpg', 'ccbf62e4aef604773e0a3a94047a424a.jpg', 0, '2020-04-21 08:47:55', '2020-07-22 07:34:35', 1, 1, 0, 1),
(2, 'Italian', 'Italian', 'cfa3def42ae878613d4f277aa0fe3468.jpg', '12842460029d4fca3db7370d2a18ec3a.jpg', 0, '2020-05-05 08:54:47', '2020-07-22 07:21:36', 1, 1, 0, 1),
(3, 'chinese', 'chinese', '1d85e01ee68397a13b61c65a42aba4cf.jpg', 'f320c4806b362d573bdb1a9b93ca42ec.jpg', 0, '2020-05-05 08:12:50', '2020-07-22 07:21:37', 1, 1, 0, 1),
(4, 'Middle Eastern', 'Middle Eastern', '0f8593cd9fb1dc090e225c5ade2eaca4.jpg', '87e31b375f2b253519f8f1081c1b6321.webp', 0, '2020-05-06 08:35:29', '2020-07-22 07:40:38', 1, 1, 0, 1);

-- --------------------------------------------------------

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
-- Dumping data for table `coupons`
--

INSERT INTO `coupons` (`coupon_id`, `coupon_code`, `users`, `coupon_type`, `multi_usage`, `discount`, `discount_type`, `min_order_amount`, `max_discount_amount`, `validity_start`, `validity_end`, `description_en`, `description_ar`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 'A0001', '', 'general', 1, 5, 'flat', 20, 100, '2020-04-21', '2020-05-27', 'test discount', 'test discount', '2020-04-21 11:09:10', '2020-08-14 07:54:17', 1, 1, 0);

-- --------------------------------------------------------

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
-- Dumping data for table `delivery_boy`
--

INSERT INTO `delivery_boy` (`delivery_boy_id`, `boy_name`, `boy_phone`, `boy_password`, `boy_email`, `boy_id_proof`, `boy_licence`, `id_photo`, `licence_photo`, `vehicle_no`, `boy_photo`, `android_token`, `ios_token`, `status`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 'Demo Boy 1', '8888888888', 'e10adc3949ba59abbe56e057f20f883e', 'demo_boy@dsinfoway.com', '09A222939S', '', '', '', 'XX00AAAA', '15a4558cc048807080ef4ebc88e06a3f.jpg', '78d5ca1e-7a7d-4dec-8563-b43921748768', '', 1, '2020-04-03 07:35:39', '2020-08-15 11:20:25', 1, 1, 0),
(2, 'Demo Boy 2', '7777777777', 'e10adc3949ba59abbe56e057f20f883e', 'demo_boy2@dsinfoway.com', '123321', '', '', '', 'XX00AAAA', '89c7bb440634f09968aad16e1cf09947.jpg', '1f1309df-21f1-4a82-9a6a-c3712a4eb1b4', '', 1, '2020-04-14 19:37:52', '2020-08-14 12:27:59', 1, 1, 0),
(3, 'demo man', '8989898989', 'e10adc3949ba59abbe56e057f20f883e', 'demo@demo.com', 'licence', '', '3997922a15b2330df85ff2e1f20ec0c0.png', '575166bda7487c632997a982b3b69d43.png', 'jckldklcj9090', '117271d11d8f19427a1318b4691fe28a.png', '', '', 0, '2020-08-14 07:30:47', '2020-08-14 07:53:47', 1, 1, 0);

-- --------------------------------------------------------

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
(53, 'item_id', '28601748', 'app_setting', 1),
(54, 'api_key', '8ca27279-50c3-4869-b747-53e9a5d874bc', 'app_setting', 1),
(55, 'date_default_timezone', 'Asia/Kolkata', 'general_setting', 1);


-- --------------------------------------------------------
INSERT INTO `options` ( `name`, `value`, `type`, `autoload`) VALUES ('env_token', '4f613328ff02e1b0ac5fce28400013d8eb9ce71d99e4c1c98e6dadbc340ef4e1659e0f684d3e3d80107f139f914015930era0615oUxduHtDXC3P6W0hMtcC03ioO8LN1WtJzRo9BXFCHDzyU2LvhiUQPhwaFUqKmauOZBd0Xx09wg9880Og--', 'token', '1');

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
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `category_id`, `product_name_en`, `product_name_ar`, `product_desc_en`, `product_desc_ar`, `level`, `is_veg`, `product_image`, `is_promotional`, `price`, `price_note`, `calories`, `status`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 1, 'gujarati', 'gujarati', '', '', 0, 1, 'eae12a34d424984dd2fb7a9da9cd4f43.jpg', 0, 150, '', '120', 1, '2020-04-25 12:57:50', '2020-08-15 06:18:21', 1, 1, 0),
(2, 1, 'punjabi', 'punjabi', '', '', 0, 1, '7b9cd8c15121aac22fa84223751d8ef1.jpg', 0, 300, '', '150', 1, '2020-04-25 12:19:56', '2020-07-22 07:06:32', 1, 1, 0),
(3, 4, 'Baklava', 'Baklava', '<p>This is the ultimate baklava recipe, for the baklava lovers. It is a great alternative for people who doesn&#39;t like walnuts or that are allergic to it</p>\r\n', '<p>This is the ultimate baklava recipe, for the baklava lovers. It is a great alternative for people who doesn&#39;t like walnuts or that are allergic to it</p>\r\n', 0, 1, '54b447ac501a4b5a97f3ed4551a6252f.jpg', 0, 35.5, '', '45', 1, '2020-05-06 08:35:31', '2020-07-22 07:57:30', 1, 1, 0),
(4, 4, 'Halwa', 'Halwa', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>4 cups sugar</li>\r\n	<li>5 cups water</li>\r\n	<li>4 teaspoons lemon juice</li>\r\n	<li>5 tablespoons unsalted butter</li>\r\n	<li>2 tablespoons ground cardamom (+ &frac12; teaspoon (for the butter))</li>\r\n</ul>\r\n', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>4 cups sugar</li>\r\n	<li>5 cups water</li>\r\n	<li>4 teaspoons lemon juice</li>\r\n	<li>5 tablespoons unsalted butter</li>\r\n	<li>2 tablespoons ground cardamom (+ &frac12; teaspoon (for the butter))</li>\r\n</ul>\r\n', 0, 1, 'ed0bd06d15dfe05b32351932fdb37b76.jpg', 0, 45, 'per Dish', '124 Kcal', 1, '2020-05-06 08:15:34', '2020-07-22 07:14:30', 1, 1, 0),
(5, 4, 'Chicken and Rice(Kabsa)', 'Chicken and Rice(Kabsa)', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>2 Tablespoons (36g) ghee/butter or oil.</li>\r\n	<li>1/2 Tablespoon (3g) dry ground coriander.</li>\r\n	<li>1/2 Tablespoon (3g) paprika.</li>\r\n	<li>1/2 teaspoon turmeric.</li>\r\n	<li>1/2 teaspoon ground black pepper.</li>\r\n</ul>\r\n', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>2 Tablespoons (36g) ghee/butter or oil.</li>\r\n	<li>1/2 Tablespoon (3g) dry ground coriander.</li>\r\n	<li>1/2 Tablespoon (3g) paprika.</li>\r\n	<li>1/2 teaspoon turmeric.</li>\r\n	<li>1/2 teaspoon ground black pepper.</li>\r\n</ul>\r\n', 0, 1, '91e335341b45a83b0d7be78d511d1f9a.jpg', 0, 85, 'per Dish', '285 Kcal', 1, '2020-05-06 08:06:41', '2020-07-22 07:31:29', 1, 1, 0),
(6, 2, 'Sauce Pasta With Short Ribs and Turkey Sausage', 'Sauce Pasta With Short Ribs and Turkey Sausage', '<p>Yield: Serves 8 (serving size: 3/4 cup pasta and 3/4 cup meat sauce)</p>\r\n\r\n<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1 tablespoon olive oil</li>\r\n	<li>1 1/2 pounds bone-in beef short ribs</li>\r\n	<li>12 ounces hot Italian turkey sausage (about 3 sausage links)</li>\r\n	<li>2 cups finely chopped yellow onion</li>\r\n	<li>1/2 cup finely chopped celery</li>\r\n</ul>\r\n', '<p>Yield: Serves 8 (serving size: 3/4 cup pasta and 3/4 cup meat sauce)</p>\r\n\r\n<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1 tablespoon olive oil</li>\r\n	<li>1 1/2 pounds bone-in beef short ribs</li>\r\n	<li>12 ounces hot Italian turkey sausage (about 3 sausage links)</li>\r\n	<li>2 cups finely chopped yellow onion</li>\r\n	<li>1/2 cup finely chopped celery</li>\r\n</ul>\r\n', 0, 0, '3429c293ae363e7f47bc8d6a28902b80.jpg', 0, 75.85, 'per Dish', '386', 1, '2020-05-06 08:13:45', '2020-07-22 07:40:28', 1, 1, 0),
(7, 2, 'Pasta and Kale', 'Pasta and Kale', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>12 oz. ziti pasta (or pasta of choice) (we used GF brown rice pasta)</li>\r\n	<li>&frac12; cup finely diced red onion</li>\r\n	<li>2 Tablespoons minced garlic</li>\r\n	<li>6 to 7 sundried tomato halves (2 Tablespoons), finely chopped</li>\r\n	<li>&frac34; cup vegetable broth *</li>\r\n</ul>\r\n', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>12 oz. ziti pasta (or pasta of choice) (we used GF brown rice pasta)</li>\r\n	<li>&frac12; cup finely diced red onion</li>\r\n	<li>2 Tablespoons minced garlic</li>\r\n	<li>6 to 7 sundried tomato halves (2 Tablespoons), finely chopped</li>\r\n	<li>&frac34; cup vegetable broth *</li>\r\n</ul>\r\n', 0, 1, '376b5cd2a9c76e30cb63d3a7108885df.jpg', 0, 59, 'per Dish', '96', 1, '2020-05-06 08:26:47', '2020-07-22 07:11:28', 1, 1, 0),
(8, 2, 'Margherita Pizza', 'Margherita Pizza', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1 Basil, fresh</li>\r\n	<li>1 Pizza peel</li>\r\n	<li>1 large can San marzano tomatoes</li>\r\n	<li>7 3/4 cup Flour, white unbleached</li>\r\n	<li>1 3/4 tbsp Sea salt, fine</li>\r\n</ul>\r\n', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1 Basil, fresh</li>\r\n	<li>1 Pizza peel</li>\r\n	<li>1 large can San marzano tomatoes</li>\r\n	<li>7 3/4 cup Flour, white unbleached</li>\r\n	<li>1 3/4 tbsp Sea salt, fine</li>\r\n</ul>\r\n', 0, 1, 'cab99dccf279a64bd071e3d277d34c48.jpg', 0, 125, 'per Pizza', '145 Kcal', 1, '2020-05-06 08:27:49', '2020-07-22 07:24:27', 1, 1, 0),
(9, 2, 'Classic Pizza Italiano', 'Classic Pizza Italiano', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1/2 lb. bulk Italian sausage (or links with casing removed)</li>\r\n	<li>1 onion, chopped</li>\r\n	<li>1/2 cup diced green pepper (optional)</li>\r\n	<li>1 cup sliced fresh mushrooms</li>\r\n	<li>1 (12-inch) pizza crust or Italian bread shell</li>\r\n</ul>\r\n', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1/2 lb. bulk Italian sausage (or links with casing removed)</li>\r\n	<li>1 onion, chopped</li>\r\n	<li>1/2 cup diced green pepper (optional)</li>\r\n	<li>1 cup sliced fresh mushrooms</li>\r\n	<li>1 (12-inch) pizza crust or Italian bread shell</li>\r\n</ul>\r\n', 0, 0, '0037c7e4da8db194b047ec29ca1bf8bb.jpg', 1, 99, 'per Pizza', '356', 1, '2020-05-06 08:23:51', '2020-07-23 00:37:58', 1, 1, 0),
(10, 3, 'Chow Mein', 'Chow Mein', '<p>Delicious Chow Mein ready in just 15 minutes, easy to put together, all in one pot</p>\r\n\r\n<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1 cup hot water</li>\r\n	<li>1/4 cup soy sauce (low sodium)</li>\r\n	<li>2 tbsp oyster sauce</li>\r\n	<li>2 tbsp Chinese cooking wine</li>\r\n	<li>1 tsp dark soy sauce</li>\r\n</ul>\r\n', '<p>Delicious Chow Mein ready in just 15 minutes, easy to put together, all in one pot</p>\r\n\r\n<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1 cup hot water</li>\r\n	<li>1/4 cup soy sauce (low sodium)</li>\r\n	<li>2 tbsp oyster sauce</li>\r\n	<li>2 tbsp Chinese cooking wine</li>\r\n	<li>1 tsp dark soy sauce</li>\r\n</ul>\r\n', 0, 1, '2f353ca0e5de65964e01a85a5ac24cab.jpg', 0, 49, 'per Bowl', '86', 1, '2020-05-06 08:41:53', '2020-07-22 07:53:25', 1, 1, 0),
(11, 3, 'Spicy Sichuan Noodles', 'Spicy Sichuan Noodles', '<p>Spicy Sichuan Noodles - cold noodles in a spicy, savory and numbing Sichuan sauce.</p>\r\n\r\n<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>8 oz dry noodles, Lo Mein</li>\r\n	<li>2 tablespoons ground peanuts</li>\r\n	<li>1 stalk scallion, cut into small rounds</li>\r\n	<li>5 tablespoons oil</li>\r\n	<li>2 tablespoons dried chili flakes</li>\r\n</ul>\r\n', '<p>Spicy Sichuan Noodles - cold noodles in a spicy, savory and numbing Sichuan sauce.</p>\r\n\r\n<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>8 oz dry noodles, Lo Mein</li>\r\n	<li>2 tablespoons ground peanuts</li>\r\n	<li>1 stalk scallion, cut into small rounds</li>\r\n	<li>5 tablespoons oil</li>\r\n	<li>2 tablespoons dried chili flakes</li>\r\n</ul>\r\n', 0, 1, '809ec7e1306ca4280d7260660bf38c17.jpg', 0, 89, 'per Bowl', '78', 1, '2020-05-06 08:22:55', '2020-07-22 07:14:25', 1, 1, 0),
(12, 3, 'Lo Mein Noodles', 'Lo Mein Noodles', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1.5 tbsp vegetable or peanut oil</li>\r\n	<li>2 garlic cloves (, finely minced (Note 1))</li>\r\n	<li>1/2 onion (, finely sliced)</li>\r\n	<li>300g / 10oz chicken or other protein (, sliced 0.5cm / 1/5&quot; thick (Note 2))</li>\r\n	<li>2 medium carrots (, peeled and cut into 4 x 0.75cm / 1.75 x 1/3&quot; batons)</li>\r\n</ul>\r\n', '<p>Ingredients</p>\r\n\r\n<ul>\r\n	<li>1.5 tbsp vegetable or peanut oil</li>\r\n	<li>2 garlic cloves (, finely minced (Note 1))</li>\r\n	<li>1/2 onion (, finely sliced)</li>\r\n	<li>300g / 10oz chicken or other protein (, sliced 0.5cm / 1/5&quot; thick (Note 2))</li>\r\n	<li>2 medium carrots (, peeled and cut into 4 x 0.75cm / 1.75 x 1/3&quot; batons)</li>\r\n</ul>\r\n', 0, 0, '337ca5ba0a2aee6a6374e122b0201886.jpg', 0, 125, 'per Bowl', '654', 1, '2020-05-06 08:15:58', '2020-07-22 07:26:24', 1, 1, 0);

-- --------------------------------------------------------

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
-- Dumping data for table `product_discounts`
--

INSERT INTO `product_discounts` (`product_discount_id`, `product_id`, `discount`, `discount_type`, `start_date`, `end_date`, `status`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 1, 50, 'flat', '2020-04-21', '2020-05-23', 1, '2020-04-26 10:34:27', '0000-00-00 00:00:00', 1, 0, 0),
(2, 4, 10, 'percentage', '2020-05-09', '2020-05-30', 1, '2020-05-09 00:06:34', '0000-00-00 00:00:00', 1, 0, 0),
(3, 7, 10, 'flat', '2020-07-20', '2020-08-31', 1, '2020-05-09 00:30:34', '2020-07-23 01:20:01', 1, 1, 0);

-- --------------------------------------------------------

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
-- Dumping data for table `product_options`
--

INSERT INTO `product_options` (`product_option_id`, `product_id`, `option_name_en`, `option_name_ar`, `option_desc_en`, `option_desc_ar`, `price`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`, `multiple`) VALUES
(1, 5, 'Chicken and Rice(Kabsa)', 'Chicken and Rice(Kabsa)', 'A flavorful one pot chicken and rice recipe for an easy weeknight dinner.', 'A flavorful one pot chicken and rice recipe for an easy weeknight dinner.', 85, '2020-05-06 08:06:41', '0000-00-00 00:00:00', 1, 0, 0, 0),
(2, 6, 'Sauce Pasta With Short Ribs and Turkey Sausage', 'Sauce Pasta With Short Ribs and Turkey Sausage', 'Yield: Serves 8 (serving size: 3/4 cup pasta and 3/4 cup meat sauce)', 'Yield: Serves 8 (serving size: 3/4 cup pasta and 3/4 cup meat sauce)', 75.85, '2020-05-06 08:13:45', '0000-00-00 00:00:00', 1, 0, 0, 0),
(3, 7, 'Pasta and Kale', 'Pasta and Kale', 'Pasta just screams cozy vibes, and we\'ve got a simple, comforting pasta dish that is sure to please!', 'Pasta just screams cozy vibes, and we\'ve got a simple, comforting pasta dish that is sure to please!', 59, '2020-05-06 08:26:47', '0000-00-00 00:00:00', 1, 0, 0, 0),
(4, 8, 'Margherita Pizza', 'Margherita Pizza', '', '', 125, '2020-05-06 08:27:49', '0000-00-00 00:00:00', 1, 0, 0, 0),
(5, 9, 'Classic Pizza Italiano', 'Classic Pizza Italiano', '', '', 99, '2020-05-06 08:23:51', '0000-00-00 00:00:00', 1, 0, 0, 0),
(6, 10, 'Chow Mein', 'Chow Mein', 'Delicious Chow Mein ready in just 15 minutes, easy to put together, all in one pot', 'Delicious Chow Mein ready in just 15 minutes, easy to put together, all in one pot', 49, '2020-05-06 08:41:53', '0000-00-00 00:00:00', 1, 0, 0, 0),
(7, 11, 'Spicy Sichuan Noodles', 'Spicy Sichuan Noodles', 'Spicy Sichuan Noodles - cold noodles in a spicy, savory and numbing Sichuan sauce. ', 'Spicy Sichuan Noodles - cold noodles in a spicy, savory and numbing Sichuan sauce. ', 89, '2020-05-06 08:22:55', '0000-00-00 00:00:00', 1, 0, 0, 0),
(9, 8, 'Margherita Pizza small size', 'Margherita Pizza small size', 'Margherita Pizza small size', 'Margherita Pizza small size', 100, '2020-05-06 09:17:05', '0000-00-00 00:00:00', 1, 0, 0, 0),
(10, 8, 'Margherita Pizza larg size', 'Margherita Pizza larg size', 'Margherita Pizza larg size', 'Margherita Pizza larg size', 130, '2020-05-06 09:49:05', '0000-00-00 00:00:00', 1, 0, 0, 0),
(11, 9, 'Classic Pizza Italiano small size', 'Classic Pizza Italiano small size', 'Classic Pizza Italiano small size', 'Classic Pizza Italiano small size', 85, '2020-05-06 09:31:06', '0000-00-00 00:00:00', 1, 0, 0, 0),
(12, 9, 'Classic Pizza Italiano larg size', 'Classic Pizza Italiano larg size', 'Classic Pizza Italiano larg size', 'Classic Pizza Italiano larg size', 85, '2020-05-06 09:03:07', '0000-00-00 00:00:00', 1, 0, 0, 0),
(13, 4, 'Halwa', 'Halwa', 'Halwa', 'Halwa', 45, '2020-05-06 09:57:07', '0000-00-00 00:00:00', 1, 0, 0, 0),
(14, 12, 'test', 'test', '', '', 4, '2020-07-16 13:54:57', '0000-00-00 00:00:00', 1, 0, 0, 0),
(15, 12, 'test', 'req', '', '', 10, '2020-07-17 07:10:59', '0000-00-00 00:00:00', 1, 0, 0, 1),
(16, 12, 'rt34t24', 'rgtre', '', '', 75, '2020-07-17 07:32:59', '0000-00-00 00:00:00', 1, 0, 0, 1),
(18, 8, 'Extra Cheese', 'Extra Cheese', '', '', 5, '2020-07-22 08:42:42', '0000-00-00 00:00:00', 1, 0, 0, 1);

-- --------------------------------------------------------

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
(1, 1, 'admin@gmail.com', 'e10adc3949ba59abbe56e057f20f883e', 'Admin', 'User', '1111111111', 'ed9d000b7f21720f449a69b6922dabbd_DS LOGO 2.jpg', 'wayweb', 'W0001', '', '', 0, 0, '4ab8f3960ea51263b6daf54555cbda44', 1, 0, '0000-00-00 00:00:00', '2020-07-19 12:54:00', 0, 1, 0),
(2, 2, 'contact@dsinfoway.com', 'e10adc3949ba59abbe56e057f20f883e', 'Demo', 'User', '9999999999', '51d08cfdd26bfbfdce17fafd3c0a5cb5.jpg', '', '', '78d5ca1e-7a7d-4dec-8563-b43921748768', '', 1, 1, '', 0, 0, '0000-00-00 00:00:00', '2020-08-15 11:57:33', 0, 0, 0);

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
-- Dumping data for table `user_address`
--

INSERT INTO `user_address` (`user_address_id`, `user_id`, `address_line1`, `postal_code`, `address_line2`, `city`, `latitude`, `longitude`, `is_active`, `created_at`, `modified_at`, `created_by`, `modified_by`, `draft`) VALUES
(1, 2, '1', '363330', '', '', 0, 0, 0, '0000-00-00 00:00:00', '0000-00-00 00:00:00', 0, 0, 0),
(5, 2, 'Unnamed Road, Paras Society, Gokul Nagar, Una, Gujarat 362560, India', '362560', '', 'Una', 20.815438541650245, 71.04462068527937, 0, '2020-05-08 06:24:56', '0000-00-00 00:00:00', 0, 0, 0),
(6, 2, 'Unnamed Road, Alka Nagri Society, Paras Society, Gokul Nagar, Una, Gujarat 362560, India', '362560', '', 'Una', 20.816272477157675, 71.04533784091473, 0, '2020-05-08 07:12:31', '0000-00-00 00:00:00', 0, 0, 0),
(7, 2, 'Unnamed Road, Paras Society, Gokul Nagar, Una, Gujarat 362560, India', '362560', '', 'Una', 20.81538087739348, 71.0458555072546, 0, '2020-05-08 10:16:12', '0000-00-00 00:00:00', 0, 0, 0),
(8, 2, 'laxminagar, Gujarat State Highway 98, Paras Society, Gokul Nagar, Paras Society, Gokul Nagar, Una, Gujarat 362560, India', '362560', '', 'Una', 20.815426632729455, 71.04423981159925, 0, '2020-05-08 10:47:12', '0000-00-00 00:00:00', 0, 0, 0),
(9, 2, 'GJ SH 104, Paras Society, Gokul Nagar, Una, Gujarat 362560, India', '362560', '', 'Una', 20.814794831793254, 71.0433104261756, 0, '2020-05-08 10:14:14', '0000-00-00 00:00:00', 0, 0, 0),
(10, 2, 'Una-Delwada road, PARIMAL SOCIETY, Diu-Una Rd, Srinagar, Una, Gujarat 362560, India', '362560', '', 'Una', 20.813735243027647, 71.04294095188378, 0, '2020-05-08 10:34:14', '0000-00-00 00:00:00', 0, 0, 0);

-- --------------------------------------------------------

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
