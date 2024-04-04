CREATE TABLE check_ins
(
    id          INTEGER      NOT NULL PRIMARY KEY IDENTITY,
    attendee_id VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT check_ins_attendees_fk FOREIGN KEY (attendee_id) REFERENCES attendees (id) ON DELETE RESTRICT ON UPDATE CASCADE
);