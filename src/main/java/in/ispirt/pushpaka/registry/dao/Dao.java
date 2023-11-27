package in.ispirt.pushpaka.registry.dao;

import in.ispirt.pushpaka.registry.models.Country;
import in.ispirt.pushpaka.registry.models.OperationCategory;
import in.ispirt.pushpaka.registry.models.State;
import in.ispirt.pushpaka.registry.models.UasPropulsionCategory;
import in.ispirt.pushpaka.registry.models.UasStatus;
import in.ispirt.pushpaka.registry.models.UasWeightCategory;
import in.ispirt.pushpaka.registry.models.UserStatus;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.registry.utils.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Dao implements Serializable {
  private static final Random RAND = new Random();
  private static final boolean FORCE_RETRY = false;
  private static final String RETRY_SQL_STATE = "40001";
  private static final int MAX_ATTEMPT_COUNT = 6;

  // LegalEntity is our model, which corresponds to the "legal_entities" database table.
  @Entity(name = LegalEntity.PERSISTENCE_NAME)
  @Table(name = LegalEntity.PERSISTENCE_NAME)
  public static class LegalEntity {
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

    public String cin;

    public String getCin() {
      return cin;
    }

    public void setCin(String c) {
      this.cin = c;
    }

    @Column(name = "name")
    public String name;

    public String getName() {
      return name;
    }

    public void setName(String newname) {
      this.name = newname;
    }

    @Column(name = "gstin")
    public String gstin;

    public String getGstin() {
      return gstin;
    }

    public void setGstin(String a) {
      this.gstin = a;
    }

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

    @Column(name = "timestamp_updated")
    public OffsetDateTime timestampUpdated;

    public OffsetDateTime getTimestampUpdated() {
      return timestampUpdated;
    }

    public void setTimestampUpdated(OffsetDateTime a) {
      this.timestampUpdated = a;
    }

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

    public static LegalEntity create(Session s, LegalEntity le) {
      Address aa = Address.create(s, le.getAddress());
      Transaction t = s.beginTransaction();
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
    }

    public static List<LegalEntity> getAll(Session s) {
      return s.createQuery("from LegalEntity", LegalEntity.class).getResultList();
    }

    public static LegalEntity get(Session s, UUID id) {
      return s
        .createQuery("from LegalEntity where id= :id", LegalEntity.class)
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from LegalEntity where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static LegalEntity update(Session s, UUID id, LegalEntity le) {
      LegalEntity leo = s
        .createQuery("from LegalEntity where id= :id", LegalEntity.class)
        .setString("id", id.toString())
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
      return leo;
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

  // Manufacturer is our model, which corresponds to the "uas_types" database table.
  @Entity(name = Manufacturer.PERSISTENCE_NAME)
  @Table(name = Manufacturer.PERSISTENCE_NAME)
  public static class Manufacturer {
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

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

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

    public static Manufacturer create(Session s, Manufacturer m) throws DaoException {
      LegalEntity le = LegalEntity.get(s, m.getLegalEntity().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "LegalEntity");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setLegalEntity(le);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<Manufacturer> getAll(Session s) {
      return s.createQuery("from Manufacturer", Manufacturer.class).getResultList();
    }

    public static Manufacturer get(Session s, UUID id) {
      return s
        .createQuery("from Manufacturer where id= :id", Manufacturer.class)
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Manufacturer where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static Manufacturer update(Session s, UUID id, Manufacturer le) {
      Manufacturer leo = s
        .createQuery("from Manufacturer where id= :id", Manufacturer.class)
        .setString("id", id.toString())
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
      return leo;
    }
  }

  // UasType is our model, which corresponds to the "uas_types" database table.
  @Entity(name = UasType.PERSISTENCE_NAME)
  @Table(name = UasType.PERSISTENCE_NAME)
  public static class UasType {
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

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

    @Column(name = "timestamp_updated")
    public OffsetDateTime timestampUpdated;

    public OffsetDateTime getTimestampUpdated() {
      return timestampUpdated;
    }

    public void setTimestampUpdated(OffsetDateTime a) {
      this.timestampUpdated = a;
    }

    public String modelNumber;

    public String getModelNumber() {
      return modelNumber;
    }

    public void setModelNumber(String c) {
      this.modelNumber = c;
    }

    @Column(name = "photo_url")
    public URL photoUrl;

    public URL getPhotoUrl() {
      return photoUrl;
    }

    public void setPhotoUrl(URL c) {
      this.photoUrl = c;
    }

    public Float mtow;

    public Float getMtow() {
      return mtow;
    }

    public void setMtow(Float c) {
      this.mtow = c;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 16, name = "propulsion_category")
    private UasPropulsionCategory propulsionCategory;

    public UasPropulsionCategory getPropulsionCategory() {
      return this.propulsionCategory;
    }

    public UasWeightCategory getWeightCategory() {
      return Utils.toOpenApiWeightCategory(this.mtow);
    }

    public List<OperationCategory> getSupportedOperationCategories() {
      return new ArrayList<OperationCategory>();
    }

    // Convenience constructor.
    public UasType(
      UUID id,
      Manufacturer manufacturer,
      String modelNumber,
      URL photoUrl,
      Float mtow,
      OffsetDateTime tc,
      OffsetDateTime tu,
      UasPropulsionCategory pc
    ) {
      this.id = id;
      this.manufacturer = manufacturer;
      this.modelNumber = modelNumber;
      this.photoUrl = photoUrl;
      this.mtow = mtow;
      this.timestampCreated = tc;
      this.timestampUpdated = tu;
      this.propulsionCategory = pc;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public UasType() {}

    public static UasType create(Session s, UasType m) throws DaoException {
      Manufacturer le = Manufacturer.get(s, m.getManufacturer().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "Manufacturer");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setManufacturer(le);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<UasType> getAll(Session s) {
      return s.createQuery("from UasType", UasType.class).getResultList();
    }

    public static UasType get(Session s, UUID id) {
      return s
        .createQuery("from UasType where id= :id", UasType.class)
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from UasType where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static UasType update(Session s, UUID id, UasType le) {
      UasType leo = s
        .createQuery("from UasType where id= :id", UasType.class)
        .setString("id", id.toString())
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      Manufacturer a = leo.getManufacturer();
      Manufacturer ao = le.getManufacturer();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setManufacturer(ao);
      return leo;
    }
  }

  // UasType is our model, which corresponds to the "uas_types" database table.
  @Entity
  @Table(name = "uass")
  public static class Uas {
    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

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

    @Column(name = "oem_serial_no")
    public String oemSerialNo;

    public String getOemSerialNo() {
      return oemSerialNo;
    }

    public void setOemSerialNo(String c) {
      this.oemSerialNo = c;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 16, name = "status")
    private UasStatus status;

    public UasStatus getStatus() {
      return this.status;
    }

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

    @Column(name = "timestamp_updated")
    public OffsetDateTime timestampUpdated;

    public OffsetDateTime getTimestampUpdated() {
      return timestampUpdated;
    }

    public void setTimestampUpdated(OffsetDateTime a) {
      this.timestampUpdated = a;
    }

    // Convenience constructor.
    public Uas(
      UUID id,
      UasType ut,
      String oemSerialNo,
      UasStatus s,
      OffsetDateTime tc,
      OffsetDateTime tu
    ) {
      this.id = id;
      this.uasType = ut;
      this.oemSerialNo = oemSerialNo;
      this.status = s;
      this.timestampCreated = tc;
      this.timestampUpdated = tu;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public Uas() {}

    public static Uas create(Session s, Uas m) throws DaoException {
      UasType le = UasType.get(s, m.getUasType().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "UasType");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setUasType(le);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<Uas> getAll(Session s) {
      return s.createQuery("from Uas", Uas.class).getResultList();
    }

    public static Uas get(Session s, UUID id) {
      return s
        .createQuery("from Uas where id= :id", Uas.class)
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Uas where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static Uas update(Session s, UUID id, Uas le) {
      Uas leo = s
        .createQuery("from Uas where id= :id", Uas.class)
        .setString("id", id.toString())
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      UasType a = leo.getUasType();
      UasType ao = le.getUasType();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(ao);
      s.saveOrUpdate(leo);
      leo.setUasType(ao);
      return leo;
    }
  }

  // User is our model, which corresponds to the "users" database table.
  @Entity
  @Table(name = "users")
  public static class User {
    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public User(UUID id) {
      this.id = id;
    }

    public String email;

    public String getEmail() {
      return email;
    }

    public void setEmail(String c) {
      this.email = c;
    }

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

    @Column(name = "timestamp_updated")
    public OffsetDateTime timestampUpdated;

    public OffsetDateTime getTimestampUpdated() {
      return timestampUpdated;
    }

    public void setTimestampUpdated(OffsetDateTime a) {
      this.timestampUpdated = a;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 16, name = "status")
    private UserStatus status;

    // Convenience constructor.
    public User(
      UUID id,
      String email,
      OffsetDateTime tc,
      OffsetDateTime tu,
      UserStatus s
    ) {
      this.id = id;
      this.email = email;
      this.timestampCreated = tc;
      this.timestampUpdated = tu;
      this.status = s;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public User() {}
  }

  // Pilot is our model, which corresponds to the "pilots" database table.
  @Entity
  @Table(name = "pilots")
  public static class Pilot {
    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public Pilot(UUID id) {
      this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "FK_user")
    // @Column(name = "balance")
    public User user;

    public User getUser() {
      return user;
    }

    public void setUser(User m) {
      this.user = m;
    }

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

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
      User u,
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
  }

  // Address is our model, which corresponds to the "addresses" database table.
  @Entity(name = Address.PERSISTENCE_NAME)
  @Table(name = Address.PERSISTENCE_NAME)
  public static class Address {
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

    @Column(name = "city")
    public String city;

    public String getCity() {
      return city;
    }

    public void setCity(String a) {
      this.city = a;
    }

    @Column(name = "pin_code")
    public String pinCode;

    public String getPinCode() {
      return pinCode;
    }

    public void setPinCode(String a) {
      this.pinCode = a;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 32, name = "state")
    private State state;

    public State getState() {
      return state;
    }

    public void setState(State s) {
      this.state = s;
    }

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

    public static Address create(Session s, Address a) {
      Transaction t = s.beginTransaction();
      UUID aid = UUID.randomUUID();
      a.setId(aid);
      System.out.println(
        "Create Address: " + a.getLine1().toString() + " " + a.toString()
      );
      s.save(a);
      s.flush();
      t.commit();
      s.refresh(a);
      return a;
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

  // Civil Aviation Authority is our model, which corresponds to the "civil_aviation_authorities" database table.
  @Entity
  @Table(name = "civil_aviation_authorities")
  public static class CivilAviationAuthority {
    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

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

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

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
    public CivilAviationAuthority(
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
    public CivilAviationAuthority() {}
  }

  // Operator is our model, which corresponds to the "operators" database table.
  @Entity(name = Operator.PERSISTENCE_NAME)
  @Table(name = Operator.PERSISTENCE_NAME)
  public static class Operator {
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

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

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

    public static Operator create(Session s, Operator m) throws DaoException {
      LegalEntity le = LegalEntity.get(s, m.getLegalEntity().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "LegalEntity");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setLegalEntity(le);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<Operator> getAll(Session s) {
      return s.createQuery("from Operator", Operator.class).getResultList();
    }

    public static Operator get(Session s, UUID id) {
      return s
        .createQuery("from Operator where id= :id", Operator.class)
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Operator where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static Operator update(Session s, UUID id, Operator le) {
      Operator leo = s
        .createQuery("from Operator where id= :id", Operator.class)
        .setString("id", id.toString())
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
      return leo;
    }
  }

  // Utmsp is our model, which corresponds to the "utmsps" database table.
  @Entity(name = Utmsp.PERSISTENCE_NAME)
  @Table(name = Utmsp.PERSISTENCE_NAME)
  public static class Utmsp {
    static final String PERSISTENCE_NAME = "Utmsp";

    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public Utmsp setId(UUID id) {
      this.id = id;
      return this;
    }

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

    @Column(name = "timestamp_created")
    public OffsetDateTime timestampCreated;

    public OffsetDateTime getTimestampCreated() {
      return timestampCreated;
    }

    public void setTimestampCreated(OffsetDateTime a) {
      this.timestampCreated = a;
    }

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
    public Utmsp(
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
    public Utmsp() {}

    public static Utmsp create(Session s, Utmsp m) throws DaoException {
      LegalEntity le = LegalEntity.get(s, m.getLegalEntity().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "LegalEntity");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setLegalEntity(le);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<Utmsp> getAll(Session s) {
      return s.createQuery("from Utmsp", Utmsp.class).getResultList();
    }

    public static Utmsp get(Session s, UUID id) {
      return s
        .createQuery("from Utmsp where id= :id", Utmsp.class)
        .setString("id", id.toString())
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Utmsp where id= :id")
        .setString("id", id.toString())
        .executeUpdate();
      t.commit();
    }

    public static Utmsp update(Session s, UUID id, Utmsp le) {
      Utmsp leo = s
        .createQuery("from Utmsp where id= :id", Utmsp.class)
        .setString("id", id.toString())
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
      return leo;
    }
  }

  public static Function<Session, BigDecimal> addUasTypes() throws JDBCException {
    Function<Session, BigDecimal> f = s -> {
      BigDecimal rv = new BigDecimal(0);
      try {
        OffsetDateTime n = OffsetDateTime.now();
        Address a = new Address(
          UUID.randomUUID(),
          "Address Line 1",
          "Address Line 2",
          "Address Line 3",
          "Address City",
          State.MAHARASHTRA,
          "400000",
          Country.IND
        );
        s.save(a);
        LegalEntity l = new LegalEntity(
          UUID.randomUUID(),
          "TEST COMPANY PVT LTD",
          a,
          "CIN0000000",
          "GSTN000000",
          n,
          n
        );
        Manufacturer m = new Manufacturer(UUID.randomUUID(), l, n, n, n, n);
        UasType ut = new UasType(
          UUID.randomUUID(),
          m,
          "UASMN",
          new URL("https://ispirt.github.io/pushpaka"),
          2.5f,
          n,
          n,
          UasPropulsionCategory.VTOL
        );
        Uas u = new Uas(UUID.randomUUID(), ut, "UAS000000", UasStatus.REGISTERED, n, n);
        s.save(l);
        s.save(m);
        s.save(ut);
        s.save(u);
        User uu = new User(
          UUID.randomUUID(),
          "test@company.com",
          n,
          n,
          UserStatus.ACTIVE
        );
        s.save(uu);
        Pilot p = new Pilot(UUID.randomUUID(), uu, n, n, n, n);
        s.save(p);

        CivilAviationAuthority caa = new CivilAviationAuthority(
          UUID.randomUUID(),
          l,
          n,
          n,
          n,
          n
        );
        s.save(caa);

        Operator o1 = new Operator(UUID.randomUUID(), l, n, n, n, n);
        s.save(o1);

        rv = BigDecimal.valueOf(1);
        System.out.printf("APP: addUasTypes() --> %.2f\n", rv);
      } catch (JDBCException e) {
        throw e;
      } catch (MalformedURLException e) {
        // throw e;
      }
      return rv;
    };
    return f;
  }

  // Test our retry handling logic if FORCE_RETRY is true.  This
  // method is only used to test the retry logic.  It is not
  // intended for production code.
  private static Function<Session, BigDecimal> forceRetryLogic() throws JDBCException {
    Function<Session, BigDecimal> f = s -> {
      BigDecimal rv = new BigDecimal(-1);
      try {
        System.out.printf("APP: testRetryLogic: BEFORE EXCEPTION\n");
        s.createNativeQuery("SELECT crdb_internal.force_retry('1s')").executeUpdate();
      } catch (JDBCException e) {
        System.out.printf("APP: testRetryLogic: AFTER EXCEPTION\n");
        throw e;
      }
      return rv;
    };
    return f;
  }

  // Run SQL code in a way that automatically handles the
  // transaction retry logic so we don't have to duplicate it in
  // various places.
  public static BigDecimal runTransaction(
    Session session,
    Function<Session, BigDecimal> fn
  ) {
    BigDecimal rv = new BigDecimal(0);
    int attemptCount = 0;

    while (attemptCount < MAX_ATTEMPT_COUNT) {
      attemptCount++;

      if (attemptCount > 1) {
        System.out.printf("APP: Entering retry loop again, iteration %d\n", attemptCount);
      }

      Transaction txn = session.beginTransaction();
      System.out.printf("APP: BEGIN;\n");

      if (attemptCount == MAX_ATTEMPT_COUNT) {
        String err = String.format("hit max of %s attempts, aborting", MAX_ATTEMPT_COUNT);
        throw new RuntimeException(err);
      }

      // This block is only used to test the retry logic.
      // It is not necessary in production code.  See also
      // the method 'testRetryLogic()'.
      if (FORCE_RETRY) {
        session.createNativeQuery("SELECT now()").list();
      }

      try {
        rv = fn.apply(session);
        if (!rv.equals(-1)) {
          txn.commit();
          System.out.printf("APP: COMMIT;\n");
          break;
        }
      } catch (JDBCException e) {
        if (RETRY_SQL_STATE.equals(e.getSQLState())) {
          // Since this is a transaction retry error, we
          // roll back the transaction and sleep a little
          // before trying again.  Each time through the
          // loop we sleep for a little longer than the last
          // time (A.K.A. exponential backoff).
          System.out.printf(
            "APP: retryable exception occurred:\n    sql state = [%s]\n    message = [%s]\n    retry counter = %s\n",
            e.getSQLState(),
            e.getMessage(),
            attemptCount
          );
          System.out.printf("APP: ROLLBACK;\n");
          txn.rollback();
          int sleepMillis = (int) (Math.pow(2, attemptCount) * 100) + RAND.nextInt(100);
          System.out.printf(
            "APP: Hit 40001 transaction retry error, sleeping %s milliseconds\n",
            sleepMillis
          );
          try {
            Thread.sleep(sleepMillis);
          } catch (InterruptedException ignored) {
            // no-op
          }
          rv = BigDecimal.valueOf(-1);
        } else {
          throw e;
        }
      }
    }
    return rv;
  }

  public static void main(String[] args) {
    // Create a SessionFactory based on our hibernate.cfg.xml configuration
    // file, which defines how to connect to the database.
    SessionFactory sessionFactory = new Configuration()
      .configure("hibernate.cfg.xml")
      .addAnnotatedClass(UasType.class)
      .buildSessionFactory();

    try (Session session = sessionFactory.openSession()) {
      long fromUasTypeId = 1;
      long toUasTypeId = 2;
      BigDecimal transferAmount = BigDecimal.valueOf(100);

      if (FORCE_RETRY) {
        System.out.printf("APP: About to test retry logic in 'runTransaction'\n");
        runTransaction(session, forceRetryLogic());
      } else {
        runTransaction(session, addUasTypes());
        // BigDecimal fromBalance = runTransaction(
        //   session,
        //   getUasTypeBalance(fromUasTypeId)
        // );
        // BigDecimal toBalance = runTransaction(session, getUasTypeBalance(toUasTypeId));
        // if (!fromBalance.equals(-1) && !toBalance.equals(-1)) {
        //   // Success!
        //   System.out.printf(
        //     "APP: getUasTypeBalance(%d) --> %.2f\n",
        //     fromUasTypeId,
        //     fromBalance
        //   );
        //   System.out.printf(
        //     "APP: getUasTypeBalance(%d) --> %.2f\n",
        //     toUasTypeId,
        //     toBalance
        //   );
        // }
      }
    } finally {
      sessionFactory.close();
    }
  }
}
