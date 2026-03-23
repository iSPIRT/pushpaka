package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Manufacturer;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public Manufacturer create(Manufacturer manufacturer) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Manufacturer saved =
      in.ispirt.pushpaka.dao.entities.Manufacturer.create(
        sf(),
        Manufacturer.fromOa(manufacturer)
      );
    return Manufacturer.toOa(saved);
  }

  public void delete(UUID manufacturerId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Manufacturer.delete(sf(), manufacturerId);
  }

  public List<Manufacturer> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Manufacturer.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.Manufacturer::toOa)
      .collect(Collectors.toList());
  }

  public Manufacturer getById(UUID manufacturerId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Manufacturer entity =
      in.ispirt.pushpaka.dao.entities.Manufacturer.get(sf(), manufacturerId);
    return in.ispirt.pushpaka.models.Manufacturer.toOa(entity);
  }

  public Manufacturer update(UUID manufacturerId, Manufacturer manufacturer) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Manufacturer updated =
      in.ispirt.pushpaka.dao.entities.Manufacturer.update(
        sf(),
        manufacturerId,
        Manufacturer.fromOa(manufacturer)
      );
    return in.ispirt.pushpaka.models.Manufacturer.toOa(updated);
  }
}
