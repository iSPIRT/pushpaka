package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Trader;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TraderService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  public Trader create(Trader trader) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Trader saved =
      in.ispirt.pushpaka.dao.entities.Trader.create(sf(), Trader.fromOa(trader));
    return Trader.toOa(saved);
  }

  public void delete(UUID traderId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Trader.delete(sf(), traderId);
  }

  public List<Trader> getAll() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Trader.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.Trader::toOa)
      .collect(Collectors.toList());
  }

  public Trader getById(UUID traderId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Trader entity =
      in.ispirt.pushpaka.dao.entities.Trader.get(sf(), traderId);
    return in.ispirt.pushpaka.models.Trader.toOa(entity);
  }

  public Trader update(UUID traderId, Trader trader) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Trader updated =
      in.ispirt.pushpaka.dao.entities.Trader.update(sf(), traderId, Trader.fromOa(trader));
    return in.ispirt.pushpaka.models.Trader.toOa(updated);
  }
}
