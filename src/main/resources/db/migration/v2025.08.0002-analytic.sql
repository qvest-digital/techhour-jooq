create view revenue as
    select c.id, c.name, c.email, c.since, sum (p.quantity * b.price) as amount
        from purchase      p
        join customer c on p.customer = c.id
        join book     b on p.book     = b.id
        group by c.id, c.name, c.email, c.since
;
