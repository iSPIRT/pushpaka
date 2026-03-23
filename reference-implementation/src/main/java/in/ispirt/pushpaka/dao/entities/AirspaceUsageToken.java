package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.models.OperationCategory;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.utils.Logging;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

// AirspaceUsageToken is our model, which corresponds to the "AirspaceUsageTokenes" database table.
@Entity(name = AirspaceUsageToken.PERSISTENCE_NAME)
@Table(name = AirspaceUsageToken.PERSISTENCE_NAME)
public class AirspaceUsageToken {
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

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public AirspaceUsageToken() {}

  public static AirspaceUsageToken create(SessionFactory sf, AirspaceUsageToken a)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      Logging.info("AUT CREATE OperationCategory: " + a.getId());
      t = s.beginTransaction();
      if (a.getUas().getUasType().getOperationCategory() == OperationCategory.C) {
        FlightPlan fp = FlightPlan.get(sf, a.getFlightPlan().getId());
        a.setFlightPlan(fp);
        s.save(a);
        s.flush();
        t.commit();
        s.refresh(a);
      } else {
        Logging.info("AUT CREATE Pilot " + a.getPilot().getId());
        Logging.info("AUT CREATE Uas " + a.getUas().getId());
        Pilot pilot = Pilot.get(sf, a.getPilot().getId());
        Uas uas = Uas.get(sf, a.getUas().getId());
        a.setPilot(pilot);
        a.setUas(uas);
        s.save(a);
        s.flush();
        t.commit();
        s.refresh(a);
      }
      return a;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken create");
    } finally {
      s.close();
    }
  }

  public static List<AirspaceUsageToken> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<AirspaceUsageToken> auts = s
        .createQuery("from AirspaceUsageToken", AirspaceUsageToken.class)
        .getResultList();
      t.commit();
      return auts;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken getAll");
    } finally {
      s.close();
    }
  }

  public static AirspaceUsageToken get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      AirspaceUsageToken aut = s
        .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return aut;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken get");
    } finally {
      s.close();
    }
  }

  public static void delete(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      s
        .createQuery("delete from AirspaceUsageToken where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken delete");
    } finally {
      s.close();
    }
  }

  public static AirspaceUsageToken update(
    SessionFactory sf,
    UUID id,
    AirspaceUsageToken le
  )
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      AirspaceUsageToken leo = s
        .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
        .setParameter("id", id)
        .uniqueResult();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(leo);
      return leo;
    } catch (Exception e) {
      throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken update");
    } finally {
      s.close();
    }
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
