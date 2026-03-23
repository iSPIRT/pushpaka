package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.LegalEntity;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class LegalEntityService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public LegalEntity create(LegalEntity legalEntity) throws DaoException {
    in.ispirt.pushpaka.dao.entities.LegalEntity entity = LegalEntity.fromOa(legalEntity);
    in.ispirt.pushpaka.dao.entities.LegalEntity saved =
      in.ispirt.pushpaka.dao.entities.LegalEntity.create(sf(), entity);
    return LegalEntity.toOa(saved);
  }

  public void delete(UUID legalEntityId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.LegalEntity.delete(sf(), legalEntityId);
  }

  public List<LegalEntity> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.LegalEntity.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.LegalEntity::toOa)
      .collect(Collectors.toList());
  }

  public LegalEntity getById(UUID legalEntityId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.LegalEntity entity =
      in.ispirt.pushpaka.dao.entities.LegalEntity.get(sf(), legalEntityId);
    return in.ispirt.pushpaka.models.LegalEntity.toOa(entity);
  }

  public LegalEntity update(UUID legalEntityId, LegalEntity legalEntity) throws DaoException {
    in.ispirt.pushpaka.dao.entities.LegalEntity updated =
      in.ispirt.pushpaka.dao.entities.LegalEntity.update(
        sf(),
        legalEntityId,
        LegalEntity.fromOa(legalEntity)
      );
    return LegalEntity.toOa(updated);
  }
}
