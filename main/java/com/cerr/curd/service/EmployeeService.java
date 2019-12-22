package com.cerr.curd.service;

import com.cerr.curd.bean.Employee;
import com.cerr.curd.bean.EmployeeExample;
import com.cerr.curd.dao.EmployeeMapper;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    public void updateEmp(Employee employee){
        employeeMapper.updateByPrimaryKeySelective(employee);
    }
    /**
     * 查询一个Employee
     * @param id
     * @return
     */
    public Employee getEmp(Integer id){
        return employeeMapper.selectByPrimaryKey(id);
    }
    /**
     * 查询所有（使用分页查询）
     * @return
     */
    public List<Employee> getAll(){
        return employeeMapper.selectByExampleWithDept(null);
    }

    /**
     * 保存员工
     * @param employee
     * @return
     */
    public boolean saveEmp(Employee employee){
        employeeMapper.insertSelective(employee);
        return true;
    }

    /**
     * 校验用户名是否可用
     * @param name
     * @return true:代表当前姓名可用 flase:代表当前姓名不可用
     */
    public boolean checkUser(String name){
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(name);
        long count = employeeMapper.countByExample(example);
        return count == 0;
    }

    public void deleteEmp(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    public void deleteBatch(List<Integer> ids){
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);
    }
}
