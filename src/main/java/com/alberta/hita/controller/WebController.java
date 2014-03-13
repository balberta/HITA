package com.alberta.hita.controller;


import com.alberta.hita.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * Description.
 * <p/>
 * Date: 9/30/13
 *
 * @version 1.0
 */
@Controller
public class WebController {
    private Storage db_store = new Storage();
    private Task uriTask = null;

    @RequestMapping("/retrieve")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Task getResponse(@RequestParam("id") Integer id) {
        System.out.println("id was: " + id);
        return getDatabaseInformation(id);
    }

    @RequestMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    void doUpdate(@RequestParam(value = "name", required = false) String name,
                  @RequestParam(value = "type", required = false) String type,
                  @RequestParam(value = "status", required = false) String status,
                  @RequestParam(value = "desc", required = false) String desc,
                  @RequestParam("id") Integer id) {
        System.out.println("id was: " + id);

        uriTask = new Task(id, name, type, status, desc);

        try {
            db_store.updateTask(uriTask);
        } catch (SQLException e) {
            throw new RuntimeException("Internal System Error.", e);
        }
    }


    @RequestMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Task doCreate(@RequestParam(value = "name", required = true) String name,
                  @RequestParam(value = "type", required = true) String type,
                  @RequestParam(value = "desc", required = true) String desc) {


        uriTask = new Task(name, type, desc);

        try {
            int newID = db_store.createTask(uriTask);
            return db_store.getTaskById(newID);
        } catch (SQLException e) {
            throw new RuntimeException("Internal System Error.", e);
        }
    }

    @RequestMapping("/search/{field}")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    void doSearch(@PathVariable("field") String field,
                  @RequestParam(value = "name", required = false) String name,
                  @RequestParam(value = "type", required = false) String type,
                  @RequestParam(value = "status", required = false) String status,
                  @RequestParam(value = "sdate", required = false) String sdate,
                  @RequestParam(value = "edate", required = false) String edate,
                  @RequestParam("id") Integer id) {
        try {
            switch (field) {
                case "byStatus":
                    db_store.searchByStatus(status);
                    break;
                case "byType":
                    db_store.searchByType(type);
                    break;
                case "byDate":
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Task getDatabaseInformation(Integer id) {
        try {
            return db_store.getTaskById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
