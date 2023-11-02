package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.entities.Supplier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class SupplierServiceImplTest {
    @Autowired
    SupplierServiceImpl supplierService;

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveAllSuppliers() {
        final List<Supplier> AllSuppliers = this.supplierService.retrieveAllSuppliers();
        assertEquals(1, AllSuppliers.size());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void addSupplier() {

        Supplier supplier = new Supplier();
        supplier.setLabel("hamma");
        supplierService.addSupplier(supplier);

        final List<Supplier> AllSuppliers = this.supplierService.retrieveAllSuppliers();
        assertEquals(2,AllSuppliers.size());

        final Supplier addedSupplier = this.supplierService.retrieveSupplier(2L);
        assertEquals("hamma", addedSupplier.getLabel());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void updateSupplier() {
        Supplier supplier = this.supplierService.retrieveSupplier(1L);
        supplier.setLabel("UpdatedLabel");
        supplierService.updateSupplier(supplier);
        Supplier updateSupplier = this.supplierService.retrieveSupplier(1L);
        assertEquals("UpdatedLabel", updateSupplier.getLabel());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void deleteSupplier() {
        final Supplier supplier = this.supplierService.retrieveSupplier(1L);
        supplierService.deleteSupplier(supplier.getIdSupplier());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier() {
        final Supplier supplier = this.supplierService.retrieveSupplier(1L);
        assertEquals("SUPP", supplier.getLabel());
    }
    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier_nullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            final Supplier  supplier  = this.supplierService.retrieveSupplier(100L);
    });
    }
}