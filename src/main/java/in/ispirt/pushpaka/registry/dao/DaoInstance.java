package in.ispirt.pushpaka.registry.dao;

import in.ispirt.pushpaka.registry.dao.Dao;
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
        .addAnnotatedClass(Dao.LegalEntity.class)
        .addAnnotatedClass(Dao.Manufacturer.class)
        .addAnnotatedClass(Dao.UasType.class)
        .addAnnotatedClass(Dao.Uas.class)
        .addAnnotatedClass(Dao.User.class)
        .addAnnotatedClass(Dao.Pilot.class)
        .addAnnotatedClass(Dao.Address.class)
        .addAnnotatedClass(Dao.CivilAviationAuthority.class)
        .addAnnotatedClass(Dao.Operator.class)
        .buildSessionFactory();
    session = sessionFactory.openSession();
  }

  // Static method
  // Static method to create instance of DaoInstance class
  public static synchronized DaoInstance getInstance() {
    if (single_instance == null) single_instance = new DaoInstance();
    return single_instance;
  }

  public static void addUasTypes() {
    _dao.runTransaction(session, _dao.addUasTypes());
  }

  protected void finalize() {
    sessionFactory.close();
    System.out.println("Finalise DaoInstance: close SessionFactory");
  }
}
