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
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;

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

    public static Manufacturer create(SessionFactory sf, Manufacturer m)
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
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer create");
      } finally {
        s.close();
      }
    }

    public static List<Manufacturer> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<Manufacturer> ms = s
          .createQuery("from Manufacturer", Manufacturer.class)
          .getResultList();
        t.commit();
        return ms;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer getAll");
      } finally {
        s.close();
      }
    }

    public static Manufacturer get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        Manufacturer m = s
          .createQuery("from Manufacturer where id= :id", Manufacturer.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return m;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer get");
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
          .createQuery("delete from Manufacturer where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer delete");
      } finally {
        s.close();
      }
    }

    public static Manufacturer update(SessionFactory sf, UUID id, Manufacturer le)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
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
        t.commit();
        return leo;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Manufacturer update");
      } finally {
        s.close();
      }
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

    public static RepairAgency create(SessionFactory sf, RepairAgency m)
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
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "RepairAgency create");
      } finally {
        s.close();
      }
    }

    public static List<RepairAgency> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<RepairAgency> ras = s
          .createQuery("from RepairAgency", RepairAgency.class)
          .getResultList();
        t.commit();
        return ras;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "RepairAgency getAll");
      } finally {
        s.close();
      }
    }

    public static RepairAgency get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        RepairAgency ra = s
          .createQuery("from RepairAgency where id= :id", RepairAgency.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return ra;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "RepairAgency get");
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
          .createQuery("delete from RepairAgency where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "RepairAgency delete");
      } finally {
        s.close();
      }
    }

    public static RepairAgency update(SessionFactory sf, UUID id, RepairAgency le)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
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
        t.commit();
        return leo;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "RepairAgency update");
      } finally {
        s.close();
      }
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

    public static Trader create(SessionFactory sf, Trader m) throws DaoException {
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
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Trader create");
      } finally {
        s.close();
      }
    }

    public static List<Trader> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<Trader> ts = s.createQuery("from Trader", Trader.class).getResultList();
        t.commit();
        return ts;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Trader getAll");
      } finally {
        s.close();
      }
    }

    public static Trader get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        Trader tr = s
          .createQuery("from Trader where id= :id", Trader.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return tr;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Trader get");
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
          .createQuery("delete from Trader where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Trader delete");
      } finally {
        s.close();
      }
    }

    public static Trader update(SessionFactory sf, UUID id, Trader le)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
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
        t.commit();
        return leo;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Trader update");
      } finally {
        s.close();
      }
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
      List<Sale> sales = Dao.Sale.getAll(sf, this.id);
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
      // if (nonholdings > 0) {
      //   sb.append(nonholdings.toString());
      // } else {
      //   sb.append("-" + holdings.toString());
      // }
      sb.append(String.format("%02x", holdings).toUpperCase());
      sb.append(String.format("%02x", nonholdings).toUpperCase());
      // Logging.info(
      //   "setHumanReadableId: " +
      //   String.valueOf(this.uasType.getModelNumber()) +
      //   " " +
      //   String.valueOf(
      //     this.getOemSerialNo() +
      //     " " +
      //     String.valueOf(holdings) +
      //     " " +
      //     String.valueOf(nonholdings)
      //   )
      // );
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
    public Person user;

    public Person getUser() {
      return user;
    }

    public void setUser(Person m) {
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
      Person u,
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

    public static Pilot create(SessionFactory sf, Pilot le) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        Person aa = Person.get(sf, le.getUser().getId());
        if (aa == null) {
          throw new DaoException(DaoException.Code.NOT_FOUND, "User");
        }
        t = s.beginTransaction();
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
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Pilot create");
      } finally {
        s.close();
      }
    }

    public static List<Pilot> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<Pilot> p = s.createQuery("from Pilot", Pilot.class).getResultList();
        t.commit();
        return p;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Pilot getAll");
      } finally {
        s.close();
      }
    }

    public static Pilot get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        Pilot p = s
          .createQuery("from Pilot where id= :id", Pilot.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return p;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Pilot get");
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
          .createQuery("delete from Pilot where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Pilot delete");
      } finally {
        s.close();
      }
    }

    public static Pilot update(SessionFactory sf, UUID id, Pilot le) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        Pilot leo = s
          .createQuery("from Pilot where id= :id", Pilot.class)
          .setParameter("id", id)
          .uniqueResult();
        leo.setTimestampUpdated(OffsetDateTime.now());
        s.saveOrUpdate(leo);
        t.commit();
        return leo;
      } catch (Exception e) {
        e.printStackTrace();
        if (t != null) t.rollback();
        throw new DaoException(DaoException.Code.UNKNOWN, "Pilot update");
      } finally {
        s.close();
      }
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

    public static Address create(SessionFactory sf, Address a) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        UUID aid = UUID.randomUUID();
        a.setId(aid);
        // Logging.info(
        //   "Create Address: " + a.getLine1().toString() + " " + a.toString()
        // );
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

  // User is our model, which corresponds to the "Person" database table.
  @Entity(name = Person.PERSISTENCE_NAME)
  @Table(name = Person.PERSISTENCE_NAME)
  public static class Person {
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

    // public static Person get(SessionFactory sf, UUID id) throws DaoException {
    //   Session s = sf.openSession();
    //   Transaction t = null;
    //   try {
    //     return s
    //       .createQuery("from Person where id= :id", Person.class)
    //       .setParameter("id", id)
    //       .uniqueResult();
    //   } catch (Exception e) {
    //     e.printStackTrace();
    //     throw new DaoException(DaoException.Code.UNKNOWN, "LegalEntity create");
    //   } finally {
    //     s.close();
    //   }
    // }

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
      SessionFactory sf,
      DigitalSkyServiceProvider m
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
        e.printStackTrace();
        throw new DaoException(
          DaoException.Code.UNKNOWN,
          "DigitalSkyServiceProvider create"
        );
      } finally {
        s.close();
      }
    }

    public static List<DigitalSkyServiceProvider> getAll(SessionFactory sf)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<DigitalSkyServiceProvider> dssps = s
          .createQuery("from DigitalSkyServiceProvider", DigitalSkyServiceProvider.class)
          .getResultList();
        t.commit();
        return dssps;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(
          DaoException.Code.UNKNOWN,
          "DigitalSkyServiceProvider getAll"
        );
      } finally {
        s.close();
      }
    }

    public static DigitalSkyServiceProvider get(SessionFactory sf, UUID id)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        DigitalSkyServiceProvider dssp = s
          .createQuery(
            "from DigitalSkyServiceProvider where id= :id",
            DigitalSkyServiceProvider.class
          )
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return dssp;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(
          DaoException.Code.UNKNOWN,
          "DigitalSkyServiceProvider get"
        );
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
          .createQuery("delete from DigitalSkyServiceProvider where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(
          DaoException.Code.UNKNOWN,
          "DigitalSkyServiceProvider delete"
        );
      } finally {
        s.close();
      }
    }

    public static DigitalSkyServiceProvider update(
      SessionFactory sf,
      UUID id,
      DigitalSkyServiceProvider le
    )
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
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
        t.commit();
        return leo;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(
          DaoException.Code.UNKNOWN,
          "DigitalSkyServiceProvider update"
        );
      } finally {
        s.close();
      }
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
        // if (m.getTimestampCreated() != null) Logging.info(
        //   "Timestamps " + m.getTimestampCreated().toString()
        // );
        // if (m.getTimestampUpdated() != null) Logging.info(
        //   "Timestamps " + m.getTimestampUpdated().toString()
        // );
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

    public static Lease create(SessionFactory sf, Lease m) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        OffsetDateTime n = OffsetDateTime.now();
        m.setId(UUID.randomUUID());
        m.setTimestampCreated(n);
        m.setTimestampUpdated(n);
        s.save(m);
        s.flush();
        t.commit();
        s.refresh(m);
        return m;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "Lease create");
      } finally {
        s.close();
      }
    }

    public static List<Lease> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<Lease> ll = s.createQuery("from Lease", Lease.class).getResultList();
        t.commit();
        return ll;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "Lease getAll");
      } finally {
        s.close();
      }
    }

    public static Lease get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        Lease l = s
          .createQuery("from Lease where id= :id", Lease.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return l;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "Lease get");
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
          .createQuery("delete from Lease where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "Lease delete");
      } finally {
        s.close();
      }
    }

    public static Lease update(SessionFactory sf, UUID id, Lease le) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        Lease leo = s
          .createQuery("from Lease where id= :id", Lease.class)
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
        throw new DaoException(DaoException.Code.UNKNOWN, "Lease update");
      } finally {
        s.close();
      }
    }
  }

  public static void deleteAll(SessionFactory sf) throws DaoException {
    Session s = sf.openSession();
    Transaction t = null;
    try {
      t = s.beginTransaction();
      s.createQuery("delete from LegalEntity", LegalEntity.class).executeUpdate();
      s.createQuery("delete from Address").executeUpdate();
      s.createQuery("delete from LegalEntity").executeUpdate();
      t.commit();
    } catch (Exception e) {
      e.printStackTrace();
      throw new DaoException(DaoException.Code.UNKNOWN, "deleteAll");
    } finally {
      s.close();
    }
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
    public FlightPlan(UUID id, Uas u, Pilot p, OffsetDateTime st, OffsetDateTime et) {
      this.id = id;
      this.uas = u;
      this.pilot = p;
      this.startTime = st;
      this.endTime = et;
    }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public FlightPlan() {}

    public static FlightPlan create(SessionFactory sf, FlightPlan a) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        Pilot pilot = Pilot.get(sf, a.getPilot().getId());
        Uas uas = Uas.get(sf, a.getUas().getId());
        t = s.beginTransaction();
        a.setPilot(pilot);
        a.setUas(uas);
        s.save(a);
        s.flush();
        t.commit();
        s.refresh(a);
        return a;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan create");
      } finally {
        s.close();
      }
    }

    public static List<FlightPlan> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<FlightPlan> fpl = s
          .createQuery("from FlightPlan", FlightPlan.class)
          .getResultList();
        t.commit();
        return fpl;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan getAll");
      } finally {
        s.close();
      }
    }

    public static FlightPlan get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        FlightPlan fp = s
          .createQuery("from FlightPlan where id= :id", FlightPlan.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return fp;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan get");
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
          .createQuery("delete from FlightPlan where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan delete");
      } finally {
        s.close();
      }
    }

    public static FlightPlan update(SessionFactory sf, UUID id, FlightPlan le)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        FlightPlan leo = s
          .createQuery("from FlightPlan where id= :id", FlightPlan.class)
          .setParameter("id", id)
          .uniqueResult();
        // ao.setCountry(a.getCountry());
        s.saveOrUpdate(leo);
        t.commit();
        return leo;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "FlightPlan update");
      } finally {
        s.close();
      }
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

    // Convenience constructor.
    // public AirspaceUsageToken(
    //   UUID id
    // ) {
    //   this.id = id;
    // }

    // Hibernate needs a default (no-arg) constructor to create model objects.
    public AirspaceUsageToken() {}

    public static AirspaceUsageToken create(SessionFactory sf, AirspaceUsageToken a)
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        Logging.info("AUT CREATE OperationCategory: " + a.getId());
        t = s.beginTransaction();
        if (a.getUas().getUasType().getOperationCategory() == OperationCategory.C) {
          FlightPlan fp = FlightPlan.get(sf, a.getFlightPlan().getId());
          a.setFlightPlan(fp);
          s.save(a);
          s.flush();
          t.commit();
          s.refresh(a);
        } else {
          Logging.info("AUT CREATE Pilot " + a.getPilot().getId());
          Logging.info("AUT CREATE Uas " + a.getUas().getId());
          Pilot pilot = Pilot.get(sf, a.getPilot().getId());
          Uas uas = Uas.get(sf, a.getUas().getId());
          a.setPilot(pilot);
          a.setUas(uas);
          s.save(a);
          s.flush();
          t.commit();
          s.refresh(a);
        }
        return a;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken create");
      } finally {
        s.close();
      }
    }

    public static List<AirspaceUsageToken> getAll(SessionFactory sf) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        List<AirspaceUsageToken> auts = s
          .createQuery("from AirspaceUsageToken", AirspaceUsageToken.class)
          .getResultList();
        t.commit();
        return auts;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken getAll");
      } finally {
        s.close();
      }
    }

    public static AirspaceUsageToken get(SessionFactory sf, UUID id) throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        AirspaceUsageToken aut = s
          .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
          .setParameter("id", id)
          .uniqueResult();
        t.commit();
        return aut;
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken get");
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
          .createQuery("delete from AirspaceUsageToken where id= :id")
          .setParameter("id", id)
          .executeUpdate();
        t.commit();
      } catch (Exception e) {
        e.printStackTrace();
        throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken delete");
      } finally {
        s.close();
      }
    }

    public static AirspaceUsageToken update(
      SessionFactory sf,
      UUID id,
      AirspaceUsageToken le
    )
      throws DaoException {
      Session s = sf.openSession();
      Transaction t = null;
      try {
        t = s.beginTransaction();
        AirspaceUsageToken leo = s
          .createQuery("from AirspaceUsageToken where id= :id", AirspaceUsageToken.class)
          .setParameter("id", id)
          .uniqueResult();
        // ao.setCountry(a.getCountry());
        s.saveOrUpdate(leo);
        return leo;
      } catch (Exception e) {
        throw new DaoException(DaoException.Code.UNKNOWN, "AirspaceUsageToken update");
      } finally {
        s.close();
      }
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
