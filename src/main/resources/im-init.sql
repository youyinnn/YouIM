CREATE TABLE IF NOT EXISTS `group_ship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(18) DEFAULT NULL,
  `owner_user_id` varchar(18) DEFAULT NULL,
  `administrator_ids` varchar(255) DEFAULT NULL,
  `group_member_ids` varchar(3600) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `friend_ship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(18) DEFAULT NULL,
  `friend_list` varchar(3600) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;