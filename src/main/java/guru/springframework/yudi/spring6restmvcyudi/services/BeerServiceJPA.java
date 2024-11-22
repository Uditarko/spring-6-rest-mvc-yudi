package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public static final String DB_WILDCARD = "%";
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
    public List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle) {
        List<Beer> beers;
        if (StringUtils.hasText(beerName)) {
            beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase(DB_WILDCARD + beerName + DB_WILDCARD);
        } else if (beerStyle != null) {
            beers = beerRepository.findAllByBeerStyle(beerStyle);
        } else {
            beers = beerRepository.findAll();
        }
        return beers.stream()
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
                elem -> {
                    elem.setBeerName(beerDTO.getBeerName());
                    elem.setBeerStyle(beerDTO.getBeerStyle());
                    elem.setPrice(beerDTO.getPrice());
                    elem.setUpc(beerDTO.getUpc());
                    elem.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    elem.setUpdateDate(LocalDateTime.now());
                    Beer savedBeer = beerRepository.save(elem);
                    returnBeerDto.set(Optional.of(beerMapper.beerToBeerDto(savedBeer)));
                }
                , () -> {
                    returnBeerDto.set(Optional.empty());
                });

        return returnBeerDto.get();
    }

    /**
     * @param beerId
     * @return
     */
    @Override
    public Boolean deleteById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param beerId
     * @param beerDTO
     * @return
     */
    @Override
    public Optional<BeerDTO> patchById(UUID beerId, BeerDTO beerDTO) {
        AtomicReference<Optional<BeerDTO>> returnBeerDto = new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(elem -> {
                    if (StringUtils.hasText(beerDTO.getBeerName())) {
                        elem.setBeerName(beerDTO.getBeerName());
                    }
                    if (StringUtils.hasText(beerDTO.getUpc())) {
                        elem.setUpc(beerDTO.getUpc());
                    }
                    if (beerDTO.getBeerStyle() != null) {
                        elem.setBeerStyle(beerDTO.getBeerStyle());
                    }
                    if (beerDTO.getPrice() != null) {
                        elem.setPrice(beerDTO.getPrice());
                    }
                    if (beerDTO.getQuantityOnHand() != null) {
                        elem.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    }
                    returnBeerDto.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(elem))));
                }, () -> {
                    returnBeerDto.set(Optional.empty());
                }
        );
        return returnBeerDto.get();
    }

}
