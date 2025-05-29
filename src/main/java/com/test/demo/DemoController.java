package com.test.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("ping")
public class DemoController {
    @GetMapping()
    @ResponseBody
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok()
                .body("Ping push notification4...!!!!");
    }
}
