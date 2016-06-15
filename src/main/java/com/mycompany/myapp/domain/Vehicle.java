package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_last_accessed")
    private ZonedDateTime dateLastAccessed;

    @ManyToOne
    private Unit unit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getDateLastAccessed() {
        return dateLastAccessed;
    }

    public void setDateLastAccessed(ZonedDateTime dateLastAccessed) {
        this.dateLastAccessed = dateLastAccessed;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vehicle vehicle = (Vehicle) o;
        if(vehicle.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + id +
            ", brand='" + brand + "'" +
            ", licensePlate='" + licensePlate + "'" +
            ", vehicleType='" + vehicleType + "'" +
            ", dateCreated='" + dateCreated + "'" +
            ", dateLastAccessed='" + dateLastAccessed + "'" +
            '}';
    }
}
