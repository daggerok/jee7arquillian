package com.daggerok.arqjee7.ctrl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

import com.daggerok.arqjee7.arquillian.AbstractArquillianTest;
import com.daggerok.arqjee7.model.Employee;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.Test;

public class EmployeeResourceTest extends AbstractArquillianTest {
    private WebTarget target;

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "registry/employee").toExternalForm()));
        target.register(Employee.class);
    }

    /**
     * Test of getList method, of class MyResource.
     */
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

        Employee[] list = target.request().get(Employee[].class);
        assertEquals(3, list.length);

        assertEquals("Penny", list[0].getName());
        assertEquals(1, list[0].getAge());

        assertEquals("Leonard", list[1].getName());
        assertEquals(2, list[1].getAge());

        assertEquals("Sheldon", list[2].getName());
        assertEquals(3, list[2].getAge());
    }

    /**
     * Test of getPerson method, of class MyResource.
     */
    @Test
    @InSequence(2)
    public void testGetSingle() {
        Employee p = target
                .path("{id}")
                .resolveTemplate("id", "1")
                .request(MediaType.APPLICATION_XML)
                .get(Employee.class);
        assertEquals("Leonard", p.getName());
        assertEquals(2, p.getAge());
    }

    /**
     * Test of putToList method, of class MyResource.
     */
    @Test
    @InSequence(3)
    public void testPut() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Howard");
        map.add("age", "4");
        target.request().post(Entity.form(map));

        Employee[] list = target.request().get(Employee[].class);
        assertEquals(4, list.length);

        assertEquals("Howard", list[3].getName());
        assertEquals(4, list[3].getAge());
    }

    /**
     * Test of deleteFromList method, of class MyResource.
     */
    @Test
    @InSequence(4)
    public void testDelete() {
        target
                .path("{name}")
                .resolveTemplate("name", "Howard")
                .request()
                .delete();
        Employee[] list = target.request().get(Employee[].class);
        assertEquals(3, list.length);
    }

    @Test
    @InSequence(5)
    public void testClientSideNegotiation() {
        JsonArray json = target.request().accept(MediaType.APPLICATION_JSON).get(JsonArray.class);
        assertEquals(3, json.size());
        for (int i = 0; i < json.size(); i++) {
            JsonObject obj = json.getJsonObject(i);
            String name = obj.getString("name");
            int age = obj.getInt("age");

            assertTrue("Unknown Employee was returned [" + name + ", " + age + "]",
                    Arrays.asList("Penny", "Leonard", "Sheldon").contains(name));
        }
    }

    @Test
    @InSequence(6)
    public void testDeleteAll() {
        Employee[] list = target.request().get(Employee[].class);
        for (Employee p : list) {
            target
                    .path("{name}")
                    .resolveTemplate("name", p.getName())
                    .request()
                    .delete();
        }
        list = target.request().get(Employee[].class);
        assertEquals(0, list.length);
    }
}
