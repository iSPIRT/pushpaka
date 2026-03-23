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

// Sale is our model, which corresponds to the "uas_types" database table.
@Entity(name = Sale.PERSISTENCE_NAME)
@Table(name = Sale.PERSISTENCE_NAME)
public class Sale {
  static final String PERSISTENCE_NAME = "Sale";

  @Id
  @Column(name = "id")
  public UUID id;

  public Sale setId(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getId() {
    return id;
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

  @Column(name = "holding")
  public Boolean holding;

  public Boolean getHolding() {
    return holding;
  }

  public void setHolding(Boolean a) {
    this.holding = a;
  }

  @ManyToOne
  @JoinColumn(name = "FK_uas")
  // @Column(name = "uas")
  public Uas uas;

  public Uas getUas() {
    return uas;
  }

  public void setUas(Uas a) {
    this.uas = a;
  }

  @ManyToOne
  @JoinColumn(name = "FK_seller_user")
  // @Column(name = "seller_user")
  public Person sellerUser;

  public Person getSellerUser() {
    return sellerUser;
  }

  public void setSellerUser(Person a) {
    this.sellerUser = a;
  }

  @ManyToOne
  @JoinColumn(name = "FK_buyer_user")
  // @Column(name = "buyer_user")
  public Person buyerUser;

  public Person getBuyerUser() {
    return buyerUser;
  }

  public void setBuyerUser(Person a) {
    this.buyerUser = a;
  }

  @ManyToOne
  @JoinColumn(name = "FK_seller_legal_entity")
  // @Column(name = "seller_legal_entity")
  public LegalEntity sellerLegalEntity;

  public LegalEntity getSellerLegalEntity() {
    return sellerLegalEntity;
  }

  public void setSellerLegalEntity(LegalEntity a) {
    this.sellerLegalEntity = a;
  }

  @ManyToOne
  @JoinColumn(name = "FK_buyer_legal_entity")
  // @Column(name = "buyer_legal_entity")
  public LegalEntity buyerLegalEntity;

  public LegalEntity getBuyerLegalEntity() {
    return buyerLegalEntity;
  }

  public void setBuyerLegalEntity(LegalEntity a) {
    this.buyerLegalEntity = a;
  }

  // Convenience constructor.
  public Sale(
    UUID id,
    Uas uas,
    OffsetDateTime tc,
    OffsetDateTime tu,
    OffsetDateTime vs,
    OffsetDateTime ve,
    Person su,
    Person bu,
    LegalEntity sle,
    LegalEntity ble,
    Boolean h
  ) {
    this.id = id;
    this.uas = uas;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
    this.validityStart = vs;
    this.validityEnd = ve;
    this.sellerUser = su;
    this.buyerUser = bu;
    this.sellerLegalEntity = sle;
    this.buyerLegalEntity = ble;
    this.holding = h;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public Sale() {}

  public static Sale create(SessionFactory sf, Sale m) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setTimestampCreated(n);
      m.setTimestampUpdated(n);
      //
      Uas uas = Uas.get(sf, m.getUas().getId());
      if (m.getBuyerUser() != null) {
        Person bu = Person.get(sf, m.getBuyerUser().getId());
        m.setBuyerUser(bu);
      }
      if (m.getSellerUser() != null) {
        Person su = Person.get(sf, m.getSellerUser().getId());
        m.setSellerUser(su);
      }
      if (m.getBuyerLegalEntity() != null) {
        LegalEntity ble = LegalEntity.get(sf, m.getBuyerLegalEntity().getId());
        m.setBuyerLegalEntity(ble);
      }
      if (m.getSellerLegalEntity() != null) {
        LegalEntity sle = LegalEntity.get(sf, m.getSellerLegalEntity().getId());
        m.setSellerLegalEntity(sle);
      }

      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Sale create");
    } finally {
      s.close();
    }
  }

  public static List<Sale> getAll(SessionFactory sf, UUID uasId) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<Sale> sl = s
        .createQuery("from Sale where FK_uas= :id", Sale.class)
        .setParameter("id", uasId)
        .getResultList();
      t.commit();
      return sl;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Sale getAll");
    } finally {
      s.close();
    }
  }

  public static Sale get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Sale ss = s
        .createQuery("from Sale where id= :id", Sale.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return ss;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Sale get");
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
        .createQuery("delete from Sale where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Sale delete");
    } finally {
      s.close();
    }
  }

  public static Sale update(SessionFactory sf, UUID id, Sale le) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Sale leo = s
        .createQuery("from Sale where id= :id", Sale.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      leo.setValidityStart(le.getValidityStart());
      leo.setValidityEnd(le.getValidityEnd());
      s.saveOrUpdate(leo);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Sale update");
    } finally {
      s.close();
    }
  }
}
