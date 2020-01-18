--
-- Table structure for table branch_tbl
--
create table brand_tbl (
    id           bigint primary key generated always as identity,
    brand_name   varchar(128) not null,
    brand_code   varchar(16)  not null,
    brand_image  varchar(500) not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);

--
-- Table structure for table branch_tbl
--
create table branch_tbl (
    id             bigint primary key generated always as identity,
    branch_name    varchar(128) not null,
    branch_code    varchar(16)  not null,
    branch_address varchar(500),
    brand_id       bigint       not null,
    updated_by     bigint,
    created_by     bigint,
    updated_date   timestamp,
    created_date   timestamp,
    deleted_flag   boolean default false,
    foreign key (brand_id) references brand_tbl (id)
);

--
-- Table structure for table role_tbl
--
create table role_tbl (
    id           bigint primary key generated always as identity,
    role_name    varchar(128) not null,
    role_code    varchar(16)  not null,
    description  varchar(2000),
    branch_id    bigint,
    brand_id     bigint,
    is_root      boolean default false,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (branch_id) references branch_tbl (id),
    foreign key (brand_id) references brand_tbl (id)
);

--
-- Table structure for table user_tbl
--
create table user_tbl (
    id           bigint primary key generated always as identity,
    user_name    varchar(64)  not null,
    password     varchar(256) not null,
    full_name    varchar(256) not null,
    sex          smallint,
    birth_date   date,
    avatar       varchar(500),
    email        varchar(45)  not null,
    phone        varchar(45),
    branch_id    bigint,
    brand_id     bigint,
    disable_flag boolean      not null default false,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean               default false,
    foreign key (branch_id) references branch_tbl (id),
    foreign key (brand_id) references brand_tbl (id)
);
comment on column user_tbl.sex is 'Gender: 0-Other, 1-Male, 2-Female';

--
-- Table structure for table permission_tbl
--
create table permission_tbl (
    id                   bigint primary key,
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
    always_show          boolean,
    icon                 varchar(100),
    is_route             boolean     default true,
    is_leaf              boolean     default false,
    keep_alive           boolean     default true,
    hidden               boolean     default false,
    description          varchar(255),
    rule_flag            boolean     default false,
    status               boolean     default true,
    internal_or_external boolean     default true,
    updated_by           bigint,
    created_by           bigint,
    updated_date         timestamp,
    created_date         timestamp,
    deleted_flag         boolean     default false
);
comment on column permission_tbl.always_show is 'Aggregate child routes';
comment on column permission_tbl.is_route is 'Whether to route the menu';
comment on column permission_tbl.is_leaf is 'Whether leaf node';
comment on column permission_tbl.keep_alive is 'Whether to cache the page';
comment on column permission_tbl.hidden is 'Whether to hide routes';
comment on column permission_tbl.rule_flag is 'Whether to add data permissions';
comment on column permission_tbl.internal_or_external is 'How to open the external menu';
comment on column permission_tbl.status is 'Button permission status';
comment on column permission_tbl.menu_type is 'Menu type (0: first-level menu; 1: sub-menu: 2: button permissions)';
comment on column permission_tbl.perms is 'Menu permission encoding';
comment on column permission_tbl.perms_type is 'Permission Policy 1 Display 2 Disable';
comment on column permission_tbl.redirect is 'First-level menu jump address';

--
-- Table structure for table permission_role_tbl
--
create table permission_role_tbl (
    id            bigint primary key generated always as identity,
    role_id       bigint not null,
    permission_id bigint not null,
    api_id        varchar(255),
    updated_by    bigint,
    created_by    bigint,
    updated_date  timestamp,
    created_date  timestamp,
    deleted_flag  boolean default false,
    foreign key (role_id) references role_tbl (id),
    foreign key (permission_id) references permission_tbl (id)
);

--
-- Table structure for table user_role_tbl
--
create table user_role_tbl (
    id           bigint primary key generated always as identity,
    role_id      bigint not null,
    user_id      bigint not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (role_id) references role_tbl (id),
    foreign key (user_id) references user_tbl (id)
);

--
-- Table structure for table `qrtz_job_details`
--
create table qrtz_job_details (
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
create table qrtz_triggers (
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
create table qrtz_simple_triggers (
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
create table qrtz_cron_triggers (
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
create table qrtz_simprop_triggers (
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
create table qrtz_blob_triggers (
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
create table qrtz_calendars (
    sched_name    varchar(120) not null,
    calendar_name varchar(190) not null,
    calendar      bytea        not null,
    primary key (sched_name, calendar_name)
);

--
-- Table structure for table `qrtz_paused_trigger_grps`
--
create table qrtz_paused_trigger_grps (
    sched_name    varchar(120) not null,
    trigger_group varchar(190) not null,
    primary key (sched_name, trigger_group)
);

--
-- Table structure for table `qrtz_fired_triggers`
--
create table qrtz_fired_triggers (
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
create table qrtz_scheduler_state (
    sched_name        varchar(120) not null,
    instance_name     varchar(190) not null,
    last_checkin_time bigint       not null,
    checkin_interval  bigint       not null,
    primary key (sched_name, instance_name)
);

--
-- Table structure for table `qrtz_locks`
--
create table qrtz_locks (
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
-- Table structure for table access_token_tbl
--
create table access_token_tbl (
    id           bigint primary key generated always as identity,
    jti          varchar(2000) not null,
    refresh_jti  varchar(2000) not null,
    user_id      bigint        not null,
    client       varchar(50)   not null,
    created_date timestamp     not null
);

--
-- Insert data to table `brand_tbl`
--
insert into brand_tbl (brand_name, brand_code, brand_image, updated_by, created_by, updated_date, created_date,
                       deleted_flag)
values ('Juno', 'juno', '/brand/1/image/logo-2_1547868176839.png', 1, 1, '2019-09-01 00:00:00.000000',
        '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `branch_tbl`
--
insert into branch_tbl (branch_name, branch_code, branch_address, brand_id, updated_by, created_by, updated_date,
                        created_date, deleted_flag)
values ('Headquarters', 'juno_hanoi', '26 Hàng Bài, Hà Nội', 1, 1, 1, '2019-09-01 00:00:00.000000',
        '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `role_tbl`
--
insert into role_tbl (role_name, role_code, description, brand_id, branch_id, is_root, updated_by, created_by,
                      updated_date, created_date, deleted_flag)
values ('Super Administrator System', 'root',
        'This is the highest role of the system, created when the system initialized, can not be edited or deleted.',
        null, null, true, 1, 1, '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false),
       ('Admin brand', 'juno.admin', 'This is the highest role of the Juno brand.', 1, null, true, 1, 1,
        '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false),
       ('Admin Headquarters', 'juno.admin.ho', 'This is the highest role of the headquarters of the Juno brand.', 1, 1,
        true, 1, 1, '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false),
       ('Staff', 'juno.staff.ho', 'This is the normal role of the Juno Brand Headquarters.',
        1, 1, false, 1, 1, '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `user_tbl`
--
insert into user_tbl(user_name, password, full_name, sex, birth_date, avatar, email, phone, disable_flag, brand_id,
                     branch_id, updated_by, created_by, updated_date, created_date, deleted_flag)
values ('root', '$2a$10$DVdKtQPt6gByXrXZNtMU8.7g5LL.TpLXno72T7NY03zzLt6382iLG', 'Đoàn Hải', 1, '1995-01-02',
        '/user/1/avatar/logo-2_1547868176839.png', 'doanhai8080@gmail.com', '0982445665', false,
        null, null, 1, 1, '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `permission_tbl`
--
insert into permission_tbl (id, permission_name, parent_id, url, component, component_name, redirect, menu_type, perms,
                            perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description,
                            rule_flag, status, internal_or_external, updated_by, created_by, updated_date, created_date,
                            deleted_flag)
values (1, 'Home', null, '/dashboard/analysis', 'dashboard/Analysis', null, null, 0, null, null, 0.00, false, 'home',
        true, true, null, false, null, false, null, null, 1, 1, '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2, 'System Management', null, '/system', 'layouts/RouteView', null, null, 0, null, null, 2.00, false, 'setting',
        true, false, null, false, null, false, null, null, 1, 1, '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (20, 'Role management', 2, '/system/role', 'system/RoleList', null, null, 1, null, null, 3.00, false, null, true,
        false, null, false, null, false, null, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2000, 'Create role', 20, null, null, null, null, 2, 'role:create', '1', 1.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2001, 'Update role', 20, null, null, null, null, 2, 'role:update', '1', 2.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2002, 'Delete role', 20, null, null, null, null, 2, 'role:delete', '1', 3.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2003, 'Read role', 20, null, null, null, null, 2, 'role:read', '1', 4.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2004, 'Export role', 20, null, null, null, null, 2, 'role:export', '1', 5.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2005, 'Import role', 20, null, null, null, null, 2, 'role:import', '1', 6.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2006, 'Authorize role', 20, null, null, null, null, 2, 'role:permission:update', '1', 7.00, false, null, false,
        true, null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (21, 'User Management', 2, '/system/user', 'system/UserList', null, null, 1, null, null, 4.00, false, null, true,
        false, null, false, null, false, null, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2100, 'Create user', 21, null, null, null, null, 2, 'user:create', '1', 1.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2101, 'Update user', 21, null, null, null, null, 2, 'user:update', '1', 2.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2102, 'Delete user', 21, null, null, null, null, 2, 'user:delete', '1', 3.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2103, 'Read user', 21, null, null, null, null, 2, 'user:read', '1', 4.00, false, null, false, false, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2104, 'Export user', 21, null, null, null, null, 2, 'user:export', '1', 5.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2105, 'Import user', 21, null, null, null, null, 2, 'user:import', '1', 6.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (22, 'Menu management', 2, '/system/permission', 'system/PermissionList', null, null, 1, null, '1', 5.00, false,
        null, true, false, false, false, null, false, null, false, 1, 1, '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (23, 'Branch management', 2, '/system/branch', 'system/BranchList', null, null, 1, null, '1', 6.00, false,
        null, true, false, false, false, null, false, null, false, 1, 1, '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2300, 'Create branch', 23, null, null, null, null, 2, 'branch:create', '1', 1.00, false, null, false,
        true, null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2301, 'Update branch', 23, null, null, null, null, 2, 'branch:update', '1', 2.00, false, null, false, true,
        null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2302, 'Delete branch', 23, null, null, null, null, 2, 'branch:delete', '1', 3.00, false, null, false, true,
        null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2303, 'Read branch', 23, null, null, null, null, 2, 'branch:read', '1', 4.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2304, 'Export branch', 23, null, null, null, null, 2, 'branch:export', '1', 5.00, false, null, false, true,
        null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2305, 'Import branch', 23, null, null, null, null, 2, 'branch:import', '1', 6.00, false, null, false, true,
        null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (24, 'Brand management', 2, '/system/brand', 'system/BrandList', null, null, 1, null, '1', 6.00, false,
        null, true, false, false, false, null, false, null, false, 1, 1, '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2400, 'Create brand', 24, null, null, null, null, 2, 'brand:create', '1', 1.00, false, null, false,
        true, null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2401, 'Update brand', 24, null, null, null, null, 2, 'brand:update', '1', 2.00, false, null, false, true,
        null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2402, 'Delete brand', 24, null, null, null, null, 2, 'brand:delete', '1', 3.00, false, null, false, true,
        null, false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false),
       (2403, 'Read brand', 24, null, null, null, null, 2, 'brand:read', '1', 4.00, false, null, false, true, null,
        false, null, false, true, null, 1, 1, '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000', false);

--
-- Insert data to table `permission_role_tbl`
--
insert into permission_role_tbl (role_id, permission_id, updated_by, created_by, updated_date, created_date,
                                 deleted_flag, api_id)
select r.id,
       p.id,
       1,
       1,
       '2019-09-01 00:00:00.000000',
       '2019-09-01 00:00:00.000000',
       false,
       p.perms
from role_tbl r
         cross join permission_tbl p
where r.id = 1
  and p.deleted_flag = false;

--
-- Insert data to table `user_role_tbl`
--
insert into user_role_tbl(role_id, user_id, updated_by, created_by, updated_date, created_date, deleted_flag)
values (1, 1, 1, 1, '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);