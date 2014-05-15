package org.erik.tbss.springmvc;

import org.erik.tbss.dao.PersonDao;
import org.erik.tbss.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PersonDao personDao;

    @RequestMapping("/index/{username}")
    public String index(@PathVariable("username") String username){
        System.out.println("执行了controller"+username);

        Person person = new Person();
        person.setName(username);
        personDao.save(person);

        return "index";
    }

}
