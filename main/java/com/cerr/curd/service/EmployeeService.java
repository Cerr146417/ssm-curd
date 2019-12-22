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
     * ��ѯһ��Employee
     * @param id
     * @return
     */
    public Employee getEmp(Integer id){
        return employeeMapper.selectByPrimaryKey(id);
    }
    /**
     * ��ѯ���У�ʹ�÷�ҳ��ѯ��
     * @return
     */
    public List<Employee> getAll(){
        return employeeMapper.selectByExampleWithDept(null);
    }

    /**
     * ����Ա��
     * @param employee
     * @return
     */
    public boolean saveEmp(Employee employee){
        employeeMapper.insertSelective(employee);
        return true;
    }

    /**
     * У���û����Ƿ����
     * @param name
     * @return true:����ǰ�������� flase:����ǰ����������
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
