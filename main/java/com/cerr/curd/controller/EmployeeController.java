package com.cerr.curd.controller;

import com.cerr.curd.bean.Employee;
import com.cerr.curd.bean.Msg;
import com.cerr.curd.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工的crud请求
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 删除
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("id") String ids){
        if (ids.contains("-")){ //批量删除
            List<Integer> del_ids = new ArrayList<Integer>();
            String [] str_ids = ids.split("-");
            for (String s : str_ids){
                del_ids.add(Integer.parseInt(s));
            }
            employeeService.deleteBatch(del_ids);
        }else{  //单个删除
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }
    /**
     *
     * 员工更新方法
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee){
        System.out.println("要更新："+employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    /**
     * 校验用户名是否可用
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUser")
    public Msg checkUser(@RequestParam("empName")String empName){
        //校验用户名是否合法
        String regx = "(^[a-zA-z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return Msg.fail().add("va_msg","用户名必须是2-5位中文或者6-16位英文和数字的组合！");
        }
        //数据库用户名重复校验
        boolean isOk = employeeService.checkUser(empName);
        if(isOk){
            return Msg.success();
        }
        return Msg.fail().add("va_msg","用户名已经有人使用了");
    }

    /**
     * 员工保存：支持JSR303校验
     * 需要导入hibernate-Validator
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    public Msg saveEmp(@Valid Employee employee, BindingResult result){
        Map<String ,Object> map = new HashMap<String, Object>();
        System.out.println(result);
        if (result.getErrorCount() > 0){
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors){
                System.out.println("错误的字段名"+error.getField());
                System.out.println("错误信息："+error.getDefaultMessage());
                map.put(error.getField(),error.getDefaultMessage());
            }
            return Msg.fail().add("errors",map);
        }
        if(employeeService.saveEmp(employee)){
            return Msg.success();
        }
        return Msg.fail();
    }

    @ResponseBody
    @RequestMapping("/emps")
    public Msg getEmps(@RequestParam(value = "pn",defaultValue = "1")Integer pn, Model model){
        //引入分页插件
        PageHelper.startPage(pn,5);
        //分页查询
        List<Employee> employees = employeeService.getAll();
        //包装查询的结果：封装了详细的分页信息及其我们查询出来的信息
        //第二个参数为连续显示的页数
        PageInfo page = new PageInfo(employees,5);
        return Msg.success().add("pageInfo",page);
    }
    /*
    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1")Integer pn, Model model){
        //引入分页插件
        PageHelper.startPage(pn,5);
        //分页查询
        List<Employee> employees = employeeService.getAll();
        //包装查询的结果：封装了详细的分页信息及其我们查询出来的信息
        //第二个参数为连续显示的页数
        PageInfo page = new PageInfo(employees,5);

        //添加到数据模型中
        model.addAttribute("pageInfo",page);
        return "list";
    }*/
}
