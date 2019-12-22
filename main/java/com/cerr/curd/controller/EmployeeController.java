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
 * ����Ա����crud����
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * ɾ��
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("id") String ids){
        if (ids.contains("-")){ //����ɾ��
            List<Integer> del_ids = new ArrayList<Integer>();
            String [] str_ids = ids.split("-");
            for (String s : str_ids){
                del_ids.add(Integer.parseInt(s));
            }
            employeeService.deleteBatch(del_ids);
        }else{  //����ɾ��
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }
    /**
     *
     * Ա�����·���
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee){
        System.out.println("Ҫ���£�"+employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * ����id��ѯԱ��
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
     * У���û����Ƿ����
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkUser")
    public Msg checkUser(@RequestParam("empName")String empName){
        //У���û����Ƿ�Ϸ�
        String regx = "(^[a-zA-z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return Msg.fail().add("va_msg","�û���������2-5λ���Ļ���6-16λӢ�ĺ����ֵ���ϣ�");
        }
        //���ݿ��û����ظ�У��
        boolean isOk = employeeService.checkUser(empName);
        if(isOk){
            return Msg.success();
        }
        return Msg.fail().add("va_msg","�û����Ѿ�����ʹ����");
    }

    /**
     * Ա�����棺֧��JSR303У��
     * ��Ҫ����hibernate-Validator
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
                System.out.println("������ֶ���"+error.getField());
                System.out.println("������Ϣ��"+error.getDefaultMessage());
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
        //�����ҳ���
        PageHelper.startPage(pn,5);
        //��ҳ��ѯ
        List<Employee> employees = employeeService.getAll();
        //��װ��ѯ�Ľ������װ����ϸ�ķ�ҳ��Ϣ�������ǲ�ѯ��������Ϣ
        //�ڶ�������Ϊ������ʾ��ҳ��
        PageInfo page = new PageInfo(employees,5);
        return Msg.success().add("pageInfo",page);
    }
    /*
    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1")Integer pn, Model model){
        //�����ҳ���
        PageHelper.startPage(pn,5);
        //��ҳ��ѯ
        List<Employee> employees = employeeService.getAll();
        //��װ��ѯ�Ľ������װ����ϸ�ķ�ҳ��Ϣ�������ǲ�ѯ��������Ϣ
        //�ڶ�������Ϊ������ʾ��ҳ��
        PageInfo page = new PageInfo(employees,5);

        //��ӵ�����ģ����
        model.addAttribute("pageInfo",page);
        return "list";
    }*/
}
