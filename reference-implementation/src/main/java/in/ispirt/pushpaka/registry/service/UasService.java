package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Uas;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UasService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public Uas create(Uas uas) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Uas entity = Uas.fromOa(uas);
    in.ispirt.pushpaka.dao.entities.Uas saved =
      in.ispirt.pushpaka.dao.entities.Uas.create(sf(), entity);
    return Uas.toOa(saved);
  }

  public void delete(UUID uasId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Uas.delete(sf(), uasId);
  }

  public List<Uas> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Uas.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.Uas::toOa)
      .collect(Collectors.toList());
  }

  public Uas getById(UUID uasId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Uas entity =
      in.ispirt.pushpaka.dao.entities.Uas.get(sf(), uasId);
    return in.ispirt.pushpaka.models.Uas.toOa(entity);
  }
}
