package com.daggerok.jee7app.ctrl;

import com.daggerok.jee7app.arquillian.AbstractArquillianTest;
import com.daggerok.jee7app.model.Employee;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.logging.Logger;
import org.junit.Before;
import org.junit.Test;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class EmployeeResourceTest extends AbstractArquillianTest {
    private final Logger logger = Logger.getLogger(getClass());

    private WebTarget target;

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "registry/employee").toExternalForm()));
        target.register(Employee.class);
    }

    @Test
    @InSequence(1) // sequenced test! pretty cool, yeah? :)
    public void testPostAndGet() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Penny");
        map.add("age", "1");
        target.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Leonard");
        map.add("age", "2");
        target.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Sheldon");
        map.add("age", "3");
        target.request().post(Entity.form(map));

        Employee[] employees = target.request().get(Employee[].class);
        assertEquals(3, employees.length);

        assertEquals("Penny", employees[0].getName());
        assertEquals(1, employees[0].getAge());

        assertEquals("Leonard", employees[1].getName());
        assertEquals(2, employees[1].getAge());

        assertEquals("Sheldon", employees[2].getName());
        assertEquals(3, employees[2].getAge());
    }

    @Test
    @InSequence(2)
    public void testGetSingle() {
        Employee employee = target.path("{id}").resolveTemplate("id", "1")
                .request(MediaType.APPLICATION_XML).get(Employee.class);
        assertEquals("Leonard", employee.getName());
        assertEquals(2, employee.getAge());
    }

    @Test
    @InSequence(3)
    public void testPut() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Howard");
        map.add("age", "4");
        target.request().post(Entity.form(map));

        Employee[] employees = target.request().get(Employee[].class);
        assertEquals(4, employees.length);
        assertEquals("Howard", employees[3].getName());
        assertEquals(4, employees[3].getAge());
    }

    @Test
    @InSequence(4)
    public void testDelete() {
        target.path("{name}").resolveTemplate("name", "Howard").request().delete();
        Employee[] employees = target.request().get(Employee[].class);
        assertEquals(3, employees.length);
    }

    @Test
    @InSequence(5)
    public void testClientSideNegotiation() {
        JsonArray json = target.request().accept(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);
        assertEquals(3, json.size());
        for (int i = 0; i < json.size(); i++) {
            JsonObject obj = json.getJsonObject(i);
            String name = obj.getString("name");
            int age = obj.getInt("age");

            assertTrue(String.format("Unknown Employee was returned: {name='%s', age='%d'}"
                    , name, age), Arrays.asList("Penny", "Leonard", "Sheldon").contains(name));
        }
    }

    @Test
    @InSequence(6)
    public void testDeleteAll() {
        Employee[] employees = target.request().get(Employee[].class);
        for (Employee p : employees) {
            target.path("{name}").resolveTemplate("name", p.getName()).request().delete();
        }
        employees = target.request().get(Employee[].class);
        assertEquals(0, employees.length);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    @Transactional(rollbackOn = {SQLException.class,
            ArrayIndexOutOfBoundsException.class})
    public void testThrowExpectation() throws Exception {
        Employee[] employees = target.request().get(Employee[].class);
        logger.debugf("you shouldn't se this message {}", employees[employees.length]);

        fail("should throw an ArrayIndexOutOfBoundsException");
    }

    // @Test(expected = IllegalStateException.class)
    @Transactional(dontRollbackOn = {IllegalStateException.class})
    public void testFailed() throws Exception {
        Employee[] employees = target.request().get(Employee[].class);
        logger.debugf("you shouldn't se this message {}", employees[employees.length]);

        fail("should throw an ArrayIndexOutOfBoundsException, " +
                "but was specify wrong one: IllegalStateException");
    }
}
