package org.erik.tbss.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Write class comments here.
 * User: caiwd
 * Date: 14-5-15 下午2:03
 * version $Id: demo.java, v0.1 Exp $.
 */
@Controller
public class DemoController {

    @RequestMapping("/index/{username}")
    public String index(@PathVariable("username") String username){
        System.out.println("执行了controller"+username);
        return "index";
    }

}
