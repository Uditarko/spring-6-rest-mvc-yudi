package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.mappers.BeerMapper;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle;
import guru.springframework.yudi.spring6restmvcyudi.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;


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
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Beer> beerPage;
        if (StringUtils.hasText(beerName) && beerStyle==null) {
            beerPage = beerRepository.findAllByBeerNameIsLikeIgnoreCase(DB_WILDCARD + beerName + DB_WILDCARD, pageRequest);
        } else if (beerStyle != null && !StringUtils.hasText(beerName)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle!=null) {
            beerPage = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(DB_WILDCARD + beerName + DB_WILDCARD, beerStyle, pageRequest);
        } else {
            beerPage = new PageImpl<>(beerRepository.findAll());
        }
        if(showInventory == null || !showInventory){
            beerPage.getContent().forEach(beer -> beer.setQuantityOnHand(null));
        }
        return beerPage.map(beerMapper::beerToBeerDto);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        return PageRequest.of(queryPageNumber, queryPageSize);

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
