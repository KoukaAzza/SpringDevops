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
import tn.esprit.devops_project.entities.Stock;
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
class StockServiceImplTest {

    @Autowired
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addStock() {
        Stock stock = new Stock();
        stock.setTitle("titre1");
        stockService.addStock(stock);

        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        assertEquals(2, allStocks.size());

        final Stock stock1 = this.stockService.retrieveStock(2L);
        assertEquals("titre1", stock1.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock() {
        final Stock stock = this.stockService.retrieveStock(1L);
        assertEquals("stock 1", stock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllStock() {
        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        assertEquals(1 ,allStocks.size() );

    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Stock stock = this.stockService.retrieveStock(100L);
        });
    }
}
