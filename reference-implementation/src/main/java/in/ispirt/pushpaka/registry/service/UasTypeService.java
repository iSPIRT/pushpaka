package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.UasType;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UasTypeService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public UasType create(UasType uasType) throws DaoException {
    in.ispirt.pushpaka.dao.entities.UasType entity = UasType.fromOa(uasType);
    in.ispirt.pushpaka.dao.entities.UasType saved =
      in.ispirt.pushpaka.dao.entities.UasType.create(sf(), entity);
    return UasType.toOa(saved);
  }

  public UasType approve(UUID uasTypeId, Integer modelNumber) throws DaoException {
    in.ispirt.pushpaka.dao.entities.UasType entity =
      in.ispirt.pushpaka.dao.entities.UasType.setModelNumber(sf(), uasTypeId, modelNumber);
    entity = in.ispirt.pushpaka.dao.entities.UasType.approve(sf(), uasTypeId);
    return UasType.toOa(entity);
  }

  public void delete(UUID uasTypeId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.UasType.delete(sf(), uasTypeId);
  }

  public List<UasType> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.UasType.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.UasType::toOa)
      .collect(Collectors.toList());
  }

  public UasType getById(UUID uasTypeId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.UasType entity =
      in.ispirt.pushpaka.dao.entities.UasType.get(sf(), uasTypeId);
    return in.ispirt.pushpaka.models.UasType.toOa(entity);
  }

  public UasType update(UasType uasType) throws DaoException {
    in.ispirt.pushpaka.dao.entities.UasType updated =
      in.ispirt.pushpaka.dao.entities.UasType.update(
        sf(),
        uasType.getId(),
        UasType.fromOa(uasType)
      );
    return in.ispirt.pushpaka.models.UasType.toOa(updated);
  }
}
