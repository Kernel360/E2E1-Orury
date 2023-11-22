-- 기존 remark2, 3 삭제
alter table user drop column remark2;
alter table user drop column remark3;

-- remark1 -> remark 변경
alter table user change remark1 remark varchar(255);
