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

// Operator is our model, which corresponds to the "operators" database table.
@Entity(name = Operator.PERSISTENCE_NAME)
@Table(name = Operator.PERSISTENCE_NAME)
public class Operator {
  static final String PERSISTENCE_NAME = "Operator";

  @Id
  @Column(name = "id")
  public UUID id;

  public UUID getId() {
    return id;
  }

  public Operator setId(UUID id) {
    this.id = id;
    return this;
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
  public Operator(
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
  public Operator() {}

  public static Operator create(SessionFactory sf, Operator m) throws DaoException {
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
      throw new DaoException(DaoException.Code.UNKNOWN, "Operator create");
    } finally {
      s.close();
    }
  }

  public static List<Operator> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      return s.createQuery("from Operator", Operator.class).getResultList();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Operator getAll");
    } finally {
      s.close();
    }
  }

  public static Operator get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Operator o = s
        .createQuery("from Operator where id= :id", Operator.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return o;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Operator get");
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
        .createQuery("delete from Operator where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Operator delete");
    } finally {
      s.close();
    }
  }

  public static Operator update(SessionFactory sf, UUID id, Operator le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Operator leo = s
        .createQuery("from Operator where id= :id", Operator.class)
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
      throw new DaoException(DaoException.Code.UNKNOWN, "Operator update");
    } finally {
      s.close();
    }
  }
}
