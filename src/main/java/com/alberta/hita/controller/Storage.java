package com.alberta.hita.controller;

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
 *
 * @version 1.0
 */
public class Storage {
    Task task = new Task();
    public Task getData(Integer id) {
        //Database connection parameters
        String url = "jdbc:mysql://localhost:3306/";
        String dbname = "hita";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "root";


        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + dbname, userName, password);
            System.out.printf("Sending the query with id= %d \n", id);
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM master WHERE id = ?");
            statement.setInt(1,id);
            ResultSet res = statement.executeQuery();
            while(res.next())
            {
                DateTime dt = new DateTime(res.getTimestamp("updateTime"));
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                task.setUuid(res.getInt("id"));
                task.setName(res.getString("name"));
                task.setType(res.getString("type"));
                task.setStatus(res.getString("status"));
                task.setDescription(res.getString("description"));
                task.setUpdateTime(fmt.print(dt));
            }
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return task;
    }
}
