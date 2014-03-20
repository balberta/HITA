package com.alberta.hita.controller;


import com.alberta.hita.model.Task;
import com.alberta.hita.model.nocMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description.
 * Web controller the accepts URI arguments and delegates operations based on these
 * arguments.
 * Date: 3/15/14
 *
 * @version 1.0
 */
@Controller
public class WebController {
    ;
    private static final Logger log = LoggerFactory.getLogger(WebController.class);
    private Storage db_store = new Storage();
    private Task uriTask = null;


    @RequestMapping("/retrieve")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Task getResponse(@RequestParam("id") Integer id) {
        log.debug("id was: " + id);
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

        log.debug("update-> name:{} type:{} status:{} desc:{} ", name, type, status, desc);
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

        log.debug("create -> name:{} type:{} desc: {}", name, type, desc);
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
        log.debug("update-> field: {} type:{} status:{} sdate:{} edate:{} offset: {} limit:{}", field, type, status, sdate, edate, offset, limit);
        List searchRes = new ArrayList<>();
        try {
            switch (field) {
                case "byStatus":
                    log.debug("Searching by status");
                    searchRes = db_store.searchByStatus(status, limit, offset);
                    break;
                case "byType":
                    log.debug("Searching by type");
                    searchRes = db_store.searchByType(type, limit, offset);
                    break;
                case "byDate":
                    break;
                case "byTySt":
                    log.debug("Searching by type and status");
                    searchRes = db_store.searchByTypeStatus(type, status, limit, offset);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return searchRes;
    }

    @RequestMapping("/metrics/{facet}")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    nocMetric doMetrics(@PathVariable("facet") String facet
    ) {
        log.debug("metrics -> facet:{}", facet);
        nocMetric metricRes = null;
        try {
            switch (facet) {
                case "all":
                    metricRes = db_store.getHighLevelMetrics();
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metricRes;
    }


}
