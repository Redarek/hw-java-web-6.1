package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }


    @Test
    void replenishmentOfTheFirstCard() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        var authInfo = getAuthInfo();
        var verificationCode = getVerificationCodeFor(authInfo);
        var cardNumber = getSecondCardInfo();

        new LoginPage().validLogin(authInfo).validVerify(verificationCode);
        DashboardPage page = new DashboardPage();

        int currentBalanceFirstCard = page.getCardBalance(0);
        int currentBalanceSecondCard = page.getCardBalance(1);
        int amount = 1000;

        page.transferFirstCardBalance().cardReplenishment(String.valueOf(cardNumber), String.valueOf(amount));

        int expected = currentBalanceFirstCard + amount;
        int actual = page.getCardBalance(0);
        Assertions.assertEquals(expected, actual);

        int expected2 = currentBalanceSecondCard - amount;
        int actual2 = page.getCardBalance(1);
        Assertions.assertEquals(expected2, actual2);
    }

    private void cardReplenishment(Card cardNumber, String s) {
    }


    @Test
    void replenishmentOfTheSecondCard() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        var authInfo = getAuthInfo();
        var verificationCode = getVerificationCodeFor(authInfo);
        var cardNumber = getFirstCardInfo();

        new LoginPage().validLogin(authInfo).validVerify(verificationCode);
        DashboardPage page = new DashboardPage();

        int currentBalanceFirstCard = page.getCardBalance(0);
        int currentBalanceSecondCard = page.getCardBalance(1);
        int amount = 5000;

        new DashboardPage().transferSecondCardBalance().cardReplenishment(String.valueOf(cardNumber), String.valueOf(amount)).upDate();

        int expected = currentBalanceFirstCard - amount;
        int actual = page.getCardBalance(0);
        Assertions.assertEquals(expected, actual);

        int expected2 = currentBalanceSecondCard + amount;
        int actual2 = page.getCardBalance(1);
        Assertions.assertEquals(expected2, actual2);
    }


    @Test
    void replenishmentOverLimit() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");

        var authInfo = getAuthInfo();
        var verificationCode = getVerificationCodeFor(authInfo);
        var cardNumber = getSecondCardInfo();

        new LoginPage().validLogin(authInfo).validVerify(verificationCode);
        DashboardPage page = new DashboardPage();

        int currentBalanceFirstCard = page.getCardBalance(0);
        int currentBalanceSecondCard = page.getCardBalance(1);
        int amount = 44000;

        new DashboardPage().transferFirstCardBalance().cardReplenishment(String.valueOf(cardNumber), String.valueOf(amount)).upDate();

        int expected = currentBalanceSecondCard;
        int actual = page.getCardBalance(1);
        Assertions.assertEquals(expected, actual);

        int expected2 = currentBalanceFirstCard;
        int actual2 = page.getCardBalance(0);
        Assertions.assertEquals(expected2, actual2);
    }


}