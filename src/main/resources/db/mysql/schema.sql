CREATE DATABASE IF NOT EXISTS mpgtracker;

ALTER DATABASE mpgtracker
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON mpgtracker.* TO mpgtracker@localhost IDENTIFIED BY 'mpgtracker';

USE mpgtracker;

CREATE TABLE `users` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(30) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `usertype` varchar(30) NOT NULL,
  `lastLoginDt` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8

CREATE TABLE `session` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `userid` int(4) unsigned NOT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ipAddress` varchar(255) DEFAULT '',
  `state` varchar(255) DEFAULT 'LOGGEDOUT',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `session_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `vehicles` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `make` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `year` int(4) DEFAULT NULL,
  `purchased` timestamp NULL DEFAULT NULL,
  `sortkey` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8

CREATE TABLE `mileages` (
  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,
  `mileage` int(9) NOT NULL,
  `gallons` decimal(6,3) DEFAULT NULL,
  `totalCost` decimal(5,2) NOT NULL,
  `timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `vid` int(4) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vid` (`vid`),
  CONSTRAINT `mileages_ibfk_1` FOREIGN KEY (`vid`) REFERENCES `vehicles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=486 DEFAULT CHARSET=utf8