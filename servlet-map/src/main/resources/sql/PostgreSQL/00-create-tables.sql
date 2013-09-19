-- NOTE!;
-- THE FILE IS TOKENIZED WITH SEMICOLON CHARACTER!;
-- EACH COMMENT _NEED_ TO END WITH A SEMICOLON OR OTHERWISE THE NEXT ACTUAL SQL IS NOT RUN!;
-- ----------------------------------------------------------------------------------------;

--DROP RULE IF EXISTS set_uuid ON portti_view;
--DROP RULE IF EXISTS set_default_user ON portti_view_supplement;

DROP TABLE portti_view_supplement IF EXISTS;
DROP TABLE portti_view IF EXISTS;
DROP TABLE portti_bundle IF EXISTS;
DROP TABLE portti_view_bundle_seq IF EXISTS;




CREATE TABLE portti_view_supplement (
   id               IDENTITY	  PRIMARY KEY,
   app_startup      VARCHAR(512),
   baseaddress      VARCHAR(512),
   creator          BIGINT        DEFAULT -1,
   pubdomain        VARCHAR(512)  DEFAULT '',
   lang             VARCHAR(2)    DEFAULT 'en',
   width            INTEGER       DEFAULT 0,
   height           INTEGER       DEFAULT 0,
   is_public        BOOLEAN       DEFAULT FALSE,
   old_id	    BIGINT	  DEFAULT -1
);


CREATE TABLE portti_view (
   uuid             VARCHAR(128),
   id               int GENERATED BY DEFAULT AS IDENTITY (START WITH 1),
   name             VARCHAR(128)  NOT NULL,
   supplement_id    BIGINT        ,
   is_default       BOOLEAN       DEFAULT FALSE,
   type		    varchar(16)	  DEFAULT 'USER',
   description   VARCHAR(2000) ,
   page character varying(128) DEFAULT 'index',
   application character varying(128) DEFAULT 'servlet',
   application_dev_prefix character varying(256) DEFAULT '/applications/sample'
);



CREATE TABLE portti_bundle (
   id    	    IDENTITY	  PRIMARY KEY,
   name 	    VARCHAR(128)  NOT NULL,
   config 	    character varying(20000) DEFAULT '{}',
   state 	    character varying(20000) DEFAULT '{}',
   startup 	    character varying(20000) 	  NOT NULL
);



CREATE TABLE portti_view_bundle_seq (
   view_id 	    BIGINT	  NOT NULL,
   bundle_id 	    BIGINT 	   NOT NULL,
   seqno 	    INTEGER 	  NOT NULL,
   config 	    character varying(20000) DEFAULT '{}',
   state 	    character varying(20000) DEFAULT '{}',
   startup 	    character varying(20000),
   bundleinstance character varying(128) DEFAULT '',
   CONSTRAINT 	    view_seq	  UNIQUE (view_id, seqno)
);
