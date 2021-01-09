package com.thomaskuenneth.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author thomas
 */
@RestController
public class Controller {

    private final Logger Log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello")
    @CrossOrigin
    String sayHello() {
        return "Hello";
    }

    @RequestMapping(value = "/hello/{delayInMilliseconds}", method = RequestMethod.GET)
    @CrossOrigin
    String sayHello(@PathVariable int delayInMilliseconds) {
        try {
            Thread.sleep(delayInMilliseconds);
        } catch (InterruptedException ex) {
            Log.error(null, ex);
        }
        return sayHello();
    }

    @PostMapping(path = "/hello", consumes = "text/plain", produces = "text/plain")
    @CrossOrigin
    String sayHello(@RequestBody String name) {
        return "Hello, " + name;
    }
}
