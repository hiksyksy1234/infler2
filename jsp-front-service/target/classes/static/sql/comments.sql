drop table comments3 CASCADE CONSTRAINTS;

 create table comments3(
  num          number       primary key,
  id           varchar2(30) references member3(id),
  content      varchar2(200),
  reg_date     date,
  board_num    number references board3(board_num) 
               on delete cascade 
  );  
  
  drop sequence com_seq3;
create sequence com_seq3;









