package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        return null;
    }

    /**
     * @param id
     * @param beerDTO
     */
    @Override
    public void updateBeerById(UUID id, BeerDTO beerDTO) {

    }

    /**
     * @param beerId
     */
    @Override
    public void deleteById(UUID beerId) {

    }

    /**
     * @param beerId
     * @param beerDTO
     */
    @Override
    public void patchById(UUID beerId, BeerDTO beerDTO) {

    }
}
