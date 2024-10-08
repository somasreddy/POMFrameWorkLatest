package pom.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>(); // Thread-safe test storage
    private static ThreadLocal<ExtentTest> nodeThread = new ThreadLocal<>(); // Thread-safe node storage

    public static void initReports() {
        String timestamp = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        String reportPath = System.getProperty("user.dir") + "/reports/Extent_" + timestamp + ".html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle("Automation Test Execution Report");
        reporter.config().setReportName("SwagLabs Test Results");
        
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    public static ExtentTest createTest(String testName, String author) {
        if (extent == null) {
            initReports(); // Ensure reports are initialized
        }
        
        // Create and assign test for the current thread
        ExtentTest test = extent.createTest(testName);
        test.assignAuthor(author);
        testThread.set(test); // Store the test for the current thread
        return test;
    }

    public static ExtentTest createNode(String methodName) {
        ExtentTest currentTest = testThread.get();
        if (currentTest != null) {
            ExtentTest node = currentTest.createNode(methodName); // Create a node under the current test
            nodeThread.set(node); // Store the node for the current thread
            return node;
        }
        return null; // Return null if there is no active test
    }

    public static void publishReport(String method) {
        ExtentTest currentTest = testThread.get();
        if (currentTest != null) {
            currentTest.info("Executing test: " + method);
        }
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush(); // Ensure reports are flushed at the end of all tests
        }
    }
    
    public static ExtentTest getCurrentTest() {
        return testThread.get(); // Retrieve the current test for logging or updates
    }

    public static ExtentTest getCurrentNode() {
        return nodeThread.get(); // Retrieve the current node for logging or updates
    }
}
