CREATE TABLE attendees
(
    id         VARCHAR(255) NOT NULL PRIMARY KEY,
    event_id   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT attendees_events_id_fk FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE RESTRICT ON UPDATE CASCADE
);