package guru.springframework.yudi.spring6restmvcyudi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@ToString
public class BeerOrder {

    public BeerOrder(UUID id, Timestamp createdDate, String customerRef, Timestamp lastModifiedDate, Long version, Customer customer, Set<BeerOrderLine> beerOrderLines) {
        this.id = id;
        this.createdDate = createdDate;
        this.customerRef = customerRef;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
        this.setCustomer(customer);
        this.beerOrderLines = beerOrderLines;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @Size(max = 255)
    private String customerRef;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Version
    private Long version;

    @ToString.Exclude
    @ManyToOne
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this);
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLine> beerOrderLines;
}
