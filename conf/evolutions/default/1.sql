# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table student (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_student primary key (id))
;

create sequence student_seq;




# --- !Downs

drop table if exists student cascade;

drop sequence if exists student_seq;

