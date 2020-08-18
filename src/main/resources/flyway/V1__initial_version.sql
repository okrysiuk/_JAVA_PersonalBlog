create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );
insert into hibernate_sequence values ( 1 );
create table note (id bigint not null, filename varchar(255), tag varchar(255), text varchar(32768) not null , title varchar(255), user_id bigint, primary key (id));
create table user_role (user_id bigint not null, roles varchar(255));
create table usr (id bigint not null, activation_code varchar(255), active bit not null, email varchar(255), password varchar(255) not null , username varchar(255) not null , primary key (id));
alter table note add constraint note_user_fk foreign key (user_id) references usr (id);
alter table user_role add constraint user_role_user_fk foreign key (user_id) references usr (id);