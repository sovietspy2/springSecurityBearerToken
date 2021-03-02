package wortex.stream.securityexample.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wortex.stream.securityexample.demo.model.Person;
import wortex.stream.securityexample.demo.service.PersonService;

@RestController
@RequestMapping(path = "/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(path = "/find/{name}", method = RequestMethod.GET)
    public Person find(@PathVariable String name) throws Exception {
        return personService.findPerson(name);
    }

    @RequestMapping(path = "/delete/{name}", method = RequestMethod.GET)
    public Person delete(@PathVariable String name) {
        return new Person();
    }
}
