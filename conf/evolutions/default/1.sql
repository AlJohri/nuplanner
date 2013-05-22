# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table event (
  id                        bigint not null,
  name                      varchar(255),
  creator                   varchar(255),
  location                  varchar(255),
  venue                     varchar(255),
  start_time                timestamp,
  end_time                  timestamp,
  description               TEXT,
  constraint pk_event primary key (id))
;

create table student (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_student primary key (id))
;

create sequence event_seq;

create sequence student_seq;




# --- !Downs

drop table if exists event cascade;

drop table if exists student cascade;

drop sequence if exists event_seq;

drop sequence if exists student_seq;

