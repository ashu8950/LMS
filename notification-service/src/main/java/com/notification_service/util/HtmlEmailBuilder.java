package com.notification_service.util;

public class HtmlEmailBuilder {

    public static String build(String contentHtml) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
              <style>
                body {
                  font-family: 'Roboto', sans-serif;
                  background-color: #f4f6f9;
                  margin: 0;
                  padding: 0;
                }
                .email-container {
                  max-width: 640px;
                  margin: 40px auto;
                  background-color: #ffffff;
                  border-radius: 10px;
                  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
                  overflow: hidden;
                }
                .header {
                  background: linear-gradient(135deg, #0061f2, #3b82f6);
                  padding: 20px 30px;
                  color: #ffffff;
                  text-align: center;
                }
                .header h2 {
                  margin: 0;
                  font-size: 24px;
                  font-weight: 600;
                }
                .content {
                  padding: 30px;
                  color: #333333;
                  font-size: 16px;
                  line-height: 1.6;
                }
                .content p {
                  margin-bottom: 16px;
                }
                .footer {
                  padding: 20px;
                  font-size: 13px;
                  text-align: center;
                  color: #888888;
                  background-color: #f1f1f1;
                }
                a.button {
                  display: inline-block;
                  margin-top: 20px;
                  padding: 12px 20px;
                  background-color: #3b82f6;
                  color: white;
                  border-radius: 6px;
                  text-decoration: none;
                  font-weight: 500;
                }
                a.button:hover {
                  background-color: #2563eb;
                }
              </style>
            </head>
            <body>
              <div class="email-container">
                <div class="header">
                  <h2>LMS Notification</h2>
                </div>
                <div class="content">
                  %s
                </div>
                <div class="footer">
                  &copy; 2025 LMS. All rights reserved.
                </div>
              </div>
            </body>
            </html>
        """.formatted(contentHtml);
    }
}
