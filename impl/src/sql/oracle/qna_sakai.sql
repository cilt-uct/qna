CREATE TABLE   qna_ANSWERS  (
   id                  varchar2(255)   NOT NULL,
   question_id         varchar2(255)   NOT NULL,
   ownerId             varchar2(255)   NOT NULL,
   answerText          long            NOT NULL,
   lastModifierId      varchar2(255)   NOT NULL,
   dateLastModified    timestamp(6)    default NULL,
   dateCreated         timestamp(6)    NOT NULL,
   approved            char(1)         NOT NULL,
   privateReply        char(1)         NOT NULL,
   anonymous           char(1)         NOT NULL,
  PRIMARY KEY  ( id )
);

Create index FK63487CFAB9F9349F on qna_ANSWERS ( question_id );

CREATE TABLE   qna_CATEGORIES  (
   id                  varchar2(255)   NOT NULL,
   ownerId             varchar2(255)   NOT NULL,
   location            varchar2(255)   NOT NULL,
   categoryText        varchar2(255)   NOT NULL,
   dateLastModified    timestamp(6)    NOT NULL,
   dateCreated         timestamp(6)    NOT NULL,
   sortOrder           integer         default NULL,
   hidden              char(1)         default NULL,
  PRIMARY KEY  ( id )
);

CREATE TABLE   qna_CUSTOM_EMAIL_LIST  (
   id                  varchar2(255)   NOT NULL,
   options_id          varchar2(255)   NOT NULL,
   ownerId             varchar2(255)   NOT NULL,
   email               varchar2(255)   NOT NULL,
   dateCreated         timestamp(6)    default NULL,
  PRIMARY KEY  ( id )
) ;
create index FKD08CBF742EDEC7B5 on qna_CUSTOM_EMAIL_LIST  ( options_id );

CREATE TABLE   qna_OPTIONS  (
   id                     varchar2(255) NOT NULL,
   ownerId                varchar2(255) NOT NULL,
   location               varchar2(255) NOT NULL,
   dateLastModified       timestamp(6)  default NULL,
   anonymousAllowed       char(1)       default NULL,
   moderated              char(1)       default NULL,
   emailNotification      char(1)       default NULL,
   emailNotificationType  varchar2(255) default NULL,
   defaultStudentView     varchar2(255) default NULL,
  PRIMARY KEY  ( id )
);

CREATE TABLE  qna_QUESTIONS (
  id                    varchar2(255)   NOT NULL,
  category_id           varchar2(255)   default NULL,
  ownerId               varchar2(255)   NOT NULL,
   lastModifierId       varchar2(255)   NOT NULL,
   location             varchar2(255)   NOT NULL,
   questionText         long            NOT NULL,
   views                number(11)      NOT NULL,
   dateLastModified     timestamp(6)    NOT NULL,
   dateCreated          timestamp(6)    NOT NULL,
   sortOrder            number(11)      NOT NULL,
   anonymous            char(1)         NOT NULL,
   published            char(1)         NOT NULL,
   notify               char(1)         NOT NULL,
   contentCollection    varchar2(255)   default NULL,
   hidden               char(1)         default NULL,
  PRIMARY KEY  ( id )
) ;
create index FKE3F0F49224A87D9F on qna_questions ( category_id );