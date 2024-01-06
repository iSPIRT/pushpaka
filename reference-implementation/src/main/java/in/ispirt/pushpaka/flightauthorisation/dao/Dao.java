package in.ispirt.pushpaka.flightauthorisation.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Dao implements Serializable {
  private static final Random RAND = new Random();
  private static final boolean FORCE_RETRY = false;
  private static final String RETRY_SQL_STATE = "40001";
  private static final int MAX_ATTEMPT_COUNT = 6;

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

    // Convenience constructor.
    // public FlightPlan(
    //   UUID id
    // ) {
    //   this.id = id;
    // }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public FlightPlan() {}

    public static FlightPlan create(Session s, FlightPlan a) {
      Transaction t = s.beginTransaction();
      UUID aid = UUID.randomUUID();
      a.setId(aid);
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

    @Column(name = "flight_plan_id")
    public UUID flightPlanId;

    public UUID getFlightPlanId() {
      return flightPlanId;
    }

    public void setFlightPlanId(UUID flightPlanId) {
      this.flightPlanId = flightPlanId;
    }

    @Column(name = "pilot_id")
    public UUID pilotId;

    public UUID getPilotId() {
      return pilotId;
    }

    public void setPilotId(UUID pilotId) {
      this.pilotId = pilotId;
    }

    @Column(name = "uas_id")
    public UUID uasId;

    public UUID getUasId() {
      return uasId;
    }

    public void setUasId(UUID id) {
      this.uasId = id;
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
