package in.ispirt.pushpaka.dao;

import in.ispirt.pushpaka.models.Country;
import in.ispirt.pushpaka.models.OperationCategory;
import in.ispirt.pushpaka.models.State;
import in.ispirt.pushpaka.models.UasPropulsionCategory;
import in.ispirt.pushpaka.models.UasStatus;
import in.ispirt.pushpaka.models.UasWeightCategory;
import in.ispirt.pushpaka.models.UserStatus;
import in.ispirt.pushpaka.registry.utils.DaoException;
import in.ispirt.pushpaka.registry.utils.Utils;
import in.ispirt.pushpaka.utils.Logging;
import java.io.Serializable;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Dao implements Serializable {
  private static final long serialVersionUID = 1L;

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

    public static LegalEntity create(Session s, LegalEntity le) {
      Address aa = Address.create(s, le.getAddress());
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      // le.setId(UUID.randomUUID());
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
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
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
    }

    public static LegalEntity update(Session s, UUID id, LegalEntity le) {
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

    public static Manufacturer create(Session s, Manufacturer m)
      throws DaoException, ConstraintViolationException {
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
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Manufacturer where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Manufacturer update(Session s, UUID id, Manufacturer le) {
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
      return leo;
    }
  }

  // RepairAgency is our model, which corresponds to the "uas_types" database table.
  @Entity(name = RepairAgency.PERSISTENCE_NAME)
  @Table(name = RepairAgency.PERSISTENCE_NAME)
  public static class RepairAgency {
    static final String PERSISTENCE_NAME = "RepairAgency";

    @Id
    @Column(name = "id")
    public UUID id;

    public RepairAgency setId(UUID id) {
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
    public RepairAgency(
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
    public RepairAgency() {}

    public static RepairAgency create(Session s, RepairAgency m)
      throws DaoException, ConstraintViolationException {
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

    public static List<RepairAgency> getAll(Session s) {
      return s.createQuery("from RepairAgency", RepairAgency.class).getResultList();
    }

    public static RepairAgency get(Session s, UUID id) {
      return s
        .createQuery("from RepairAgency where id= :id", RepairAgency.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from RepairAgency where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static RepairAgency update(Session s, UUID id, RepairAgency le) {
      RepairAgency leo = s
        .createQuery("from RepairAgency where id= :id", RepairAgency.class)
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
      return leo;
    }
  }

  // Trader is our model, which corresponds to the "uas_types" database table.
  @Entity(name = Trader.PERSISTENCE_NAME)
  @Table(name = Trader.PERSISTENCE_NAME)
  public static class Trader {
    static final String PERSISTENCE_NAME = "Trader";

    @Id
    @Column(name = "id")
    public UUID id;

    public Trader setId(UUID id) {
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
    public Trader(
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
    public Trader() {}

    public static Trader create(Session s, Trader m)
      throws DaoException, ConstraintViolationException {
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

    public static List<Trader> getAll(Session s) {
      return s.createQuery("from Trader", Trader.class).getResultList();
    }

    public static Trader get(Session s, UUID id) {
      return s
        .createQuery("from Trader where id= :id", Trader.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Trader where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Trader update(Session s, UUID id, Trader le) {
      Trader leo = s
        .createQuery("from Trader where id= :id", Trader.class)
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
    public String modelNumber;

    public String getModelNumber() {
      return modelNumber;
    }

    public void setModelNumber(String c) {
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
    @Column //(name = "supported_operation_categories")
    @ElementCollection(targetClass = OperationCategory.class)
    private List<OperationCategory> supportedOperationCategories;

    public List<OperationCategory> getSupportedOperationCategories() {
      return this.supportedOperationCategories;
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
      UasPropulsionCategory pc,
      List<OperationCategory> ocs,
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
      this.supportedOperationCategories = ocs;
      this.approved = approved;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public UasType() {}

    public static UasType create(Session s, UasType m)
      throws DaoException, ConstraintViolationException {
      Manufacturer le = Manufacturer.get(s, m.getManufacturer().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "Manufacturer");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setManufacturer(le);
      m.setApproved(false);
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static UasType approve(Session s, UUID id) throws DaoException {
      UasType uu = s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
      if (uu == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "UasType");
      }
      uu.setApproved(true);
      s.save(uu);
      uu.setApproved(true);
      s.refresh(uu);
      return uu;
    }

    public static UasType setModelNumber(Session s, UUID id, String modelNumber)
      throws DaoException {
      return s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static List<UasType> getAll(Session s) {
      return s.createQuery("from UasType", UasType.class).getResultList();
    }

    public static UasType get(Session s, UUID id) {
      return s
        .createQuery("from UasType where id= :id", UasType.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from UasType where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static UasType update(Session s, UUID id, UasType le) {
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
      return leo;
    }
  }

  // UasType is our model, which corresponds to the "uas_types" database table.
  @Entity(name = Uas.PERSISTENCE_NAME)
  @Table(name = Uas.PERSISTENCE_NAME)
  public static class Uas {
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
    public String oemSerialNo;

    public String getOemSerialNo() {
      return oemSerialNo;
    }

    public void setOemSerialNo(String c) {
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

    public String getHumanReadableId() {
      StringBuilder sb = new StringBuilder();
      sb.append(this.uasType.getModelNumber());
      sb.append("-");
      sb.append(this.getOemSerialNo());
      return sb.toString();
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

    public static Uas create(Session s, Uas m)
      throws DaoException, ConstraintViolationException {
      UasType le = UasType.get(s, m.getUasType().getId());
      if (le == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "UasType");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      m.setTimestampCreated(n);
      m.setTimestampUpdated(n);
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
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Uas where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Uas update(Session s, UUID id, Uas le) {
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
      return leo;
    }
  }

  // Pilot is our model, which corresponds to the "pilots" database table.
  @Entity(name = Pilot.PERSISTENCE_NAME)
  @Table(name = Pilot.PERSISTENCE_NAME)
  public static class Pilot {
    static final String PERSISTENCE_NAME = "Pilot";

    @Id
    @Column(name = "id")
    private UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public Pilot(UUID id) {
      this.id = id;
    }

    @NotNull
    @OneToOne
    @JoinColumn(name = "FK_user")
    // @Column(name = "balance")
    public Users user;

    public Users getUser() {
      return user;
    }

    public void setUser(Users m) {
      this.user = m;
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
    public Pilot(
      UUID id,
      Users u,
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

    public static Pilot create(Session s, Pilot le)
      throws DaoException, ConstraintViolationException {
      Users aa = Users.get(s, le.getUser().getId());
      if (aa == null) {
        throw new DaoException(DaoException.Code.NOT_FOUND, "User");
      }
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      le.setId(UUID.randomUUID());
      le.setUser(aa);
      le.setTimestampCreated(n);
      le.setTimestampUpdated(n);
      s.save(le);
      s.flush();
      t.commit();
      s.refresh(le);
      return le;
    }

    public static List<Pilot> getAll(Session s) {
      return s.createQuery("from Pilot", Pilot.class).getResultList();
    }

    public static Pilot get(Session s, UUID id) {
      return s
        .createQuery("from Pilot where id= :id", Pilot.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Pilot where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Pilot update(Session s, UUID id, Pilot le) {
      Pilot leo = s
        .createQuery("from Pilot where id= :id", Pilot.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      s.saveOrUpdate(leo);
      return leo;
    }
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

    public static Address get(Session s, UUID id) {
      return s
        .createQuery("from Address where id= :id", Address.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Address where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
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

  // User is our model, which corresponds to the "users" database table.
  @Entity(name = Users.PERSISTENCE_NAME)
  @Table(name = Users.PERSISTENCE_NAME)
  public static class Users {
    static final String PERSISTENCE_NAME = "Users";

    @Id
    @Column(name = "id")
    private UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public Users(UUID id) {
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
    @JoinColumn(name = "FK_address")
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
    public Users(
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
    public Users() {}

    public static Users create(Session s, Users le)
      throws DaoException, ConstraintViolationException {
      Users u = s
        .createQuery("from Users where id= :id", Users.class)
        .setParameter("id", le.getId())
        .uniqueResult();
      if (u != null) {
        Address aa = Address.get(s, u.getAddress().getId());
        Transaction t = s.beginTransaction();
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
        Address aa = Address.create(s, le.getAddress());
        Transaction t = s.beginTransaction();
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
    }

    public static List<Users> getAll(Session s) {
      return s.createQuery("from Users", Users.class).getResultList();
    }

    public static Users get(Session s, UUID id) {
      return s
        .createQuery("from Users where id= :id", Users.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      Users le = s
        .createQuery("from Users where id= :id", Users.class)
        .setParameter("id", id)
        .uniqueResult();
      s
        .createQuery("delete from Address where id= :id")
        .setString("id", le.getAddress().getId().toString())
        .executeUpdate();
      s
        .createQuery("delete from Users where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Users update(Session s, UUID id, Users le) {
      Users leo = s
        .createQuery("from Users where id= :id", Users.class)
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
      return leo;
    }
  }

  // Civil Aviation Authority is our model, which corresponds to the "civil_aviation_authorities" database table.
  @Entity(name = CivilAviationAuthority.PERSISTENCE_NAME)
  @Table(name = CivilAviationAuthority.PERSISTENCE_NAME)
  public static class CivilAviationAuthority {
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

    public static CivilAviationAuthority create(Session s, CivilAviationAuthority m)
      throws DaoException {
      Logging.info("LE: " + m.getLegalEntity().getId());
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

    public static List<CivilAviationAuthority> getAll(Session s) {
      return s
        .createQuery("from CivilAviationAuthority", CivilAviationAuthority.class)
        .getResultList();
    }

    public static CivilAviationAuthority get(Session s, UUID id) {
      return s
        .createQuery(
          "from CivilAviationAuthority where id= :id",
          CivilAviationAuthority.class
        )
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from CivilAviationAuthority where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static CivilAviationAuthority update(
      Session s,
      UUID id,
      CivilAviationAuthority le
    ) {
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
      return leo;
    }
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

    public static Operator create(Session s, Operator m)
      throws DaoException, ConstraintViolationException {
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
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Operator where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Operator update(Session s, UUID id, Operator le) {
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
      return leo;
    }
  }

  // DigitalSkyServiceProvider is our model, which corresponds to the "digitalSkyServiceProviders" database table.
  @Entity(name = DigitalSkyServiceProvider.PERSISTENCE_NAME)
  @Table(name = DigitalSkyServiceProvider.PERSISTENCE_NAME)
  public static class DigitalSkyServiceProvider {
    static final String PERSISTENCE_NAME = "DigitalSkyServiceProvider";

    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public DigitalSkyServiceProvider setId(UUID id) {
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
    public DigitalSkyServiceProvider(
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
    public DigitalSkyServiceProvider() {}

    public static DigitalSkyServiceProvider create(
      Session s,
      DigitalSkyServiceProvider m
    )
      throws DaoException, ConstraintViolationException {
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

    public static List<DigitalSkyServiceProvider> getAll(Session s) {
      return s
        .createQuery("from DigitalSkyServiceProvider", DigitalSkyServiceProvider.class)
        .getResultList();
    }

    public static DigitalSkyServiceProvider get(Session s, UUID id) {
      return s
        .createQuery(
          "from DigitalSkyServiceProvider where id= :id",
          DigitalSkyServiceProvider.class
        )
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from DigitalSkyServiceProvider where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static DigitalSkyServiceProvider update(
      Session s,
      UUID id,
      DigitalSkyServiceProvider le
    ) {
      DigitalSkyServiceProvider leo = s
        .createQuery(
          "from DigitalSkyServiceProvider where id= :id",
          DigitalSkyServiceProvider.class
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
      return leo;
    }
  }

  // Sale is our model, which corresponds to the "uas_types" database table.
  @Entity(name = Sale.PERSISTENCE_NAME)
  @Table(name = Sale.PERSISTENCE_NAME)
  public static class Sale {
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
    public Users sellerUser;

    public Users getSellerUser() {
      return sellerUser;
    }

    public void setSellerUser(Users a) {
      this.sellerUser = a;
    }

    @ManyToOne
    @JoinColumn(name = "FK_buyer_user")
    // @Column(name = "buyer_user")
    public Users buyerUser;

    public Users getBuyerUser() {
      return buyerUser;
    }

    public void setBuyerUser(Users a) {
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
      Users su,
      Users bu,
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

    public static Sale create(Session s, Sale m)
      throws DaoException, ConstraintViolationException {
      if (m.getTimestampCreated() != null) Logging.info(
        "Timestamps " + m.getTimestampCreated().toString()
      );
      if (m.getTimestampUpdated() != null) Logging.info(
        "Timestamps " + m.getTimestampUpdated().toString()
      );
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      //
      Uas uas = Uas.get(s, m.getUas().getId());
      if (m.getBuyerUser() != null) {
        Users bu = Users.get(s, m.getBuyerUser().getId());
        m.setBuyerUser(bu);
      }
      if (m.getSellerUser() != null) {
        Users su = Users.get(s, m.getSellerUser().getId());
        m.setSellerUser(su);
      }
      if (m.getBuyerLegalEntity() != null) {
        LegalEntity ble = LegalEntity.get(s, m.getBuyerLegalEntity().getId());
        m.setBuyerLegalEntity(ble);
      }
      if (m.getSellerLegalEntity() != null) {
        LegalEntity sle = LegalEntity.get(s, m.getSellerLegalEntity().getId());
        m.setSellerLegalEntity(sle);
      }
      ///

      ///
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<Sale> getAll(Session s) {
      return s.createQuery("from Sale", Sale.class).getResultList();
    }

    public static Sale get(Session s, UUID id) {
      return s
        .createQuery("from Sale where id= :id", Sale.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Sale where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Sale update(Session s, UUID id, Sale le) {
      Sale leo = s
        .createQuery("from Sale where id= :id", Sale.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      leo.setValidityStart(le.getValidityStart());
      leo.setValidityEnd(le.getValidityEnd());
      s.saveOrUpdate(leo);
      return leo;
    }
  }

  // Lease is our model, which corresponds to the "uas_types" database table.
  @Entity(name = Lease.PERSISTENCE_NAME)
  @Table(name = Lease.PERSISTENCE_NAME)
  public static class Lease {
    static final String PERSISTENCE_NAME = "Lease";

    @Id
    @Column(name = "id")
    public UUID id;

    public Lease setId(UUID id) {
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

    // Convenience constructor.
    public Lease(
      UUID id,
      OffsetDateTime tc,
      OffsetDateTime tu,
      OffsetDateTime vs,
      OffsetDateTime ve
    ) {
      this.id = id;
      this.timestampCreated = tc;
      this.timestampUpdated = tu;
      this.validityStart = vs;
      this.validityEnd = ve;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public Lease() {}

    public static Lease create(Session s, Lease m)
      throws DaoException, ConstraintViolationException {
      Transaction t = s.beginTransaction();
      OffsetDateTime n = OffsetDateTime.now();
      m.setId(UUID.randomUUID());
      s.save(m);
      s.flush();
      t.commit();
      s.refresh(m);
      return m;
    }

    public static List<Lease> getAll(Session s) {
      return s.createQuery("from Lease", Lease.class).getResultList();
    }

    public static Lease get(Session s, UUID id) {
      return s
        .createQuery("from Lease where id= :id", Lease.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from Lease where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static Lease update(Session s, UUID id, Lease le) {
      Lease leo = s
        .createQuery("from Lease where id= :id", Lease.class)
        .setParameter("id", id)
        .uniqueResult();
      leo.setTimestampUpdated(OffsetDateTime.now());
      leo.setValidityStart(le.getValidityStart());
      leo.setValidityEnd(le.getValidityEnd());
      s.saveOrUpdate(leo);
      return leo;
    }
  }

  public static void deleteAll(Session s) {
    Transaction t = s.beginTransaction();
    s.createQuery("delete from LegalEntity", LegalEntity.class).executeUpdate();
    s.createQuery("delete from Address").executeUpdate();
    s.createQuery("delete from LegalEntity").executeUpdate();
    t.commit();
  }

  // FlightPlan is our model, which corresponds to the "FlightPlanes" database table.
  @Entity(name = FlightPlan.PERSISTENCE_NAME)
  @Table(name = FlightPlan.PERSISTENCE_NAME)
  public static class FlightPlan {
    static final String PERSISTENCE_NAME = "FlightPlan";

    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public FlightPlan(UUID id) {
      this.id = id;
    }

    @NotNull
    @Column(name = "operation_category")
    public OperationCategory operationCategory;

    public OperationCategory getOperationCategory() {
      return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
      this.operationCategory = operationCategory;
    }

    @NotNull
    @Column(name = "start_time")
    // @Column(name = "uas_id")
    public OffsetDateTime startTime;

    public OffsetDateTime getStartTime() {
      return startTime;
    }

    public void setStartTime(OffsetDateTime id) {
      this.startTime = id;
    }

    @NotNull
    @Column(name = "end_time")
    // @Column(name = "uas_id")
    public OffsetDateTime endTime;

    public OffsetDateTime getEndTime() {
      return endTime;
    }

    public void setEndTime(OffsetDateTime id) {
      this.endTime = id;
    }

    @ManyToOne
    @JoinColumn(name = "FK_pilot")
    // @Column(name = "pilot_id")
    public Pilot pilot;

    public Pilot getPilot() {
      return pilot;
    }

    public void setPilot(Pilot pilot) {
      this.pilot = pilot;
    }

    @ManyToOne
    @JoinColumn(name = "FK_uas")
    // @Column(name = "uas_id")
    public Uas uas;

    public Uas getUas() {
      return uas;
    }

    public void setUas(Uas id) {
      this.uas = id;
    }

    //
    // Convenience constructor.
    public FlightPlan(
      UUID id,
      Uas u,
      Pilot p,
      OffsetDateTime st,
      OffsetDateTime et,
      OperationCategory c
    ) {
      this.id = id;
      this.uas = u;
      this.pilot = p;
      this.startTime = st;
      this.endTime = et;
      this.operationCategory = c;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public FlightPlan() {}

    public static FlightPlan create(Session s, FlightPlan a) {
      Pilot pilot = Pilot.get(s, a.getPilot().getId());
      Uas uas = Uas.get(s, a.getUas().getId());
      Transaction t = s.beginTransaction();
      a.setPilot(pilot);
      a.setUas(uas);
      s.save(a);
      s.flush();
      t.commit();
      s.refresh(a);
      return a;
    }

    public static List<FlightPlan> getAll(Session s) {
      return s.createQuery("from FlightPlan", FlightPlan.class).getResultList();
    }

    public static FlightPlan get(Session s, UUID id) {
      return s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from FlightPlan where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static FlightPlan update(Session s, UUID id, FlightPlan le) {
      FlightPlan leo = s
        .createQuery("from FlightPlan where id= :id", FlightPlan.class)
        .setParameter("id", id)
        .uniqueResult();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(leo);
      return leo;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("class FlightPlan {\n");
      sb.append("    id: ").append(id.toString()).append("\n");
      sb.append("}");
      return sb.toString();
    }
  }

  // AirspaceUsageToken is our model, which corresponds to the "AirspaceUsageTokenes" database table.
  @Entity(name = AirspaceUsageToken.PERSISTENCE_NAME)
  @Table(name = AirspaceUsageToken.PERSISTENCE_NAME)
  public static class AirspaceUsageToken {
    static final String PERSISTENCE_NAME = "AirspaceUsageToken";

    @Id
    @Column(name = "id")
    public UUID id;

    public UUID getId() {
      return id;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public AirspaceUsageToken(UUID id) {
      this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "FK_flight_plan")
    // @Column(name = "flight_plan_id")
    public FlightPlan flightPlan;

    public FlightPlan getFlightPlan() {
      return flightPlan;
    }

    public void setFlightPlan(FlightPlan flightPlan) {
      this.flightPlan = flightPlan;
    }

    @ManyToOne
    @JoinColumn(name = "FK_pilot")
    // @Column(name = "pilot_id")
    public Pilot pilot;

    public Pilot getPilot() {
      return pilot;
    }

    public void setPilot(Pilot pilot) {
      this.pilot = pilot;
    }

    @ManyToOne
    @JoinColumn(name = "FK_uas")
    // @Column(name = "uas_id")
    public Uas uas;

    public Uas getUas() {
      return uas;
    }

    public void setUas(Uas id) {
      this.uas = id;
    }

    @NotNull
    @Column(name = "operation_category")
    public OperationCategory operationCategory;

    public OperationCategory getOperationCategory() {
      return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
      this.operationCategory = operationCategory;
    }

    // Convenience constructor.
    // public AirspaceUsageToken(
    //   UUID id
    // ) {
    //   this.id = id;
    // }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public AirspaceUsageToken() {}

    public static AirspaceUsageToken create(Session s, AirspaceUsageToken a) {
      Logging.info("AUT CREATE OperationCategory: " + a.getOperationCategory());
      // TODO remove this?
      if (a.getOperationCategory() == null) a.setOperationCategory(OperationCategory.C1);
      if (a.getOperationCategory() == OperationCategory.C3) {
        FlightPlan fp = FlightPlan.get(s, a.getFlightPlan().getId());
        Transaction t = s.beginTransaction();
        a.setFlightPlan(fp);
        s.save(a);
        s.flush();
        t.commit();
        s.refresh(a);
      } else {
        Logging.info("AUT CREATE Pilot " + a.getPilot().getId());
        Logging.info("AUT CREATE Uas " + a.getUas().getId());
        Pilot pilot = Pilot.get(s, a.getPilot().getId());
        Uas uas = Uas.get(s, a.getUas().getId());
        Transaction t = s.beginTransaction();
        a.setPilot(pilot);
        a.setUas(uas);
        s.save(a);
        s.flush();
        t.commit();
        s.refresh(a);
      }
      return a;
    }

    public static List<AirspaceUsageToken> getAll(Session s) {
      return s
        .createQuery("from AirspaceUsageToken", AirspaceUsageToken.class)
        .getResultList();
    }

    public static AirspaceUsageToken get(Session s, UUID id) {
      return s
        .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
        .setParameter("id", id)
        .uniqueResult();
    }

    public static void delete(Session s, UUID id) {
      Transaction t = s.beginTransaction();
      s
        .createQuery("delete from AirspaceUsageToken where id= :id")
        .setParameter("id", id)
        .executeUpdate();
      t.commit();
    }

    public static AirspaceUsageToken update(Session s, UUID id, AirspaceUsageToken le) {
      AirspaceUsageToken leo = s
        .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
        .setParameter("id", id)
        .uniqueResult();
      // ao.setCountry(a.getCountry());
      s.saveOrUpdate(leo);
      return leo;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("class AirspaceUsageToken {\n");
      sb.append("    id: ").append(id.toString()).append("\n");
      sb.append("}");
      return sb.toString();
    }
  }
}
