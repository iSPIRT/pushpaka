package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.models.Country;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

// Civil Aviation Authority is our model, which corresponds to the "civil_aviation_authorities" database table.
@Entity(name = CivilAviationAuthority.PERSISTENCE_NAME)
@Table(name = CivilAviationAuthority.PERSISTENCE_NAME)
public class CivilAviationAuthority {
  static final String PERSISTENCE_NAME = "CivilAviationAuthority";

  @Id
  @Column(name = "id")
  public UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 3, name = "country", unique = true)
  public Country country;

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country a) {
    this.country = a;
  }

  // Convenience constructor.
  public CivilAviationAuthority(
    UUID id,
    LegalEntity legalEntity,
    OffsetDateTime tc,
    OffsetDateTime tu,
    OffsetDateTime vs,
    OffsetDateTime ve,
    Country c
  ) {
    this.id = id;
    this.legalEntity = legalEntity;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
    this.validityStart = vs;
    this.validityEnd = ve;
    this.country = c;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public CivilAviationAuthority() {}

  public static CivilAviationAuthority create(
    SessionFactory sf,
    CivilAviationAuthority m
  )
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
      if (
        e
          .getCause()
          .getClass()
          .getName()
          .equals("org.hibernate.exception.ConstraintViolationException")
      ) {
        throw new DaoException(
          DaoException.Code.CONSTRAINT_VIOLATION,
          "CivilAviationAuthority"
        );
      } else throw e;
    } finally {
      s.close();
    }
  }

  public static List<CivilAviationAuthority> getAll(SessionFactory sf)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<CivilAviationAuthority> caal = s
        .createQuery("from CivilAviationAuthority", CivilAviationAuthority.class)
        .getResultList();
      t.commit();
      return caal;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(
        DaoException.Code.UNKNOWN,
        "CivilAviationAuthority getAll"
      );
    } finally {
      s.close();
    }
  }

  public static CivilAviationAuthority get(SessionFactory sf, UUID id)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      CivilAviationAuthority caa = s
        .createQuery(
          "from CivilAviationAuthority where id= :id",
          CivilAviationAuthority.class
        )
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return caa;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "CivilAviationAuthority get");
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
        .createQuery("delete from CivilAviationAuthority where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(
        DaoException.Code.UNKNOWN,
        "CivilAviationAuthority delete"
      );
    } finally {
      s.close();
    }
  }

  public static CivilAviationAuthority update(
    SessionFactory sf,
    UUID id,
    CivilAviationAuthority le
  )
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      CivilAviationAuthority leo = s
        .createQuery(
          "from CivilAviationAuthority where id= :id",
          CivilAviationAuthority.class
        )
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
      throw new DaoException(
        DaoException.Code.UNKNOWN,
        "CivilAviationAuthority update"
      );
    } finally {
      s.close();
    }
  }
}
