package guru.springframework.yudi.spring6restmvcyudi.repositories;

import guru.springframework.yudi.spring6restmvcyudi.entities.Beer;
import guru.springframework.yudi.spring6restmvcyudi.entities.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Beer testBeer;

    @BeforeEach
    void setUp(){
        testBeer = beerRepository.findAll().getFirst();
    }

    @Transactional
    @Test
    void testAddingCategory(){
        Category category = Category.builder().description("Test Category").build();
        Category savedCategory = categoryRepository.save(category);
        testBeer.addCategory(savedCategory);
        Beer savedBeer = beerRepository.save(testBeer);

        System.out.println(savedBeer);
        savedBeer.getCategories().forEach(category1 -> System.out.println(category1.getBeers()));
        assertThat(savedBeer.getCategories().size()).isGreaterThanOrEqualTo(1);
        assertThat(savedBeer.getCategories().stream().noneMatch(category1 -> category1.getBeers().isEmpty())).isTrue();
    }
}