package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.CivilAviationAuthority;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CivilAviationAuthorityService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public CivilAviationAuthority create(CivilAviationAuthority civilAviationAuthority)
    throws DaoException {
    in.ispirt.pushpaka.dao.entities.CivilAviationAuthority saved =
      in.ispirt.pushpaka.dao.entities.CivilAviationAuthority.create(
        sf(),
        CivilAviationAuthority.fromOa(civilAviationAuthority)
      );
    return CivilAviationAuthority.toOa(saved);
  }

  public void delete(UUID civilAviationAuthorityId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.CivilAviationAuthority.delete(sf(), civilAviationAuthorityId);
  }

  public List<CivilAviationAuthority> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.CivilAviationAuthority.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.CivilAviationAuthority::toOa)
      .collect(Collectors.toList());
  }

  public CivilAviationAuthority getById(UUID civilAviationAuthorityId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.CivilAviationAuthority entity =
      in.ispirt.pushpaka.dao.entities.CivilAviationAuthority.get(sf(), civilAviationAuthorityId);
    return in.ispirt.pushpaka.models.CivilAviationAuthority.toOa(entity);
  }

  public CivilAviationAuthority update(
    UUID civilAviationAuthorityId,
    CivilAviationAuthority civilAviationAuthority
  ) throws DaoException {
    in.ispirt.pushpaka.dao.entities.CivilAviationAuthority updated =
      in.ispirt.pushpaka.dao.entities.CivilAviationAuthority.update(
        sf(),
        civilAviationAuthorityId,
        CivilAviationAuthority.fromOa(civilAviationAuthority)
      );
    return in.ispirt.pushpaka.models.CivilAviationAuthority.toOa(updated);
  }
}
