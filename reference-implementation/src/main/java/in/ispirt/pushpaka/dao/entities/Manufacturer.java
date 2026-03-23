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

// Manufacturer is our model, which corresponds to the "uas_types" database table.
@Entity(name = Manufacturer.PERSISTENCE_NAME)
@Table(name = Manufacturer.PERSISTENCE_NAME)
public class Manufacturer {
  static final String PERSISTENCE_NAME = "Manufacturer";

  @Id
  @Column(name = "id")
  public UUID id;

  public Manufacturer setId(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getId() {
    return id;
  }

  @NotNull
  @OneToOne
  @JoinColumn(name = "FK_legal_entity")
  // @Column(name = "legal_entity")
  public LegalEntity legalEntity;

  public LegalEntity getLegalEntity() {
    return legalEntity;
  }

  public void setLegalEntity(LegalEntity nlegalEntity) {
    this.legalEntity = nlegalEntity;
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
  public Manufacturer(
    UUID id,
    LegalEntity legalEntity,
    OffsetDateTime tc,
    OffsetDateTime tu,
    OffsetDateTime vs,
    OffsetDateTime ve
  ) {
    this.id = id;
    this.legalEntity = legalEntity;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
    this.validityStart = vs;
    this.validityEnd = ve;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public Manufacturer() {}

  public static Manufacturer create(SessionFactory sf, Manufacturer m)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      LegalEntity le = LegalEntity.get(sf, m.getLegalEntity().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "LegalEntity");
      }
      t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setTimestampCreated(n);
      m.setTimestampUpdated(n);
      m.setLegalEntity(le);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer create");
    } finally {
      s.close();
    }
  }

  public static List<Manufacturer> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<Manufacturer> ms = s
        .createQuery("from Manufacturer", Manufacturer.class)
        .getResultList();
      t.commit();
      return ms;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer getAll");
    } finally {
      s.close();
    }
  }

  public static Manufacturer get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Manufacturer m = s
        .createQuery("from Manufacturer where id= :id", Manufacturer.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return m;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer get");
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
        .createQuery("delete from Manufacturer where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer delete");
    } finally {
      s.close();
    }
  }

  public static Manufacturer update(SessionFactory sf, UUID id, Manufacturer le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Manufacturer leo = s
        .createQuery("from Manufacturer where id= :id", Manufacturer.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      leo.setValidityStart(le.getValidityStart());
      leo.setValidityEnd(le.getValidityEnd());
      LegalEntity a = leo.getLegalEntity();
      LegalEntity ao = le.getLegalEntity();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setLegalEntity(ao);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer update");
    } finally {
      s.close();
    }
  }
}
