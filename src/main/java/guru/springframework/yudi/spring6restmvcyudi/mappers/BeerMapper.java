package guru.springframework.yudi.spring6restmvcyudi.mappers;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO dto);
    BeerDTO beerToBeerDto(Beer beer);
}
