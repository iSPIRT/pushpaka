package in.ispirt.pushpaka.flightauthorisation.dao;

import in.ispirt.pushpaka.flightauthorisation.models.FlightPlan;
import in.ispirt.pushpaka.flightauthorisation.utils.DaoException;
import in.ispirt.pushpaka.flightauthorisation.utils.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from FlightPlan where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static FlightPlan update(Session s, UUID id, FlightPlan le) {
      FlightPlan leo = s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setString("id", id.toString())
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
}
