create table persistent_logins(
 username varchar(64) not null,
 series   varchar(64) primary key, --기기, 브라우저별 쿠키를 구분할 고유 값
 token    varchar(64) not null,--브라우저가 가지고 있는 쿠키의 값을 검증할 인증값
 last_used timestamp not null--가장 최신 자동 로그인 시간
);






create table WATER_INTAKE(
wi_no			 NUMBER primary key,
user_id			 VARCHAR2(60) 
title		 	 VARCHAR2(10),
time_start		 varchar2(10)
);

insert into water_intake
values(4, 'user01','100','2021-10-13');

insert into water_intake
values(5, 'user01','200','2021-10-13');

 insert into PERSONAL_MANAGEMENT values(
    4,'user01','1000', '2021-10-13');


select * from water_intake;
 WI_NO USER_ID TITLE TIME_START
 ----- ------- ----- ----------
     1 user01  1     2021-10-14
     2 user01  1     2021-10-14
     3 user01  1     2021-10-14
     4 user01  100   2021-10-13
     5 user01  200   2021-10-13
====================================================
create table PERSONAL_MANAGEMENT(
PM_NO			 NUMBER primary key,
USER_ID			 VARCHAR2(60) 
PM_KCAL			 VARCHAR2(20),
PM_DATE			 DATE
);


select * from personal_management;
 PM_NO USER_ID PM_KCAL PM_DATE
 ----- ------- ------- ---------------------
     1 user01  100     2021-10-14 15:44:49.0
     2 user01  200     2021-10-14 15:45:02.0
     3 user01  1000    2021-10-14 16:43:40.0
     4 user01  1000    2021-10-13 00:00:00.0

====================================================
���� : ��¥ ��, title�� �հ�  pm_kal�� ���� ���մϴ�.
select title,time_start,pm_kcal 
from (select sum(title) title, time_start
      from water_intake
      where user_id = 'user01'
      group by time_start), 
      (select sum(pm_kcal) pm_kcal  , to_char(PM_DATE, 'yyyy-mm-dd') as pm_date
       from personal_management 
       where user_id = 'user01'
       group by to_char(PM_DATE, 'yyyy-mm-dd')
       )
where  PM_DATE = time_start  

       
 TITLE TIME_START PM_KCAL
 ----- ---------- -------
     3 2021-10-14    1300
   300 2021-10-13    1000

