package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    Optional<BeerDTO> getBeerById(UUID id);

    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO);

    Boolean deleteById(UUID beerId);

    Optional<BeerDTO> patchById(UUID beerId, BeerDTO beerDTO);
}
