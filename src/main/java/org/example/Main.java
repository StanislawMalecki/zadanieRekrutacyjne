package org.example;

import dev.failsafe.internal.util.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class Main {

    static final String expectedText = "Login znajdziesz w umowie oraz w aneksach do umowy. Jeśli korzystasz z aplikacji, znajdziesz go w sekcji „Moje dane”.\n" +
            "Sprawdź SMS-y od nas – wysyłamy tak login dla nowo otwartych bankowości. Zawsze też możesz sprawdzić go w placówce bankowej.\n" +
            "Login zawiera 3 pierwsze litery imienia i 3 pierwsze litery nazwiska i 4 cyfry, np. jankow1234";
    static final String login = "abcdef1234";

    public static void main(String[] args) throws InterruptedException
    {
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.get("https://www.ing.pl/indywidualni/odnosniki/aktywacja");
        WebElement gdzieZnajdeLogin = webDriver.findElement(By.id("ctl00_CPH_Content_NPH_GdzieZnajdeLogin"));
        gdzieZnajdeLogin.click();

        WebElement gdzieZnajdeLoginText = webDriver.findElement(By.id("ctl00_CPH_Content_NPH_GdzieLogin"));
        String text = gdzieZnajdeLoginText.getText();
        Assert.isTrue(text.equals(expectedText),
                "Text was different then expectedText!");

        WebElement loginTextField = webDriver.findElement(By.id("ctl00_CPH_Content_NPH_Login"));
        loginTextField.sendKeys(login);

        WebElement typIdentyfikatora = webDriver.findElement(By.id("ctl00_CPH_CarretID_DropDown1_NPH_TypIdentyfikatora"));
        typIdentyfikatora.isEnabled();
        typIdentyfikatora.click();

        //brak id, moge tylko po kolejności
        WebElement paszport = webDriver.findElement(By.xpath("//*[@id='ctl00_CPH_Content_NPH_TypIdentyfikatoraContainer']/div/div/div/ul/li[3]"));
        paszport.click();

        //Id to czasami ctl00_CPH_Content_NPH_NumerPESEL a czasami ctl00_CPH_Content_NPH_NumerPaszportu, więc łapię po czym innym
        WebElement numerIdentyfikatoraTextField = webDriver.findElement(By.id("ctl00_CPH_Content_NPH_NumerPaszportu"));
        numerIdentyfikatoraTextField.sendKeys("Test123");

        WebElement nazwiskoRodoweTextField = webDriver.findElement(By.id("ctl00_CPH_Content_NPH_NazwiskoRodoweMatki"));
        nazwiskoRodoweTextField.sendKeys("Łęcka");

        WebElement dalejButton = webDriver.findElement(By.id("ctl00_CPH_nav_G010_TwojeDane_Button1_a010_Dalej"));
        dalejButton.click();

        FluentWait wait = new FluentWait(webDriver);
        wait.withTimeout(Duration.ofSeconds(6));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value = 'Wstecz']")));

        WebElement wsteczButton = webDriver.findElement(By.xpath("//input[@value = 'Wstecz']"));
        wsteczButton.click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("ctl00_CPH_Content_NPH_Login")));

        Assert.isTrue(webDriver.findElement(By.id("ctl00_CPH_Content_NPH_Login")).getAttribute("value").equals(login),
                "Login was not filled after pressing 'wstecz' button!");

        webDriver.close();
    }

}