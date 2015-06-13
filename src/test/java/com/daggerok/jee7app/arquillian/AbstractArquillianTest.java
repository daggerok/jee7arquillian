package com.daggerok.jee7app.arquillian;

import com.daggerok.jee7app.Jee7app;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.runner.RunWith;

import javax.transaction.Transactional;

/**
 * Created by mak on 6/13/15.
 */
@Transactional
@RunWith(Arquillian.class)
public abstract class AbstractArquillianTest {
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, Jee7app.class.getPackage())
                .addAsLibraries(
                        // javax.xml.bind.annotations used by models
                        Maven.resolver().resolve("com.sun.xml.bind:jaxb-impl:2.0.1").withoutTransitivity().asSingleFile(),
                        // guava used in Employee.toString
                        Maven.resolver().resolve("com.google.guava:guava:18.0").withoutTransitivity().asSingleFile()
                );
    }
}
