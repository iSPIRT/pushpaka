package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.registry.utils.DaoException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

// FlightPlan is our model, which corresponds to the "FlightPlanes" database table.
@Entity(name = FlightPlan.PERSISTENCE_NAME)
@Table(name = FlightPlan.PERSISTENCE_NAME)
public class FlightPlan {
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
  public FlightPlan(UUID id, Uas u, Pilot p, OffsetDateTime st, OffsetDateTime et) {
    this.id = id;
    this.uas = u;
    this.pilot = p;
    this.startTime = st;
    this.endTime = et;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public FlightPlan() {}

  public static FlightPlan create(SessionFactory sf, FlightPlan a) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      Pilot pilot = Pilot.get(sf, a.getPilot().getId());
      Uas uas = Uas.get(sf, a.getUas().getId());
      t = s.beginTransaction();
      a.setPilot(pilot);
      a.setUas(uas);
      s.save(a);
      s.flush();
      t.commit();
      s.refresh(a);
      return a;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan create");
    } finally {
      s.close();
    }
  }

  public static List<FlightPlan> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<FlightPlan> fpl = s
        .createQuery("from FlightPlan", FlightPlan.class)
        .getResultList();
      t.commit();
      return fpl;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan getAll");
    } finally {
      s.close();
    }
  }

  public static FlightPlan get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      FlightPlan fp = s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return fp;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan get");
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
        .createQuery("delete from FlightPlan where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan delete");
    } finally {
      s.close();
    }
  }

  public static FlightPlan update(SessionFactory sf, UUID id, FlightPlan le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      FlightPlan leo = s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setParameter("id", id)
        .uniqueResult();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(leo);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan update");
    } finally {
      s.close();
    }
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
