package wortex.stream.securityexample.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wortex.stream.securityexample.demo.model.Person;
import wortex.stream.securityexample.demo.service.PersonService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

    private ArrayList<Person> database;

    @Override
    public Person findPerson(String name) throws Exception {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        log.debug("auth information available in service: "+auth.getPrincipal());

        Optional<Person> person = database
                .stream()
                .filter(p->p.getName().equals(name))
                .findFirst();

        if (person.isPresent()) {
            return person.get();
        } else {
            throw new Exception("Not found");
        }
    }

    @Override
    public Person deletePerson(String name) {
        return new Person();
    }

    @PostConstruct
    private void init() {

        database = new ArrayList<>();
        database.add(new Person("Peter", 99, "LA"));
        database.add(new Person("Erica", 99, "BP"));
        database.add(new Person("Spider", 55, "NY"));
    }

}
