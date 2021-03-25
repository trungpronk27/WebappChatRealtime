package com.messages.service.impl;

import com.messages.entity.Employee;
import com.messages.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements com.messages.service.EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> searchEmployee(String term) {
        return employeeRepository.findByFirstName(term);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public void saveEmp(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(long id) {
        Optional<Employee> optional = employeeRepository.findById(id);
        Employee employee = null;
        if(optional.isPresent()){
            employee = optional.get();
        }else{
            throw new RuntimeException("Employee not found for id : " + id);
        }
        return employee;
    }

    @Override
    public void deleteEmpById(long id) {
        this.employeeRepository.deleteById(id);
    }
}
