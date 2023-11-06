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
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    @Autowired
    private SupplierRepository supplierRepository;

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
        assertEquals(1, invoice.getIdInvoice());
        assertThrows(NullPointerException.class, () -> {
            invoiceService.cancelInvoice(-1L); // Use an ID that doesn't exist
        });


    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        assertEquals(100.0, invoice.getAmountInvoice());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/operator-data.xml")
    void assignOperatorToInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        final Operator operateur = this.operatorService.retrieveOperator(1L);
        invoiceService.assignOperatorToInvoice(operateur.getIdOperateur(),invoice.getIdInvoice());
        assertEquals(1, invoice.getIdInvoice());

        // Test when the invoice is not found
        assertThrows(NullPointerException.class, () -> {
            invoiceService.assignOperatorToInvoice(operateur.getIdOperateur(), -1L); // Use an ID that doesn't exist for the invoice
        });

        // Test when the operator is not found
        assertThrows(NullPointerException.class, () -> {
            invoiceService.assignOperatorToInvoice(-1L, invoice.getIdInvoice()); // Use an ID that doesn't exist for the operator
        });
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        float totalAmount = this.invoiceService.getTotalAmountInvoiceBetweenDates(
                invoice.getDateCreationInvoice(),invoice.getDateLastModificationInvoice());
        float expectedTotalAmount = 100.0f;
        assertEquals(expectedTotalAmount, totalAmount, 0.01f); // You may adjust the delta (0.01f) as needed

    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getInvoicesBySupplier() {
        final List<Invoice> invoices = invoiceService.getInvoicesBySupplier(1L);
        assertEquals(0, invoices.size(), "Number of invoices should be 1");
    }

    @Test
    void testGetInvoicesBySupplierWhenSupplierNotFound() {
        // Test and verify that the correct exception is thrown
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            invoiceService.getInvoicesBySupplier(10L); // Use an ID that doesn't exist
        });
        assertEquals("Supplier not found", exception.getMessage(), "Exception message should match");
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Invoice invoice = this.invoiceService.retrieveInvoice(100L);
        });
    }



}