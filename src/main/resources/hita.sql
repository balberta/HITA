CREATE TABLE master ( id int NOT NULL AUTO_INCREMENT, name text NOT NULL, type text NOT NULL, status text NOT NULL, description text NOT NULL, submitTime timestamp NULL, updateTime timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--/
CREATE DEFINER=`root`@`%` TRIGGER set_submit_time
BEFORE INSERT ON 
hita.master
FOR EACH ROW SET NEW.submitTime = NOW()
/
