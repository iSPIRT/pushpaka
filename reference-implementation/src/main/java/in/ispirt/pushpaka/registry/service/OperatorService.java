package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Operator;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OperatorService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public Operator create(Operator operator) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Operator saved =
      in.ispirt.pushpaka.dao.entities.Operator.create(sf(), Operator.fromOa(operator));
    return Operator.toOa(saved);
  }

  public void delete(UUID operatorId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Operator.delete(sf(), operatorId);
  }

  public List<Operator> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Operator.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.Operator::toOa)
      .collect(Collectors.toList());
  }

  public Operator getById(UUID operatorId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Operator entity =
      in.ispirt.pushpaka.dao.entities.Operator.get(sf(), operatorId);
    return in.ispirt.pushpaka.models.Operator.toOa(entity);
  }

  public Operator update(UUID operatorId, Operator operator) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Operator updated =
      in.ispirt.pushpaka.dao.entities.Operator.update(sf(), operatorId, Operator.fromOa(operator));
    return in.ispirt.pushpaka.models.Operator.toOa(updated);
  }
}
