package com.simplex.controller;

import com.simplex.model.SimplexRequest;
import com.simplex.model.SimplexResponse;
import com.simplex.service.SimplexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simplex")
@CrossOrigin(origins = "*")
public class SimplexController {

    @Autowired
    private SimplexService simplexService;

    @PostMapping("/solve")
    public SimplexResponse solve(@RequestBody SimplexRequest request) {
        return simplexService.solve(request);
    }
}