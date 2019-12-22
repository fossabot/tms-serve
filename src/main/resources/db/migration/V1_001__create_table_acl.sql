--
-- Table structure for table role_tbl
--
create table role_tbl (
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
-- Table structure for table branch_tbl
--
create table branch_tbl (
    id             bigint primary key generated always as identity,
    branch_name    varchar(128) not null,
    branch_code    varchar(16)  not null,
    branch_address varchar(500),
    updated_by     varchar(64),
    created_by     varchar(64),
    updated_date   timestamp,
    created_date   timestamp,
    deleted_flag   boolean default false
);

--
-- Table structure for table user_tbl
--
create table user_tbl (
    id           bigint primary key generated always as identity,
    user_name    varchar(64)  not null,
    password     varchar(256) not null,
    full_name    varchar(256) not null,
    sex          smallint     not null,
    birth_date   date         not null,
    avatar       varchar(500) not null,
    email        varchar(45)  not null,
    phone        varchar(45)  not null,
    branch_id    bigint       not null,
    disable_flag boolean      not null default false,
    updated_by   varchar(64),
    created_by   varchar(64),
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean               default false,
    foreign key (branch_id) references branch_tbl (id)
);
comment on column user_tbl.sex is 'Gender: 0-Other, 1-Male, 2-Female';

--
-- Table structure for table user_branch_tbl
--
create table user_branch_tbl (
    id           bigint primary key generated always as identity,
    user_id      bigint not null,
    branch_id    bigint not null,
    updated_by   varchar(64),
    created_by   varchar(64),
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (branch_id) references branch_tbl (id),
    foreign key (user_id) references user_tbl (id)
);

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
    updated_by           varchar(64),
    created_by           varchar(64),
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
    role_id       bigint       not null,
    permission_id bigint       not null,
    api_id        varchar(255),
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
create table user_role_tbl (
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
-- Insert data to table `role_tbl`
--
insert into role_tbl (role_name, role_code, description, updated_by, created_by, updated_date, created_date,
                      deleted_flag)
values ('Super Administrator', 'root', 'Super Administrator. Have access to view all pages.', 'root', 'root',
        '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `branch_tbl`
--
insert into branch_tbl (branch_name, branch_code, branch_address, updated_by, created_by, updated_date, created_date,
                        deleted_flag)
values ('Headquarters', 'BR00001', 'HaNoi', 'root', 'root',
        '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `user_tbl`
--
insert into user_tbl(user_name, password, full_name, sex, birth_date, avatar, email, phone, disable_flag, updated_by,
                     branch_id, created_by, updated_date, created_date, deleted_flag)
values ('root', '$2a$10$DVdKtQPt6gByXrXZNtMU8.7g5LL.TpLXno72T7NY03zzLt6382iLG', 'Đoàn Hải', 1, '1995-01-02',
        'user/20190119/logo-2_1547868176839.png', 'doanhai8080@gmail.com', '0982445665', false,
        'admin', 1, 'admin', '2019-09-01 00:00:00.000000', '2019-09-01 00:00:00.000000', false);

--
-- Insert data to table `permission_tbl`
--
insert into permission_tbl (id, permission_name, parent_id, url, component, component_name, redirect, menu_type, perms,
                            perms_type, sort_no, always_show, icon, is_route, is_leaf, keep_alive, hidden, description,
                            rule_flag, status, internal_or_external, updated_by, created_by, updated_date, created_date,
                            deleted_flag)
values (1, 'Home', null, '/dashboard/analysis', 'dashboard/Analysis', null, null, 0, null, null, 0.00, false, 'home',
        true, true, null, false, null, false, null, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2, 'System Management', null, '/system', 'layouts/RouteView', null, null, 0, null, null, 2.00, false, 'setting',
        true, false, null, false, null, false, null, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (20, 'Role management', 2, '/system/role', 'system/RoleList', null, null, 1, null, null, 3.00, false, null, true,
        false, null, false, null, false, null, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2000, 'Create role', 20, null, null, null, null, 2, 'role:create', '1', 1.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2001, 'Update role', 20, null, null, null, null, 2, 'role:update', '1', 2.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2002, 'Delete role', 20, null, null, null, null, 2, 'role:delete', '1', 3.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2003, 'Read role', 20, null, null, null, null, 2, 'role:read', '1', 4.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2004, 'Export role', 20, null, null, null, null, 2, 'role:export', '1', 5.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2005, 'Import role', 20, null, null, null, null, 2, 'role:import', '1', 6.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2006, 'Batch delete role', 20, null, null, null, null, 2, 'role:batch:delete', '1', 7.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2007, 'Batch enable role', 20, null, null, null, null, 2, 'role:batch:enable', '1', 8.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2008, 'Batch disable role', 20, null, null, null, null, 2, 'role:batch:disable', '1', 9.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (21, 'User Management', 2, '/system/user', 'system/UserList', null, null, 1, null, null, 4.00, false, null, true,
        false, null, false, null, false, null, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2100, 'Create user', 21, null, null, null, null, 2, 'user:create', '1', 1.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2101, 'Update user', 21, null, null, null, null, 2, 'user:update', '1', 2.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2102, 'Delete user', 21, null, null, null, null, 2, 'user:delete', '1', 3.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2103, 'Read user', 21, null, null, null, null, 2, 'user:read', '1', 4.00, false, null, false, false, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2104, 'Export user', 21, null, null, null, null, 2, 'user:export', '1', 5.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2105, 'Import user', 21, null, null, null, null, 2, 'user:import', '1', 6.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2106, 'Batch delete user', 21, null, null, null, null, 2, 'user:batch:delete', '1', 7.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2107, 'Batch enable user', 21, null, null, null, null, 2, 'user:batch:enable', '1', 8.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2108, 'Batch disable user', 21, null, null, null, null, 2, 'user:batch:disable', '1', 9.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (22, 'Menu management', 2, '/system/permission', 'system/PermissionList', null, null, 1, null, '1', 5.00, false,
        null, true, false, false, false, null, false, null, false, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (23, 'Branch management', 2, '/system/branch', 'system/BranchList', null, null, 1, null, '1', 6.00, false,
        null, true, false, false, false, null, false, null, false, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2300, 'Create branch', 23, null, null, null, null, 2, 'branch:create', '1', 1.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2301, 'Update branch', 23, null, null, null, null, 2, 'branch:update', '1', 2.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2302, 'Delete branch', 23, null, null, null, null, 2, 'branch:delete', '1', 3.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2303, 'Read branch', 23, null, null, null, null, 2, 'branch:read', '1', 4.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2304, 'Export branch', 23, null, null, null, null, 2, 'branch:export', '1', 5.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2305, 'Import branch', 23, null, null, null, null, 2, 'branch:import', '1', 6.00, false, null, false, true, null,
        false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000', '2019-01-01 00:00:00.000000',
        false),
       (2306, 'Batch delete branch', 23, null, null, null, null, 2, 'branch:batch:delete', '1', 7.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2307, 'Batch enable branch', 23, null, null, null, null, 2, 'branch:batch:enable', '1', 8.00, false, null, false,
        true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false),
       (2308, 'Batch disable branch', 23, null, null, null, null, 2, 'branch:batch:disable', '1', 9.00, false, null,
        false, true, null, false, null, false, true, null, 'root', 'root', '2019-01-01 00:00:00.000000',
        '2019-01-01 00:00:00.000000', false);

--
-- Insert data to table `permission_role_tbl`
--
insert into permission_role_tbl (role_id, permission_id, updated_by, created_by, updated_date, created_date,
                                 deleted_flag, api_id)
select r.id,
       p.id,
       'root',
       'root',
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
select r.id,
       u.id,
       'root',
       'root',
       '2019-09-01 00:00:00.000000',
       '2019-09-01 00:00:00.000000',
       false
from role_tbl r
         cross join user_tbl u
where r.id = 1;
