create user "project" IDENTIFIED BY "projectPW";

GRANT ALL PRIVILEGES
ON artproject.*
TO 'project'
WITH GRANT OPTION;