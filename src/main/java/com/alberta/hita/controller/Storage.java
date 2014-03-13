package com.alberta.hita.controller;

import com.alberta.hita.model.Status;
import com.alberta.hita.model.Task;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.*;

/**
 * Description.
 * <p/>
 * Date: 10/1/13
 *
 * @version 1.0
 */
public class Storage {
    //Database connection parameters
    String url = "jdbc:mysql://localhost:3306/";
    String dbname = "hita";
    String userName = "root";
    String password = "root";
    Task task = new Task();
    Connection conn;
    //TODO:Add support for submission time

    /**
     * Create new storage engine. The only field to be initialized in the connection.
     */
    public Storage() {
        try {
            conn = DriverManager.getConnection(url + dbname, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException("Internal System Error.", e);
        }
    }

    /**
     * This method searches the HITA database by id. It accepts an integer id and returns the
     * corresponding record.
     *
     * @param id Integer id value of the task.
     * @return Task Task record matching the supplied id.
     */
    public Task getTaskById(Integer id) {
        try {
            System.out.printf("Sending the query with id= %d \n", id);
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE id = ?");
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                DateTime dt = new DateTime(res.getTimestamp("updateTime"));
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                task.setUuid(res.getInt("id"));
                task.setName(res.getString("name"));
                task.setType(res.getString("type"));
                task.setStatus(res.getString("status"));
                task.setDescription(res.getString("description"));
                task.setUpdateTime(fmt.print(dt));
            }
            //conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    public int createTask(Task task) throws SQLException {

        PreparedStatement statement = conn.prepareStatement("INSERT INTO master(name, type, status, description) VALUES (?,?,?,?)");
        statement.setString(1,task.getName());
        statement.setString(2,task.getType());
        statement.setString(3, (Status.NEW).toString());
        statement.setString(4,task.getDescription());
        statement.executeUpdate();
        return getMostRecentTask();

    }

    public void updateTask(Task task) throws SQLException {

        System.out.printf("Sending the update query with id = %d\n", task.getUuid());

        if (task.getName() != null) {
            System.out.println("name exists");
            updateName(task.getUuid(), task.getName());
        }
        if (task.getType() != null) {
            System.out.println("type exists");
            updateTaskType(task.getUuid(), task.getType());
        }
        if (task.getStatus() != null) {
            System.out.println("status exists");
            updateStatus(task.getUuid(), task.getStatus());
        }
        if (task.getDescription() != null) {
            System.out.println("desc exists");
            updateDesc(task.getUuid(), task.getDescription());
        }


    }

    private void updateStatus(Integer id, String status) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET status = ? WHERE id = ?");
        statement.setString(1, status);
        statement.setInt(2, id);
        statement.executeUpdate();

    }

    private void updateTaskType(Integer id, String task) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET type = ? WHERE id = ?");
        statement.setString(1, task);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    private void updateName(Integer id, String name) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET name = ? WHERE id = ?");
        statement.setString(1, name);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    private void updateDesc(Integer id, String desc) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET description = ? WHERE id = ?");
        statement.setString(1, desc);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    public void taskSearch() {

    }

    public void searchByStatus(String status) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE status = ?");
        statement.setString(1, status);
        statement.executeQuery();
    }

    public void searchByType(String type) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE type = ?");
        statement.setString(1, type);
        statement.executeQuery();
    }

   private int getMostRecentTask() throws SQLException {
       int max = 0;
       PreparedStatement maxID = conn.prepareStatement("SELECT MAX(id) FROM master");
       ResultSet res = maxID.executeQuery();
       while (res.next()) {
           max = res.getInt("MAX(id)");
       }

       return max;
   }
}
