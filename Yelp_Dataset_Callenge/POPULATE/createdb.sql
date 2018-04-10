create table yelp_user
(
    yelping_since varchar2(10),
    funny_votes number,
    useful_votes number,
    cool_votes number,
    review_count number,
    user_name varchar2(255),
    user_id varchar2(30),
    fans number,
    average_stars float,
    u_type varchar2(10),
    hot_compliment number,
    more_compliment number,
    profile_compliment number,
    cute_compliment number,
    list_compliment number,
    note_compliment number,
    plain_compliment number,
    cool_compliment number,
    funny_compliment number,
    writer_compliment number,
    photos_compliment number,
    primary key (user_id)
);

create table friends
(
    user_id varchar2(30),
    friend_id varchar2(30),
    primary key (user_id,friend_id),
    foreign key (user_id) references yelp_user (user_id) on delete cascade
);

create table elite_years
(
    user_id varchar2(30),
    elite number,
    primary key (user_id,elite),
    foreign key (user_id) references yelp_user (user_id) on delete cascade
);

create table business
(
    bid varchar2(30),
    full_address varchar2(255),
    open_now int,
    city varchar2(255),
    review_count number,
    b_name varchar2(255),
    neighborhoods varchar2(255),
    longitude float,
    state varchar2(255),
    stars float,
    latitude float,
    b_type varchar2(20),
    primary key (bid)
);

create table b_hours
(
    d_o_w varchar2(20),
    from_h float,
    to_h float,
    bid varchar2(30),
    primary key (bid, d_o_w),
    foreign key (bid) references business (bid) on delete cascade
);

create table neighborhoods
(
	n_name varchar2(100),
	bid varchar2(30),
	primary key (bid, n_name),
    foreign key (bid) references business (bid) on delete cascade
);

create table b_main_category
(
    c_name varchar2(255),
    bid varchar2(30),
    primary key (bid, c_name),
    foreign key (bid) references business (bid) on delete cascade
);

create table b_sub_category
(
    c_name varchar2(255),
    bid varchar2(30),
    primary key (bid, c_name),
    foreign key (bid) references business (bid) on delete cascade
);

create table b_attributes
(
    a_name varchar2(255),
    bid varchar2(30),
    primary key (bid, a_name),
    foreign key (bid) references business (bid) on delete cascade
);

create table check_in
(
    ci_day varchar2(30),
    ci_hour number,
    ci_count number,
    ci_type varchar2(20),
    bid varchar2(30),
    primary key (bid, ci_day, ci_count),
    foreign key (bid) references business (bid) on delete cascade
);

create table reviews
(
    funny_vote number,
    useful_vote number,
    cool_vote number,
    user_id varchar2(30),
    review_id varchar2(30),
    stars number,
    r_date varchar2(20),
    r_text clob,
    r_type varchar2(20),
    bid varchar2(30),
    primary key (review_id),
    foreign key (bid) references business (bid) on delete cascade,
    foreign key (user_id) references yelp_user (user_id) on delete cascade
);
