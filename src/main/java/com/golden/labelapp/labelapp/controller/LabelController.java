package com.golden.labelapp.labelapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.golden.labelapp.labelapp.dao.LabelServicesImpl;
import com.golden.labelapp.labelapp.dto.Labels;




@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelServicesImpl labelServicesImpl;

    @Transactional(readOnly = true)
    @GetMapping("/all")
    public List<Labels> getAllLabels() {
        return (List<Labels>) labelServicesImpl.getAllLabels();
    }

    @Transactional(readOnly = true)
    @GetMapping("/getid/{name}")
    public int getLabelByName(@PathVariable String name) {
        return labelServicesImpl.getLabelId(name);
    }
    
    
}
