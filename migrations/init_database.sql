CREATE TABLE IF NOT EXISTS chat
(
    chat_id BIGINT,

    PRIMARY KEY (chat_id)
);

CREATE TABLE IF NOT EXISTS link
(
    link_id             BIGINT                              GENERATED ALWAYS AS IDENTITY,
    last_update_time    TIMESTAMP WITH TIME ZONE,
    name                varchar(100)                        NOT NULL ,
    url                 text                                NOT NULL ,

    PRIMARY KEY (link_id),
    UNIQUE (url)
);

CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id BIGINT,
    link_id BIGINT,
    FOREIGN KEY (chat_id) REFERENCES chat(chat_id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES link(link_id) ON DELETE CASCADE,
    PRIMARY KEY (chat_id, link_id)
);
