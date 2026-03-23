package in.ispirt.pushpaka.dao.seeds;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.dao.entities.Address;
import in.ispirt.pushpaka.dao.entities.CivilAviationAuthority;
import in.ispirt.pushpaka.dao.entities.LegalEntity;
import in.ispirt.pushpaka.dao.entities.Manufacturer;
import in.ispirt.pushpaka.dao.entities.Operator;
import in.ispirt.pushpaka.dao.entities.Person;
import in.ispirt.pushpaka.dao.entities.Pilot;
import in.ispirt.pushpaka.dao.entities.UasType;
import in.ispirt.pushpaka.utils.Logging;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Writes {@link Seeds} fixtures to the database.
 *
 * <p>All operations are idempotent: if a record with the seed's stable UUID already
 * exists it is left untouched. Call {@link #loadAll()} once at test setup or app
 * startup to ensure a baseline dataset is present.
 *
 * <p>Persistence uses {@code Session.save()} directly (bypassing the entity static
 * {@code create()} helpers which generate random UUIDs) so that seed IDs remain stable
 * across runs.
 */
public class SeedLoader {

  private final SessionFactory sf;

  public SeedLoader() {
    this.sf = DaoInstance.getInstance().getSessionFactory();
  }

  public SeedLoader(SessionFactory sf) {
    this.sf = sf;
  }

  /**
   * Loads all seed entities in dependency order. Safe to call multiple times.
   */
  public void loadAll() {
    saveIfAbsent(Address.class, Seeds.ADDRESS_CAA_ID, Seeds.addressCaa());
    saveIfAbsent(Address.class, Seeds.ADDRESS_MFR_ID, Seeds.addressManufacturer());
    saveIfAbsent(Address.class, Seeds.ADDRESS_OPS_ID, Seeds.addressOperator());
    saveIfAbsent(Address.class, Seeds.ADDRESS_PERSON_1_ID, Seeds.addressPerson1());

    saveIfAbsent(LegalEntity.class, Seeds.LEGAL_ENTITY_CAA_ID, Seeds.legalEntityCaa());
    saveIfAbsent(
      LegalEntity.class,
      Seeds.LEGAL_ENTITY_MFR_ID,
      Seeds.legalEntityManufacturer()
    );
    saveIfAbsent(
      LegalEntity.class,
      Seeds.LEGAL_ENTITY_OPS_ID,
      Seeds.legalEntityOperator()
    );

    saveIfAbsent(CivilAviationAuthority.class, Seeds.CAA_ID, Seeds.caa());
    saveIfAbsent(Manufacturer.class, Seeds.MANUFACTURER_ID, Seeds.manufacturer());
    saveIfAbsent(Operator.class, Seeds.OPERATOR_ID, Seeds.operator());

    saveIfAbsent(Person.class, Seeds.PERSON_1_ID, Seeds.person1());
    saveIfAbsent(Pilot.class, Seeds.PILOT_1_ID, Seeds.pilot1());

    saveIfAbsent(UasType.class, Seeds.UAS_TYPE_1_ID, Seeds.uasType1());

    Logging.info("SeedLoader: seed pass complete");
  }

  /**
   * Persists {@code entity} only if no row with {@code id} exists in the table for
   * {@code entityClass}. Returns true if a new row was inserted.
   */
  public <T> boolean saveIfAbsent(Class<T> entityClass, UUID id, T entity) {
    Session s = sf.openSession();
    Transaction tx = null;
    try {
      tx = s.beginTransaction();
      T existing = s.get(entityClass, id);
      if (existing != null) {
        tx.commit();
        return false;
      }
      s.save(entity);
      s.flush();
      tx.commit();
      Logging.info("SeedLoader: inserted " + entityClass.getSimpleName() + " id=" + id);
      return true;
    } catch (Exception e) {
      if (tx != null) tx.rollback();
      Logging.warning(
        "SeedLoader: failed to seed " + entityClass.getSimpleName() + " id=" + id
      );
      e.printStackTrace();
      return false;
    } finally {
      s.close();
    }
  }
}
