create table customer (
    id          bigint          not null,
    name        text            not null,
    email       text,
    since       timestamptz     not null,

    primary key (id)
)
;

create table book (
    id  	bigint      not null,
    title	text        not null,
    author	text        not null,
    price	numeric,

    primary key (id)
)
;

create index book_title_author_idx on book (title, author)
;

create table purchase (
    id	        bigint          not null,
    customer	bigint          not null,
    book	    bigint          not null,
    quantity	int             not null,
    at      	timestamptz     not null,

    primary key (id),
    foreign key (customer) references customer (id),
    foreign key (book)     references book     (id)
)
;
