package in.ispirt.pushpaka.dao.entities;

import in.ispirt.pushpaka.models.Country;
import in.ispirt.pushpaka.models.State;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

// Address is our model, which corresponds to the "addresses" database table.
@Entity(name = Address.PERSISTENCE_NAME)
@Table(name = Address.PERSISTENCE_NAME)
public class Address {
  static final String PERSISTENCE_NAME = "Address";

  @Id
  @Column(name = "id")
  public UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Address(UUID id) {
    this.id = id;
  }

  @NotNull
  @Column(name = "line1")
  public String line1;

  public String getLine1() {
    return line1;
  }

  public void setLine1(String a) {
    this.line1 = a;
  }

  @Column(name = "line2")
  public String line2;

  public String getLine2() {
    return line2;
  }

  public void setLine2(String a) {
    this.line2 = a;
  }

  @Column(name = "line3")
  public String line3;

  public String getLine3() {
    return line3;
  }

  public void setLine3(String a) {
    this.line3 = a;
  }

  @NotNull
  @Column(name = "city")
  public String city;

  public String getCity() {
    return city;
  }

  public void setCity(String a) {
    this.city = a;
  }

  @NotNull
  @Column(name = "pin_code")
  public String pinCode;

  public String getPinCode() {
    return pinCode;
  }

  public void setPinCode(String a) {
    this.pinCode = a;
  }

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 32, name = "state")
  private State state;

  public State getState() {
    return state;
  }

  public void setState(State s) {
    this.state = s;
  }

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length = 3, name = "country")
  private Country country;

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country s) {
    this.country = s;
  }

  // Convenience constructor.
  public Address(
    UUID id,
    String l1,
    String l2,
    String l3,
    String c,
    State s,
    String pinCode,
    Country co
  ) {
    this.id = id;
    this.line1 = l1;
    this.line2 = l2;
    this.line3 = l3;
    this.city = c;
    this.pinCode = pinCode;
    this.state = s;
    this.country = co;
  }

  // Hibernate needs a default (no-arg) constructor to create model objects.
  public Address() {}

  public static Address create(SessionFactory sf, Address a) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      UUID aid = UUID.randomUUID();
      a.setId(aid);
      s.save(a);
      s.flush();
      t.commit();
      s.refresh(a);
      return a;
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "Address create");
    } finally {
      s.close();
    }
  }

  public static Address get(SessionFactory sf, UUID id) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      Address a = s
        .createQuery("from Address where id= :id", Address.class)
        .setParameter("id", id)
        .uniqueResult();
      t.commit();
      return a;
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Address get");
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
        .createQuery("delete from Address where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      if (t != null) t.rollback();
      throw new DaoException(DaoException.Code.UNKNOWN, "Address delete");
    } finally {
      s.close();
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Address {\n");
    sb.append("    id: ").append(id.toString()).append("\n");
    sb.append("    line1: ").append(line1).append("\n");
    sb.append("    line2: ").append(line2).append("\n");
    sb.append("    line3: ").append(line3).append("\n");
    sb.append("    city: ").append(city).append("\n");
    sb.append("    state: ").append(state).append("\n");
    sb.append("    pinCode: ").append(pinCode).append("\n");
    sb.append("    country: ").append(country).append("\n");
    sb.append("}");
    return sb.toString();
  }
}
