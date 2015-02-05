package app.victor.questionario.automatedtests;

import app.victor.questionario.login.LoginActivity;

import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.WebElement;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

public class AutomatedTestsActivity extends ActivityInstrumentationTestCase2 <LoginActivity> {
    
    
    public AutomatedTestsActivity() {
        super(LoginActivity.class);
    }

    private Solo solo;

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testLogin() throws Exception {
        solo.assertCurrentActivity("LoginActivity", LoginActivity.class);
        solo.enterText(0, "teste"); //username
        solo.clickOnButton("Cadastrar usuário"); //clica em cadastro para checar a senha vazia
        solo.waitForText("Senha vazia.");
        solo.enterText(1, "teste"); //senha
        solo.clickOnButton(0); //clica em cadastro para checar a senha vazia
        solo.waitForText("Senha vazia.");

//        solo.enterText(1, "test"); //senha
//        
//        solo.waitForText("Senha vazia.");
        
//        solo.waitForWebElement(By.id("btnadmin"));
//        solo.clickOnButton("ADMIN");
//        solo.clickOnButton(0);
//        solo.waitForText("Creating New Password");
//        solo.enterText(0, "test");
//        solo.enterText(1, "test");
//        solo.clickOnButton("Okay");
//        solo.waitForText("Attendance Login");
//        solo.enterText(0, "Anitha");
//        solo.enterText(1, "test");
//        solo.clickOnButton("Login");
//        solo.waitForWebElement(By.id("btnaddnew"));
//        solo.clickOnButton("Add New Details");
//        solo.waitForText("Enter the Employee Details");
//        solo.enterText(0, "Anitha");
//        solo.enterText(1, "6");
//        solo.enterText(2, "Testing Engineer");
//        solo.clickOnRadioButton(1);
//        solo.clickOnButton("Okay");
//        solo.waitForWebElement(By.id("tvempID"));
        System.out.println(solo.getText(0));

    }

    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}