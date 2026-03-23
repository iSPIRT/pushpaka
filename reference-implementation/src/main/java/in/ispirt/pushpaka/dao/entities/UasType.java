package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.models.OperationCategory;
import in.ispirt.pushpaka.models.UasPropulsionCategory;
import in.ispirt.pushpaka.models.UasWeightCategory;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.registry.utils.Utils;
import java.net.URL;
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

// UasType is our model, which corresponds to the "uas_types" database table.
@Entity(name = UasType.PERSISTENCE_NAME)
@Table(name = UasType.PERSISTENCE_NAME)
public class UasType {
  static final String PERSISTENCE_NAME = "UasType";

  @Id
  @Column(name = "id")
  public UUID id;

  public UUID getId() {
    return id;
  }

  public UasType setId(UUID id) {
    this.id = id;
    return this;
  }

  @NotNull
  @ManyToOne
  @JoinColumn(name = "FK_manufacturer")
  // @Column(name = "balance")
  public Manufacturer manufacturer;

  public Manufacturer getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(Manufacturer m) {
    this.manufacturer = m;
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

  @Column(name = "model_number")
  public Integer modelNumber;

  public Integer getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(Integer c) {
    this.modelNumber = c;
  }

  @NotNull
  @Column(length = 1024, name = "photo_url")
  public URL photoUrl;

  public URL getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(URL c) {
    this.photoUrl = c;
  }

  @NotNull
  @Column(name = "mtow")
  public Float mtow;

  public Float getMtow() {
    return mtow;
  }

  public void setMtow(Float c) {
    this.mtow = c;
  }

  @NotNull
  @Column(name = "approved")
  public Boolean approved;

  public Boolean getApproved() {
    return approved;
  }

  public void setApproved(Boolean c) {
    this.approved = c;
  }

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 16, name = "propulsion_category")
  private UasPropulsionCategory propulsionCategory;

  public UasPropulsionCategory getPropulsionCategory() {
    return this.propulsionCategory;
  }

  public UasWeightCategory getWeightCategory() {
    return Utils.toOpenApiWeightCategory(this.mtow);
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_category")
  private OperationCategory operationCategory;

  public OperationCategory getOperationCategory() {
    return this.operationCategory;
  }

  public void setOperationCategory(OperationCategory o) {
    this.operationCategory = o;
  }

  // Convenience constructor.
  public UasType(
    UUID id,
    Manufacturer manufacturer,
    Integer modelNumber,
    URL photoUrl,
    Float mtow,
    OffsetDateTime tc,
    OffsetDateTime tu,
    UasPropulsionCategory pc,
    OperationCategory oc,
    Boolean approved
  ) {
    this.id = id;
    this.manufacturer = manufacturer;
    this.modelNumber = modelNumber;
    this.photoUrl = photoUrl;
    this.mtow = mtow;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
    this.propulsionCategory = pc;
    this.operationCategory = oc;
    this.approved = approved;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public UasType() {}

  public static UasType create(SessionFactory sf, UasType m) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      Manufacturer le = Manufacturer.get(sf, m.getManufacturer().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "Manufacturer");
      }
      t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setTimestampCreated(n);
      m.setTimestampUpdated(n);
      m.setManufacturer(le);
      m.setApproved(false);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType create");
    } finally {
      s.close();
    }
  }

  public static UasType approve(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      UasType uu = s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
      if (uu == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "UasType");
      }
      uu.setApproved(true);
      s.save(uu);
      s.flush();
      t.commit();
      s.refresh(uu);
      return uu;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType approve");
    } finally {
      s.close();
    }
  }

  public static UasType setModelNumber(SessionFactory sf, UUID id, Integer modelNumber)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      UasType uu = s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
      if (uu == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "UasType");
      }
      uu.setModelNumber(modelNumber);
      s.save(uu);
      s.flush();
      t.commit();
      s.refresh(uu);
      return uu;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType setModelNumber");
    } finally {
      s.close();
    }
  }

  public static List<UasType> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<UasType> utl = s.createQuery("from UasType", UasType.class).getResultList();
      t.commit();
      return utl;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType getAll");
    } finally {
      s.close();
    }
  }

  public static UasType get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      UasType ut = s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return ut;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType get");
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
        .createQuery("delete from UasType where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType delete");
    } finally {
      s.close();
    }
  }

  public static UasType update(SessionFactory sf, UUID id, UasType le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      UasType leo = s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      Manufacturer a = leo.getManufacturer();
      Manufacturer ao = le.getManufacturer();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setManufacturer(ao);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "UasType update");
    } finally {
      s.close();
    }
  }
}
