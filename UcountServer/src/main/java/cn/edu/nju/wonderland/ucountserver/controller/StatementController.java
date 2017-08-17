package cn.edu.nju.wonderland.ucountserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statements")
public class StatementController {

    @GetMapping
    public Map<String, Object> getStatement(@RequestParam Long userId) {
        return null;
    }

}
