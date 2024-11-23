package guru.springframework.yudi.spring6restmvcyudi.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, columnDefinition = "varchar(36)", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    UUID id;

    @Column(length = 50, columnDefinition = "varchar(50)")
    String description;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Version
    private Long version;

    @Builder.Default
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "beer_category",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "beer_id"))
    private Set<Beer> beers = new HashSet<>();

//    public void addBeer(Beer beer){
//        this.beers.add(beer);
//        beer.getCategories().add(this);
//    }
//
//    public void removeCategory(Beer beer){
//        this.beers.remove(beer);
//        beer.getCategories().remove(this);
//    }

}
