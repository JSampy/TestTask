package ru.sber.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.test.entity.Payment;
import ru.sber.test.service.TransferService;

import java.math.BigDecimal;

@RestController
@RequestMapping("transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "{from}/{to}/{sum}", method = RequestMethod.POST)
    public Payment transfer(
            @PathVariable("from") Long from,
            @PathVariable("to") Long to,
            @PathVariable("sum") BigDecimal sum
    ) {
        return transferService.transfer(
                from,
                to,
                sum
        ).orElseThrow(IllegalArgumentException::new);
    }

}