create table delete_file(
board_file varchar2(50),
reg_date date default sysdate
);  

drop trigger save_delete_file;

create or replace trigger save_delete_file
after update or delete
on board3
 for each row
 begin
   if(:old.board_file is not null) then
    if(:old.board_file != :new.board_file or :new.board_file is null ) then
     insert into delete_file
      (board_file)
     values(:old.board_file);
     end if;
   end if;
 end;
/






