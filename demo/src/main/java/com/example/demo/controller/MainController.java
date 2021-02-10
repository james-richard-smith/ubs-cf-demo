package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping(path="/employee")
    public @ResponseBody String addEmployee (@RequestParam String name, @RequestParam String email) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employeeRepository.save(employee);
        return "Employee Saved:" + employee.getName();
    }

    @GetMapping(path="/employee")
    public @ResponseBody Iterable<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

}

