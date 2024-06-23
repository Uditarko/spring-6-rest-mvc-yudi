package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.Beer;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle.PALE_ALE;

@Service
public class BeerServiceImpl implements BeerService{
    @Override
    public Beer getBeerById(UUID id) {
        return Beer.builder()
                .id(id)
                .beerName("Galaxy Cat")
                .beerStyle(PALE_ALE)
                .version(1)
                .price(new BigDecimal("12.99"))
                .upc("123456")
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
