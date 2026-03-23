package in.ispirt.pushpaka.registry.service;

import in.ispirt.pushpaka.dao.DaoInstance;
import in.ispirt.pushpaka.models.Lease;
import in.ispirt.pushpaka.models.Sale;
import in.ispirt.pushpaka.registry.utils.DaoException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OwnershipService {

  private org.hibernate.SessionFactory sf() {
    return DaoInstance.getInstance().getSessionFactory();
  }

  // Lease operations

  public Lease createLease(Lease lease) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Lease entity = Lease.fromOa(lease);
    in.ispirt.pushpaka.dao.entities.Lease saved =
      in.ispirt.pushpaka.dao.entities.Lease.create(sf(), entity);
    return Lease.toOa(saved);
  }

  public void deleteLease(UUID leaseId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Lease.delete(sf(), leaseId);
  }

  public List<Lease> getAllLeases() throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Lease.getAll(sf())
      .stream()
      .map(in.ispirt.pushpaka.models.Lease::toOa)
      .collect(Collectors.toList());
  }

  public Lease getLeaseById(UUID leaseId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Lease entity =
      in.ispirt.pushpaka.dao.entities.Lease.get(sf(), leaseId);
    return in.ispirt.pushpaka.models.Lease.toOa(entity);
  }

  public Lease updateLease(Lease lease) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Lease entity = Lease.fromOa(lease);
    in.ispirt.pushpaka.dao.entities.Lease updated =
      in.ispirt.pushpaka.dao.entities.Lease.update(sf(), entity.getId(), entity);
    return Lease.toOa(updated);
  }

  // Sale operations

  public Sale createSale(Sale sale) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Sale entity = Sale.fromOa(sale);
    in.ispirt.pushpaka.dao.entities.Sale saved =
      in.ispirt.pushpaka.dao.entities.Sale.create(sf(), entity);
    return Sale.toOa(saved);
  }

  public void deleteSale(UUID saleId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Sale.delete(sf(), saleId);
  }

  public List<Sale> getAllSales(UUID uasId) throws DaoException {
    return in.ispirt.pushpaka.dao.entities.Sale.getAll(sf(), uasId)
      .stream()
      .map(in.ispirt.pushpaka.models.Sale::toOa)
      .collect(Collectors.toList());
  }

  public Sale getSaleById(UUID saleId) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Sale entity =
      in.ispirt.pushpaka.dao.entities.Sale.get(sf(), saleId);
    return in.ispirt.pushpaka.models.Sale.toOa(entity);
  }

  public Sale updateSale(Sale sale) throws DaoException {
    in.ispirt.pushpaka.dao.entities.Sale entity = Sale.fromOa(sale);
    in.ispirt.pushpaka.dao.entities.Sale updated =
      in.ispirt.pushpaka.dao.entities.Sale.update(sf(), entity.getId(), entity);
    return Sale.toOa(updated);
  }
}
