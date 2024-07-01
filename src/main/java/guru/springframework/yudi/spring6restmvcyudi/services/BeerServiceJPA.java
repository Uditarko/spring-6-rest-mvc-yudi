package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    /**
     * @param id
     * @return
     */
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        return Optional.ofNullable(beerMapper.beerToBeerDto(
                beerRepository.findById(id).orElse(null)));
    }

    /**
     * @return
     */
    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll().stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    /**
     * @param beerDTO
     * @return
     */
    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDto(
                beerRepository.save(beerMapper.beerDtoToBeer(beerDTO)));
    }

    /**
     * @param id
     * @param beerDTO
     * @return
     */
    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> returnBeerDto = new AtomicReference<>();
        beerRepository.findById(id).ifPresentOrElse(
                elem -> {elem.setBeerName(beerDTO.getBeerName());
                elem.setBeerStyle(beerDTO.getBeerStyle());
                elem.setPrice(beerDTO.getPrice());
                elem.setUpc(beerDTO.getUpc());
                elem.setQuantityOnHand(beerDTO.getQuantityOnHand());
                elem.setUpdateDate(LocalDateTime.now());
                Beer savedBeer = beerRepository.save(elem);
                returnBeerDto.set(Optional.of(beerMapper.beerToBeerDto(savedBeer)));}
        , () -> { returnBeerDto.set(Optional.empty());});

        return returnBeerDto.get();
    }

    /**
     * @param beerId
     */
    @Override
    public void deleteById(UUID beerId) {
        beerRepository.deleteById(beerId);
    }

    /**
     * @param beerId
     * @param beerDTO
     */
    @Override
    public void patchById(UUID beerId, BeerDTO beerDTO) {

    }
}
