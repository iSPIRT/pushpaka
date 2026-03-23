package in.ispirt.pushpaka.dao;

import in.ispirt.pushpaka.dao.entities.Address;
import in.ispirt.pushpaka.dao.entities.AirspaceUsageToken;
import in.ispirt.pushpaka.dao.entities.CivilAviationAuthority;
import in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider;
import in.ispirt.pushpaka.dao.entities.FlightPlan;
import in.ispirt.pushpaka.dao.entities.Lease;
import in.ispirt.pushpaka.dao.entities.LegalEntity;
import in.ispirt.pushpaka.dao.entities.Manufacturer;
import in.ispirt.pushpaka.dao.entities.Operator;
import in.ispirt.pushpaka.dao.entities.Person;
import in.ispirt.pushpaka.dao.entities.Pilot;
import in.ispirt.pushpaka.dao.entities.RepairAgency;
import in.ispirt.pushpaka.dao.entities.Sale;
import in.ispirt.pushpaka.dao.entities.Trader;
import in.ispirt.pushpaka.dao.entities.Uas;
import in.ispirt.pushpaka.dao.entities.UasType;
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
    String dbUrl = System.getenv().getOrDefault(
      "DATABASE_URL",
      "jdbc:postgresql://localhost:15432/pushpaka?sslmode=disable"
    );
    String dbUser = System.getenv().getOrDefault("DATABASE_USER", "postgres");
    String dbPassword = System.getenv().getOrDefault("DATABASE_PASSWORD", "secret");
    sessionFactory =
      new Configuration()
        .configure("hibernate.cfg.xml")
        .setProperty("hibernate.connection.url", dbUrl)
        .setProperty("hibernate.connection.username", dbUser)
        .setProperty("hibernate.connection.password", dbPassword)
        .addAnnotatedClass(LegalEntity.class)
        .addAnnotatedClass(Manufacturer.class)
        .addAnnotatedClass(UasType.class)
        .addAnnotatedClass(Sale.class)
        .addAnnotatedClass(Uas.class)
        .addAnnotatedClass(Person.class)
        .addAnnotatedClass(Pilot.class)
        .addAnnotatedClass(Address.class)
        .addAnnotatedClass(CivilAviationAuthority.class)
        .addAnnotatedClass(Operator.class)
        .addAnnotatedClass(DigitalSkyServiceProvider.class)
        .addAnnotatedClass(RepairAgency.class)
        .addAnnotatedClass(Trader.class)
        .addAnnotatedClass(FlightPlan.class)
        .addAnnotatedClass(AirspaceUsageToken.class)
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
