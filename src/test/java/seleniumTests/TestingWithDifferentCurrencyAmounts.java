package seleniumTests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestingWithDifferentCurrencyAmounts {
    private WebDriver webDriver;
    private static ExtentReports extent; // fill data in report
    private static ExtentSparkReporter spark; // spark report page
    private static ExtentTest test; //

    @BeforeAll
    static void beforeAll() {
        extent = new ExtentReports();
        spark = new ExtentSparkReporter("target/Spark/TestingWithDifferentAmountsOfCurrency.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Testing WithDifferent From Currencies");
        spark.config().setReportName("Extent report for different currency");
        extent.attachReporter(spark);
    }

    @AfterAll
    static void afterAll() {
        extent.flush();//to flush data into the report
    }


    @BeforeEach
    void setUp() {
        webDriver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        webDriver.quit();
    }

    @Test
    void NoAmountGiven() {
        test = extent.createTest("Testing with no amount given ");
        webDriver.get("http://localhost:8080/");
        test.pass("URL is loaded");
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement amountField = webDriver.findElement(By.cssSelector("#amount"));
        WebElement fromCurrencyElement = webDriver.findElement(By.cssSelector("#from"));
        Select fromSelect = new Select(fromCurrencyElement);
        fromSelect.selectByValue("ILS");
        WebElement toCurrencyElement = webDriver.findElement(By.cssSelector("#to"));
        Select toSelect = new Select(toCurrencyElement);
        toSelect.selectByValue("USD");
        test.info("Values are Submited to be converted with no amount");
        //convert btn
        WebElement convertBtn = webDriver.findElement(By.cssSelector("#convertCurrency"));
        convertBtn.click();

        //getting results
        WebElement resultInput = webDriver.findElement(By.cssSelector("#result"));
        WebElement conversionRateInput = webDriver.findElement(By.cssSelector("#conversionRate"));
        try {
            assertEquals("", resultInput.getAttribute("value"));
            test.pass("Did not put random value in result");
            assertEquals("", conversionRateInput.getAttribute("value"));
            test.pass("Did not put random value in conversion rate");
            assertEquals("Enter valid amount", amountField.getAttribute("placeholder"));
            test.pass("Gave user feedback 'Enter valid amount'");
        } catch (AssertionError e) {
            test.fail(e.getMessage());
            throw e;
        }
    }

    @ParameterizedTest(name = "Testing with amount: {0}")
    @MethodSource("givenAmount")
    void TestingWithDifferentAmounts(String amount) {
        webDriver.get("http://localhost:8080/");
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        WebElement amountField = webDriver.findElement(By.cssSelector("#amount"));
        amountField.sendKeys(amount);
        WebElement fromCurrencyElement = webDriver.findElement(By.cssSelector("#from"));
        Select fromSelect = new Select(fromCurrencyElement);
        fromSelect.selectByValue("ILS");
        WebElement toCurrencyElement = webDriver.findElement(By.cssSelector("#to"));
        Select toSelect = new Select(toCurrencyElement);
        toSelect.selectByValue("USD");
        //convert btn
        WebElement convertBtn = webDriver.findElement(By.cssSelector("#convertCurrency"));
        convertBtn.click();

        // Verify the result and conversion rate
        WebElement resultInput = webDriver.findElement(By.cssSelector("#result"));
        WebElement conversionRateInput = webDriver.findElement(By.cssSelector("#conversionRate"));
        // waiting time so api call is done and page is updated with maximum of 10s https://www.selenium.dev/documentation/webdriver/waits/
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(conversionRateInput, "value", "")));
        //assertions
        assertNotEquals("", conversionRateInput.getAttribute("value"));
        String conversionRate = conversionRateInput.getAttribute("value");
        if (conversionRate.contains(","))
            conversionRate = conversionRate.replace(",", ".");
        //assert equals with .05 delta because of how many decimals are required for the  to-currency
        assertEquals(Double.parseDouble(amount) * Double.parseDouble(conversionRate), Double.parseDouble(resultInput.getAttribute("value"))
                , 0.05);
    }

    private static List<String> givenAmount() {
        // You can provide different currencies here
        return Arrays.asList("100", "0.5", "32432", "1000000");
    }

}
