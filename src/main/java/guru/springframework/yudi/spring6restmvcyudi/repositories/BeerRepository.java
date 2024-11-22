package guru.springframework.yudi.spring6restmvcyudi.repositories;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    @Query
    List<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName);

    @Query
    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);
}
