package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.models.UserStatus;
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

// User is our model, which corresponds to the "Person" database table.
@Entity(name = Person.PERSISTENCE_NAME)
@Table(name = Person.PERSISTENCE_NAME)
public class Person {
  static final String PERSISTENCE_NAME = "Person";

  @Id
  @Column(name = "id")
  private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Person(UUID id) {
    this.id = id;
  }

  public String phone;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String c) {
    this.phone = c;
  }

  public String aadharId;

  public String getAadharId() {
    return aadharId;
  }

  public void setAadharId(String c) {
    this.aadharId = c;
  }

  @OneToOne
  @JoinColumn(name = "fk_address")
  private Address address;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address x) {
    this.address = x;
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
  @Enumerated(EnumType.STRING)
  @Column(length = 16, name = "status")
  private UserStatus status;

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus x) {
    this.status = x;
  }

  // Convenience constructor.
  public Person(
    UUID id,
    String phone,
    String aadharId,
    Address address,
    OffsetDateTime tc,
    OffsetDateTime tu,
    UserStatus s
  ) {
    this.id = id;
    this.phone = phone;
    this.aadharId = aadharId;
    this.address = address;
    this.timestampCreated = tc;
    this.timestampUpdated = tu;
    this.status = s;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public Person() {}

  public static Person get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Person u = s
        .createQuery("from Person where id= :id", Person.class)
        .setParameter("id", id)
        .uniqueResult();
      return u;
    } catch (Exception e) {
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Person get");
    } finally {
      s.close();
    }
  }

  public static Person create(SessionFactory sf, Person le) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      Person u = Person.get(sf, le.id);
      t = s.beginTransaction();
      if (u != null) {
        Address aa = Address.get(sf, u.getAddress().getId());
        OffsetDateTime n = OffsetDateTime.now();
        aa.setLine1(le.getAddress().getLine1());
        aa.setLine2(le.getAddress().getLine2());
        aa.setLine3(le.getAddress().getLine3());
        aa.setCity(le.getAddress().getCity());
        aa.setState(le.getAddress().getState());
        aa.setPinCode(le.getAddress().getPinCode());
        aa.setCountry(le.getAddress().getCountry());
        le.setTimestampCreated(n);
        le.setTimestampUpdated(n);
        le.setAddress(aa);
        le.setStatus(UserStatus.INACTIVE);
        s.merge(aa);
        s.merge(le);
        s.flush();
        t.commit();
        s.refresh(le);
        // s.evict(le);
        return le;
      } else {
        Address aa = Address.create(sf, le.getAddress());
        OffsetDateTime n = OffsetDateTime.now();
        le.setTimestampCreated(n);
        le.setTimestampUpdated(n);
        le.setAddress(aa);
        le.setStatus(UserStatus.INACTIVE);
        s.save(le);
        s.flush();
        t.commit();
        s.refresh(le);
        // s.evict(le);
        return le;
      }
    } catch (Exception e) {
      if (t != null) t.rollback();
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Person create");
    } finally {
      s.close();
    }
  }

  public static List<Person> getAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      List<Person> pl = s.createQuery("from Person", Person.class).getResultList();
      t.commit();
      return pl;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Person getAll");
    } finally {
      s.close();
    }
  }

  public static void delete(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Person le = s
        .createQuery("from Person where id= :id", Person.class)
        .setParameter("id", id)
        .uniqueResult();
      s
        .createQuery("delete from Address where id= :id")
        .setString("id", le.getAddress().getId().toString())
        .executeUpdate();
      s
        .createQuery("delete from Person where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Person delete");
    } finally {
      s.close();
    }
  }

  public static Person update(SessionFactory sf, UUID id, Person le)
    throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Person leo = s
        .createQuery("from Person where id= :id", Person.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setPhone(leo.getPhone());
      leo.setAadharId(leo.getAadharId());
      leo.setTimestampUpdated(OffsetDateTime.now());
      Address a = leo.getAddress();
      Address ao = le.getAddress();
      ao.setLine1(a.getLine1());
      ao.setLine2(a.getLine2());
      ao.setLine3(a.getLine3());
      ao.setCity(a.getCity());
      ao.setPinCode(a.getPinCode());
      ao.setState(a.getState());
      ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setAddress(ao);
      t.commit();
      return leo;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Person update");
    } finally {
      s.close();
    }
  }
}
