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
import tn.esprit.devops_project.entities.*;

import java.sql.Date;
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
class InvoiceServiceImplTest {
    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private OperatorServiceImpl operatorService;
    @Autowired
    private SupplierServiceImpl supplierService;
    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
            final List<Invoice> AllInvoices = this.invoiceService.retrieveAllInvoices();
            assertEquals(1, AllInvoices.size());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        invoiceService.cancelInvoice(invoice.getIdInvoice());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        assertEquals(false, invoice.getArchived());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Invoice invoice = this.invoiceService.retrieveInvoice(100L);
        });
    }
    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveOperator_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Operator operator = this.operatorService.retrieveOperator(100L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier_nullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            final Supplier stock = this.supplierService.retrieveSupplier(100L);
        });

    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/operator-data.xml")
    void assignOperatorToInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        final Operator operateur = this.operatorService.retrieveOperator(1L);
        invoiceService.assignOperatorToInvoice(operateur.getIdOperateur(),invoice.getIdInvoice());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        float totalAmount = this.invoiceService.getTotalAmountInvoiceBetweenDates(
                invoice.getDateCreationInvoice(),invoice.getDateLastModificationInvoice());
        float expectedTotalAmount = 30.0f;
        assertEquals(expectedTotalAmount, totalAmount, 0.01f); // You may adjust the delta (0.01f) as needed

    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getInvoicesBySupplier() {
        //final Supplier supplier = this.supplierService.retrieveSupplier(1L);
       // final List<Invoice> AllInvoice = this.invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());
       // assertEquals(AllInvoice.size(), 0);
    }

}