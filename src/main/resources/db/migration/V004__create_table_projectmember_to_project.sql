create table projectmember_to_project (
    id serial primary key,
    project_name varchar(100) not null,
    projectmember_name varchar(100) not null,
    task_name varchar(100) not null,
    description varchar(500),
    status varchar(100)
);