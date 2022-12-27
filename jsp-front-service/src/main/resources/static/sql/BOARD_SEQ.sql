drop table board11
select * from tab;

CREATE TABLE BOARD11(
	BOARD_NUM NUMBER,
	BOARD_NAME VARCHAR2(20),
	BOARD_PASS VARCHAR2(15),
	BOARD_SUBJECT VARCHAR2(50),
	BOARD_CONTENT VARCHAR2(2000),
	BOARD_FILE VARCHAR2(50),
	BOARD_RE_REF NUMBER,
	BOARD_RE_LEV NUMBER,
	BOARD_RE_SEQ NUMBER,
	BOARD_READCOUNT NUMBER,
	BOARD_DATE DATE,
	PRIMARY KEY(BOARD_NUM)
);

create sequence board11_seq
start with 1
increment by 1
nocache;

drop sequence board11_seq

drop table board11;
select * from tab;
select * from board11;
delete  from board11;



insert into board11
(BOARD_NUM,BOARD_NAME,BOARD_PASS,BOARD_SUBJECT,
BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF,
BOARD_RE_LEV,BOARD_RE_SEQ,BOARD_READCOUNT,
BOARD_DATE) 
values(board11_seq.nextval,'123','1','하하','0','',
board11_seq.nextval,0,0,0,sysdate);

select board11_seq.nextval from dual



grant connect,resource,unlimited tablespace to scott identified by tiger;
alter user scott default tablespace users;
alter user scott temporary tablespace temp;
connect scott/tiger
