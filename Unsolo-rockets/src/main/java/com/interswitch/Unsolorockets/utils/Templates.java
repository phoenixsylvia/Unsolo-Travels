package com.interswitch.Unsolorockets.utils;

import java.math.BigDecimal;

public class Templates {


    public static String generateDebitNotificationHtml(BigDecimal transferAmount, BigDecimal walletBalance, BigDecimal transferCharge, String name) {

        return "<html>"
                + "<head>"
                + "<style>"
                + "/* Define your CSS styles here */"
                + "body { background-color: " + "#0080FF" + "; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<h2 style='color: blue;'>Debit Notification</h2>"
                + "<p>Hello " + name + ",</p>"
                + "<p>A debit transaction has occurred on your account (wallet).</p>"
                + "<p>Transfer Amount: ₦" + transferAmount + "</p>"
                + "<p>Wallet Balance: ₦" + walletBalance + "</p>"
                + "<p>Transfer Charge: ₦" + transferCharge + "</p>"
                + "<p>Thank you for using our service.</p>"
                + "</body>"
                + "</html>";
    }

    public static String generateCreditNotificationHtml(BigDecimal creditAmount, BigDecimal walletBalance, String name, String sender) {

        return "<html>"
                + "<head>"
                + "<style>"
                + "/* Define your CSS styles here */"
                + "body { background-color: " + "#0080FF" + "; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<h2 style='color: green;'>Credit Notification</h2>"
                + "<p>Hello " + name + ",</p>"
                + "<p>A credit transaction has occurred on your account (wallet).</p>"
                + "<p>Sender: " + sender + "</p>"
                + "<p>Credit Amount: ₦" + creditAmount + "</p>"
                + "<p>Wallet Balance: ₦" + walletBalance + "</p>"
                + "<p>Thank you for using our service.</p>"
                + "</body>"
                + "</html>";
    }
}
