package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Mission.
 */
@Entity
@Table(name = "mission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @Column(name = "date_started")
    private ZonedDateTime dateStarted;

    @Column(name = "date_completed")
    private ZonedDateTime dateCompleted;

    @ManyToOne
    private MissionStatus currentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(ZonedDateTime dateStarted) {
        this.dateStarted = dateStarted;
    }

    public ZonedDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(ZonedDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public MissionStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(MissionStatus missionStatus) {
        this.currentStatus = missionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mission mission = (Mission) o;
        if(mission.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mission.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Mission{" +
            "id=" + id +
            ", isAccepted='" + isAccepted + "'" +
            ", name='" + name + "'" +
            ", notes='" + notes + "'" +
            ", dateStarted='" + dateStarted + "'" +
            ", dateCompleted='" + dateCompleted + "'" +
            '}';
    }
}
