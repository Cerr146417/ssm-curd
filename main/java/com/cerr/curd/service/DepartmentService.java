package com.cerr.curd.service;

import com.cerr.curd.bean.Department;
import com.cerr.curd.dao.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    public List<Department> getDepts(){
        List<Department> list  = departmentMapper.selectByExample(null);
        return list;
    }
}
