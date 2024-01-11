package in.ispirt.pushpaka.flightauthorisation.dao;

import in.ispirt.pushpaka.registry.dao.Dao.Pilot;
import in.ispirt.pushpaka.registry.dao.Dao.Uas;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Dao implements Serializable {
  private static final long serialVersionUID = 1L;

  // FlightPlan is our model, which corresponds to the "FlightPlanes" database table.
  @Entity(name = FlightPlan.PERSISTENCE_NAME)
  @Table(name = FlightPlan.PERSISTENCE_NAME)
  public static class FlightPlan {
    static final String PERSISTENCE_NAME = "FlightPlan";

    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public FlightPlan(UUID id) {
      this.id = id;
    }

    @NotNull
    @Column(name = "operation_category")
    // @Column(name = "pilot_id")
    public OperationCategory operationCategory;

    public OperationCategory getOperationCategory() {
      return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
      this.operationCategory = operationCategory;
    }

    @NotNull
    @Column(name = "start_time")
    // @Column(name = "uas_id")
    public OffsetDateTime startTime;

    public OffsetDateTime getStartTime() {
      return startTime;
    }

    public void setStartTime(OffsetDateTime id) {
      this.startTime = id;
    }

    @NotNull
    @Column(name = "end_time")
    // @Column(name = "uas_id")
    public OffsetDateTime endTime;

    public OffsetDateTime getEndTime() {
      return endTime;
    }

    public void setEndTime(OffsetDateTime id) {
      this.endTime = id;
    }

    @ManyToOne
    @JoinColumn(name = "FK_pilot")
    // @Column(name = "pilot_id")
    public Pilot pilot;

    public Pilot getPilot() {
      return pilot;
    }

    public void setPilot(Pilot pilot) {
      this.pilot = pilot;
    }

    @ManyToOne
    @JoinColumn(name = "FK_uas")
    // @Column(name = "uas_id")
    public Uas uas;

    public Uas getUas() {
      return uas;
    }

    public void setUas(Uas id) {
      this.uas = id;
    }

    //
    // Convenience constructor.
    public FlightPlan(
      UUID id,
      Uas u,
      Pilot p,
      OffsetDateTime st,
      OffsetDateTime et,
      OperationCategory c
    ) {
      this.id = id;
      this.uas = u;
      this.pilot = p;
      this.startTime = st;
      this.endTime = et;
      this.operationCategory = c;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public FlightPlan() {}

    public static FlightPlan create(Session s, FlightPlan a) {
      Pilot pilot = Pilot.get(s, a.getPilot().getId());
      Uas uas = Uas.get(s, a.getUas().getId());
      Transaction t = s.beginTransaction();
      a.setPilot(pilot);
      a.setUas(uas);
      s.save(a);
      s.flush();
      t.commit();
      s.refresh(a);
      return a;
    }

    public static List<FlightPlan> getAll(Session s) {
      return s.createQuery("from FlightPlan", FlightPlan.class).getResultList();
    }

    public static FlightPlan get(Session s, UUID id) {
      return s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from FlightPlan where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static FlightPlan update(Session s, UUID id, FlightPlan le) {
      FlightPlan leo = s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setParameter("id", id)
        .uniqueResult();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(leo);
      return leo;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("class FlightPlan {\n");
      sb.append("    id: ").append(id.toString()).append("\n");
      sb.append("}");
      return sb.toString();
    }
  }

  // AirspaceUsageToken is our model, which corresponds to the "AirspaceUsageTokenes" database table.
  @Entity(name = AirspaceUsageToken.PERSISTENCE_NAME)
  @Table(name = AirspaceUsageToken.PERSISTENCE_NAME)
  public static class AirspaceUsageToken {
    static final String PERSISTENCE_NAME = "AirspaceUsageToken";

    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public AirspaceUsageToken(UUID id) {
      this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "FK_flight_plan")
    // @Column(name = "flight_plan_id")
    public FlightPlan flightPlan;

    public FlightPlan getFlightPlan() {
      return flightPlan;
    }

    public void setFlightPlan(FlightPlan flightPlan) {
      this.flightPlan = flightPlan;
    }

    @ManyToOne
    @JoinColumn(name = "FK_pilot")
    // @Column(name = "pilot_id")
    public Pilot pilot;

    public Pilot getPilot() {
      return pilot;
    }

    public void setPilot(Pilot pilot) {
      this.pilot = pilot;
    }

    @ManyToOne
    @JoinColumn(name = "FK_uas")
    // @Column(name = "uas_id")
    public Uas uas;

    public Uas getUas() {
      return uas;
    }

    public void setUas(Uas id) {
      this.uas = id;
    }

    // Convenience constructor.
    // public AirspaceUsageToken(
    //   UUID id
    // ) {
    //   this.id = id;
    // }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public AirspaceUsageToken() {}

    public static AirspaceUsageToken create(Session s, AirspaceUsageToken a) {
      Transaction t = s.beginTransaction();
      UUID aid = UUID.randomUUID();
      a.setId(aid);
      s.save(a);
      s.flush();
      t.commit();
      s.refresh(a);
      return a;
    }

    public static List<AirspaceUsageToken> getAll(Session s) {
      return s
        .createQuery("from AirspaceUsageToken", AirspaceUsageToken.class)
        .getResultList();
    }

    public static AirspaceUsageToken get(Session s, UUID id) {
      return s
        .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from AirspaceUsageToken where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static AirspaceUsageToken update(Session s, UUID id, AirspaceUsageToken le) {
      AirspaceUsageToken leo = s
        .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
        .setParameter("id", id)
        .uniqueResult();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(leo);
      return leo;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("class AirspaceUsageToken {\n");
      sb.append("    id: ").append(id.toString()).append("\n");
      sb.append("}");
      return sb.toString();
    }
  }
}
