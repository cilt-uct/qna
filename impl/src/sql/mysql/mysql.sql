CREATE TABLE  `unisa`.`qna_ANSWERS` (
  `id` varchar(255) NOT NULL,
  `question_id` varchar(255) NOT NULL,
  `ownerId` varchar(255) NOT NULL,
  `answerText` text NOT NULL,
  `lastModifierId` varchar(255) NOT NULL,
  `dateLastModified` datetime default NULL,
  `dateCreated` datetime NOT NULL,
  `approved` bit(1) NOT NULL,
  `privateReply` bit(1) NOT NULL,
  `anonymous` bit(1) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `FK63487CFAB9F9349F` (`question_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8

CREATE TABLE  `unisa`.`qna_CATEGORIES` (
  `id` varchar(255) NOT NULL,
  `ownerId` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `categoryText` varchar(255) NOT NULL,
  `dateLastModified` datetime NOT NULL,
  `dateCreated` datetime NOT NULL,
  `sortOrder` int(11) default NULL,
  `hidden` bit(1) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8

CREATE TABLE  `unisa`.`qna_CUSTOM_EMAIL_LIST` (
  `id` varchar(255) NOT NULL,
  `options_id` varchar(255) NOT NULL,
  `ownerId` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `dateCreated` datetime default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKD08CBF742EDEC7B5` (`options_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8

CREATE TABLE  `unisa`.`qna_OPTIONS` (
  `id` varchar(255) NOT NULL,
  `ownerId` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `dateLastModified` datetime default NULL,
  `anonymousAllowed` bit(1) default NULL,
  `moderated` bit(1) default NULL,
  `emailNotification` bit(1) default NULL,
  `emailNotificationType` varchar(255) default NULL,
  `defaultStudentView` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8

CREATE TABLE  `unisa`.`qna_QUESTIONS` (
  `id` varchar(255) NOT NULL,
  `category_id` varchar(255) default NULL,
  `ownerId` varchar(255) NOT NULL,
  `lastModifierId` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `questionText` text NOT NULL,
  `views` int(11) NOT NULL,
  `dateLastModified` datetime NOT NULL,
  `dateCreated` datetime NOT NULL,
  `sortOrder` int(11) NOT NULL,
  `anonymous` bit(1) NOT NULL,
  `published` bit(1) NOT NULL,
  `notify` bit(1) NOT NULL,
  `contentCollection` varchar(255) default NULL,
  `hidden` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `FKE3F0F49224A87D9F` (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8