package com.alberta.hita.controller;


import com.alberta.hita.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Description.
 * <p/>
 * Date: 9/30/13
 *
 *
 * @version 1.0
 */
@Controller
public class WebController {
    public Storage db_store = new Storage();

    @RequestMapping("/retrieve")
    @ResponseStatus(HttpStatus.OK)
    public
    @ResponseBody
    Task getResponse(
                     @RequestParam(value = "result", required = false) String result,
                     @RequestParam("id") Integer id) {
        System.out.println("id was: " + id);
        return getDatabaseInformation(id);
    }


    private Task getDatabaseInformation(Integer id) {
        try {
            return db_store.getData(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
