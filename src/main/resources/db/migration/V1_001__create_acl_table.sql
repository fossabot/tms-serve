--
-- Table structure for table role_tbl
--
create table role_tbl
(
    id           bigint primary key generated always as identity,
    role_name    varchar(128) not null,
    role_code    varchar(16)  not null,
    description  varchar(2000),
    updated_by   varchar(64),
    created_by   varchar(64),
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);

--
-- Table structure for table user_tbl
--
create table user_tbl
(
    id           bigint primary key generated always as identity,
    user_name    varchar(64)  not null,
    password     varchar(256) not null,
    full_name    varchar(256) not null,
    sex          smallint     not null,
    birth_date   date         not null,
    avatar       varchar(500) not null,
    email        varchar(45)  not null,
    phone        varchar(45)  not null,
    disable_flag boolean      not null default false,
    updated_by   varchar(64),
    created_by   varchar(64),
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean               default false
);

--
-- Table structure for table permission_tbl
--
create table permission_tbl
(
    id                   bigint primary key generated always as identity,
    permission_name      varchar(1024) not null,
    parent_id            bigint,
    url                  varchar(255),
    component            varchar(255),
    component_name       varchar(100),
    redirect             varchar(255),
    menu_type            smallint,
    perms                varchar(255),
    perms_type           varchar(10) default '0',
    sort_no              numeric(8, 2),
    always_show          boolean     default true,
    icon                 varchar(100),
    is_route             boolean     default true,
    is_leaf              boolean     default false,
    keep_alive           boolean     default true,
    hidden               boolean     default false,
    description          varchar(255),
    rule_flag            boolean     default false,
    status               boolean     default true,
    internal_or_external boolean     default true,
    updated_by           varchar(64),
    created_by           varchar(64),
    updated_date         timestamp,
    created_date         timestamp,
    deleted_flag         boolean     default false
);

--
-- Table structure for table permission_role_tbl
--
create table permission_role_tbl
(
    id            bigint primary key generated always as identity,
    role_id       bigint not null,
    permission_id bigint not null,
    updated_by    varchar(64),
    created_by    varchar(64),
    updated_date  timestamp,
    created_date  timestamp,
    deleted_flag  boolean default false,
    foreign key (role_id) references role_tbl (id),
    foreign key (permission_id) references permission_tbl (id)
);

--
-- Table structure for table user_role_tbl
--
create table user_role_tbl
(
    id           bigint primary key generated always as identity,
    role_id      bigint not null,
    user_id      bigint not null,
    updated_by   varchar(64),
    created_by   varchar(64),
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (role_id) references role_tbl (id),
    foreign key (user_id) references user_tbl (id)
);

--
-- Table structure for table `qrtz_job_details`
--
create table qrtz_job_details
(
    sched_name        varchar(120) not null,
    job_name          varchar(190) not null,
    job_group         varchar(190) not null,
    description       varchar(250) null,
    job_class_name    varchar(250) not null,
    is_durable        boolean      not null,
    is_nonconcurrent  boolean      not null,
    is_update_data    boolean      not null,
    requests_recovery boolean      not null,
    job_data          bytea        null,
    primary key (sched_name, job_name, job_group)
);

--
-- Table structure for table `qrtz_triggers`
--
create table qrtz_triggers
(
    sched_name     varchar(120) not null,
    trigger_name   varchar(190) not null,
    trigger_group  varchar(190) not null,
    job_name       varchar(190) not null,
    job_group      varchar(190) not null,
    description    varchar(250) null,
    next_fire_time bigint       null,
    prev_fire_time bigint       null,
    priority       integer      null,
    trigger_state  varchar(16)  not null,
    trigger_type   varchar(8)   not null,
    start_time     bigint       not null,
    end_time       bigint       null,
    calendar_name  varchar(190) null,
    misfire_instr  smallint     null,
    job_data       bytea        null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, job_name, job_group)
        references qrtz_job_details (sched_name, job_name, job_group)
);

--
-- Table structure for table `qrtz_simple_triggers`
--
create table qrtz_simple_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(190) not null,
    trigger_group   varchar(190) not null,
    repeat_count    bigint       not null,
    repeat_interval bigint       not null,
    times_triggered bigint       not null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_cron_triggers`
--
create table qrtz_cron_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(190) not null,
    trigger_group   varchar(190) not null,
    cron_expression varchar(120) not null,
    time_zone_id    varchar(80),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_simprop_triggers`
--
create table qrtz_simprop_triggers
(
    sched_name    varchar(120)   not null,
    trigger_name  varchar(190)   not null,
    trigger_group varchar(190)   not null,
    str_prop_1    varchar(512)   null,
    str_prop_2    varchar(512)   null,
    str_prop_3    varchar(512)   null,
    int_prop_1    int            null,
    int_prop_2    int            null,
    long_prop_1   bigint         null,
    long_prop_2   bigint         null,
    dec_prop_1    numeric(13, 4) null,
    dec_prop_2    numeric(13, 4) null,
    bool_prop_1   boolean        null,
    bool_prop_2   boolean        null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_blob_triggers`
--
create table qrtz_blob_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(190) not null,
    trigger_group varchar(190) not null,
    blob_data     bytea        null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_calendars`
--
create table qrtz_calendars
(
    sched_name    varchar(120) not null,
    calendar_name varchar(190) not null,
    calendar      bytea        not null,
    primary key (sched_name, calendar_name)
);

--
-- Table structure for table `qrtz_paused_trigger_grps`
--
create table qrtz_paused_trigger_grps
(
    sched_name    varchar(120) not null,
    trigger_group varchar(190) not null,
    primary key (sched_name, trigger_group)
);

--
-- Table structure for table `qrtz_fired_triggers`
--
create table qrtz_fired_triggers
(
    sched_name        varchar(120) not null,
    entry_id          varchar(95)  not null,
    trigger_name      varchar(190) not null,
    trigger_group     varchar(190) not null,
    instance_name     varchar(190) not null,
    fired_time        bigint       not null,
    sched_time        bigint       not null,
    priority          integer      not null,
    state             varchar(16)  not null,
    job_name          varchar(190) null,
    job_group         varchar(190) null,
    is_nonconcurrent  boolean      null,
    requests_recovery boolean      null,
    primary key (sched_name, entry_id)
);

--
-- Table structure for table `qrtz_scheduler_state`
--
create table qrtz_scheduler_state
(
    sched_name        varchar(120) not null,
    instance_name     varchar(190) not null,
    last_checkin_time bigint       not null,
    checkin_interval  bigint       not null,
    primary key (sched_name, instance_name)
);

--
-- Table structure for table `qrtz_locks`
--
create table qrtz_locks
(
    sched_name varchar(120) not null,
    lock_name  varchar(40)  not null,
    primary key (sched_name, lock_name)
);

create index idx_qrtz_j_req_recovery on qrtz_job_details (sched_name, requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details (sched_name, job_group);

create index idx_qrtz_t_j on qrtz_triggers (sched_name, job_name, job_group);
create index idx_qrtz_t_jg on qrtz_triggers (sched_name, job_group);
create index idx_qrtz_t_c on qrtz_triggers (sched_name, calendar_name);
create index idx_qrtz_t_g on qrtz_triggers (sched_name, trigger_group);
create index idx_qrtz_t_state on qrtz_triggers (sched_name, trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers (sched_name, trigger_name, trigger_group, trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers (sched_name, trigger_group, trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers (sched_name, next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers (sched_name, trigger_state, next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers (sched_name, misfire_instr, next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_group,
                                                             trigger_state);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers (sched_name, instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers (sched_name, instance_name, requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers (sched_name, job_name, job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers (sched_name, job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers (sched_name, trigger_name, trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers (sched_name, trigger_group);


--
-- Insert data to table `role_tbl`
--
insert into role_tbl (role_name, role_code, description, updated_by, created_by, updated_date, created_date,
                      deleted_flag)
values ('Super Administrator', 'admin', 'Super Administrator. Have access to view all pages.', 'admin', 'admin',
        '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `permission_tbl`
--
insert into permission_tbl (permission_name, parent_id, url, component, component_name, redirect, menu_type, perms,
                            perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description,
                            rule_flag, status, internal_or_external, updated_by, created_by, updated_date, created_date,
                            deleted_flag)
VALUES ('Home', null, '/dashboard/analysis', 'dashboard/Analysis', null, null, 0, null, null, 0.00, false, 'home', true,
        true, null, false, null, false, null, null, 'admin', null, '2019-03-29 11:04:13.000000',
        '2018-12-25 20:34:38.000000', false),
       ('System management', null, '/system', 'layouts/RouteView', null, null, 0, null, null, 1.00, false, 'setting',
        true, false, null, false, null, false, null, null, 'admin', null, '2019-03-31 22:19:52.000000',
        '2018-12-25 20:34:38.000000', false),
       ('Menu management', 2, '/system/menu-setting', 'system/NewPermissionList', null, null, 1, null, '1', 1.00,
        false, null, true, true, false, false, null, false, true, false, 'admin', 'admin', '2019-09-08 15:02:57.000000',
        '2019-09-08 15:00:05.000000', false),
       ('Role management', 2, '/system/role-setting', 'system/RoleList', null, null, 1, null, '1', 2.00, false, null, true,
        true, false, false, null, false, true, false, null, null, null, '2018-12-25 20:34:38.000000', false),
       ('Job management', 2, '/isystem/position', 'system/SysPositionList', null, null, 1, null, '1', 3.00, false, null,
        true, true, false, false, null, false, true, false, 'admin', 'admin', '2019-09-19 10:15:22.000000',
        '2019-09-19 10:14:13.000000', false),
       ('Address book', 2, '/isystem/addressList', 'system/AddressList', null, null, 1, null, '1', 4.00, false, null,
        true, true, false, false, null, false, true, false, null, 'admin', null, '2019-09-19 15:45:21.000000', false),
       ('Role maintenance', 2, '/isystem/roleUserList', 'system/RoleUserList', null, null, 1, null, null, 5.00, false,
        null, true, true, null, false, null, false, null, null, null, 'admin', null, '2019-04-17 15:13:56.000000',
        false),
       ('Timed task', 2, '/isystem/QuartzJobList', 'system/QuartzJobList', null, null, 1, null, null, 6.00, false, null,
        true, true, null, false, null, false, null, null, 'admin', null, '2019-04-02 10:24:13.000000',
        '2019-01-03 09:38:52.000000', false),
       ('Log', 2, '/isystem/log', 'system/LogList', null, null, 1, null, null, 7.00, false, null, true, true, null,
        false, null, false, null, null, 'admin', null, '2019-04-02 11:38:17.000000', '2018-12-26 10:11:18.000000',
        false),
       ('Depart', 2, '/isystem/depart', 'system/DepartList', null, null, 1, null, null, 8.00, false, null, true, true,
        null, false, null, false, null, null, 'admin', 'admin', '2019-03-07 19:23:16.000000',
        '2019-01-29 18:47:40.000000', false),
       ('Depart-User', 2, '/isystem/departUserList', 'system/DepartUserList', null, null, 1, null, null, 9.00, false,
        null, true, true, null, false, null, false, null, null, null, 'admin', null, '2019-04-17 15:12:24.000000',
        false),
       ('User management', 2, '/isystem/user', 'system/UserList', null, null, 1, null, null, 10.00, false, null, true,
        false, null, false, null, false, null, null, 'admin', null, '2019-03-16 11:20:33.000000',
        '2018-12-25 20:34:38.000000', false),
       ('Permission management', 2, '/isystem/permission', 'system/PermissionList', null, null, 1, null, null, 11.00,
        null, null, true, true, null, false, null, false, null, null, null, null, null, '2018-12-25 20:34:38.000000',
        false),
       ('System notice', 2, '/isystem/annountCement', 'system/SysAnnouncementList', null, null, 1, 'annountCement',
        null, 6.00, null, null, true, true, null, false, null, false, null, null, null, null,
        '2019-01-02 17:31:23.000000', '2019-01-02 17:23:01.000000', false),
       ('Data Dictionary', 2, '/isystem/dict', 'system/DictList', null, null, 1, null, null, 12.00, null, null, true,
        true, null, false, null, false, null, null, null, null, '2018-12-28 15:37:54.000000',
        '2018-12-28 13:54:43.000000', false);

--
-- Insert data to table `user_tbl`
--
insert into user_tbl(user_name, password, full_name, sex, birth_date, avatar, email, phone, disable_flag, updated_by,
                     created_by, updated_date, created_date, deleted_flag)
values ('admin', '$2a$10$DVdKtQPt6gByXrXZNtMU8.7g5LL.TpLXno72T7NY03zzLt6382iLG', 'Đoàn Hải', 1, '1995-01-02',
        'user/20190119/logo-2_1547868176839.png', 'doanhai8080@gmail.com', '0982445665', false,
        'admin', 'admin', '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `permission_role_tbl`
--
insert into permission_role_tbl (role_id, permission_id, updated_by, created_by, updated_date, created_date,
                                 deleted_flag)
select r.id,
       p.id,
       'admin',
       'admin',
       '2019-09-01 00:00:00.000000',
       '2019-09-01 00:00:00.000000',
       false
from role_tbl r
         cross join permission_tbl p
where r.id = 1;

--
-- Insert data to table `user_role_tbl`
--
insert into user_role_tbl(role_id, user_id, updated_by, created_by, updated_date, created_date, deleted_flag)
select r.id,
       u.id,
       'admin',
       'admin',
       '2019-09-01 00:00:00.000000',
       '2019-09-01 00:00:00.000000',
       false
from role_tbl r
         cross join user_tbl u
where r.id = 1;
