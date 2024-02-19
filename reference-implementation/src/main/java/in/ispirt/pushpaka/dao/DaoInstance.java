package in.ispirt.pushpaka.dao;

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
        .addAnnotatedClass(Dao.LegalEntity.class)
        .addAnnotatedClass(Dao.Manufacturer.class)
        .addAnnotatedClass(Dao.UasType.class)
        .addAnnotatedClass(Dao.Sale.class)
        .addAnnotatedClass(Dao.Uas.class)
        .addAnnotatedClass(Dao.Person.class)
        .addAnnotatedClass(Dao.Pilot.class)
        .addAnnotatedClass(Dao.Address.class)
        .addAnnotatedClass(Dao.CivilAviationAuthority.class)
        .addAnnotatedClass(Dao.Operator.class)
        .addAnnotatedClass(Dao.DigitalSkyServiceProvider.class)
        .addAnnotatedClass(Dao.RepairAgency.class)
        .addAnnotatedClass(Dao.Trader.class)
        .addAnnotatedClass(Dao.FlightPlan.class)
        .addAnnotatedClass(Dao.AirspaceUsageToken.class)
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

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  protected void finalize() {
    sessionFactory.close();
    System.out.println("Finalise DaoInstance: close SessionFactory");
  }
}
