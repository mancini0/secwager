create table users(user_id text primary key,
pub_key text not null,
priv_key text not null,
p2pkh_addr text not null);

create table acct_balance(user_id text primary key references users(user_id) ,
available_balance int default 0,
escrowed_balance int default 0);


create type order_status as enum ('CANCELLED','FILLED','OPEN', 'REJECTED');

create type txn_reason as enum ('SAFE_DEPOSIT',
'RISKY_DEPOSIT',
'RISKY_DEPOSIT_BECOMES_SAFE',
'SAFE_DEPOSIT_BECOMES_RISKY',
'WITHDRAWAL',
'BET_LOSS',
'BET_WIN',
'BET_PUSH',
'POST_MARGIN',
'RESET_MARGIN',
'TRANSFER_TO_FRIEND');

create table orders(order_id text primary key, user_id text references users(user_id), submit_time timestamp,
order_status order_status, details jsonb);

create table txn_ledger(user_id text,
txn_time timestamp,
txn_reason txn_reason,
related_entity text,
available_balance int,
escrowed_balance int,
foreign key (user_id) references users(user_id));

