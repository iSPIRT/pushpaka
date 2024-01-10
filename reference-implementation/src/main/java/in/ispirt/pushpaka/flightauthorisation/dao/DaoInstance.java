package in.ispirt.pushpaka.flightauthorisation.dao;

import in.ispirt.pushpaka.registry.dao.Dao.Address;
import in.ispirt.pushpaka.registry.dao.Dao.CivilAviationAuthority;
import in.ispirt.pushpaka.registry.dao.Dao.DigitalSkyServiceProvider;
import in.ispirt.pushpaka.registry.dao.Dao.LegalEntity;
import in.ispirt.pushpaka.registry.dao.Dao.Manufacturer;
import in.ispirt.pushpaka.registry.dao.Dao.Operator;
import in.ispirt.pushpaka.registry.dao.Dao.Pilot;
import in.ispirt.pushpaka.registry.dao.Dao.RepairAgency;
import in.ispirt.pushpaka.registry.dao.Dao.Sale;
import in.ispirt.pushpaka.registry.dao.Dao.Trader;
import in.ispirt.pushpaka.registry.dao.Dao.Uas;
import in.ispirt.pushpaka.registry.dao.Dao.UasType;
import in.ispirt.pushpaka.registry.dao.Dao.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DaoInstance {
  // Static variable reference of single_instance
  // of type Singleton
  private static DaoInstance single_instance = null;
  private static SessionFactory sessionFactory = null;
  private static Session session = null;

  // Declaring a variable of type String
  private static Dao _dao;

  // Constructor
  // Here we will be creating private constructor
  // restricted to this class itself
  private DaoInstance() {
    _dao = new Dao();
    sessionFactory =
      new Configuration()
        .configure("hibernate.cfg.xml")
        .addAnnotatedClass(Dao.FlightPlan.class)
        .addAnnotatedClass(Dao.AirspaceUsageToken.class)
        .addAnnotatedClass(Uas.class)
        .addAnnotatedClass(Pilot.class)
        .addAnnotatedClass(LegalEntity.class)
        .addAnnotatedClass(Manufacturer.class)
        .addAnnotatedClass(UasType.class)
        .addAnnotatedClass(Sale.class)
        .addAnnotatedClass(Uas.class)
        .addAnnotatedClass(Users.class)
        .addAnnotatedClass(Pilot.class)
        .addAnnotatedClass(Address.class)
        .addAnnotatedClass(CivilAviationAuthority.class)
        .addAnnotatedClass(Operator.class)
        .addAnnotatedClass(DigitalSkyServiceProvider.class)
        .addAnnotatedClass(RepairAgency.class)
        .addAnnotatedClass(Trader.class)
        .buildSessionFactory();
    session = sessionFactory.openSession();
  }

  // Static method
  // Static method to create instance of DaoInstance class
  public static synchronized DaoInstance getInstance() {
    if (single_instance == null) single_instance = new DaoInstance();
    return single_instance;
  }

  public Session getSession() {
    return session;
  }

  protected void finalize() {
    sessionFactory.close();
    System.out.println("Finalise DaoInstance: close SessionFactory");
  }
}
