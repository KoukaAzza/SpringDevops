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
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tn.esprit.devops_project.entities.ProductCategory.ELECTRONICS;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class ProductServiceImplTest {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    void addProduct() {
        Product product = new Product();
        product.setTitle("ORANGE");
        final Stock stock = this.stockService.retrieveStock(1L);
        product.setStock(stock);
        productService.addProduct(product,stock.getIdStock());
        final List<Product> AllProduct = this.productService.retreiveAllProduct();
        assertEquals(2, AllProduct.size());
        final Product produit = this.productService.retrieveProduct(2L);
        assertEquals("ORANGE", produit.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct() {
        final Product product = this.productService.retrieveProduct(1L);
        assertEquals("Fruit", product.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveAllProduct() {
        final List<Product> AllProduct = this.productService.retreiveAllProduct();
        assertEquals(1, AllProduct.size());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProductByCategory() {
        final List<Product> AllProduct = this.productService.retrieveProductByCategory(ELECTRONICS);
        assertEquals(0, AllProduct.size());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")

    void deleteProduct() {
        final Product product = this.productService.retrieveProduct(1L);
        productService.deleteProduct(product.getIdProduct());


    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    void retreiveProductStock() {
        final Stock stock = this.stockService.retrieveStock(1L);
        final List<Product> AllProduct = this.productService.retreiveProductStock(stock.getIdStock());
        assertEquals(0, AllProduct.size());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Product product = this.productService.retrieveProduct(100L);
        });
    }
}