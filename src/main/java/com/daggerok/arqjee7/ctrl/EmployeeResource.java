package com.daggerok.arqjee7.ctrl;

import com.daggerok.arqjee7.model.Employee;
import com.daggerok.arqjee7.dao.EmployeeRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
