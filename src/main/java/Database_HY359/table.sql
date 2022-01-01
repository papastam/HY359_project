create table MEMBER
(
    ID INTEGER not null generated always as identity (start with 1, increment by 1),

    USERNAME VARCHAR(30) not null unique,	
    EMAIL VARCHAR(30) not null unique,	
    PASSWORD VARCHAR(32) not null,
    FIRSTNAME VARCHAR(20) not null,
    LASTNAME VARCHAR(30) not null,
    BIRTHDATE DATE not null,
    TEL_MOBILE VARCHAR(14) not null unique,

    TEL_HOME VARCHAR(14),
    GENDER  VARCHAR (7) CONSTRAINT GENDER_CK CHECK (GENDER IN ('Male','Female','Unknown')),	
    COUNTRY VARCHAR(30) not null,
    CITY VARCHAR(50) not null,
    JOB VARCHAR(50),
    ADDRESS VARCHAR(50),
	
    PHOTO_PATH VARCHAR(100),
    INFO VARCHAR(500),

    FACEBOOK VARCHAR(50),
    SKYPE VARCHAR(50),
    GMAIL VARCHAR(50),

    primary key (ID)	 
)
