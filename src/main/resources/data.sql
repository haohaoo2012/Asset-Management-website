INSERT INTO public.roles
    (roleid , rolename)
VALUES(1001, 'ROLE_USER');
INSERT INTO public.roles
    (roleid , rolename)
VALUES(1002, 'ROLE_ADMIN');
INSERT INTO public.roles
    (roleid , rolename)
VALUES(1003, 'ROLE_USER_LOCKED');
INSERT INTO public.roles
    (roleid , rolename)
VALUES(1004, 'ROLE_ADMIN_LOCKED');



INSERT INTO public.locations
    (locationid , address)
VALUES(101, 'HCM');
INSERT INTO public.locations
    (locationid , address)
VALUES(102, 'HN');



INSERT INTO public.categories
    (categorycode , categoryname)
VALUES('BM', 'Bluetooth Mouse');
INSERT INTO public.categories
    (categorycode , categoryname)
VALUES('PC', 'Personal Computer');
INSERT INTO public.categories
    (categorycode , categoryname)
VALUES('PJ', 'Projector');
INSERT INTO public.categories
    (categorycode , categoryname)
VALUES('MO', 'Monitor');
INSERT INTO public.categories
    (categorycode , categoryname)
VALUES('LA', 'Laptop');



insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('LA000001',
        'Laptop HP Probook 450 G1',
        '2021-07-06 14:24:05',
        false,
        'intel core i3',
        1,
        'LA',
        101
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('LA000002',
        'Laptop HP Probook 450 G1',
        '2021-07-06 14:25:05',
        false,
        'intel core i5',
        3,
        'LA',
        101
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('LA000003',
        'Laptop HP Probook 450 G1',
        '2021-07-06 14:26:05',
        false,
        'intel core i7',
        2,
        'LA',
        101
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('LA000004',
        'Laptop HP Probook 450 G1',
        '2021-07-06 14:24:05',
        false,
        'intel core i3',
        3,
        'LA',
        102
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('LA000005',
        'Laptop HP Probook 450 G1',
        '2021-07-06 14:25:05',
        false,
        'intel core i5',
        3,
        'LA',
        102
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('LA000006',
        'Laptop HP Probook 450 G1',
        '2021-07-06 14:26:05',
        false,
        'intel core i7',
        1,
        'LA',
        102
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('PC000001',
        'Personal Computer',
        '2021-07-06 14:24:05',
        false,
        'intel core i3',
        3,
        'PC',
        101
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('PC000002',
        'Personal Computer',
        '2021-07-06 14:25:05',
        false,
        'intel core i5',
        3,
        'PC',
        101
);
insert
	into
	public.assets
    (assetcode ,
    assetname,
    installdate,
    isdeleted,
    specification,
    state,
    categorycode,
    locationid)
values('PC000003',
        'Personal Computer',
        '2021-07-06 14:26:05',
        false,
        'intel core i7',
        2,
        'PC',
        101
);

-- password là nhimh@01062000
INSERT INTO public.users
    (staffcode, dateofbirth, firstlogin, firstname, gender, isdeleted, joineddate, lastname, password, username, locationid, roleid)
VALUES
    ('SD0001', '2000-06-01 05:00:00', true, 'Nhi', 'Female', false, '2021-08-06 12:30:00', 'Mai Hoang', '$2a$10$OLjR1nFlun7W1qhg98K4k.YVtGva/cqv2F.kN8RH8TRDJ8kaOjxCy', 'nhimh', 101, 1002);
INSERT INTO public.users
    (staffcode, dateofbirth, firstlogin, firstname, gender, isdeleted, joineddate, lastname, password, username, locationid, roleid)
VALUES
    ('SD0002', '2000-06-01 05:00:00', false, 'Nhi', 'Female', false, '2021-08-06 12:30:00', 'Mai Hoang', '$2a$10$OLjR1nFlun7W1qhg98K4k.YVtGva/cqv2F.kN8RH8TRDJ8kaOjxCy', 'nhimh1', 101, 1002);
INSERT INTO public.users
    (staffcode, dateofbirth, firstlogin, firstname, gender, isdeleted, joineddate, lastname, password, username, locationid, roleid)
VALUES
    ('SD0003', '2000-06-01 05:00:00', false, 'Nhi', 'Female', false, '2021-08-06 12:30:00', 'Mai Hoang', '$2a$10$G9L9OcCBIGTgaAIw19s9yO28qrG51R/wUSj7Ain0wNhu7t24O2VxC', 'nhimh2', 102, 1002);
INSERT INTO public.users
    (staffcode, dateofbirth, firstlogin, firstname, gender, isdeleted, joineddate, lastname, password, username, locationid, roleid)
VALUES
    ('SD0004', '2000-06-01 05:00:00', true, 'Nhi', 'Female', false, '2021-08-06 12:30:00', 'Mai Hoang', '$2a$10$OLjR1nFlun7W1qhg98K4k.YVtGva/cqv2F.kN8RH8TRDJ8kaOjxCy', 'nhimh3', 101, 1001);
INSERT INTO public.users
    (staffcode, dateofbirth, firstlogin, firstname, gender, isdeleted, joineddate, lastname, password, username, locationid, roleid)
VALUES
    ('SD0005', '2000-06-01 05:00:00', false, 'Nhi', 'Female', false, '2021-08-06 12:30:00', 'Mai Hoang', '$2a$10$OLjR1nFlun7W1qhg98K4k.YVtGva/cqv2F.kN8RH8TRDJ8kaOjxCy', 'nhimh4', 101, 1001);
INSERT INTO public.users
    (staffcode, dateofbirth, firstlogin, firstname, gender, isdeleted, joineddate, lastname, password, username, locationid, roleid)
VALUES
    ('SD0006', '2000-06-01 05:00:00', false, 'Nhi', 'Female', false, '2021-08-06 12:30:00', 'Mai Hoang', '$2a$10$G9L9OcCBIGTgaAIw19s9yO28qrG51R/wUSj7Ain0wNhu7t24O2VxC', 'nhimh5', 102, 1001);



INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (3, '2021-01-17 00:00:00', false, 'test', 3, 'LA000003', 'SD0001', 'SD0004');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (4, '2021-02-15 00:00:00', false, 'test', 1, 'PC000001', 'SD0001', 'SD0002');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (1, '2021-01-15 00:00:00', false, 'test', 4, 'LA000001', 'SD0001', 'SD0004');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (2, '2021-01-16 00:00:00', false, 'test', 2, 'LA000002', 'SD0001', 'SD0004');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (5, '2021-02-16 00:00:00', true, 'test', 2, 'PC000002', 'SD0001', 'SD0002');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (6, '2021-02-16 00:00:00', false, 'test', 2, 'PC000002', 'SD0001', 'SD0002');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (7, '2021-02-17 00:00:00', true, 'test', 3, 'PC000003', 'SD0001', 'SD0002');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (8, '2021-02-18 00:00:00', false, 'test', 3, 'PC000003', 'SD0001', 'SD0002');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (9, '2021-02-15 00:00:00', false, 'test', 1, 'LA000004', 'SD0003', 'SD0006');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (10, '2021-02-16 00:00:00', true, 'test', 2, 'LA000005', 'SD0003', 'SD0006');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (13, '2021-02-18 00:00:00', false, 'test', 3, 'LA000006', 'SD0003', 'SD0006');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (11, '2021-02-16 00:00:00', false, 'test', 2, 'LA000005', 'SD0003', 'SD0006');
INSERT INTO public.assignments
    (assignmentid, assigneddate, isdeleted, note, state, assetcode, assignedby, assignedto)
VALUES
    (12, '2021-02-17 00:00:00', true, 'test', 3, 'LA000006', 'SD0003', 'SD0006');



INSERT INTO public.requests
    (requestid, isdeleted, returneddate, state, acceptedby, assignmentid, requestedby)
VALUES
    (1, true, NULL, 1, NULL, 1, 'SD0001');
INSERT INTO public.requests
    (requestid, isdeleted, returneddate, state, acceptedby, assignmentid, requestedby)
VALUES
    (2, false, '2021-01-16 00:00:00', 2, 'SD0002', 1, 'SD0001');
INSERT INTO public.requests
    (requestid, isdeleted, returneddate, state, acceptedby, assignmentid, requestedby)
VALUES
    (3, false, NULL, 1, NULL, 4, 'SD0001');
