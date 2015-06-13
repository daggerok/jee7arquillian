package com.daggerok.jee7app.ctrl;

import com.daggerok.jee7app.dao.EmployeeRepository;
import com.daggerok.jee7app.model.Employee;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@RequestScoped
@Transactional
@Path("employee")
public class EmployeeResource {
    private static final Logger logger = Logger.getLogger(EmployeeResource.class.getName());

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Employee> get() {
        return employeeRepository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Employee get(@PathParam("id") int id) {
        return employeeRepository.findOne(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void post(@FormParam("name") String name, @FormParam("age") int age) {
        Employee employee = new Employee(name, age);

        logger.info(String.format("Creating a new one '%s'", employee));
        employeeRepository.save(employee);
    }

    @PUT
    public void put(@FormParam("name") String name, @FormParam("age") int age) {
        logger.info(String.format("updating item '%s'", name));
        // put should update, but...
        post(name, age);
    }

    @DELETE
    @Path("{name}")
    public void delete(@PathParam("name") String name) {
        logger.info(String.format("removing item '%s'", name));
        employeeRepository.deleteByName(name);
    }
}
