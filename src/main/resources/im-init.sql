CREATE TABLE IF NOT EXISTS `group_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(18) DEFAULT NULL,
  `owner_id` varchar(18) DEFAULT NULL,
  `administrator_ids` varchar(255) DEFAULT '[]',
  `group_member_ids` varchar(3600) DEFAULT '[]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `user_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(18) DEFAULT NULL,
  `friend_set` varchar(3600) DEFAULT '[]',
  `group_set` varchar(3600) DEFAULT '[]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
