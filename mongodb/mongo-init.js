db.createUser(
    {
        user: "event_service_user",
        pwd: "notsecretpassword",
        roles: [
            {
                role: "readWrite",
                db: "test"
            }
        ]
    }
);