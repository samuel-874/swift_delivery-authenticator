package com.swiftdelivery.authenticator.model.mail;

import lombok.Data;

@Data
public class MailTemplates {

    public String verificationMail(String link){

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Swift Delivery - Activate Your Account</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 20px auto;\n" +
                "            padding: 20px;\n" +
                "            border: 1px solid #ddd;\n" +
                "            border-radius: 8px;\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            color: #007bff;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .activation-link {\n" +
                "            display: inline-block;\n" +
                "            padding: 10px 20px;\n" +
                "            background-color: #007bff;\n" +
                "            color: #fff;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "        }\n" +
                "\n" +
                "        .activation-link:hover {\n" +
                "            background-color: #0056b3;\n" +
                "        }\n" +
                "\n" +
                "        footer {\n" +
                "            margin-top: 20px;\n" +
                "            color: #777;\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Welcome to Swift Delivery!</h1>\n" +
                "        <p>Thank you for choosing us. To get started, please activate your account by clicking the link below:</p>\n" +
                "        <a href=\""+ link +"\" class=\"activation-link\">Activate Your Account</a>\n" +
                "        <p>If the link doesn't work, copy and paste the URL into your browser.</p>\n" +
                "        <p>If you didn't sign up for an account with [Your SaaS Company], please disregard this email.</p>\n" +
                "        <footer>\n" +
                "            <p>If you have any questions or need assistance, contact our support team at [Your Support Email].</p>\n" +
                "            <p> Swift Delivery | suport@swiftdelivery.com  | <a href=\"https://swiftdelivery.vercel.app\">[Your Website]</a></p>\n" +
                "        </footer>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
