package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.dao.entities.Person;
import in.ispirt.pushpaka.models.User;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public User create(User user) throws DaoException {
    Person entity = User.fromOa(user);
    Person saved = Person.create(sf(), entity);
    return User.toOa(saved);
  }

  public User getByUsername(String username) throws DaoException {
    UUID id = UUID.fromString(username);
    Person entity = Person.get(sf(), id);
    return User.toOa(entity);
  }

  public List<User> getAll() throws DaoException {
    return Person.getAll(sf()).stream().map(User::toOa).collect(Collectors.toList());
  }

  public void delete(String username) throws DaoException {
    UUID id = UUID.fromString(username);
    Person.delete(sf(), id);
  }

  public User update(String username, User user) throws DaoException {
    UUID id = UUID.fromString(username);
    Person entity = User.fromOa(user);
    Person updated = Person.update(sf(), id, entity);
    return User.toOa(updated);
  }
}
