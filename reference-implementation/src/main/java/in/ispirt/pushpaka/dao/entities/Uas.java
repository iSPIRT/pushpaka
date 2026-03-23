package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.models.UasStatus;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.utils.Logging;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

// Uas is our model, which corresponds to the "uas_types" database table.
@Entity(name = Uas.PERSISTENCE_NAME)
@Table(name = Uas.PERSISTENCE_NAME)
public class Uas {
  static final String PERSISTENCE_NAME = "Uas";

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
  @ManyToOne
  @JoinColumn(name = "FK_uas_type")
  // @Column(name = "uas_type")
  public UasType uasType;

  public UasType getUasType() {
    return uasType;
  }

  public void setUasType(UasType ut) {
    this.uasType = ut;
  }

  @NotNull
  @Column(name = "oem_serial_no", unique = true)
  public Integer oemSerialNo;

  public Integer getOemSerialNo() {
    return oemSerialNo;
  }

  public void setOemSerialNo(Integer c) {
    this.oemSerialNo = c;
  }

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 16, name = "status")
  private UasStatus status;

  public UasStatus getStatus() {
    return this.status;
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

  @Column(name = "human_readable_id")
  private String humanReadableId;

  public String getHumanReadableId() {
    return humanReadableId;
  }

  public void setHumanReadableId(SessionFactory sf) throws DaoException {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%03x", this.uasType.getModelNumber()).toUpperCase());
    sb.append(this.uasType.getOperationCategory().getValue());
    sb.append(String.format("%04x", this.getOemSerialNo()).toUpperCase());
    List<Sale> sales = Sale.getAll(sf, this.id);
    Integer holdings = 0;
    Integer nonholdings = 0;
    for (Sale t : sales) {
      // Logging.info("Sale: " + t.toString());
      if (t.getHolding()) {
        holdings += 1;
      } else {
        nonholdings += 1;
      }
    }
    sb.append(String.format("%02x", holdings).toUpperCase());
    sb.append(String.format("%02x", nonholdings).toUpperCase());
    this.humanReadableId = sb.toString();
  }

  // Convenience constructor.
  public Uas(
    UUID id,
    UasType ut,
    Integer oemSerialNo,
    UasStatus s,
    OffsetDateTime tc,
    OffsetDateTime tu,
    String hrid
  ) {
    this.id = id;
    this.uasType = ut;
    this.oemSerialNo = oemSerialNo;
    this.status = s;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
    this.humanReadableId = hrid;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public Uas() {}

  public static Uas create(SessionFactory sf, Uas m) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      UasType le = UasType.get(sf, m.getUasType().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "UasType");
      }
      t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setTimestampCreated(n);
      m.setTimestampUpdated(n);
      m.setUasType(le);
      m.setHumanReadableId(sf);
      Logging.info("DAO UAS Type for UAS create: " + le.toString());
      Logging.info("DAO UAS Create: " + m.getHumanReadableId());
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Uas create");
    } finally {
      s.close();
    }
  }

  public static List<Uas> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<Uas> ul = s.createQuery("from Uas", Uas.class).getResultList();
      t.commit();
      return ul;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Uas getAll");
    } finally {
      s.close();
    }
  }

  public static Uas get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Uas u = s
        .createQuery("from Uas where id= :id", Uas.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return u;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Uas get");
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
        .createQuery("delete from Uas where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Uas delete");
    } finally {
      s.close();
    }
  }

  public static Uas update(SessionFactory sf, UUID id, Uas le) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Uas leo = s
        .createQuery("from Uas where id= :id", Uas.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      UasType a = leo.getUasType();
      UasType ao = le.getUasType();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setUasType(ao);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Uas update");
    } finally {
      s.close();
    }
  }
}
