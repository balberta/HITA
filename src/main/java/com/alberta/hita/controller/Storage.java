package com.alberta.hita.controller;

import com.alberta.hita.model.DataPoint;
import com.alberta.hita.model.Status;
import com.alberta.hita.model.Task;
import com.alberta.hita.model.nocMetric;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Description.
 * This class creates and maintains a connection to the database used for processing.
 * Various methods are included for collecting and processing information from the database.
 * Date: 10/1/13
 *
 * @version 1.0
 */
public class Storage {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Storage.class);
    //Database connection parameters
    String url = "jdbc:mysql://10.129.59.133:3306/";
    String driver = "com.mysql.jdbc.Driver";
    String dbname = "hita";
    String userName = "hitaUser";
    String password = "u34atFRwNNMKB375";
    /*
    String url = "jdbc:mysql://192.168.1.100:3306/";
    String dbname = "hita";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "hitaUser";
    String password = "Yr9deFf9zntDRPun";
      */
    Task task = new Task();
    Connection conn;
    //TODO:Add support for submission time

    /**
     * Create new storage engine. The only field to be initialized in the connection.
     */
    public Storage() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url + dbname, userName, password);
        } catch (SQLException e) {
            throw new RuntimeException("Internal System Error.", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * This method searches the hita database by id. It accepts an integer id and returns the
     * corresponding record.
     *
     * @param id Integer id value of the task.
     * @return Task Task record matching the supplied id.
     */
    public Task getTaskById(Integer id) {
        try {
            log.debug("Sending the query with id= {}", id);
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE id = ?");
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                DateTime udt = new DateTime(res.getTimestamp("updateTime"));
                DateTime sdt = new DateTime(res.getTimestamp("submitTime"));
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                task.setUuid(res.getInt("id"));
                task.setName(res.getString("name"));
                task.setType(res.getString("type"));
                task.setStatus(res.getString("status"));
                task.setDescription(res.getString("description"));
                task.setUpdateTime(fmt.print(udt));
                task.setSubmitTime(fmt.print(sdt));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return task;
    }

    /**
     * This method creates a new entry in the database based on Task object passed in.
     *
     * @param task Task object containing the values to be entered into the database.
     * @return Integer value identifying the new task id.
     * @throws SQLException
     */
    public int createTask(Task task) throws SQLException {
        log.debug("Creating task...");
        PreparedStatement statement = conn.prepareStatement("INSERT INTO master(name, type, status, description) VALUES (?,?,?,?)");
        statement.setString(1, task.getName());
        statement.setString(2, task.getType());
        statement.setString(3, (Status.NEW).getText());
        statement.setString(4, task.getDescription());
        statement.executeUpdate();
        return getMostRecentTask();

    }

    /**
     * Updates a task in the database based on the Task input. This method determines
     * which fields have been updated and only updates those fields in the database.
     *
     * @param task Task containing the updated information.
     * @throws SQLException
     */
    public void updateTask(Task task) throws SQLException {
        log.debug("Sending the update query with id = {}", task.getUuid());

        if (task.getName() != null) {
            log.debug("name exists");
            updateName(task.getUuid(), task.getName());
        }
        if (task.getType() != null) {
            log.debug("type exists");
            updateTaskType(task.getUuid(), task.getType());
        }
        if (task.getStatus() != null) {
            log.debug("status exists");
            updateStatus(task.getUuid(), task.getStatus());
        }
        if (task.getDescription() != null) {
            log.debug("desc exists");
            updateDesc(task.getUuid(), task.getDescription());
        }


    }

    /**
     * Update the status of a task based on the Integer id.
     *
     * @param id     Integer ID of the task to be updated.
     * @param status String indicating the updated status.
     * @throws SQLException
     */
    private void updateStatus(Integer id, String status) throws SQLException {
        log.debug("Updating status...");
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET status = ? WHERE id = ?");
        statement.setString(1, status);
        statement.setInt(2, id);
        statement.executeUpdate();

    }

    /**
     * Update the type of the task based on the Integer ID.
     *
     * @param id   Integer ID of the task to be updated.
     * @param task String indicating the type of the updated task.
     * @throws SQLException
     */
    private void updateTaskType(Integer id, String task) throws SQLException {
        log.debug("Updating task...");
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET type = ? WHERE id = ?");
        statement.setString(1, task);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    /**
     * Update the name of the task based on the Integer ID.
     *
     * @param id   Integer ID of the task to be updated.
     * @param name String indicating the name of the updated task.
     * @throws SQLException
     */
    private void updateName(Integer id, String name) throws SQLException {
        log.debug("Updating name...");
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET name = ? WHERE id = ?");
        statement.setString(1, name);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    /**
     * Update the description of the task based on the Integer ID.
     *
     * @param id   Integer ID of the task to be updated.
     * @param desc String indicating the description of the updated task.
     * @throws SQLException
     */
    private void updateDesc(Integer id, String desc) throws SQLException {
        log.debug("Updating description...");
        PreparedStatement statement = conn.prepareStatement("UPDATE master SET description = ? WHERE id = ?");
        statement.setString(1, desc);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    /**
     * Searches the database and returns a list of Tasks based on the status
     * provided. Currently this returns all tasks that fit this status, however,
     * future enhancements will allow for pagination therefore only returning a limited
     * set of tasks based on a provided offset.
     *
     * @param status String indicating the status of the tasks to searched.
     * @param limit  Integer value indicating the number of results to return.
     * @param offset Integer value indicating the offset.
     * @return A list of Tasks matching the search criteria.
     * @throws SQLException
     */
    public List searchByStatus(String status, String limit, String offset) throws SQLException {
        log.debug("In searchByStatus");
        List<Task> searchRes = new ArrayList();
        log.debug("SELECT * FROM master WHERE status = %s\n", status);
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE status = ?");
        statement.setString(1, status);
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            Task task = new Task();
            DateTime udt = new DateTime(res.getTimestamp("updateTime"));
            DateTime sdt = new DateTime(res.getTimestamp("submitTime"));
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            task.setUuid(res.getInt("id"));
            task.setName(res.getString("name"));
            task.setType(res.getString("type"));
            task.setStatus(res.getString("status"));
            task.setDescription(res.getString("description"));
            task.setUpdateTime(fmt.print(udt));
            task.setSubmitTime(fmt.print(sdt));
            searchRes.add(task);
        }
        return searchRes;
    }

    /**
     * Searches the database and returns a list of Tasks based on the type
     * provided. Currently this returns all tasks that fit this status, however,
     * future enhancements will allow for pagination therefore only returning a limited
     * set of tasks based on a provided offset.
     *
     * @param type   String indicating the type of the tasks to searched.
     * @param limit  Integer value indicating the number of results to return.
     * @param offset Integer value indicating the offset.
     * @return A list of Tasks matching the search criteria.
     * @throws SQLException
     */
    public List searchByType(String type, String limit, String offset) throws SQLException {
        log.debug("In searchByType");
        List<Task> searchRes = new ArrayList();
        log.debug("SELECT * FROM master WHERE type = %s\n", type);
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE type = ?");
        statement.setString(1, type);
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            Task task = new Task();
            DateTime udt = new DateTime(res.getTimestamp("updateTime"));
            DateTime sdt = new DateTime(res.getTimestamp("submitTime"));
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            task.setUuid(res.getInt("id"));
            task.setName(res.getString("name"));
            task.setType(res.getString("type"));
            task.setStatus(res.getString("status"));
            task.setDescription(res.getString("description"));
            task.setUpdateTime(fmt.print(udt));
            task.setSubmitTime(fmt.print(sdt));
            searchRes.add(task);
        }
        System.out.println(searchRes.toString());
        return searchRes;
    }

    /**
     * Searches the database and returns a list of Tasks based on the status and type
     * provided. Currently this returns all tasks that fit this status, however,
     * future enhancements will allow for pagination therefore only returning a limited
     * set of tasks based on a provided offset.
     *
     * @param status String indicating the status of the tasks to searched.
     * @param type   String indicating the type of the task to be searched.
     * @param limit  Integer value indicating the number of results to return.
     * @param offset Integer value indicating the offset.
     * @return A list of Tasks matching the search criteria.
     * @throws SQLException
     */
    public List searchByTypeStatus(String type, String status, String limit, String offset) throws SQLException {
        log.debug("In searchByTypeStatus");
        List<Task> searchRes = new ArrayList();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE type = ? AND status = ?");
        statement.setString(1, type.toUpperCase());
        statement.setString(2, status.toUpperCase());
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            Task task = new Task();
            DateTime udt = new DateTime(res.getTimestamp("updateTime"));
            DateTime sdt = new DateTime(res.getTimestamp("submitTime"));
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            task.setUuid(res.getInt("id"));
            task.setName(res.getString("name"));
            task.setType(res.getString("type"));
            task.setStatus(res.getString("status"));
            task.setDescription(res.getString("description"));
            task.setUpdateTime(fmt.print(udt));
            task.setSubmitTime(fmt.print(sdt));
            searchRes.add(task);
        }
        return searchRes;
    }

    /**
     * Captures the most recent task ID from the database. This method queries the database
     * for the maximum integer ID value which indicates the most recent task ID.
     *
     * @return Integer ID of the newest task.
     * @throws SQLException
     */
    private int getMostRecentTask() throws SQLException {
        log.debug("Getting most recent task...");
        int max = 0;
        PreparedStatement maxID = conn.prepareStatement("SELECT MAX(id) FROM master");
        ResultSet res = maxID.executeQuery();
        while (res.next()) {
            max = res.getInt("MAX(id)");
        }

        return max;
    }

    /**
     * Returns a set of metrics for the application. These metrics include the number of
     * open items vs closed vs new. In addition this provides a breakdown of the previous metrics
     * by issue vs task.
     *
     * @return nocMetric containing the statistics of the application.
     * @throws SQLException
     */
    public nocMetric getHighLevelMetrics() throws SQLException {
        log.debug("In getHighLevelMetrics");
        nocMetric resp = new nocMetric();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM stats");
        ResultSet res = statement.executeQuery();
        parseNOC(resp);
        parseIvT(res, resp);
        return resp;


    }

    /**
     * This method parses the output of a query and modifies the nocMetric object passed. This
     * modification adds the metrics for status (open, new, closed) for each type of item.
     *
     * @param resp nocMetric containing the metrics.
     * @throws SQLException
     */
    private void parseNOC(nocMetric resp) throws SQLException {
        log.debug("Parsing metrics");
        ArrayList<DataPoint<Integer>> data = new ArrayList<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM statsTotals ");
        ResultSet res = statement.executeQuery();
        while (res.next()) {
            DataPoint<Integer> point = new DataPoint<>(res.getString("status"), res.getInt("count"));
            data.add(point);
        }
        resp.setNocMet(data);
    }

    /**
     * This method parses the output of a query and modifies the nocMetric object. This modification
     * adds the metrics for the breakdown of the open, closed, new vs issue and task.
     *
     * @param res  ResultSet of the query to fetch the data.
     * @param resp nocMetric containing the metrics.
     * @throws SQLException
     */
    private void parseIvT(ResultSet res, nocMetric resp) throws SQLException {
        log.debug("Parsing IvT");
        ArrayList<DataPoint<Integer>> openData = new ArrayList<>();
        ArrayList<DataPoint<Integer>> newData = new ArrayList<>();
        ArrayList<DataPoint<Integer>> closedData = new ArrayList<>();
        DataPoint<Integer> point;
        //Loop through the result set and create a new datapoint for each entry based on status.
        while (res.next()) {
            switch (res.getString("status")) {
                case "Open":
                    point = new DataPoint<>(res.getString("type"), res.getInt("count"));
                    openData.add(point);
                    break;
                case "New":
                    point = new DataPoint<>(res.getString("type"), res.getInt("count"));
                    newData.add(point);
                    break;
                case "Closed":
                    point = new DataPoint<>(res.getString("type"), res.getInt("count"));
                    closedData.add(point);
                    break;
            }

        }
        //Assign the data to the nocMetric object
        resp.setNewMet(newData);
        resp.setClosedMet(closedData);
        resp.setOpenMet(openData);
    }

}
