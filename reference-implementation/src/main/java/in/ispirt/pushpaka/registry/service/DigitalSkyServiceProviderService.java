package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.DigitalSkyServiceProvider;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DigitalSkyServiceProviderService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public DigitalSkyServiceProvider create(DigitalSkyServiceProvider digitalSkyServiceProvider)
    throws DaoException {
    in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider saved =
      in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider.create(
        sf(),
        DigitalSkyServiceProvider.fromOa(digitalSkyServiceProvider)
      );
    return DigitalSkyServiceProvider.toOa(saved);
  }

  public void delete(UUID digitalSkyServiceProviderId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider.delete(
      sf(),
      digitalSkyServiceProviderId
    );
  }

  public List<DigitalSkyServiceProvider> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.DigitalSkyServiceProvider::toOa)
      .collect(Collectors.toList());
  }

  public DigitalSkyServiceProvider getById(UUID digitalSkyServiceProviderId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider entity =
      in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider.get(
        sf(),
        digitalSkyServiceProviderId
      );
    return in.ispirt.pushpaka.models.DigitalSkyServiceProvider.toOa(entity);
  }

  public DigitalSkyServiceProvider update(
    UUID digitalSkyServiceProviderId,
    DigitalSkyServiceProvider digitalSkyServiceProvider
  ) throws DaoException {
    in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider updated =
      in.ispirt.pushpaka.dao.entities.DigitalSkyServiceProvider.update(
        sf(),
        digitalSkyServiceProviderId,
        DigitalSkyServiceProvider.fromOa(digitalSkyServiceProvider)
      );
    return in.ispirt.pushpaka.models.DigitalSkyServiceProvider.toOa(updated);
  }
}
