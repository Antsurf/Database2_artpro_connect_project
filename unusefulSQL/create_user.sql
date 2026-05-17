create user "project" IDENTIFIED BY "projectPW";

GRANT ALL PRIVILEGES
ON artproject.*
TO 'project'
WITH GRANT OPTION;

create user "admin" IDENTIFIEd BY "admin";

GRANT ALL PRIVILEGES
ON artproject.*
TO 'admin'
WITH GRANT OPTION;

GRANT EXECUTE ON artproject.* TO "admin";