package guru.springframework.yudi.spring6restmvcyudi.controllers;

import guru.springframework.yudi.spring6restmvcyudi.model.Beer;
import guru.springframework.yudi.spring6restmvcyudi.services.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class BeerController {

    private final BeerService beerService;

    public Beer getBeerById(UUID id){
        return beerService.getBeerById(id);
    }
}
