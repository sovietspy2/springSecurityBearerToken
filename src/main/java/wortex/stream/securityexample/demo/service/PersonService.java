package wortex.stream.securityexample.demo.service;

import wortex.stream.securityexample.demo.model.Person;

public interface PersonService {

    Person findPerson(String name) throws Exception;

    Person deletePerson(String name);

}
