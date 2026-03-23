package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.registry.utils.DaoException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

// Pilot is our model, which corresponds to the "pilots" database table.
@Entity(name = Pilot.PERSISTENCE_NAME)
@Table(name = Pilot.PERSISTENCE_NAME)
public class Pilot {
  static final String PERSISTENCE_NAME = "Pilot";

  @Id
  @Column(name = "id")
  private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Pilot(UUID id) {
    this.id = id;
  }

  @NotNull
  @OneToOne
  @JoinColumn(name = "FK_user")
  // @Column(name = "balance")
  public Person user;

  public Person getUser() {
    return user;
  }

  public void setUser(Person m) {
    this.user = m;
  }

  @NotNull
  @Column(name = "timestamp_created")
  public OffsetDateTime timestampCreated;

  public OffsetDateTime getTimestampCreated() {
    return timestampCreated;
  }

  public void setTimestampCreated(OffsetDateTime a) {
    this.timestampCreated = a;
  }

  @NotNull
  @Column(name = "timestamp_updated")
  public OffsetDateTime timestampUpdated;

  public OffsetDateTime getTimestampUpdated() {
    return timestampUpdated;
  }

  public void setTimestampUpdated(OffsetDateTime a) {
    this.timestampUpdated = a;
  }

  @Column(name = "validity_start")
  public OffsetDateTime validityStart;

  public OffsetDateTime getValidityStart() {
    return validityStart;
  }

  public void setValidityStart(OffsetDateTime a) {
    this.validityStart = a;
  }

  @Column(name = "validity_end")
  public OffsetDateTime validityEnd;

  public OffsetDateTime getValidityEnd() {
    return validityEnd;
  }

  public void setValidityEnd(OffsetDateTime a) {
    this.validityEnd = a;
  }

  // Convenience constructor.
  public Pilot(
    UUID id,
    Person u,
    OffsetDateTime tc,
    OffsetDateTime tu,
    OffsetDateTime vs,
    OffsetDateTime ve
  ) {
    this.id = id;
    this.user = u;
    this.validityStart = vs;
    this.validityEnd = ve;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public Pilot() {}

  public static Pilot create(SessionFactory sf, Pilot le) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      Person aa = Person.get(sf, le.getUser().getId());
      if (aa == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "User");
      }
      t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      le.setId(UUID.randomUUID());
      le.setUser(aa);
      le.setTimestampCreated(n);
      le.setTimestampUpdated(n);
      s.save(le);
      s.flush();
      t.commit();
      s.refresh(le);
      return le;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Pilot create");
    } finally {
      s.close();
    }
  }

  public static List<Pilot> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<Pilot> p = s.createQuery("from Pilot", Pilot.class).getResultList();
      t.commit();
      return p;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Pilot getAll");
    } finally {
      s.close();
    }
  }

  public static Pilot get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Pilot p = s
        .createQuery("from Pilot where id= :id", Pilot.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return p;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Pilot get");
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
        .createQuery("delete from Pilot where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Pilot delete");
    } finally {
      s.close();
    }
  }

  public static Pilot update(SessionFactory sf, UUID id, Pilot le) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Pilot leo = s
        .createQuery("from Pilot where id= :id", Pilot.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      s.saveOrUpdate(leo);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Pilot update");
    } finally {
      s.close();
    }
  }
}
