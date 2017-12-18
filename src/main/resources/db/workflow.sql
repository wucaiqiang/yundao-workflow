
delimiter //

DROP PROCEDURE if exists tenant_sql_update_1_1;

create procedure tenant_sql_update_1_1()
begin

 update act_hi_taskinst set CATEGORY_ = 'audit' where CATEGORY_ is null  and TASK_DEF_KEY_ like '%audit%';
 update act_hi_taskinst set CATEGORY_ = 'reject_edit' where CATEGORY_ is null  and TASK_DEF_KEY_ like '%reject_edit%';
  
 update act_ru_task set CATEGORY_ = 'audit' where CATEGORY_ is null  and TASK_DEF_KEY_ like '%audit%';
 update act_ru_task set CATEGORY_ = 'reject_edit' where CATEGORY_ is null  and TASK_DEF_KEY_ like '%reject_edit%';
 
end;

call tenant_sql_update_1_1();

DROP PROCEDURE if exists tenant_sql_update_1_1;
//