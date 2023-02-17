package com.hse.iphreactive.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("customer")
public class CustomerEntity implements Persistable {
    @Id
    private Long id;
    private String uri;//rename to path
    private String customerName;
    @Transient
    @JsonIgnore
    private boolean newProduct;

    @Override
    @JsonIgnore
    @Transient
    public boolean isNew() {
        return this.newProduct || id == null;
    }

    public CustomerEntity(String uri, String customerName) {
        this.uri = uri;
        this.customerName = customerName;
    }

    public CustomerEntity(Long id, String uri, String customerName) {
        this.id = id;
        this.uri = uri;
        this.customerName = customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerEntity that = (CustomerEntity) o;
        return Objects.equals(uri, that.uri) && Objects.equals(customerName, that.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, customerName);
    }


}
