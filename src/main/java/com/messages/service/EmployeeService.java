package com.messages.service;

import com.messages.entity.Employee;

import java.util.List;

public interface EmployeeService
{
    List<Employee> searchEmployee(String term);

    List<Employee> getAllEmployee();

    Employee getEmployeeById(long id);

    void saveEmp(Employee employee);

    void deleteEmpById(long id);
}
