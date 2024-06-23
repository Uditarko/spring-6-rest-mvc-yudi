package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.model.Beer;
import guru.springframework.yudi.spring6restmvcyudi.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class BeerController {

    private final BeerService beerService;

    public Beer getBeerById(UUID id){
        log.debug("In BeerController --> getBeerById(UUID id)");
        return beerService.getBeerById(id);
    }
}
