package com.alberta.hita.controller;


import com.alberta.hita.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        return db_store.getTaskById(id);
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
    List doSearch(@PathVariable("field") String field,
                  @RequestParam(value = "type", required = false) String type,
                  @RequestParam(value = "status", required = false) String status,
                  @RequestParam(value = "sdate", required = false) String sdate,
                  @RequestParam(value = "edate", required = false) String edate,
                  @RequestParam(value = "offset", required = false) String offset,
                  @RequestParam(value = "limit", required = false) String limit) {
        List<Task> searchRes = new ArrayList<Task>();
        try {
            switch (field) {
                case "byStatus":
                    searchRes = db_store.searchByStatus(status, limit, offset);
                    break;
                case "byType":
                    searchRes = db_store.searchByType(type, limit, offset);
                    break;
                case "byDate":
                    break;
                case "byTySt":
                    searchRes = db_store.searchByTypeStatus(type, status, limit, offset);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchRes;
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
