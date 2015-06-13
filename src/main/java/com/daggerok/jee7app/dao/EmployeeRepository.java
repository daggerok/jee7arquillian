package com.daggerok.jee7app.dao;

import com.daggerok.jee7app.model.Employee;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmployeeRepository {
    private final List<Employee> db = new ArrayList<>();

    public void save(Employee employee) {
        db.add(employee);
    }

    public void deleteByName(String name) {
        Employee employee = findByName(name);

        if (employee != null) {
            db.remove(employee);
        }
    }

    private Employee findByName(String name) {
        for (Employee employee : db) {
            if (name.equals(employee.getName())) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> findAll() {
        return db;
    }

    public Employee findOne(int id) {
        if (id >= db.size()) return null;

        return db.get(id);
    }
}
