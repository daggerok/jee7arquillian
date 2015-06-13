package com.daggerok.jee7app.ctrl;

import com.daggerok.jee7app.dao.EmployeeRepository;
import com.daggerok.jee7app.model.Employee;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Transactional
@Path("employee")
public class EmployeeResource {
    // Ideally this state should be stored in a database
    @Inject
    EmployeeRepository bean;

    @GET
    @Produces({"application/xml", "application/json"})
    public Employee[] getList() {
        return bean.getEmployees().toArray(new Employee[0]);
    }

    @GET
    @Produces({"application/json", "application/xml"})
    @Path("{id}")
    public Employee get(@PathParam("id") int id) {
        if (id < bean.getEmployees().size())
            return bean.getEmployees().get(id);
        else
            return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addToList(@FormParam("name") String name,
                          @FormParam("age") int age) {
        System.out.println("Creating a new item: " + name);
        bean.addEmployee(new Employee(name, age));
    }

    @PUT
    public void putToList(@FormParam("name") String name,
                          @FormParam("age") int age) {
        addToList(name, age);
    }

    @DELETE
    @Path("{name}")
    public void deleteFromList(@PathParam("name") String name) {
        bean.deleteEmployee(name);
    }
}
