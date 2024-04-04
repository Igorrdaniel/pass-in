CREATE UNIQUE INDEX idx_events_slug ON events (slug);
CREATE UNIQUE INDEX idx_attendees_event_id_email ON attendees (event_id, email);
CREATE UNIQUE INDEX idx_check_ins_attendee_id ON check_ins (attendee_id);