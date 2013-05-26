# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table my_event (
  eid                       bigint not null,
  name                      varchar(255),
  creator                   varchar(255),
  location                  varchar(255),
  venue                     varchar(255),
  start_time                timestamp,
  end_time                  timestamp,
  description               TEXT,
  constraint pk_my_event primary key (eid))
;

create table student (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_student primary key (id))
;

create sequence my_event_seq;

create sequence student_seq;




# --- !Downs

drop table if exists my_event cascade;

drop table if exists student cascade;

drop sequence if exists my_event_seq;

drop sequence if exists student_seq;

