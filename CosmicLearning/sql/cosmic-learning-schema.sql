drop database if exists cosmic_learning;
create database cosmic_learning;
use cosmic_learning;

create table level (
level_id int primary key auto_increment,
level int not null,
amount_points int not null
);

create table app_user (
app_user_id int primary key auto_increment,
username varchar(50) not null,
password_hash varchar(2048) not null,
enabled bit not null default(1)
);

create table app_role (
app_role_id int primary key auto_increment,
name varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
	constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table user (
user_id int primary key auto_increment,
first_name varchar(150) not null,
last_name varchar(150) not null,
age int not null,
title varchar(5),
university varchar(150) not null,
email varchar(150) not null,
amount int not null,
app_user_id int not null,
constraint fk_user_app_user_id
         foreign key (app_user_id)
         references app_user(app_user_id)
);

create table subject (
subject_id int primary key auto_increment,
name varchar(150) not null,
description varchar(1000) not null
);

create table question (
question_id int primary key auto_increment,
question varchar(5000) not null,
hint varchar(2000) not null,
user_id int not null,
subject_id int not null,
constraint fk_question_user_id
          foreign key (user_id)
          references user(user_id),
constraint fk_question_subject_id
          foreign key (subject_id)
          references subject(subject_id)
);

create table review (
review_id int primary key auto_increment,
description varchar(2000) not null,
stars int not null,
review_date date not null,
user_id int not null,
constraint fk_review_user_id
           foreign key (user_id)
           references user(user_id)
);

create table answer_options (
answer_option_id int primary key auto_increment,
option_answer varchar(500) not null,
correct boolean not null default 0,
question_id int not null,
constraint fk_answer_options_question_id
           foreign key (question_id)
           references question(question_id)
);

create table response (
question_id int not null,
user_id int not null,
answer_option_id int not null,
constraint pk_app_question_user_answer_option
        primary key (question_id, user_id, answer_option_id),
constraint fk_response_question_id
           foreign key (question_id)
           references question(question_id),
constraint fk_response_user_id
           foreign key (user_id)
           references user(user_id),
constraint fk_response_answer_option_id
           foreign key (answer_option_id)
           references answer_options(answer_option_id)
);
