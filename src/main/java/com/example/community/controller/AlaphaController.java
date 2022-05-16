package com.example.community.controller;

import com.example.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author zx
 * @date 2022/5/10 15:40
 */

@Controller
@RequestMapping("/alpha")
public class AlaphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayhello() {
        return "hello spring boot!";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        //获取请求数据
        System.out.println(request.getMethod());//获取请求方式
        System.out.println(request.getServletPath());//请求路径
        Enumeration<String> enumeration = request.getHeaderNames();//得到所有请求行的key
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));//获取名为code的数据

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = response.getWriter();//获取输出流
        ) {
            writer.write("<h1>牛客网</h1>");//通过writer向浏览器打印一个网页
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //GET请求，用于希望向服务器获取请求，也是默认发送的请求

    // /students?curr=1&limit=20
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "curr", required = false, defaultValue = "1") int curr, //通过@RequestParam对参数注入进行详细声明
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(curr);
        System.out.println(limit);
        return "some students";
    }

    // /student/123
    @RequestMapping(path = "student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getstudent(@PathVariable("id") int id) {//使用路径变量
        System.out.println(id);
        return "a student";
    }

    // POST请求
    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String savestudent(String name,int age) {//和表单中数据名一致就可以传，也可以加@RequestParam注解
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应HTML数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)//返回HTML就不要加ResponseBody注解
    public ModelAndView getteacher() {
        ModelAndView mav=new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age",30);
        mav.setViewName("/demo/view");//指的是template文件夹下的demo/view.html
        return mav;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model) {//效果和上一个方法相同，建议采用这种，更简洁
        model.addAttribute("name","北京大学");
        model.addAttribute("age",80);
        return "/demo/view";
    }

    //响应json数据（异步请求）
    // JAva对象-》浏览器用JS解析对象，需要通过JSON实现两者的兼容，即Java对象->JSON对象->JS对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp() {
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salaer",8000.00);
        return emp;
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps() {//返回json集合
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salaer",8000.00);
        list.add(emp);
        emp=new HashMap<>();
        emp.put("name","李四");
        emp.put("age",24);
        emp.put("salaer",9000.00);
        list.add(emp);
        emp=new HashMap<>();
        emp.put("name","王五");
        emp.put("age",25);
        emp.put("salaer",10000.00);
        list.add(emp);
        return list;
    }
}
