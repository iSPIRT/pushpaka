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

// LegalEntity is our model, which corresponds to the "legal_entities" database table.
@Entity(name = LegalEntity.PERSISTENCE_NAME)
@Table(name = LegalEntity.PERSISTENCE_NAME)
public class LegalEntity {
  static final String PERSISTENCE_NAME = "LegalEntity";

  @Id
  @Column(name = "id")
  public UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @Column(name = "cin", unique = true)
  public String cin;

  public String getCin() {
    return cin;
  }

  public void setCin(String c) {
    this.cin = c;
  }

  @NotNull
  @Column(name = "name")
  public String name;

  public String getName() {
    return name;
  }

  public void setName(String newname) {
    this.name = newname;
  }

  @Column(name = "gstin", unique = true)
  public String gstin;

  public String getGstin() {
    return gstin;
  }

  public void setGstin(String a) {
    this.gstin = a;
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

  @NotNull
  @OneToOne
  @JoinColumn(name = "FK_address")
  // @Column(name = "name")
  public Address address;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address a) {
    this.address = a;
  }

  // Convenience constructor.
  public LegalEntity(
    UUID id,
    String name,
    Address a,
    String cin,
    String gstin,
    OffsetDateTime tc,
    OffsetDateTime tu
  ) {
    this.id = id;
    this.name = name;
    this.address = a;
    this.cin = cin;
    this.gstin = gstin;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public LegalEntity() {}

  public static LegalEntity create(SessionFactory sf, LegalEntity le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      Address aa = Address.create(sf, le.getAddress());
      t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      le.setId(UUID.randomUUID());
      le.setTimestampCreated(n);
      le.setTimestampUpdated(n);
      le.setAddress(aa);
      s.save(le);
      s.flush();
      t.commit();
      s.refresh(le);
      return le;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "LegalEntity create");
    } finally {
      s.close();
    }
  }

  public static List<LegalEntity> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<LegalEntity> les = s
        .createQuery("from LegalEntity", LegalEntity.class)
        .getResultList();
      t.commit();
      return les;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "LegalEntity getAll");
    } finally {
      s.close();
    }
  }

  public static LegalEntity get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      LegalEntity le = s
        .createQuery("from LegalEntity where id= :id", LegalEntity.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return le;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "LegalEntity get");
    } finally {
      s.close();
    }
  }

  public static void delete(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      LegalEntity le = s
        .createQuery("from LegalEntity where id= :id", LegalEntity.class)
        .setParameter("id", id)
        .uniqueResult();
      s
        .createQuery("delete from Address where id= :id")
        .setParameter("id", le.getAddress().getId())
        .executeUpdate();
      s
        .createQuery("delete from LegalEntity where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "LegalEntity delete");
    } finally {
      s.close();
    }
  }

  public static LegalEntity update(SessionFactory sf, UUID id, LegalEntity le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      LegalEntity leo = s
        .createQuery("from LegalEntity where id= :id", LegalEntity.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setName(le.getName());
      leo.setCin(le.getCin());
      leo.setGstin(le.getGstin());
      leo.setTimestampUpdated(OffsetDateTime.now());
      Address a = leo.getAddress();
      Address ao = le.getAddress();
      ao.setLine1(a.getLine1());
      ao.setLine2(a.getLine2());
      ao.setLine3(a.getLine3());
      ao.setCity(a.getCity());
      ao.setPinCode(a.getPinCode());
      ao.setState(a.getState());
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setAddress(ao);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "LegalEntity update");
    } finally {
      s.close();
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LegalEntity: {");
    sb.append("  address: ").append(address.toString()).append("\n");
    sb.append("  cin: ").append(cin).append("\n");
    sb.append("  gstin: ").append(gstin).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
