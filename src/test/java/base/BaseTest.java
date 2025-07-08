package base;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.config;

public class BaseTest {

    @BeforeClass
    public void init() {

        RestAssured.baseURI = ConfigReader.get("base.url");

        createAllureEnvironmentFile();
    }

    public void createAllureEnvironmentFile() {
        try {
            Properties props = new Properties();
            props.setProperty("base.url", ConfigReader.get("base.url"));
            props.setProperty("Environment", ConfigReader.get("env"));
            props.setProperty("build.version", ConfigReader.get("build.version"));

            File resultsDir = new File("target/allure-results");
            if (!resultsDir.exists()) resultsDir.mkdirs();

            File file = new File(resultsDir, "environment.properties");
            FileOutputStream fos = new FileOutputStream(file);
            props.store(fos, "Allure Environment Properties");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
