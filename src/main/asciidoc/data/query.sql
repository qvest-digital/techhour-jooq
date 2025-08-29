-- How many copies of each book have been sold
select b.title, sum (p.quantity) as sold
    from purchase      p
    join book     b on p.book = b.id
    group by      b.title
;

-- How many different books each customer has bought (only including those who bought more than one)
select c.name, count (distinct p.book) as bought
    from purchase      p
    join customer c on p.customer = c.id
    group by      c.name
    having count (distinct p.book) > 1
;

-- How much each customer has spent on books
select c.id, c.name, c.email, c.since, sum (p.quantity * b.price) as amount
    from purchase      p
    join customer c on p.customer = c.id
    join book     b on p.book     = b.id
    group by c.id, c.name, c.email, c.since
;
