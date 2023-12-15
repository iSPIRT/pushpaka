package in.ispirt.pushpaka.flightauthorisation.dao;

import in.ispirt.pushpaka.flightauthorisation.dao.Dao;
import java.math.BigDecimal;
import java.util.function.Function;
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
