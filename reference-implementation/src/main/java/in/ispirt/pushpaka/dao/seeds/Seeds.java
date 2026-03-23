package in.ispirt.pushpaka.dao.seeds;

import in.ispirt.pushpaka.dao.entities.Address;
import in.ispirt.pushpaka.dao.entities.CivilAviationAuthority;
import in.ispirt.pushpaka.dao.entities.LegalEntity;
import in.ispirt.pushpaka.dao.entities.Manufacturer;
import in.ispirt.pushpaka.dao.entities.Operator;
import in.ispirt.pushpaka.dao.entities.Person;
import in.ispirt.pushpaka.dao.entities.Pilot;
import in.ispirt.pushpaka.dao.entities.UasType;
import in.ispirt.pushpaka.models.Country;
import in.ispirt.pushpaka.models.OperationCategory;
import in.ispirt.pushpaka.models.State;
import in.ispirt.pushpaka.models.UserStatus;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Named, deterministic fixture data for tests and local dev seeding.
 *
 * <p>Two kinds of constants are provided:
 * <ul>
 *   <li>SPICEDB_* — string resource IDs used by the SpiceDB AuthZ layer.</li>
 *   <li>*_ID UUIDs + entity builder methods — Hibernate/DB layer fixtures with
 *       stable, well-known IDs so seeds can be loaded idempotently.</li>
 * </ul>
 *
 * <p>Builder methods return <em>unpersisted</em> entity objects. Use {@link SeedLoader}
 * to write them to the database.
 */
public final class Seeds {

  private Seeds() {}

  // ── SpiceDB resource IDs ─────────────────────────────────────────────────

  public static final String SPICEDB_CAA_RESOURCE_ID = "caa-authority";
  public static final String SPICEDB_MANUFACTURER_RESOURCE_ID = "manufacturer-1";
  public static final String SPICEDB_OPERATOR_RESOURCE_ID = "operator-1";
  public static final String SPICEDB_DSSP_RESOURCE_ID = "dssp-1";
  public static final String SPICEDB_TRADER_RESOURCE_ID = "trader-1";
  public static final String SPICEDB_REPAIR_AGENCY_RESOURCE_ID = "repairagency-1";

  public static final String SPICEDB_PLATFORM_USER_ID = "platform-user";
  public static final String SPICEDB_CAA_ADMIN_USER_ID = "caa-user";
  public static final String SPICEDB_MANUFACTURER_ADMIN_USER_ID = "manufacturer-user";
  public static final String SPICEDB_OPERATOR_ADMIN_USER_ID = "operator-user";
  public static final String SPICEDB_DSSP_ADMIN_USER_ID = "dssp-user";
  public static final String SPICEDB_TRADER_ADMIN_USER_ID = "trader-user";
  public static final String SPICEDB_REPAIR_AGENCY_ADMIN_USER_ID = "repairagency-user";

  public static final String SPICEDB_PILOT_RESOURCE_1_ID = "pilot-resource-1";
  public static final String SPICEDB_PILOT_RESOURCE_2_ID = "pilot-resource-2";
  public static final String SPICEDB_PILOT_USER_1_ID = "pilot-user-1";
  public static final String SPICEDB_PILOT_USER_2_ID = "pilot-user-2";

  public static final String SPICEDB_UAS_RESOURCE_ID = "uas-1";
  public static final String SPICEDB_UAS_TYPE_RESOURCE_ID = "uastype-1";

  // ── Stable DB entity UUIDs ───────────────────────────────────────────────
  // Prefix encodes entity type for easier debugging:
  //   00000001-… Address
  //   00000002-… LegalEntity
  //   00000003-… CivilAviationAuthority
  //   00000004-… Manufacturer
  //   00000005-… Operator
  //   00000006-… Person
  //   00000007-… Pilot
  //   00000008-… UasType

  public static final UUID ADDRESS_CAA_ID = UUID.fromString(
    "00000001-0000-0000-0000-000000000001"
  );
  public static final UUID ADDRESS_MFR_ID = UUID.fromString(
    "00000001-0000-0000-0000-000000000002"
  );
  public static final UUID ADDRESS_OPS_ID = UUID.fromString(
    "00000001-0000-0000-0000-000000000003"
  );
  public static final UUID ADDRESS_PERSON_1_ID = UUID.fromString(
    "00000001-0000-0000-0000-000000000004"
  );

  public static final UUID LEGAL_ENTITY_CAA_ID = UUID.fromString(
    "00000002-0000-0000-0000-000000000001"
  );
  public static final UUID LEGAL_ENTITY_MFR_ID = UUID.fromString(
    "00000002-0000-0000-0000-000000000002"
  );
  public static final UUID LEGAL_ENTITY_OPS_ID = UUID.fromString(
    "00000002-0000-0000-0000-000000000003"
  );

  public static final UUID CAA_ID = UUID.fromString(
    "00000003-0000-0000-0000-000000000001"
  );
  public static final UUID MANUFACTURER_ID = UUID.fromString(
    "00000004-0000-0000-0000-000000000001"
  );
  public static final UUID OPERATOR_ID = UUID.fromString(
    "00000005-0000-0000-0000-000000000001"
  );
  public static final UUID PERSON_1_ID = UUID.fromString(
    "00000006-0000-0000-0000-000000000001"
  );
  public static final UUID PILOT_1_ID = UUID.fromString(
    "00000007-0000-0000-0000-000000000001"
  );
  public static final UUID UAS_TYPE_1_ID = UUID.fromString(
    "00000008-0000-0000-0000-000000000001"
  );

  // ── Entity builders (unpersisted) ────────────────────────────────────────

  public static Address addressCaa() {
    Address a = new Address();
    a.setId(ADDRESS_CAA_ID);
    a.setLine1("Rajiv Gandhi Bhavan");
    a.setLine2("Safdarjung Airport");
    a.setCity("New Delhi");
    a.setPinCode("110003");
    a.setState(State.ANDHRA_PRADESH); // placeholder — State enum has no DELHI yet
    a.setCountry(Country.IND);
    return a;
  }

  public static Address addressManufacturer() {
    Address a = new Address();
    a.setId(ADDRESS_MFR_ID);
    a.setLine1("Plot 42 MIDC Industrial Area");
    a.setLine2("Chinchwad");
    a.setCity("Pune");
    a.setPinCode("411019");
    a.setState(State.MAHARASHTRA);
    a.setCountry(Country.IND);
    return a;
  }

  public static Address addressOperator() {
    Address a = new Address();
    a.setId(ADDRESS_OPS_ID);
    a.setLine1("14 Koramangala 5th Block");
    a.setCity("Bengaluru");
    a.setPinCode("560034");
    a.setState(State.ANDHRA_PRADESH); // placeholder
    a.setCountry(Country.IND);
    return a;
  }

  public static Address addressPerson1() {
    Address a = new Address();
    a.setId(ADDRESS_PERSON_1_ID);
    a.setLine1("123 ABC Housing Society");
    a.setLine2("Bandra West");
    a.setCity("Mumbai");
    a.setPinCode("400050");
    a.setState(State.MAHARASHTRA);
    a.setCountry(Country.IND);
    return a;
  }

  public static LegalEntity legalEntityCaa() {
    OffsetDateTime n = OffsetDateTime.now();
    LegalEntity le = new LegalEntity();
    le.setId(LEGAL_ENTITY_CAA_ID);
    le.setName("Civil Aviation Authority of India");
    le.setCin("CAA0000001");
    le.setGstin("27AABCC1234D1Z5");
    le.setAddress(addressCaa());
    le.setTimestampCreated(n);
    le.setTimestampUpdated(n);
    return le;
  }

  public static LegalEntity legalEntityManufacturer() {
    OffsetDateTime n = OffsetDateTime.now();
    LegalEntity le = new LegalEntity();
    le.setId(LEGAL_ENTITY_MFR_ID);
    le.setName("Test Drone Manufacturer Pvt Ltd");
    le.setCin("MFR0000001");
    le.setGstin("27AABCM1234D1Z5");
    le.setAddress(addressManufacturer());
    le.setTimestampCreated(n);
    le.setTimestampUpdated(n);
    return le;
  }

  public static LegalEntity legalEntityOperator() {
    OffsetDateTime n = OffsetDateTime.now();
    LegalEntity le = new LegalEntity();
    le.setId(LEGAL_ENTITY_OPS_ID);
    le.setName("Test Drone Operator Pvt Ltd");
    le.setCin("OPS0000001");
    le.setGstin("29AABCO1234D1Z5");
    le.setAddress(addressOperator());
    le.setTimestampCreated(n);
    le.setTimestampUpdated(n);
    return le;
  }

  public static CivilAviationAuthority caa() {
    OffsetDateTime n = OffsetDateTime.now();
    CivilAviationAuthority c = new CivilAviationAuthority();
    c.setId(CAA_ID);
    c.setLegalEntity(legalEntityCaa());
    c.setCountry(Country.IND);
    c.setTimestampCreated(n);
    c.setTimestampUpdated(n);
    return c;
  }

  public static Manufacturer manufacturer() {
    OffsetDateTime n = OffsetDateTime.now();
    Manufacturer m = new Manufacturer();
    m.setId(MANUFACTURER_ID);
    m.setLegalEntity(legalEntityManufacturer());
    m.setTimestampCreated(n);
    m.setTimestampUpdated(n);
    return m;
  }

  public static Operator operator() {
    OffsetDateTime n = OffsetDateTime.now();
    Operator o = new Operator();
    o.setId(OPERATOR_ID);
    o.setLegalEntity(legalEntityOperator());
    o.setTimestampCreated(n);
    o.setTimestampUpdated(n);
    return o;
  }

  public static Person person1() {
    OffsetDateTime n = OffsetDateTime.now();
    Person p = new Person(PERSON_1_ID);
    p.setPhone("+919876543210");
    p.setAadharId("1234-5678-9012");
    p.setAddress(addressPerson1());
    p.setTimestampCreated(n);
    p.setTimestampUpdated(n);
    p.setStatus(UserStatus.ACTIVE);
    return p;
  }

  public static Pilot pilot1() {
    OffsetDateTime n = OffsetDateTime.now();
    Pilot pl = new Pilot();
    pl.setId(PILOT_1_ID);
    pl.setUser(person1());
    pl.setTimestampCreated(n);
    pl.setTimestampUpdated(n);
    return pl;
  }

  public static UasType uasType1() {
    OffsetDateTime n = OffsetDateTime.now();
    UasType ut = new UasType();
    ut.setId(UAS_TYPE_1_ID);
    ut.setManufacturer(manufacturer());
    ut.setModelNumber(1001);
    ut.setMtow(2.5f);
    ut.setApproved(false);
    ut.setOperationCategory(OperationCategory.A);
    try {
      ut.setPhotoUrl(new URL("https://example.com/uas-type-1.jpg"));
    } catch (MalformedURLException e) {
      throw new RuntimeException("Seed URL is malformed", e);
    }
    ut.setTimestampCreated(n);
    ut.setTimestampUpdated(n);
    return ut;
  }
}
