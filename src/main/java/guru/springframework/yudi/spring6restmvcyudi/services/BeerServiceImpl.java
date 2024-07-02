package guru.springframework.yudi.spring6restmvcyudi.services;

import guru.springframework.yudi.spring6restmvcyudi.model.BeerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle.IPA;
import static guru.springframework.yudi.spring6restmvcyudi.model.BeerStyle.PALE_ALE;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beerDTO1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beerDTO3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beerDTO1.getId(), beerDTO1);
        beerMap.put(beerDTO2.getId(), beerDTO2);
        beerMap.put(beerDTO3.getId(), beerDTO3);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("In BeerServiceImpl --> getBeerById(UUID id)");
        return Optional.of(beerMap.get(id));
    }

    @Override
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = BeerDTO.builder()
                .id(UUID.randomUUID())
                .beerName(beerDTO.getBeerName())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerStyle(beerDTO.getBeerStyle())
                .version(1)
                .upc(beerDTO.getUpc())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .price(beerDTO.getPrice())
                .build();
        beerMap.put(savedBeerDTO.getId(), savedBeerDTO);
        return savedBeerDTO;
    }

    /**
     * @param id
     * @param beerDTO
     * @return
     */
    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDTO) {
        Optional<BeerDTO> existing = getBeerById(id);

        if(existing.isPresent()) {
            existing.get().setBeerName(beerDTO.getBeerName());
            existing.get().setBeerStyle(beerDTO.getBeerStyle());
            existing.get().setPrice(beerDTO.getPrice());
            existing.get().setUpc(beerDTO.getUpc());
            existing.get().setQuantityOnHand(beerDTO.getQuantityOnHand());
            existing.get().setVersion(existing.get().getVersion() + 1);
            existing.get().setUpdateDate(LocalDateTime.now());
            beerMap.put(existing.get().getId(), existing.get());
            return existing;
        }
        else{
            return Optional.empty();
        }
    }

    /**
     * @param beerId
     * @return
     */
    @Override
    public Boolean deleteById(UUID beerId) {
        beerMap.remove(beerId);
        return true;
    }

    /**
     * @param beerId
     * @param beerDTO
     * @return
     */
    @Override
    public Optional<BeerDTO> patchById(UUID beerId, BeerDTO beerDTO) {
        Optional<BeerDTO> existing = getBeerById(beerId);

        if(existing.isPresent()) {
            if (StringUtils.hasText(beerDTO.getBeerName())) {
                existing.get().setBeerName(beerDTO.getBeerName());
            }
            if (StringUtils.hasText(beerDTO.getUpc())) {
                existing.get().setUpc(beerDTO.getUpc());
            }
            if (beerDTO.getBeerStyle() != null) {
                existing.get().setBeerStyle(beerDTO.getBeerStyle());
            }
            if (beerDTO.getPrice() != null) {
                existing.get().setPrice(beerDTO.getPrice());
            }
            if (beerDTO.getQuantityOnHand() != null) {
                existing.get().setQuantityOnHand(beerDTO.getQuantityOnHand());
            }
        }
        return existing;
    }
}
