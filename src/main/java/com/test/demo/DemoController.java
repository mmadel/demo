package com.test.demo;

import com.test.demo.entity.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("ping")
public class DemoController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping()
    @ResponseBody
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok()
                .body("Last Ping ...!!!");
    }

    @PostMapping("create")
    public ResponseEntity<?> createPerson(@RequestBody com.test.demo.entity.Person person) {
        personRepository.save(person);
        return ResponseEntity.ok().build();
    }


}
