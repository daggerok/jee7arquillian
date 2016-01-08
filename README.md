jee7arquillian [![build](https://travis-ci.org/daggerok/jee7arquillian.svg?branch=master)](https://travis-ci.org/daggerok/jee7arquillian)
==============

## jee arquillian testing without pain :)

#### to test in different ways look into pom.xml profiles configurations
    
    $ mvn test # or $ mvn test -Pwildfly-embedded-arquillian # default
    $ mvn test -Pwildfly-managed-arquillian
    $ mvn test -Pwildfly-remote-arquillian (see comments)
    $ mvn test -Pglassfish-embedded-arquillian

#### use jbossas profile to run developed war into dev container
    
    $ mvn -Pjbossas install && mvn -Pjbossas jboss-as:run
    $ mvn -Pjbossas jboss-as:deploy
    # do something, for example:
    $ curl --data "age=-100&name=Maksimko" http://0.0.0.0:8080/jee7arquillian-1.0/registry/employee
    $ curl -X PUT --data "name=Maksimko%20PUT&age=100" http://0.0.0.0:8080/jee7arquillian-1.0/registry/employee
    $ curl -X POST --data "age=-1&name=Maksimko%20POST" http://0.0.0.0:8080/jee7arquillian-1.0/registry/employee
    $ curl -H "Accept: application/json" http://0.0.0.0:8080/jee7arquillian-1.0/registry/employee
    $ mvn -Pjbossas jboss-as:redeploy
    # do something else... and finally
    $ mvn -Pjbossas jboss-as:shutdown

#### read more

http://arquillian.org/

https://docs.jboss.org/author/display/ARQ/Reference+Guide

https://developer.jboss.org/en/arquillian/faq

https://community.jboss.org/groups/testing

and of course: https://github.com/arquillian

cheers
